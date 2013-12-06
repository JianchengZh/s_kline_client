package com.zhangwei.stock.bs;

public interface ISellCallBack {
	public boolean onSellSucess(SellPoint sellpoint, HoldUnit hu);
	
	public boolean onSellCancel(SellPoint sellpoint, HoldUnit hu);
}
