package com.yorbee.qgs.bigdata.hbase.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ada.log.bean.EventLog;
import com.ada.log.dao.AccessLogDao;
import com.yorbee.qgs.bigdata.hbase.dsmt.StatementMgt;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;

//@Service
public class AccessLogDaoImp implements AccessLogDao{
	@Value("${phoenix.host:}")
	String _host;
	@Value("${phoenix.port:}")
	String _port;
	public void batchInsert(List<AccessLog> accessLoglist) {
		// TODO Auto-generated method stub
		StatementMgt smgt=new StatementMgt();
		smgt.init(_host, _port);
		smgt.batchAddAccessLog(accessLoglist);
	}

	public List<AccessLog> findBySiteId(Integer siteId, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		List<AccessLog> accesslogList=new ArrayList<AccessLog>();
		StatementMgt smgt=new StatementMgt();
		smgt.init(_host, _port);
		accesslogList=smgt.queryAccesslog(siteId, pageSize, pageNo);
		return accesslogList;
	}

	@Override
	public List<AccessLog> findByDomainId(Integer domainId, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccessLog> findBySiteIdAndIp(Integer siteId, String ipAddress, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccessLog> findByDomainIdAndIp(Integer domainId, String ipAddress, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccessLog> findBySiteIdAndUrlLike(Integer siteId, String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccessLog> findByDomainIdAndUrlLike(Integer domainId, String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countBySiteIdAndIp(Integer siteId, String ipAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countByDomainIdAndIp(Integer domainId, String ipAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countBySiteIdAndUrlLike(Integer siteId, String ipAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countByDomainIdAndUrlLike(Integer domainId, String ipAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statSiteIP(Integer siteId, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statSitePV(Integer siteId, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statSiteUV(Integer siteId, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statDomainIP(Integer domainId, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statDomainPV(Integer domainId, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statDomainUV(Integer domainId, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statSiteRegionIP(Integer siteId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statSiteRegionPV(Integer siteId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statSiteRegionUV(Integer siteId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statDomainRegionIP(Integer domainId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statDomainRegionPV(Integer domainId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer statDomainRegionUV(Integer domainId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccessLog> findByTime(Timestamp startTime, Timestamp endTime, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteByTime(Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		
	}

}
