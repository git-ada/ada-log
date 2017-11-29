package com.ada.log.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ada.log.bean.ADPage;
import com.ada.log.bean.Channel;
import com.ada.log.bean.Site;
import com.ada.log.dao.AdPageDao;
import com.ada.log.dao.ChannelDao;
import com.ada.log.dao.SiteDao;
import com.ada.log.service.ChannelService;
import com.ada.log.service.DomainService;

/**
 * 渠道服务实现类
 */
@Service
public class ChannelServiceImpl2 implements ChannelService,InitializingBean {
	
	private final static Log log = LogFactory.getLog(ChannelServiceImpl2.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ChannelDao channelDao;
	
	@Autowired
	private SiteDao siteDao;
	
	@Autowired
	private AdPageDao adPageDao;
	
	@Autowired
	private DomainService domainService;
	
	/** 所有站点下渠道字符串和渠道ID映射缓存 **/
	private Map<Integer,Map<String,Integer>> siteChannelMapping = new HashMap<Integer,Map<String,Integer>>(); //<SiteId,<ChannelStr,ChannelId>>;
	private Map<Integer,List<ADPage>> siteAdPages = new HashMap<Integer,List<ADPage>>();

	
	@Override
	public Integer queryChannel(Integer siteId, String browsingPage) {
		ADPage adPage = matchAdPage(siteId, browsingPage);
		Integer channelId = null;
		if(adPage!=null){
			String domain = domainService.getDomain(browsingPage);
			String channelStr = buildChannelStr(domain,adPage,browsingPage);
			channelId =getChannelIdCache(siteId,channelStr);
			if(log.isDebugEnabled()){
				log.debug("内存中无缓存渠道Id,channelStr->"+channelStr);
			}
			if(channelId==null){
				Integer domainId = domainService.queryDomain(siteId, domain);
				Object domainLock = getDomainLock(domainId);
				synchronized (domainLock) {
					channelId = getChannelIdCache(siteId,channelStr);
					if(channelId==null){
						/** 跨服务器数据库并发问题 **/
						//TODO
						try {
							jdbcTemplate.update("insert into ada_channel(siteId,domainId,channelName,channelStr,adId,createTime) values(?,?,?,?,?,now())", new Object[]{siteId,domainId,"",channelStr,adPage.getId()});
							log.info("创建新的渠道链接->"+channelStr);
						} catch (Exception e) {
							log.error("创建新的渠道失败->"+e.getMessage(),e);
						}
						
						try {
							List<Channel> list = channelDao.findBySiteIdAndChannelStr(siteId, channelStr);
							if(list!=null && !list.isEmpty()){
								Channel c = list.get(0);
								setChannelIdCache(siteId, channelStr, c.getId());
								channelId = c.getId();
							}
						} catch (Exception e) {
							log.error("查询渠道链接失败->"+e.getMessage(),e);
						}
					}
				}
			}
		}
		
		if(log.isDebugEnabled()){
			log.debug("返回渠道Id,channelId->"+channelId);
		}

		return channelId;
	}
	
	private Map<Integer,Object> domainLocks = new HashMap();
	
	protected Object getDomainLock(Integer domainId){
		synchronized (domainLocks) {
			Object domainLock = domainLocks.get(domainId);
			if(domainLock==null){
				domainLock = new Object();
				domainLocks.put(domainId, domainLock);
			}
			return domainLock;
		}
	}
	
	@Scheduled(cron="0 0 0 * * ?")
	public void cleanDomainLock(){
		synchronized (domainLocks) {
			domainLocks.clear();
		}
	}
	
	/** 构造渠道字符串 **/
	protected String buildChannelStr(String domain,ADPage adPage,String browsingPage) {
		String parameterName = adPage.getChannelKey();
		String parameterValue = getParameterValue(browsingPage,parameterName);
		
		StringBuffer s = new StringBuffer();
		s.append(domain).append(adPage.getMatchContent()).append("?").append(parameterName).append("=").append(parameterValue);
		
		String channelStr = s.toString();
		
		if(log.isDebugEnabled()){
			log.debug("构建渠道字符串->"+channelStr);
		}
		
		return channelStr;
	}
	
	/** 获取参数值 **/
	protected String getParameterValue(String browsingPage ,String parameterName){
		try {
			String queryString = browsingPage.substring(browsingPage.indexOf("?")+1, browsingPage.length()-1);
			String[] parameters = queryString.split("&");
			for(String p:parameters){
				String[] t = p.split("=");
				if(p.length() ==2){
					String key = t[0];
					String value =  t[1];
					if(key.equals(parameterName)){
						if(log.isDebugEnabled()){
							log.debug("获取到参数值->"+browsingPage+",parameterName->"+parameterName+",parameterValue->"+value);
						}
						return value;
					}
				}
			}
		} catch (Exception e) {
			log.error("提取参数值出错,browsingPage->"+browsingPage+",parameterName->"+parameterName);
		}
		return null;
	}
	
	protected ADPage matchAdPage(Integer siteId,String browsingPage){
		List<ADPage> adPages = siteAdPages.get(siteId);
		if(siteAdPages!=null && !siteAdPages.isEmpty()){
			for(ADPage page:adPages){
				/** 页面链接包含广告页关键字 **/
				if(browsingPage.indexOf(page.getMatchContent()) !=-1){
					if(log.isDebugEnabled()){
						log.debug("匹配广告页成功->"+browsingPage+",->"+page.getMatchContent()+",adId->"+page.getId());
					}
					return page;
				}
			}
		}
		return null;
	}
	
	protected Integer getChannelIdCache(Integer siteId,String channelStr) {
		Map<String,Integer>  channelMapping = siteChannelMapping.get(siteId);
		if(channelMapping!=null){
			Integer channelId = channelMapping.get(channelStr);
			return channelId;
		}
		return null;
	}
	
	protected void setChannelIdCache(Integer siteId,String channelStr,Integer channelId) {
		Map<String,Integer>  channelMapping = siteChannelMapping.get(siteId);
		if(channelMapping==null){
			channelMapping = new HashMap<String,Integer>();
		}
		
		channelMapping.put(channelStr, channelId);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		loadChannels();
		loadADs();
	}
	
	protected void loadChannels() {
		log.debug("加载现有渠道数据");
		List<Site> sites = siteDao.findAll();
		
		for(Site site:sites){
			List<Channel> channels = channelDao.findBySiteId(site.getId());
			if(channels!=null && !channels.isEmpty()){
				for(Channel c:channels){
					if(c.getChannelStr()!=null && !c.getChannelStr().isEmpty()){
						setChannelIdCache(site.getId(),c.getChannelStr(), c.getId());
					}
				}
			}
		}
	}
	
	@Scheduled(cron="0/10 0 * * * ?")   //10秒钟加载一次
	protected void loadADs() {
		log.debug("加载广告页");
		List<Site> sites = siteDao.findAll();
		Map<Integer,List<ADPage>> loadSiteAdPages = new HashMap<Integer,List<ADPage>>();
		
		for(Site site:sites){
			List<ADPage> ads = adPageDao.findBySiteId(site.getId());
			if(ads!=null && !ads.isEmpty()){
				loadSiteAdPages.put(site.getId(), ads);
			}
		}
		Map<Integer,List<ADPage>> temp = this.siteAdPages;
		this.siteAdPages = loadSiteAdPages;		
		temp.clear();
	}
}
