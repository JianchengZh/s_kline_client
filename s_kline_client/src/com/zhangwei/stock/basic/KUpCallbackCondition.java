package com.zhangwei.stock.basic;

import java.util.ArrayList;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;

public class KUpCallbackCondition implements Condition {
	
	public KUpCallbackCondition(int mainUpPercent, int mainDownPercent, int subUpPercent){
		
	}

	@Override
	public boolean checkCondition(StockInfo info, ArrayList<KLineUnit> kl,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		return false;
	}

}
