package com.zhangwei.stock.condition;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.util.StockHelper;

public class VolumeChangeCondition implements ICondition {
	private boolean direction;//true change bigger or false change smaller
	private int nDay;
	private int percent;

	public VolumeChangeCondition(int nDay, int percent){
		this.nDay = nDay;
		this.percent = Math.abs(percent);
		this.direction = this.percent>100?true:false;
	}

	@Override
	public boolean checkCondition(StockInfo info, List<KLineUnit> kl,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		if(!StockHelper.checkKlineVaild(kl)){
			throw new StockException("kl is inVaild!");
		}
		
		int lastIndex = kl.size() - 1;
		
		if(lastIndex<nDay+1){
			throw new StockException("kl's size is too small!");
		}
		
		int averageVolOfRest = StockHelper.calcAverage(kl.subList(0, lastIndex-nDay), 3);
		int averageVolOflastNDay = StockHelper.calcAverage(kl.subList(lastIndex-nDay, lastIndex+1), 3);
		
		if(direction){
			return (averageVolOflastNDay * 100 /averageVolOfRest) > percent;
		}else{
			return (averageVolOflastNDay * 100 /averageVolOfRest) < percent;
		}
		
	}

}
