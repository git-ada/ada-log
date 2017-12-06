package com.ada.log.service;

/**
 * 历史访问IP地址库，
 */
public interface IPSetService {

	/**
	 * 是否存在
	 * @param ipAddress
	 * @return
	 */
	public boolean exists(Integer domainId,String ipAddress);
	
	/**
	 * 添加一个
	 * @param domainId
	 * @param ipAddress
	 * @return
	 */
	public boolean add(Integer domainId);
}
