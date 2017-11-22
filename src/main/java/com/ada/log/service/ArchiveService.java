package com.ada.log.service;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ada.log.bean.Channel;
import com.ada.log.bean.ChannelStat;
import com.ada.log.bean.Domain;
import com.ada.log.bean.DomainStat;
import com.ada.log.bean.Site;
import com.ada.log.bean.SiteStat;
import com.ada.log.dao.ChannelDao;
import com.ada.log.dao.ChannelStatDao;
import com.ada.log.dao.DomainDao;
import com.ada.log.dao.DomainStatDao;
import com.ada.log.dao.SiteDao;
import com.ada.log.dao.SiteStatDao;
import com.ada.log.util.Dates;

@Service
public class ArchiveService {
	
	private final static Log log = LogFactory.getLog(ArchiveService.class);
	
	@Autowired
	private StatService statService;
	
	@Autowired
	private ChannelStatDao channelStatDao;
	
	@Autowired
	private DomainStatDao domainStatDao;
	
	@Autowired
	private SiteStatDao siteStatDao;
	
	@Autowired
	private ChannelDao channelDao;
	
	@Autowired
	private DomainDao domainDao;
	
	@Autowired
	private SiteDao siteDao;
	
	/**
	 * 归档昨日站点统计数据
	 */
	
	@Transactional(readOnly=false,propagation=Propagation.REQUIRED)
	public void archive() {
		Date yestoday = Dates.yestoday();
		
		List<Site> sites = siteDao.findAll();
		for(Site site:sites){
			try {
			    SiteStat stat = statService.statSite(site.getId(), yestoday);
			    siteStatDao.save(stat);
			    log.info("站点 "+site.getId()+":"+site.getSiteName() +" 归档成功");
			} catch (Exception e) {
				log.error("站点 "+site.getId()+":"+site.getSiteName() +" 归档失败,Msg->"+e.getMessage(),e);
			}
			
			 List<Channel> channels = channelDao.findBySiteId(site.getId());
			 for(Channel channel:channels){
				try {
				    ChannelStat channelStat =  statService.statChannel(site.getId(), channel.getId(), yestoday);
				    channelStatDao.save(channelStat);
				    log.info("渠道 "+channel.getId()+":"+channel.getChannelName()+" 归档成功");
				} catch (Exception e) {
					log.info("渠道 "+channel.getId()+":"+channel.getChannelName()+" 归档失败,Msg->"+e.getMessage(),e);
				}
			 }
			 
			 List<Domain> domains = domainDao.findBySiteId(site.getId());
			 for(Domain domain:domains){
				try {
					DomainStat domainStat =  statService.statDomain(site.getId(), domain.getId(), yestoday);
					domainStatDao.save(domainStat);
				    log.info("域名 "+domain.getId()+":"+domain.getDomain()+" 归档成功");
				} catch (Exception e) {
					log.info("域名 "+domain.getId()+":"+domain.getDomain()+" 归档失败,Msg->"+e.getMessage(),e);
				}
			 }
		}
	}

}
