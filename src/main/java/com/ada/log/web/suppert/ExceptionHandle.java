package com.ada.log.web.suppert;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常拦截处理
 */
public class ExceptionHandle implements HandlerExceptionResolver{
	private final static Log log = LogFactory.getLog(ExceptionHandle.class);

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
	
		try {
			/** 无论任何异常都返回成功 **/
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println("");
			response.getWriter().close();
		} catch (IOException e) {
		}
		
		String local = "";
		if(ex.getStackTrace()!=null && ex.getStackTrace().length>0){
			StackTraceElement st = ex.getStackTrace()[0];
			local = "("+st.getClassName()+":"+st.getLineNumber()+")";
			
		}
		
		log.error(new StringBuffer().append(request.getRequestURI()).append("?").append(request.getQueryString()).toString());
		if(log.isDebugEnabled()){
			log.error(ex.getClass().getName()+local,ex);
		}else{
			log.error(ex.getClass().getName()+local);
		}
		
		return new ModelAndView();
	}

}
