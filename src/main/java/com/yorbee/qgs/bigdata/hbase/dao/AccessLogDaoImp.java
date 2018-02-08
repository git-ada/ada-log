package com.yorbee.qgs.bigdata.hbase.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ada.log.dao.AccessLogDao;
import com.yorbee.qgs.bigdata.hbase.dsmt.AccessLogStateMentMgt;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;

//@Service
public class AccessLogDaoImp implements AccessLogDao{
	@Value("${phoenix.host:}")
	String _host;
	@Value("${phoenix.port:}")
	String _port;
	public void batchInsert(List<AccessLog> accessLoglist) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		smgt.batchAddAccessLog(accessLoglist);
	}

	public List<AccessLog> findBySiteId(Integer siteId, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		List<AccessLog> accesslogList=new ArrayList<AccessLog>();
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
 
		smgt.init(_host, _port);
		accesslogList=smgt.queryAccesslog(siteId, pageSize, pageNo);
		return accesslogList;
	}

	public List<AccessLog> findByDomainId(Integer domainId, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		List<AccessLog> accesslogList=new ArrayList<AccessLog>();
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		accesslogList=smgt.queryAccesslogByDomainId(domainId, pageSize, pageNo);
		return accesslogList;
	}

	public List<AccessLog> findBySiteIdAndIp(Integer siteId, String ipAddress, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		List<AccessLog> accesslogList=new ArrayList<AccessLog>();
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		accesslogList=smgt.queryAccesslogBySiteIdAndIp(siteId,ipAddress, pageSize, pageNo);
		return accesslogList;
	}

	public List<AccessLog> findByDomainIdAndIp(Integer domainId, String ipAddress, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		List<AccessLog> accesslogList=new ArrayList<AccessLog>();
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		accesslogList=smgt.queryAccesslogByDomainIdAndIp(domainId,ipAddress, pageSize, pageNo);
		return accesslogList;
	}

	public List<AccessLog> findBySiteIdAndUrlLike(Integer siteId, String url) {
		// TODO Auto-generated method stub
		List<AccessLog> accesslogList=new ArrayList<AccessLog>();
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		accesslogList=smgt.queryAccesslogBySiteIdAndUrlLike(siteId,url);
		return accesslogList;
	}

	public List<AccessLog> findByDomainIdAndUrlLike(Integer domainId, String url) {
		// TODO Auto-generated method stub
		List<AccessLog> accesslogList=new ArrayList<AccessLog>();
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		accesslogList=smgt.queryAccesslogByDomainIdAndUrlLike(domainId,url);
		return accesslogList;
	}

	public Integer countBySiteIdAndIp(Integer siteId, String ipAddress) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.countBySiteIdAndIp(siteId,ipAddress);
	}

	public Integer countByDomainIdAndIp(Integer domainId, String ipAddress) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.countByDomainIdAndIp(domainId,ipAddress);
	}

	public Integer countBySiteIdAndUrlLike(Integer siteId, String url) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.countBySiteIdAndUrlLike(siteId,url);
	}

	public Integer countByDomainIdAndUrlLike(Integer domainId, String url) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.countByDomainIdAndUrlLike(domainId,url);
	}

	public Integer statSiteIP(Integer siteId,  Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.statSiteIP(siteId, startTime, endTime);
	}

	public Integer statSitePV(Integer siteId,  Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.statSitePV(siteId, startTime, endTime);
	}

	public Integer statSiteUV(Integer siteId,  Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.statSiteUV(siteId, startTime, endTime);
	}

	public Integer statDomainIP(Integer domainId,  Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return null;
	}

	public Integer statDomainPV(Integer domainId,  Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return null;
	}

	public Integer statDomainUV(Integer domainId,  Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return null;
	}

	public Integer statSiteRegionIP(Integer siteId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.statSiteRegionIP(siteId, region, startTime, endTime);
	}

	public Integer statSiteRegionPV(Integer siteId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.statSiteRegionPV(siteId, region, startTime, endTime);
	}

	public Integer statSiteRegionUV(Integer siteId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.statSiteRegionUV(siteId, region, startTime, endTime);
	}

	public Integer statDomainRegionIP(Integer domainId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.statDomainRegionIP(domainId, region, startTime, endTime);
	}

	public Integer statDomainRegionPV(Integer domainId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.statDomainRegionPV(domainId, region, startTime, endTime);
	}

	public Integer statDomainRegionUV(Integer domainId, String region, Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		return smgt.statDomainRegionUV(domainId, region, startTime, endTime);
	}

	public List<AccessLog> findByTime(Timestamp startTime, Timestamp endTime, Integer pageSize, Integer pageNo) {
		// TODO Auto-generated method stub
		List<AccessLog> accesslogList=new ArrayList<AccessLog>();
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		accesslogList=smgt.queryAccesslogByTime(startTime,endTime, pageSize, pageNo);
		return accesslogList;
	}

	public void deleteByTime(Timestamp startTime, Timestamp endTime) {
		// TODO Auto-generated method stub
		AccessLogStateMentMgt smgt=new AccessLogStateMentMgt();
		smgt.init(_host, _port);
		smgt.deleteAccessLogByTime(startTime, endTime);
	}

}
