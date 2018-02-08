package com.yorbee.qgs.bigdata.hbase.entity;

import java.sql.Timestamp;

@Table("ada_access_log")
public class AccessLog implements java.io.Serializable{
	@Column("serialVersionUID")
	private static final long serialVersionUID = -3291838374333591320L;
	@Column("id")  
	private Long id;

	@Column("siteId")
	private Integer siteId;                    

	@Column("domainId")
	private Integer domainId;                    

	@Column("channelId")
	private Integer channelId;

	@Column("adId")
	private Integer adId;

	@Column("entranceType")
	private Integer entranceType;

	@Column("ipAddress")
	private String ipAddress;

	@Column("region")
	private String region;

	@Column("uuid")
	private String uuid;

	@Column("url")
	private String url;                    

	@Column("useragent")
	private String useragent;                    

	@Column("os")
	private String os;                    

	@Column("browser")
	private String browser;                    

	@Column("screenSize")
	private String screenSize;                    

	@Column("pageSize")
	private String pageSize;                    

	@Column("referer")
	private String referer;                    

	@Column("iframe")
	private Integer iframe;                    

	@Column("firstTime")
	private Long firstTime;                    

	@Column("todayTime")
	private Long todayTime;                    

	@Column("requestTime")
	private Long requestTime;
	
	
	@Column("createTime")
	private Timestamp  createTime;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	

	public boolean isTodayFirst(){
		return todayTime !=null;
	}

	public Integer getEntranceType() {
		return entranceType;
	}

	public void setEntranceType(Integer entranceType) {
		this.entranceType = entranceType;
	}

}
