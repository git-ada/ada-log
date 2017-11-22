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

import com.ada.log.service.ChannelService;
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
	private  Map<Integer,List<Map>> domainMap;//map<站点id,域名list<域名id,<字段名，字段值>>>
	
	@Override
	public Integer queryDomain(Integer siteId,String domain) {
		List<Map> list = this.domainMap.get(siteId);
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map = list.get(i);
				if(domain!=null && domain.equals((String)map.get("domain"))){
					return (Integer) map.get("id");
				}else{
					Integer resloutID = jdbcTemplate.queryForObject("select id from ada_domain where siteId=? and domain=?", 
							new Object[] {siteId,domain }, Integer.class); 
					return resloutID;
				}
			}
		}
		
		return null;
	}

//	@Scheduled(cron="0 0/5 * * * ?")  
	@Scheduled(cron="0/5 * * * * ?") //每1秒执行一次  
	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("重新加载域名数据");
		domainMap= new HashMap<Integer, List<Map>>();
		
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select id,domain,siteId  from ada_domain");
		List<Map<String, Object>> siteList = jdbcTemplate.queryForList("select id from ada_site");
		
		if(queryForList!=null && queryForList.size()>0 && siteList!=null && siteList.size()>0){
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			for(int i=0;i<siteList.size();i++){//循环所有站点
				Integer siteId = (Integer) siteList.get(i).get("id");
				List<Map> maps = new ArrayList<Map>();
				for(int j=0;j<queryForList.size();j++){//循环所有域名
					Map cMap = queryForList.get(j);
					if(siteId.toString().equals(cMap.get("siteId").toString())){
						maps.add(cMap);
					}
				}
				domainMap.put(siteId, maps);
			}
		}
	}
	
	@Override
	public void addDomain(Integer siteId, String domain) {
//		domain="www.baidu.com";
		jdbcTemplate.execute("insert into ada_domain(siteId,domain,createTime) values("+1000+",'"+domain+"',now())");
//		jdbcTemplate.update("insert into ada_domain values(?,?,now())", siteId,domain);
	}


}

