package com.ada.log.bean;

/** 
 * 渠道
 * @author ASUS
 *
 */
public class Channel {

    /** 渠道ID */
	private Integer id;                    
    /** 站点ID */
	private Integer siteId;
	
	private Integer domainId;
	
	private Integer adId;
	/** 渠道名称*/
	private String channelName;
	
	private String channelStr;

	
	public Integer getDomainId() {
		return domainId;
	}
	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}
	public Integer getAdId() {
		return adId;
	}
	public void setAdId(Integer adId) {
		this.adId = adId;
	}
	public String getChannelStr() {
		return channelStr;
	}
	public void setChannelStr(String channelStr) {
		this.channelStr = channelStr;
	}

	
	public Integer getId() {
		return id;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

}
