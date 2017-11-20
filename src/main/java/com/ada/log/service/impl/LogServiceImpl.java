package com.ada.log.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.ada.log.constant.RedisKeys;
import com.ada.log.service.JedisPools;
import com.ada.log.service.LogService;
import com.ada.log.service.SiteService;

/**
 * 核心实现
 *
 */
@Service

public class LogServiceImpl implements LogService{

	@Autowired
    private  JedisPools jedisPools;
	
	@Autowired
	private SiteService siteService;
	
	private final static Log log = LogFactory.getLog(LogServiceImpl.class);

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
	public void log(String ipAddress,String uuid, Integer siteId, Integer channelId,Integer clickNum, Integer browsingTime, String browsingPage) {
		/** 1）保存站点IP Set **/
		putSiteIPSet(siteId, ipAddress);
		/** 2）保存站点PV **/
		increSitePV(siteId);
		if(channelId!=null){
			/** 3) 保存渠道IP Set **/
			putChannelIPSet(channelId, ipAddress);
			/** 4) 保存渠道PV Set **/
			increChannelPV(channelId);
			/** 5) 保存IP鼠标点击次数 **/
			Integer newClickNum = increIPClickNum(ipAddress, clickNum);
			Integer oldClickNum = newClickNum - clickNum;
			/** 6) 更新渠道点击IP数 **/
			updateChannelClickIP(channelId, newClickNum, oldClickNum);
			/** 7) 保存渠道进入目标页IPSet**/
			if(siteService.matchTargetPage(siteId, browsingPage)){
				putChannelTIPSet(channelId, ipAddress);
			}
		}
	}
	
	@Override
	public void log1(String ipAddress, String uuid, Integer siteId,Integer channelId, String browsingPage) {
		/** 1）保存站点IP Set **/
		putSiteIPSet(siteId, ipAddress);
		/** 2）保存站点PV **/
		increSitePV(siteId);
		if(channelId!=null){
			/** 3) 保存渠道IP Set **/
			putChannelIPSet(channelId, ipAddress);
			/** 4) 保存渠道PV Set **/
			increChannelPV(channelId);
			/** 7) 保存渠道进入目标页IPSet**/
			if(siteService.matchTargetPage(siteId, browsingPage)){
				putChannelTIPSet(channelId, ipAddress);
			}
		}
	}

	@Override
	public void log2(String ipAddress, String uuid, Integer siteId,Integer channelId, Integer clickNum) {
		if(log.isDebugEnabled()){
			log.debug("ip->"+ipAddress+",siteId->"+siteId+",channelId->"+channelId+",clickNum->"+clickNum);
		}
		
		
		if(channelId!=null){
			/** 6) 更新渠道点击IP数 **/
			Integer oldClickNum = getAndSetIPClickNum(ipAddress,clickNum);
			if(log.isDebugEnabled()){
				log.debug("oldClickNum->"+clickNum);
			}
			updateChannelClickIP(channelId, clickNum, oldClickNum);
		}
	}

	/**
	 * 保存站点IPSet集合
	 * @param siteId       站点ID
	 * @param ipAddress    IP地址
	 */
	protected void putSiteIPSet(Integer siteId,String ipAddress) {
		Jedis jedis = getJedis();
		jedis.sadd("SiteIP_"+siteId+"", ipAddress);
		returnResource(jedis);
	}
	
	/**
	 * 保存站点PV +1
	 * @param siteId       站点ID
	 * @param ipAddress
	 */
	protected void increSitePV(Integer siteId) {
		Jedis jedis = getJedis();
		jedis.incr("SitePV_"+siteId+"");
		returnResource(jedis);
	}
	
	/**
	 * 保存渠道IPSet集合
	 * @param channelId    渠道ID
	 * @param ipAddress    IP地址
	 */
	protected void putChannelIPSet(Integer channelId,String ipAddress){
		Jedis jedis = getJedis();
		jedis.sadd("ChannelIP_"+channelId+"", ipAddress);
		returnResource(jedis);
	}
	
	/**
	 * 保存渠道PV +1
	 * @param channelId       渠道ID
	 * @param ipAddress    IP地址
	 */
	protected void increChannelPV(Integer channelId) {
		Jedis jedis = getJedis();
		jedis.incr("ChannelPV_"+channelId+"");	
		returnResource(jedis);
	}
	
	/**
	 * 
	 * @param ipAddress   IP地址
	 * @param clickNum    页面点击次数
	 * @return
	 */
	protected Integer increIPClickNum(String ipAddress,Integer pageClickNum){
		Jedis jedis = getJedis();
		Long pageClickTotal = jedis.incrBy("CIPNum_"+ipAddress, pageClickNum);
		returnResource(jedis);
		int pageClickTotal2 = Integer.parseInt(String.valueOf(pageClickTotal)); 
		return pageClickTotal2;
	}
	
	
	protected Integer getAndSetIPClickNum(String ipAddress,Integer pageClickNum){
		Jedis jedis = getJedis();
		String value = jedis.getSet("CIPNum_"+ipAddress, pageClickNum.toString());
		returnResource(jedis);
		if(value != null){
			return Integer.valueOf(pageClickNum);
		}else{
			return null;
		}
	}
	
	/**
	 * 更新渠道多个点击次数区间
	 * @param channelId   渠道ID
	 * @param ipClickNum  IP累计点击次数
	 */
	protected void updateChannelMultipleRangeClickIP(Integer channelId,Integer ipClickNum){
		Jedis jedis = getJedis();
		
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
//		logServiceImpl.setJedisPool(jedisPool);
		
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
		Jedis jedis = getJedis();
		jedis.sadd("ChannelTIP_"+channelId, ipAddress);
		returnResource(jedis);
	}
	
	/**
	 * 更新渠道点击IP数
	 * @param channelId
	 * @param newClickNum
	 * @param oldClickNum
	 */
	protected void updateChannelClickIP(Integer channelId,Integer newClickNum,Integer oldClickNum){
		/** 拿到上次点击数区间 **/
		Jedis jedis = getJedis();
		String lastClickIPKey = null;
		if(oldClickNum!=null){
			lastClickIPKey = matchClickRangeKey(oldClickNum);
		}
		
		String currentClickIPKey =  matchClickRangeKey(newClickNum);
		
		if(lastClickIPKey!=null){
			jedis.decr(lastClickIPKey+channelId);
			log.debug(lastClickIPKey+channelId+"--");
		}
		if(currentClickIPKey!=null){
			jedis.incr(currentClickIPKey+channelId);
			log.debug(currentClickIPKey+channelId+"++");
		}
		
		returnResource(jedis);
	}
	
	protected Jedis getJedis(){
		return jedisPools.getResource();
	}
	
	protected void returnResource(Jedis jedis){
		jedisPools.returnResource(jedis);
	}
	
	protected String matchClickRangeKey(Integer clickNum){
		if(clickNum==null){
			return null;
		}else if(clickNum >=1 && clickNum <= 2){
			return RedisKeys.ChannelC1IP.getKey();
		}else if (clickNum >=3 && clickNum <= 5){
			return RedisKeys.ChannelC2IP.getKey();
		}else if (clickNum >=6 && clickNum <= 10){
			return RedisKeys.ChannelC3IP.getKey();
		}else if (clickNum > 10){
			return RedisKeys.ChannelC4IP.getKey();
		}else {
			return null;
		}
	}


	
}
