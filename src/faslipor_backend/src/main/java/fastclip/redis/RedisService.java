package fastclip.redis;

import com.alibaba.fastjson.TypeReference;
import fastclip.domain.House;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;
    /**
     * 获取当个对象
     * */
    public <T> T get(String key,  Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String  str = jedis.get(key);

            T t =  stringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    public void del(String key){
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            jedis.del(key);
        }finally {
            returnToPool(jedis);
        }
    }


    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }
    @SuppressWarnings("unchecked")
    private <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else if(clazz== List.class){


            return (T) Arrays.asList(JSON.parseObject(str,new TypeReference<Object[]>(){}));

        }
        else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    /**
     * 设置对象
     * */
    public <T> boolean set(String key,  T value) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length() <= 0) {
                return false;
            }
                jedis.set(key, str);
            return true;
        }finally {
            returnToPool(jedis);
        }
    }
    private <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }
}
