package com.ada.log.service.impl;

import com.ada.log.service.SiteService;

public class SiteServiceImpl implements SiteService{

	/**
	 * 匹配是否目标页
	 * @param siteId
	 * @param browsingPage 
	 * @return
	 */
	public boolean matchTargetPage(Integer siteId,String browsingPage){
		String[] split = browsingPage.split("\\?");
		
		if(split[0].contains("目标页面")){
			return true;
		}
		
		
		return false;
	}

}
