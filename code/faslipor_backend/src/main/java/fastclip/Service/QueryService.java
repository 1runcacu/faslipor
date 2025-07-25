package fastclip.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fastclip.domain.*;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.alibaba.fastjson.JSONObject.*;
import static fastclip.server.MessageEventHandler.*;

@Slf4j
@Service
public class QueryService {

    @Autowired
    RedisService redisService;
    public JSONObject addRoom(SocketIOClient client,String roomName,String brief) throws IOException {
        //新建房间
        House newRoom=new House();
        Date time0 = new Date();
        newRoom.rid=((Long) time0.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
       // log.info(JSON.toJSONString(newRoom));
        newRoom.name=roomName;
        newRoom.description=brief;
        newRoom.limit=10;
        newRoom.state=true;
        newRoom.stats=1;
        //新建用户
        Usr newUser = new Usr();
        Date time1 = new Date();
        newUser.uid = ((Long) time1.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        newUser.state = "管理员";
        socketIOClientMap.put(newUser.uid,client);
        socketIOClientMap1.put(client,newUser.uid);
        //加入房间列表
        List<JSONObject> r = redisService.get("list", List.class);
        if(r==null) r=new ArrayList<>();
        List<JSONObject> arrList = new ArrayList(r);
        arrList.add(JSON.parseObject(JSON.toJSONString(newRoom)));
        redisService.set("list",arrList);
        //名单
        NameList newNameList=new NameList();
        newNameList.uid=newUser.uid;
        newNameList.state=newUser.state;
        List<NameList> nameList=new ArrayList<>();
        nameList.add(newNameList);
        redisService.set(newRoom.rid, newRoom);
        redisService.set(newRoom.rid+"nameList",nameList);
        redisService.set(newUser.uid+"Room",newRoom);
        Layout myLayout=new Layout();
        time0 = new Date();
        myLayout.name="sheet";
        newUser.lid=((Long) time1.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        myLayout.lid=newUser.lid+"";
        log.info(String.valueOf(newUser.lid==myLayout.lid));
        nowAllMap.put(newRoom.rid+myLayout.lid+"",new JSONObject());
        List<Layout> layouts=redisService.get(newRoom.rid+"layout",List.class);
        if(layouts==null){
            layouts=new ArrayList<>();
        }
        layouts.add(myLayout);
        redisService.set(newRoom.rid+"layout",layouts);
        redisService.set(newUser.uid+"layout",myLayout);
        redisService.set(newUser.uid+"User",newUser);
        Redirect redirect = new Redirect();
        redirect.path="/panel";
        redirect.params.user=newUser;
        redirect.params.room=newRoom;
        redirect.params.layout=layouts;
        log.info(JSONObject.toJSONString(redirect));
        log.info(""+JSON.parseObject(JSONObject.toJSONString(redirect)));
        return JSON.parseObject(JSONObject.toJSONString(redirect));
    }

    public Result list(){
        List<House> r = redisService.get("list", List.class);
        if (r == null) {
            r=new ArrayList();
        }
        log.info(r.toString());
        Result myResult=new Result();
        myResult.data=r;
        myResult.event="list";
        myResult.state=200;
        return myResult;
    }

    public Redirect exit(SocketIOClient client,String rid, String uid){
        socketIOClientMap.remove(uid);//移除流接受列表
        socketIOClientMap1.remove(client);
        Redirect redirect=new Redirect();
        House myRoom=redisService.get(rid, House.class);
        List<JSONObject> r = redisService.get("list", List.class);
        List<JSONObject> arrList = new ArrayList(r);
        log.info(r.toString());
        Iterator<JSONObject> iterator = arrList.iterator();
        House u=null;
        while (iterator.hasNext()) {
             u = JSON.parseObject(String.valueOf(iterator.next()),House.class);
            log.info(JSON.toJSONString(u));
            if (myRoom.rid.equals(u.rid)) {
                log.info(myRoom.rid+"  "+u.rid);
                iterator.remove();//使用迭代器的删除
                break;
            }
        }
        List<JSONObject> myNameList = redisService.get(rid+"nameList", List.class);
        List<JSONObject> myNameList0 = new ArrayList(myNameList);
        log.info(r.toString());
        Iterator<JSONObject> iterator0 = myNameList0.iterator();
        NameList p=null;
        while (iterator0.hasNext()) {
            p = JSON.parseObject(String.valueOf(iterator0.next()),NameList.class);
            log.info(JSON.toJSONString(p));
            if (uid.equals(p.uid)) {
                log.info(uid+"  "+p.uid);
                iterator0.remove();//使用迭代器的删除
                break;
            }
        }
        log.info("删后的"+arrList.toString());
        if(u.stats>1){
            myRoom.stats--;
            arrList.add(JSON.parseObject(JSON.toJSONString(myRoom)));
        }
        log.info("判断人数后的"+arrList.toString());
        redisService.set("list",arrList);
        redisService.set(myRoom.rid,myRoom);
        redisService.set(rid+"nameList",myNameList0);
        redisService.del(uid+"User");
        redisService.del(rid+"Room");
        Result myResult=new Result();
        myResult.data=redisService.get("list", List.class);
        log.info("redis里的"+myResult.data.toString());
        redirect.path="/";
        redirect.params.result=myResult;
        return redirect;
    }


    public Redirect select(SocketIOClient client,String rid){
        Redirect redirect=new Redirect();
        House myRoom=redisService.get(rid, House.class);
        if(myRoom==null){
            redirect.params.state=404;
            return redirect;
        }
       if(!myRoom.state){
            redirect.params.state=403;
            return redirect;
        }
        if(myRoom.limit==myRoom.stats){
            redirect.params.state=403;
            return redirect;
        }
        List<JSONObject> r = redisService.get("list", List.class);
        List<JSONObject> arrList = new ArrayList(r);
        log.info(r.toString());
        Iterator<JSONObject> iterator = arrList.iterator();
        while (iterator.hasNext()) {
            House u = JSON.parseObject(String.valueOf(iterator.next()),House.class);
            log.info(JSON.toJSONString(u));
            if (myRoom.rid.equals(u.rid)) {
                log.info(myRoom.rid+"  "+u.rid);
                iterator.remove();//使用迭代器的删除
            }
        }
        int num=myRoom.stats+1;
        myRoom.stats=num;
        arrList.add(JSON.parseObject(JSON.toJSONString(myRoom)));
        redisService.set("list",arrList);
        Usr newUser = new Usr();
        Date time1 = new Date();
        newUser.uid = ((Long) time1.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        newUser.state = "普通成员";
        socketIOClientMap.put(newUser.uid,client);
        socketIOClientMap1.put(client,newUser.uid);
        log.info(newUser.uid+" "+client);
        NameList newNameList=new NameList();
        newNameList.uid=newUser.uid;
        newNameList.state=newUser.state;
        List<JSONObject> nameList=redisService.get(rid+"nameList",List.class);
        List<JSONObject> nameList0 = new ArrayList(nameList);
        nameList0.add(JSON.parseObject(JSON.toJSONString(newNameList)));
        redisService.set(myRoom.rid, myRoom);
        redisService.set(myRoom.rid+"nameList",nameList0);
        redisService.set(newUser.uid+"Room",myRoom);
        redirect.path="/panel";
        redirect.params.room=myRoom;
        redirect.params.layout=redisService.get(myRoom.rid+"layout",List.class);
        newUser.lid=JSON.parseObject(JSON.toJSONString(redirect.params.layout.get(0)),Layout.class).lid;
        redirect.params.user=newUser;
        redisService.set(newUser.uid+"User",newUser);
        return redirect;
    }

}
