package com.ada.log.service;

public interface SiteService {

	/**
	 * 匹配是否目标页
	 * @param siteId
	 * @param browsingPage 
	 * @return
	 */
	public boolean matchTargetPage(Integer siteId,String browsingPage);
}
