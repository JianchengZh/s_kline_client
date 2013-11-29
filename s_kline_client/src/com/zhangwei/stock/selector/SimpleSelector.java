package com.zhangwei.stock.selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.zhangwei.stock.Constants;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockInfoManager;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.strategy.BasicStrategy;

public class SimpleSelector implements ISelector {

	@Override
	public Map<String, BuyPoint> getBestBuyPoint(Map<String, BuyPoint> input, BasicStrategy bs) {
		// TODO Auto-generated method stub
		Map<String, BuyPoint> ret = new HashMap<String, BuyPoint>();
		int value_min = Integer.MAX_VALUE;
		BuyPoint theOne = null;
		for(Entry<String, BuyPoint> entry : input.entrySet()){
			BuyPoint bp = entry.getValue();
			Stock stock = StockManager.getInstance().getStock(bp.stock_id, bp.market_type);
			bs.checkBuy(stock.info, stock.getNDayExRightKline(Constants.BUYPOINT_PREFIX_LEN, bp.date), null);
		
			if(value_min>bs.getValue()){
				theOne = bp;
			}
		}

		
		ret.put(theOne.getKey(), theOne);
		return ret;
	}

}
