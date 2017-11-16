package com.ada.log.service;

import java.util.Date;

import com.ada.log.bean.ChannelStat;
import com.ada.log.bean.SiteStat;

/**
 * 统计数据
 * @author ASUS
 *
 */
public interface StatService {
	/**
	 * 统计站点
	 * @param site
	 * @return
	 */
	public SiteStat statSite(Integer site,Date date);
	
	

	/**
	 * 统计渠道
	 * @param channelId
	 * @return
	 */
	public ChannelStat statChannel(Integer siteId,Integer channelId,Date date);

}
