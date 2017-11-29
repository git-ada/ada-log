package com.ada.log.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ada.log.bean.AccessLog;
import com.ada.log.dao.AccessLogDao;

@Service
public class AccessLogDaoImpl implements AccessLogDao {
	
	private final static Log log = LogFactory.getLog(AccessLogDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void batchInsert(final List<AccessLog> logs) {
		Long startTime = System.currentTimeMillis();
		
		jdbcTemplate.batchUpdate("INSERT INTO `ada_access_log`(siteId,domainId,channelId,ipAddress,uuid,url,useragent,os,browser,screenSize,pageSize,referer,iframe,firstTime,todayTime,requestTime,createTime) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())", new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				AccessLog log = logs.get(i);
				Integer parameterIndex = 1;
				setInteger(ps, parameterIndex++,log.getSiteId());
				setInteger(ps, parameterIndex++,log.getDomainId());
				setInteger(ps, parameterIndex++,log.getChannelId());
				setString(ps, parameterIndex++,log.getIpAddress());
				setString(ps, parameterIndex++,log.getUuid());
				setString(ps, parameterIndex++, log.getUrl());
				setString(ps, parameterIndex++, log.getUseragent());
				setString(ps, parameterIndex++, log.getOs());
				setString(ps, parameterIndex++, log.getBrowser());
				setString(ps, parameterIndex++, log.getScreenSize());
				setString(ps, parameterIndex++, log.getPageSize());
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
		
		if(log.isDebugEnabled()){
			Long endTime = System.currentTimeMillis();
			Long cost = endTime -startTime;
			log.info("批量插入支持 ->"+logs.size()+",用时"+cost+"ms");
		}
	}
}
