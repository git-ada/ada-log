package com.ada.log.constant;

/**
 * Redis数据库,键值元数据
 * @author 老腊肉
 *
 */
public enum RedisKeys {
	
	SitePV("站点PV","SitePV_",""),
	SiteIP("站点IP","SiteIP_",""),
	ChannelPV("渠道PV","ChannelPV_",""),
	ChannelIP("渠道IP","ChannelIP_",""),
	DomainPV("域名PV","DomainPV_",""),
	DomainIP("域名IP","DomainIP_",""),
	ChannelC1IP("渠道鼠标点击1-2次IP数","ChannelC1IP_",""),
	ChannelC2IP("渠道鼠标点击3-5次IP数","ChannelC2IP_",""),
	ChannelC3IP("渠道鼠标点击6-10次IP数","ChannelC3IP_",""),
	ChannelC4IP("渠道鼠标点击10次以上IP数","ChannelC4IP_",""),
	DomainC1IP("域名鼠标点击1-2次IP数","DomainC1IP_",""),
	DomainC2IP("域名鼠标点击3-5次IP数","DomainC2IP_",""),
	DomainC3IP("域名鼠标点击6-10次IP数","DomainC3IP_",""),
	DomainC4IP("域名鼠标点击10次以上IP数","DomainC4IP_",""),
	CIPNum("页面鼠标点击次数","CIPNum_",""),
	ChannelTIP("渠道进入目标页IP集合","ChannelTIP_",""),
	DomainTIP("域名进入目标页IP集合","DomainTIP_",""),
	
	ChannelStayTime1IP("","ChannelStayTime1IP_",""),
	ChannelStayTime2IP("","ChannelStayTime2IP_",""),
	ChannelStayTime3IP("","ChannelStayTime3IP_",""),
	ChannelStayTime4IP("","ChannelStayTime4IP_",""),
	
	DomainStayTime1IP("","DomainStayTime1IP_",""),
	DomainStayTime2IP("","DomainStayTime2IP_",""),
	DomainStayTime3IP("","DomainStayTime3IP_",""),
	DomainStayTime4IP("","DomainStayTime4IP_",""),
	
	ChannelMouseMove1IP("","ChannelMouseMove1IP_",""),
	ChannelMouseMove2IP("","ChannelMouseMove2IP_",""),
	ChannelMouseMove3IP("","ChannelMouseMove3IP_",""),
	ChannelMouseMove4IP("","ChannelMouseMove4IP_",""),
	
	DomainMouseMove1IP("","DomainMouseMove1IP_",""),
	DomainMouseMove2IP("","DomainMouseMove2IP_",""),
	DomainMouseMove3IP("","DomainMouseMove3IP_",""),
	DomainMouseMove4IP("","DomainMouseMove4IP_",""),
	
	ChannelScroll1IP("","ChannelScroll1IP_",""),
	ChannelScroll2IP("","ChannelScroll2IP_",""),
	ChannelScroll3IP("","ChannelScroll3IP_",""),
	ChannelScroll4IP("","ChannelScroll4IP_",""),
	
	DomainScroll1IP("","DomainScroll1IP_",""),
	DomainScroll2IP("","DomainScroll2IP_",""),
	DomainScroll3IP("","DomainScroll3IP_",""),
	DomainScroll4IP("","DomainScroll4IP_","")
	;
	
	private String title;
	private String key;
	private String desc;
	
	RedisKeys(String title,String key,String desc){
		this.title = title;
		this.key = key;
		this.desc = desc;
	}

	public String getTitle() {
		return title;
	}

	public String getKey() {
		return key;
	}

	public String getDesc() {
		return desc;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
