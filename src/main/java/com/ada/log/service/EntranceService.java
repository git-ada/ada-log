package com.ada.log.service;

import com.ada.log.bean.Entrance;

public interface EntranceService {

	/**
	 * 获取今日入口信息,
	 * @param ip			访问IP
	 * @param browsingPage  当前浏览页面，如果没有历史入口信息，则当前页面即为入口
	 * @return
	 */
	public Entrance getAndSetEntrance(String ip,Integer domainId,Integer adId,String page);
}
