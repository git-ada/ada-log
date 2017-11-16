package com.ada.log.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ada.log.bean.Channel;
import com.ada.log.bean.Site;
import com.ada.log.dao.SiteDao;
@Service
public class SiteDaoImpl implements SiteDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Site> findAll() {
		// TODO Auto-generated method stub
		List<Site> list = new ArrayList<Site>();
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select id,siteName from ada_site");
		if(queryForList!=null && queryForList.size()>0){
			for(int i=0;i<queryForList.size();i++){
				Map map = queryForList.get(i);
				Site site = new Site();
				site.setId((Integer) map.get("id"));
				site.setSiteName((String) map.get("siteName"));
				
				list.add(site);
			}
		}
		
		return list;
	}

}
