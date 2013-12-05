package com.zhangwei.stock.strategy;

import com.zhangwei.stock.condition.KUpDownUpCondition;
import com.zhangwei.stock.condition.LastNdayCondition;
import com.zhangwei.stock.condition.StopEarnCondition;
import com.zhangwei.stock.condition.StopLossCondition;

public class MyHighSellLowBuyStrategy extends BasicStrategy{

	/**
	 * 
	 */
	private static final long MyHighSellLowBuyStrategyUID = 8287568540830L;

	public MyHighSellLowBuyStrategy(String MarketID){
		super(MarketID, MyHighSellLowBuyStrategyUID);
	}
	

	public void init() {
		// TODO Auto-generated method stub
		super.init();
		addBuyLittleCondition(new KUpDownUpCondition(30, 80, 10, 10, 5, 5));
		
		//addSellBigCondition(new StopLossCondition(8), true);
		addSellBigCondition(new LastNdayCondition(5), true);
		//addSellBigCondition(new StopEarnCondition(8), true);
	}
}
