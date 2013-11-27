package com.zhangwei.stock.manager;

public interface DayGenerater {

	/**
	 * 获取合法的日期
	 * <br>1. 对于real系统而言，是指交易开盘收盘的日期，每次调用根据实际日期返回，若<0则今天不交易，若为0表示还没开盘
	 * <br>2. 对于emu系统而言，是根据不同的Strategy得到的日期序列，若<0则表示没有后续日期
	 * */
	public int getToday();
	
}
