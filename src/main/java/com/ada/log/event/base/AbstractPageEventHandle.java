package com.ada.log.event.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;

import com.ada.log.service.JedisPools;

/**
 * 超偶像的页面点击事件处理器
 * @author ASUS
 *
 */
public abstract class AbstractPageEventHandle implements PageEventHandle{
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
    private  JedisPools jedisPools;
	
	private final static String spacer = "_";
	private final static String domainKey = "Domain";
	private final static String channelKey = "Channel";
	
	private String eventKey; //Click,
	private Integer[] pageEventThresholds;
	
	public AbstractPageEventHandle(Integer[] pageEventThresholds,String eventKey) {
		super();
		this.pageEventThresholds = pageEventThresholds;
		this.eventKey = eventKey;
	}

	@Override
	public void handle(String ipAddress, String uuid, Integer siteId,
			Integer channelId, Integer domainId, Integer number,
			String browsingPage) {
		
		if(log.isDebugEnabled()){
			log.debug("ip->"+ipAddress+",siteId->"+siteId+",channelId->"+channelId+",number->"+number);
		}
		
		if(number==null){
			return;
		}
		
	    Integer oldNumber = getAndSetIPEventNum(ipAddress,number);
	    
	    if(oldNumber!=null && oldNumber==number){
	    	/** 未变动忽略  **/
	    	return;
	    }
	    
		if(channelId!=null){
			/** 1) 更新渠道IP数 **/
			updateChannelEventIP(channelId, number, oldNumber);
		}
		if(domainId!=null){
			/** 2) 更新域名IP数 **/
			updateDomainEventIP(domainId, number, oldNumber);
		}
	}
	
	/**
	 * 更新事件数值并获得老值
	 * @param ipAddress
	 * @param number
	 * @return
	 */
	protected Integer getAndSetIPEventNum(String ipAddress,Integer number){
		Jedis jedis = getJedis();
		String redisKey = new StringBuffer().append(eventKey).append("IP").append(spacer).append(ipAddress).toString();
		String oldvalue = jedis.getSet(redisKey, number.toString());
		returnResource(jedis);
		if(oldvalue != null){
			return Integer.valueOf(oldvalue);
		}else{
			return null;
		}
	}
	
	/**
	 * 更新渠道点击IP数
	 * @param channelId
	 * @param newClickNum
	 * @param oldClickNum
	 */
	protected void updateChannelEventIP(Integer channelId,Integer newNumber,Integer oldNumber){
		/** 拿到上次点击数区间 **/
		Integer oldrange = matchRange(oldNumber);
		Integer newrange = matchRange(newNumber);
		
		if(oldrange!=null && newrange != null && oldrange == newrange){
			/** 同样请求 **/
			log.warn("意料之外出现同数据,channelId->"+channelId+",newNumber->"+newNumber+",oldNumber->"+oldNumber);
			return ;
		}
		
		Jedis jedis = getJedis();
		if(oldrange != null && newrange !=null ){
			/** 构造Key 如: ChannelClick1_192.168.1.10 **/
			String lastEventIPKey = new StringBuffer().append(channelKey).append(eventKey).append(oldrange).append("IP").append(spacer).append(channelId).toString();
			jedis.decr(lastEventIPKey);
			log.debug(lastEventIPKey+"--");
		}
		
		if(newrange != null){
			String currentEventIPKey =  new StringBuffer().append(channelKey).append(eventKey).append(newrange).append("IP").append(spacer).append(channelId).toString();
			jedis.incr(currentEventIPKey);
			log.debug(currentEventIPKey+"++");
		}
		returnResource(jedis);
	}
	
	/**
	 * 更新域名点击IP数
	 * @param domainId
	 * @param newClickNum
	 * @param oldClickNum
	 */
	protected void updateDomainEventIP(Integer domainId,Integer newNumber,Integer oldNumber){
		/** 拿到上次点击数区间 **/
		Integer oldrange = matchRange(oldNumber);
		Integer newrange = matchRange(newNumber);
		
		if(oldrange!=null && newrange != null && oldrange == newrange){
			/** 同样请求 **/
			log.warn("意料之外出现同数据,domainId->"+domainId+",newNumber->"+newNumber+",oldNumber->"+oldNumber);
			return ;
		}
		
		Jedis jedis = getJedis();
		if(oldrange != null && newrange !=null ){
			/** 构造Key 如: ChannelClick1_192.168.1.10 **/
			String lastEventIPKey = new StringBuffer().append(domainKey).append(eventKey).append(oldrange).append("IP").append(spacer).append(domainId).toString();
			jedis.decr(lastEventIPKey);
			log.debug(lastEventIPKey+"--");
		}
		
		if(newrange != null){
			String currentEventIPKey =  new StringBuffer().append(domainKey).append(eventKey).append(newrange).append("IP").append(spacer).append(domainId).toString();
			jedis.incr(currentEventIPKey);
			log.debug(currentEventIPKey+"++");
		}
		returnResource(jedis);
	}
	
	protected Integer matchRange(Integer clickNum){
		for(int i=1;i<=pageEventThresholds.length;i++){
			Integer threshold = pageEventThresholds[i-1];
			if(threshold == clickNum){
				return i;
			}
		}
		return null;
	}
	
	protected Jedis getJedis(){
		return jedisPools.getResource();
	}
	
	protected void returnResource(Jedis jedis){
		jedisPools.returnResource(jedis);
	}
}