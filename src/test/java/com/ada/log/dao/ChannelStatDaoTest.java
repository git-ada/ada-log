package com.ada.log.dao;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ada.log.service.impl.DomainServiceImpl;

@ActiveProfiles("local")
@ContextConfiguration(locations = "classpath:spring/context.xml")
@TransactionConfiguration(defaultRollback = false)
public class ChannelStatDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final Logger logger = LoggerFactory.getLogger(ChannelStatDaoTest.class);

	@Autowired
	private DomainServiceImpl domainService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void testSave(){
		
		for(int i=0;i<10;i++){
			new Thread(new InsertTask()).start();
		}
		
		while(true){
			
		}
	}
	
	public class InsertTask implements Runnable{
		@Override
		public void run() {
			Integer domianiId =domainService.queryForUpdateDomain(1000, "www.1632232.com");
			System.out.println(domianiId);
		}
		
	}

}
