package com.ada.log.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.net.MalformedURLException;
import java.net.URL;
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
@SuppressWarnings("all")
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
	 * @param domainId      域名ID
	 * @param clickNum      点击次数
	 * @param browsingTime  浏览时间,精确到毫秒
	 * @param browsingPage  当前页面链接
	 */
	public void log(String ipAddress,String uuid, Integer siteId, Integer channelId,Integer domainId,Integer clickNum, Integer browsingTime, String browsingPage) {
		/** 1）保存站点IP Set **/
		putSiteIPSet(siteId, ipAddress);
		/** 2）保存站点PV **/
		increSitePV(siteId);
		/** 3) 保存域名IP Set **/
		putDomainIPSet(domainId, ipAddress);
		/** 4) 保存域名PV  **/
		increDomainPV(domainId);
		/** 5) 保存IP鼠标点击次数 **/
		Integer newClickNum = increIPClickNum(ipAddress, clickNum);
		Integer oldClickNum = newClickNum - clickNum;
		/** 6) 更新域名点击IP数 **/
		updateDomainClickIP(domainId, newClickNum, oldClickNum);
		/** 7) 保存域名进入目标页IPSet**/
		if(siteService.matchTargetPage(siteId, browsingPage)){
			putDomainTIPSet(domainId, ipAddress);
		}
		if(channelId!=null){
			/** 3) 保存渠道IP Set **/
			putChannelIPSet(channelId, ipAddress);
			/** 4) 保存渠道PV **/
			increChannelPV(channelId);
			/** 6) 更新渠道点击IP数 **/
			updateChannelClickIP(channelId, newClickNum, oldClickNum);
			/** 7) 保存渠道进入目标页IPSet**/
			if(siteService.matchTargetPage(siteId, browsingPage)){
				putChannelTIPSet(channelId, ipAddress);
			}
		}
	}
	
	@Override
	public void log1(String ipAddress, String uuid, Integer siteId,Integer channelId,Integer domainId, String browsingPage) {
		/** 1）保存站点IP Set **/
		putSiteIPSet(siteId, ipAddress);
		/** 2）保存站点PV **/
		increSitePV(siteId);
		/** 3) 保存域名IP Set **/
		putDomainIPSet(domainId, ipAddress);
		/** 4) 保存域名PV  **/
		increDomainPV(domainId);
		/** 5) 保存域名进入目标页IPSet**/
		if(siteService.matchTargetPage(siteId, browsingPage)){
			putDomainTIPSet(domainId, ipAddress);
		}
		if(channelId!=null){
			/** 6) 保存渠道IP Set **/
			putChannelIPSet(channelId, ipAddress);
			/** 7) 保存渠道PV  **/
			increChannelPV(channelId);
			/** 8) 保存渠道进入目标页IPSet**/
			if(siteService.matchTargetPage(siteId, browsingPage)){
				putChannelTIPSet(channelId, ipAddress);
			}
		}
		
	}

	@Override
	public void log2(String ipAddress, String uuid, Integer siteId,Integer channelId,Integer domainId, Integer clickNum) {
		if(log.isDebugEnabled()){
			log.debug("ip->"+ipAddress+",siteId->"+siteId+",channelId->"+channelId+",clickNum->"+clickNum);
		}
	    Integer oldClickNum = getAndSetIPClickNum(ipAddress,clickNum);
//		if(oldClickNum == null){
//			oldClickNum= 0;
//		}
		if(channelId!=null){
			/** 1) 更新渠道点击IP数 **/
			updateChannelClickIP(channelId, clickNum, oldClickNum);
		}
		/** 2) 更新域名点击IP数 **/
		updateDomainClickIP(domainId, clickNum, oldClickNum);
	}

	/**
	 * 保存站点IPSet集合
	 * @param siteId       站点ID
	 * @param ipAddress    IP地址
	 */
	protected void putSiteIPSet(Integer siteId,String ipAddress) {
		Jedis jedis = getJedis();
		jedis.sadd(RedisKeys.SiteIP.getKey()+siteId+"", ipAddress);
		returnResource(jedis);
	}
	
	/**
	 * 保存站点PV +1
	 * @param siteId       站点ID
	 * @param ipAddress
	 */
	protected void increSitePV(Integer siteId) {
		Jedis jedis = getJedis();
		jedis.incr(RedisKeys.SitePV.getKey()+siteId+"");
		returnResource(jedis);
	}
	
	/**
	 * 保存渠道IPSet集合
	 * @param channelId    渠道ID
	 * @param ipAddress    IP地址
	 */
	protected void putChannelIPSet(Integer channelId,String ipAddress){
		Jedis jedis = getJedis();
		jedis.sadd(RedisKeys.ChannelIP.getKey()+channelId+"", ipAddress);
		returnResource(jedis);
	}
	
	/**
	 * 保存渠道PV +1
	 * @param channelId       渠道ID
	 * @param ipAddress    IP地址
	 */
	protected void increChannelPV(Integer channelId) {
		Jedis jedis = getJedis();
		jedis.incr(RedisKeys.ChannelPV.getKey()+channelId+"");	
		returnResource(jedis);
	}
	
	/**
	 * 保存域名IPSet集合
	 * @param domainId     域名ID
	 * @param ipAddress    IP地址
	 */
	protected void putDomainIPSet(Integer domainId,String ipAddress){
		Jedis jedis = getJedis();
		jedis.sadd(RedisKeys.DomainIP.getKey()+domainId+"", ipAddress);
		returnResource(jedis);
	}
	
	/**
	 * 保存域名PV +1
	 * @param domainId       域名ID
	 */
	protected void increDomainPV(Integer domainId) {
		Jedis jedis = getJedis();
		jedis.incr(RedisKeys.DomainPV.getKey()+domainId+"");	
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
		Long pageClickTotal = jedis.incrBy(RedisKeys.CIPNum.getKey()+ipAddress, pageClickNum);
		returnResource(jedis);
		int pageClickTotal2 = Integer.parseInt(String.valueOf(pageClickTotal)); 
		return pageClickTotal2;
	}
	
	protected Integer getAndSetIPClickNum(String ipAddress,Integer pageClickNum){
		Jedis jedis = getJedis();
		String value = jedis.getSet(RedisKeys.CIPNum.getKey()+ipAddress, pageClickNum.toString());
		returnResource(jedis);
		if(value != null){
			return Integer.valueOf(value);
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
	public static void main(String[] args) throws Exception {
//		JedisPoolConfig config = new JedisPoolConfig();
//		
//		JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379, 20000, "g^h*123T", 2);
//		LogServiceImpl logServiceImpl = new LogServiceImpl();
//		logServiceImpl.setJedisPool(jedisPool);
		
		
		
	}
	
	
	/**
	 * 
	 * 渠道进入目标页IP集合
	 * @param channelId
	 */
	protected void putChannelTIPSet(Integer channelId,String ipAddress){
		Jedis jedis = getJedis();
		jedis.sadd(RedisKeys.ChannelTIP.getKey()+channelId, ipAddress);
		returnResource(jedis);
	}
	
	/**
	 * 
	 * 域名进入目标页IP集合
	 * @param domainId
	 * @param ipAddress
	 */
	protected void putDomainTIPSet(Integer domainId,String ipAddress){
		Jedis jedis = getJedis();
		jedis.sadd(RedisKeys.DomainTIP.getKey()+domainId, ipAddress);
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
		String lastClickIPKey = null;
		if(oldClickNum!=null){
			lastClickIPKey = matchChannelClickRangeKey(oldClickNum);
		}

		String currentClickIPKey =  matchChannelClickRangeKey(newClickNum);
		Jedis jedis = getJedis();
		if(lastClickIPKey != null){
			jedis.decr(lastClickIPKey+channelId);
			log.debug(lastClickIPKey+channelId+"--");
		}
		if(currentClickIPKey != null){
			jedis.incr(currentClickIPKey+channelId);
			log.debug(currentClickIPKey+channelId+"++");
		}
		returnResource(jedis);
	}
	
	/**
	 * 更新域名点击IP数
	 * @param domainId
	 * @param newClickNum
	 * @param oldClickNum
	 */
	protected void updateDomainClickIP(Integer domainId,Integer newClickNum,Integer oldClickNum){
		String lastClickIPKey = null;
		if(oldClickNum!=null){
			lastClickIPKey = matchDomainClickRangeKey(oldClickNum);
		}

		String currentClickIPKey =  matchDomainClickRangeKey(newClickNum);
		Jedis jedis = getJedis();
		
		if(lastClickIPKey!=null){
			jedis.decr(lastClickIPKey+domainId);
			log.debug(lastClickIPKey+domainId+"--");
		}
		if(currentClickIPKey!=null){
			jedis.incr(currentClickIPKey+domainId);
			log.debug(currentClickIPKey+domainId+"++");
		}
		
		returnResource(jedis);
	}
	
	protected Jedis getJedis(){
		return jedisPools.getResource();
	}
	
	protected void returnResource(Jedis jedis){
		jedisPools.returnResource(jedis);
	}
	
	protected String matchDomainClickRangeKey(Integer clickNum){
		if(clickNum == null){
			return null;
		}else if(clickNum >=1 && clickNum <= 2){
			return RedisKeys.DomainC1IP.getKey();
		}else if (clickNum >=3 && clickNum <= 5){
			return RedisKeys.DomainC2IP.getKey();
		}else if (clickNum >=6 && clickNum <= 10){
			return RedisKeys.DomainC3IP.getKey();
		}else if (clickNum > 10){
			return RedisKeys.DomainC4IP.getKey();
		}else {
			return null;
		}
	}
	
	protected String matchChannelClickRangeKey(Integer clickNum){
		if(clickNum == null){
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
