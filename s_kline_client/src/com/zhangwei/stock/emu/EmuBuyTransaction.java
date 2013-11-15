package com.zhangwei.stock.emu;

import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.IBuy;

public class EmuBuyTransaction implements IBuy {
	private BuyPoint buypoint;

	@Override
	public boolean buy(StockInfo stockinfo, int date, int time, int price,
			int vol) {
		// TODO Auto-generated method stub
		buypoint = new BuyPoint(stockinfo, date, 0, price, vol);
		EmuTradeSystem.getInstance().submitBuyTransaction(this);
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
		EmuTradeSystem.getInstance().completeBuyTransaction(buypoint);
		return true;
	}

	@Override
	public boolean onCancel() {
		// TODO Auto-generated method stub
		return false;
	}

}
