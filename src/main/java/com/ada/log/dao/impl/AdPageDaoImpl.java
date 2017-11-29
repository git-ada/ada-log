package com.ada.log.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ada.log.bean.ADPage;
import com.ada.log.dao.AdPageDao;

@Service
public class AdPageDaoImpl implements AdPageDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ADPage> findBySiteId(Integer siteId) {
		List<ADPage> adpages = jdbcTemplate.queryForList("select * from ada_ad_page where siteId = ?", ADPage.class, siteId);
		return adpages;
	}
}
