package com.ada.log.constant;

/**
 * Redis数据库,键值元数据
 * @author 老腊肉
 *
 */
public enum RedisKeys {

	ChannelC1IP("渠道鼠标点击1-2次IP数","ChannelC1IP_",""),
	ChannelC2IP("渠道鼠标点击3-5次IP数","ChannelC2IP_",""),
	ChannelC3IP("渠道鼠标点击6-10次IP数","ChannelC3IP_",""),
	ChannelC4IP("渠道鼠标点击10次以上IP数","ChannelC4IP_","");
	;
	private String title;
	private String key;
	private String desc;
	
	RedisKeys(String title,String key,String desc){
		this.title = title;
		this.key = key;
		this.desc = desc;
	}

	public String getTitle() {
		return title;
	}

	public String getKey() {
		return key;
	}

	public String getDesc() {
		return desc;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}