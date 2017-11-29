package com.ada.log.bean;


/**
 * 访问日志 Entity
 */
public class AccessLog  implements java.io.Serializable{
	private static final long serialVersionUID = -3291838374333591320L;
                   
    /** 站点ID */
	private Integer siteId;                    
    /** 域名ID */
	private Integer domainId;                    
    /** 渠道ID */
	private Integer channelId;                    
    /** IP地址 */
	private String ipAddress;
	private String uuid;
    /** 浏览页 */
	private String url;                    
    /** 客户端头信息 */
	private String useragent;                    
    /** 操作系统 */
	private String os;                    
    /** 浏览器 */
	private String browser;                    
    /** 屏幕大小 */
	private String screenSize;                    
    /** 页面大小 */
	private String pageSize;                    
    /** 引用页 */
	private String referer;                    
    /** 在Iframe中 */
	private Integer iframe;                    
    /** 首次访问时间 */
	private Long firstTime;                    
    /** 当天首次访问时间 */
	private Long todayTime;                    
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
	
	public String getUseragent(){
		return this.useragent;
	}
	
	public void setUseragent(String useragent){
		this.useragent = useragent;
	}
	
	public String getOs(){
		return this.os;
	}
	
	public void setOs(String os){
		this.os = os;
	}
	
	public String getBrowser(){
		return this.browser;
	}
	
	public void setBrowser(String browser){
		this.browser = browser;
	}
	
	public String getScreenSize(){
		return this.screenSize;
	}
	
	public void setScreenSize(String screenSize){
		this.screenSize = screenSize;
	}
	
	public String getPageSize(){
		return this.pageSize;
	}
	
	public void setPageSize(String pageSize){
		this.pageSize = pageSize;
	}
	
	public String getReferer(){
		return this.referer;
	}
	
	public void setReferer(String referer){
		this.referer = referer;
	}
	
	public Integer getIframe(){
		return this.iframe;
	}
	
	public void setIframe(Integer iframe){
		this.iframe = iframe;
	}

	public Long getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(Long firstTime) {
		this.firstTime = firstTime;
	}

	public Long getTodayTime() {
		return todayTime;
	}

	public void setTodayTime(Long todayTime) {
		this.todayTime = todayTime;
	}

	public Long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	

	
}
