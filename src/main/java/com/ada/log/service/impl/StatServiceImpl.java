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
		int siteIP = 0;
		//取出站点PV
		Boolean CHCKSitePVExists = jedis.exists("SitePV_"+site+"");
		if(true == CHCKSitePVExists){
			sitePV = Integer.valueOf(jedis.get("SitePV_"+site+""));
		}
		//取出站点IPSet集合
		Boolean checkSiteIPExists = jedis.exists("SiteIP_"+site+"");
		if(true == checkSiteIPExists){
			siteIP = jedis.scard("SiteIP_"+site+"").intValue();
		}
		return new SiteStat(site, siteIP, sitePV, date);
	}
	
	public ChannelStat statChannel(Integer siteId, Integer channelId, Date date) {
		Jedis jedis = getJedis(date);
		Integer sitePV = 0;
		int channelIP = 0;
		int targetpageIP = 0;
		Integer clickip1 = 0;
		Integer clickip2 = 0;
		Integer clickip3 = 0;
		Integer clickip4 = 0;
		//取出渠道IPSet集合
		Boolean checkChannelTIPExists = jedis.exists("ChannelIP_"+channelId+"");
		if(true == checkChannelTIPExists){
			channelIP = jedis.scard("ChannelIP_"+channelId+"").intValue();
		}
		//取出渠道PV 
		Boolean checkChannelPVExists = jedis.exists("ChannelPV_"+channelId+"");
		if(true == checkChannelPVExists){
			sitePV = Integer.valueOf(jedis.get("ChannelPV_"+channelId+""));
		}
		//取出渠道进入目标页IP集合
		Boolean checkTargetpageIPExists = jedis.exists("ChannelTIP_"+channelId+"");
		if(true == checkTargetpageIPExists){
			targetpageIP = jedis.scard("ChannelTIP_"+channelId+"").intValue();
		}
		//取出渠道多个点击区间次数
		Boolean checkclickip1Exists = jedis.exists("ChannelC1IP_"+channelId+"");
		if(true == checkclickip1Exists){
			clickip1 = Integer.valueOf(jedis.get("ChannelC1IP_"+channelId+""));
		}
		Boolean checkclickip2Exists = jedis.exists("ChannelC2IP_"+channelId+"");
		if(true == checkclickip2Exists){
			clickip1 = Integer.valueOf(jedis.get("ChannelC2IP_"+channelId+""));
		}
		Boolean checkclickip3Exists = jedis.exists("ChannelC3IP_"+channelId+"");
		if(true == checkclickip3Exists){
			clickip1 = Integer.valueOf(jedis.get("ChannelC3IP_"+channelId+""));
		}
		Boolean checkclickip4Exists = jedis.exists("ChanneLC4IP_"+channelId+"");
		if(true == checkclickip4Exists){
			clickip1 = Integer.valueOf(jedis.get("ChannelC4IP_"+channelId+""));
		}
		return new ChannelStat(siteId, channelId, channelIP, sitePV, clickip1, clickip2, clickip3, clickip4, targetpageIP, date);
	}

	protected Jedis getJedis(Date date){	
		return jedisPools.getResource(date.getDay());
	}
}
