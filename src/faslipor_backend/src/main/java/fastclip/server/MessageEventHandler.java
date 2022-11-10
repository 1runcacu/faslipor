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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public static Map<String,JSONObject> nowAllMap=new HashMap<>(); //rid, (gid,pixel)

    public static Map<String,Stack<Map<String,JSONObject>>> historyAllMap=new HashMap<>();//rid,(Stack<gid,pixel>)

    public static Map<String,Stack<Map<String,JSONObject>>> redoAllMap=new HashMap<>();

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

//event,rid,uid,lid,frame:roomBuffer[rid]
    @OnEvent(value="stream")
    public void onStream(SocketIOClient client, AckRequest request, JSONObject data0) throws InterruptedException, JSONException, IOException {
       log.info("stream");
       log.info(JSON.toJSONString(data0));
       if(data0.get("event").equals("save")){
           log.info("save");
           String uid = socketIOClientMap1.get(client);
           House myRoom = redisService.get(uid + "Room", House.class);
           if(!nowAllMap.containsKey(myRoom.rid)) return;
           //部署时注意这里需要改
           Message message=new Message();
           message.type="info";
           String filePath="/www/web/app/Room";
           File dir=new File(filePath);
           if(!dir.exists()){
               dir.mkdirs();
           }
           File checkFile=new File(filePath+"/"+uid+".fsl");
           FileWriter writer=null;
           try{
               if(!checkFile.exists()){
                   checkFile.createNewFile();
               }
               writer=new FileWriter(checkFile,false);
               writer.append(JSON.toJSONString(nowAllMap.get(myRoom.rid)));
               writer.flush();
           } catch (IOException e) {
               e.printStackTrace();
           }finally {
               if(null!=writer)
                   writer.close();
           }
           message.message="/Room"+"/"+uid+".fsl";
           log.info(JSON.toJSONString(message));
           /*/localhost:8101/Room/360689.fsl
           * */
           client.sendEvent("message",message);
       }
       if(data0.get("event").equals("refresh")) {
           log.info("refresh");
           String uid = socketIOClientMap1.get(client);
           House myRoom = redisService.get(uid + "Room", House.class);
           if(!nowAllMap.containsKey(myRoom.rid)) return;
           Refresh myRe=new Refresh();
           myRe.rid=myRoom.rid;
           myRe.uid=uid;
           log.info(nowAllMap.containsKey(myRoom.rid)+" ");
           myRe.frame=nowAllMap.get(myRoom.rid);
           log.info(myRe.frame.toJSONString());
           log.info(JSONObject.toJSONString(myRe));
           client.sendEvent("stream",myRe);
       }
       if(data0.get("event").equals("undo")){
           log.info("undo");
           String uid = socketIOClientMap1.get(client);
           House myHouse=redisService.get(uid+"Room",House.class);
           if(historyAllMap.get(myHouse.rid)==null) return;
           if(historyAllMap.get(myHouse.rid).size()==0) return;
           log.info(historyAllMap.get(myHouse.rid).size()+" ");
           log.info("回撤前的历史记录"+historyAllMap.get(myHouse.rid));
           Map<String,JSONObject> elem=historyAllMap.get(myHouse.rid).pop();
           Stack<Map<String,JSONObject>> myStack=redoAllMap.get(myHouse.rid);
           if(myStack==null) myStack=new Stack<>();
           myStack.push(elem);
           redoAllMap.put(myHouse.rid,myStack);
           //更新当前帧
           //让它去调用refresh
           Refresh  myRefresh=new Refresh();
           myRefresh.uid=uid;
           myRefresh.rid=myHouse.rid;
           log.info("回撤后的历史记录"+historyAllMap.get(myHouse.rid));
           if(historyAllMap.get(myHouse.rid).size()==0) //
           {
               myRefresh.frame=null;
              // log.info(myRefresh.frame.toJSONString());
              // log.info(JSONObject.toJSONString(myRefresh));
               nowAllMap.put(myHouse.rid,null);
           }else{
               nowAllMap.put(myHouse.rid,(JSONObject) JSONObject.toJSON(historyAllMap.get(myHouse.rid).peek()));
               myRefresh.frame=(JSONObject) JSONObject.toJSON(historyAllMap.get(myHouse.rid).peek());
               log.info("栈的顶部，即当前应该显示的内容"+JSONObject.toJSONString(historyAllMap.get(myHouse.rid).peek()));
           }
           List<JSONObject> nameList=redisService.get(myHouse.rid+"nameList",List.class);
           List<JSONObject> nameList0 = new ArrayList(nameList);
           for(JSONObject cur:nameList0){ //uid
               NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
                   log.info(u.uid);
                   myRefresh.uid=u.uid;
                   log.info("向房间里所有人发送的内容"+JSONObject.toJSONString(myRefresh));
                   socketIOClientMap.get(u.uid).sendEvent("stream",myRefresh);
           }

       }
       if(data0.get("event").equals("all")) {
          log.info("all");
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
           log.info("myQueue.size()"+String.valueOf(myQueue.size()));
           log.info(String.valueOf(flag)+"转发");
          while(!flag){
              Thread.sleep(10);
              log.info(data.frame.pixel.get("date").toString()+"的"+Thread.currentThread().getName() +"在等待");
          }
          log.info("开始");
          flag=false;
          log.info(String.valueOf(flag)+"不允许转发");
           List<JSONObject> nameList=redisService.get(data.rid+"nameList",List.class);
           List<JSONObject> nameList0 = new ArrayList(nameList);
           Istream myData=new Istream();
           myData=myQueue.removeLast();
           //all是该房间的全量帧 由一个个gid对应的pixel组成
           Map<String,JSONObject> all=JSON.toJavaObject(nowAllMap.get(myData.rid),Map.class);
           if(all==null) all=new HashMap<>();
           //log.info(all.toString());
           //这里需要缓存之前的gid
           boolean f=true;
           if(all.get(myData.frame.pixel.get("gid").toString())!=null&&!myData.syn){
               f=false;
           }
           all.put(myData.frame.pixel.get("gid").toString(),myData.frame.pixel);
           Stack<Map<String,JSONObject>> myStack=historyAllMap.get(myData.rid);
           log.info(JSONObject.toJSONString(myStack));
           if(myStack==null)//该房间还未有历史记录
           {myStack=new Stack<>();
           historyAllMap.put(myData.rid, myStack);
           }
           if(f) {
               log.info("当前的历史记录大小"+historyAllMap.get(myData.rid).size());
               log.info("当前的历史记录"+myStack);
               Map<String,JSONObject> all0=new HashMap<>();
               ObjectMapper objectMapper=new ObjectMapper();
               all0=objectMapper.readValue(objectMapper.writeValueAsString(all),Map.class);
               myStack.push(all0);
               log.info("加入后的历史记录1"+myStack);
               log.info("加入后的历史记录大小1 "+String.valueOf(historyAllMap.get(myData.rid).size()));
               historyAllMap.put(myData.rid, myStack);  //引用类型，不用put
               log.info("加入后的历史记录2"+historyAllMap.get(myData.rid));
               log.info("加入后的历史记录大小2 "+String.valueOf(historyAllMap.get(myData.rid).size()));
           }
           log.info("put"+myData.rid);
           nowAllMap.put(myData.rid, (JSONObject) JSONObject.toJSON(all));
           pre=Long.valueOf(myData.frame.pixel.get("date").toString());
          for(JSONObject cur:nameList0){ //uid
              NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
              //Thread.sleep(1000);
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
