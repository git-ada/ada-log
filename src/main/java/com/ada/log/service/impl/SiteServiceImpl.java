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

import com.ada.log.service.SiteService;
import com.alibaba.fastjson.JSON;
/**
 * 
 *站点服务实现类
 */
@Service
public class SiteServiceImpl implements SiteService,InitializingBean{
	
	private final static Log log = LogFactory.getLog(SiteServiceImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private Map<Integer, List<Map>> targetMap;//map<站点id,目标页面list<map>>
	/**
	 * 匹配是否目标页
	 * @param siteId
	 * @param browsingPage 
	 * @return
	 */
	public boolean matchTargetPage(Integer siteId,String browsingPage){
		
		try {
			browsingPage = URLDecoder.decode(browsingPage, "utf-8");
			List<Map> list = targetMap.get(siteId);
			
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Integer matchModel = (Integer) list.get(i).get("matchMode");
					String url = (String) list.get(i).get("url");
					
					log.debug("browsingPage->"+browsingPage+",url->"+url+",matchModel->"+matchModel);
					
					if(browsingPage!=null && matchModel==1 && browsingPage.trim().equals(url.trim())){/**全匹配**/
						
						log.debug("browsingPage->"+browsingPage+",url->"+url+",matchModel->"+matchModel);
						return true;
					}else if(browsingPage!=null && matchModel==2 && browsingPage.trim().startsWith(url.trim())){/**前缀匹配**/
						
						log.debug("browsingPage->"+browsingPage+",url->"+url+",matchModel->"+matchModel);
						return true;
					}else if(browsingPage!=null && matchModel==3 && browsingPage.trim().indexOf(url.trim())!=-1){/**模糊匹配**/
						
						log.debug("browsingPage->"+browsingPage+",url->"+url+",matchModel->"+matchModel);
						return true;
					}
					
				}
				
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
		return false;
	}

	@Scheduled(cron="0/10 * * * * ?")   //每5分钟执行一次  
	@Override
	public void afterPropertiesSet() throws Exception {
		Map newtargetMap = new HashMap<Integer, List<Map>>();
		
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select siteId,url,matchMode from ada_target_page");
		List<Map<String, Object>> siteList = jdbcTemplate.queryForList("select id from ada_site");
		
		if(queryForList!=null && queryForList.size()>0 && siteList!=null && siteList.size()>0){
			for(int i=0;i<siteList.size();i++){//循环所有站点
				Integer siteId = (Integer) siteList.get(i).get("id");
				List<Map> maps = new ArrayList<Map>();
				for(int j=0;j<queryForList.size();j++){//循环所有目标页
					Map tMap = queryForList.get(j);
					if(siteId.toString().equals(tMap.get("siteId").toString())){
						maps.add(tMap);
					}
				}
				//log.debug("加载目标页配置,siteId->"+siteId+",data->"+JSON.toJSONString(maps));
				newtargetMap.put(siteId, maps);
			}
		}
		this.targetMap = newtargetMap;
	}

}
