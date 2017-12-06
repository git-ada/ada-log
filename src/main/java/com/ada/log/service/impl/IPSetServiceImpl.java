package com.ada.log.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ada.log.service.IPSetService;

@Service
public class IPSetServiceImpl implements IPSetService {
	
	@Value("${redis.host:}")
	private String host;
	@Value("${redis.port:}")
	private String port;
	@Value("${redis.pass:}")
	private String pass;
	
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	
	private JedisPool ipSetJedisPool;  /** , **/
	private Integer defualtDBindex =  8; /** 默认库 **/
	
	public void afterPropertiesSet() throws Exception {
		JedisPoolConfig config = new JedisPoolConfig();
		ipSetJedisPool = new JedisPool(jedisPoolConfig,host, Integer.valueOf(port), 1000, pass, defualtDBindex);
	}

	@Override
	public boolean exists(Integer domainId, String ipAddress) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean add(Integer domainId, String ipAddress) {
		// TODO Auto-generated method stub
		return false;
	}

}
