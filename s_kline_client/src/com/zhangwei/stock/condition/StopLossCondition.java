package com.zhangwei.stock.condition;

import java.util.ArrayList;
import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.util.StockHelper;

/**
 * 止损条件
 * */
public class StopLossCondition implements ICondition {
	int percent;
	
	public StopLossCondition(int percent){
		if(percent<=0 || percent>50){
			percent = 8; //default 8%
		}
		this.percent = percent;
	}

	/**
	 * 注意：止损期过长，中间发生扩股的情况
	 * */
	@Override
	public boolean checkCondition(StockInfo info, List<KLineUnit> kl,
			Point lastPoint) throws StockException{
		// TODO Auto-generated method stub
		if(!StockHelper.checkKlineVaild(kl)){
			throw new StockException("kl is inVaild!");
		}
		
		if(kl.size()<1){
			throw new StockException("kl's size is too small!");
		}
		
		if(lastPoint==null){
			throw new StockException("lastPoint is null");
		}
		
	
		KLineUnit elem = StockHelper.binSearch(kl, lastPoint.date, 1);

		if(elem!=null){
			int curPrice = kl.get(kl.size()-1).close;
			int exRightFactor = StockHelper.getExrightFactorPercent(kl, lastPoint.date);
			if(curPrice * exRightFactor / lastPoint.price < 100 - percent){
				return true;
			}else{
				return false;
			}
		}else{
			throw new StockException("lastPoint.date not in kl list");
		}

	}

}
