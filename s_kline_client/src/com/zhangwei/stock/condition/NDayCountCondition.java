package com.zhangwei.stock.condition;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.StockHelper;

public class NDayCountCondition implements ICondition {
	private int nDay;
	private int count;
	private boolean up;

	public NDayCountCondition(int nDay, int count, boolean up){
		this.nDay = Math.abs(nDay);
		this.count = count;
		this.up = up;
	}

	@Override
	public boolean checkCondition(StockInfo info, KLineTypeResult rlt,
			List<KLineUnit> kl, Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		
		if(!StockHelper.checkKlineVaild(kl)){
			throw new StockException("kl is inVaild!");
		}
		
		if(kl.size()-1<nDay){
			throw new StockException("kl's size is too small!");
		}
		
		int index1 = kl.size() - 1 - nDay;
		int index2 = kl.size() - 1;
		int count_t=0;
		KLineUnit last = null;
		for(int index=index1; index<=index2; index++){
			KLineUnit item = kl.get(index);
			if(last!=null){
				if(up && last.close<item.close){
					count_t++;
				}else if(!up && last.close>item.close){
					count_t++;
				}
			}
			
			last = item;
		}
		
		return count_t>=count;
	}

}
