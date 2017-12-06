package com.ada.log.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ada.log.constant.RedisKeys;
import com.ada.log.service.IPSetService;
import com.ada.log.service.LogService;
import com.ada.log.util.Dates;

@Service
public class IPSetServiceImpl implements IPSetService {
	private final static Log log = LogFactory.getLog(IPSetServiceImpl.class);
	@Value("${redis.host:}")
	private String host;
	@Value("${redis.port:}")
	private String port;
	@Value("${redis.pass:}")
	private String pass;
	
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	
	@Autowired
	private LogService logService;
	
	private JedisPool ipSetJedisPool;  /** , **/
	private Integer defualtDBindex =  8; /** 默认库 **/
	
	public JedisPool afterPropertiesSet() throws Exception {
		JedisPoolConfig config = new JedisPoolConfig();
		ipSetJedisPool = new JedisPool(jedisPoolConfig,host, Integer.valueOf(port), 1000, pass, defualtDBindex);
		return ipSetJedisPool;
	}

	@Override
	public boolean exists(Integer domainId,String ipAddress) {
		Jedis jedis = getJedis();
		Boolean exDomain = jedis.hexists(RedisKeys.DomainIPMap.getKey()+domainId+"", ipAddress);
		if(exDomain){
			Map<String, String> allIp = jedis.hgetAll(RedisKeys.DomainIPMap.getKey()+domainId+"");
			returnResource(jedis);
			if(allIp.containsKey(ipAddress)){
            	return true;
            }else{
            	return false;
            }
		}
		returnResource(jedis);
		return false;
	}
	
	@Override
	public boolean add(Integer domainId) {
		Jedis jedis = getJedis();
		Set<String> ipSet = logService.loopDomainIPSet(domainId);
		if(ipSet !=null && ipSet.size()>0){
			for (String ip : ipSet) {  
				boolean exIpAddress = exists(domainId, ip);
				if(!exIpAddress){
					jedis.hset(RedisKeys.DomainIPMap.getKey()+domainId+"", ip, String.valueOf(System.currentTimeMillis()));
				}
			} 
		} 
		returnResource(jedis);
		return false;
	}
	
	
	protected Jedis getJedis()  {  
		Jedis jedis = null; 
		try {
			ipSetJedisPool = afterPropertiesSet();
			jedis = ipSetJedisPool.getResource(); 
		} catch (Exception e) {
			log.error("get jedis resource error !");
		}  
	    return jedis;
	}  
	protected void returnResource(Jedis jedis) {  
		 if (jedis != null) {  
	         ipSetJedisPool.returnResource(jedis);
	    }   
	} 
	

}
