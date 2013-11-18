package com.zhangwei.stock.Strategy;

import com.zhangwei.stock.condition.KUpDownUpCondition;
import com.zhangwei.stock.condition.LastNdayCondition;
import com.zhangwei.stock.condition.StopEarnCondition;
import com.zhangwei.stock.condition.StopLossCondition;

public class MyHighSellLowBuyStrategy extends BasicStrategy {

	public MyHighSellLowBuyStrategy(){
		super();
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		addBuySufficientCondition(new KUpDownUpCondition(30, 30, 20, 20, 5, 5));
		
		addSellNecessaryCondition(new StopLossCondition(10));
		addSellNecessaryCondition(new LastNdayCondition(10));
		addSellNecessaryCondition(new StopEarnCondition(10));
	}
}
