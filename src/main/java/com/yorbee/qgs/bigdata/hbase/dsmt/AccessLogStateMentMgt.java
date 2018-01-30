package com.yorbee.qgs.bigdata.hbase.dsmt;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.phoenix.jdbc.PhoenixResultSet;

import com.yorbee.qgs.bigdata.hbase.client.PhoenixClientConnection;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLogToSql;

public class AccessLogStateMentMgt extends StatementMgt{
	private final static Logger logger = Logger.getLogger(AccessLogStateMentMgt.class.getName());
	public List<AccessLog> queryAccesslog(Integer siteId,Integer pageSize,Integer pageNo) {

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
 			String phoenixSQL="select siteId,domainId,channelId,adId,entranceType,ipAddress,region,uuid,url,useragent,os,browser,screenSize,pageSize,referer,iframe,firstTime,todayTime,requestTime from ADA_ACCESS_LOG where siteId="+siteId+"  LIMIT "+pageSize+" OFFSET "+pageNo+" ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

			while (set.next()) {
				
				AccessLog accessLog = new AccessLog();
				accessLog.setSiteId(set.getInt("siteId"));
				accessLog.setDomainId(set.getInt("domainId"));
				accessLog.setChannelId(set.getInt("channelId"));
				accessLog.setAdId(set.getInt("adId"));
				accessLog.setEntranceType(set.getInt("entranceType"));
				accessLog.setIpAddress(set.getString("ipAddress"));
				accessLog.setRegion(set.getString("region"));
				accessLog.setUuid(set.getString("uuid"));
				accessLog.setUrl(set.getString("url"));
				accessLog.setUseragent(set.getString("useragent"));
				accessLog.setOs(set.getString("os"));
				accessLog.setBrowser(set.getString("browser"));
				accessLog.setScreenSize(set.getString("screenSize"));
				accessLog.setPageSize(set.getString("pageSize"));
				accessLog.setReferer(set.getString("referer"));
				accessLog.setIframe(set.getInt("iframe"));
				accessLog.setFirstTime(set.getTimestamp("firstTime").getTime());
				accessLog.setTodayTime(set.getTimestamp("todayTime").getTime());
				accessLog.setRequestTime(set.getTimestamp("requestTime").getTime());
				AccessLogList.add(accessLog);
		 
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
		return AccessLogList;
	}
	
	public List<AccessLog> queryAccesslogByDomainId(Integer domainId,Integer pageSize,Integer pageNo) {

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
 			String phoenixSQL="select siteId,domainId,channelId,adId,entranceType,ipAddress,region,uuid,url,useragent,os,browser,screenSize,pageSize,referer,iframe,firstTime,todayTime,requestTime from ADA_ACCESS_LOG where domainId="+domainId+"  LIMIT "+pageSize+" OFFSET "+pageNo+" ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

	
			while (set.next()) {
				
				AccessLog accessLog = new AccessLog();
				accessLog.setSiteId(set.getInt("siteId"));
				accessLog.setDomainId(set.getInt("domainId"));
				accessLog.setChannelId(set.getInt("channelId"));
				accessLog.setAdId(set.getInt("adId"));
				accessLog.setEntranceType(set.getInt("entranceType"));
				accessLog.setIpAddress(set.getString("ipAddress"));
				accessLog.setRegion(set.getString("region"));
				accessLog.setUuid(set.getString("uuid"));
				accessLog.setUrl(set.getString("url"));
				accessLog.setUseragent(set.getString("useragent"));
				accessLog.setOs(set.getString("os"));
				accessLog.setBrowser(set.getString("browser"));
				accessLog.setScreenSize(set.getString("screenSize"));
				accessLog.setPageSize(set.getString("pageSize"));
				accessLog.setReferer(set.getString("referer"));
				accessLog.setIframe(set.getInt("iframe"));
				accessLog.setFirstTime(set.getTimestamp("firstTime").getTime());
				accessLog.setTodayTime(set.getTimestamp("todayTime").getTime());
				accessLog.setRequestTime(set.getTimestamp("requestTime").getTime());
				AccessLogList.add(accessLog);
		 
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
		return AccessLogList;
	}
	
	public List<AccessLog> queryAccesslogBySiteIdAndIp(Integer siteId, String ipAddress, Integer pageSize, Integer pageNo){
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
 			String phoenixSQL="select siteId,domainId,channelId,adId,entranceType,ipAddress,region,uuid,url,useragent,os,browser,screenSize,pageSize,referer,iframe,firstTime,todayTime,requestTime from ADA_ACCESS_LOG where siteId="+siteId+" and ipAddress="+ipAddress+" LIMIT "+pageSize+" OFFSET "+pageNo+" ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);
 
					
			while (set.next()) {
				
				AccessLog accessLog = new AccessLog();
				accessLog.setSiteId(set.getInt("siteId"));
				accessLog.setDomainId(set.getInt("domainId"));
				accessLog.setChannelId(set.getInt("channelId"));
				accessLog.setAdId(set.getInt("adId"));
				accessLog.setEntranceType(set.getInt("entranceType"));
				accessLog.setIpAddress(set.getString("ipAddress"));
				accessLog.setRegion(set.getString("region"));
				accessLog.setUuid(set.getString("uuid"));
				accessLog.setUrl(set.getString("url"));
				accessLog.setUseragent(set.getString("useragent"));
				accessLog.setOs(set.getString("os"));
				accessLog.setBrowser(set.getString("browser"));
				accessLog.setScreenSize(set.getString("screenSize"));
				accessLog.setPageSize(set.getString("pageSize"));
				accessLog.setReferer(set.getString("referer"));
				accessLog.setIframe(set.getInt("iframe"));
				accessLog.setFirstTime(set.getTimestamp("firstTime").getTime());
				accessLog.setTodayTime(set.getTimestamp("todayTime").getTime());
				accessLog.setRequestTime(set.getTimestamp("requestTime").getTime());
				AccessLogList.add(accessLog);
		 
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
		return AccessLogList;
	}
	
	public List<AccessLog> queryAccesslogByDomainIdAndIp(Integer domainId, String ipAddress, Integer pageSize, Integer pageNo){
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
 			String phoenixSQL="select siteId,domainId,channelId,adId,entranceType,ipAddress,region,uuid,url,useragent,os,browser,screenSize,pageSize,referer,iframe,firstTime,todayTime,requestTime from ADA_ACCESS_LOG where domainId="+domainId+" and ipAddress="+ipAddress+" LIMIT "+pageSize+" OFFSET "+pageNo+" ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

					
			while (set.next()) {
				
				AccessLog accessLog = new AccessLog();
				accessLog.setSiteId(set.getInt("siteId"));
				accessLog.setDomainId(set.getInt("domainId"));
				accessLog.setChannelId(set.getInt("channelId"));
				accessLog.setAdId(set.getInt("adId"));
				accessLog.setEntranceType(set.getInt("entranceType"));
				accessLog.setIpAddress(set.getString("ipAddress"));
				accessLog.setRegion(set.getString("region"));
				accessLog.setUuid(set.getString("uuid"));
				accessLog.setUrl(set.getString("url"));
				accessLog.setUseragent(set.getString("useragent"));
				accessLog.setOs(set.getString("os"));
				accessLog.setBrowser(set.getString("browser"));
				accessLog.setScreenSize(set.getString("screenSize"));
				accessLog.setPageSize(set.getString("pageSize"));
				accessLog.setReferer(set.getString("referer"));
				accessLog.setIframe(set.getInt("iframe"));
				accessLog.setFirstTime(set.getTimestamp("firstTime").getTime());
				accessLog.setTodayTime(set.getTimestamp("todayTime").getTime());
				accessLog.setRequestTime(set.getTimestamp("requestTime").getTime());
				AccessLogList.add(accessLog);
		 
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
		return AccessLogList;
	}
	
	public List<AccessLog> queryAccesslogBySiteIdAndUrlLike(Integer siteId, String url){
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
 			String phoenixSQL="select siteId,domainId,channelId,adId,entranceType,ipAddress,region,uuid,url,useragent,os,browser,screenSize,pageSize,referer,iframe,firstTime,todayTime,requestTime from ADA_ACCESS_LOG where siteId="+siteId+" and url="+url+" ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);
 
			while (set.next()) {
				
				AccessLog accessLog = new AccessLog();
				accessLog.setSiteId(set.getInt("siteId"));
				accessLog.setDomainId(set.getInt("domainId"));
				accessLog.setChannelId(set.getInt("channelId"));
				accessLog.setAdId(set.getInt("adId"));
				accessLog.setEntranceType(set.getInt("entranceType"));
				accessLog.setIpAddress(set.getString("ipAddress"));
				accessLog.setRegion(set.getString("region"));
				accessLog.setUuid(set.getString("uuid"));
				accessLog.setUrl(set.getString("url"));
				accessLog.setUseragent(set.getString("useragent"));
				accessLog.setOs(set.getString("os"));
				accessLog.setBrowser(set.getString("browser"));
				accessLog.setScreenSize(set.getString("screenSize"));
				accessLog.setPageSize(set.getString("pageSize"));
				accessLog.setReferer(set.getString("referer"));
				accessLog.setIframe(set.getInt("iframe"));
				accessLog.setFirstTime(set.getTimestamp("firstTime").getTime());
				accessLog.setTodayTime(set.getTimestamp("todayTime").getTime());
				accessLog.setRequestTime(set.getTimestamp("requestTime").getTime());
				AccessLogList.add(accessLog);
		 
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
		return AccessLogList;
	}
	
	
	public List<AccessLog> queryAccesslogByDomainIdAndUrlLike(Integer domainId, String url){
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
 			String phoenixSQL="select siteId,domainId,channelId,adId,entranceType,ipAddress,region,uuid,url,useragent,os,browser,screenSize,pageSize,referer,iframe,firstTime,todayTime,requestTime from ADA_ACCESS_LOG where domainId="+domainId+" and url="+url+" ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL); 
					
			while (set.next()) {
				
				AccessLog accessLog = new AccessLog();
				accessLog.setSiteId(set.getInt("siteId"));
				accessLog.setDomainId(set.getInt("domainId"));
				accessLog.setChannelId(set.getInt("channelId"));
				accessLog.setAdId(set.getInt("adId"));
				accessLog.setEntranceType(set.getInt("entranceType"));
				accessLog.setIpAddress(set.getString("ipAddress"));
				accessLog.setRegion(set.getString("region"));
				accessLog.setUuid(set.getString("uuid"));
				accessLog.setUrl(set.getString("url"));
				accessLog.setUseragent(set.getString("useragent"));
				accessLog.setOs(set.getString("os"));
				accessLog.setBrowser(set.getString("browser"));
				accessLog.setScreenSize(set.getString("screenSize"));
				accessLog.setPageSize(set.getString("pageSize"));
				accessLog.setReferer(set.getString("referer"));
				accessLog.setIframe(set.getInt("iframe"));
				accessLog.setFirstTime(set.getTimestamp("firstTime").getTime());
				accessLog.setTodayTime(set.getTimestamp("todayTime").getTime());
				accessLog.setRequestTime(set.getTimestamp("requestTime").getTime());
				AccessLogList.add(accessLog);
		 
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
		return AccessLogList;
	}

	
	public Integer countBySiteIdAndIp(Integer siteId, String ipAddress) {
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
 			String phoenixSQL="select count(siteId) as sum_ct from ADA_ACCESS_LOG where siteId="+siteId+" and ipAddress="+ipAddress+" ";
			//logger.info(phoenixSQL);
			
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
	
	public Integer countByDomainIdAndIp(Integer domainId, String ipAddress) {
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
 			String phoenixSQL="select count(siteId) as sum_ct from ADA_ACCESS_LOG where domainId="+domainId+" and ipAddress="+ipAddress+" ";
			//logger.info(phoenixSQL);
			
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

	public Integer countBySiteIdAndUrlLike(Integer siteId, String url) {
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
 			String phoenixSQL="select count(siteId) as sum_ct from ADA_ACCESS_LOG where siteId="+siteId+" and url like %"+url+" ";
			//logger.info(phoenixSQL);
			
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

	public Integer countByDomainIdAndUrlLike(Integer domainId, String url) {
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
 			String phoenixSQL="select count(siteId) as sum_ct from ADA_ACCESS_LOG where domainId="+domainId+" and url like %"+url+" ";
			//logger.info(phoenixSQL);
			
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
	
	public Integer statSiteIP(Integer siteId, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
 
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
			//select count(*) from ( SELECT DISTINCT ipAddress from ada_access_log where siteId = ? and createTime > ? and createTime < ?) a 

 			String phoenixSQL="select count(*) as sum_ct from ( SELECT DISTINCT ipAddress from ADA_ACCESS_LOG where siteId="+siteId+"   and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ) as TA";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);
					
			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statSitePV(Integer siteId,  Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
 
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
			//select count(*) from ( SELECT DISTINCT ipAddress from ada_access_log where siteId = ? and createTime > ? and createTime < ?) a 

 			String phoenixSQL="select count(*) as sum_ct from ( SELECT DISTINCT uuid from ADA_ACCESS_LOG where siteId="+siteId+"   and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ) as TA";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);
					
			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statSiteUV(Integer siteId,  Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
		List<AccessLog>  AccessLogList=new ArrayList<AccessLog> ();
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
 			String phoenixSQL="select count(siteId) as sum_ct from ADA_ACCESS_LOG where siteId="+siteId+"   and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statDomainIP(Integer domainId,  Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
 
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
			//select count(*) from ( SELECT DISTINCT ipAddress from ada_access_log where siteId = ? and createTime > ? and createTime < ?) a 

 			String phoenixSQL="select count(*) as sum_ct from ( SELECT DISTINCT ipAddress from ADA_ACCESS_LOG where domainId="+domainId+"  and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ) as TA";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statDomainPV(Integer domainId, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
 
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
			//select count(*) from ( SELECT DISTINCT ipAddress from ada_access_log where siteId = ? and createTime > ? and createTime < ?) a 

 			String phoenixSQL="select count(*) as sum_ct from ( SELECT DISTINCT uuid from ADA_ACCESS_LOG where domainId="+domainId+"   and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ) as TA";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);
					
			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statDomainUV(Integer domainId, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
		List<AccessLog>  AccessLogList=new ArrayList<AccessLog> ();
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
 			String phoenixSQL="select count(siteId) as sum_ct from ADA_ACCESS_LOG where domainId="+domainId+"   and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statSiteRegionIP(Integer siteId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
 
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
			//select count(*) from ( SELECT DISTINCT ipAddress from ada_access_log where siteId = ? and createTime > ? and createTime < ?) a 

 			String phoenixSQL="select count(*) as sum_ct from ( SELECT DISTINCT ipAddress from ADA_ACCESS_LOG where siteId="+siteId+" and  region="+region+"  and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ) as TA";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);
					
			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statSiteRegionPV(Integer siteId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
 
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
			//select count(*) from ( SELECT DISTINCT ipAddress from ada_access_log where siteId = ? and createTime > ? and createTime < ?) a 

 			String phoenixSQL="select count(*) as sum_ct from ( SELECT DISTINCT uuid from ADA_ACCESS_LOG where siteId="+siteId+" and  region="+region+"  and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ) as TA";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);


			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statSiteRegionUV(Integer siteId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
		List<AccessLog>  AccessLogList=new ArrayList<AccessLog> ();
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
 			String phoenixSQL="select count(siteId) as sum_ct from ADA_ACCESS_LOG where siteId="+siteId+" and  region="+region+"  and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);


					
			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statDomainRegionIP(Integer domainId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
 
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
			//select count(*) from ( SELECT DISTINCT ipAddress from ada_access_log where siteId = ? and createTime > ? and createTime < ?) a 

 			String phoenixSQL="select count(*) as sum_ct from ( SELECT DISTINCT ipAddress from ADA_ACCESS_LOG where domainId="+domainId+" and  region="+region+"  and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ) as TA";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);


					
			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statDomainRegionPV(Integer domainId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
 
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
			//select count(*) from ( SELECT DISTINCT ipAddress from ada_access_log where siteId = ? and createTime > ? and createTime < ?) a 

 			String phoenixSQL="select count(*) as sum_ct from ( SELECT DISTINCT uuid from ADA_ACCESS_LOG where domainId="+domainId+" and  region="+region+"  and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ) as TA";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);


					
			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}

	public Integer statDomainRegionUV(Integer domainId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		Integer retInteger=null;
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
		List<AccessLog>  AccessLogList=new ArrayList<AccessLog> ();
		try {
			// 耗时监控：记录一个开始时间
			long bTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
 			String phoenixSQL="select count(siteId) as sum_ct from ADA_ACCESS_LOG where domainId="+domainId+" and  region="+region+"  and createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"') ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

					
			while (set.next()) {
				retInteger=set.getInt("sum_ct");
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
		return retInteger;
	}
	
	public List<AccessLog> queryAccesslogByTime(Timestamp startTime, Timestamp endTime, Integer pageSize, Integer pageNo){
		Connection conn = null;
		Statement stmt = null;
		PhoenixResultSet set = null;
		List<AccessLog>  AccessLogList=new ArrayList<AccessLog> ();
		try {
			// 耗时监控：记录一个开始时间
			long beginT = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			stmt = conn.createStatement();
 			String phoenixSQL="select siteId,domainId,channelId,adId,entranceType,ipAddress,region,uuid,url,useragent,os,browser,screenSize,pageSize,referer,iframe,firstTime,todayTime,requestTime from ADA_ACCESS_LOG where  createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"')  LIMIT "+pageSize+" OFFSET "+pageNo+" ";
			//logger.info(phoenixSQL);
			
			set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

			while (set.next()) {
				
				AccessLog accessLog = new AccessLog();
				accessLog.setSiteId(set.getInt("siteId"));
				accessLog.setDomainId(set.getInt("domainId"));
				accessLog.setChannelId(set.getInt("channelId"));
				accessLog.setAdId(set.getInt("adId"));
				accessLog.setEntranceType(set.getInt("entranceType"));
				accessLog.setIpAddress(set.getString("ipAddress"));
				accessLog.setRegion(set.getString("region"));
				accessLog.setUuid(set.getString("uuid"));
				accessLog.setUrl(set.getString("url"));
				accessLog.setUseragent(set.getString("useragent"));
				accessLog.setOs(set.getString("os"));
				accessLog.setBrowser(set.getString("browser"));
				accessLog.setScreenSize(set.getString("screenSize"));
				accessLog.setPageSize(set.getString("pageSize"));
				accessLog.setReferer(set.getString("referer"));
				accessLog.setIframe(set.getInt("iframe"));
				accessLog.setFirstTime(set.getTimestamp("firstTime").getTime());
				accessLog.setTodayTime(set.getTimestamp("todayTime").getTime());
				accessLog.setRequestTime(set.getTimestamp("requestTime").getTime());
				AccessLogList.add(accessLog);
		 
			}

			long endT= System.currentTimeMillis();
            
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
		return AccessLogList;
	}

	public void deleteAccessLogByTime(Timestamp startTime, Timestamp endTime) {
		String phoenixSQL="delete from ADA_ACCESS_LOG where createTime BETWEEN  TO_TIMESTAMP('"+startTime+"') AND  TO_TIMESTAMP('"+endTime+"')";
		execUpdateSql(phoenixSQL);
	}
	
	public int batchAddAccessLog(List<AccessLog> accessLoglist) {
		Connection conn =null;
        Statement stmt =null;
		
		int counts=0;
		try {
			// 耗时监控：记录一个开始时间
			long startTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			if (conn == null) {
				logger.info("batchAddAccessLog 连接执行出错：conn == null。" ); 
				return -1;
			}
			// 准备查询
			stmt = conn.createStatement();
			for(AccessLog accessLog:accessLoglist) {
				String phoenixSQL="";
				phoenixSQL=AccessLogToSql.insertStr(accessLog);
				//logger.info(phoenixSQL);
//			    

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
}
