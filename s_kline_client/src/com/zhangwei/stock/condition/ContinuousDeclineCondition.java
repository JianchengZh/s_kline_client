package com.zhangwei.stock.condition;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.basic.Condition;
import com.zhangwei.stock.basic.Point;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.util.StockHelper;

/**
 * n天累计下跌Percent
 * */
public class ContinuousDeclineCondition implements Condition {
	private int nDay;
	private int Percent;

	public ContinuousDeclineCondition(int nDay, int Percent){
		this.nDay = Math.abs(nDay);
		this.Percent = Math.abs(Percent);
	}

	@Override
	public boolean checkCondition(StockInfo info, List<KLineUnit> kl,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		if(!StockHelper.checkKlineVaild(kl)){
			throw new StockException("kl is inVaild!");
		}
		
		if(kl.size()-1<nDay){
			throw new StockException("kl's size is too small!");
		}
		
		int index1 = kl.size() - 1 - nDay;
		int index2 = kl.size() - 1;
		
		int fudu = kl.get(index2).close * 100 / kl.get(index1).close - 100;
		if(fudu>-Percent){
			return false;
		}
		
		return true;
	}

}