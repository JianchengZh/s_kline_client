package com.zhangwei.stock.task;

import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.SellPoint;

public interface ITaskSellResultCheck {
	public void check(HoldUnit hu);

}
