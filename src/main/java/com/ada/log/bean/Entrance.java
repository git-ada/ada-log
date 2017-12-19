package com.ada.log.bean;

import java.sql.Timestamp;

/**
 * 入口
 * @author
 */
public class Entrance {
	private Integer adId;
	private String page;
	private Timestamp time;
	
	public Entrance() {
		super();
	}
	public Entrance(Integer adId, String page, Timestamp time) {
		super();
		this.adId = adId;
		this.page = page;
		this.time = time;
	}

	public static final Integer EntranceAd = 1;
	public static final Integer EntranceNotAd = 2;
	
	public Integer getAdId() {
		return adId;
	}
	public void setAdId(Integer adId) {
		this.adId = adId;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Entrance [adId=" + adId + ", page=" + page + ", time=" + time
				+ "]";
	}
	
	public Integer getType(){
		if(adId!=null){
			return EntranceAd;
		}else{
			return EntranceNotAd;
		}
	}
}
