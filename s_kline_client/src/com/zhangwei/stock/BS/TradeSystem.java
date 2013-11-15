package com.zhangwei.stock.BS;


public interface TradeSystem {
	public void submitBuyTransaction(IBuy buy);

	public void completeBuyTransaction(BuyPoint buypoint);
	
	public void submitSellTransaction(ISell sell);
	
	public void completeSellTransaction(SellPoint Sellpoint);
}
