package com.ada.log.bean;

public class Site {

    /** 站点ID */
	private Integer id;                                   
    /** 站点名称 */
	private String siteName;
	
	public Integer getId() {
		return id;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
}
