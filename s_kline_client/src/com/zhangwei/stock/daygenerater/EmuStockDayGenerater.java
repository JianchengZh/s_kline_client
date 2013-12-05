package com.zhangwei.stock.daygenerater;

import java.util.ArrayList;

import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockManager;


public class EmuStockDayGenerater implements DayGenerater{
	int index;
	ArrayList<Integer> todays;
	boolean status;
	private Stock stock;
	
	public EmuStockDayGenerater(String stock_id, int market_type){
		index = 0;

		this.stock = StockManager.getInstance().getStock(stock_id, market_type);
	
	}
	
	

	@Override
	public int getToday() {
		// TODO Auto-generated method stub

		return stock.getDate(index++);

	}

}
