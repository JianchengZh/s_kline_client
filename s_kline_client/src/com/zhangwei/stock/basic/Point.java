package com.zhangwei.stock.basic;

import com.zhangwei.stock.StockInfo;

public class Point {
	public boolean buyFlag = true;
	
	/**
	 *  价格， 单位 分
	 */
	public int price; 
	
	/**
	 *  日期， 格式20131114
	 * */
	public int date;
	
	/**
	 *  stock info 
	 * */
	public StockInfo info;
	
	public Point(StockInfo info, int date, int price, boolean buyFlag){
		this.info = info;
		this.date = date;
		this.price = price;
		this.buyFlag = buyFlag;
	}
	
	public int getPrice(){
		return price;
	}
	
	public int getDate(){
		return date;
	}
	
	public StockInfo getStockInfo(){
		return info;
	}

	public boolean isBuy(){
		return buyFlag;
	}

}
