package fastclip.controller;

/*
*
 功能              请求方法                 链接         参数       返回参数

获取所有房间信息      http/get             /login/list             json数组（data，state）

搜索（前端）

创建房间           http/post            /login/{name}  String    json数组（房间信息，个人信息）

加入房间           websocket            /login/{id}    String    json数组（最近的全量帧及之后的增量帧及房间信息，个人信息）*/

import com.alibaba.fastjson.JSON;
import fastclip.domain.House;
import fastclip.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;



@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Autowired
    RedisService redisService;

    @GetMapping("/list")
    public String list() {
        List<House> r = redisService.get("list", List.class);
        if (r == null) {
            redisService.set("list", new ArrayList());
            return "null";
        }
        String R = String.valueOf(JSON.parseArray(String.valueOf(r)));
        return R;
    }




}
