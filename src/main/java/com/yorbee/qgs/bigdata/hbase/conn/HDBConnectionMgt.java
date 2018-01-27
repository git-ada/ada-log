package com.yorbee.qgs.bigdata.hbase.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
/***
 * 
 * @author yorbee
 *
 */
public class HDBConnectionMgt implements HDBConnection{
 	
	 public Connection getNConnection(final String host, final String port) {
         Connection cc = null;
         if (cc == null) {
             try {
                 final ExecutorService exec = Executors.newFixedThreadPool(5);
                 Callable<Connection> call = new Callable<Connection>() {
                     public Connection call() throws Exception {
                         return ConnectionManager.getConnection(host,port);
                     }
                 };
                 Future<Connection> future = exec.submit(call);
                 // 如果在5s钟之内，还没得到 Connection 对象，则认为连接超时，不继续阻塞，防止服务夯死
                 cc = future.get(1000 * 60, TimeUnit.MILLISECONDS);
                 exec.shutdownNow();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             } catch (ExecutionException e) {
                 e.printStackTrace();
             } catch (TimeoutException e) {
                 e.printStackTrace();
             }
         }
         return cc;
     }
	
	 public Connection getConnection(String host, String port) {
         Connection cc = null;
         final String url = "jdbc:phoenix:" + host + ":" + port;

         if (cc == null) {
             try {
                 final ExecutorService exec = Executors.newFixedThreadPool(5);
                 Callable<Connection> call = new Callable<Connection>() {
                     public Connection call() throws Exception {
                    	 try {
                 			Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
                 		} catch (ClassNotFoundException e) {
                 			// TODO Auto-generated catch block
                 			e.printStackTrace();
                 		}
                         return DriverManager.getConnection(url);
                     }
                 };
                 Future<Connection> future = exec.submit(call);
                 // 如果在5s钟之内，还没得到 Connection 对象，则认为连接超时，不继续阻塞，防止服务夯死
                 cc = future.get(3000 * 60, TimeUnit.MILLISECONDS);
                 exec.shutdownNow();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             } catch (ExecutionException e) {
                 e.printStackTrace();
             } catch (TimeoutException e) {
                 e.printStackTrace();
             }
         }
         return cc;
     }
}
