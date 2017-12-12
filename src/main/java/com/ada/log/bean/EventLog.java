package com.ada.log.bean;



/**
 * 访问日志 Entity
 */
public class EventLog  implements java.io.Serializable{
	private static final long serialVersionUID = -3291838374333591320L;
                   
    /** 站点ID */
	private Integer siteId;                    
    /** 域名ID */
	private Integer domainId;                    
    /** 渠道ID */
	private Integer channelId;
	/** 广告ID */
	private Integer adId;
    /** IP地址 */
	private String ipAddress;
	/** 地区 */
	private String region;
	/** 客户端ID */
	private String uuid;
	/** 事件 **/
	private String event;
	/** 参数 **/
	private String args;
    /** 浏览页 */
	private String url;                    
    /** 客户端请求时间 */
	private Long requestTime;          
	
	public Integer getSiteId(){
		return this.siteId;
	}
	
	public void setSiteId(Integer siteId){
		this.siteId = siteId;
	}
	
	public Integer getDomainId(){
		return this.domainId;
	}
	
	public void setDomainId(Integer domainId){
		this.domainId = domainId;
	}
	
	public Integer getChannelId(){
		return this.channelId;
	}
	
	public void setChannelId(Integer channelId){
		this.channelId = channelId;
	}
	
	public String getIpAddress(){
		return this.ipAddress;
	}
	
	public void setIpAddress(String ipAddress){
		this.ipAddress = ipAddress;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}

	public Integer getAdId() {
		return adId;
	}

	public void setAdId(Integer adId) {
		this.adId = adId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public Long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}
	
	
}
