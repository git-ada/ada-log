package com.ada.log.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 简单 Date 相关静态工具
 * 
 * @author wanghl.cn
 */
public class Dates {

	public static final String CHINESE_DATE_FORMAT_LINE = "yyyy-MM-dd";
	public static final String CHINESE_DATETIME_FORMAT_LINE = "yyyy-MM-dd HH:mm:ss";
	public static final String CHINESE_DATE_FORMAT_SLASH = "yyyy/MM/dd";
	public static final String CHINESE_DATETIME_FORMAT_SLASH = "yyyy/MM/dd HH:mm:ss";
	public static final String DATETIME_NOT_SEPARATOR="yyyyMMddHHmmssSSS";

	private static final String[] SUPPORT_ALL_FORMATS = new String[]{CHINESE_DATE_FORMAT_LINE,
			CHINESE_DATETIME_FORMAT_LINE, CHINESE_DATE_FORMAT_SLASH, CHINESE_DATETIME_FORMAT_SLASH};

	private static final String DEFAULT_DATE_FORMAT = CHINESE_DATETIME_FORMAT_LINE;
	private static final SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	private static final SimpleDateFormat sdf1 = new SimpleDateFormat(CHINESE_DATE_FORMAT_LINE);
	private static final SimpleDateFormat[] sdfs = new SimpleDateFormat[]{sdf,sdf1};
	
	
	private static DateFormat df = null;
	private final static Calendar c = Calendar.getInstance();
	
	public final static String HH_MM_SS = "HH:mm:ss";
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String  YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public final static String  YYYY_MM_DD_HH_MM_SS_MI = "yyyy-MM-dd HH:mm:ss.S";
	
	public final static String HHMMSS = "HHmmss";
	public final static String 	YYYYMMDD = "yyyyMMdd";
	public final static String YYYYMMDDHHMMSS = "yyyyMMdd-HHmmss";
	

	public static String format(Date date, String pattern) {
		sdf.applyPattern(pattern);
		return sdf.format(date);
	}

	public static String format(Date date) {
		return format(date, DEFAULT_DATE_FORMAT);
	}

	public static String format(String pattern) {
		return format(new Date(), pattern);
	}

	public static Date parse(String dateString, String pattern) {
		sdf.applyPattern(pattern);
		try {
			return sdf.parse(dateString);
		} catch (Exception e) {
			throw new RuntimeException("parse String[" + dateString + "] to Date faulure with pattern[" + pattern
					+ "].");
		}
	}

	public static Date parse(String dateString, String[] patterns) {
//		for (String pattern : patterns) {
//			if (StringUtils.isBlank(pattern)) {
//				continue;
//			}
//			sdf.applyPattern(pattern);
//			try {
//				return sdf.parse(dateString);
//			} catch (Exception e) {
//				// ignore exception
//				continue;
//			}
//		}
		
		for(SimpleDateFormat sdf:sdfs){
			try {
				return sdf.parse(dateString);
			} catch (Exception e) {
				continue;
			}
		}
		throw new UnsupportedOperationException("Parse String[" + dateString + "] to Date faulure with patterns["
				+ Arrays.toString(patterns) + "]");

	}

	public static Date parse(String dateString) {
		return parse(dateString, SUPPORT_ALL_FORMATS);
	}

	public static Date addDay(Date date) {
		long oneDayMillisecond = 24 * 60 * 60 * 1000;
		return addDate(date, oneDayMillisecond);
	}

	public static Date minusDay(Date date) {
		long oneDayMillisecond = 24 * 60 * 60 * 1000;
		return addDate(date, -oneDayMillisecond);
	}

	public static Date addDate(Date date, long millisecond) {
		return new Date(date.getTime() + millisecond);
	}
	
	/**
	 * date to string with format
	 * @param d
	 * @param format
	 * @return
	 */
	public static String toString(Date d,String format){
		if (d != null){
			df = new SimpleDateFormat(format);
			return df.format(d);
		}else{
			return null;
		}
	}
	
	/**
	 * @param time
	 * @param format
	 * @return
	 */
	public static String toString(long time,String format){
		Timestamp t = new Timestamp(time);
		return toString(t,format);
	}
	/**
	 * @param t
	 * @param format
	 * @return
	 */
	public static String toString(Timestamp t,String format){
		if (t != null){
			df = new SimpleDateFormat(format);
			return df.format((Date)t);
		}else{
			return null;
		}
	}
	/**
	 * @return
	 */
	public static String today(){
		return toString(new Date(),YYYY_MM_DD);
	}
	
	/**
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Timestamp toTimestamp(String dateStr,String format) throws IllegalArgumentException{
		if (dateStr != null){
			df = new SimpleDateFormat(format);
			try {
				Date d=df.parse(dateStr);
				if (d != null){
					return new Timestamp(d.getTime());
				}
			} catch (ParseException e) {
				throw new IllegalArgumentException(":::[toTimestamp]parse ["+dateStr+"] error, format["+format+"]"); 
			}
		}		
		return null;
	}
	
	/**
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static long toLong(String dateStr,String format){
		Timestamp t = toTimestamp(dateStr,format);
		if (t != null){
			return t.getTime();
		}else{
			return 0l;
		}
	}
	
	/**
	 * @return
	 */
	public static Timestamp now(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	public static Date yestoday(){
		synchronized (c) {
			//-----------------------------------
			c.add(Calendar.DAY_OF_YEAR, -1);
			//-----------------------------------
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return new Timestamp(c.getTimeInMillis());
		}
	}
	
	/**
	 * @param t
	 * @return
	 */
	public static int getMinute(Timestamp t){
		if (t == null){
			return 0;
		}
		synchronized (c) {
			c.setTime(t);
			return c.get(Calendar.MINUTE);
		}
	}
	
	/**
	 * @param t
	 * @return
	 */
	public static int getHour24(Timestamp t){
		if (t == null){
			return 0;
		}
		synchronized (c) {
			c.setTime(t);
			return c.get(Calendar.HOUR_OF_DAY);
		}
	}	
	
	/**
	 * 2010.9.3 11:00:00
	 * @return
	 */
	public static Timestamp hourStart(){
		return hourStart(new Date(),0);
	}
	

	/**
	 * 2010.9.3 11:59:59
	 * @return
	 */
	public static Timestamp hourEnd(){
		return hourEnd(new Date(),0);
	}
	
	/**
	 * @param date
	 * @param hoursOffset
	 * @return
	 */
	public static Timestamp hourEnd(Date date,int hoursOffset){
		synchronized (c) {
			c.setTime(date);
			//---------------------------------------
			c.add(Calendar.HOUR, hoursOffset);
			//---------------------------------------
			c.add(Calendar.HOUR_OF_DAY, 1);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.SECOND, -1);
			return new Timestamp(c.getTimeInMillis());
		}
	}	
	/**
	 * @param date
	 * @param hoursOffset
	 * @return
	 */
	public static Timestamp hourStart(Date date,int hoursOffset){
		synchronized (c) {
			c.setTime(date);
			//---------------------------------------
			c.add(Calendar.HOUR, hoursOffset);
			//---------------------------------------
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return new Timestamp(c.getTimeInMillis());
		}
	}		
	/**
	 * @param date
	 * @return
	 */
	public static Timestamp dayStart(Date date){
		return dayStart(date,0);
	}
	
	/**
	 * @param date
	 * @param dayOffset
	 * @return
	 */
	public static Timestamp dayStart(Date date,int dayOffset){
		synchronized (c) {
			c.setTime(date);
			//---------------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			//---------------------------------------
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return new Timestamp(c.getTimeInMillis());
		}
	}	

	/**
	 * @param date
	 * @return
	 */
	public static Timestamp dayEnd(Date date,int dayOffset){
		synchronized (c) {
			c.setTime(date);
			//---------------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			//---------------------------------------
			c.add(Calendar.DAY_OF_YEAR, 1);
			c.set(Calendar.HOUR_OF_DAY,0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.SECOND, -1);
			return new Timestamp(c.getTimeInMillis());
		}
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static Timestamp dayEnd(Date date){
		return dayEnd(date,0);
	}	
	/**
	 * 2010.9.3 00:00:00
	 * @return
	 */
	public static Timestamp todayStart(){
		return dayStart(new Date());
	}
	
	/**
	 * 2010.9.3 23:59:59
	 * @return
	 */
	public static Timestamp todayEnd(){
		return dayEnd(new Date());
	}
	
	
	/**
	 * @param date
	 * @param dayOffset
	 * @return
	 */
	public static Timestamp monthStart(Date date){
		return monthStart(date,0);
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static Timestamp monthStart(Date date,int dayOffset){
		synchronized (c) {
			c.setTime(date);
			//-----------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			//-----------------------------------
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return new Timestamp(c.getTimeInMillis());
		}
	}
	
	/**
	 * @param date
	 * @param dayOffset
	 * @return
	 */
	public static Timestamp monthEnd(Date date){
		return monthEnd(date,0);
	}
	
	/**
	 * 2010.10.3 -> 2010.11.1 -> 2010.10.31
	 * @param date
	 * @return
	 */
	public static Timestamp monthEnd(Date date,int dayOffset){
		synchronized (c) {
			c.setTime(date);
			//-----------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			//-----------------------------------
			c.add(Calendar.MONTH, 1);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.SECOND, -1);
			//------------------------------------
			
			//------------------------------------
			return new Timestamp(c.getTimeInMillis());
		}
	}	
	/**
	 * 2010.9.1 00:00:00
	 * @return
	 */
	public static Timestamp monthStart(){
		return monthStart(new Date());
	}
	
	/**
	 * 2010.9.30 23:59:59
	 * @return
	 */
	public static Timestamp monthEnd(){
		return monthEnd(new Date());
	}		
	
	/**
	 * @param date
	 * @return
	 */
	public static Timestamp yearStart(Date date){
		return yearStart(date,0);
	}
	/**
	 * @param date
	 * @return
	 */
	public static Timestamp yearStart(Date date,int dayOffset){
		synchronized (c) {
			c.setTime(date);
			//-----------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			//-----------------------------------
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return new Timestamp(c.getTimeInMillis());
		}
	
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static Timestamp yearEnd(Date date){
		return yearEnd(date,0);
	}
	/**
	 * @param date
	 * @param dayOffset
	 * @return
	 */
	public static Timestamp yearEnd(Date date,int dayOffset){
		synchronized (c) {
			c.setTime(date);
			//-----------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			//-----------------------------------
			c.add(Calendar.YEAR, 1);
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.SECOND, -1);
			return new Timestamp(c.getTimeInMillis());
		}
	
	}
	/**
	 * 
	 * @return
	 */
	public static Timestamp yearStart(){
		return yearStart(new Date());
	}
	
	/**
	 * @return
	 */
	public static Timestamp yearEnd(){
		return yearEnd(new Date());
	}
	
	/**
	 * @param timestamp
	 * @param field
	 * @param value
	 * @return
	 */
	public static Date add(Date timestamp,int field, int value){
		synchronized (c) {
			c.setTimeInMillis(timestamp.getTime());
			c.add(field, value);
			return new Date(c.getTimeInMillis());
		}
		
	}
	
	
	
	/**
	 * @param timestamp
	 * @param field
	 * @return
	 */
	public static int get(Date timestamp,int field){
		synchronized (c) {
			c.setTimeInMillis(timestamp.getTime());
			return c.get(field);
		}
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonthDays(Date date){
		//1st of next month -1s
		int dayOffset = 0;
		synchronized (c) {
			c.setTime(date);
			//-----------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			//-----------------------------------
			c.add(Calendar.MONTH, 1);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.SECOND, -1);
			//------------------------------------
			
			return c.get(Calendar.DAY_OF_MONTH);
		}		
	}

	/**
	 * @param serverTime
	 * @param serverZT
	 * @param clientZT
	 * @return
	 */
	public static long getClientTime(long serverTime,TimeZone serverZT,TimeZone clientZT){
		return serverTime+clientZT.getRawOffset()-serverZT.getRawOffset();
	}
	
	/**
	 * 1 - sunday
	 * 2 - monday
	 * ..
	 * @param date
	 * @return
	 */
	public static int getWeekDay(Date date){
		synchronized (c) {
			c.setTime(date);
			return c.get(Calendar.DAY_OF_WEEK);
		}
	}
	
	/**
	 * 
	 * @param milliseconds
	 * @return xx天xx小时xx分钟xx秒
	 */
	public static String toTimeString(Long milliseconds){
			if(milliseconds < 1000l){
				return "0";
			}
		
		   long day=milliseconds/(24*60*60*1000);
		   long hour=(milliseconds/(60*60*1000)-day*24);
		   long min=((milliseconds/(60*1000))-day*24*60-hour*60);
		   long s=(milliseconds/1000-day*24*60*60-hour*60*60-min*60);
		   
		   StringBuffer sb = new StringBuffer();
		   if(day >0){
			   sb.append(day);
			   sb.append("天");
		   }
		   if(hour > 0 || (day>0 && (min > 0 || s > 0))){
			   sb.append(hour);
			   sb.append("小时");
		   }
		   if(min > 0 || ((day>0 || hour>0) && s > 0)){
			   sb.append(min);
			   sb.append("分");
		   }
		   if(s > 0){
			   sb.append(s);
			   sb.append("秒");
		   }
		return sb.toString();
	}
	

	public static void main(String[] args) {
		Date current = parse("2012-12-31");
		System.out.println(format(current));
		System.out.println(format(addDay(current)));
	}

}
