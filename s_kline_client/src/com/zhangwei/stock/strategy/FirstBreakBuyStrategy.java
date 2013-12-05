package com.zhangwei.stock.strategy;

import com.zhangwei.stock.condition.ContinuousPercentCondition;
import com.zhangwei.stock.condition.DetectBigChangeCondition;
import com.zhangwei.stock.condition.KUpDownUpCondition;
import com.zhangwei.stock.condition.LastNdayCondition;
import com.zhangwei.stock.condition.NewHigherORLowerCondition;
import com.zhangwei.stock.condition.StopEarnCondition;
import com.zhangwei.stock.condition.StopLossCondition;
import com.zhangwei.stock.condition.VolumeChangeCondition;

public class FirstBreakBuyStrategy extends BasicStrategy {
	
	/**
	 * 
	 */
	private static final long FirstBreakBuyStrategyUID = 6281091140874L;

	public FirstBreakBuyStrategy(String MarketID) {
		super(MarketID, FirstBreakBuyStrategyUID);
		// TODO Auto-generated constructor stub
	}
	
	
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		addBuyBigCondition(new DetectBigChangeCondition(-5, 1, -1, true), false);
		addBuyLittleCondition(new NewHigherORLowerCondition(10, 30)); //10% - 40 %
		addBuyLittleCondition(new VolumeChangeCondition(1, 5));
		
		//addSellBigCondition(new StopLossCondition(8), true);
		addSellBigCondition(new LastNdayCondition(5), true);
		//addSellBigCondition(new StopEarnCondition(20), true);
	}

}
