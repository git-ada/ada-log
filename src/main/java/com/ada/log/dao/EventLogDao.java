package com.ada.log.dao;

import java.sql.Timestamp;
import java.util.List;

import com.ada.log.bean.EventLog;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;

public interface EventLogDao {

	void batchInsert(List<EventLog> logs);
	
	List<EventLog> findBySiteIdAndEvent(Integer siteId,String event,Integer pageSize,Integer pageNo);
	
	List<EventLog> findByDomainIdAndEvent(Integer domainId,String event,Integer pageSize,Integer pageNo);
	
	Integer countBySiteIdAndEvent(Integer siteId,String event);
	Integer countByDomainIdAndEvent(Integer domainId,String event);
	
	List<AccessLog> findByTime(Timestamp startTime, Timestamp endTime,Integer pageSize,Integer pageNo);
	void deleteByTime(Timestamp startTime, Timestamp endTime);
	
}
