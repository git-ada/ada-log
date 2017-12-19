package com.ada.log.redis;


public abstract class JedisBytesValue {

	public abstract byte[] toBytes();
	public abstract void setBytes(byte[] bytes);
	
}
