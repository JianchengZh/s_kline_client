package com.zhangwei.stock.tradesystem;

import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.IBuy;
import com.zhangwei.stock.bs.ISell;
import com.zhangwei.stock.bs.SellPoint;


public interface ITradeSystem {
	public void submitBuyTransaction(IBuy buy, BuyPoint buypoint);

	public void completeBuyTransaction(BuyPoint buypoint);
	
	public void submitSellTransaction(ISell sell, SellPoint sellpoint, HoldUnit hu);
	
	public void completeSellTransaction(SellPoint sellpoint, HoldUnit hu);
}
