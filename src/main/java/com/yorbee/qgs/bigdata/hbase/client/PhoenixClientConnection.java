package com.yorbee.qgs.bigdata.hbase.client;

import java.sql.Connection;

import com.yorbee.qgs.bigdata.hbase.conn.HDBConnection;
import com.yorbee.qgs.bigdata.hbase.conn.HDBConnectionMgt;


public class PhoenixClientConnection{
   public static Connection getConnection(String host, String port){
	   HDBConnection HDBConnection= new HDBConnectionMgt();
	   return HDBConnection.getConnection(host, port);
   }
}
