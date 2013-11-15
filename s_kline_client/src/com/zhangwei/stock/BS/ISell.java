package com.zhangwei.stock.BS;

import com.zhangwei.stock.StockInfo;

public interface ISell {
	public boolean sell(StockInfo stockinfo, int date, int time, int price, int vol);
	
	public boolean cancel();
	
	public boolean onSucess();
	
	public boolean onCancel();
}
