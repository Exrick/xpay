package cn.exrick.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Exrickx
 */
@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final Logger log= LoggerFactory.getLogger(RedisUtils.class);

    public String get(String key){

        String temp=null;
        try {
            temp=stringRedisTemplate.opsForValue().get(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return temp;
    }

    public void set(String key,String value,Long time,TimeUnit timeUnit){

        try {
            stringRedisTemplate.opsForValue().set(key,value,time, timeUnit);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
