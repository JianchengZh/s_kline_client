package com.zhangwei.stock.BS;

import com.zhangwei.stock.StockInfo;

public class BuyPoint extends Point {


	public BuyPoint(StockInfo info, int date, int time, int price, int vol) {
		super(info, date, time, price, vol, true);
		// TODO Auto-generated constructor stub

	}
	
	public String toString(){
		return "买入：" + info.stock_id + " " + info.name + ", 价格(元)：" + price*1.0/100 + ", 数量（手）:" + vol/100;  
	}
	

}
