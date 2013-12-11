package com.zhangwei.stock.bs;

public interface IBuyCallBack {
	public boolean onBuySucess(String stock_id, int market_type, int date,
			int buy_price, int buy_vol);
	
	public boolean onBuyCancel(String stock_id, int market_type, int date,
			int buy_price, int buy_vol);
}
