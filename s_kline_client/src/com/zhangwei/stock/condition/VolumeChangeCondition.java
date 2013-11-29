package com.zhangwei.stock.condition;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.bs.Point;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.StockHelper;

public class VolumeChangeCondition implements ICondition {
	private boolean direction;//true change bigger or false change smaller
	private int nDay;
	private int percent;
	private int value;

	public VolumeChangeCondition(int nDay, int percent){
		this.nDay = nDay;
		this.percent = Math.abs(percent);
		this.direction = this.percent>100?true:false;
	}

	@Override
	public boolean checkCondition(StockInfo info, KLineTypeResult rlt, List<KLineUnit> kl,
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
			value = (averageVolOflastNDay * 100 /averageVolOfRest) - 100;
			return (averageVolOflastNDay * 100 /averageVolOfRest) > percent;
		}else{
			value = 100 - (averageVolOflastNDay * 100 /averageVolOfRest);
			return (averageVolOflastNDay * 100 /averageVolOfRest) < percent;
		}
		
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return value;
	}

}
