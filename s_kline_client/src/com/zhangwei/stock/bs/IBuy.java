package com.zhangwei.stock.bs;

import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;

public interface IBuy {
	public boolean buy(Stock stock, BuyPoint buypoint);
	
	public boolean buyCancel(BuyPoint buypoint);

}
