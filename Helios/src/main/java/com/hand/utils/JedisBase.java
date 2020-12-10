package com.hand.utils;

import com.hand.baseMethod.ThreadLocalUtil;
import com.hand.basicConstant.BaseConstant;
import lombok.extern.slf4j.Slf4j;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;

/**
 * @Author peng.zhang
 * @Date 2019/11/4
 * @Version 1.0
 **/

@Slf4j
public class JedisBase {
    private String redisIp;
    private int redisPort;
    private String redisPassword;
    private JedisPoolConfig jedisPoolConfig;
    private JedisPool jedisPool;
    private Jedis jedis;
    private int jedisPoolMaxTotal;
    private int jedisPoolMaxIdle;
    private int jedisPoolMaxWaitMillis;
    private Boolean jedisPoolTestOnBorrow;
    private Boolean jedisPoolTestOnReturn;
    private String redisIpKey = "redis.ip";
    private String redisPortKey = "redis.port";
    private String redisPasswordKey = "redis.password";
    private String jedisPoolMaxTotalKey = "jedis.pool.maxTotal";
    private String jedisPoolMaxIdleKey = "jedis.pool.maxIdle";
    private String jedisPoolMaxWaitMillsKey = "jedis.pool.maxWaitMillis";
    private String jedisPoolTestOnBorrowKey = "jedis.pool.testOnBorrow";
    private String jedisPoolTestOnReturnKey = "jedis.pool.testOnReturn";

    private static ThreadLocalUtil<Jedis> jedisThreadLocalUtil = new ThreadLocalUtil<Jedis>();
    private static ThreadLocal<Jedis> threadJedis = new ThreadLocal<Jedis>();

    // 根据读取的配置文件，给变量赋值
    public void setValue() throws IOException {
        this.redisIp = PropertyReader.getValue( redisIpKey );
        this.redisPort = Integer.valueOf( PropertyReader.getValue( redisPortKey ));
        this.redisPassword = PropertyReader.getValue( redisPasswordKey );
        this.jedisPoolMaxTotal = Integer.valueOf( PropertyReader.getValue( jedisPoolMaxTotalKey ) );
        this.jedisPoolMaxIdle = Integer.valueOf( PropertyReader.getValue( jedisPoolMaxIdleKey ));
        this.jedisPoolMaxWaitMillis = Integer.valueOf( PropertyReader.getValue( jedisPoolMaxWaitMillsKey ) );
        this.jedisPoolTestOnBorrow = Boolean.valueOf( PropertyReader.getValue( jedisPoolTestOnBorrowKey ) );
        this.jedisPoolTestOnReturn = Boolean.valueOf( PropertyReader.getValue( jedisPoolTestOnReturnKey ) );
    }

    // 获得JedisPool对象
    public JedisPool getJedisPool() throws Exception{
        setValue();
        jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal( jedisPoolMaxTotal );
        jedisPoolConfig.setMaxIdle( jedisPoolMaxIdle );
        jedisPoolConfig.setMaxWaitMillis( jedisPoolMaxWaitMillis );
        jedisPoolConfig.setTestOnBorrow( jedisPoolTestOnBorrow );
        jedisPoolConfig.setTestOnReturn( jedisPoolTestOnReturn );
        createJedisPool();
        return jedisPool;
    }

    // 创建JedisPool
    public JedisPool createJedisPool(){
        try {
            if(this.redisPassword.equals( "" ) || this.redisPassword == null){
                // 无需密码连接
                jedisPool = new JedisPool( jedisPoolConfig, redisIp, redisPort , BaseConstant.THREE_THOUSANG);
            }else{
                // 需要密码连接
                jedisPool = new JedisPool( jedisPoolConfig, redisIp, redisPort , BaseConstant.TEN_THOUSANG, redisPassword);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("创建JedisPool失败");
        }
        return jedisPool;
    }

    public void setJedisThread(){
        try {
            jedisThreadLocalUtil.setThreadValue( threadJedis, jedisPool.getResource());
            log.info("成功连接到redis服务器");
        }catch (Exception e){
            e.printStackTrace();
            log.info("连接redis服务器失败");
        }
    }

    // 获得jedis
    public Jedis getJedis() {
        return jedisThreadLocalUtil.getThreadValue( threadJedis );
    }

    // 设置jedis
    public void setJedis(Jedis jedis){
        this.jedis = jedis;
    }

    // 设置key对应的value值以及key的过期时间10min
    public String getKey(String key){
        return getJedis().get( key );
    }

    // 设置key对应的value值以及key的过期时间10min
    public void setKey(String key, String value){
        getJedis().set( key, value );
        getJedis().expire( key, 18000);
    }
    //删除所有key
    public void delete(){
       getJedis().flushAll();
    }

    // 归还jedis对象
    public void returnJedis(){
        setJedis(getJedis());
        if (jedis != null && jedisPool != null){
            jedis.close();
            log.info("成功归还jedis对象");
            threadJedis.remove();
        }
    }
}
