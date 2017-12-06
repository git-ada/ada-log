package com.ada.log.service;


/**
 * IP数据库服务
 * @author ASUS
 *
 */
public interface IPDBService {

	/**
	 * 获得IP地址所在地区，如：美国芝加哥,中国香港，中国北京，中国重庆，中国澳门，菲利宾，中国台湾，英国
	 * @param ipAddress 23.163.27.189
	 * @return
	 */
	public String getRegion(String ipAddress);
}
