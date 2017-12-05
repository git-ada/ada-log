package com.ada.log.service;

import com.ada.log.bean.ADPage;

/**
 * 渠道相关服务处理
 * @author wanghl
 *
 */
public interface ChannelService {

	/**
	 * 
	 * @param siteId        站点ID
	 * @param browsingPage  浏览页面链接
	 * @return              渠道ID, 如果没有查询到则返回null
	 */
	public Integer queryChannel(Integer siteId,String browsingPage);
	
	public ADPage matchAdPage(Integer siteId,String browsingPage);
	
}
