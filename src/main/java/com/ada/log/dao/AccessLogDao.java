package com.ada.log.dao;

import java.util.List;

import com.ada.log.bean.AccessLog;
import com.ada.log.bean.EventLog;

public interface AccessLogDao {

	
	void batchInsert(List<AccessLog> logs);

	void batchInsertEventLog(List<EventLog> logs);
}
