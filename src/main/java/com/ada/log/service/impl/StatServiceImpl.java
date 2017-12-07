package com.ada.log.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ada.log.bean.ChannelStat;
import com.ada.log.bean.DomainStat;
import com.ada.log.bean.SiteStat;
import com.ada.log.constant.RedisKeys;
import com.ada.log.service.JedisPools;
import com.ada.log.service.StatService;

@Service
@SuppressWarnings("all")
public class StatServiceImpl implements StatService{

	@Autowired
    private  JedisPools jedisPools;//自动切库,周一至周日每天一库

	/*测试*/
	public static void main(String[] args) throws Exception {
//		JedisPoolConfig config = new JedisPoolConfig();
//		
//		JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379, 20000, "g^h*123T", 2);
//		StatServiceImpl statServiceImpl = new StatServiceImpl();
		

		
	}

	public SiteStat statSite(Integer site, Date date) {
		Jedis jedis = getJedis(date);
		try {
			Integer sitePV = 0;
			//取出站点PV
			String _SitePV = jedis.get(RedisKeys.SitePV.getKey()+site+"");
			if(_SitePV != null) sitePV = Integer.valueOf(_SitePV);
			//取出站点IPSet集合
			int siteIP = jedis.scard(RedisKeys.SiteIP.getKey()+site+"").intValue();
			
			return new SiteStat(site, siteIP, sitePV, date);
		} finally{
			returnResource(date,jedis);
		}
		
	}
	
	public ChannelStat statChannel(Integer siteId, Integer channelId, Date date) {
		Jedis jedis = getJedis(date);
		try {
			Integer site_ChannelPV = 0;
			Integer clickip1 = 0;
			Integer clickip2 = 0;
			Integer clickip3 = 0;
			Integer clickip4 = 0;
			
			//取出渠道IPSet集合
			int channelIP = jedis.scard(RedisKeys.ChannelIP.getKey()+channelId+"").intValue();
			//取出渠道PV 
			String ChannelPV = jedis.get(RedisKeys.ChannelPV.getKey()+channelId+"");
			if(ChannelPV != null) site_ChannelPV = Integer.valueOf(ChannelPV);
			//取出渠道进入目标页IP集合
			int targetpageIP  = jedis.scard(RedisKeys.ChannelTIP.getKey()+channelId+"").intValue();
			//取出渠道多个点击区间次数
			String ChannelC1IP = jedis.get(RedisKeys.ChannelC1IP.getKey()+channelId+"");
			if(ChannelC1IP != null) clickip1 = Integer.valueOf(ChannelC1IP);
			
			String ChannelC2IP = jedis.get(RedisKeys.ChannelC2IP.getKey()+channelId+"");
			if(ChannelC2IP != null) clickip2 = Integer.valueOf(ChannelC2IP);
			
			String ChannelC3IP = jedis.get(RedisKeys.ChannelC3IP.getKey()+channelId+"");
			if(ChannelC3IP != null) clickip3 = Integer.valueOf(ChannelC3IP);
			
			String ChannelC4IP = jedis.get(RedisKeys.ChannelC4IP.getKey()+channelId+"");
			if(ChannelC4IP != null) clickip4 = Integer.valueOf(ChannelC4IP);
			
			return new ChannelStat(siteId, channelId, channelIP, site_ChannelPV, clickip1, clickip2, clickip3, clickip4, targetpageIP, date);
		} finally{
			returnResource(date,jedis);
		}
	}
	
	public DomainStat statDomain(Integer siteId, Integer domainId, Date date) {
		Jedis jedis = getJedis(date);
		try {
			Integer site_domainPV = 0;
			Integer clickip1 = 0;
			Integer clickip2 = 0;
			Integer clickip3 = 0;
			Integer clickip4 = 0;
			
			//取出域名IPSet集合
			int domainIP = jedis.scard(RedisKeys.DomainIP.getKey()+domainId+"").intValue();
			//取出域名PV 
			String domainPV = jedis.get(RedisKeys.DomainPV.getKey()+domainId+"");
			if(domainPV != null) site_domainPV = Integer.valueOf(domainPV);
			//取出域名进入目标页IP集合
			int targetpageIP  = jedis.scard(RedisKeys.DomainTIP.getKey()+domainId+"").intValue();
			//取出域名多个点击区间次数
			String domainC1IP = jedis.get(RedisKeys.DomainC1IP.getKey()+domainId+"");
			if(domainC1IP != null) clickip1 = Integer.valueOf(domainC1IP);
			
			String domainC2IP = jedis.get(RedisKeys.DomainC2IP.getKey()+domainId+"");
			if(domainC2IP != null) clickip2 = Integer.valueOf(domainC2IP);
			
			String domainC3IP = jedis.get(RedisKeys.DomainC3IP.getKey()+domainId+"");
			if(domainC3IP != null) clickip3 = Integer.valueOf(domainC3IP);
			
			String domainC4IP = jedis.get(RedisKeys.DomainC4IP.getKey()+domainId+"");
			if(domainC4IP != null) clickip4 = Integer.valueOf(domainC4IP);
			return new DomainStat(siteId, domainId, domainIP, site_domainPV, clickip1, clickip2, clickip3, clickip4, targetpageIP, date);
		} finally{
			returnResource(date,jedis);
		}
	}

	protected Jedis getJedis(Date date){	
		return jedisPools.getResource(date.getDay());
	}
	
	public void returnResource(Date date,Jedis jedis) {
		jedisPools.returnResource(date.getDay(),jedis);
    }
}
