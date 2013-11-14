package com.zhangwei.stock.basic;

import java.util.ArrayList;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;


public class StopLossCondition implements Condition {
	int percent;
	
	public StopLossCondition(int percent){
		if(percent<=0){
			percent = 8; //default 8%
		}
		this.percent = percent;
	}

	/**
	 * 注意：止损期过长，中间发生扩股的情况
	 * */
	@Override
	public boolean checkCondition(StockInfo info, ArrayList<KLineUnit> kl,
			Point lastPoint) {
		// TODO Auto-generated method stub
		
		return false;
	}

}
