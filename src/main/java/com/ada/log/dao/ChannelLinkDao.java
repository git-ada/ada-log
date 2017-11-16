package com.ada.log.dao;

import java.util.List;

import com.ada.log.bean.ChannelLink;

public interface ChannelLinkDao {

	
	/**
	 * 查询站点链接
	 * @param siteId
	 * @return
	 */
	public List<ChannelLink> findBySiteId(Integer siteId);
}
