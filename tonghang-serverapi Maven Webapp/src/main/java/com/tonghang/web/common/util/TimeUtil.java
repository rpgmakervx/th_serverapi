package com.tonghang.web.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtil {
	public static String getFormatStringTime(String datetime){
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime dt = dtf.parseDateTime(datetime);
		return dt.toString(dtf);
	}
	
	public static Date getFormatDate(String datetime){
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime dt = dtf.parseDateTime(datetime);
		return dt.toDate();
	}
	
	public static Date getFormatShortDate(String datetime){
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime dt = dtf.parseDateTime(datetime);
		return dt.toDate();
	}
	public static String getFormatString(Date date) {
		 DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 return format.format(date);
	}
	
	public static String getFormatStringDate(Date date) {
		 DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 return format.format(date);
	}
	
	public static Date plusHour(int hours,Date datetime){
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime dt = dtf.parseDateTime(getFormatString(datetime));
		dt.plusHours(hours);
		return dt.toDate();
	}
	
	public static Date plusDate(int days,Date datetime){
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime dt = dtf.parseDateTime(getFormatString(datetime));
		dt = dt.plusDays(days);
		return dt.toDate();
	}
	public static Date plusMonth(int months,Date datetime){
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime dt = dtf.parseDateTime(getFormatStringDate(datetime));
		dt = dt.plusMonths(months);
		return dt.toDate();
	}
	//d2-d1
	public static int dateGap(Date d1,Date d2){
		DateTime dt1 = new DateTime(d1);
  		DateTime dt2 = new DateTime(d2);
  		return Days.daysBetween(dt1, dt2).getDays();
	}
}
