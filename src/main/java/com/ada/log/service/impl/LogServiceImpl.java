package com.ada.log.service.impl;

import org.springframework.stereotype.Service;

import com.ada.log.service.LogService;

/**
 * 核心实现
 *
 */
@Service
public class LogServiceImpl implements LogService{

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
	public void log(String ipAddress,String uuid, Integer siteId, Integer channelId,String clickNum, String browsingTime, String browsingPage) {
	}

	/**
	 * 保存站点IPSet集合
	 * @param siteId       站点ID
	 * @param ipAddress    IP地址
	 */
	protected void putSiteIPSet(Integer siteId,String ipAddress) {
		
	}
	
	/**
	 * 保存站点PV +1
	 * @param siteId       站点ID
	 * @param ipAddress
	 */
	protected void increSitePV(Integer siteId) {
		
	}
	
	/**
	 * 保存渠道IPSet集合
	 * @param channelId    渠道ID
	 * @param ipAddress    IP地址
	 */
	protected void putChannelIPSet(Integer channelId,String ipAddress){
		;
	}
	
	/**
	 * 保存站点PV +1
	 * @param siteId       站点ID
	 * @param ipAddress    IP地址
	 */
	protected void increChannelPV(Integer channelId) {
		;	
	}

	/**
	 * 
	 * @param ipAddress   IP地址
	 * @param clickNum    页面点击次数
	 * @return
	 */
	protected Integer increIPClickNum(String ipAddress,Integer pageClickNum){
		return null;
	}
	
	/**
	 * 更新渠道多个点击次数区间
	 * @param channelId   渠道ID
	 * @param ipClickNum  IP累计点击次数
	 */
	protected void updateChannelMultipleRangeClickIP(Integer channelId,Integer ipClickNum){
		if(ipClickNum>=1 && ipClickNum<=2){
			
		}else if(ipClickNum>=1 && ipClickNum<=2){
			
		}else if(ipClickNum>=3 && ipClickNum<=5){
			
		}else if(ipClickNum>=6 && ipClickNum<=10){
			
		}else if(ipClickNum>10){
			
		}
	}
	
	/**
	 * 匹配是否目标页
	 * @param siteId
	 * @param browsingPage 
	 * @return
	 */
	protected boolean matchTargetPage(Integer siteId,String browsingPage){
		return false;
	}

	/**
	 * 
	 * 渠道进入进入目标页IP集合
	 * @param channelId
	 */
	protected void putChannelTIPSet(Integer channelId,String ipAddress){
		
	}

}
