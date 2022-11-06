package fastclip.server;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import fastclip.Service.QueryService;
import fastclip.domain.Frame;
import fastclip.domain.House;
import fastclip.domain.Query;
import fastclip.domain.Stream;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/*
stream           | {uid,rid,frame} | increment 事件
(String event,String uid,String rid,frame:{String gid,String type, style:{int left,int top,int radius}})

* */

@Component
@Slf4j
public class MessageEventHandler {

    @Autowired
    private SocketIOServer socketIoServer;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QueryService queryService;

    public static Set<SocketIOClient> socketIOClientMap = new HashSet<>();


    public static volatile boolean flag=true;


    @OnEvent(value = "query")
    public void onQuery(SocketIOClient client, AckRequest request, Query data) {
          log.info("query");
          if(data.event.equals("add"))
        { log.info("add");
            client.sendEvent("redirect", queryService.addRoom(data.params.roomName,data.params.description));
        }

        if(data.event.equals("select")){
            log.info("select");
            client.sendEvent("redirect",queryService.select(data.params.rid));
        }
        if(data.event.equals("exit")){
            log.info("exit");
            client.sendEvent("redirect",queryService.exit(data.params.rid,data.params.uid));
        }
          if(data.event.equals("list")){
            log.info("list");
            client.sendEvent("asset",queryService.list());
          }else{
              int i=0;
              for(SocketIOClient cur:socketIOClientMap){
                  cur.sendEvent("asset",queryService.list());
                  log.info(i++ +" "+queryService.list().toString());
              }
          }

        //广播消息
        //sendBroadcast();
    }

    @OnEvent(value="stream")
    public void onStream(SocketIOClient client, AckRequest request, Stream data) throws InterruptedException {
       log.info("stream");
       if(data.event.equals("increment")){
           log.info("increment");
          while(!flag){
              Thread.sleep(10);
          }
          flag=false;
          List<String> nameList=redisService.get(data.rid,List.class);
          for(String cur:nameList){
              if(!cur.equals(data.uid)){}
             // socketIOClientMap.get(cur).sendEvent("stream","123");
          }
          flag=true;
       }
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


         socketIOClientMap.add(client);
        log.info(String.valueOf(socketIOClientMap.size()));
        String message="d";
        List<House> r = redisService.get("list", List.class);
        if (r == null) {
            redisService.set("list", new ArrayList());

        }
        message = String.valueOf(JSON.parseArray(String.valueOf(r)));
        //client.sendEvent("message", r);
        //sendBroadcast();
        log.info("客户端:" + client.getSessionId() + "已连接,mac=" + "mac");
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
        for (SocketIOClient client : socketIOClientMap) {
            if (client.isChannelOpen()) {
                client.sendEvent("redirect", "当前时间", System.currentTimeMillis());
            }
        }

    }
}
