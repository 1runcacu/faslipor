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
import fastclip.Service.StreamService;
import fastclip.domain.*;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Queue;

import java.io.File;
import java.io.FileNotFoundException;
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

    @Autowired
    private StreamService streamService;

    public static Set<SocketIOClient> socketIOClientSet = new HashSet<>();

    public static Map<String,SocketIOClient> socketIOClientMap = new ConcurrentHashMap<>();

    public static Map<SocketIOClient,String> socketIOClientMap1=new ConcurrentHashMap<>();

    public static Map<String,JSONObject> nowAllMap=new HashMap<>(); //rid, (gid,pixel)

    public static Map<String,Stack<Map<String,JSONObject>>> historyAllMap=new HashMap<>();//rid,(Stack<gid,pixel>)

    public static Map<String,Stack<Map<String,JSONObject>>> redoAllMap=new HashMap<>();

    public static Map<String,List<Frame>> chatHistory=new ConcurrentHashMap<>();

    public static  LinkedList<Istream> myQueue=new LinkedList<>();

    public static java.util.Queue<JSONObject> myQueueAll=new LinkedList<>();

    public static Long pre= Long.valueOf(0);

    public static volatile boolean flag=true;

    public static volatile boolean flaga=true;


    @OnEvent(value = "query")
    public void onQuery(SocketIOClient client, AckRequest request, Query data) throws IOException {
          log.info("query");
          log.info(""+JSONObject.toJSONString(data));
          if(data.event.equals("add"))
        { log.info("add");
            client.sendEvent("redirect",queryService.addRoom(client,data.params.roomName,data.params.description));
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

    @OnEvent(value = "chat")
    public void onChat(SocketIOClient client,AckRequest request,JSONObject data0){
        log.info("chat");
        log.info(JSON.toJSONString(data0));
        Istream data=JSON.parseObject(JSON.toJSONString(data0),Istream.class);
        if(data.event.equals("refresh")){
            data.frame=new Frame();
            data.frame.data=chatHistory.get(data.rid);
            if(data.frame.data==null) {
                data.frame.data=new ArrayList<>();
                chatHistory.put(data.rid,data.frame.data);
            }
        }else{
            List<Frame> h=chatHistory.get(data.rid);
            h.add(data.frame);
            chatHistory.put(data.rid,h);
        }
        List<JSONObject> nameList=redisService.get(data.rid+"nameList",List.class);
        List<JSONObject> nameList0 = new ArrayList(nameList);
       //data.syn=true;
        for(JSONObject cur:nameList0){ //uid
            NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
            log.info(u.uid);
            log.info(JSON.toJSONString(data));
            socketIOClientMap.get(u.uid).sendEvent("chat",data);
        }
    }

   @OnEvent(value = "console")
   public void onConsole(SocketIOClient client, AckRequest request, JSONObject data0){
        log.info("console");
        log.info(JSON.toJSONString(data0));
       Istream data=JSON.parseObject(JSON.toJSONString(data0),Istream.class);
       if(data0.get("event").equals("lock")){
           log.info("lock");
          House myHouse=redisService.get(data.rid,House.class);
          myHouse.lock=data.frame.lock.equals("true");
          redisService.set(data.rid,myHouse);
          //asset room被锁定
           List<JSONObject> nameList=redisService.get(data.rid+"nameList",List.class);
           List<JSONObject> nameList0 = new ArrayList(nameList);
           Result myResult=new Result();
           myResult.params.room=redisService.get(data.rid,House.class);
           myResult.event="sync";
           myResult.params.layout=redisService.get(myHouse.rid+"layout",List.class);
           for(JSONObject cur:nameList0){ //uid
               NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
               myResult.params.user=redisService.get(u.uid+"User",Usr.class);
               //同步lid?
               socketIOClientMap.get(u.uid).sendEvent("asset",myResult);
           }
           //所有人被同步到房主的房间
           Refresh myRe=streamService.refresh(client,data.lid);
           if(myRe==null) return;
           if(redisService.get(data.rid,House.class).lock) {
               for (JSONObject cur : nameList0) { //uid
                   NameList u = JSON.parseObject(JSON.toJSONString(cur), NameList.class);
                   myRe.uid = redisService.get(u.uid + "User", Usr.class).uid;
                   socketIOClientMap.get(u.uid).sendEvent("stream", myRe);
               }
           }
       }
   }


    @OnEvent(value = "file")
    public void onFile(SocketIOClient client, AckRequest request, JSONObject data0){
        log.info("file");
        log.info(""+""+JSONObject.toJSONString(data0));
        if(data0.get("event").equals("refresh")){
          Istream data=JSON.parseObject(JSON.toJSONString(data0),Istream.class);
            log.info(data.frame.file);
          nowAllMap.put(data.rid+data.lid, JSONObject.parseObject(JSON.toJSONString(JSONObject.parseObject(data.frame.file).get("pixel"))));
            List<JSONObject> nameList=redisService.get(data.rid+"nameList",List.class);
            List<JSONObject> nameList0 = new ArrayList(nameList);
            //client.sendEvent("stream",streamService.refresh(client,data.lid));
            for(JSONObject cur:nameList0){ //uid
                NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
                if(redisService.get(u.uid+"User",Usr.class).lid.equals(data.lid)) //有图层在被删图层
                    socketIOClientMap.get(u.uid).sendEvent("stream",streamService.refresh(socketIOClientMap.get(u.uid),data.lid));
            }

        }
    }
    @OnEvent(value="stream")
    public void onStream(SocketIOClient client, AckRequest request, JSONObject data0) throws InterruptedException, JSONException, IOException {
       log.info("stream");
       log.info(""+""+JSONObject.toJSONString(data0));
       if(data0.get("event").equals("export")){
           log.info("export");
           client.sendEvent("file",streamService.save(client,request,data0));
       }

       if(data0.get("event").equals("refresh")) {
           log.info("refresh");
           Istream data=JSON.parseObject(JSON.toJSONString(data0),Istream.class);
           if(data.frame.type==null||data.frame.type.equals("切换")) //刚进入
           {  Refresh myRe=streamService.refresh(client,data.lid);
               if(myRe==null) return;
               if(redisService.get(data.rid,House.class).lock){
                   List<JSONObject> nameList=redisService.get(data.rid+"nameList",List.class);
                   List<JSONObject> nameList0 = new ArrayList(nameList);
                   for(JSONObject cur:nameList0){ //uid
                       NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
                       myRe.uid=redisService.get(u.uid+"User",Usr.class).uid;
                       socketIOClientMap.get(u.uid).sendEvent("stream",myRe);
                   }
               }else{
               client.sendEvent("stream",myRe);}
           }
           else if(data.frame.type.equals("创建")){
               log.info("创建");
               //给同一房间的人发
               if(redisService.get(data.rid,House.class).lock) return;
               List<JSONObject> nameList=redisService.get(data.rid+"nameList",List.class);
               List<JSONObject> nameList0 = new ArrayList(nameList);
               Result myResult=streamService.create(client,data);
               for(JSONObject cur:nameList0){ //uid
                   NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
                   myResult.params.user=redisService.get(u.uid+"User",Usr.class);
                   socketIOClientMap.get(u.uid).sendEvent("asset",myResult);
               }
               Refresh myRe=streamService.refresh(client,myResult.params.user.lid);
               client.sendEvent("stream",myRe);
           }else if(data.frame.type.equals("删除")){
               log.info("删除"); //拉黑该lid 如果遇到编辑和刷新都无视
               if(redisService.get(data.rid,House.class).lock) return;
               nowAllMap.put(data.rid+data.lid,null);
               Usr myUser=redisService.get(data.uid+"User",Usr.class);
               List<Layout> layouts1 = redisService.get(data.rid+"layout", List.class);
               List<Layout> layouts=new ArrayList<>(layouts1);
               Layout myLayout=redisService.get(data.uid + "layout", Layout.class);
               Iterator<Layout> iterator = layouts.iterator();
               Layout u0=null;
               while (iterator.hasNext()) {
                   u0 = JSON.parseObject(String.valueOf(iterator.next()),Layout.class);
                   log.info(JSON.toJSONString(u0));
                   log.info(data.lid);
                   if (data.lid.equals(u0.lid)) {
                       iterator.remove();//使用迭代器的删除
                       break;
                   }
               }
               redisService.set(data.rid + "layout", layouts);
               log.info("到底删了没"+JSONObject.toJSONString(layouts));
               if(layouts.size()==0){
                   List<JSONObject> nameList=redisService.get(data.rid+"nameList",List.class);
                   List<JSONObject> nameList0 = new ArrayList(nameList);
                   Result myResult=streamService.create(client,data);
                   String lid=myResult.params.user.lid;
                   for(JSONObject cur:nameList0){ //uid

                       NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
                       Usr myUsr=redisService.get(u.uid+"User",Usr.class);
                       myUsr.lid=lid;
                       redisService.set(u.uid+"User",myUsr);
                       myResult.params.user=redisService.get(u.uid+"User",Usr.class);
                       socketIOClientMap.get(u.uid).sendEvent("asset",myResult);
                   }
                   layouts1 = redisService.get(data.rid+"layout", List.class);
                   layouts=new ArrayList<>(layouts1);
               }
               // NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
               myUser.lid=JSON.parseObject(JSON.toJSONString(layouts.get(0)),Layout.class).lid;
               redisService.set(myUser.uid+"User",myUser);
               Result myResult=new Result();
               myResult.event="sync";
               myResult.params.room=redisService.get(data.rid,House.class);
               myResult.params.user=myUser;
               myResult.params.layout=layouts;
               List<JSONObject> nameList=redisService.get(data.rid+"nameList",List.class);
               List<JSONObject> nameList0 = new ArrayList(nameList);
               client.sendEvent("stream",streamService.refresh(client,myUser.lid));
               for(JSONObject cur:nameList0){ //uid
                   NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
                   myResult.params.user=redisService.get(u.uid+"User",Usr.class);
                   socketIOClientMap.get(u.uid).sendEvent("asset",myResult); //更新房间列表
                   log.info(redisService.get(u.uid+"User",Usr.class).lid);
                   log.info(myUser.lid);
                   if(redisService.get(u.uid+"User",Usr.class).lid.equals(data.lid)||redisService.get(u.uid+"User",Usr.class).lid.equals(myUser.lid)) //有图层在被删图层
                   socketIOClientMap.get(u.uid).sendEvent("stream",streamService.refresh(socketIOClientMap.get(u.uid),myUser.lid));
               }
           }
           /*
                   case "删除": {
                       //群发list
                       break;
                   }
               }
           }*/
       }
       if(data0.get("event").equals("redo")){
           log.info("redo");
            String uid = socketIOClientMap1.get(client);
            House myHouse=redisService.get(uid+"Room",House.class);
            if(myHouse.lock) return;
            Usr myUsr=redisService.get(uid+"User",Usr.class);
            if(redoAllMap.get(myHouse.rid+myUsr.lid)==null) return;
            if(redoAllMap.get(myHouse.rid+myUsr.lid).size()==0) return;
            Map<String,JSONObject> elem=redoAllMap.get(myHouse.rid+myUsr.lid).pop();
            Stack<Map<String,JSONObject>> myStack=historyAllMap.get(myHouse.rid+myUsr.lid);
            if(myStack==null) myStack=new Stack<>();
            myStack.push(elem);
            historyAllMap.put(myHouse.rid+myUsr.lid,myStack); //可不加
            Refresh  myRefresh=new Refresh();
            myRefresh.uid=uid;
            myRefresh.rid=myHouse.rid;
            nowAllMap.put(myHouse.rid+myUsr.lid,(JSONObject) JSONObject.toJSON(historyAllMap.get(myHouse.rid+myUsr.lid).peek()));
            myRefresh.frame=(JSONObject) JSONObject.toJSON(historyAllMap.get(myHouse.rid+myUsr.lid).peek());
            log.info("栈的顶部，即当前应该显示的内容"+JSONObject.toJSONString(historyAllMap.get(myHouse.rid+myUsr.lid).peek()));
            List<JSONObject> nameList=redisService.get(myHouse.rid+"nameList",List.class);
            List<JSONObject> nameList0 = new ArrayList(nameList);
           for(JSONObject cur:nameList0){ //uid
               NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
               log.info(u.uid);
               myRefresh.uid=u.uid;
               if(redisService.get(u.uid+"User",Usr.class).lid.equals(myUsr.lid)){
                   myRefresh.lid=myUsr.lid;
                   log.info("向房间里所有人发送的内容"+JSONObject.toJSONString(myRefresh));
                   socketIOClientMap.get(u.uid).sendEvent("stream",myRefresh);
               }
           }
       }
       if(data0.get("event").equals("undo")){
           log.info("undo");
           String uid = socketIOClientMap1.get(client);
           House myHouse=redisService.get(uid+"Room",House.class);
           if(myHouse.lock) return;
           Usr myUsr=redisService.get(uid+"User",Usr.class);
           if(historyAllMap.get(myHouse.rid+myUsr.lid)==null) return;
           if(historyAllMap.get(myHouse.rid+myUsr.lid).size()==0) return;
           log.info(historyAllMap.get(myHouse.rid+myUsr.lid).size()+" ");
           log.info("回撤前的历史记录"+historyAllMap.get(myHouse.rid+myUsr.lid));
           Map<String,JSONObject> elem=historyAllMap.get(myHouse.rid+myUsr.lid).pop();
           Stack<Map<String,JSONObject>> myStack=redoAllMap.get(myHouse.rid+myUsr.lid);
           if(myStack==null) myStack=new Stack<>();
           myStack.push(elem);
           redoAllMap.put(myHouse.rid+myUsr.lid,myStack);
           //更新当前帧
           //让它去调用refresh
           Refresh  myRefresh=new Refresh();
           myRefresh.uid=uid;
           myRefresh.rid=myHouse.rid;
           log.info("回撤后的历史记录"+historyAllMap.get(myHouse.rid+myUsr.lid));
           if(historyAllMap.get(myHouse.rid+myUsr.lid).size()==0) //
           {
               myRefresh.frame=new JSONObject();
               nowAllMap.put(myHouse.rid+myUsr.lid,new JSONObject());
           }else{
               nowAllMap.put(myHouse.rid+myUsr.lid,(JSONObject) JSONObject.toJSON(historyAllMap.get(myHouse.rid+myUsr.lid).peek()));
               myRefresh.frame=(JSONObject) JSONObject.toJSON(historyAllMap.get(myHouse.rid+myUsr.lid).peek());
               log.info("栈的顶部，即当前应该显示的内容"+JSONObject.toJSONString(historyAllMap.get(myHouse.rid+myUsr.lid).peek()));
           }
           List<JSONObject> nameList=redisService.get(myHouse.rid+"nameList",List.class);
           List<JSONObject> nameList0 = new ArrayList(nameList);
           for(JSONObject cur:nameList0){ //uid
               NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
                   log.info(u.uid);
                   myRefresh.uid=u.uid;
               if(redisService.get(u.uid+"User",Usr.class).lid.equals(myUsr.lid)){
                   myRefresh.lid=myUsr.lid;
                   log.info("向房间里所有人发送的内容"+JSONObject.toJSONString(myRefresh));
                   socketIOClientMap.get(u.uid).sendEvent("stream",myRefresh);
               }
           }


       }
       if(data0.get("event").equals("edit")){
           log.info("edit");
           Istream data=JSON.parseObject(JSON.toJSONString(data0),Istream.class);
           //查看时间戳比队尾的大才加，否则扔掉
          // if(redisService.get(data.rid,House.class).lock) return;
           Long dateNext= Long.valueOf(data.frame.pixel.get("date").toString());
           if(myQueue.size()==0&&dateNext<pre){
           return;
           }
           if(myQueue.size()>0&&Long.valueOf(myQueue.getFirst().frame.pixel.get("date").toString())>dateNext){
               return;
           }
           if(nowAllMap.get(data.rid+data.lid)==null){
               return ;
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
           Map<String,JSONObject> all=JSON.toJavaObject(nowAllMap.get(myData.rid+myData.lid),Map.class);
           if(all==null) all=new HashMap<>();
           //log.info(all.toString());
           //这里需要缓存之前的gid
           boolean f=true;
           if(all.containsKey(myData.frame.pixel.get("gid").toString())&&all.get(myData.frame.pixel.get("gid").toString())==null){
               //已经被删除
               return;
           }
           if(myData.frame.type.equals("删除")){
               all.put(myData.frame.pixel.get("gid").toString(),null);
           }else{
               all.put(myData.frame.pixel.get("gid").toString(),myData.frame.pixel);
           }

           if(all.get(myData.frame.pixel.get("gid").toString())!=null&&!myData.syn){
               f=false;
           }
           Stack<Map<String,JSONObject>> myStack=historyAllMap.get(myData.rid+myData.lid);
           log.info(JSONObject.toJSONString(myStack));
           if(myStack==null)//该房间还未有历史记录
           {myStack=new Stack<>();
           historyAllMap.put(myData.rid+myData.lid, myStack);
           }
           if(f) {
               log.info("当前的历史记录大小"+historyAllMap.get(myData.rid+myData.lid).size());
               log.info("当前的历史记录"+myStack);
               Map<String,JSONObject> all0=new HashMap<>();
               ObjectMapper objectMapper=new ObjectMapper();
               all0=objectMapper.readValue(objectMapper.writeValueAsString(all),Map.class); //历史记录相当于无这个pixel
               myStack.push(all0);
               log.info("加入后的历史记录1"+myStack);
               log.info("加入后的历史记录大小1 "+String.valueOf(historyAllMap.get(myData.rid+myData.lid).size()));
               log.info("加入后的历史记录2"+historyAllMap.get(myData.rid+myData.lid));
               log.info("加入后的历史记录大小2 "+String.valueOf(historyAllMap.get(myData.rid+myData.lid).size()));
           }
           log.info("put"+myData.rid);
           nowAllMap.put(myData.rid+myData.lid, (JSONObject) JSONObject.toJSON(all));
           pre=Long.valueOf(myData.frame.pixel.get("date").toString());
          for(JSONObject cur:nameList0){ //uid
              NameList u = JSON.parseObject(JSON.toJSONString(cur),NameList.class);
              //Thread.sleep(1000);
              log.info(redisService.get(u.uid+"User",Usr.class).lid);
              log.info(myData.lid);
              if(!u.uid.equals(myData.uid)&&redisService.get(u.uid+"User",Usr.class).lid.equals(myData.lid)){
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
        Redirect my=new Redirect();
        my.path="/";
        Result myResult=new Result();
        myResult.data=redisService.get("list", List.class);
       // log.info("redis里的"+myResult.data.toString());
        my.params.result=myResult;
        client.sendEvent("redirect",my);
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
    public void onDisconnect(SocketIOClient client) throws FileNotFoundException {
        log.info("检测到断连");
        socketIOClientSet.remove(client);
        log.info("移除client "+client.toString());
        String uid=socketIOClientMap1.get(client);
        log.info("拿到uid"+ uid);
        if(uid!=null){
            String rid=redisService.get(uid+"Room",House.class).rid;
            log.info("拿到rid"+ rid);
            log.info("要退出房间了");
            client.sendEvent("redirect",queryService.exit(client,rid,uid));
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
