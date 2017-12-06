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
	DomainScroll4IP("","DomainScroll4IP_",""),
	
	SiteOldUserIP("站点老用户数","SiteOldUserIP_",""),
	ChannelOldUserIP("渠道老用户数","ChannelOldUserIP_",""),
	DomainOldUserIP("域名老用户数","DomainOldUserIP_",""),

	/**2017-12-04新增**/
	DomainCitySet("当天访问域名的城市列表","DomainCitySet_",""),
	
	DomainAdIP("域名广告入口IP","DomainAdIP_",""),
	DomainCityIP("域名下城市IP","DomainCityIP_",""),
	DomainAdCityIP("域名下的城市广告入口IP","DomainAdCityIP_",""),
	
	DomainAdPV("域名广告入口PV","DomainAdPV_",""),
	DomainCityPV("域名下城市PV","DomainCityPV_",""),
	DomainAdCityPV("域名下的城市广告入口PV","DomainAdCityPV_",""),
	
	SiteUV("站点UV","SiteUV_",""),
	DomainUV("域名UV","DomainUV_",""),
	DomainAdUV("域名广告入口UV","DomainAdUV_",""),
	DomainCityUV("域名下城市UV","DomainCityUV_",""),
	DomainAdCityUV("域名下的城市广告入口UV","DomainAdCityUV_",""),
	
	DomainAdTIP("域名广告入口进入目标页IP集合","DomainAdTIP_",""),
	DomainCityTIP("域名下城市进入目标页IP集合","DomainCityTIP_",""),
	DomainAdCityTIP("域名下的城市广告入口进入目标页IP集合","DomainAdCityTIP_",""),
	
	DomainAdOldUserIP("域名广告入口老用户数","DomainAdOldUserIP_",""),
	DomainCityOldUserIP("域名下城市老用户数","DomainCityOldUserIP_",""),
	DomainAdCityOldUserIP("域名下的城市广告入口老用户数","DomainAdCityOldUserIP_",""),
	
	DomainOldIP("域名老IP","DomainOldIP_",""),
	DomainAdOldIP("域名广告入口老IP","DomainAdOldIP_",""),
	DomainCityOldIP("域名下城市老IP","DomainCityOldIP_",""),
	DomainAdCityOldIP("域名下城市广告入口老IP","DomainAdCityOldIP_",""),
	
	DomainIPMap("域名历史IPMap","DomainIPMap_",""),
	DomainAdIPMap("域名广告入口历史IPMap","DomainAdIPMap_",""),
	DomainCityIPMap("域名下城市历史IPMap","DomainCityIPMap_",""),
	DomainCityAdIPMap("域名下城市广告入口历史IPMap","DomainCityAdIPMap_",""),
	
	DomainLoginIp("域名登录用户数","DomainLoginIp_",""),
	DomainAdLoginIp("域名广告入口登录用户数","DomainAdLoginIp_",""),
	DomainCityLoginIp("域名下城市登录用户数","DomainCityLoginIp_",""),
	DomainAdCityLoginIp("域名下城市广告入口登录用户数","DomainAdCityLoginIp_",""),
	
	DomainAdC1IP("域名广告入口鼠标点击1-2次IP数","DomainAdC1IP_",""),
	DomainAdC2IP("域名广告入口鼠标点击3-5次IP数","DomainAdC2IP_",""),
	DomainAdC3IP("域名广告入口鼠标点击6-10次IP数","DomainAdC3IP_",""),
	DomainAdC4IP("域名广告入口鼠标点击10次以上IP数","DomainAdC4IP_",""),
	DomainCityC1IP("域名下城市鼠标点击1-2次IP数","DomainCityC1IP_",""),
	DomainCityC2IP("域名下城市鼠标点击3-5次IP数","DomainCityC2IP_",""),
	DomainCityC3IP("域名下城市鼠标点击6-10次IP数","DomainCityC3IP_",""),
	DomainCityC4IP("域名下城市鼠标点击10次以上IP数","DomainCityC4IP_",""),
	DomainCityAdC1IP("域名下城市广告入口鼠标点击1-2次IP数","DomainCityAdC1IP_",""),
	DomainCityAdC2IP("域名下城市广告入口鼠标点击3-5次IP数","DomainCityAdC2IP_",""),
	DomainCityAdC3IP("域名下城市广告入口鼠标点击6-10次IP数","DomainCityAdC3IP_",""),
	DomainCityAdC4IP("域名下城市广告入口鼠标点击10次以上IP数","DomainCityAdC4IP_",""),
	
	DomainAdStayTime1IP("","DomainAdStayTime1IP_",""),
	DomainAdStayTime2IP("","DomainAdStayTime2IP_",""),
	DomainAdStayTime3IP("","DomainAdStayTime3IP_",""),
	DomainAdStayTime4IP("","DomainAdStayTime4IP_",""),
	DomainCityStayTime1IP("","DomainCityStayTime1IP_",""),
	DomainCityStayTime2IP("","DomainCityStayTime2IP_",""),
	DomainCityStayTime3IP("","DomainCityStayTime3IP_",""),
	DomainCityStayTime4IP("","DomainCityStayTime4IP_",""),
	DomainCityAdStayTime1IP("","DomainCityAdStayTime1IP_",""),
	DomainCityAdStayTime2IP("","DomainCityAdStayTime2IP_",""),
	DomainCityAdStayTime3IP("","DomainCityAdStayTime3IP_",""),
	DomainCityAdStayTime4IP("","DomainCityAdStayTime4IP_",""),
	
	DomainAdMouseMove1IP("","DomainAdMouseMove1IP_",""),
	DomainAdMouseMove2IP("","DomainAdMouseMove2IP_",""),
	DomainAdMouseMove3IP("","DomainAdMouseMove3IP_",""),
	DomainAdMouseMove4IP("","DomainAdMouseMove4IP_",""),
	DomainCityMouseMove1IP("","DomainCityMouseMove1IP_",""),
	DomainCityMouseMove2IP("","DomainCityMouseMove2IP_",""),
	DomainCityMouseMove3IP("","DomainCityMouseMove3IP_",""),
	DomainCityMouseMove4IP("","DomainCityMouseMove4IP_",""),
	DomainCityAdMouseMove1IP("","DomainCityAdMouseMove1IP_",""),
	DomainCityAdMouseMove2IP("","DomainCityAdMouseMove2IP_",""),
	DomainCityAdMouseMove3IP("","DomainCityAdMouseMove3IP_",""),
	DomainCityAdMouseMove4IP("","DomainCityAdMouseMove4IP_",""),
	
	DomainAdScroll1IP("","DomainAdScroll1IP_",""),
	DomainAdScroll2IP("","DomainAdScroll2IP_",""),
	DomainAdScroll3IP("","DomainAdScroll3IP_",""),
	DomainAdScroll4IP("","DomainAdScroll4IP_",""),
	DomainCityScroll1IP("","DomainCityScroll1IP_",""),
	DomainCityScroll2IP("","DomainCityScroll2IP_",""),
	DomainCityScroll3IP("","DomainCityScroll3IP_",""),
	DomainCityScroll4IP("","DomainCityScroll4IP_",""),
	DomainCityAdScroll1IP("","DomainCityAdScroll1IP_",""),
	DomainCityAdScroll2IP("","DomainCityAdScroll2IP_",""),
	DomainCityAdScroll3IP("","DomainCityAdScroll3IP_",""),
	DomainCityAdScroll4IP("","DomainCityAdScroll4IP_","")
	
	
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
