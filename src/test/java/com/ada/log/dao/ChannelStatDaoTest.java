package com.ada.log.dao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ada.log.bean.Channel;
import com.ada.log.bean.ChannelLink;
import com.ada.log.bean.ChannelStat;
import com.ada.log.bean.Site;
import com.ada.log.bean.SiteStat;
import com.ada.log.bean.TargetPage;
import com.ada.log.service.SiteService;

@ActiveProfiles("local")
@ContextConfiguration(locations = "classpath:spring/context.xml")
@TransactionConfiguration(defaultRollback = false)
public class ChannelStatDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static final Logger logger = LoggerFactory.getLogger(ChannelStatDaoTest.class);

	@Autowired
	private ChannelStatDao channelStatDao;
	
	@Autowired
	private SiteStatDao siteStatDao;
	//////////////////////////////////
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private ChannelLinkDao channelLinkDao;
	@Autowired
	private SiteDao siteDao;
	@Autowired
	private TargetPageDao targetPageDao;
	@Autowired
	private SiteService siteService;
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
	@Test
	public void testchannelDao(){
		List<Channel> findBySiteId = channelDao.findBySiteId(1);
		System.out.println("开始测试");
		if(findBySiteId!=null && findBySiteId.size()>0){
			
			for(int i=0;i<findBySiteId.size();i++){
				Channel channel = findBySiteId.get(i);
				System.out.println(channel.getChannelName()+"__"+channel.getId()+"__"+channel.getSiteId());
			}
			
		}
		
	}
	
	@Test
	public void testchannelLinkDao(){
		
		List<ChannelLink> findBySiteId = channelLinkDao.findBySiteId(1);
		System.out.println("开始测试");
		if(findBySiteId!=null && findBySiteId.size()>0){
			
			for(int i=0;i<findBySiteId.size();i++){
				ChannelLink channelLink = findBySiteId.get(i);
				System.out.println(channelLink.getSiteId()+"__"+channelLink.getChannelId()+"__"+channelLink.getUrl()+"__"+channelLink.getParameter());
			}
			
		}
	}
	
	@Test
	public void testsiteDao(){
		
		List<Site> findAll = siteDao.findAll();
		System.out.println("开始测试");
		if(findAll!=null && findAll.size()>0){
			
			for(int i=0;i<findAll.size();i++){
				Site site = findAll.get(i);
				System.out.println(site.getId()+"__"+site.getSiteName());
			}
			
		}
	}
	
	@Test
	public void testtargetPageDao(){
		
		List<TargetPage> findBySiteId = targetPageDao.findBySiteId(1);
		System.out.println("开始测试");
		if(findBySiteId!=null && findBySiteId.size()>0){
			
			for(int i=0;i<findBySiteId.size();i++){
				TargetPage targetPage = findBySiteId.get(i);
				System.out.println(targetPage.getSiteId()+"__"+targetPage.getUrl());
			}
			
		}
		
	}
	
	@Test
	public void SiteServiceImpl() throws UnsupportedEncodingException{
		
		String url = URLEncoder.encode("http://aadfad.com?asdfsdf","utf-8");
		
		//String url = URLEncoder.encode("http://ASDFADSF.com?asdfsdf","utf-8");
		
		System.out.println("------------------->"+siteService.matchTargetPage(1, url));
		
		
	}
	

}
