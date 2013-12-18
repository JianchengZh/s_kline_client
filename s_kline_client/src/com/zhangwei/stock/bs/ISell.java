package com.zhangwei.stock.bs;

import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;

public interface ISell {
	public boolean sell(Stock stock, HoldUnit hu);
	
/*	public boolean sell(SellPoint sellpoint, HoldUnit hu);*/
	
	public boolean sellCancel(SellPoint sellpoint, HoldUnit hu);
	

}
