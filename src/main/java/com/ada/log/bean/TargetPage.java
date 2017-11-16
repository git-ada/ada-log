package com.ada.log.bean;

public class TargetPage {

    /** 站点ID */
	private Integer siteId;                    
    /** 网页地址 */
	private String url;
	public Integer getSiteId() {
		return siteId;
	}
	public String getUrl() {
		return url;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
