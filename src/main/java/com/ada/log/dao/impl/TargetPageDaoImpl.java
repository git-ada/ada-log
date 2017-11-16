package com.ada.log.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ada.log.bean.Site;
import com.ada.log.bean.TargetPage;
import com.ada.log.dao.TargetPageDao;
@Service
public class TargetPageDaoImpl implements TargetPageDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<TargetPage> findBySiteId(Integer siteId) {
		// TODO Auto-generated method stub
		List<TargetPage> list = new ArrayList<TargetPage>();
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select siteId,url from ada_target_page where siteId=?",siteId);
		if(queryForList!=null && queryForList.size()>0){
			for(int i=0;i<queryForList.size();i++){
				Map map = queryForList.get(i);
				TargetPage targetPage = new TargetPage();
				targetPage.setSiteId((Integer) map.get("siteId"));
				targetPage.setUrl((String) map.get("url"));
				
				list.add(targetPage);
			}
		}
		
		return list;
	}

}
