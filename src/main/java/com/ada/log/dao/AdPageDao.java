package com.ada.log.dao;

import java.util.List;

import com.ada.log.bean.ADPage;
public interface AdPageDao {

	public List<ADPage> findBySiteId(Integer siteId);
}
