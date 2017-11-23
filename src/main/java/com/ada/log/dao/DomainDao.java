package com.ada.log.dao;

import java.util.List;
import com.ada.log.bean.Domain;

/**
 * 
 * @author Zhao xiang
 * @since  2017/11/21
 * 
 */
public interface DomainDao {

	/**
	 * 通过站点ID查询下属所有域名
	 * @param siteId
	 * @return
	 */
	public List<Domain> findBySiteId(Integer siteId);
	
	
}
