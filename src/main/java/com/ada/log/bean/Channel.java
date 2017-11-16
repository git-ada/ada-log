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

}
