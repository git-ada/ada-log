package com.ada.log.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ada.log.service.ChannelService;
import com.ada.log.service.LogService;
import com.ada.log.util.IpUtils;

/**
 * 主控制器
 * @author wanghl
 */
@Controller
public class MainController {

	private final static Log log = LogFactory.getLog(MainController.class);
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private LogService logService;
	
	@RequestMapping(value = "track")
	public void queryChannel( HttpServletRequest request,
            HttpServletResponse response) {
		
		log.info(request.getRequestURL().append(request.getQueryString()));
	}
	
	
	/**
	 * 查询渠道
	 * @param uuid            客户端UUID
	 * @param siteId          站点ID
	 * @param browsingPage    用户当前访问地址
	 * @return
	 */
	@RequestMapping(value = "q")
	public void queryChannel(@RequestParam(value="u",required=false)String uuid,
			                   @RequestParam(value="s",required=false)Integer siteId,
			                   @RequestParam(value="p",required=false)String browsingPage,
			                   @RequestParam(value="t1",required=false)String timestamp,
			                   
			                   @RequestHeader(value="User-Agent",required=false)String useragent,
			                   @RequestHeader(value="Referer",required=false)String referer,
			                   @RequestHeader(value="Cookie",required=false)String cookie,
			                   
			                   HttpServletRequest request,
			                   HttpServletResponse response
			                   ){
		String ipAddress = IpUtils.getIpAddr(request);
		if(log.isDebugEnabled()){
			log.debug(ipAddress+ " u->"+uuid+",s->"+siteId+",p->"+browsingPage+",t1->"+timestamp+" "+ useragent+ " "+ cookie+ " "+ referer);
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
		logService.log(ipAddress, uuid, siteId, channelId, clickNum, browsingTime, browsingPage);
		
	}
}
