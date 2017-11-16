package com.ada.log.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ada.log.bean.SiteStat;
import com.ada.log.dao.SiteStatDao;

@Service
public class SiteStatDaoImpl implements SiteStatDao {
	
	private final static Log log = LogFactory.getLog(SiteStatDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public void save(SiteStat item) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("insert into ada_site_stat(siteId,ip,pv,date,createTime) VALUES(?,?,?,?,now())",
				item.getSiteId(),
				item.getIp(),
				item.getPv(),
				item.getDate());
		if(log.isDebugEnabled()){
			log.info("保存站点统计结果成功");
		}

	}

}
