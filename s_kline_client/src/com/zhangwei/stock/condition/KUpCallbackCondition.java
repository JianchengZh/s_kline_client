package com.zhangwei.stock.condition;

import java.util.ArrayList;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.basic.Condition;
import com.zhangwei.stock.basic.Point;
import com.zhangwei.stock.basic.StockException;

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
