package com.ada.log.web;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ada.log.bean.ADPage;
import com.ada.log.bean.EventLog;
import com.ada.log.event.LoginEventHandle;
import com.ada.log.event.MouseClickEventHandle;
import com.ada.log.event.MouseMoveEventHandle;
import com.ada.log.event.ScrollEventHandle;
import com.ada.log.event.StayTimeEventHandle;
import com.ada.log.service.ChannelService;
import com.ada.log.service.DomainService;
import com.ada.log.service.EntranceService;
import com.ada.log.service.IPDBService;
import com.ada.log.service.LogService;
import com.ada.log.util.IpUtils;
import com.alibaba.fastjson.JSONObject;
import com.yorbee.qgs.bigdata.hbase.entity.AccessLog;

/**
 * 主控制器
 * @author wanghl
 */
@Controller
@SuppressWarnings("all")
public class MainController {

	private final static Log log = LogFactory.getLog(MainController.class);
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private DomainService domainService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private MouseMoveEventHandle mouseMoveEventHandle;
	
	@Autowired
	private ScrollEventHandle scrollEventHandle;
	
	@Autowired
	private StayTimeEventHandle stayTimeEventHandle;
	
	@Autowired
	private MouseClickEventHandle mouseClickEventHandle;
	
	@Autowired
	private LoginEventHandle loginEventHandle;
	
	@Autowired
	private IPDBService IPDBService;
	
	@Autowired
	private EntranceService entranceService;
	
	private String clientVersion = "1";
	
	@RequestMapping(value = "l1")
	public void onAccess(
			  @RequestParam(value="u",required=false)String uuid,
	          @RequestParam(value="s",required=false)Integer siteId,
	          @RequestParam(value="c",required=false)Integer channelId,
	          @RequestParam(value="a",required=false)Integer adId,
	          
	          @RequestParam(value="e",required=false)Integer entranceType, /** 1:广告入口，2:非广告入口 **/
	          @RequestParam(value="ep",required=false)String entrancePage,
	          @RequestParam(value="v",required=false)String version,
	          
	          @RequestParam(value="p",required=false)String browsingPage,
	          @RequestParam(value="o",required=false)String firstTime,
	          @RequestParam(value="t",required=false)String timestamp,
	          @RequestParam(value="f",required=false)String firstTimeToday,
	          @RequestParam(value="r",required=false)String beforReferer,
	          @RequestParam(value="os",required=false)String os,
	          @RequestParam(value="br",required=false)String browser,
	          @RequestParam(value="ss",required=false)String screenSize,
	          @RequestParam(value="ps",required=false)String pageSize,
	          @RequestParam(value="if",required=false)Integer iframe,
	          @RequestParam(value="ua",required=false)String jsuseragent,
	          @RequestHeader(value="User-Agent",required=false)String useragent,
	          @RequestHeader(value="Referer",required=false)String referer,
	          @RequestHeader(value="Cookie",required=false)String cookie,
		      HttpServletRequest request,
	          HttpServletResponse response){
		
		String ipAddress = IpUtils.getIpAddr(request);
		String region = IPDBService.getRegion(ipAddress);
		if(region==null && "".equals(region)){
			region = "未知地区";
		}
		if(log.isDebugEnabled()){
			log.debug(ipAddress+" "+region+" onAccess u->"+uuid+",s->"+siteId+",c->"+channelId + ",p->"+browsingPage+ ",r->"+beforReferer+",o->"+firstTime+",f->"+firstTimeToday+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
		}
		
		if(siteId==null){
			return;
		}
		
		if(!clientVersion.equals(version)){
			return;
		}
		
		try {
			browsingPage = URLDecoder.decode(browsingPage, "utf-8");
		} catch (Exception e1) {
		}
		
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		
		if(channelId==null && browsingPage!=null){
			channelId = channelService.queryChannel(siteId, browsingPage);
			if(log.isDebugEnabled() && channelId !=null){
				log.debug("匹配到渠道,siteId->"+siteId+",browsingPage->"+browsingPage+",channelId->"+channelId);
			}
		}
		
		ADPage adPage = null;
		if(entranceType==null || "".equals(entranceType)){
			adPage = channelService.matchAdPage(siteId, browsingPage);
			if(log.isDebugEnabled() && adPage !=null){
				log.debug("匹配到广告,siteId->"+siteId+",browsingPage->"+browsingPage+",adaId->"+adPage.getId());
			}
			if(adPage!=null){
				adId = adPage.getId();
			}
			entranceType = entranceService.getAndSetEntrance(ipAddress, domainId, adId, browsingPage).getType();
		}
		Long now = System.currentTimeMillis();
		
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			JSONObject ret = new JSONObject();
			if(channelId!=null){
				ret.put("c", channelId);
			}
			if(adPage !=null ){
				ret.put("a", adPage.getId());
			}
			ret.put("o", now);
			ret.put("e", entranceType);
			response.getWriter().println(ret.toJSONString());
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AccessLog req = new AccessLog();
		req.setSiteId(siteId);
		req.setDomainId(domainId);
		req.setChannelId(channelId);
		req.setAdId(adId);
		req.setIpAddress(ipAddress);
		req.setRegion(region);
		req.setEntranceType(entranceType);
		req.setUrl(browsingPage);
		req.setUuid(uuid);
		req.setUseragent(jsuseragent);
		req.setReferer(beforReferer);
		req.setOs(os);
		req.setBrowser(browser);
		req.setScreenSize(screenSize);
		req.setPageSize(pageSize);
		req.setIframe(iframe);
		
		try {
			if(firstTime!=null && !"".equals(firstTime)){
				req.setFirstTime(Long.valueOf(firstTime));
			}else{
				req.setFirstTime(now);
			}
		} catch (Exception e) {
			log.error("解析firstTime失败->"+firstTime);
		}
		
		if(firstTimeToday!=null && !"".equals(firstTimeToday)){
			req.setTodayTime(now);
		}
		req.setRequestTime(now);
		try {
			logService.log(req);
		} catch (Exception e) {
			log.error(e);
		}
		
	}
	
	@RequestMapping(value = "l2")
	public void onClick(@RequestParam(value="u",required=false)String uuid,
			          @RequestParam(value="s",required=false)Integer siteId,
			          @RequestParam(value="c",required=false)Integer channelId,
			          @RequestParam(value="a",required=false)Integer adId,
			          @RequestParam(value="e",required=false)Integer entranceType,
			          @RequestParam(value="ep",required=false)String entrancePage,
			          @RequestParam(value="v",required=false)String version,
			          @RequestParam(value="n",required=false)Integer clickNum,
			          @RequestParam(value="p",required=false)String browsingPage,
			          @RequestParam(value="t",required=false)String timestamp,
			          @RequestHeader(value="User-Agent",required=false)String useragent,
	                  @RequestHeader(value="Referer",required=false)String referer,
	                  @RequestHeader(value="Cookie",required=false)String cookie,
			          HttpServletRequest request,
	                  HttpServletResponse response
			          ) {
		String ipAddress = IpUtils.getIpAddr(request);
		if(log.isDebugEnabled()){
			log.debug(ipAddress+" onClick u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+clickNum+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
		}
		
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(siteId==null){
			return;
		}
		
		if(!clientVersion.equals(version)){
			return;
		}

		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		
		ADPage adPage = null;
		if(entranceType==null || "".equals(entranceType)){
			adPage = channelService.matchAdPage(siteId, browsingPage);
			if(log.isDebugEnabled() && adPage !=null){
				log.debug("匹配到广告,siteId->"+siteId+",browsingPage->"+browsingPage+",adaId->"+adPage.getId());
			}
			if(adPage!=null){
				adId = adPage.getId();
			}
			entranceType = entranceService.getAndSetEntrance(ipAddress, domainId, adId, browsingPage).getType();
		}

		String region = IPDBService.getRegion(ipAddress);
		if(region==null && "".equals(region)){
			region = "未知地区";
		}
		
		//logService.log2(ipAddress, uuid, siteId, channelId,domainId, clickNum);
		mouseClickEventHandle.handle(ipAddress, uuid, siteId, channelId, domainId, adId,entranceType,region, clickNum);
		
		EventLog log = new EventLog();
		log.setAdId(adId);
		log.setDomainId(domainId);
		log.setIpAddress(ipAddress);
		log.setUuid(uuid);
		log.setSiteId(siteId);
		log.setRegion(region);
		log.setUrl(browsingPage);
		log.setEvent("onClick");
		log.setRequestTime(Long.valueOf(timestamp));
		log.setArgs(clickNum.toString());
		logService.log(log);
	}
	
	@RequestMapping(value = "l3")
	public void onStayTime(@RequestParam(value="u",required=false)String uuid,
			          @RequestParam(value="s",required=false)Integer siteId,
			          @RequestParam(value="c",required=false)Integer channelId,
			          @RequestParam(value="a",required=false)Integer adId,
			          @RequestParam(value="e",required=false)Integer entranceType,
			          @RequestParam(value="ep",required=false)String entrancePage,
			          @RequestParam(value="v",required=false)String version,
			          @RequestParam(value="n",required=false)Integer number,
			          @RequestParam(value="p",required=false)String browsingPage,
			          @RequestParam(value="t",required=false)String timestamp,
			          @RequestHeader(value="User-Agent",required=false)String useragent,
	                  @RequestHeader(value="Referer",required=false)String referer,
	                  @RequestHeader(value="Cookie",required=false)String cookie,
			          HttpServletRequest request,
	                  HttpServletResponse response
			          ) {
		String ipAddress = IpUtils.getIpAddr(request);
		if(log.isDebugEnabled()){
			log.debug(ipAddress+" onStayTime u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+number+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
		}
		
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(siteId==null){
			return;
		}
		
		if(!clientVersion.equals(version)){
			return;
		}
		
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		
		ADPage adPage = null;
		if(entranceType==null || "".equals(entranceType)){
			adPage = channelService.matchAdPage(siteId, browsingPage);
			if(log.isDebugEnabled() && adPage !=null){
				log.debug("匹配到广告,siteId->"+siteId+",browsingPage->"+browsingPage+",adaId->"+adPage.getId());
			}
			if(adPage!=null){
				adId = adPage.getId();
			}
			entranceType = entranceService.getAndSetEntrance(ipAddress, domainId, adId, browsingPage).getType();
		}
		
		String region = IPDBService.getRegion(ipAddress);
		if(region==null && "".equals(region)){
			region = "未知地区";
		}
		stayTimeEventHandle.handle(ipAddress, uuid, siteId, channelId, domainId, adId,entranceType,region, number);
		
		
		EventLog log = new EventLog();
		log.setAdId(adId);
		log.setDomainId(domainId);
		log.setIpAddress(ipAddress);
		log.setUuid(uuid);
		log.setSiteId(siteId);
		log.setRegion(region);
		log.setUrl(browsingPage);
		log.setEvent("onStayTime");
		log.setRequestTime(Long.valueOf(timestamp));
		log.setArgs(number.toString());
		logService.log(log);
	}
	
	@RequestMapping(value = "l4")
	public void onMouseMove(@RequestParam(value="u",required=false)String uuid,
			          @RequestParam(value="s",required=false)Integer siteId,
			          @RequestParam(value="c",required=false)Integer channelId,
			          @RequestParam(value="a",required=false)Integer adId,
			          @RequestParam(value="e",required=false)Integer entranceType,
			          @RequestParam(value="ep",required=false)String entrancePage,
			          @RequestParam(value="v",required=false)String version,
			          @RequestParam(value="n",required=false)Integer number,
			          @RequestParam(value="p",required=false)String browsingPage,
			          @RequestParam(value="t",required=false)String timestamp,
			          @RequestHeader(value="User-Agent",required=false)String useragent,
	                  @RequestHeader(value="Referer",required=false)String referer,
	                  @RequestHeader(value="Cookie",required=false)String cookie,
			          HttpServletRequest request,
	                  HttpServletResponse response
			          ) {
		String ipAddress = IpUtils.getIpAddr(request);
		if(log.isDebugEnabled()){
			log.debug(ipAddress+" onMouseMove u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+number+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
		}
		
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(siteId==null){
			return;
		}
		
		if(!clientVersion.equals(version)){
			return;
		}
		
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		
		ADPage adPage = null;
		if(entranceType==null || "".equals(entranceType)){
			adPage = channelService.matchAdPage(siteId, browsingPage);
			if(log.isDebugEnabled() && adPage !=null){
				log.debug("匹配到广告,siteId->"+siteId+",browsingPage->"+browsingPage+",adaId->"+adPage.getId());
			}
			if(adPage!=null){
				adId = adPage.getId();
			}
			entranceType = entranceService.getAndSetEntrance(ipAddress, domainId, adId, browsingPage).getType();
		}
		
		String region = IPDBService.getRegion(ipAddress);
		if(region==null && "".equals(region)){
			region = "未知地区";
		}
		mouseMoveEventHandle.handle(ipAddress, uuid, siteId, channelId, domainId, adId,entranceType,region, number);
		
		EventLog log = new EventLog();
		log.setAdId(adId);
		log.setDomainId(domainId);
		log.setIpAddress(ipAddress);
		log.setUuid(uuid);
		log.setSiteId(siteId);
		log.setRegion(region);
		log.setUrl(browsingPage);
		log.setEvent("onMouseMove");
		log.setRequestTime(Long.valueOf(timestamp));
		log.setArgs(number.toString());
		logService.log(log);
	}
	
	
	@RequestMapping(value = "l5")
	public void onScroll(@RequestParam(value="u",required=false)String uuid,
			          @RequestParam(value="s",required=false)Integer siteId,
			          @RequestParam(value="c",required=false)Integer channelId,
			          @RequestParam(value="a",required=false)Integer adId,
			          @RequestParam(value="e",required=false)Integer entranceType,
			          @RequestParam(value="ep",required=false)String entrancePage,
			          @RequestParam(value="v",required=false)String version,
			          @RequestParam(value="n",required=false)Integer number,
			          @RequestParam(value="p",required=false)String browsingPage,
			          @RequestParam(value="t",required=false)String timestamp,
			          @RequestHeader(value="User-Agent",required=false)String useragent,
	                  @RequestHeader(value="Referer",required=false)String referer,
	                  @RequestHeader(value="Cookie",required=false)String cookie,
			          HttpServletRequest request,
	                  HttpServletResponse response
			          ) {
		String ipAddress = IpUtils.getIpAddr(request);
		if(log.isDebugEnabled()){
			log.debug(ipAddress+" onScroll u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+number+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
		}
		
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(siteId==null){
			return;
		}
		
		if(!clientVersion.equals(version)){
			return;
		}
		
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		
		ADPage adPage = null;
		if(entranceType==null || "".equals(entranceType)){
			adPage = channelService.matchAdPage(siteId, browsingPage);
			if(log.isDebugEnabled() && adPage !=null){
				log.debug("匹配到广告,siteId->"+siteId+",browsingPage->"+browsingPage+",adaId->"+adPage.getId());
			}
			if(adPage!=null){
				adId = adPage.getId();
			}
			entranceType = entranceService.getAndSetEntrance(ipAddress, domainId, adId, browsingPage).getType();
		}
		
		String region = IPDBService.getRegion(ipAddress);
		if(region==null && "".equals(region)){
			region = "未知地区";
		}
		scrollEventHandle.handle(ipAddress, uuid, siteId, channelId, domainId, adId,entranceType,region, number);
		
		EventLog log = new EventLog();
		log.setAdId(adId);
		log.setDomainId(domainId);
		log.setIpAddress(ipAddress);
		log.setUuid(uuid);
		log.setSiteId(siteId);
		log.setRegion(region);
		log.setUrl(browsingPage);
		log.setEvent("onScroll");
		log.setRequestTime(Long.valueOf(timestamp));
		log.setArgs(number.toString());
		logService.log(log);
	}
	
	
	@RequestMapping(value = "l6")
	public void onLogin(@RequestParam(value="u",required=false)String uuid,
			          @RequestParam(value="s",required=false)Integer siteId,
			          @RequestParam(value="c",required=false)Integer channelId,
			          @RequestParam(value="a",required=false)Integer adId,
			          @RequestParam(value="e",required=false)Integer entranceType,
			          @RequestParam(value="ep",required=false)String entrancePage,
			          @RequestParam(value="v",required=false)String version,
			          @RequestParam(value="p",required=false)String browsingPage,
			          @RequestParam(value="t",required=false)String timestamp,
			          @RequestHeader(value="User-Agent",required=false)String useragent,
	                  @RequestHeader(value="Referer",required=false)String referer,
	                  @RequestHeader(value="Cookie",required=false)String cookie,
			          HttpServletRequest request,
	                  HttpServletResponse response
			          ) {
		
		String ipAddress = IpUtils.getIpAddr(request);
		if(log.isDebugEnabled()){
			log.debug(ipAddress+" onLogin u->"+uuid+",s->"+siteId+",c->"+channelId+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
		}
		
		if(siteId==null){
			return;
		}
		
		if(!clientVersion.equals(version)){
			return;
		}
		
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		
		ADPage adPage = null;
		if(entranceType==null || "".equals(entranceType)){
			adPage = channelService.matchAdPage(siteId, browsingPage);
			if(log.isDebugEnabled() && adPage !=null){
				log.debug("匹配到广告,siteId->"+siteId+",browsingPage->"+browsingPage+",adaId->"+adPage.getId());
			}
			if(adPage!=null){
				adId = adPage.getId();
			}
			entranceType = entranceService.getAndSetEntrance(ipAddress, domainId, adId, browsingPage).getType();
		}
		
		String region = IPDBService.getRegion(ipAddress);
		if(region==null && "".equals(region)){
			region = "未知地区";
		}
		loginEventHandle.handle(ipAddress, uuid, siteId, channelId, domainId, region, adId,entranceType);
		
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		EventLog log = new EventLog();
		log.setAdId(adId);
		log.setDomainId(domainId);
		log.setIpAddress(ipAddress);
		log.setUuid(uuid);
		log.setSiteId(siteId);
		log.setRegion(region);
		log.setUrl(browsingPage);
		log.setRequestTime(Long.valueOf(timestamp));
		log.setEvent("onLogin");
		logService.log(log);
	}
	
	/**
	 * 获取域名
	 * @param browsingPage
	 * @return
	 */
	public String getDomain(String browsingPage) {
		String domain = null;
		Object o=browsingPage;
		if(browsingPage != null && !"".equals(browsingPage)){
			try {
				URL url = new URL(browsingPage);
				domain = url.getHost();
				int port = url.getPort();
				if(port == 80 || port == -1){
					return domain;
				}
				return domain+":"+port;
			} catch (Exception e) {
				log.error("域名解析错误: ---> "+browsingPage);
			}
		}
		return null;
	}
	
	protected Integer getEntranceType() {
		return null;
	}
	
	@RequestMapping(value = "ping")
	public void ping(
			@RequestHeader(value="User-Agent",required=false)String useragent,
            @RequestHeader(value="Referer",required=false)String referer,
            @RequestHeader(value="Cookie",required=false)String cookie,
			HttpServletRequest request,HttpServletResponse response) {
		String ipAddress = IpUtils.getIpAddr(request);
		if(log.isDebugEnabled()){
			log.debug(ipAddress +" ping "+ useragent+ " "+ cookie+ " "+ referer);
		}
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "d")
	public void debug(
			@RequestParam(value="u",required=false)String uuid,
			@RequestParam(value="s",required=false)Integer siteId,
			@RequestParam(value="m",required=false)String message,
			@RequestParam(value="t",required=false)String timestamp,
			@RequestParam(value="p",required=false)String browsingPage,
            @RequestHeader(value="User-Agent",required=false)String useragent,
            @RequestHeader(value="Referer",required=false)String referer,
            @RequestHeader(value="Cookie",required=false)String cookie,
            HttpServletRequest request,
            HttpServletResponse response){
		if(log.isDebugEnabled()){
			String ipAddress = IpUtils.getIpAddr(request);
			log.debug(ipAddress+ " DEBUG m->"+message+",u->"+uuid+",s->"+siteId+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
		}
	}
	
}
