package com.ada.log.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 作者:simple.song
 * @date 创建时间：2016年12月13日 下午1:45:33
 * 类说明：request.getRemoteAddr()这种方法在大部分情况下都是有效的。但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了。
 */
public class IpUtils {
	
	/**
	 * 解决反向代理获取不到真是ip地址
	 * @param request	请求信息
	 * @return
	 */
    public static String getIpAddr(HttpServletRequest request) {  
        String [] headeNames = new String[]{"X-Real-IP","X-real-ip","X-REAL-IP","X-Forwarded-For","HTTP_X_FORWARDED_FOR","Proxy-Client-IP","WL-Proxy-Client-IP"};
        for(String headerName:headeNames){
        	String ip = get(request,headerName);
        	if(ip!=null){
        		return ip;
        	}
        }
		return request.getRemoteAddr();
    }  
    
    protected static String get(HttpServletRequest request,String headerName){
    	 String ip = request.getHeader(headerName);
    	 if(ip!=null && !ip.isEmpty() && !ip.equals("unknown")){
    		 return ip;
    	 }
    	 
    	 return null;
    }
}
