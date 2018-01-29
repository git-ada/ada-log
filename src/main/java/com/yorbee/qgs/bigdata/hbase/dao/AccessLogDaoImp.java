package com.yorbee.qgs.bigdata.hbase.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ada.log.bean.EventLog;
import com.ada.log.dao.AccessLogDao;
import com.yorbee.qgs.bigdata.hbase.dsmt.StatementMgt;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;

@Service
public class AccessLogDaoImp implements AccessLogDao{
	@Value("${phoenix.host:}")
	String _host;
	@Value("${phoenix.port:}")
	String _port;
	public void batchInsert(List<AccessLog> accessLoglist) {
		// TODO Auto-generated method stub
		StatementMgt smgt=new StatementMgt();
		smgt.init(_host, _port);
		smgt.batchAddAccessLog(accessLoglist);
	}

	public List<AccessLog> findBySiteId(Integer siteId, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		List<AccessLog> accesslogList=new ArrayList<AccessLog>();
		StatementMgt smgt=new StatementMgt();
		smgt.init(_host, _port);
		accesslogList=smgt.queryAccesslog(siteId, pageSize, pageNo);
		return accesslogList;
	}

	@Override
	public void batchInsertEventLog(List<EventLog> logs) {
		// TODO Auto-generated method stub
		
	}

}
