package com.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/10/23.
 */
@Service
@Slf4j
public class RedisTempleteUtil {
    @Resource(name="longRedisTemplate")
    private RedisTemplate redisTemplate;

    ValueOperations<String, String> valueops;

    @PostConstruct
    void init() {
        valueops = redisTemplate.opsForValue();
    }


    /**
     * 获取redis的值
     * @param redisKey
     * @return
     */
    public String get(String redisKey){
        return valueops.get(redisKey);
    }

    /**
     * 获取long类型的redis值
     * @param redisKey
     * @return
     */
    public long getLong(String redisKey){
        String key = valueops.get(redisKey);
        return Long.parseLong(key == null?"0":key);
    }

    /**
     * 获取int类型的redis值
     * @param redisKey
     * @return
     */
    public int getInt(String redisKey){
        String key = valueops.get(redisKey);
        return Integer.parseInt(key == null ? "0" : key);
    }

    /**
     * set redis值
     * @param redisKey
     * @param value
     */
    public void set(String redisKey,String value){
        valueops.set(redisKey, value);
    }

    public void  setBySecond(String redisKey,String valueKey,long time){
        valueops.set(redisKey, valueKey, time, TimeUnit.SECONDS);
    }

    /**
     * 自增count
     * @param key
     * @param count
     * @return
     */
    public Long increment(String key,long count){
        log.info("key={}自增{}",key,count);
        return valueops.increment(key, count);
    }


    /**
     * 自增1并设置超时时间
     * @param key
     * @param timeOut
     * @param timeUnit
     * @return
     */
    public Long incrOneByExpire(String key,Long timeOut,TimeUnit timeUnit){
        Long increment = valueops.increment(key, 1L);
        redisTemplate.expire(key,timeOut,timeUnit);
        log.info("key={},设置超时时间{},自增1",key,timeOut);
        return increment;

    }

    /**
     * 值自增1
     * @param key
     * @return
     */
    public Long incrOne(String key){
        log.info("key={}自增1",key);
        return valueops.increment(key,1L);
    }

    public boolean expireAtNextDay(String key, Date date){
        return redisTemplate.expireAt(key, date);
    }

}
