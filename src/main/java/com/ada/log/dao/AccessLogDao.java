package com.ada.log.dao;

import java.util.List;

import com.ada.log.bean.AccessLog;

public interface AccessLogDao {

	
	public void batchInsert(List<AccessLog> logs);
}
