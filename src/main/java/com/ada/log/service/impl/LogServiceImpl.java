package com.ada.log.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ada.log.service.LogService;

/**
 * 核心实现
 *
 */
@Service

public class LogServiceImpl implements LogService{
	@Autowired
    private  JedisPool jedisPool;//非切片连接池
    
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 记录日志
	 * @param ipAddress     IP地址
	 * @param uuid          客户端UUID
	 * @param siteId        站点ID
	 * @param channelId     渠道ID
	 * @param clickNum      点击次数
	 * @param browsingTime  浏览时间,精确到毫秒
	 * @param browsingPage  当前页面链接
	 */
	public void log(String ipAddress,String uuid, Integer siteId, Integer channelId,String clickNum, String browsingTime, String browsingPage) {
		
		
		
	}

	/**
	 * 保存站点IPSet集合
	 * @param siteId       站点ID
	 * @param ipAddress    IP地址
	 */
	protected void putSiteIPSet(Integer siteId,String ipAddress) {
		Jedis jedis = jedisPool.getResource();
		jedis.sadd("SiteIP_"+siteId+"", ipAddress);
	}
	
	/**
	 * 保存站点PV +1
	 * @param siteId       站点ID
	 * @param ipAddress
	 */
	protected void increSitePV(Integer siteId) {
		Jedis jedis = jedisPool.getResource();
		jedis.incr("SitePV_"+siteId+"");
	}
	
	/**
	 * 保存渠道IPSet集合
	 * @param channelId    渠道ID
	 * @param ipAddress    IP地址
	 */
	protected void putChannelIPSet(Integer channelId,String ipAddress){
		Jedis jedis = jedisPool.getResource();
		jedis.sadd("ChannelIP_"+channelId+"", ipAddress);
	}
	
	/**
	 * 保存渠道PV +1
	 * @param channelId       渠道ID
	 * @param ipAddress    IP地址
	 */
	protected void increChannelPV(Integer channelId) {
		Jedis jedis = jedisPool.getResource();
		jedis.incr("ChannelPV_"+channelId+"");	
	}
	
	/**
	 * 
	 * @param ipAddress   IP地址
	 * @param clickNum    页面点击次数
	 * @return
	 */
	protected Integer increIPClickNum(String ipAddress,Integer pageClickNum){
		Jedis jedis = jedisPool.getResource();
		Long pageClickTotal = jedis.incrBy("CIPNum_"+ipAddress, pageClickNum);
		int pageClickTotal2 = Integer.parseInt(String.valueOf(pageClickTotal)); 
		return pageClickTotal2;
	}
	
	/**
	 * 更新渠道多个点击次数区间
	 * @param channelId   渠道ID
	 * @param ipClickNum  IP累计点击次数
	 */
	protected void updateChannelMultipleRangeClickIP(Integer channelId,Integer ipClickNum){
		Jedis jedis = jedisPool.getResource();
		
	}
	
	/**
	 * 匹配是否目标页
	 * @param siteId
	 * @param browsingPage 
	 * @return
	 */
	protected boolean matchTargetPage(Integer siteId,String browsingPage){
		String[] split = browsingPage.split("\\?");
		
		if(split[0].contains("目标页面")){
			return true;
		}
		
		
		return false;
	}
	/*测试*/
	public static void main(String[] args) {
		JedisPoolConfig config = new JedisPoolConfig();
		
		JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379, 20000, "g^h*123T", 2);
		LogServiceImpl logServiceImpl = new LogServiceImpl();
		logServiceImpl.setJedisPool(jedisPool);
		
		String s= "http://sss.com?t=9";
		String[] split = s.split("\\?");
		String string = split[0];
		System.out.println(string);
		
	}
	
	
	/**
	 * 
	 * 渠道进入目标页IP集合
	 * @param channelId
	 */
	protected void putChannelTIPSet(Integer channelId,String ipAddress){
		Jedis jedis = jedisPool.getResource();
		jedis.sadd("ChannelTIP_"+channelId, ipAddress);
	}
	
	
	

}
