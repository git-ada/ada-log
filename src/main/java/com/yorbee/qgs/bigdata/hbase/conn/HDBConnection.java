package com.yorbee.qgs.bigdata.hbase.conn;

import java.sql.Connection;

public interface HDBConnection {
	public Connection getConnection(String host, String port);
	public Connection getNConnection(String host, String port);
}
