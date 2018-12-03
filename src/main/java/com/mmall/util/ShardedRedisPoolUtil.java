package com.mmall.util;

import com.mmall.common.Redis;
import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * @program: mmall
 * @description: redisPool工具类
 * @author: xxxshi
 * @create: 2018-12-02 08:23
 * @Version: 1.0
 **/
@Slf4j
public class ShardedRedisPoolUtil {

    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set key {} value {} ", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }

        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String setEx(String key, String value,int exTime) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key, exTime,value);
        } catch (Exception e) {
            log.error("set key{} value{} exTime{}", key, value,exTime, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }

        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String get(String key) {
        ShardedJedis ShardJedis = null;
        String result = null;

        try {
            ShardJedis = RedisShardedPool.getJedis();
            result = ShardJedis.get(key);
        } catch (Exception e) {
            log.error("get key {} ", key,  e);
            RedisShardedPool.returnBrokenResource(ShardJedis);
            return result;
        }

        RedisShardedPool.returnResource(ShardJedis);
        return result;
    }

    /**
     * 设置已存在的key的存在时间，单位：秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime) {
        ShardedJedis ShardJedis = null;
        Long result = null;

        try {
            ShardJedis = RedisShardedPool.getJedis();
            result = ShardJedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("set key{} exTime{} ", key, exTime,e);
            RedisShardedPool.returnBrokenResource(ShardJedis);
            return result;
        }

        RedisShardedPool.returnResource(ShardJedis);
        return result;
    }

    public static Long del(String key) {
        ShardedJedis ShardJedis = null;
        Long result = null;

        try {
            ShardJedis = RedisShardedPool.getJedis();
            result = ShardJedis.del(key);
        } catch (Exception e) {
            log.error("del key{} ", key,  e);
            RedisShardedPool.returnBrokenResource(ShardJedis);
            return result;
        }

        RedisShardedPool.returnResource(ShardJedis);
        return result;
    }


    public static void main(String[] args) {

        for (int i = 0; i <10 ; i++) {
            ShardedRedisPoolUtil.set("key" + i, ""+i);
        }

    }

}
