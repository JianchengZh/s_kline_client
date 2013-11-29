package com.zhangwei.stock.bs;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;

public class Point {
	public String BSID;
	
	public String stock_id;
	
	public int market_type;
	
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
	 * 时间
	 * */
	public int time; //231034 -> 23:10:34
	
	public int vol; //100 100股
	
	/**
	 *  stock info 
	 * */
	//public StockInfo info;
	
	public KLineUnit last;
	
	public Point(String BSID, String stock_id, int market_type,/*StockInfo info,*/ int date, int time, int price, int vol, boolean buyFlag, KLineUnit last){
		this.BSID = BSID;
		//this.info = info;
		this.stock_id = stock_id;
		this.market_type = market_type;
		this.date = date;
		this.price = price;
		this.buyFlag = buyFlag;
		this.time = time;
		this.vol = vol;
		this.last = last;
	}
	
	public int getPrice(){
		return price;
	}
	
	public int getDate(){
		return date;
	}
	
/*	public StockInfo getStockInfo(){
		return info;
	}*/

	public boolean isBuy(){
		return buyFlag;
	}
	
	public String getKey() {
		// TODO Auto-generated method stub
		if(stock_id!=null && market_type>0){
			return stock_id + "." + market_type;
		}else{
			return null;
		}
	}

}
