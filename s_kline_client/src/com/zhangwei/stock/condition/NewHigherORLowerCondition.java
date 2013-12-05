package com.zhangwei.stock.condition;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.bs.Point;
import com.zhangwei.stock.kline.KLineType;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.StockHelper;

public class NewHigherORLowerCondition implements ICondition {
	int mainUpPercent;
	int mainUpRange;

	private int value;
	
	public NewHigherORLowerCondition(int mainUpPercent, int mainUpRange){
		this.mainUpPercent = Math.abs(mainUpPercent);
		this.mainUpRange = Math.abs(mainUpRange);
	}

	@Override
	public boolean checkCondition(StockInfo info, KLineTypeResult rlt, List<KLineUnit> kl,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		if(!StockHelper.checkKlineVaild(kl)){
			throw new StockException("kl is inVaild!");
		}
		
		KLineTypeResult ktr = StockHelper.getKlineType(kl);
		if(ktr.type!=KLineType.LH && ktr.type!=KLineType.LLH){
			return false;
		}
		
		List<KLineUnit> sub_kl = StockHelper.getInterval(kl, ktr.LOW, ktr.HIGH);
		if(!StockHelper.checkKlineVaild(sub_kl)){
			return false;
		}
		
		if(!StockHelper.checkRange(ktr.LOW, ktr.HIGH, mainUpPercent, mainUpRange, true)){
			return false;
		}
		
		value = ktr.HIGH.close * 100 / ktr.LOW.close;
		
		return true;
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return value;
	}

}
