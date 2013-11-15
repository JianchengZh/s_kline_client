package com.zhangwei.stock.condition;

import java.util.ArrayList;
import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.basic.Condition;
import com.zhangwei.stock.basic.Point;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.util.StockHelper;

/**
 * 止损条件
 * */
public class StopLossCondition implements Condition {
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
		
		if(lastPoint==null){
			throw new StockException("lastPoint is null");
		}
		
		if(!lastPoint.isBuy()){
			throw new StockException("lastPoint is sell");
		}
		
		if(kl==null){
			throw new StockException("kline is null");
		}
		
		if(kl.size()<1){
			throw new StockException("kline.size is < 1");
		}
		
		KLineUnit elem = StockHelper.binSearch(kl, lastPoint.date, 1);

		if(elem!=null){
			int curPrice = kl.get(kl.size()-1).close;
			int exRightFactor = StockHelper.getExrightFactor(kl, lastPoint.date);
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
