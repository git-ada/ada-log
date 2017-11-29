package com.ada.log.bean;

/** 广告页 **/
public class ADPage {
	
	private Integer id;
	private Integer siteId;
	private String channelKey; //渠道参数名
	private String matchContent; //匹配内容
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getChannelKey() {
		return channelKey;
	}
	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}
	public String getMatchContent() {
		return matchContent;
	}
	public void setMatchContent(String matchContent) {
		this.matchContent = matchContent;
	}


}
