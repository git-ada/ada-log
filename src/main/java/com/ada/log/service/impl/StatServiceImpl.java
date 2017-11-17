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
		//statServiceImpl.setJedisPool(jedisPool);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = df.parse("2017-11-15");
		
		//SiteStat statChannel = statServiceImpl.statSite(2, dt);
		ChannelStat channelStat = statServiceImpl.statChannel(2, 2, dt);
		System.out.println("channelPV:  ==> "+channelStat.getPv()+"channelIP:  ==> "+channelStat.getIp());
		
	}

	public SiteStat statSite(Integer site, Date date) {
		Jedis jedis = getJedis(date);
		//取出站点PV
		Integer sitePV = Integer.valueOf(jedis.get("SitePV_"+site+""));
		//取出站点IPSet集合
		Set<String> siteIPSet = jedis.smembers("SiteIP_"+site+"");
		int siteIP = siteIPSet.size();

		return new SiteStat(site, siteIP, sitePV, date);
	}
	
	public ChannelStat statChannel(Integer siteId, Integer channelId, Date date) {
		Jedis jedis = getJedis(date);
		//取出渠道IPSet集合
		Set<String> channelIPSet = jedis.smembers("ChannelIP_"+channelId+"");
		int channelIP = channelIPSet.size();
		//取出渠道PV 
		Integer sitePV = Integer.valueOf(jedis.get("ChannelPV_"+channelId+""));
		//取出渠道进入目标页IP集合
		Set<String> targetpageipSet = jedis.smembers("ChannelTIP_"+channelId+"");
		int targetpageIP = targetpageipSet.size();
		//取出渠道多个点击区间次数
		Integer clickip1 = Integer.valueOf(jedis.get("ChannelC1IP_"+channelId+""));
		Integer clickip2 = Integer.valueOf(jedis.get("ChannelC2IP_"+channelId+""));
		Integer clickip3 = Integer.valueOf(jedis.get("ChannelC3IP_"+channelId+""));
		Integer clickip4 = Integer.valueOf(jedis.get("ChannelC4IP_"+channelId+""));
		return new ChannelStat(siteId, channelId, channelIP, sitePV, clickip1, clickip2, clickip3, clickip4, targetpageIP, date);
	}

	protected Jedis getJedis(Date date){
		return jedisPools.getResource(date.getDay());
	}
}
