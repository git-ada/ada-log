package com.ada.log.dao;

import java.util.List;

import com.ada.log.bean.AccessLog;
import com.ada.log.bean.EventLog;

public interface AccessLogDao {

	
	/**
	 * 批量插入，每次插入不超过5000条
	 * @param logs
	 */
	void batchInsert(List<AccessLog> logs);
	
	/**
	 * 通过站点ID查询日志
	 * @param siteId 站点ID
	 * @param pageSize 单页条数
	 * @param pageNo 页码,从0开始,
	 * @return
	 */
	List<AccessLog> findBySiteId(Integer siteId,Integer pageSize,Integer pageNo);

	void batchInsertEventLog(List<EventLog> logs);
}
