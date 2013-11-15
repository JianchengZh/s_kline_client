package com.zhangwei.stock.BS;

import com.zhangwei.stock.StockInfo;

public interface IBuy {
	public boolean buy(StockInfo stockinfo, int date, int time, int price, int vol);
	
	public boolean cancel();
	
	public boolean onSucess();
	
	public boolean onCancel();

}
