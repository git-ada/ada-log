package com.yorbee.qgs.bigdata.hbase.entity;
@Table("ada_event_log")
public class EventLog implements java.io.Serializable{
	@Column("serialVersionUID")
	private static final long serialVersionUID = -3291838374333591320L;
	 @Column("id")  
	private String id;
    /** 站点ID */
	@Column("siteId")
	private Integer siteId;                    
    /** 域名ID */
	@Column("domainId")
	private Integer domainId;                    
    /** 渠道ID */
	@Column("channelId")
	private Integer channelId;
	/** 广告ID */
	@Column("adId")
	private Integer adId;
    /** IP地址 */
	@Column("ipAddress")
	private String ipAddress;
	/** 地区 */
	@Column("region")
	private String region;
	/** 客户端ID */
	@Column("uuid")
	private String uuid;
	/** 事件 **/
	@Column("event")
	private String event;
	/** 参数 **/
	@Column("args")
	private String args;
    /** 浏览页 */
	@Column("url")
	private String url;                    
    /** 客户端请求时间 */
	@Column("requestTime")
	private Long requestTime;
	
	@Column("createTime")
	private Long createTime;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	

}
