package com.zhangwei.stock.transaction;

import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.IBuy;
import com.zhangwei.stock.tradesystem.EmuTradeSystem;

public class EmuBuyTransaction implements IBuy {
	private BuyPoint buypoint;
	
/*	public void buy(StockInfo info, KLineUnit kLineUnit, int vol) {
		// TODO Auto-generated method stub
		buy(info, kLineUnit.date, 0, kLineUnit.close, vol);
	}*/

	@Override
	public boolean buy(StockInfo stockinfo, BuyPoint buypoint) {
		// TODO Auto-generated method stub
		this.buypoint = buypoint;/*new BuyPoint(stockinfo, date, 0, price, vol);*/
		EmuTradeSystem.getInstance().submitBuyTransaction(this, buypoint);
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
