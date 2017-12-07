package com.ada.log.service;

import java.util.Set;

import com.ada.log.bean.AccessLog;


/**
 * 核心日志服务
 * @author wanghl
 */
public interface LogService {

	public void log(AccessLog log);
	
	/**
	 * 得到昨天IPSet集合
	 * @param  domainId
	 * @return IPSet
	 */
	public Set<String> getYesterdayDomainIPSet(Integer domainId);
	


}
