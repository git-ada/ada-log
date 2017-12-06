package com.ada.log.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.ada.log.constant.RedisKeys;
import com.ada.log.service.JedisPools;

@Service
public class LoginEventHandle {
	
	private final static Log log = LogFactory.getLog(LoginEventHandle.class);
	
	@Autowired
    private  JedisPools jedisPools;

	public void handle(
			String ipAddress,
			String uuid,
			Integer siteId, 
			Integer channelId,
			Integer domainId,
			String region,
			Integer adId) {
		
		if(log.isDebugEnabled()){
			log.debug("ip->"+ipAddress+",siteId->"+siteId+",channelId->"+channelId+",domainId->"+domainId+",region->"+region+",adid");
		}
		
		Jedis jedis = jedisPools.getResource();
		
		jedis.sadd(new StringBuffer().append(RedisKeys.DomainLoginIp.getKey()).append(domainId).toString(), ipAddress);
		jedis.sadd(new StringBuffer().append(RedisKeys.DomainCityLoginIp.getKey()).append(region).toString(), ipAddress);
		
		if(adId!=null){
			jedis.sadd(new StringBuffer().append(RedisKeys.DomainAdLoginIp.getKey()).append(domainId).toString(), ipAddress);
			jedis.sadd(new StringBuffer().append(RedisKeys.DomainAdCityLoginIp.getKey()).append(region).toString(), ipAddress);
		}
		
		//TODO渠道
		
		jedisPools.returnResource(jedis);
	}

}
