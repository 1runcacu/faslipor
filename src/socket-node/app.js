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
    uid,
    socket
  }];
  const user = {
    uid,
    state:"管理员"
  }
  const data = {
    room,
    user
  };
  rooms[rid] = room;
  users[uid] = user;
  goto(socket,"/panel",data);
}

function enterRoom(socket,params){
  const {rid} = params;
  const uid = ID();
  const room = rooms[rid];
  const user = {
    uid,
    state:"普通成员"
  }
  room.stats++;
  roomsUsers[rid].push({
    uid,
    socket
  });
  const data = {
    room,
    user
  };
  goto(socket,"/panel",data);
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
      break;
    }
    exitRoom(socket,{uid,rid});
  }
}

function broadCastList(list){
  for(let key in sockets){
    reqlist(sockets[key],list);
  }
}

function broadCastRoomUsers(rid,data){
  console.log(roomsUsers[rid]);
  roomsUsers[rid].forEach(user=>{
    user.socket.emit("stream",data)
  })
}

io.on('connection',(socket) => {
    const id = ID();
    console.log(`${id}上线`);
    sockets[id] = socket;
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
        const {event,rid,uid,frame} = data;
        switch(event){
          case "increment":broadCastRoomUsers(rid,data);break;
        }
      }catch(err){}
    });
    socket.on('message',(data) => {
        console.log(`收到客户端的消息：${data}`);
        try{
          socket.emit('message', {
            msg: `你好${data}`,
            code: 200
          });  
        }catch(err){}
    });
    socket.on("reconnect",()=>{
      console.log(`${id}重新连接`);
      try{
        sockets[id] = socket;
        setTimeout(() => {
          reqlist(socket,Object.values(rooms));
        }, 500);
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


