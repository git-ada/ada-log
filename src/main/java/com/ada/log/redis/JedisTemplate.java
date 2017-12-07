package com.ada.log.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisTemplate {
	
	public static <T> T execute(JedisPool jedisPool,JedisCallback<T> callback){
		Jedis jedis = jedisPool.getResource();
		try {
			return callback.doInRedis(jedis);
		} finally{
			jedisPool.returnResource(jedis);
		}
	}
}
