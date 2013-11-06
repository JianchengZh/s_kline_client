package com.zhangwei.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	public static int Today(){
		Date d = new Date();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
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
	
	public static void main(String[] args){
		System.out.println(Yesteray());
	}
}
