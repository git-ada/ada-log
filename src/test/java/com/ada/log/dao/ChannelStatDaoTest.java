package com.ada.log.dao;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ada.log.bean.ChannelStat;
import com.ada.log.bean.SiteStat;

@ActiveProfiles("local")
@ContextConfiguration(locations = "classpath:spring/context.xml")
@TransactionConfiguration(defaultRollback = false)
public class ChannelStatDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final Logger logger = LoggerFactory.getLogger(ChannelStatDaoTest.class);

	@Autowired
	private ChannelStatDao channelStatDao;
	
	@Autowired
	private SiteStatDao siteStatDao;

	@Test
	public void testSave(){
		ChannelStat item = new ChannelStat();
		item.setChannelId(1);
		item.setClickip1(1);
		channelStatDao.save(item);
	}
	
	@Test
	public void testSavesite(){
		SiteStat item = new SiteStat();
		item.setSiteId(1);
		item.setDate(new Date());
		item.setIp(10);
		item.setPv(200);
		siteStatDao.save(item);
	}

}
