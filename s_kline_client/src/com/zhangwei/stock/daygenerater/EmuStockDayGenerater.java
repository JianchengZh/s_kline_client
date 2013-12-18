package com.zhangwei.stock.daygenerater;

import java.util.ArrayList;

import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockManager;


public class EmuStockDayGenerater implements DayGenerater{
	int index;
	ArrayList<Integer> todays;
	boolean status;
	private Stock stock;
	
	public EmuStockDayGenerater(Stock s){
		index = 0;

		this.stock = s;
	
	}
	
	

	@Override
	public int getToday() {
		// TODO Auto-generated method stub

		return stock.getDate(index++);

	}

}
