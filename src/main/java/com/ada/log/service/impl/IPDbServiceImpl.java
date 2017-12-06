package com.ada.log.service.impl;

import org.springframework.stereotype.Service;

import com.ada.log.service.IPDBService;
import com.ada.log.util.IpSearchNewUtil;

@Service
public class IPDbServiceImpl implements IPDBService {
	
	@Override
	public String getRegion(String ipAddress) {
		return IpSearchNewUtil.getIPAddress(ipAddress);
	}
	
	public static void main(String[] args)  throws Exception{
		IPDbServiceImpl impl = new IPDbServiceImpl();
		Long startTime = System.currentTimeMillis();
		
		System.out.println(impl.getRegion("116.62.195.28"));
		
		Long endTime = System.currentTimeMillis();
		System.out.println(endTime-startTime);
	}
}
