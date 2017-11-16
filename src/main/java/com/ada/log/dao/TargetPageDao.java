package com.ada.log.dao;

import java.util.List;

import com.ada.log.bean.TargetPage;

public interface TargetPageDao {
	
	/**
	 * 查询站点链接
	 * @param siteId
	 * @return
	 */
	public List<TargetPage> findBySiteId(Integer siteId);
}
