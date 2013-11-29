package com.zhangwei.stock.bs;

import com.zhangwei.stock.StockInfo;

public interface IBuy {
	public boolean buy(BuyPoint buypoint);
	
	public boolean buyCancel(BuyPoint buypoint);
	
	public boolean onBuySucess(BuyPoint buypoint);
	
	public boolean onBuyCancel(BuyPoint buypoint);

}
