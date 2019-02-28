package com.mmall.util;

import com.mmall.common.Redis;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @program: mmall
 * @description: redisPool工具类，已废弃
 * @author: xxxshi
 * @create: 2018-12-02 08:23
 * @Version: 1.0
 **/
@Slf4j
public class RedisPoolUtil {

    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = Redis.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key {} value {} ", key, value, e);
            Redis.returnBrokenResource(jedis);
            return result;
        }

        Redis.returnResource(jedis);
        return result;
    }

    public static String setEx(String key, String value,int exTime) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = Redis.getJedis();
            result = jedis.setex(key, exTime,value);
        } catch (Exception e) {
            log.error("set key{} value{} exTime{}", key, value,exTime, e);
            Redis.returnBrokenResource(jedis);
            return result;
        }

        Redis.returnResource(jedis);
        return result;
    }

    public static String get(String key) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = Redis.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key {} ", key,  e);
            Redis.returnBrokenResource(jedis);
            return result;
        }

        Redis.returnResource(jedis);
        return result;
    }

    /**
     * 设置已存在的key的存在时间，单位：秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime) {
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = Redis.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("set key{} exTime{} ", key, exTime,e);
            Redis.returnBrokenResource(jedis);
            return result;
        }

        Redis.returnResource(jedis);
        return result;
    }

    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = Redis.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("del key{} ", key,  e);
            Redis.returnBrokenResource(jedis);
            return result;
        }

        Redis.returnResource(jedis);
        return result;
    }




}
