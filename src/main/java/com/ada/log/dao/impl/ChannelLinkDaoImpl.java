package com.ada.log.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ada.log.bean.Channel;
import com.ada.log.bean.ChannelLink;
import com.ada.log.dao.ChannelLinkDao;
@Service
public class ChannelLinkDaoImpl implements ChannelLinkDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<ChannelLink> findBySiteId(Integer siteId) {
		// TODO Auto-generated method stub
		List<ChannelLink> list = new ArrayList<ChannelLink>();
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select siteId,channelId,url,parameter from ada_channel_link where siteId=?",siteId);
		if(queryForList!=null && queryForList.size()>0){
			for(int i=0;i<queryForList.size();i++){
				Map map = queryForList.get(i);
				ChannelLink channelLink = new ChannelLink();
				channelLink.setSiteId((Integer) map.get("siteId"));
				channelLink.setChannelId((Integer) map.get("channelId"));
				channelLink.setUrl((String) map.get("url"));
				channelLink.setParameter((String) map.get("parameter"));
				
				list.add(channelLink);
			}
		}
		
		return list;
	}

}
