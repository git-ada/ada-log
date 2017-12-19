package com.ada.log.service.impl;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.ada.log.bean.Entrance;
import com.ada.log.redis.JedisCallback;
import com.ada.log.redis.JedisTemplate;
import com.ada.log.redis.key.EntranceKey;
import com.ada.log.service.EntranceService;
import com.ada.log.service.JedisPools;
import com.ada.log.util.Dates;
import com.ada.log.util.IPv4;
import com.alibaba.fastjson.JSON;

@Service
public class EntranceServiceImpl implements EntranceService{

	@Autowired
    private  JedisPools jedisPools;

	@Override
	public Entrance getAndSetEntrance(final String ip,final  Integer domainId,final Integer adId, final String page) {
		return JedisTemplate.execute(jedisPools.getJedisPool() , new JedisCallback<Entrance>(){
			public Entrance doInRedis(Jedis jedis) {
				EntranceKey key = new EntranceKey(IPv4.toInt(ip),domainId);
				byte[] j = key.toBytes();
				byte[] b = jedis.get(j);
				try {
					if(b != null){
						Entrance e = JSON.parseObject(new String(b, "utf-8"), Entrance.class);
						return e;
					}else{
						Entrance e = new Entrance();
						e.setAdId(adId);
						e.setPage(page);
						e.setTime(Dates.now());
						String json = JSON.toJSONString(e);
						jedis.set(j, json.getBytes("utf-8"));
						return e;
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

}
