package fastclip.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import fastclip.Service.QueryService;
import fastclip.domain.*;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Queue;

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

    public static Set<SocketIOClient> socketIOClientSet = new HashSet<>();

    public static Map<String,SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

    public static Map<SocketIOClient,String> socketIOClientMap1=new ConcurrentHashMap<>();

    public static  LinkedList<Istream> myQueue=new LinkedList<>();

    public static java.util.Queue<JSONObject> myQueueAll=new LinkedList<>();

    public static Long pre= Long.valueOf(0);

    public static volatile boolean flag=true;

    public static volatile boolean flaga=true;


    @OnEvent(value = "query")
    public void onQuery(SocketIOClient client, AckRequest request, Query data) {
          log.info("query");
          if(data.event.equals("add"))
        { log.info("add");
            client.sendEvent("redirect", queryService.addRoom(client,data.params.roomName,data.params.description));
        }
        if(data.event.equals("select")){
            log.info("select");
            log.info(JSON.toJSONString(data));
            client.sendEvent("redirect",queryService.select(client,data.params.rid));
        }
        if(data.event.equals("exit")){
            log.info("exit");
            client.sendEvent("redirect",queryService.exit(client,data.params.rid,data.params.uid));
        }
          if(data.event.equals("list")){
            log.info("list");
            client.sendEvent("asset",queryService.list());
          }else{
              int i=0;
              for(SocketIOClient cur:socketIOClientSet){
                  cur.sendEvent("asset",queryService.list());
                  log.info(i++ +" "+queryService.list().toString());
              }
          }

        //广播消息
        //sendBroadcast();
    }

    @OnEvent(value="stream")
    public void onStream(SocketIOClient client, AckRequest request, JSONObject data0) throws InterruptedException, JSONException {
       log.info("stream");
       log.info(JSON.toJSONString(data0));
       if(data0.get("event").equals("all")) {
          log.info("all");
           //String data=JSON.toJSONString(data0);
          //Stream data=JSON.parseObject(JSON.toJSONString(data0),Stream.class);
         /* myQueueAll.add(data0);
          log.info(String.valueOf(myQueueAll.size()));
           while(!flaga){
               Thread.sleep(10);
           }1667869490284
           log.info("开始");
           flag=false;
           List<JSONObject> nameList=redisService.get(data0.get("rid")+"nameList",List.class);
           List<JSONObject> nameList0 = new ArrayList(nameList);
           JSONObject myData=myQueueAll.poll();
           for(JSONObject cur:nameList0){ //uid
               NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
               if(!u.uid.equals(data0.get("uid"))){
                   log.info(u.uid);
                   log.info(JSON.toJSONString(myData));
                   socketIOClientMap.get(u.uid).sendEvent("stream",myData);
               }
           }
           flag=true;*/
       }
       if(data0.get("event").equals("increment")){
         //  Long dataNext=Long.valueOf(JSON.parseObject(data0.get("frame").toString()).get("params").toString()     toString());
           log.info("increment");
           List<JSONObject> nameList=redisService.get(data0.get("rid")+"nameList",List.class);
           List<JSONObject> nameList0 = new ArrayList(nameList);
         //  Istream myData=new Istream();
          // pre=Long.valueOf(myData.frame.pixel.get("date").toString());
           for(JSONObject cur:nameList0){ //uid
               NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
               if(!u.uid.equals(data0.get("rid"))){
                   log.info(u.uid);
                   log.info(JSON.toJSONString(data0));
                   socketIOClientMap.get(u.uid).sendEvent("stream",data0);
               }
           }
           flag=true;
       }

       if(data0.get("event").equals("edit")){
           log.info("edit");
           Istream data=JSON.parseObject(JSON.toJSONString(data0),Istream.class);
           //查看时间戳比队尾的大才加，否则扔掉
           Long dateNext= Long.valueOf(data.frame.pixel.get("date").toString());
           if(myQueue.size()==0&&dateNext<pre){
           return;
           }
           if(myQueue.size()>0&&Long.valueOf(myQueue.getFirst().frame.pixel.get("date").toString())>dateNext){
               return;
           }
           myQueue.add(data);
           log.info(String.valueOf(myQueue.size()));
          while(!flag){
              Thread.sleep(10);
          }
          log.info("开始");
          flag=false;
           List<JSONObject> nameList=redisService.get(data.rid+"nameList",List.class);
           List<JSONObject> nameList0 = new ArrayList(nameList);
           Istream myData=new Istream();
           myData=myQueue.removeLast();
           pre=Long.valueOf(myData.frame.pixel.get("date").toString());
          for(JSONObject cur:nameList0){ //uid
              NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
              if(!u.uid.equals(data.uid)){
                  log.info(u.uid);
                  log.info(JSON.toJSONString(myData));
                  socketIOClientMap.get(u.uid).sendEvent("stream",myData);
              }
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
         socketIOClientSet.add(client);
        log.info(String.valueOf(socketIOClientSet.size()));
        String message="d";
        List<House> r = redisService.get("list", List.class);
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
        log.info("检测到断连");
        socketIOClientSet.remove(client);
        log.info("移除client "+client.toString());
        String uid=socketIOClientMap1.get(client);
        log.info("拿到uid"+ uid);
        if(uid!=null){
            String rid=redisService.get(uid+"Room",House.class).rid;
            log.info("拿到rid"+ rid);
            log.info("要退出房间了");
            queryService.exit(client,rid,uid);
        }int i=0;
        for(SocketIOClient cur:socketIOClientSet){
            cur.sendEvent("asset",queryService.list());
            log.info(i++ +" "+queryService.list().toString());
        }
        log.info("客户端:" + client.getSessionId() + "断开连接");
    }


    /**
     * 广播消息
     */
    public void sendBroadcast() {
        for (SocketIOClient client : socketIOClientSet) {
            if (client.isChannelOpen()) {
                client.sendEvent("redirect", "当前时间", System.currentTimeMillis());
            }
        }

    }
}
