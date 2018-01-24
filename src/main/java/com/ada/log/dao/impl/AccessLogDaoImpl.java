package com.ada.log.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ada.log.bean.AccessLog;
import com.ada.log.bean.EventLog;
import com.ada.log.dao.AccessLogDao;
import com.ada.log.util.Dates;

@Service
public class AccessLogDaoImpl implements AccessLogDao,InitializingBean {
	
	private final static Log log1 = LogFactory.getLog(AccessLogDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private String insertAcccessLogSql;
	private String insertEventLogSql;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		resetSql();
	}
	
	@Scheduled(cron="0 0 0 * * ?")
	protected void resetSql() {
		SimpleDateFormat df =new SimpleDateFormat("yyyyMMdd");
		String date = df.format(Dates.now());
		insertAcccessLogSql = "INSERT INTO `ada_access_log_"+date+"`(siteId,domainId,channelId,adId,ipAddress,region,uuid,url,useragent,os,browser,screenSize,pageSize,referer,iframe,firstTime,todayTime,requestTime,createTime) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
		insertEventLogSql = "INSERT INTO `ada_event_log_"+date+"`(siteId,domainId,channelId,adId,ipAddress,region,uuid,url,event,args,requestTime,createTime) values (?,?,?,?,?,?,?,?,?,?,?,now())";
	}
	
	public void createAccessTable(String data){
		String sql = "CREATE TABLE `ada_access_log_"+data+"` (`id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',`siteId` int(11) DEFAULT NULL COMMENT '站点ID',`domainId` int(11) DEFAULT NULL COMMENT '域名ID',`channelId` int(11) DEFAULT NULL COMMENT '渠道ID',`adId` int(11) DEFAULT NULL COMMENT '广告ID',`ipAddress` varchar(24) DEFAULT NULL COMMENT 'IP地址',`region` varchar(64) DEFAULT NULL COMMENT '地区',`uuid` varchar(32) DEFAULT NULL COMMENT '客户端ID',`url` varchar(256) DEFAULT NULL COMMENT '浏览页',`referer` varchar(256) DEFAULT NULL COMMENT '引用页',`useragent` varchar(256) DEFAULT NULL COMMENT '客户端头信息',`os` varchar(24) DEFAULT NULL COMMENT '操作系统',`browser` varchar(24) DEFAULT NULL COMMENT '浏览器',`screenSize` varchar(16) DEFAULT NULL COMMENT '屏幕大小',`pageSize` varchar(16) DEFAULT NULL COMMENT '页面大小',`iframe` int(11) DEFAULT NULL COMMENT '在Iframe中',`firstTime` datetime DEFAULT NULL COMMENT '首次访问时间',`todayTime` datetime DEFAULT NULL COMMENT '当天首次访问时间',`requestTime` datetime DEFAULT NULL COMMENT '客户端请求时间',`createTime` datetime DEFAULT NULL COMMENT '创建时间',PRIMARY KEY (`id`),KEY `index_ip` (`ipAddress`)) ENGINE=InnoDB AUTO_INCREMENT=19431900 DEFAULT CHARSET=utf8 COMMENT='访问日志'";
	}

	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void batchInsert(final List<AccessLog> logs) {
		Long startTime = System.currentTimeMillis();
		
		jdbcTemplate.batchUpdate(insertAcccessLogSql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				AccessLog log = logs.get(i);
				if(log==null){
					log1.error("日志对象为空,logs->"+logs.size()+",i->"+i);;
				}
				Integer parameterIndex = 1;
				setInteger(ps, parameterIndex++,log.getSiteId());
				setInteger(ps, parameterIndex++,log.getDomainId());
				setInteger(ps, parameterIndex++,log.getChannelId());
				setInteger(ps, parameterIndex++,log.getAdId());
				setString(ps, parameterIndex++,log.getIpAddress());
				setString(ps, parameterIndex++,log.getRegion());
				setString(ps, parameterIndex++,log.getUuid());
				if(log.getUrl()!=null &&log.getUrl().length()>128){
					log.setUrl(log.getUrl().substring(0, 128));
				}
				setString(ps, parameterIndex++, log.getUrl());
				if(log.getUseragent()!=null &&log.getUseragent().length()>128){
					log.setUseragent(log.getUseragent().substring(0, 128));
				}
				setString(ps, parameterIndex++, log.getUseragent());
				setString(ps, parameterIndex++, log.getOs());
				setString(ps, parameterIndex++, log.getBrowser());
				setString(ps, parameterIndex++, log.getScreenSize());
				setString(ps, parameterIndex++, log.getPageSize());
				if(log.getReferer()!=null &&log.getReferer().length()>128){
					log.setReferer(log.getReferer().substring(0, 128));
				}
				setString(ps, parameterIndex++, log.getReferer());
				setInteger(ps, parameterIndex++, log.getIframe());
				setTimestamp(ps, parameterIndex++, log.getFirstTime());
				setTimestamp(ps, parameterIndex++, log.getTodayTime());
				setTimestamp(ps, parameterIndex++, log.getRequestTime());
			}
			
			protected void setInteger(PreparedStatement ps,Integer parameterIndex,Integer value) throws SQLException {
				if(value==null){
					ps.setNull(parameterIndex, Types.INTEGER);
				}else{
					ps.setInt(parameterIndex, value);
				}
			}
			
			protected void setString(PreparedStatement ps,Integer parameterIndex,String value)  throws SQLException {
				if(value==null){
					ps.setNull(parameterIndex, Types.VARCHAR);
				}else{
					ps.setString(parameterIndex, value);
				}
			}
			
			protected void setTimestamp(PreparedStatement ps,Integer parameterIndex,Long value)  throws SQLException {
				if(value==null){
					ps.setNull(parameterIndex, Types.TIMESTAMP);
				}else{
					ps.setTimestamp(parameterIndex, new Timestamp(value));
				}
			}
			
			@Override
			public int getBatchSize() {
				return logs.size();
			}
		});
		
		if(log1.isDebugEnabled()){
			Long endTime = System.currentTimeMillis();
			Long cost = endTime -startTime;
			log1.info("批量插入支持 ->"+logs.size()+",用时"+cost+"ms");
		}
	}
	
	
	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void batchInsertEventLog(final List<EventLog> logs) {
		Long startTime = System.currentTimeMillis();
		
		jdbcTemplate.batchUpdate(insertEventLogSql, new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				EventLog log = logs.get(i);
				Integer parameterIndex = 1;
				setInteger(ps, parameterIndex++,log.getSiteId());
				setInteger(ps, parameterIndex++,log.getDomainId());
				setInteger(ps, parameterIndex++,log.getChannelId());
				setInteger(ps, parameterIndex++,log.getAdId());
				setString(ps, parameterIndex++,log.getIpAddress());
				setString(ps, parameterIndex++,log.getRegion());
				setString(ps, parameterIndex++,log.getUuid());
				if(log.getUrl()!=null &&log.getUrl().length()>128){
					log.setUrl(log.getUrl().substring(0, 128));
				}
				setString(ps, parameterIndex++, log.getUrl());
				setString(ps, parameterIndex++, log.getEvent());
				setString(ps, parameterIndex++, log.getArgs());
				setTimestamp(ps, parameterIndex++, log.getRequestTime());
			}
			
			protected void setInteger(PreparedStatement ps,Integer parameterIndex,Integer value) throws SQLException {
				if(value==null){
					ps.setNull(parameterIndex, Types.INTEGER);
				}else{
					ps.setInt(parameterIndex, value);
				}
			}
			
			protected void setString(PreparedStatement ps,Integer parameterIndex,String value)  throws SQLException {
				if(value==null){
					ps.setNull(parameterIndex, Types.VARCHAR);
				}else{
					ps.setString(parameterIndex, value);
				}
			}
			
			protected void setTimestamp(PreparedStatement ps,Integer parameterIndex,Long value)  throws SQLException {
				if(value==null){
					ps.setNull(parameterIndex, Types.TIMESTAMP);
				}else{
					ps.setTimestamp(parameterIndex, new Timestamp(value));
				}
			}
			
			@Override
			public int getBatchSize() {
				return logs.size();
			}
		});
		
		if(log1.isDebugEnabled()){
			Long endTime = System.currentTimeMillis();
			Long cost = endTime -startTime;
			log1.info("批量插入支持 ->"+logs.size()+",用时"+cost+"ms");
		}
	}

}
