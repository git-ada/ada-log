package com.ada.log.bean;

import java.io.Serializable;


/**
 * 渠道链接
 */
public class ChannelLink implements Serializable {

	private static final long serialVersionUID = 3764301133934411588L;
	    
	/** 站点ID */
	private Integer siteId;
	/** 渠道ID */
	private Integer channelId;                    
    /** 网页链接地址 */
	private String url;                    
    /** 参数 */
	private String parameter;                  
	
    public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	
	public Integer getChannelId(){
		return this.channelId;
	}
	
	public void setChannelId(Integer channelId){
		this.channelId = channelId;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getParameter(){
		return this.parameter;
	}
	
	public void setParameter(String parameter){
		this.parameter = parameter;
	}
	
	
}
