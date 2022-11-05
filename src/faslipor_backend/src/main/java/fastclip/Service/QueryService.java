package fastclip.Service;

import fastclip.domain.Redirect;
import fastclip.domain.Result;
import fastclip.domain.Room;
import fastclip.domain.User;
import fastclip.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QueryService {

    @Autowired
    RedisService redisService;
    public Redirect addRoom(String roomName,String brief){
        //新建房间
        Room newRoom = new Room();
        Date time0 = new Date();
        newRoom.setId(((Long) time0.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8));
        newRoom.setName(roomName);
        newRoom.setDescription(brief);
        newRoom.setLimit(10);
        newRoom.setStats(1);
        newRoom.setState(true);
        //新建用户
        User newUser = new User();
        Date time1 = new Date();
        newUser.uid = ((Long) time1.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        newUser.state = "创建者";
        //加入房间列表
        List<Room> r = redisService.get("list", List.class);
        if (r == null) {
            r=new ArrayList();
        }
        r.add(newRoom);
        redisService.set("list", r);
        //名单
        List<String> nameList=new ArrayList<>();
        nameList.add(newUser.uid);
        redisService.set(newRoom.getId(), newRoom);
        redisService.set(newRoom.getId()+"nameList",nameList);
        redisService.set(newUser.uid,newUser);
        Redirect redirect=new Redirect();
        redirect.path="/panel";
        redirect.params.user=newUser;
        redirect.params.room=newRoom;
        return redirect;
    }

    public Result list(){
        List<Room> r = redisService.get("list", List.class);
        if (r == null) {
            r=new ArrayList();
        }
        Result myResult=new Result();
        myResult.data=r;
        myResult.event="list";
        myResult.state=200;
        return myResult;
    }

    public Redirect select(String rid){
        Redirect redirect=new Redirect();
        Room myRoom=redisService.get(rid,Room.class);
        if(myRoom==null){
            redirect.params.state=404;
            return redirect;
        }
        if(!myRoom.getState()){
            redirect.params.state=403;
            return redirect;
        }
        /*if(myRoom.getLimit()==myRoom.getStats()){

        }*/
        User newUser = new User();
        Date time1 = new Date();
        newUser.uid = ((Long) time1.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        newUser.state = "创建者";
        return redirect;
    }

}
