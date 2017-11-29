package com.ada.log.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.ada.log.bean.AccessLog;
import com.ada.log.constant.RedisKeys;
import com.ada.log.dao.AccessLogDao;
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
	
	private List<AccessLog> cacheLogs = new ArrayList();
	
	@Autowired
	private AccessLogDao accessLogDao;
	
	@Override
	public void log(AccessLog log) {
		Boolean isOldUser = false;
		
		/** 当天第一次请求，判断老用户逻辑，减少操作次数，提高性能**/
		if(log.getTodayTime()!=null){
			/** 通过初次设置cookie值 判断是否老用户**/
			if(log.getFirstTime()!=null && log.getFirstTime()>1){
				try {
					boolean isSmpeDate = isSameDate(new Date(log.getFirstTime()), new Date());
					if(!isSmpeDate){
						isOldUser = true;
					}
				} catch (Exception e) {
				}
			}
			
			/** 通过是否直接访问判断是否老用户，粗暴数据不准确**/
			if(!isOldUser){
				if(log.getReferer()==null || "".equals(log.getReferer())){
					isOldUser = true;
				}
			}
		}
		
		log1(log.getIpAddress(), log.getUuid(), log.getSiteId(), log.getChannelId(),log.getDomainId(), log.getBrowser(), isOldUser);
		
		cacheLogs.add(log);
	}
	
	@Scheduled(cron="0/1 0 * * * ?")   /** 每间隔1秒钟保存一次 **/
	public void batchSave(){
		if(!cacheLogs.isEmpty()){
			List<AccessLog> temp = this.cacheLogs;
			cacheLogs = new ArrayList();
			accessLogDao.batchInsert(temp);
			temp.clear();
		}
	}

	@Override
	public void log1(String ipAddress, String uuid, Integer siteId,Integer channelId,Integer domainId, String browsingPage,Boolean isOldUser) {
		/** 1）保存站点IP Set **/
		putSiteIPSet(siteId, ipAddress);
		/** 2）保存站点PV **/
		increSitePV(siteId);
		/** 3) 保存域名IP Set **/
		putDomainIPSet(domainId, ipAddress);
		/** 4) 保存域名PV  **/
		increDomainPV(domainId);
		/** 5) 保存域名进入目标页IPSet**/
		
		if(isOldUser){
			putSiteOlduserIPSet(siteId,ipAddress);
			putDomainOlduserIPSet(domainId,ipAddress);
		}
		
		Boolean matchTarget = siteService.matchTargetPage(siteId, browsingPage);
		if(log.isDebugEnabled()){
			log.debug("匹配目标页 ->"+matchTarget +",browsingPage->"+browsingPage);
		}
		
		if(matchTarget){
			putDomainTIPSet(domainId, ipAddress);
		}
		if(channelId!=null){
			/** 6) 保存渠道IP Set **/
			putChannelIPSet(channelId, ipAddress);
			/** 7) 保存渠道PV  **/
			increChannelPV(channelId);
			/** 8) 保存渠道进入目标页IPSet**/
			if(matchTarget){
				putChannelTIPSet(channelId, ipAddress);
			}
			
			putChanneOlduserIPSet(channelId,ipAddress);
		}
		
	}
	
	protected void putDomainOlduserIPSet(Integer domainId,String ipAddress){
		Jedis jedis = getJedis();
		jedis.sadd(new StringBuffer().append(RedisKeys.DomainOldUserIP.getKey()).append(domainId).toString(), ipAddress);
		returnResource(jedis);
	}
	
	protected void putSiteOlduserIPSet(Integer siteId,String ipAddress){
		Jedis jedis = getJedis();
		jedis.sadd(new StringBuffer().append(RedisKeys.SiteOldUserIP.getKey()).append(siteId).toString(), ipAddress);
		returnResource(jedis);
	}
	
	protected void putChanneOlduserIPSet(Integer channeId,String ipAddress){
		Jedis jedis = getJedis();
		jedis.sadd(new StringBuffer().append(RedisKeys.ChannelOldUserIP.getKey()).append(channeId).toString(), ipAddress);
		returnResource(jedis);
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
	
	protected Jedis getJedis(){
		return jedisPools.getResource();
	}
	
	protected void returnResource(Jedis jedis){
		jedisPools.returnResource(jedis);
	}

	protected boolean isSameDate(Date date1,Date date2){
		if(date1 !=null && date2 !=null){
			if(date1.getYear() == date2.getYear()
				&& date1.getMonth() == date2.getMonth()
				&& date1.getDate() == date2.getDate()
				){
				return true;
			}
		}
		return false;
	}
}
