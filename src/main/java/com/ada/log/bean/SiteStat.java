package com.ada.log.bean;

import java.util.Date;

/**
 * 站点统计结果
 */
public class SiteStat {

	private Integer siteId;
	private Integer ip;
	private Integer pv;
	private Date date;

	public Integer getSiteId() {
		return siteId;
	}

	public Integer getIp() {
		return ip;
	}

	public Integer getPv() {
		return pv;
	}

	public Date getDate() {
		return date;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public void setIp(Integer ip) {
		this.ip = ip;
	}

	public void setPv(Integer pv) {
		this.pv = pv;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
