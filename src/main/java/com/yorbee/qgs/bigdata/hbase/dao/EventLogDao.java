package com.yorbee.qgs.bigdata.hbase.dao;

import java.sql.Timestamp;
import java.util.List;

import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;
import com.yorbee.qgs.bigdata.hbase.entity.EventLog;

public interface EventLogDao {

	void batchInsert(List<EventLog> logs);

	List<EventLog> findBySiteIdAndEvent(Integer siteId, String event, Integer pageSize, Integer pageNo);

	List<EventLog> findByDomainIdAndEvent(Integer domainId, String event, Integer pageSize, Integer pageNo);

	Integer countBySiteIdAndEvent(Integer siteId, String event);

	Integer countByDomainIdAndEvent(Integer domainId, String event);

	List<EventLog> findByTime(Timestamp startTime, Timestamp endTime, Integer pageSize, Integer pageNo);

	void deleteByTime(Timestamp startTime, Timestamp endTime);

}
