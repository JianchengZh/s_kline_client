package com.zhangwei.stock.BS;


public interface TradeMethod {
	public void submitBuyTransaction(IBuy buy, BuyPoint buypoint);

	public void completeBuyTransaction(BuyPoint buypoint);
	
	public void submitSellTransaction(ISell sell, SellPoint Sellpoint);
	
	public void completeSellTransaction(BuyPoint buypoint, SellPoint Sellpoint);
}
