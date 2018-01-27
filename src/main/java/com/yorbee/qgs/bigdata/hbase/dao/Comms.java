package com.yorbee.qgs.bigdata.hbase.dao;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

 

public class Comms {
    public static String HB_HOST="";//"master,slave1,slave2";//"10.3.5.50,10.3.5.51,10.3.5.52";
    public static String HB_PORT="";//"2181";
    
	public static void init_load() {
		try {
			Properties prop = new Properties();
			String realPath=System.getProperty ("user.dir")+System.getProperty("file.separator")+"conf"+System.getProperty("file.separator")+"config.properties";
			
			InputStream in = new BufferedInputStream(new FileInputStream(realPath));
			System.out.println("-----------------begin.---------------------");
			
			System.out.println("user.dir path: " + System. getProperty ("user.dir"));
 
			 
			prop.load(in); /// 加载属性列表
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				System.out.println(key + ":" + prop.getProperty(key));
				if(key.equals("HB_HOST")) {
					Comms.HB_HOST=""+prop.getProperty(key);
				}
				else if(key.equals("HB_PORT")) {
					Comms.HB_PORT=""+prop.getProperty(key);			
				}
 
			}
			in.close();
			System.out.println(Comms.HB_HOST);
			System.out.println(Comms.HB_PORT);
 
			
			System.out.println("-----------------end.---------------------");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}