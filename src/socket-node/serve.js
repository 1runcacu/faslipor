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

const sockets = {};

const rooms = {
    // rid:{
    //     rid,
    //     name:roomName,
    //     description,
    //     state:true,
    //     limit:10,
    //     stats:1,
    //     members:{
    //         uid:state
    //     }
    // }
};

const users = {
    // uid:{
    //     uid,
    //     rid,
    //     socket
    // }
};

const DATA = new Proxy({},{
    get(target,key){
        switch(key){
            case "rooms":
                return getRooms();
        }
    },
    set(target,key,value){
        switch(key){
            case "createRoom":
                createRoom(value);
                break;
            case "deleteRoom":
                deleteRoom(value);
                break;
        }
        return true;
    }
});

function getRooms(){
    return Object.values(rooms).map(v=>{
        const {rid,name,description,state,limit,stats} = v;
        return {rid,name,description,state,limit,stats};
    })
}

function createRoom(params){
    const {uid,rid} = params;
    const socket = sockets[uid];
    if(socket){
        users[uid] = {
            uid,
            rid,
            socket
        };
        if(rid){
            if(room[rid]){
                if(rooms[rid].members[uid]){
                    return;
                }else{
                    rooms[rid].stats++;
                    rooms[rid].members[uid] = "普通成员";
                }
            }else{
                const {room} = params;
                rooms[rid] = room;
                rooms.stats = 1;
                rooms[rid].members = {
                    uid:"管理员"
                };
            }
        }
    }
}

function deleteRoom(uid){
    const {rid} = users;
    if(rooms[rid]&&rooms[rid].members[uid]){
        delete rooms[rid].members[uid];
        rooms[rid].stats--;
    }
    delete users[rid];
}


function reqlist(socket,data=[]){
    socket.emit("asset",{
      event:"list",
      state:200,
      data
    });
}

function broadCastList(list){
    for(let key in sockets){
      reqlist(sockets[key],list);
    }
}

io.on('connection',(socket) => {
    const id = ID();
    console.log(`${id}上线`);
    sockets[id] = socket;
    console.log(Object.keys(sockets));
    socket.on('query',data=>{
      try{
        const {event,params} = data;
        switch(event){
            case "list":reqlist(socket,DATA.rooms);break;
            case "add":createRoom(socket,params);broadCastList(DATA.rooms);break;
            case "select":enterRoom(socket,params);broadCastList(DATA.rooms);break;
            case "exit":exitRoom(socket,params);broadCastList(DATA.rooms);break;
        }

      }catch(err){}
    });
    socket.on('stream',data=>{
      try{

      }catch(err){}
    });
    socket.on('message',(data) => {
        console.log(data);
        try{
 
        }catch(err){}
    });
    socket.on("reconnect",()=>{
      console.log(`${id}重新连接`);
      try{
        sockets[id] = socket;
      }catch(err){}
    });
    socket.on('disconnect', () => {
      console.log(`${id}下线`);
      try{
        delete sockets[id];
      }catch(err){}
    })
});

server.listen(8099,() => {
    console.log("server is up and running on port 8102");
});


