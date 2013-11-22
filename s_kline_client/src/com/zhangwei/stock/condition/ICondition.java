package com.zhangwei.stock.condition;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.kline.KLineTypeResult;

public interface ICondition {
	public boolean checkCondition(StockInfo info, KLineTypeResult rlt, List<KLineUnit> kl, Point lastPoint) throws StockException;

}
