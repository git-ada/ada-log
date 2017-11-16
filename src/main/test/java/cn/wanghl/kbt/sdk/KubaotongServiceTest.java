package cn.wanghl.kbt.sdk;

import org.junit.Test;

import cn.wanghl.jsd.mobile.service.KubaotongService;
import cn.wanghl.jsd.mobile.service.impl.KubaotongServiceImpl;

public class KubaotongServiceTest{
	
	private static KubaotongService instance;
	
	public static KubaotongService getInstance(){
		if(instance==null){
			instance = new KubaotongServiceImpl();
		}
		return instance;
	}

	@Test
	public void getBalance()  {
		System.out.println(getInstance().getBalance());
	}

	@Test
	public void recharge() {

	}

	@Test
	public void getResult(){
	}

}
