const express = require('express');
const http = require('http');
const path = require('path');
const socketIO = require('socket.io');
const app = express();
const server = http.createServer(app);
const fs = require('fs');
const dlHost = `http://127.0.0.1:8099/static`;

//redis
//https://docs.redis.com/latest/rs/references/client_references/client_nodejs/

const io = socketIO(server,{
    cors: {
        origin: '*'
    }
});

app.use('/static', express.static('./static'))

function writeBinary(filename, buf, callback) {
  const wstream = fs.createWriteStream(filename);
  wstream.write(buf);
  wstream.end();
  wstream.on('finish', function() {
    callback();
  });
  wstream.on('error', function(err) {
    console.log(err);
  });
}

const ID = ()=>Date.now().toString(36)+Math.random().toString(36).substr(2,8);

const users = {};

const rooms = {};
const roomsUsers = {};
const roomBuffer = {};
const actionBuffer = {};
const layoutBuffer = {};
const sockets = {};
const MAXTIME = 60*1000*5;
const mirror = {};

const addr = path.normalize(__dirname + '/static');

function message(socket,msg="",type="info"){
  socket.emit("message",{
    message:msg,
    type
  });
}

function goto(socket,path="/",params={}){
  socket.emit("redirect",{
    path,params
  });
}

function reqlist(socket,data=[]){
  socket.emit("asset",{
    event:"list",
    state:200,
    data
  });
}

function createRoom(socket,params){
  const rid = ID();
  const uid = ID();
  const lid = ID();
  const {roomName,description} = params;
  const room = {
    rid,
    name:roomName,
    description,
    state:true,
    limit:10,
    stats:1
  };
  rooms[rid] = room;
  roomBuffer[rid] = {};
  roomBuffer[rid][lid] = {};
  mirror[rid] = {};
  mirror[rid][lid] = {};
  actionBuffer[rid] = {};
  actionBuffer[rid][lid] = {};
  const user = {
    uid,lid,
    state:"管理员",
    socket
  }
  roomsUsers[rid] = {};
  roomsUsers[rid][uid] = user;  
  layoutBuffer[rid] = {};
  layoutBuffer[rid][lid] = {
    lid,name:"Sheet"
  };
  const data = {
    room,
    user:{
      uid:user.uid,
      lid:user.lid,
      state:user.state
    },
    layout:Object.values(layoutBuffer[rid])
  };
  goto(socket,`/panel`,data);
}

function enterRoom(socket,params){
  const {rid} = params;
  const uid = ID();
  const room = rooms[rid];
  const user = {
    uid,lid:Object.keys(layoutBuffer[rid])[0],
    state:"普通成员",socket
  }
  room.stats++;
  roomsUsers[rid][uid] = user;
  const data = {
    room,
    user:{
      uid:user.uid,
      lid:user.lid,
      state:user.state
    },
    layout:Object.values(layoutBuffer[rid])
  };
  goto(socket,`/panel`,data);
}

function exitRoom(socket,params){
  const {uid,rid} = params;
  const room = rooms[rid];
  const user = roomsUsers[rid][uid];
  if(user){
    room.stats--;
    delete roomsUsers[rid][uid];
  }
  if(Object.keys(roomsUsers[rid]).length===0||room.stats==0){
    delete rooms[rid];
    delete roomsUsers[rid];
    delete roomBuffer[rid];
    delete actionBuffer[rid];
    delete layoutBuffer[rid];
    delete mirror[rid];
  }
  goto(socket,"/",{});
}

function exitUser(socket){
  let rid,uid;
  for(let key in roomsUsers){
    let has = Object.values(roomsUsers[key]).find(v=>v.socket===socket);
    if(has){
      rid = key;
      uid = has.uid;
      exitRoom(socket,{uid,rid});
    }
  }
}

function broadCastList(list){
  for(let key in sockets){
    reqlist(sockets[key],list);
  }
}

function broadCastRoomUsers(rid,data){
  const {lid} = data;
  if(!lid)return;
  Object.values(roomsUsers[rid]).forEach(user=>{
    if(user.lid===lid){
      user.socket.emit("stream",data);
    }
  })
}

function broadCastAsset(rid,dlid){
  const room = rooms[rid];
  let layout = Object.values(layoutBuffer[rid]);
  for(let uid in roomsUsers[rid]){
    let user = roomsUsers[rid][uid];
    const {state,lid} = user;
    if(dlid === lid){
      user.socket.emit("asset",{
        event:"sync",
        params:{
          room,user:{
            uid,state,lid:layout[0].lid,
          },
          layout
        }
      });
      user.lid = layout[0].lid;
      refresh(user.socket,rid,layout[0].lid,uid);
    }else{
      user.socket.emit("asset",{
        event:"sync",
        params:{
          room,user:{
            uid,state,lid
          },
          layout
        }
      });
    }
  }
}

function record(rid,lid,uid,frame,syn){
  if(!roomBuffer[rid])roomBuffer[rid]={};
  if(!roomBuffer[rid][lid])return {};//roomBuffer[rid][lid]={};
  if(!actionBuffer[rid])actionBuffer[rid]={};if(!actionBuffer[rid][lid])actionBuffer[rid][lid]={};
  const buf = roomBuffer[rid][lid];
  const act = actionBuffer[rid][lid];
  const {type,pixel:{gid,date}} = frame;
  if(act[gid]){
    if(act[gid].type=="删除"){
      return {
        rid,lid,uid,event:"edit",
        frame:{type:"删除",pixel:{
          gid
        }}
      };
    }
    if(act[gid].date<date){
      buf[gid] = frame.pixel;
      act[gid] = {
        type,date
      };
    }
  }else{
    buf[gid] = frame.pixel;
    act[gid] = {
      type,date
    }
  }
  if(type=="删除"){
    delete buf[gid];
    if(syn)screenshot(rid,lid);
    return {
      rid,lid,uid,event:"edit",
      frame:{type,pixel:{gid}}
    };
  }
  if(syn)screenshot(rid,lid);
  return {
    rid,lid,uid,event:"edit",
    frame:{type,pixel:buf[gid]}
  }
}

function synRoom(rid,uid,lid,dlid){
  const US = roomsUsers[rid][uid];
  if(dlid==null){
    US.lid = lid;
    broadCastAsset(rid);
    refresh(US.socket,rid,lid,uid);
  }else{
    broadCastAsset(rid,dlid);
    refresh(US.socket,rid,lid,uid);
  }
}

function layoutRefesh(socket,rid,uid,lid,frame){
  let {name,type} = frame;
  if(lid){
    if(type==="删除"){
      delete roomBuffer[rid][lid];
      delete layoutBuffer[rid][lid];
      let dlid = lid;
      if(Object.keys(layoutBuffer[rid]).length===0){
        lid = ID();
        roomBuffer[rid][lid] = {};
        mirror[rid][lid] = {};
        layoutBuffer[rid][lid] = {
          lid,name:"Sheet"
        }
      }
      lid = Object.keys(layoutBuffer[rid])[0];
      synRoom(rid,uid,lid,dlid);
      return;
    }else{
      let user = roomsUsers[rid][uid];
      user.lid = lid;
      // console.log(lid);
      refresh(socket,rid,lid,uid);
      return;
    }
  }else{
    lid = ID();
    roomBuffer[rid][lid] = {};
    mirror[rid][lid] = {};
    layoutBuffer[rid][lid] = {
      lid,name
    }
  }
  synRoom(rid,uid,lid);
}

function refresh(socket,rid,lid,uid){
  socket.emit("stream",{
    event:"refresh",rid,uid,lid,
    frame:roomBuffer[rid][lid]||{}
  });
}

function screenshot(rid,lid){
  let scshot = JSON.stringify({
    pixel:roomBuffer[rid][lid],
    action:actionBuffer[rid][lid]
  });
  if(!mirror[rid])mirror[rid] = {};
  if(!mirror[rid][lid])mirror[rid][lid] = {
    history:[],
    back:[]
  };
  if(!mirror[rid][lid].history)mirror[rid][lid].history=[];
  if(!mirror[rid][lid].back)mirror[rid][lid].back=[];
  if(mirror[rid][lid].history.length>0){
    if(mirror[rid][lid].history[mirror[rid][lid].history.length-1]===scshot)return;
  }
  mirror[rid][lid].history.push(scshot);
  if(mirror[rid][lid].history.length>10)mirror[rid][lid].history.shift();
  mirror[rid][lid].back.length = 0;
}

function undoapply(rid,lid){
  if(!mirror[rid])mirror[rid] = {};
  if(!mirror[rid][lid])mirror[rid][lid] = {
    history:[],
    back:[]
  };
  if(!mirror[rid][lid].history)mirror[rid][lid].history=[];
  if(!mirror[rid][lid].back)mirror[rid][lid].back=[];
  let scshot = mirror[rid][lid].history.pop()||"{}";
  mirror[rid][lid].back.push(scshot);
  let {pixel,action} = JSON.parse(scshot);
  roomBuffer[rid][lid] = pixel;
  actionBuffer[rid][lid] = action;
  broadCastRoomUsers(rid,{
    event:"refresh",rid,lid,
    frame:roomBuffer[rid][lid]||{}
  });
}

function redoapply(rid,lid){
  if(!mirror[rid])mirror[rid] = {};
  if(!mirror[rid][lid])mirror[rid][lid] = {
    history:[],
    back:[]
  };
  if(!mirror[rid][lid].history)mirror[rid][lid].history=[];
  if(!mirror[rid][lid].back)mirror[rid][lid].back=[];
  let scshot = mirror[rid][lid].back.pop()||"{}";
  mirror[rid][lid].history.push(scshot);
  let {pixel,action} = JSON.parse(scshot);
  roomBuffer[rid][lid] = pixel;
  actionBuffer[rid][lid] = action;
  broadCastRoomUsers(rid,{
    event:"refresh",rid,lid,
    frame:roomBuffer[rid][lid]||{}
  });
}

function download(socket,name,url="#"){
  socket.emit("file",{
    name,url
  });
}

function screenSave(socket,rid,lid){
  const scshot = JSON.stringify({
    pixel:roomBuffer[rid][lid],
    action:actionBuffer[rid][lid]
  });
  let {name} = layoutBuffer[rid][lid]||"Sheet";
  let fname = ID();
  let url = `${dlHost}/${fname}.fsl`;
  fs.writeFile(`${addr}/${fname}.fsl`,scshot,{ flag: 'w' }, function (error){
    if(error){
        console.log(error)
    }else{
      download(socket,`${name||"Sheet"}.fsl`,url);
      setTimeout(() => {
        fs.unlink(`${addr}/${fname}.fsl`,function(error){
          
        });
      }, MAXTIME);
      // message(socket,`/static/${name||"Sheet"}.fsl`,"info");
    }
  });
}

function makeAct(socket,event,rid,lid,uid,frame){
  // console.log(event,rid,lid,uid);
  switch(event){
    case "save":
      screenshot(rid,lid);
      message(socket,"保存成功","success");
      break;
    case "export":
      screenSave(socket,rid,lid);
      break;
    case "import":
        break;
    case "undo":
      undoapply(rid,lid);
      message(socket,"撤回成功","success");
      break;
    case "redo":
      redoapply(rid,lid);
      message(socket,"重做成功","success");
      break;
  }
}

io.on('connection',(socket) => {
    const id = ID();
    console.log(`${id}上线`);
    sockets[id] = socket;
    goto(socket,"/",{});
    // console.log(Object.keys(sockets));
    socket.on('query',data=>{
      try{
        const {event,params} = data;
        switch(event){
          case "list":reqlist(socket,Object.values(rooms));break;
          case "add":createRoom(socket,params);broadCastList(Object.values(rooms));break;
          case "select":enterRoom(socket,params);broadCastList(Object.values(rooms));break;
          case "exit":exitRoom(socket,params);broadCastList(Object.values(rooms));break;
        }
      }catch(err){console.log(err)}
    });
    socket.on('stream',data=>{
      try{
        const {event,rid,uid,lid,frame,syn} = data;
        switch(event){
          // case "increment"://broadCastRoomUsers(rid,data);break;
          // case "all":broadCastRoomUsers(rid,data);break;
          case "edit":broadCastRoomUsers(rid,record(rid,lid,uid,frame,syn));break;
          case "refresh":layoutRefesh(socket,rid,uid,lid,frame);break;
          default:
            makeAct(socket,event,rid,lid,uid,frame);
            break;
        }
      }catch(err){
        console.log(err);
      }
    });
    socket.on('message',(data) => {
        console.log(data);
        message(socket,data);
    });
    socket.on("reconnect",()=>{
      console.log(`${id}重新连接`);
      try{
        sockets[id] = socket;
        goto(socket,"/",{});
        reqlist(socket,Object.values(rooms));
      }catch(err){}
    });
    socket.on('disconnect', () => {
      console.log(`${id}下线`);
      try{
        delete sockets[id];
        exitUser(socket);
        broadCastList(Object.values(rooms));
      }catch(err){
        console.log(err);
      }
    });
});

server.listen(8099,() => {
    console.log("server is up and running on port 8102");
});