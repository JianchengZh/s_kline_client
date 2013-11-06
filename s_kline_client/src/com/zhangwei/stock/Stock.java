package com.zhangwei.stock;

public class Stock {
	
	public StockInfo info;
	public Kline line;
	
	public  Stock(StockInfo info){
		this.info = info;
	}
	
	public String getKey(){
		if(info!=null){
			return info.getKey();
		}else{
			return null;
		}

	}

	public boolean outOfDate() {
		// TODO Auto-generated method stub
		if(line!=null){
			return line.outOfDate();
		}
		return true;
	}

}
