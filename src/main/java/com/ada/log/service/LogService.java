package com.ada.log.service;


/**
 * 核心日志服务
 * @author wanghl
 */
public interface LogService {

	/**
	 * 记录日志
	 * @param ipAddress     IP地址
	 * @param uuid          客户端UUID
	 * @param siteId        站点ID
	 * @param channelId     渠道ID
	 * @param clickNum      点击次数
	 * @param browsingTime  浏览时间,精确到毫秒
	 * @param browsingPage  当前页面链接
	 */
	public void log(
			  String ipAddress,
			  String uuid,
	          Integer siteId,
	          Integer channelId,
	          Integer domainId,
	          Integer clickNum,
	          Integer browsingTime,
	          String browsingPage);
	
	public void log1(String ipAddress,
				  String uuid,
		          Integer siteId,
		          Integer channelId,Integer domainId, String browsingPage,Integer oldUser);
	
	public void log2(
			  String ipAddress,
			  String uuid,
	          Integer siteId,
	          Integer channelId,
	          Integer domainId,
	          Integer clickNum);
	

	


}
