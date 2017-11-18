package com.ada.log.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ada.log.bean.ChannelStat;
import com.ada.log.dao.ChannelStatDao;

@Service
public class ChannelStatDaoImpl implements ChannelStatDao {
	
	private final static Log log = LogFactory.getLog(ChannelStatDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void save(ChannelStat item) {
		jdbcTemplate.update("insert into ada_channel_stat(siteId,channelId,ip,pv,clickip1,clickip2,clickip3,clickip4,targetpageip,date,createTime) values(?,?,?,?,?,?,?,?,?,?,now())", 
				item.getSiteId(),
				item.getChannelId(),
				item.getIp(),
				item.getPv(),
				item.getClickip1(),
				item.getClickip2(),
				item.getClickip3(),
				item.getClickip4(),
				item.getTargetpageip(),
				item.getDate());
		
		if(log.isDebugEnabled()){
			log.info("保存渠道统计结果成功");
		}
	}

}
