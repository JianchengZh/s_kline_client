package com.zhangwei.stock.bs;

public interface ISellCallBack {
	public boolean onSellSucess(String stock_id, int market_type, int buy_date, int sell_date, int buy_price, int sell_price, int sell_vol);
	
	public boolean onSellCancel(String stock_id, int market_type, int date, int sell_price, int sell_vol);
}
