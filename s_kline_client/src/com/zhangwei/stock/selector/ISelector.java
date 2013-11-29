package com.zhangwei.stock.selector;

import java.util.ArrayList;
import java.util.HashMap;

import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.strategy.BasicStrategy;

public interface ISelector {
	public ArrayList<BuyPoint> getBestBuyPoint(HashMap<String, BuyPoint> input, BasicStrategy bs);

}
