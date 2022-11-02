package fastclip.controller;

/*
*
 功能              请求方法                 链接         参数       返回参数

获取所有房间信息      http/get             /login/list             json数组（data，state）

搜索（前端）

创建房间           http/post            /login/{name}  String    json数组（房间信息，个人信息）

加入房间           websocket            /login/{id}    String    json数组（最近的全量帧及之后的增量帧及房间信息，个人信息）*/

import com.alibaba.fastjson.JSON;
import fastclip.domain.Room;
import fastclip.domain.User;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Autowired
    RedisService redisService;

    @GetMapping("/list")
    public String list() {
        List<Room> r = redisService.get("list", List.class);
        if (r == null) {
            redisService.set("list", new ArrayList());
            return "null";
        }
        String R = String.valueOf(JSON.parseArray(String.valueOf(r)));
        return R;
    }

    @GetMapping("/{name}")
    public String get(@PathVariable String name) {
        Room newRoom = new Room();
        Date time0 = new Date();
        newRoom.id = ((Long) time0.getTime()).toString(36) + ((Double) Math.random()).toString().substring(4, 8);
        newRoom.name = name;
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
        log.info(JSON.toJSONString(redisService.get(newUser.id,User.class)));
        return JSON.toJSONString(newRoom)+JSON.toJSONString(newUser);
    }


}
