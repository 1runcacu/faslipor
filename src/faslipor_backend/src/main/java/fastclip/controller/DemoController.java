package fastclip.controller;

import fastclip.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/demo")
public class DemoController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
    @Autowired
    RedisService redisService;

    @RequestMapping("/set")
    public String set(){
        redisService.set("key",123);
        return "success";
    }
    @GetMapping("/{key}")
    public String get(@PathVariable String key){
        String out=redisService.get(key,String.class);
        return out;
    }

}
