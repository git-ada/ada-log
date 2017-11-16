package com.ada.log.dao;

import java.util.List;

import com.ada.log.bean.Channel;

public interface ChannelDao {

	/**
	 * 通过站点ID查询下属所有渠道
	 * @param siteId
	 * @return
	 */
	public List<Channel> findBySiteId(Integer siteId);
	
	
}
