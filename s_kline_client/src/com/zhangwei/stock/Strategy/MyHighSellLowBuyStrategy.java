package com.zhangwei.stock.Strategy;

import com.zhangwei.stock.condition.KUpDownUpCondition;
import com.zhangwei.stock.condition.LastNdayCondition;
import com.zhangwei.stock.condition.StopEarnCondition;
import com.zhangwei.stock.condition.StopLossCondition;

public class MyHighSellLowBuyStrategy extends BasicStrategy{

	/**
	 * 
	 */
	private static final long MyHighSellLowBuyStrategyUID = 8287568540830L;

	public MyHighSellLowBuyStrategy(){
		super(MyHighSellLowBuyStrategyUID);
	}
	

	public void init() {
		// TODO Auto-generated method stub
		super.init();
		addBuyLittleCondition(new KUpDownUpCondition(30, 30, 20, 20, 5, 5));
		
		addSellBigCondition(new StopLossCondition(10), true);
		addSellBigCondition(new LastNdayCondition(10), true);
		addSellBigCondition(new StopEarnCondition(10), true);
	}
}
