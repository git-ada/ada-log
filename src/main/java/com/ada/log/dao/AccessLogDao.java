package com.ada.log.dao;

import java.sql.Timestamp;
import java.util.List;

import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;

public interface AccessLogDao {

	/**
	 * 批量插入，每次插入不超过5000条
	 * @param logs
	 */
	void batchInsert(List<AccessLog> logs);
	
	List<AccessLog> findBySiteId(Integer siteId,Integer pageSize,Integer pageNo);
	
	List<AccessLog> findByDomainId(Integer domainId,Integer pageSize,Integer pageNo);
	List<AccessLog> findBySiteIdAndIp(Integer siteId,String ipAddress,Integer pageSize,Integer pageNo);
	List<AccessLog> findByDomainIdAndIp(Integer domainId,String ipAddress,Integer pageSize,Integer pageNo);
	List<AccessLog> findBySiteIdAndUrlLike(Integer siteId,String url);
	List<AccessLog> findByDomainIdAndUrlLike(Integer domainId,String url);
	
	Integer countBySiteIdAndIp(Integer siteId,String ipAddress);
	Integer countByDomainIdAndIp(Integer domainId,String ipAddress);
	Integer countBySiteIdAndUrlLike(Integer siteId,String ipAddress);
	Integer countByDomainIdAndUrlLike(Integer domainId,String ipAddress);
	
	Integer statSiteIP(Integer siteId,Timestamp startTime, Timestamp endTime);
	Integer statSitePV(Integer siteId,Timestamp startTime, Timestamp endTime);
	Integer statSiteUV(Integer siteId,Timestamp startTime, Timestamp endTime);
	
	Integer statDomainIP(Integer domainId,Timestamp startTime, Timestamp endTime);
	Integer statDomainPV(Integer domainId,Timestamp startTime, Timestamp endTime);
	Integer statDomainUV(Integer domainId,Timestamp startTime, Timestamp endTime);
	
	Integer statSiteRegionIP(Integer siteId,String region,Timestamp startTime, Timestamp endTime);
	Integer statSiteRegionPV(Integer siteId,String region,Timestamp startTime, Timestamp endTime);
	Integer statSiteRegionUV(Integer siteId,String region,Timestamp startTime, Timestamp endTime);

	Integer statDomainRegionIP(Integer domainId,String region,Timestamp startTime, Timestamp endTime);
	Integer statDomainRegionPV(Integer domainId,String region,Timestamp startTime, Timestamp endTime);
	Integer statDomainRegionUV(Integer domainId,String region,Timestamp startTime, Timestamp endTime);
	
	List<AccessLog> findByTime(Timestamp startTime, Timestamp endTime,Integer pageSize,Integer pageNo);
	
	void deleteByTime(Timestamp startTime, Timestamp endTime);
}
