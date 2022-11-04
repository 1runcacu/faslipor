package fastclip.Service;

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
    public Result addRoom(String roomName,String brief){
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
        newUser.id = ((Long) time1.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        newUser.state = "创建者";
        //加入房间列表
        List<Room> r = redisService.get("list", List.class);
        if (r == null) {
            r=new ArrayList();
        }
        r.add(newRoom);
        redisService.set("list", r);
        //名单
        newRoom.addName(newUser.id);
        redisService.set(newRoom.getId(), newRoom);
        redisService.set(newUser.id,newUser);
        Result result=new Result();
        result.event="add";
        result.state="200";
        result.user=newUser;
        result.room=newRoom;
        return result;
    }

}
