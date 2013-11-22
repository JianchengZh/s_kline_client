package com.zhangwei.stock.condition;

import java.util.ArrayList;
import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.StockHelper;

public class LastNdayCondition implements ICondition {
	int nDay;
	
	/**
	 * nDay from 1 to unlimit
	 * 1, 当天买第二天抛
	 * */
	public LastNdayCondition(int nDay){
		this.nDay = nDay;
	}

	@Override
	public boolean checkCondition(StockInfo info, KLineTypeResult rlt, List<KLineUnit> kl,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		if(!StockHelper.checkKlineVaild(kl)){
			throw new StockException("kl is inVaild!");
		}
		
		if(kl.size()-1<nDay){
			throw new StockException("kl's size is too small!");
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
