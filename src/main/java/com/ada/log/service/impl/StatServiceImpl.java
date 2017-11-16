package com.ada.log.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.ada.log.bean.ChannelStat;
import com.ada.log.bean.SiteStat;
import com.ada.log.service.StatService;

@Service
public class StatServiceImpl implements StatService{

	@Override
	public SiteStat statSite(Integer site, Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChannelStat statChannel(Integer siteId, Integer channelId, Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
