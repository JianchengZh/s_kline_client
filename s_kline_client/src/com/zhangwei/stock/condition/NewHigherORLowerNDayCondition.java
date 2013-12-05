package com.zhangwei.stock.condition;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.bs.Point;
import com.zhangwei.stock.kline.KLineType;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.StockHelper;

public class NewHigherORLowerNDayCondition implements ICondition {

	private boolean isUp;
	private int nDay;

	private int value;
	
	public NewHigherORLowerNDayCondition(boolean isUp, int nDay){
		this.isUp = isUp;
		this.nDay = nDay;
	}

	@Override
	public boolean checkCondition(StockInfo info, KLineTypeResult rlt, List<KLineUnit> kl,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		if(!StockHelper.checkKlineVaild(kl)){
			throw new StockException("kl is inVaild!");
		}
		

		
		if(kl.size()<nDay){
			return false;
		}
		
		int indexFrom = kl.size() - nDay;
		int indexTo = kl.size();
		
		KLineTypeResult ktr = StockHelper.getKlineType(kl.subList(indexFrom, indexTo));
		
		value = ktr.RIGHT.close * 100 / ktr.price_average;
		
		if(isUp){
			if(ktr.type!=KLineType.LH && ktr.type!=KLineType.LLH){
				return false;
			}
		}else{
			if(ktr.type!=KLineType.HL && ktr.type!=KLineType.LHL){
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return value;
	}

}
