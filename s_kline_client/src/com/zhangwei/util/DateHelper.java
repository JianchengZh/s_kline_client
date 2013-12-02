package com.zhangwei.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	public static int Hour(){
		Date d = new Date();
		SimpleDateFormat df=new SimpleDateFormat("HH");
		String d_str = df.format(d);
		
		return Integer.valueOf(d_str);

	}
	
	public static int Today(){
		Date d = new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String d_str = df.format(d);
		
		return Integer.valueOf(d_str);

	}
	
	public static int TodayHour(){
		Date d = new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHH");
		String d_str = df.format(d);
		
		return Integer.valueOf(d_str);

	}

	
	public static int Yesteray(){
		Date d = new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, -1);
		Date date = cal.getTime();

		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		String d_str = df.format(date);
		
		return Integer.valueOf(d_str);

	}
	
	public static boolean checkVaildDate(int day){
		if(day>19000000){
			return true;
		}else{
			return false;
		}
	}
	
	public static void main(String[] args){
		System.out.println(Yesteray());
	}

	public static String Day(int date) {
		// TODO Auto-generated method stub
		return String.valueOf(date%10000);
	}

	public static int Week() {
		// TODO Auto-generated method stub
		Date d = new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		Date date = cal.getTime();

		SimpleDateFormat df=new SimpleDateFormat("u");
		String d_str = df.format(date);
		
		return Integer.valueOf(d_str);
	}
}
