package fastclip.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import fastclip.domain.House;
import fastclip.domain.Redirect;
import fastclip.domain.Result;
import fastclip.domain.User;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.alibaba.fastjson.JSONObject.*;

@Slf4j
@Service
public class QueryService {

    @Autowired
    RedisService redisService;
    public Redirect addRoom(String roomName,String brief){
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
        User newUser = new User();
        Date time1 = new Date();
        newUser.uid = ((Long) time1.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        newUser.state = "创建者";
        //加入房间列表
        List<JSONObject> r = redisService.get("list", List.class);
        List<JSONObject> arrList = new ArrayList(r);
        arrList.add(JSON.parseObject(JSON.toJSONString(newRoom)));
        redisService.set("list",arrList);
        //名单
        List<String> nameList=new ArrayList<>();
        nameList.add(newUser.uid);
        redisService.set(newRoom.rid, newRoom);
        redisService.set(newRoom.rid+"nameList",nameList);
        redisService.set(newUser.uid,newUser);
        Redirect redirect=new Redirect();
        redirect.path="/panel";
        redirect.params.user=newUser;
        redirect.params.room=newRoom;
        return redirect;
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

    public Redirect select(String rid){
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
        User newUser = new User();
        Date time1 = new Date();
        newUser.uid = ((Long) time1.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        newUser.state = "创建者";
       // List<JSONObject> nameList=redisService.get(rid+"nameList",List.class);
       // List<JSONObject> nameList0 = new ArrayList(r);
       // nameList0.add(JSON.parseObject(String.valueOf(newUser.uid)));
       redisService.set(myRoom.rid, myRoom);
     //  redisService.set(myRoom.rid+"nameList",nameList0);
        redisService.set(newUser.uid,newUser);
        redirect.path="/panel";
        redirect.params.user=newUser;
        redirect.params.room=myRoom;
        return redirect;
    }

}