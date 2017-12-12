package com.ada.log.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ada.log.bean.EventLog;

@ActiveProfiles("local")
@ContextConfiguration(locations = "classpath:spring/context.xml")
@TransactionConfiguration(defaultRollback = false)
public class EventLogDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final Logger logger = LoggerFactory.getLogger(EventLogDaoTest.class);


	
	@Autowired
	private AccessLogDao accessLogDao;
	
	@Test
	public void testSave(){
		
		EventLog log = new EventLog();
		
		List list = new ArrayList();
		list.add(log);
		
		accessLogDao.batchInsertEventLog(list);
	}
	
}
