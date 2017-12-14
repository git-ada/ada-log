package com.ada.log.event.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;

import com.ada.log.service.JedisPools;
import com.ada.log.service.LogService;

/**
 * 超偶像的页面点击事件处理器
 * @author ASUS
 *
 */
public abstract class AbstractPageEventHandle implements PageEventHandle{
	
	private Log log = LogFactory.getLog(getClass());
	
	@Autowired
    private  JedisPools jedisPools;
	
	private final static String spacer = "_";
	private final static String domainKey = "Domain";
	private final static String channelKey = "Channel";
	
	private String eventKey; //Click,
	private Integer[] pageEventThresholds;
	
	@Autowired
	private LogService logService;
	
	public AbstractPageEventHandle(Integer[] pageEventThresholds,String eventKey) {
		super();
		this.pageEventThresholds = pageEventThresholds;
		this.eventKey = eventKey;
	}
	
	@Override
	public void handle(String ipAddress, String uuid, Integer siteId,
			Integer channelId, Integer domainId, Integer adId,String region,
			Integer number) {
		
		if(log.isDebugEnabled()){
			log.debug("ip->"+ipAddress+",siteId->"+siteId+",channelId->"+channelId+",number->"+number);
		}
		
		if(number==null){
			return;
		}

		Jedis jedis = jedisPools.getResource();
		try {
			Integer newrange = matchRange(number);
			if(channelId!=null){
				/** 1) 更新渠道IP数 **/
				String _channelKey =  new StringBuffer().append(channelKey).append(eventKey).append(newrange).append("IP").append(spacer).append(channelId).toString();
				jedis.sadd(_channelKey,ipAddress);
				log.debug(_channelKey+"++");
			}
			if(domainId!=null){
				/** 2) 更新域名IP数 **/
				String _domainKey =  new StringBuffer().append(domainKey).append(eventKey).append(newrange).append("IP").append(spacer).append(domainId).toString();
				jedis.sadd(_domainKey,ipAddress);
				log.debug(_domainKey+"++");
				
				/** 3）更新域名下属地区 **/
				String _domainRegionKey =  new StringBuffer().append("DomainCity").append(eventKey).append(newrange).append("IP").append(spacer).append(domainId).append(spacer).append(region).toString();
				jedis.sadd(_domainRegionKey,ipAddress);
				log.debug(_domainRegionKey+"++");
				if(adId!=null){
					String _domainAdKey =  new StringBuffer().append("DomainAd").append(eventKey).append(newrange).append("IP").append(spacer).append(domainId).toString();
					jedis.sadd(_domainAdKey,ipAddress);
					log.debug(_domainAdKey+"++");
					
					String _domainCityAdKey =  new StringBuffer().append("DomainCityAd").append(eventKey).append(newrange).append("IP").append(spacer).append(domainId).append(spacer).append(region).toString();
					jedis.sadd(_domainCityAdKey,ipAddress);
					log.debug(_domainCityAdKey+"++");
				}
			}
		} finally{
			jedisPools.returnResource(jedis);
		}
	}
	
	protected Integer matchRange(Integer number){
		for(int i=1;i<=pageEventThresholds.length;i++){
			Integer threshold = pageEventThresholds[i-1];
			if(threshold.equals(number)){
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
