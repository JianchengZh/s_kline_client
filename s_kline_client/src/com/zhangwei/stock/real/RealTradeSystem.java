package com.zhangwei.stock.real;

import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.IBuy;
import com.zhangwei.stock.bs.ISell;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.tradesystem.ITradeSystem;

public class RealTradeSystem implements ITradeSystem {

	@Override
	public void submitBuyTransaction(IBuy buy, BuyPoint buypoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void completeBuyTransaction(BuyPoint buypoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void submitSellTransaction(ISell sell, SellPoint Sellpoint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void completeSellTransaction(BuyPoint buypoint, SellPoint Sellpoint) {
		// TODO Auto-generated method stub

	}

}
