package fastclip.server;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import fastclip.Service.QueryService;
import fastclip.domain.Query;
import fastclip.domain.Result;
import fastclip.domain.Room;
import fastclip.domain.User;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/*
功能	请求方法	链接	参数	返回参数
获取所有房间信息	websocket	/login/list	无	json数组（房间信息）
搜索（前端）
创建房间	websocket
加入房间	websocket	/login/	String	json数组（最近的全量帧及之后的增量帧及房间信息，个人信息）
前端监听事件	数据格式	参数定义
asset
message	{msg,type}	type:success\
stream
reset
后端监听事件	数据格式	参数定义
query	{event,params}	3种event String类型(list,select,add)对应params分别是(null),(String rid),(String roomName,String brief)
stream	{uid,rid,frame}	(String uid,String rid,String frame)
reset	{uid,rid,frame}	(String uid,String rid,String frame)
* */

@Component
@Slf4j
public class MessageEventHandler {

    @Autowired
    private SocketIOServer socketIoServer;

    @Autowired
    private RedisService redisService;

    private QueryService queryService;

    public static ConcurrentMap<String, SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();
    @OnEvent(value = "query")
    public void onQuery(SocketIOClient client, AckRequest request, Query data) {

        if(data.event.equals("add"))
        {client.sendEvent("asset", queryService.addRoom(data.data.roomName,data.data.brief));}
        //广播消息
        //sendBroadcast();
    }

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

        String message="d";
        List<Room> r = redisService.get("list", List.class);
        if (r == null) {
            redisService.set("list", new ArrayList());

        }
        message = String.valueOf(JSON.parseArray(String.valueOf(r)));
        client.sendEvent("message", r);
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
