const express = require('express');
const http = require('http');
const socketIO = require('socket.io');
const app = express();
const server = http.createServer(app);
 
const io = socketIO(server,{
    cors: {
        origin: '*'
    }
});
 
io.on('connection',(socket) => {
    console.log('user connected');
    socket.on('message',(data) => {
        console.log(`收到客户端的消息：${data}`);
        socket.emit('message', {
          msg: `你好${data}`,
          code: 200
        });  
    });
});

app.get('/',(request, response) => {
  // io.emit('message','服务端向客户端推送消息...');
  response.writeHead(200, {"Content-type": "text/plain"});
  response.json(2333);
  response.end();
});

app.get('/test',(request, response) => {
  response.json("test");
});

server.listen(3000,() => {
    console.log("server is up and running on port 3000");
});


