package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @program: mmall
 * @description: redis配置类
 * @author: xxxshi
 * @create: 2018-12-01 09:47
 * @Version:
 **/

public class Redis {

    private static JedisPool jedisPool;//jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.tatal", "20"));//连接池最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));//最大空闲实例数
    private static Integer mixIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.mix.idle", "10"));
    private static boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));//检查从连接池获得的实例是否可用
    private static boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));//检查放回连接池获得的实例是否可用
    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

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
        jedisPool = new JedisPool(jedisPoolConfig, redisIp, redisPort, 1000 * 2);

    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 资源放回连接池
     * @param jedis
     */
    public static void returnBrokenResource(Jedis jedis) {
        jedisPool.returnBrokenResource(jedis);

    }

    public static void returnResource(Jedis jedis) {
        jedisPool.returnResource(jedis);

    }

    public static void main(String[] args) {
        Jedis jedis = jedisPool.getResource();
        jedis.set("x","y");
        returnResource(jedis);

    }
}
