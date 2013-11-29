package com.zhangwei.stock.selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.strategy.BasicStrategy;

public interface ISelector {
	public Map<String, BuyPoint> getBestBuyPoint(Map<String, BuyPoint> input, BasicStrategy bs);

}
