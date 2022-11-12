const express = require('express');
const http = require('http');
const socketIO = require('socket.io');
const app = express();
const server = http.createServer(app);
 
//redis
//https://docs.redis.com/latest/rs/references/client_references/client_nodejs/

const io = socketIO(server,{
    cors: {
        origin: '*'
    }
});

const ID = ()=>Date.now().toString(36)+Math.random().toString(36).substr(2,8);

const users = {};

const rooms = {};
const roomsUsers = {};
const roomBuffer = {};
const actionBuffer = {};
const layoutBuffer = {};

const sockets = {};

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
  roomsUsers[rid] = [{
    uid,state:"管理员",lid,
    socket
  }];
  const user = {
    uid,lid,
    state:"管理员"
  }
  const data = {
    room,
    user,
    layout:[{lid,name:"Sheet"}]
  };
  rooms[rid] = room;
  roomBuffer[rid] = {};
  roomBuffer[rid][lid] = {};
  actionBuffer[rid] = {};
  actionBuffer[rid][lid] = {};
  layoutBuffer[rid] = [];
  layoutBuffer[rid].push({
    lid,name:"Sheet"
  });
  users[uid] = user;
  goto(socket,`/panel`,data);
}

function enterRoom(socket,params){
  const {rid} = params;
  const uid = ID();
  const room = rooms[rid];
  const user = {
    uid,lid:layoutBuffer[rid][0].lid,
    state:"普通成员"
  }
  room.stats++;
  console.log(layoutBuffer[rid])
  roomsUsers[rid].push({
    uid,state:"普通成员",lid:user.lid,
    socket
  });
  const data = {
    room,
    user,
    layout:layoutBuffer[rid]
  };
  goto(socket,`/panel`,data);
}

function exitRoom(socket,params){
  const {uid,rid} = params;
  const room = rooms[rid];
  room.stats--;
  const index = roomsUsers[rid].findIndex(v=>v.uid===uid);
  roomsUsers[rid].splice(index,1);
  delete users[uid];
  if(roomsUsers[rid].length===0){
    delete rooms[rid];
    delete roomsUsers[rid];
    delete roomBuffer[rid];
    delete actionBuffer[rid];
  }
  goto(socket,"/",{});
}

function exitUser(socket){
  let rid,uid;
  for(let key in roomsUsers){
    let has = roomsUsers[key].find(v=>v.socket===socket);
    if(has){
      rid = key;
      uid = has.uid;
      exitRoom(socket,{uid,rid});
      break;
    }
  }
}

function broadCastList(list){
  for(let key in sockets){
    reqlist(sockets[key],list);
  }
}

function broadCastRoomUsers(rid,data){
  let {lid} = data;
  roomsUsers[rid].forEach(user=>{
    console.log("R:",user.lid,lid);
    if(user.lid===lid){
      user.socket.emit("stream",data);
    }
  })
}

function broadCastAsset(event="sync",rid,params){
  roomsUsers[rid].forEach(user=>{
    user.socket.emit("asset",{
      event,
      params
    });
  })
}


function record(rid,lid,uid,frame){
  if(!roomBuffer[rid])roomBuffer[rid]={};if(!roomBuffer[rid][lid])roomBuffer[rid][lid]={};
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
      }
    }
    if(act[gid].date<date){
      buf[gid] = frame.pixel;
      act[gid] = {
        type,date
      }
    }
  }else{
    buf[gid] = frame.pixel;
    act[gid] = {
      type,date
    }
  }
  if(type=="删除"){
    delete buf[gid];
    return {
      rid,lid,uid,event:"edit",
      frame:{type,pixel:{gid}}
    };
  }
  return {
    rid,lid,uid,event:"edit",
    frame:{type,pixel:buf[gid]}
  }
}

function synRoom(rid,uid,lid){
  const room = rooms[rid];
  roomsUsers[rid].forEach(v=>{
    console.log(v.uid);
  });
  const US = roomsUsers[rid].find(v=>v.uid===uid);
  US.lid = lid;
  const data = {
    room,
    user:{uid:US.uid,state:US.state,lid},
    layout:layoutBuffer[rid]
  };
  broadCastAsset("sync",rid,data);
  refresh(US.socket,rid,lid,uid);
}

function layoutRefesh(socket,rid,uid,lid,frame){
  let {name,type} = frame;
  console.log("L:",rid,uid,lid,frame);
  if(lid){
    if(type==="删除"){
      delete roomBuffer[rid][lid];
      layoutBuffer[rid] = layoutBuffer[rid].filter(v=>v.lid===lid);
      synRoom(rid,uid,lid);
      return;
    }else{
      let user = roomsUsers[rid].find(v=>v.uid===uid);
      user.lid = lid;
      refresh(socket,rid,lid,uid);
      return;
    }
  }else{
    lid = ID();
    roomBuffer[rid][lid] = {};
    layoutBuffer[rid].push({
      lid,name
    });
  }
  if(layoutBuffer[rid].length===0){
    lid = ID();
    roomBuffer[rid][lid] = {};
    layoutBuffer[rid].push({
      lid,name:"Sheet"
    });
    synRoom(rid,uid,lid);
    return;
  }
  synRoom(rid,uid,lid);
  // broadCastRoomUsers(rid,{
  //   event,rid,uid,lid,frame:roomBuffer[rid][lid]
  // });
}

function refresh(socket,rid,lid,uid){
  socket.emit("stream",{
    event:"refresh",rid,uid,lid,
    frame:roomBuffer[rid][lid]
  });
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
      }catch(err){}
    });
    socket.on('stream',data=>{
      try{
        const {event,rid,uid,lid,frame} = data;
        switch(event){
          case "increment"://broadCastRoomUsers(rid,data);break;
          case "all":broadCastRoomUsers(rid,data);break;
          case "edit":broadCastRoomUsers(rid,record(rid,lid,uid,frame));break;
          case "refresh":layoutRefesh(socket,rid,uid,lid,frame);break;
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
      }catch(err){}
    })
});

server.listen(8099,() => {
    console.log("server is up and running on port 8102");
});

// actionBuffer = {
//   "rid1":{
//     "lid1":{
//       gid1:[
//         {
//           event:"添加",
//           date:0,
//           frame:{}//xxxxxxx
//         },
//         {
//           event:"编辑",
//           date:2,
//           frame:{}//xxxxxxx
//         },
//         {
//           event:"编辑",
//           date:4,
//           frame:{}//xxxxxxx
//         },
//         {
//           event:"删除",
//           date:5,
//           frame:{}//xxxxxxx
//         }
//       ]
//     },
//     "lid2":{
//       gid1:[
//         {
//           event:"添加",
//           date:0,
//           frame:{}//xxxxxxx
//         },{
//           event:"编辑",
//           date:4,
//           frame:{}//xxxxxxx
//         },
//         {
//           event:"删除",
//           date:5,
//           frame:{}//xxxxxxx
//         }
//       ]
//     }
//   }
// }