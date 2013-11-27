package com.zhangwei.stock.bs;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;

public class BuyPoint extends Point {


	public BuyPoint(String BSID, StockInfo info, int date, int time, int price, int vol, KLineUnit last) {
		super(BSID, info, date, time, price, vol, true, last);
		// TODO Auto-generated constructor stub

	}
	
	public String toString(){
		return "买入：" + date + " " + info.stock_id + " " + info.name + ", 价格(元)：" + price*1.0/100 + ", 数量（手）:" + vol/100;  
	}
	

}
