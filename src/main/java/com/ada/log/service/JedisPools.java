package com.ada.log.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ada.log.util.Dates;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class JedisPools implements InitializingBean{
	
	private final static Log log = LogFactory.getLog(JedisPools.class);
	
	@Value("${redis.host:}")
	private String host;
	@Value("${redis.port:}")
	private String port;
	@Value("${redis.pass:}")
	private String pass;
	
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	
    private  Map<Integer,JedisPool> jedisPools;  /** <星期1-7,JedisPool>,数据库的索引表示星期天（0）-星期（6）的数据, **/
	private Integer defualtDBindex; /** 默认库 **/
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		jedisPools = new HashMap<Integer,JedisPool>();
		JedisPoolConfig config = new JedisPoolConfig();
		for(int dbindex=0;dbindex<7;dbindex++){
			JedisPool jedisPool = new JedisPool(jedisPoolConfig,host, Integer.valueOf(port), 1000, pass, dbindex);
			jedisPools.put(dbindex, jedisPool);
		}
		
		defualtDBindex = new Date().getDay();
	}
	
	/**
	 * 每晚凌晨切库,并且清除前3-5天的库数据,定时清库
	 * 
	 */
	@Scheduled(cron="0 0 0 * * ?")
	public void changeDb(){
		Date today = Dates.todayStart();
		defualtDBindex = new Date().getDay();
		log.info("开始切库,"+"DBindex->"+defualtDBindex);
		
		log.info("开始清库 ");
		for(int i=-3;i>=-5;i--){
			Date lastDay = Dates.add(today, Calendar.DAY_OF_WEEK, -1*i);
			log.info("清库 "+Dates.toString(lastDay, "yyyy-MM-dd")+":"+lastDay.getDay());
			cleanDb(lastDay.getDay());
		}
		log.info("结束清库 ");
	}
	
	public void cleanDb(Integer dbindex){
		Jedis jedis = getResource(dbindex);
		jedis.flushDB();
		returnResource(dbindex,jedis);
		log.info("已清库 DBIndex:"+dbindex);
	}
	
	/**
	 * 获取Jedis实例
	 * @return
	 */
	public Jedis getResource(){
		return jedisPools.get(defualtDBindex).getResource();
	}

	/**
	 * 获取Jedis实例
	 * @return
	 */
	public Jedis getResource(Integer dbindex){
		return jedisPools.get(dbindex).getResource();
	}
	
	public void returnResource(Jedis jedis) {
		jedisPools.get(defualtDBindex).returnResource(jedis);
    }

	public void returnResource(Integer dbindex,Jedis jedis) {
		jedisPools.get(dbindex).returnResource(jedis);
    }
}
