package com.zhangwei.stock.BS;

import com.zhangwei.stock.StockInfo;

public interface IBuy {
	public boolean buy(StockInfo stockinfo, BuyPoint buypoint);
	
	public boolean cancel();
	
	public boolean onSucess();
	
	public boolean onCancel();

}
