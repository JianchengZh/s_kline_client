package com.zhangwei.stock.bs;

import com.zhangwei.stock.StockInfo;

public interface ISell {
	public boolean sell(StockInfo stockinfo, BuyPoint buypoint, SellPoint sellpoint);
	
	public boolean cancel();
	
	public boolean onSucess();
	
	public boolean onCancel();
}
