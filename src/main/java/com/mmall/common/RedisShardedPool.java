package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: mmall
 * @description: 分布式redis
 * @author: xxxshi
 * @create: 2018-12-03 21:18
 * @Version:
 **/

public class RedisShardedPool {
    private static ShardedJedisPool shardedJedisPool;//jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.tatal", "20"));//连接池最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));//最大空闲实例数
    private static Integer mixIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.mix.idle", "10"));
    private static boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));//检查从连接池获得的实例是否可用
    private static boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));//检查放回连接池获得的实例是否可用

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    static{
        //初始化
        initPool();
    }

    private static void initPool() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(mixIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPoolConfig.setBlockWhenExhausted(true);
        //连接耗尽时，false：抛出异常；true：阻塞直到超时
        JedisShardInfo info1 = new JedisShardInfo(redis1Ip, redis1Port, 1000 * 2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip, redis2Port, 1000 * 2);
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>();
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);
        shardedJedisPool = new ShardedJedisPool(jedisPoolConfig,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    public static ShardedJedis getJedis() {
        return shardedJedisPool.getResource();
    }

    /**
     * 资源放回连接池
     * @param jedis
     */
    public static void returnBrokenResource(ShardedJedis jedis) {
        shardedJedisPool.returnBrokenResource(jedis);

    }

    public static void returnResource(ShardedJedis jedis) {
        shardedJedisPool.returnResource(jedis);

    }

    public static void main(String[] args) {
        ShardedJedis jedis = shardedJedisPool.getResource();
        jedis.set("x","y");
        returnResource(jedis);

    }
}
