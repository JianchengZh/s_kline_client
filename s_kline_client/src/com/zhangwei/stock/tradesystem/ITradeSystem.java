package com.zhangwei.stock.tradesystem;

import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.IBuy;
import com.zhangwei.stock.bs.ISell;
import com.zhangwei.stock.bs.SellPoint;


public interface ITradeSystem {
	public void submitBuyTransaction(IBuy buy, BuyPoint buypoint);

	public void completeBuyTransaction(String stock_id, int market_type, int date, int buy_price, int buy_vol);
	
	public void submitSellTransaction(ISell sell, SellPoint sellpoint, HoldUnit hu);
	
	public void completeSellTransaction(String stock_id, int market_type, int date, int sell_price, int sell_vol);
}
