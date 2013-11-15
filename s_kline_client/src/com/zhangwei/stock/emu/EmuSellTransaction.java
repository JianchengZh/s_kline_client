package com.zhangwei.stock.emu;

import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.ISell;
import com.zhangwei.stock.BS.SellPoint;

public class EmuSellTransaction implements ISell {
	private SellPoint sellpoint;
	
	@Override
	public boolean sell(StockInfo stockinfo, int date, int time, int price,
			int vol) {
		// TODO Auto-generated method stub
		sellpoint = new SellPoint(stockinfo, date, 0, price, vol);
		EmuTradeSystem.getInstance().submitSellTransaction(this);
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
		EmuTradeSystem.getInstance().completeSellTransaction(sellpoint);
		return true;
	}

	@Override
	public boolean onCancel() {
		// TODO Auto-generated method stub
		return false;
	}

}
