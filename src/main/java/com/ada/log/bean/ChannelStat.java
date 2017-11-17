package com.ada.log.bean;

import java.util.Date;

/**
 * 渠道统计结果
 */
public class ChannelStat {

	private Integer siteId;
	private Integer channelId;
	private Integer ip;
	private Integer pv;
	private Integer clickip1;
	private Integer clickip2;
	private Integer clickip3;
	private Integer clickip4;
	private Integer targetpageip;
	private Date date;
	

	public ChannelStat() {
		super();
	}

	public ChannelStat(Integer siteId, Integer channelId, Integer ip,
			Integer pv, Integer clickip1, Integer clickip2, Integer clickip3,
			Integer clickip4, Integer targetpageip, Date date) {
		super();
		this.siteId = siteId;
		this.channelId = channelId;
		this.ip = ip;
		this.pv = pv;
		this.clickip1 = clickip1;
		this.clickip2 = clickip2;
		this.clickip3 = clickip3;
		this.clickip4 = clickip4;
		this.targetpageip = targetpageip;
		this.date = date;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public Integer getIp() {
		return ip;
	}

	public Integer getPv() {
		return pv;
	}

	public Integer getClickip1() {
		return clickip1;
	}

	public Integer getClickip2() {
		return clickip2;
	}

	public Integer getClickip3() {
		return clickip3;
	}

	public Integer getClickip4() {
		return clickip4;
	}

	public Integer getTargetpageip() {
		return targetpageip;
	}

	public Date getDate() {
		return date;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public void setIp(Integer ip) {
		this.ip = ip;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public void setClickip1(Integer clickip1) {
		this.clickip1 = clickip1;
	}

	public void setClickip2(Integer clickip2) {
		this.clickip2 = clickip2;
	}

	public void setClickip3(Integer clickip3) {
		this.clickip3 = clickip3;
	}

	public void setClickip4(Integer clickip4) {
		this.clickip4 = clickip4;
	}

	public void setTargetpageip(Integer targetpageip) {
		this.targetpageip = targetpageip;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
