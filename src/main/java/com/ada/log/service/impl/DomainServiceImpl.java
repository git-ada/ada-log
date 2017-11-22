package com.ada.log.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

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
					try{
						List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select * from ada_domain  where siteId=? and domain=? ",siteId,domain);
						if(queryForList != null&& queryForList.size()>0){
							return (Integer)queryForList.get(0).get("id");
						}else{
							jdbcTemplate.execute("insert into ada_domain(siteId,domain,createTime) values("+siteId+",'"+domain+"',now())");
							List<Map<String, Object>> queryForList2 = jdbcTemplate.queryForList("select * from ada_domain where siteId=? and domain=? ",siteId,domain);
							if(queryForList2 != null&& queryForList2.size()>0){
								Map newMap = new HashMap();
								List newList = new ArrayList();
								newMap.put("id", (Integer)queryForList2.get(0).get("id"));
								newMap.put("domain", domain);
								newMap.put("siteId", siteId);
								newList.add(newMap);
								domainMap.put(siteId, newList);
								return (Integer)queryForList2.get(0).get("id");
							}
						}
					}catch(Exception e){
						log.error("查询域名ID错误 "+ siteId+"-->>"+domain);
					}
					
				}
			}
		}else{
			jdbcTemplate.execute("insert into ada_domain(siteId,domain,createTime) values("+siteId+",'"+domain+"',now())");
			List<Map<String, Object>> queryForList3 = jdbcTemplate.queryForList("select * from ada_domain where siteId=? and domain=? ",siteId,domain);
			if(queryForList3 != null && queryForList3.size()>0){
				Map newMap = new HashMap();
				List newList = new ArrayList();
				newMap.put("id", (Integer)queryForList3.get(0).get("id"));
				newMap.put("domain", domain);
				newMap.put("siteId", siteId);
				newList.add(newMap);
				domainMap.put(siteId, newList);
				return (Integer)queryForList3.get(0).get("id");
			}
		}
		return null;
	}

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
	
	
	


}

