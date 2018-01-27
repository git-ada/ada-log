package com.yorbee.qgs.bigdata.hbase.dsmt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.apache.phoenix.jdbc.PhoenixResultSet;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


import com.yorbee.qgs.bigdata.hbase.client.PhoenixClientConnection;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLogToSql;
import com.yorbee.qgs.bigdata.hbase.entity.EventLog;
import com.yorbee.qgs.bigdata.hbase.entity.EventLogToSql;

import jline.internal.Log;

 


public class StatementMgt {
	private final static Logger logger = Logger.getLogger(StatementMgt.class.getName());
	public String host;
	public String port;

	public void init(String _host, String _port) {
		if (_host == null || port == null || _host.trim() == "" || port.trim() == "") {
			// return "必须指定hbase master的IP和端口";

		}
		host = _host;
		port = _port;
	}


	public  String execQuerySql(String phoenixSQL) {
        String result = "";
        Connection conn =null;
        Statement stmt =null;
        PhoenixResultSet set =null;
        try {
            // 耗时监控：记录一个开始时间
            long startTime = System.currentTimeMillis();
         
            // 获取一个Phoenix DB连接
             conn = PhoenixClientConnection.getConnection(host, port);
            if (conn == null) {
                return "Phoenix DB连接超时！";
            }

            // 准备查询
              stmt = conn.createStatement();
              set = (PhoenixResultSet) stmt.executeQuery(phoenixSQL);

            // 查询出来的列是不固定的，所以这里通过遍历的方式获取列名
            ResultSetMetaData meta = set.getMetaData();
            ArrayList<String> cols = new ArrayList<String>();

            // 把最终数据都转成JSON返回
            JSONArray jsonArr = new JSONArray();
            while (set.next()) {
                if (cols.size() == 0) {
                    for (int i = 1, count = meta.getColumnCount(); i <= count; i++) {
                        cols.add(meta.getColumnName(i));
                    }
                }

                JSONObject json = new JSONObject();
                for (int i = 0, len = cols.size(); i < len; i++) {
                    json.put(cols.get(i), set.getString(cols.get(i)));
                }
                jsonArr.put(json);
            }
            // 耗时监控：记录一个结束时间
            long endTime = System.currentTimeMillis();

            // 结果封装
            JSONObject data = new JSONObject();
            data.put("data", jsonArr);
            data.put("cost", (endTime - startTime) + " ms");
            result = data.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQL执行出错：" + e.getMessage();
        } catch (JSONException e) {
            e.printStackTrace();
            return "JSON转换出错：" + e.getMessage();
        }finally{
        	set=null;
        	stmt=null;
        	conn=null;
        }
        return result;
    }

	public String execUpdateSql(String phoenixSQL) {
		Connection conn =null;
        Statement stmt =null;
		

		String result = "";
		try {
			// 耗时监控：记录一个开始时间
			long startTime = System.currentTimeMillis();
			if (phoenixSQL == null || phoenixSQL.trim() == "") {
				return "请指定合法的Phoenix SQL！";
			}
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			if (conn == null) {
				return "Phoenix DB连接超时！";
			}

			// 准备查询
			stmt = conn.createStatement();
			int ret = stmt.executeUpdate(phoenixSQL);
			conn.commit();
            System.out.println(""+ret);
			// 耗时监控：记录一个结束时间
			long endTime = System.currentTimeMillis();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return "SQL执行出错：" + e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			return "SQL执行出错：" + e.getMessage();
		}finally{
        	 
        	stmt=null;
        	conn=null;
        }
		return result;
	}
	
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

			// 查询出来的列是不固定的，所以这里通过遍历的方式获取列名
			ResultSetMetaData meta = set.getMetaData();
			ArrayList<String> cols = new ArrayList<String>();
 
					
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

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				set.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
			try {
				stmt.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
			
			try {
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
			
			
			set = null;
			stmt = null;
			conn = null;
			
			
		}
		return AccessLogList;
	}
	
	private AtomicInteger total = new AtomicInteger();
	
	public int batchAddAccessLog(List<AccessLog> accessLoglist) {
		Log.info("batct add " + accessLoglist.size() +",total->"+total.addAndGet(accessLoglist.size()));
		Connection conn =null;
        Statement stmt =null;
		
		int counts=0;
		try {
			// 耗时监控：记录一个开始时间
			long startTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			if (conn == null) {
				return -1;
			}
			// 准备查询
			stmt = conn.createStatement();
			for(AccessLog accessLog:accessLoglist) {
				String phoenixSQL="";
				phoenixSQL=AccessLogToSql.insertStr(accessLog);
				//logger.info(phoenixSQL);
				int ret = stmt.executeUpdate(phoenixSQL);
				counts=counts+ret;
			}
			
			conn.commit();
            
			// 耗时监控：记录一个结束时间
			long endTime = System.currentTimeMillis();
			
		} catch (SQLException e) {
			e.printStackTrace();
			 
		} catch (Exception e) {
			e.printStackTrace();
			 
		}finally{
        	 
        	stmt=null;
        	conn=null;
        }
		return counts;
	}
	
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
				return -1;
			}

			// 准备查询
			stmt = conn.createStatement();
			for(EventLog accessLog:accessLoglist) {
				String phoenixSQL="";
				phoenixSQL=EventLogToSql.insertStr(accessLog);
				logger.info(phoenixSQL);
				int ret = stmt.executeUpdate(phoenixSQL);
				counts=counts+ret;
				System.out.println(""+ret);
			}
			
			conn.commit();
			
            
			// 耗时监控：记录一个结束时间
			long endTime = System.currentTimeMillis();
			
		} catch (SQLException e) {
			e.printStackTrace();
			 
		} catch (Exception e) {
			e.printStackTrace();
			 
		}finally{
        	 
        	stmt=null;
        	conn=null;
        }
		return counts;
	}

}
