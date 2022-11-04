package fastclip.server;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import fastclip.domain.HelloUid;
import fastclip.domain.Room;
import fastclip.domain.User;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class MessageEventHandler {

    @Autowired
    private SocketIOServer socketIoServer;

    @Autowired
    private RedisService redisService;

    public static ConcurrentMap<String, SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

    /**
     * 客户端连接的时候触发
     *
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("start");
        if(client==null){
            log.info("client null");
        }
       String mac = client.getHandshakeData().getSingleUrlParam("roomId");
        log.info(client.getHandshakeData().toString());
        log.info(String.valueOf(client.getHandshakeData().getAddress()));
        Set<String> set=client.getHandshakeData().getUrlParams().keySet();
        for (String key : set) {
           log.info(key);
           log.info(client.getHandshakeData().getSingleUrlParam(key));
        }
        //String mac="transport";
        if(mac==null){
            log.info("mac null");
        }
        //存储SocketIOClient，用于发送消息
        log.info("123");
        socketIOClientMap.put(mac, client);
        //回发消息
        log.info(String.valueOf(123));
        HelloUid myData=new HelloUid();
        myData.msg="hello";
        String message="d";
        List<Room> r = redisService.get("list", List.class);
        if (r == null) {
            redisService.set("list", new ArrayList());

        }
        message = String.valueOf(JSON.parseArray(String.valueOf(r)));
        client.sendEvent("message", message);
        //sendBroadcast();
        log.info("客户端:" + client.getSessionId() + "已连接,mac=" + mac);
    }

    /**
     * 客户端关闭连接时触发
     *
     * @param client
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("客户端:" + client.getSessionId() + "断开连接");
    }

    /**
     * 客户端事件
     *
     * @param client  　客户端信息
     * @param request 请求信息
     * @param data    　客户端发送数据
     */
    @OnEvent(value = "message")
    public void onEvent(SocketIOClient client, AckRequest request, String data) {
        log.info("发来消息：" + data);
        Room newRoom = new Room();
        Date time0 = new Date();
        newRoom.id = ((Long) time0.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        newRoom.name = data;
        //新建房间并加入
        if(newRoom.name!=null&&newRoom.id!=null){
            newRoom.msg=200;
        }
        redisService.set(newRoom.id, newRoom);
        List<Room> r = redisService.get("list", List.class);
        if (r == null) {
            r=new ArrayList();
        }
        r.add(newRoom);
        redisService.set("list", r);
        //新建用户并记录
        User newUser = new User();
        Date time1 = new Date();
        newUser.id = ((Long) time1.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        newUser.state = "创建者";
        if(newUser.id!=null&&newUser.state.equals("创建者")){
            newUser.msg=200;
        }
        redisService.set(newUser.id,newUser);
        //回发消息
        HelloUid myData=new HelloUid();
        myData.msg="hello"+data;
        client.sendEvent("message", JSON.toJSONString(newRoom)+JSON.toJSONString(newUser));
        //广播消息
       //sendBroadcast();
    }

    /**
     * 广播消息
     */
    public void sendBroadcast() {
        for (SocketIOClient client : socketIOClientMap.values()) {
            if (client.isChannelOpen()) {
                client.sendEvent("Broadcast", "当前时间", System.currentTimeMillis());
            }
        }

    }
}
