package com.yorbee.qgs.bigdata.hbase.dsmt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.phoenix.jdbc.PhoenixResultSet;

import com.yorbee.qgs.bigdata.hbase.client.PhoenixClientConnection;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;
import com.yorbee.qgs.bigdata.hbase.entity.EventLog;
import com.yorbee.qgs.bigdata.hbase.entity.EventLogToSql;

public class EventLogStateMentMgt extends StatementMgt{
	private final static Logger logger = Logger.getLogger(EventLogStateMentMgt.class.getName());
	public int batchAddEventLog(List<EventLog> accessLoglist) {
		Connection conn =null;
        Statement stmt =null;
		int counts=0;
		try {
			// 耗时监控：记录一个开始时间
			long startTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			if (conn == null) {
				logger.info("连接执行出错：conn == null");
				return -1;
			}
			// 准备查询
			stmt = conn.createStatement();
			for(EventLog accessLog:accessLoglist) {
				String phoenixSQL="";
				phoenixSQL=EventLogToSql.insertStr(accessLog);
				stmt.addBatch(phoenixSQL);
			}
			stmt.executeBatch();
			conn.commit();
			
			// 耗时监控：记录一个结束时间
			long endTime = System.currentTimeMillis();
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQL执行出错：" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("出错：" + e.getMessage());
		}finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        	stmt=null;
        	conn=null;
        }
		return counts;
	}
	
	public List<EventLog> queryEventLogByfindBySiteIdAndEvent(Integer siteId, String event, Integer pageSize, Integer pageNo){
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
		List<EventLog>  eventLogList=new ArrayList<EventLog> ();
		try {
			// 耗时监控：记录一个开始时间
			long startTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
 			String phoenixSQL="select siteId,domainId,channelId,adId,ipAddress,region,uuid,url,event,args,requestTime from ADA_EVENT_LOG where siteId="+siteId+" and event='"+event+"'  LIMIT "+pageSize+" OFFSET "+pageNo+" ";
			logger.info(phoenixSQL);
			System.out.println(phoenixSQL);
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

			while (set.next()) {
				
				EventLog eventLog = new EventLog();
				eventLog.setSiteId(set.getInt("siteId"));
				eventLog.setDomainId(set.getInt("domainId"));
				eventLog.setChannelId(set.getInt("channelId"));
				eventLog.setAdId(set.getInt("adId"));
				eventLog.setIpAddress(set.getString("ipAddress"));
				eventLog.setRegion(set.getString("region"));
				eventLog.setUuid(set.getString("uuid"));
				eventLog.setUrl(set.getString("url"));
				eventLog.setEvent(set.getString("event"));
				eventLog.setArgs(set.getString("args"));
				eventLog.setRequestTime(set.getTimestamp("requestTime").getTime());
				eventLogList.add(eventLog);
		 
			}

			long endTime = System.currentTimeMillis();
            
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQL执行出错：" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("出错：" + e.getMessage());
		}finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			set = null;
			stmt = null;
			conn = null;
		}
		return eventLogList;
	}
	
	public List<EventLog> queryEventLogByDomainIdAndEvent(Integer domainId, String event, Integer pageSize, Integer pageNo){
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
		List<EventLog>  eventLogList=new ArrayList<EventLog> ();
		try {
			// 耗时监控：记录一个开始时间
			long startTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
 			String phoenixSQL="select siteId,domainId,channelId,adId,ipAddress,region,uuid,url,event,args,requestTime from ADA_EVENT_LOG where domainId="+domainId+" and event='"+event+"'  LIMIT "+pageSize+" OFFSET "+pageNo+" ";
			logger.info(phoenixSQL);
			System.out.println(phoenixSQL);
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

			while (set.next()) {
				
				EventLog eventLog = new EventLog();
				eventLog.setSiteId(set.getInt("siteId"));
				eventLog.setDomainId(set.getInt("domainId"));
				eventLog.setChannelId(set.getInt("channelId"));
				eventLog.setAdId(set.getInt("adId"));
				eventLog.setIpAddress(set.getString("ipAddress"));
				eventLog.setRegion(set.getString("region"));
				eventLog.setUuid(set.getString("uuid"));
				eventLog.setUrl(set.getString("url"));
				eventLog.setEvent(set.getString("event"));
				eventLog.setArgs(set.getString("args"));
				eventLog.setRequestTime(set.getTimestamp("requestTime").getTime());
				eventLogList.add(eventLog);
		 
			}

			long endTime = System.currentTimeMillis();
            
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQL执行出错：" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("出错：" + e.getMessage());
		}finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			set = null;
			stmt = null;
			conn = null;
		}
		return eventLogList;
	}
	
	public Integer countBySiteIdAndEvent(Integer siteId, String event) {
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
		List<AccessLog>  AccessLogList=new ArrayList<AccessLog> ();
		try {
			// 耗时监控：记录一个开始时间
			long startTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
 			String phoenixSQL="select count(siteId) as sum_ct from ADA_EVENT_LOG where siteId="+siteId+" and event='"+event+"' ";
			logger.info(phoenixSQL);
			System.out.println(phoenixSQL);
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);
					
			while (set.next()) {
				retInteger=set.getInt("sum_ct");
			}

			long endTime = System.currentTimeMillis();
            
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQL执行出错：" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("出错：" + e.getMessage());
		}finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			set = null;
			stmt = null;
			conn = null;
		}
		return retInteger;
	}

	public Integer countByDomainIdAndEvent(Integer domainId, String event) {
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
		List<AccessLog>  AccessLogList=new ArrayList<AccessLog> ();
		try {
			// 耗时监控：记录一个开始时间
			long startTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
 			String phoenixSQL="select count(siteId) as sum_ct from ADA_EVENT_LOG where domainId="+domainId+" and event='"+event+"' ";
			logger.info(phoenixSQL);
			System.out.println(phoenixSQL);
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);
					
			while (set.next()) {
				retInteger=set.getInt("sum_ct");
			}

			long endTime = System.currentTimeMillis();
            
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQL执行出错：" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("出错：" + e.getMessage());
		}finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			set = null;
			stmt = null;
			conn = null;
		}
		return retInteger;
	}
	
	public List<EventLog> queryEventLogByTime(Timestamp startTime, Timestamp endTime, Integer pageSize, Integer pageNo){
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
		List<EventLog>  eventLogList=new ArrayList<EventLog> ();
		try {
			// 耗时监控：记录一个开始时间
			long beginTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
 			String phoenixSQL="select siteId,domainId,channelId,adId,ipAddress,region,uuid,url,event,args,requestTime from ADA_EVENT_LOG where createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') LIMIT "+pageSize+" OFFSET "+pageNo+" ";
			logger.info(phoenixSQL);
			System.out.println(phoenixSQL);
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

			while (set.next()) {
				
				EventLog eventLog = new EventLog();
				eventLog.setSiteId(set.getInt("siteId"));
				eventLog.setDomainId(set.getInt("domainId"));
				eventLog.setChannelId(set.getInt("channelId"));
				eventLog.setAdId(set.getInt("adId"));
				eventLog.setIpAddress(set.getString("ipAddress"));
				eventLog.setRegion(set.getString("region"));
				eventLog.setUuid(set.getString("uuid"));
				eventLog.setUrl(set.getString("url"));
				eventLog.setEvent(set.getString("event"));
				eventLog.setArgs(set.getString("args"));
				eventLog.setRequestTime(set.getTimestamp("requestTime").getTime());
				eventLogList.add(eventLog);
		 
			}

			long eTime = System.currentTimeMillis();
            
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("SQL执行出错：" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("出错：" + e.getMessage());
		}finally{
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			set = null;
			stmt = null;
			conn = null;
		}
		return eventLogList;
	}
	
	public void deleteEventLogByTime(Timestamp startTime, Timestamp endTime) {
		String phoenixSQL="delete from ADA_EVENT_LOG where createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"')";
		execUpdateSql(phoenixSQL);
	}
}
