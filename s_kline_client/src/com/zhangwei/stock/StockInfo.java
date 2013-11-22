package com.zhangwei.stock;

import com.zhangwei.util.DateHelper;


public class StockInfo {
	public String stock_id; //600031
	public boolean index; //false: normal stock   true: index
	public int market_type; //1 普通上证股 2普通深证股 3上证index 4深证index
	
	public int start; //交易日期 左端点 20061013
	public int last; //交易日期 右端点 20131105
	public String name; //utf-8, 股票名称 如ST*昌鱼、 三一重工
	public String quick; //快捷键  STCY
	
	public int lastScanDayAndHour; //2010101314 包括小时
	private int scan;
	
	public StockInfo(String stock_id, int market, String quick, int start, int last, String name, int scan){
		if(market==1 || market==2){
			index = false;
		}else if(market==3 || market==4){
			index = true;
		}else{
			index = false;
		}
		
		this.stock_id = stock_id;
		this.start = start;
		this.market_type = market;
		this.last = last;
		this.name = name;
		this.quick = quick;
		
		this.scan = scan;
		
		
	}

	public String getKey() {
		// TODO Auto-generated method stub
		if(stock_id!=null && market_type>0){
			return stock_id + "." + market_type;
		}else{
			return null;
		}
	}
	
	public int getScanDay(){
		return scan/100;
	}
	
	public boolean outOfDate() {
		// TODO Auto-generated method stub
		if(scan>0){
			if(DateHelper.checkVaildDate(scan/100)){
				if(scan/100<DateHelper.Today()){
					return true;
				}
				
				if(scan%100>=15){
					return false;
				}
				
				if(scan>=DateHelper.Hour()){
					return false;
				}

			}

		}
		return true;
	}
	
	public String getDZHStockID() {
		// TODO Auto-generated method stub
		if(market_type==1||market_type==3){
			return "SH" + stock_id;
		}else if(market_type==2||market_type==4){
			return "SZ" + stock_id;
		}else{
			return null;
		}
	}



}
