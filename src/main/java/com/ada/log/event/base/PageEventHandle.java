package com.ada.log.event.base;

/**
 * 页面事件处理,记录页面操作事件种类
 */
public interface PageEventHandle {
	
	/**
	 * 
	 * @param ipAddress
	 * @param uuid
	 * @param siteId
	 * @param channelId
	 * @param domainId
	 * @param number
	 * @param browsingPage
	 */
	public void handle(
			String ipAddress,
			String uuid,
			Integer siteId, 
			Integer channelId,
			Integer domainId,
			Integer adId,
			Integer entranceType,
			String region,
			Integer number
			);
}
