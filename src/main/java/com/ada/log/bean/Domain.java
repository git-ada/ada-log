package com.ada.log.bean;

/** 
 * 域名
 * @author ASUS
 *
 */
public class Domain {

    /** 域名ID */
	private Integer id;                    
    /** 站点ID */
	private Integer siteId;
	/** domain*/
	private String domain;
	
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
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	

}
