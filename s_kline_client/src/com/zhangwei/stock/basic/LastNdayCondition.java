package com.zhangwei.stock.basic;

import java.util.ArrayList;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.util.StockHelper;

public class LastNdayCondition implements Condition {
	int nDay;
	
	/**
	 * nDay from 1 to unlimit
	 * 1, 当天买第二天抛
	 * */
	public LastNdayCondition(int nDay){
		this.nDay = nDay;
	}

	@Override
	public boolean checkCondition(StockInfo info, ArrayList<KLineUnit> kl,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		if(lastPoint==null){
			throw new StockException("lastPoint is null");
		}
		
		if(kl==null){
			throw new StockException("kline is null");
		}
		
		if(kl.size()<1){
			throw new StockException("kline.size is < 1");
		}
		
		KLineUnit elem = StockHelper.binSearch(kl, lastPoint.date, 1);

		if(elem!=null){
			int index = kl.indexOf(elem);
			if(kl.size()-index>nDay){
				return true;
			}else{
				return false;
			}
		}else{
			throw new StockException("lastPoint.date not in kl list");
		}
	}

}
