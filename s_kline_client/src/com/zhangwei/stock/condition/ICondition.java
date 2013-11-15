package com.zhangwei.stock.condition;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.basic.StockException;

public interface ICondition {
	public boolean checkCondition(StockInfo info, List<KLineUnit> kl, Point lastPoint) throws StockException;

}
