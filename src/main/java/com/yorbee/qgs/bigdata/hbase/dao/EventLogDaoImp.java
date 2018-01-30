package com.yorbee.qgs.bigdata.hbase.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.yorbee.qgs.bigdata.hbase.dsmt.EventLogStateMentMgt;
import com.yorbee.qgs.bigdata.hbase.entity.EventLog;

public class EventLogDaoImp implements EventLogDao{

	public void batchInsert(List<EventLog> eventLoglist) {
		// TODO Auto-generated method stub
		EventLogStateMentMgt smgt=new EventLogStateMentMgt();
		smgt.init(Comms.HB_HOST, Comms.HB_PORT);
		smgt.batchAddEventLog(eventLoglist);
	}

	public List<EventLog> findBySiteIdAndEvent(Integer siteId, String event, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		List<EventLog> eventLoglist=new ArrayList<EventLog>();
		EventLogStateMentMgt smgt=new EventLogStateMentMgt();
 
		smgt.init(Comms.HB_HOST, Comms.HB_PORT);
		eventLoglist=smgt.queryEventLogByfindBySiteIdAndEvent(siteId,event, pageSize, pageNo);
		return eventLoglist;
	}

	public List<EventLog> findByDomainIdAndEvent(Integer domainId, String event, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		List<EventLog> eventLoglist=new ArrayList<EventLog>();
		EventLogStateMentMgt smgt=new EventLogStateMentMgt();
 
		smgt.init(Comms.HB_HOST, Comms.HB_PORT);
		eventLoglist=smgt.queryEventLogByDomainIdAndEvent(domainId,event, pageSize, pageNo);
		return eventLoglist;
	}

	public Integer countBySiteIdAndEvent(Integer siteId, String event) {
		// TODO Auto-generated method stub
		EventLogStateMentMgt smgt=new EventLogStateMentMgt();
		smgt.init(Comms.HB_HOST, Comms.HB_PORT);
		return smgt.countBySiteIdAndEvent(siteId, event);
	}

	public Integer countByDomainIdAndEvent(Integer domainId, String event) {
		// TODO Auto-generated method stub
		EventLogStateMentMgt smgt=new EventLogStateMentMgt();
		smgt.init(Comms.HB_HOST, Comms.HB_PORT);
		return smgt.countByDomainIdAndEvent(domainId, event);
	}

	public List<EventLog> findByTime(Timestamp startTime, Timestamp endTime, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		List<EventLog> eventLoglist=new ArrayList<EventLog>();
		EventLogStateMentMgt smgt=new EventLogStateMentMgt();
 
		smgt.init(Comms.HB_HOST, Comms.HB_PORT);
		eventLoglist=smgt.queryEventLogByTime(startTime,endTime, pageSize, pageNo);
		return eventLoglist;
	}

	public void deleteByTime(Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		EventLogStateMentMgt smgt=new EventLogStateMentMgt();
		smgt.init(Comms.HB_HOST, Comms.HB_PORT);
		smgt.deleteEventLogByTime(startTime, endTime);
	}

}
