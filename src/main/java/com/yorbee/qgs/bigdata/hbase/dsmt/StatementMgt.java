package com.yorbee.qgs.bigdata.hbase.dsmt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.List;

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

 


public abstract class StatementMgt {
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
            logger.info( "SQL执行出错：" + e.getMessage());
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
        	set=null;
        	stmt=null;
        	conn=null;
        }
        return result;
    }

	public int execUpdateSql(String phoenixSQL) {
		Connection conn =null;
        Statement stmt =null;
        int ret = -1;
		try {
			// 耗时监控：记录一个开始时间
			long startTime = System.currentTimeMillis();
			// 获取一个Phoenix DB连接
			conn = PhoenixClientConnection.getConnection(host, port);
			// 准备查询
			 stmt = conn.createStatement();
			 ret = stmt.executeUpdate(phoenixSQL);
			conn.commit();
            System.out.println(""+ret);
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
		return ret;
	}
	
	


}
