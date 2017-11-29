package com.ada.log.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ada.log.bean.ADPage;
import com.ada.log.bean.ChannelLink;
import com.ada.log.dao.AdPageDao;

@Service
public class AdPageDaoImpl implements AdPageDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ADPage> findBySiteId(Integer siteId) {
		
		List<ADPage> list = new ArrayList<ADPage>();
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select id,siteId,channelKey,matchContent from ada_ad_page where siteId = ?",siteId);
		if(queryForList!=null && queryForList.size()>0){
			for(int i=0;i<queryForList.size();i++){
				Map map = queryForList.get(i);
				ADPage ad = new ADPage();
				ad.setSiteId((Integer) map.get("siteId"));
				ad.setMatchContent((String) map.get("matchContent"));
				ad.setChannelKey((String) map.get("channelKey"));
				ad.setId((Integer) map.get("id"));
				list.add(ad);
			}
		}
		
		
		return list;
	}
}
