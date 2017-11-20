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
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        } else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        } else if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("X-real-ip");  
        } else if  (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }else {
        	 ip = request.getRemoteAddr();  
        }
        return ip;  
    }  
}
