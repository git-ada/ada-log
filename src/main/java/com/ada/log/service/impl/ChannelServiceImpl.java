package com.ada.log.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.ada.log.job.ArchiveJob;
import com.ada.log.service.ChannelService;

/**
 * 渠道服务实现类
 */
@Service
public class ChannelServiceImpl implements ChannelService,InitializingBean {
	
	private final static Log log = LogFactory.getLog(ChannelServiceImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	private  Map<Integer,List<Map>> channelMap;//map<站点id,渠道list<渠道id,<字段名，字段值>>>
	
	@Override
	public Integer queryChannel(Integer siteId, String browsingPage) {
		try {
			browsingPage = URLDecoder.decode(browsingPage, "utf-8");
			List<Map> list = this.channelMap.get(siteId);
			String str = browsingPage.substring(browsingPage.indexOf("?")+1);
			String[] s = str.split("&");
			if(list!=null && list.size()>0){

				for(int i=0;i<list.size();i++){
					Map map = list.get(i);
					if(browsingPage.startsWith((String)map.get("url"))){//匹配url
						if(s.length>=1){
							//匹配参数parameter/
							String parameter = (String) map.get("parameter");
							for(int j=0;j<s.length;j++){
								String type = s[j];
								if(parameter!=null && parameter.equals(type)){
									return (Integer) map.get("channelId");
								}
							}
							if(map.get("parameter")==null || "".equals(map.get("parameter"))){
								return (Integer) map.get("channelId");
							}
							
						}else{
							if(map.get("parameter")==null || "".equals(map.get("parameter"))){
								return (Integer) map.get("channelId");
							}
						}
						
						
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}

	@Scheduled(cron="0 0/5 * * * ?")   //每5分钟执行一次  
	@Override
	public void afterPropertiesSet() throws Exception {
		channelMap= new HashMap<Integer, List<Map>>();
		
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select channelId,url,parameter,siteId  from ada_channel_link");
		List<Map<String, Object>> siteList = jdbcTemplate.queryForList("select id from ada_site");
		
		if(queryForList!=null && queryForList.size()>0 && siteList!=null && siteList.size()>0){
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			for(int i=0;i<siteList.size();i++){//循环所有站点
				Integer siteId = (Integer) siteList.get(i).get("id");
				List<Map> maps = new ArrayList<Map>();
				for(int j=0;j<queryForList.size();j++){//循环所有渠道
					Map cMap = queryForList.get(j);
					if(siteId.toString().equals(cMap.get("siteId").toString())){
						maps.add(cMap);
					}
				}
				
				channelMap.put(siteId, maps);
			}
			
		}
	}


}
