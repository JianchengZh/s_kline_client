package com.zhangwei.stock.BS;

import com.zhangwei.stock.Constants;

public class TradeUnit {
	public String stock_id;
	public int market_type;
	public int buy_date;
	public int sell_date;
	public int buy_price;
	public int sell_price;
	public int vol;
	
	public TradeUnit(String stock_id, int market, int buy_date,
			int sell_date, int buy_price, int sell_price, int vol) {
		// TODO Auto-generated constructor stub
		this.stock_id = stock_id;
		this.market_type = market;
		this.buy_date = buy_date;
		this.sell_date = sell_date;
		this.buy_price = buy_price;
		this.sell_price = sell_price;
		this.vol = vol;
	}
	
	public int getEarnPercent(){
		if(sell_price>0 && buy_price>0){
			return (sell_price - buy_price)*100/buy_price - Constants.YONGJIN_PERCENT;
		}else{
			return 0;
		}
	}
	
	

}
