package com.ada.log.web;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ada.log.event.MouseClickEventHandle;
import com.ada.log.event.MouseMoveEventHandle;
import com.ada.log.event.ScrollEventHandle;
import com.ada.log.event.StayTimeEventHandle;
import com.ada.log.service.ChannelService;
import com.ada.log.service.DomainService;
import com.ada.log.service.LogService;
import com.ada.log.util.IpUtils;
import com.alibaba.fastjson.JSONObject;

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
	
	@RequestMapping(value = "track")
	public void queryChannel( HttpServletRequest request,
            HttpServletResponse response) {
		
		log.info(request.getRequestURL().append(request.getQueryString()));
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
	
	
	/**
	 * 查询渠道
	 * @param uuid            客户端UUID
	 * @param siteId          站点ID
	 * @param browsingPage    用户当前访问地址
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "q")
	public void queryChannel(@RequestParam(value="u",required=false)String uuid,
			                   @RequestParam(value="s",required=false)Integer siteId,
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
			log.debug(ipAddress+ " Q u->"+uuid+",s->"+siteId+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
		}
		
		if(siteId==null){
			return;
		}
		
		try {
			browsingPage = URLDecoder.decode(browsingPage, "utf-8");
		} catch (Exception e1) {
		}
		if(siteId!=null && browsingPage!=null){
			Integer channelId = channelService.queryChannel(siteId, browsingPage);
			/** 允许跨域访问 **/
			try {
				response.setHeader("Access-Control-Allow-Origin", "*");
				if(channelId!=null){
					response.getWriter().println(channelId.toString());
					if(log.isDebugEnabled()){
						log.debug("匹配到渠道,siteId->"+siteId+",browsingPage->"+browsingPage+",channelId->"+channelId);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value = "l1")
	public void log1(@RequestParam(value="u",required=false)String uuid,
	          @RequestParam(value="s",required=false)Integer siteId,
	          @RequestParam(value="c",required=false)Integer channelId,
	          @RequestParam(value="p",required=false)String browsingPage,
	          @RequestParam(value="o",required=false)String firstTime,
	          @RequestParam(value="t",required=false)String timestamp,
	          @RequestHeader(value="User-Agent",required=false)String useragent,
	          @RequestHeader(value="Referer",required=false)String referer,
	          @RequestHeader(value="Cookie",required=false)String cookie,
		      HttpServletRequest request,
	          HttpServletResponse response){
		
		String ipAddress = IpUtils.getIpAddr(request);
		if(log.isDebugEnabled()){
			log.debug(ipAddress+" L1 u->"+uuid+",s->"+siteId+",c->"+channelId+",o->" + firstTime + ",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
		}
		
		if(siteId==null){
			return;
		}
		
		try {
			browsingPage = URLDecoder.decode(browsingPage, "utf-8");
		} catch (Exception e1) {
		}
		
		if(channelId==null && browsingPage!=null){
			channelId = channelService.queryChannel(siteId, browsingPage);
			if(log.isDebugEnabled()){
				log.debug("匹配到渠道,siteId->"+siteId+",browsingPage->"+browsingPage+",channelId->"+channelId);
			}
		}
		
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			JSONObject ret = new JSONObject();
			if(channelId!=null){
				ret.put("c", channelId);
			}
			ret.put("o", System.currentTimeMillis());
			response.getWriter().println(ret.toJSONString());
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		
		Boolean isOldUser = false;
		if(firstTime!=null && !"".equals(firstTime) && !"1".equals(firstTime)&& !"0".equals(firstTime)){
			try {
				boolean isSmpeDate = isSameDate(new Date(Long.valueOf(firstTime)), new Date());
				if(!isSmpeDate){
					isOldUser = true;
				}
			} catch (Exception e) {
			}
		}
		
		logService.log1(ipAddress, uuid, siteId, channelId,domainId,browsingPage,isOldUser);
	}
	
	public boolean isSameDate(Date date1,Date date2){
		if(date1 !=null && date2 !=null){
			if(date1.getYear() == date2.getYear()
				&& date1.getMonth() == date2.getMonth()
				&& date1.getDate() == date2.getDate()
				){
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping(value = "l2")
	public void onClick(@RequestParam(value="u",required=false)String uuid,
			          @RequestParam(value="s",required=false)Integer siteId,
			          @RequestParam(value="c",required=false)Integer channelId,
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
			log.debug(ipAddress+" L2 u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+clickNum+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
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
		
		try {
			browsingPage = URLDecoder.decode(browsingPage, "utf-8");
		} catch (Exception e1) {
		}
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		
		//logService.log2(ipAddress, uuid, siteId, channelId,domainId, clickNum);
		mouseClickEventHandle.handle(ipAddress, uuid, siteId, channelId, domainId, clickNum, browsingPage);
	}
	
	@RequestMapping(value = "l3")
	public void onStayTime(@RequestParam(value="u",required=false)String uuid,
			          @RequestParam(value="s",required=false)Integer siteId,
			          @RequestParam(value="c",required=false)Integer channelId,
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
			log.debug(ipAddress+" L3 u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+number+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
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
		
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		stayTimeEventHandle.handle(ipAddress, uuid, siteId, channelId, domainId, number, browsingPage);
	}
	
	@RequestMapping(value = "l4")
	public void onMouseMove(@RequestParam(value="u",required=false)String uuid,
			          @RequestParam(value="s",required=false)Integer siteId,
			          @RequestParam(value="c",required=false)Integer channelId,
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
			log.debug(ipAddress+" L3 u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+number+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
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
		
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		mouseMoveEventHandle.handle(ipAddress, uuid, siteId, channelId, domainId, number, browsingPage);
	}
	
	
	@RequestMapping(value = "l5")
	public void onScroll(@RequestParam(value="u",required=false)String uuid,
			          @RequestParam(value="s",required=false)Integer siteId,
			          @RequestParam(value="c",required=false)Integer channelId,
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
			log.debug(ipAddress+" L3 u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+number+",p->"+browsingPage+",t->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
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
		
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		scrollEventHandle.handle(ipAddress, uuid, siteId, channelId, domainId, number, browsingPage);
	}
	
	/**
	 * 提交日志
	 * @param uuid          客户端UUID
	 * @param siteId        站点ID
	 * @param channelId     渠道ID
	 * @param clickNum      点击次数
	 * @param browsingTime  浏览时间,精确到毫秒
	 * @param browsingPage   当前页面链接
	 * @return
	 */
	@RequestMapping(value = "l")
	public void log(@RequestParam(value="u",required=false)String uuid,
			          @RequestParam(value="s",required=false)Integer siteId,
			          @RequestParam(value="c",required=false)Integer channelId,
			          @RequestParam(value="n",required=false)Integer clickNum,
			          @RequestParam(value="t",required=false)Integer browsingTime,
			          @RequestParam(value="p",required=false)String browsingPage,
			          @RequestParam(value="t1",required=false)String timestamp,
			          @RequestHeader(value="User-Agent",required=false)String useragent,
	                  @RequestHeader(value="Referer",required=false)String referer,
	                  @RequestHeader(value="Cookie",required=false)String cookie,
			          HttpServletRequest request,
	                  HttpServletResponse response
			          ) {

		String ipAddress = IpUtils.getIpAddr(request);
		if(log.isDebugEnabled()){
			log.debug(ipAddress+" u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+clickNum+",t->"+browsingTime+",p->"+browsingPage+",t1->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
		}
		
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			browsingPage = URLDecoder.decode(browsingPage, "utf-8");
		} catch (Exception e1) {
		}
		
		String domain = getDomain(browsingPage);//得到域名
		Integer domainId = domainService.queryDomain(siteId, domain);
		logService.log(ipAddress, uuid, siteId, channelId,domainId, clickNum, browsingTime, browsingPage);
		
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
	
	public static void main(String[] args){
		MainController impl = new MainController();
//		String str ="https://datatables.net/extensions/fixedcolumns/examples/initialisation/right_column.html";
//		String str =null ;
		String str ="";
//		String str ="http://45.125.219.106:8002/index.jhtm";
		
		String str1 = "cityu";
		String str2 = "cityu";
//		String str3 = "city"+"u";
		System.out.println(str1==str2);
		String domain = impl.getDomain(str);
		
		System.out.println(domain);
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
	
//	public static void main(String[] args){
//		MainController impl = new MainController();
//		boolean isSmpeDate = impl.isSameDate(new Date(Long.valueOf("1")), new Date());
//		System.out.println(new Date(Long.valueOf("1")));
//	}
}
