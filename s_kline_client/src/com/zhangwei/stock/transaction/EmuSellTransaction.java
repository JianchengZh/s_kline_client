package com.zhangwei.stock.transaction;

import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.ISell;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.tradesystem.EmuTradeSystem;

public class EmuSellTransaction implements ISell {
	private BuyPoint buypoint;
	private SellPoint sellpoint;
	
	@Override
	public boolean sell(StockInfo stockinfo, BuyPoint buypoint, SellPoint sellpoint) {
		// TODO Auto-generated method stub
		/*sellpoint = new SellPoint(stockinfo, date, 0, price, vol);*/
		this.buypoint = buypoint;
		this.sellpoint = sellpoint;
		EmuTradeSystem.getInstance().submitSellTransaction(this, sellpoint);
		return true;
	}

	@Override
	public boolean cancel() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSucess() {
		// TODO Auto-generated method stub
		EmuTradeSystem.getInstance().completeSellTransaction(buypoint, sellpoint);
		return true;
	}

	@Override
	public boolean onCancel() {
		// TODO Auto-generated method stub
		return false;
	}

}
