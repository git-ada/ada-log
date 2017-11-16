package com.ada.log.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ada.log.service.ChannelService;

/**
 * 主控制器
 * @author wanghl
 */
@Controller
public class MainController {

	private final static Log log = LogFactory.getLog(MainController.class);
	
	@Autowired
	private ChannelService channelService;
	
	/**
	 * 查询渠道
	 * @param uuid            客户端UUID
	 * @param siteId          站点ID
	 * @param browsingPage    用户当前访问地址
	 * @return
	 */
	@RequestMapping(value = "q")
	@ResponseBody
	public Integer queryChannel(@RequestParam("u")String uuid,
			                   @RequestParam("s")Integer siteId,
			                   @RequestParam("p")String browsingPage
			                   ){
		if(log.isDebugEnabled()){
			log.debug("u->"+uuid+",s->"+siteId+",p->"+browsingPage);
		}
		
		Integer channelId = channelService.queryChannel(siteId, browsingPage);

		/** 返回渠道Id **/
		return channelId;
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
	@ResponseBody
	public String log(@RequestParam("u")String uuid,
			          @RequestParam("s")String siteId,
			          @RequestParam("c")String channelId,
			          @RequestParam("n")String clickNum,
			          @RequestParam("t")String browsingTime,
			          @RequestParam("p")String browsingPage
			          ) {
		if(log.isDebugEnabled()){
			log.debug("u->"+uuid+",s->"+siteId+",c->"+channelId+",n->"+clickNum+",t->"+browsingTime+",p->"+browsingPage);
		}

		return "ok";
	}
}
