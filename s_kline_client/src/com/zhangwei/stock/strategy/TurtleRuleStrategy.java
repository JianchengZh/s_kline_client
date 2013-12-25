package com.zhangwei.stock.strategy;

import com.zhangwei.stock.condition.ContinuousPercentCondition;
import com.zhangwei.stock.condition.DetectBigChangeCondition;
import com.zhangwei.stock.condition.LastNdayCondition;
import com.zhangwei.stock.condition.NewHigherORLowerCondition;
import com.zhangwei.stock.condition.NewHigherORLowerNDayCondition;
import com.zhangwei.stock.condition.VolumeChangeCondition;

public class TurtleRuleStrategy extends BasicStrategy {

	/**
	 * 
	 */
	private static final long TurtleRuleStrategyUID = 123456123456L;

	public TurtleRuleStrategy(String MarketID) {
		super(MarketID, TurtleRuleStrategyUID);
		// TODO Auto-generated constructor stub
	}
	
	
	public void init() {
		// TODO Auto-generated method stub
		super.init();
/*   55日最高时买入  20日最低时卖出*/  
/* 		addBuyBigCondition(new NewHigherORLowerNDayCondition(true, 55), true);
		
		addSellBigCondition(new NewHigherORLowerNDayCondition(false, 20), true);*/
		
		addBuyBigCondition(new ContinuousPercentCondition(55, -30), true);
		
		addSellBigCondition(new NewHigherORLowerNDayCondition(false, 20), true);
	}

}
