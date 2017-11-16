package com.ada.log.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ada.log.dao.ChannelStatDao;
import com.ada.log.dao.SiteStatDao;

@Service
public class ArchiveService {
	
	@Autowired
	private StatService statService;
	
//	@Autowired
	private ChannelStatDao channelStatDao;
	
//	@Autowired
	private SiteStatDao siteStatDao;
	
	/**
	 * 将每日的Redis中统计数据持续化保存到数据库
	 */
	public void execute(Date date) {
		archiveSite();
		archiveChannel();
		cleanYestodyRedisData();
	}
	
	/**
	 * 归档昨日站点统计数据
	 */
	protected void archiveSite() {
//		for(){ //遍历每个站点数据保存结果
//			SiteStat item = statService.statYestodySite(site);
//			siteStatDao.save(item);
//		}
	}
	
	
	/**
	 * 归档昨日渠道统计数据
	 */
	protected void archiveChannel() {

//		for(){ //遍历每个渠道数据保存结果
//			ChannelStat item = statService.statYestodyChannel(channelId);
//			channelStatDao.save(item);
//		}
	}
	
	/**
	 * 清除昨日REDIS数据
	 */
	protected void cleanYestodyRedisData(){
		
	}
}
