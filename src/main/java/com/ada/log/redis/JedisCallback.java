package com.ada.log.redis;

import redis.clients.jedis.Jedis;

public interface JedisCallback<T> {

	public T doInRedis(Jedis jedis);
}
