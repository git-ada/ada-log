package com.ada.log.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ada.log.bean.Channel;
import com.ada.log.dao.ChannelDao;
@Service
public class ChannelDaoImpl implements ChannelDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

//	@Override
//	public List<Channel> findBySiteId(Integer siteId) {
//		// TODO Auto-generated method stub
//		List<Channel> channels = new ArrayList<Channel>();
//		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select id,siteId,channelName from ada_channel where siteId=?",siteId);
//		if(queryForList!=null && queryForList.size()>0){
//			for(int i=0;i<queryForList.size();i++){
//				Map map = queryForList.get(i);
//				Channel channel = new Channel();
//				channel.setId((Integer) map.get("id"));
//				channel.setSiteId((Integer) map.get("siteId"));
//				channel.setChannelName((String) map.get("channelName"));
//				channels.add(channel);
//			}
//		}
//		
//		return channels;
//	}
	
	@Override
	public List<Channel> findBySiteId(Integer siteId) {
		List<Channel> channels = jdbcTemplate.queryForList("select * from ada_channel where siteId=?", Channel.class, siteId);
		return channels;
	}

	@Override
	public List<Channel> findBySiteIdAndChannelStr(Integer siteId,String channelStr) {
		List<Channel> channels = jdbcTemplate.queryForList("select * from ada_channel where siteId=? and channelStr =?", Channel.class, siteId,channelStr);
		return channels;
	}

}
