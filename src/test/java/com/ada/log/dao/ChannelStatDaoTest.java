package com.ada.log.dao;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ada.log.bean.ChannelStat;

@ActiveProfiles("local")
@ContextConfiguration(locations = "classpath:spring/context.xml")
@TransactionConfiguration(defaultRollback = false)
public class ChannelStatDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final Logger logger = LoggerFactory.getLogger(ChannelStatDaoTest.class);

	@Autowired
	private ChannelStatDao channelStatDao;

	@Test
	public void testSave(){
		ChannelStat item = new ChannelStat();
		item.setChannelId(1);
		item.setClickip1(1);
		channelStatDao.save(item);
	}

}
