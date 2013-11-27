package com.zhangwei.stock.daygenerater;

import com.zhangwei.util.DateHelper;

public class RealTodayGenerater implements DayGenerater {

	@Override
	public int getToday() {
		// TODO Auto-generated method stub
		if(DateHelper.Week()==6 || DateHelper.Week()==7){
			return -1;
		}
		
		//todo: 从DZH中获知是否休市
		
		
		return DateHelper.Today();
	}

}
