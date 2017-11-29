package com.ada.log.service;

import java.net.URL;

/**
 * 域名相关服务处理
 * @author zhao xiang
 * @since  2017/11/21
 *
 */
public interface DomainService {

	/**
	 * 查询域名, 如果没有查询到，后面则要自动insert to MySQL
	 * @param siteId        站点ID
	 * @param domain        domain
	 * @return              domainID
	 */
	public Integer queryDomain(Integer siteId,String domain);
	

	/**
	 * 获取域名
	 * @param browsingPage
	 * @return
	 */
	public String getDomain(String browsingPage);
}
