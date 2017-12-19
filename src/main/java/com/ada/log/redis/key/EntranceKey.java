package com.ada.log.redis.key;

import java.nio.ByteBuffer;

import com.ada.log.redis.JedisBytesKey;

/**
 * IP入口
 * @author ASUS
 *
 */
public class EntranceKey extends JedisBytesKey{
	/** RedisKey唯一前缀**/
	private final static short ID = 1239;
	
	public EntranceKey(Integer domainId, Integer ip) {
		this.domainId = domainId;
		this.ip = ip;
	}

	private Integer domainId;
	private Integer ip;
	
	public Integer getDomainId() {
		return domainId;
	}
	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}
	public Integer getIp() {
		return ip;
	}
	public void setIp(Integer ip) {
		this.ip = ip;
	}
	/** 10个字节 **/
	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.putShort(ID);
		buffer.putInt(domainId);
		buffer.putInt(ip);
		return buffer.array();
	}

	public static void main(String[] args){
		byte[] bs = null;
		bs = new EntranceKey(0,0).toBytes();
		for(byte b:bs){
			System.out.print(b);
		}
	}
}
