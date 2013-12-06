package com.zhangwei.stock.bs;

import com.zhangwei.stock.StockInfo;

public interface ISell {
	public boolean sell(HoldUnit hu);
	
	public boolean sell(SellPoint sellpoint, HoldUnit hu);
	
	public boolean sellCancel(SellPoint sellpoint, HoldUnit hu);
	
	public boolean onSellSucess(SellPoint sellpoint, HoldUnit hu);
	
	public boolean onSellCancel(SellPoint sellpoint, HoldUnit hu);
}
