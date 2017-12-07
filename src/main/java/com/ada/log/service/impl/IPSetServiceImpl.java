package com.ada.log.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
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
public class IPSetServiceImpl implements IPSetService,InitializingBean {
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
	
	public void afterPropertiesSet() throws Exception {
		ipSetJedisPool = new JedisPool(jedisPoolConfig,host, Integer.valueOf(port), 1000, pass, defualtDBindex);
	}

	@Override
	public boolean exists(Integer domainId,String ipAddress) {
		Jedis jedis = getJedis();
		try{
			boolean exists = jedis.hexists(RedisKeys.DomainIPMap.getKey()+domainId+"", ipAddress);
			return exists;
		} finally{
			returnResource(jedis);
		}
	}
	
	@Override
	public void add(Integer domainId,String ipAddress) {
		
	}
	
	/**
	 * 批量添加IP数据集
	 * @param domianId
	 * @param ipSet
	 * @return
	 */
	@Override
	public void batchAdd(Integer domianId,Set<String> ipSet){
		Jedis jedis = getJedis();
		try {
			if(ipSet !=null && ipSet.size()>0){
				for (String ip : ipSet) {  
					boolean exIpAddress = exists(domianId, ip);
					if(!exIpAddress){
						jedis.hset(RedisKeys.DomainIPMap.getKey()+domianId+"", ip, String.valueOf(System.currentTimeMillis()));
					}
				} 
			} 
		} finally{
			returnResource(jedis);
		}
	}
	
	
	protected Jedis getJedis(){
		return ipSetJedisPool.getResource();
	} 
	protected void returnResource(Jedis jedis) {  
		 if (jedis != null) {  
	         ipSetJedisPool.returnResource(jedis);
	    }   
	} 
	

}
