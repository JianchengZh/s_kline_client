package com.zhangwei.stock;


public class StockInfo {
	public String stock_id; //600031
	public boolean index; //false: normal stock   true: index
	public int market_type; //1 普通上证股 2普通深证股 3上证index 4深证index
	
	public int start; //交易日期 左端点 20061013
	public int last; //交易日期 右端点 20131105
	public String name; //utf-8, 股票名称 如ST*昌鱼、 三一重工
	public String quick; //快捷键  STCY
	
	public StockInfo(String stock_id, int market, String quick, int start, int last, String name){
		if(market==1 || market==2){
			index = false;
		}else if(market==3 || market==4){
			index = true;
		}else{
			index = false;
		}
		
		this.start = start;
		this.last = last;
		this.name = name;
		this.quick = quick;
		
		
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
