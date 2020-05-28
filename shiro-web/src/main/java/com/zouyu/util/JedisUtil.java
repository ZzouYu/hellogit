package com.zouyu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author zouyu
 * @description
 * @date 2019/5/10
 */
@Component
public class JedisUtil {
    @Autowired
    private JedisPool jedisPool;
    public Jedis getResource(){
        return jedisPool.getResource();
    }
    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        try {
          return  jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public void delete(byte[] key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }
    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = getResource();
        try {
           return jedis.keys((shiro_session_prefix+"*").getBytes());
        } finally {
            jedis.close();
        }
    }

    public void set(byte[] key, byte[] value) {
        Jedis jedis = getResource();
        try {
            jedis.set(key,value);
        } finally {
            jedis.close();
        }
    }

    public void expire(byte[] key, int expireTime) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key,expireTime);
        } finally {
            jedis.close();
        }
    }
}
