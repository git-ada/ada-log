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
import com.ada.log.bean.SiteStat;
import com.ada.log.service.JedisPools;
import com.ada.log.service.StatService;

@Service
public class StatServiceImpl implements StatService{

	@Autowired
    private  JedisPools jedisPools;//自动切库,周一至周日每天一库

	/*测试*/
	public static void main(String[] args) throws Exception {
		JedisPoolConfig config = new JedisPoolConfig();
		
		JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379, 20000, "g^h*123T", 2);
		StatServiceImpl statServiceImpl = new StatServiceImpl();
//		statServiceImpl.setJedisPool(jedisPool);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = df.parse("2017-11-15");
		
		SiteStat statChannel = statServiceImpl.statSite(2, dt);
//		ChannelStat channelStat = statServiceImpl.statChannel(2, 2, dt);
		System.out.println("channelPV:  ==> "+statChannel.getPv()+"channelIP:  ==> "+statChannel.getIp());
		
	}

	public SiteStat statSite(Integer site, Date date) {
		Jedis jedis = getJedis(date);
		Integer sitePV = 0;
		//取出站点PV
		String _SitePV = jedis.get("SitePV_"+site+"");
		if(_SitePV != null) sitePV = Integer.valueOf(_SitePV);
		//取出站点IPSet集合
		int siteIP = jedis.scard("SiteIP_"+site+"").intValue();
		return new SiteStat(site, siteIP, sitePV, date);
	}
	
	public ChannelStat statChannel(Integer siteId, Integer channelId, Date date) {
		Jedis jedis = getJedis(date);
		Integer sitePV = 0;
		Integer clickip1 = 0;
		Integer clickip2 = 0;
		Integer clickip3 = 0;
		Integer clickip4 = 0;
		
		//取出渠道IPSet集合
		int channelIP = jedis.scard("ChannelIP_"+channelId+"").intValue();
		//取出渠道PV 
		String ChannelPV = jedis.get("ChannelPV_"+channelId+"");
		if(ChannelPV != null) sitePV = Integer.valueOf(ChannelPV);
		//取出渠道进入目标页IP集合
		int targetpageIP  = jedis.scard("ChannelTIP_"+channelId+"").intValue();
		//取出渠道多个点击区间次数
		String ChannelC1IP = jedis.get("ChannelC1IP_"+channelId+"");
		if(ChannelC1IP != null) clickip1 = Integer.valueOf(ChannelC1IP);
		
		String ChannelC2IP = jedis.get("ChannelC2IP_"+channelId+"");
		if(ChannelC2IP != null) clickip2 = Integer.valueOf(ChannelC2IP);
		
		String ChannelC3IP = jedis.get("ChannelC3IP_"+channelId+"");
		if(ChannelC3IP != null) clickip3 = Integer.valueOf(ChannelC3IP);
		
		String ChannelC4IP = jedis.get("ChannelC4IP_"+channelId+"");
		if(ChannelC4IP != null) clickip4 = Integer.valueOf(ChannelC4IP);
		
		return new ChannelStat(siteId, channelId, channelIP, sitePV, clickip1, clickip2, clickip3, clickip4, targetpageIP, date);
	}

	protected Jedis getJedis(Date date){	
		return jedisPools.getResource(date.getDay());
	}
	
	public void returnResource(Date date,Jedis jedis) {
		jedisPools.returnResource(date.getDay(),jedis);
    }
}
