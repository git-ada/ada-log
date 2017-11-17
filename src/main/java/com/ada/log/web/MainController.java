package com.ada.log.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	/**
	 * 查询渠道
	 * @param uuid            客户端UUID
	 * @param siteId          站点ID
	 * @param browsingPage    用户当前访问地址
	 * @return
	 */
	@RequestMapping(value = "q")
	public void queryChannel(@RequestParam("u")String uuid,
			                   @RequestParam("s")Integer siteId,
			                   @RequestParam("p")String browsingPage,
			                   HttpServletRequest request,
			                   HttpServletResponse response
			                   ){
		if(log.isDebugEnabled()){
			log.debug("u->"+uuid+",s->"+siteId+",p->"+browsingPage);
		}
		
		Integer channelId = channelService.queryChannel(siteId, browsingPage);
		
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println(channelId.toString());
		} catch (IOException e) {
			e.printStackTrace();
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
	public String log(@RequestParam("u")String uuid,
			          @RequestParam("s")Integer siteId,
			          @RequestParam("c")Integer channelId,
			          @RequestParam("n")Integer clickNum,
			          @RequestParam("t")Integer browsingTime,
			          @RequestParam("p")String browsingPage,
			          HttpServletRequest request,
	                  HttpServletResponse response
			          ) {

		String ipAddress = IpUtils.getIpAddr(request);
		if(log.isDebugEnabled()){
			log.debug(ipAddress+" u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+clickNum+",t->"+browsingTime+",p->"+browsingPage);
		}
		
		/** 允许跨域访问 **/
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logService.log(ipAddress, uuid, siteId, channelId, clickNum, browsingTime, browsingPage);
		
	
		return "ok";
	}
}
