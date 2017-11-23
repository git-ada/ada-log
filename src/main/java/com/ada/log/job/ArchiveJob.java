package com.ada.log.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ada.log.service.ArchiveService;

@Service
public class ArchiveJob {
	
	private final static Log log = LogFactory.getLog(ArchiveJob.class);
	
	@Autowired
	private ArchiveService archiveService;

//	@Scheduled(cron="0 45 0 * * ?")
	public void excute(){
		log.info("开始执行归档作业");
		Long startTime = System.currentTimeMillis();
		archiveService.archive();
		Long endTime = System.currentTimeMillis();
		Long cost = endTime - startTime;
		
		log.info("结束归档作业，用时"+cost+"ms");
	}

}
