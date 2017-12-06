package com.ada.log.service;

import java.util.Set;

import com.ada.log.bean.AccessLog;


/**
 * 核心日志服务
 * @author wanghl
 */
public interface LogService {

	
	public void log1(String ipAddress,
				  String uuid,
		          Integer siteId,
		          Integer channelId,Integer domainId, String browsingPage,Boolean isOldUser);
	
	public void log(AccessLog log);
	
	/**
	 * 得到IPSet集合
	 * @param  domainId
	 * @return IPSet
	 */
	public Set<String> loopDomainIPSet(Integer domainId);
	


}
