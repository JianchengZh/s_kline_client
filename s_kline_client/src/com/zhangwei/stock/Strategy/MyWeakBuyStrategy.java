package com.zhangwei.stock.Strategy;

import com.zhangwei.stock.condition.ContinuousPercentCondition;
import com.zhangwei.stock.condition.DetectBigChangeCondition;
import com.zhangwei.stock.condition.KUpDownUpCondition;
import com.zhangwei.stock.condition.LastNdayCondition;
import com.zhangwei.stock.condition.StopEarnCondition;
import com.zhangwei.stock.condition.StopLossCondition;
import com.zhangwei.stock.condition.VolumeChangeCondition;

public class MyWeakBuyStrategy extends BasicStrategy {
	
	/**
	 * 
	 */
	private static final long MyWeakBuyStrategyUID = 7287298140871L;

	public MyWeakBuyStrategy() {
		super(MyWeakBuyStrategyUID);
		// TODO Auto-generated constructor stub
	}
	
	public void init(String MarketID) {
		// TODO Auto-generated method stub
		super.init(MarketID);
		//addBuyBigCondition(new DetectBigChangeCondition(-7, 1, 150, false), false);
		addBuyBigCondition(new DetectBigChangeCondition(-5, 1, -1, true), false);
		addBuyLittleCondition(new ContinuousPercentCondition(5, -15));
		addBuyLittleCondition(new VolumeChangeCondition(3, 50));
		
		addSellBigCondition(new StopLossCondition(8), true);
		addSellBigCondition(new LastNdayCondition(5), true);
		addSellBigCondition(new StopEarnCondition(20), true);
	}

}
