package com.zhangwei.stock.basic;

import java.util.ArrayList;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;

public interface Condition {
	public boolean checkCondition(StockInfo info, ArrayList<KLineUnit> kl, Point lastPoint);

}
