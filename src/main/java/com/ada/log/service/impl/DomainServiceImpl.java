package com.ada.log.service.impl;

import java.util.ArrayList;
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

import com.ada.log.service.DomainService;

/**
 * 域名服务实现类
 * @author zhao xiang
 * @since  2017/11/21
 * 
 */
@Service
@SuppressWarnings("all")
public class DomainServiceImpl implements DomainService,InitializingBean {
	
	private final static Log log = LogFactory.getLog(DomainServiceImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private  Map<Integer,Map<String,Integer>> domainMap;//map<站点id,域名list<域名id,<字段名，字段值>>>
	
	private Object lock = new Object();
	
	protected Integer getDomianIdCache(Integer siteId,String domain) {
		Map<String,Integer> site = domainMap.get(siteId);
		Integer domainId = null;
		if(site != null){
			domainId = site.get(domain);
			return domainId;
		}else{
			return null;
		}
	}
	
	protected void setDomainIdCache(Integer siteId,String domain,Integer domainId){
		Map<String,Integer> site = domainMap.get(siteId);
		if(site==null){
			site = new HashMap();
		}
		site.put(domain, domainId);
	}
	
	@Override
	public Integer queryDomain(Integer siteId,String domain) {
		Integer domainId = getDomianIdCache(siteId,domain);
		if(domainId != null){
			return domainId;
		}
		
		synchronized (lock) {
			domainId = getDomianIdCache(siteId,domain);
			if(domainId!=null){
				return domainId;
			}
			
			/** 跨服务器数据库并发问题 **/
			jdbcTemplate.execute("insert into ada_domain(siteId,domain,createTime) values("+siteId+",'"+domain+"',now())");
			
			List<Map<String, Object>> queryForList3 = jdbcTemplate.queryForList("select id from ada_domain where siteId=? and domain=? ",siteId,domain);
			if(queryForList3 != null && queryForList3.size()>0){
				domainId = (Integer)queryForList3.get(0).get("id");
				setDomainIdCache(siteId,domain,domainId);
			}
			return domainId;
		}
	}

	public void afterPropertiesSet() throws Exception {
		log.debug("重新加载域名数据");
		Map domainMap= new HashMap<Integer,Map<String,Integer>>();
		
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select id,domain,siteId  from ada_domain");
		List<Map<String, Object>> siteList = jdbcTemplate.queryForList("select id from ada_site");
		
		if(queryForList!=null && queryForList.size()>0 && siteList!=null && siteList.size()>0){
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			for(int i=0;i<siteList.size();i++){//循环所有站点
				Integer siteId = (Integer) siteList.get(i).get("id");
				Map<String,Integer> maps = new HashMap<String,Integer>();
				for(int j=0;j<queryForList.size();j++){//循环所有域名
					Map cMap = queryForList.get(j);
					if(siteId.toString().equals(cMap.get("siteId").toString())){
						maps.put(cMap.get("domain").toString(),Integer.valueOf(cMap.get("id").toString()));
					}
				}
				domainMap.put(siteId, maps);
			}
		}
		
		this.domainMap = domainMap;
	}
	
	
	


}

