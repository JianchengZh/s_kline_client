package com.zhangwei.stock.basic;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;

public interface Condition {
	public boolean checkCondition(StockInfo info, List<KLineUnit> kl, Point lastPoint) throws StockException;

}
