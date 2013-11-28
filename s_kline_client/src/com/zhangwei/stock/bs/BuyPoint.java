package com.zhangwei.stock.bs;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;

public class BuyPoint extends Point {


	public BuyPoint(String BSID, String stock_id, int market_type, /*StockInfo info,*/ int date, int time, int price, int vol, KLineUnit last) {
		super(BSID, stock_id, market_type, /*info,*/ date, time, price, vol, true, last);
		// TODO Auto-generated constructor stub

	}
	

	public BuyPoint(String BSID, HoldUnit hu, KLineUnit last) {
		// TODO Auto-generated constructor stub
		super(BSID, hu.stock_id, hu.market_type, hu.buy_date, 0, hu.buy_price, hu.buy_vol, true, last);
	}


	public String toString(){
		return "买入：" + date + " " + stock_id /*+ info.stock_id + " " + info.name*/ + ", 价格(元)：" + price*1.0/100 + ", 数量（手）:" + vol/100;  
	}
	

}
