package com.zhangwei.stock.Strategy;

import com.zhangwei.stock.condition.ContinuousDeclineCondition;
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
	
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		addBuySufficientCondition(new ContinuousDeclineCondition(10, 30));
		addBuySufficientCondition(new VolumeChangeCondition(3, 50));
		
		addSellNecessaryCondition(new StopLossCondition(10));
		addSellNecessaryCondition(new LastNdayCondition(15));
		addSellNecessaryCondition(new StopEarnCondition(20));
	}

}
