package com.ada.log.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ada.log.bean.EventLog;
import com.ada.log.constant.RedisKeys;
import com.ada.log.dao.AccessLogDao;
import com.ada.log.service.IPDBService;
import com.ada.log.service.IPSetService;
import com.ada.log.service.JedisPools;
import com.ada.log.service.LogService;
import com.ada.log.service.SiteService;
import com.ada.log.util.Dates;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;

import redis.clients.jedis.Jedis;

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
	
	@Autowired
	private IPDBService IPDBService;
	
	@Autowired
	private IPSetService IPSetService;
	
	private final static Log log = LogFactory.getLog(LogServiceImpl.class);
	
	private List<AccessLog> cacheLogs = new ArrayList();
	private List<EventLog> eventLogs = new ArrayList<EventLog>();
	
	private Timer timer = new Timer();
	
	@Autowired
	private AccessLogDao accessLogDao;
	
	private Integer numberOfBatchSave = 1000000;

	@Override
	public void log(AccessLog data) {
		/** 实时统计 **/
		stat(data);
		if(data!=null){
			cacheLogs.add(data);
		}
	}
	
	@Scheduled(cron="0/1 * * * * ?")   /** 每间隔1秒钟保存一次 **/
	public void scheduled(){
		new Thread(new Runnable() {
			public void run() {
				try {
					batchSave();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();;
	}
	
	public  void batchSave(){
		try {
			if(!cacheLogs.isEmpty()){
				List<AccessLog> temp = this.cacheLogs;
				if(!temp.isEmpty()){
					cacheLogs = new ArrayList();
					if(temp.size() <= numberOfBatchSave){
						accessLogDao.batchInsert(temp);
					}else{
						Integer maxBatch=  temp.size()/numberOfBatchSave + (temp.size()%numberOfBatchSave>0?1:0);
						for(int i=0;i<maxBatch;i++){
							List tlist = new ArrayList();
							for(int j=i*numberOfBatchSave,k=0;k<numberOfBatchSave && j<temp.size();j++,k++){
								AccessLog al = temp.get(j);
								if(al!=null){
									tlist.add(al);
								}
							}
							accessLogDao.batchInsert(tlist);
						}
					}
					
					temp.clear();
				}
			}
		} catch (Exception e) {
			log.error("保存访问日志出错->"+e.getMessage(),e);
		}
		
		
		try {
			if(!eventLogs.isEmpty()){
				List<EventLog> temp = this.eventLogs;
				eventLogs =  new ArrayList<EventLog>();
				accessLogDao.batchInsertEventLog(temp);
				temp.clear();
			}
		} catch (Exception e) {
			log.error("保存事件日志出错->"+e.getMessage(),e);
		}
	}
	
	public void log(EventLog log) {
		eventLogs.add(log);
		
//		if("onLogin".equals(log.getEvent())){
//			eventLogs.add(log);
//		}
	}
	
	/** 判断是否老用户**/
	protected Boolean isOldUser(AccessLog log) {
		Boolean isOldUser = false;
		
		/** 当天第一次请求，判断老用户逻辑，减少操作次数，提高性能**/
		if(log.getTodayTime()!=null && log.getFirstTime()!=null){
			/** 通过初次设置cookie值 判断是否老用户**/
//			if(log.getTodayTime() > log.getFirstTime()){
//				isOldUser = true;
//			}
//			
			Long now = System.currentTimeMillis();
			Long time = now - log.getFirstTime();
			if(time > 86400000){/** 超过24小时算老用户 **/
				isOldUser = true;
			}
			/** 通过是否直接访问判断是否老用户，粗暴数据不准确**/
//			if(!isOldUser){
//				if(log.getReferer()==null || "".equals(log.getReferer())){
//					isOldUser = true;
//				}
//			}
		}
		return isOldUser;
	}
	
	public void stat(AccessLog req) {
		/** 客户端当天第一次访问**/
		Boolean isTodayFirstTime = req.getTodayTime() !=null;
		Jedis jedis = getJedis();
		try{
			String ipAddress = req.getIpAddress();
			Integer siteId = req.getSiteId();
			Integer domainId =  req.getDomainId();
			Integer channelId = req.getChannelId();
			/** ）保存站点PV **/
			jedis.incr(new StringBuffer().append(RedisKeys.SitePV.getKey()).append(req.getSiteId()).toString());
			/** ) 保存域名PV  **/
			jedis.incr(new StringBuffer().append(RedisKeys.DomainPV.getKey()).append(domainId).toString());
	
			/** 客户端当天第一个请求处理老用户逻辑**/
			Boolean isOldUser = false;
			/** 老IP **/
			boolean oldip = false;
			/** ) 保存站点IP Set **/
			jedis.sadd(new StringBuffer().append(RedisKeys.SiteIP.getKey()).append(siteId).toString(),ipAddress);
			/** ) 保存域名IP Set **/
			jedis.sadd(new StringBuffer().append(RedisKeys.DomainIP.getKey()).append(domainId).toString(),ipAddress);
			if(isTodayFirstTime){
				oldip = IPSetService.exists(domainId, ipAddress);
				if(oldip){
					isOldUser = true;
				}else{
					isOldUser = isOldUser(req);
				}
				/** ) 站点UV ++ **/
				jedis.incr(new StringBuffer().append(RedisKeys.SiteUV.getKey()).append(siteId).toString());
				/** ) 域名UV ++ **/
				jedis.incr(new StringBuffer().append(RedisKeys.DomainUV.getKey()).append(domainId).toString());
				/** ) 保存城市列表 ++ **/
				jedis.sadd(new StringBuffer().append(RedisKeys.DomainCitySet.getKey()).append(domainId).toString(),req.getRegion());
				if(isOldUser){
					/** 记录老用户IP **/
					jedis.sadd(new StringBuffer().append(RedisKeys.DomainOldUserIP.getKey()).append(domainId).toString(), ipAddress);
				}
			}
			if(oldip){
				jedis.sadd(new StringBuffer().append(RedisKeys.DomainOldIP.getKey()).append(domainId).toString(), ipAddress);
			}
			
			/** ) 保存域名进入目标页IPSet**/
			Boolean matchTarget = siteService.matchTargetPage(siteId, req.getUrl());
			if(log.isDebugEnabled()){
				log.debug("匹配目标页 ->"+matchTarget +",browsingPage->"+req.getUrl());
			}
			
			if(matchTarget){
				/**域名进入目标页IP集合 **/
				jedis.sadd(new StringBuffer(RedisKeys.DomainTIP.getKey()).append(domainId).toString(), ipAddress);
			}
			
			/** 渠道统计 **/
			if(req.getChannelId()!=null){
				/** 保存渠道IP Set **/
				jedis.sadd(new StringBuffer(RedisKeys.ChannelIP.getKey()).append(channelId).toString(), ipAddress);
				/** 保存渠道PV  **/
				jedis.incr(new StringBuffer(RedisKeys.ChannelPV.getKey()).append(channelId).toString());
				/** 保存渠道进入目标页IPSet**/
				if(matchTarget){
					jedis.sadd(new StringBuffer(RedisKeys.ChannelTIP.getKey()).append(channelId).toString(), ipAddress);
				}
				/** 保存老用户IPSet **/
				if(isOldUser){
					jedis.sadd(new StringBuffer().append(RedisKeys.ChannelOldUserIP.getKey()).append(channelId).toString(), ipAddress);
				}
				/** 保存老IP数 **/
				//TODO
//				if(oldip){
//					jedis.sadd(new StringBuffer().append(RedisKeys.ChannelOldUserIP.getKey()).append(req.getChannelId()).toString(), ipAddress);
//				}
			}
	
			/** 广告入口统计 **/
			if(req.getEntranceType() == 1){
				/** ）保存广告IP **/
				jedis.sadd(new StringBuffer().append(RedisKeys.AdIP.getKey()).append(req.getSiteId()).toString(), ipAddress);
				/** ) 保存广告PV  **/
				jedis.incr(new StringBuffer().append(RedisKeys.AdPV.getKey()).append(req.getSiteId()).toString());
				/** ) 保存域名IP **/
				jedis.sadd(new StringBuffer().append(RedisKeys.DomainAdIP.getKey()).append(domainId).toString(), ipAddress);
				/** ) 保存域名PV  **/
				jedis.incr(new StringBuffer().append(RedisKeys.DomainAdPV.getKey()).append(domainId).toString());
				if(isTodayFirstTime){
					/** ) 域名UV ++ **/
					jedis.incr(new StringBuffer().append(RedisKeys.DomainAdUV.getKey()).append(domainId).toString());
					jedis.incr(new StringBuffer().append(RedisKeys.AdUV.getKey()).append(req.getSiteId()).toString());
				}
				/** 保存目标页 **/
				if(matchTarget){
					jedis.sadd(new StringBuffer().append(RedisKeys.DomainAdTIP.getKey()).append(domainId).toString(), ipAddress);
				}
				/** 保存老用户IPSet **/
				if(isOldUser){
					jedis.sadd(new StringBuffer().append(RedisKeys.DomainAdOldUserIP.getKey()).append(domainId).toString(), ipAddress);
				}
				/** 保存老IP数 **/
				if(oldip){
					jedis.sadd(new StringBuffer().append(RedisKeys.DomainAdOldIP.getKey()).append(domainId).toString(), ipAddress);
				}
			}
			
			/** 地域统计 **/
			
			String region = req.getRegion();
			req.setRegion(region);
			
			/** ) 保存域名IP Set **/
			jedis.sadd(new StringBuffer().append(RedisKeys.DomainCityIP.getKey()).append(domainId).append("_").append(req.getRegion()).toString(), ipAddress);
			/** ) 保存域名PV  **/
			jedis.incr(new StringBuffer().append(RedisKeys.DomainCityPV.getKey()).append(domainId).append("_").append(req.getRegion()).toString());
			if(isTodayFirstTime){
				/** ) 域名UV ++ **/
				jedis.incr(new StringBuffer().append(RedisKeys.DomainCityUV.getKey()).append(domainId).append("_").append(req.getRegion()).toString());
			}
			/** 保存目标页 **/
			if(matchTarget){
				jedis.sadd(new StringBuffer().append(RedisKeys.DomainCityTIP.getKey()).append(domainId).append("_").append(req.getRegion()).toString(), ipAddress);
			}
			/** 保存老用户IPSet **/
			if(isOldUser){
				jedis.sadd(new StringBuffer().append(RedisKeys.DomainCityOldUserIP.getKey()).append(domainId).append("_").append(req.getRegion()).toString(), ipAddress);
			}
			/** 保存老IP数 **/
			if(oldip){
				jedis.sadd(new StringBuffer().append(RedisKeys.DomainCityOldIP.getKey()).append(domainId).append("_").append(req.getRegion()).toString(), ipAddress);
			}
			
			/** 地区广告入口 **/
			if(req.getAdId()!=null){
				/** ) 保存域名IP Set **/
				jedis.sadd(new StringBuffer().append(RedisKeys.DomainAdCityIP.getKey()).append(domainId).append("_").append(req.getRegion()).toString(), ipAddress);
				/** ) 保存域名PV  **/
				jedis.incr(new StringBuffer().append(RedisKeys.DomainAdCityPV.getKey()).append(domainId).append("_").append(req.getRegion()).toString());
				if(isTodayFirstTime){
					/** ) 域名UV ++ **/
					jedis.incr(new StringBuffer().append(RedisKeys.DomainAdCityUV.getKey()).append(domainId).append("_").append(req.getRegion()).toString());
				}
				/** 保存目标页 **/
				if(matchTarget){
					jedis.sadd(new StringBuffer().append(RedisKeys.DomainAdCityTIP.getKey()).append(domainId).append("_").append(req.getRegion()).toString(), ipAddress);
				}
				/** 保存老用户IPSet **/
				if(isOldUser){
					jedis.sadd(new StringBuffer().append(RedisKeys.DomainAdCityOldUserIP.getKey()).append(domainId).append("_").append(req.getRegion()).toString(), ipAddress);
				}
				/** 保存老IP数 **/
				if(oldip){
					jedis.sadd(new StringBuffer().append(RedisKeys.DomainAdCityOldIP.getKey()).append(domainId).append("_").append(req.getRegion()).toString(), ipAddress);
				}
			}
		
		} finally{
			returnResource(jedis);
		}
	}
	
	protected void putDomainOlduserIPSet(Integer domainId,String ipAddress){
		Jedis jedis = getJedis();
		try{
			jedis.sadd(new StringBuffer().append(RedisKeys.DomainOldUserIP.getKey()).append(domainId).toString(), ipAddress);
		} finally{
			returnResource(jedis);
		}
	}
	
	protected void putSiteOlduserIPSet(Integer siteId,String ipAddress){
		Jedis jedis = getJedis();
		try {
			jedis.sadd(new StringBuffer().append(RedisKeys.SiteOldUserIP.getKey()).append(siteId).toString(), ipAddress);
		} finally{
			returnResource(jedis);
		}
	}
	
	protected void putChanneOlduserIPSet(Integer channeId,String ipAddress){
		Jedis jedis = getJedis();
		try {
			jedis.sadd(new StringBuffer().append(RedisKeys.ChannelOldUserIP.getKey()).append(channeId).toString(), ipAddress);
		} finally{
			returnResource(jedis);
		}
	}

	/**
	 * 保存站点IPSet集合
	 * @param siteId       站点ID
	 * @param ipAddress    IP地址
	 */
	protected void putSiteIPSet(Integer siteId,String ipAddress) {
		Jedis jedis = getJedis();
		try {
			jedis.sadd(RedisKeys.SiteIP.getKey()+siteId+"", ipAddress);
		} finally{
			returnResource(jedis);
		}
	}
	
	/**
	 * 保存站点PV +1
	 * @param siteId       站点ID
	 * @param ipAddress
	 */
	protected void increSiteUVSet(Integer siteId) {
		Jedis jedis = getJedis();
		try{
			jedis.incr(RedisKeys.SitePV.getKey()+siteId+"");
		} finally{
			returnResource(jedis);
		}
	}
	
	/**
	 * 保存站点PV +1
	 * @param siteId       站点ID
	 * @param ipAddress
	 */
	protected void increSitePV(Integer siteId) {
		Jedis jedis = getJedis();
		try{
			jedis.incr(RedisKeys.SitePV.getKey()+siteId+"");
		} finally{
			returnResource(jedis);
		}
	}
	
	/**
	 * 保存站点UV +1
	 * @param siteId       站点ID
	 * @param ipAddress
	 */
	protected void increSiteUV(Integer siteId) {
		Jedis jedis = getJedis();
		try{
			jedis.incr(new StringBuffer().append(RedisKeys.SiteUV.getKey()).append(siteId).toString());
		} finally{
			returnResource(jedis);
		}
	}
	
	/**
	 * 保存渠道IPSet集合
	 * @param channelId    渠道ID
	 * @param ipAddress    IP地址
	 */
	protected void putChannelIPSet(Integer channelId,String ipAddress){
		Jedis jedis = getJedis();
		try{
			jedis.sadd(RedisKeys.ChannelIP.getKey()+channelId+"", ipAddress);
			returnResource(jedis);
		} finally{
			returnResource(jedis);
		}
	}
	
	/**
	 * 保存渠道PV +1
	 * @param channelId       渠道ID
	 * @param ipAddress    IP地址
	 */
	protected void increChannelPV(Integer channelId) {
		Jedis jedis = getJedis();
		try{
			jedis.incr(RedisKeys.ChannelPV.getKey()+channelId+"");	
		} finally{
			returnResource(jedis);
		}
	}
	
	/**
	 * 保存域名IPSet集合
	 * @param domainId     域名ID
	 * @param ipAddress    IP地址
	 */
	protected void putDomainIPSet(Integer domainId,String ipAddress){
		Jedis jedis = getJedis();
		try{
			jedis.sadd(RedisKeys.DomainIP.getKey()+domainId+"", ipAddress);
		} finally{
			returnResource(jedis);
		}
	}
	
	/**
	 * 获取域名IPSet集合
	 * @param domainId     域名ID
	 * @param ipAddress    IP地址
	 */
	@Override
	public Set<String> getYesterdayDomainIPSet(Integer domainId){
		Jedis jedis = getJedis();
		try{
			Date today = Dates.todayStart();
			Date lastDay = Dates.add(today, Calendar.DAY_OF_WEEK, -1);
			jedis.select(lastDay.getDay());
			Set<String> allip = jedis.smembers(RedisKeys.DomainIP.getKey()+domainId+"");
			return allip;
		} finally{
			returnResource(jedis);
		}
	}
	
	/**
	 * 保存域名PV +1
	 * @param domainId       域名ID
	 */
	protected void increDomainPV(Integer domainId) {
		Jedis jedis = getJedis();
		try{
			jedis.incr(RedisKeys.DomainPV.getKey()+domainId+"");	
		} finally{
			returnResource(jedis);
		}
	}
	
	/**
	 * 
	 * @param ipAddress   IP地址
	 * @param clickNum    页面点击次数
	 * @return
	 */
	protected Integer increIPClickNum(String ipAddress,Integer pageClickNum){
		Jedis jedis = getJedis();
		Long pageClickTotal = 0l;
		try{
			pageClickTotal = jedis.incrBy(RedisKeys.CIPNum.getKey()+ipAddress, pageClickNum);
		} finally{
			returnResource(jedis);
		}
		int pageClickTotal2 = Integer.parseInt(String.valueOf(pageClickTotal)); 
		return pageClickTotal2;
	}
	
	protected Integer getAndSetIPClickNum(String ipAddress,Integer pageClickNum){
		Jedis jedis = getJedis();
		try{
			String value = jedis.getSet(RedisKeys.CIPNum.getKey()+ipAddress, pageClickNum.toString());
			if(value != null){
				return Integer.valueOf(value);
			}else{
				return null;
			}
		} finally{
			returnResource(jedis);
		}
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
		try{
			jedis.sadd(RedisKeys.ChannelTIP.getKey()+channelId, ipAddress);
		} finally{
			returnResource(jedis);
		}
	}
	
	/**
	 * 
	 * 域名进入目标页IP集合
	 * @param domainId
	 * @param ipAddress
	 */
	protected void putDomainTIPSet(Integer domainId,String ipAddress){
		Jedis jedis = getJedis();
		try{
			jedis.sadd(RedisKeys.DomainTIP.getKey()+domainId, ipAddress);
			returnResource(jedis);
		} finally{
			returnResource(jedis);
		}
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
