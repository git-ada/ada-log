package com.ada.log.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.ada.log.bean.Channel;
import com.ada.log.bean.Domain;
import com.ada.log.dao.DomainDao;
/**
 * 
 * @author zhao xiang
 * @since  2017/11/21
 * 
 */

@Service
@SuppressWarnings("all")
public class DomainDaoImpl implements DomainDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	public List<Domain> findBySiteId(Integer siteId) {
		// TODO Auto-generated method stub
		List<Domain> domains = new ArrayList<Domain>();
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select id,siteId,domain from ada_domain where siteId=?",siteId);
		if(queryForList!=null && queryForList.size()>0){
			for(int i=0;i<queryForList.size();i++){
				Map map = queryForList.get(i);
				Domain domain = new Domain();
				domain.setId((Integer) map.get("id"));
				domain.setSiteId((Integer) map.get("siteId"));
				domain.setDomain((String) map.get("domain"));
				domains.add(domain);
			}
		}
		
		return domains;
	}

}
