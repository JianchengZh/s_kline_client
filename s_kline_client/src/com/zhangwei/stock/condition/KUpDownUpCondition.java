package com.zhangwei.stock.condition;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.bs.Point;
import com.zhangwei.stock.kline.KLineType;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.StockHelper;

public class KUpDownUpCondition implements ICondition {
	int mainUpPercent;
	int mainUpRange;
	int mainDownPercent;
	int mainDownRange;
	int subUpPercent;
	int subUpRange;
	
	public KUpDownUpCondition(int mainUpPercent, int mainUpRange, int mainDownPercent, int mainDownRange, int subUpPercent, int subUpRange){
		this.mainUpPercent = Math.abs(mainUpPercent);
		this.mainUpRange = Math.abs(mainUpRange);
		this.mainDownPercent = Math.abs(mainDownPercent);
		this.mainDownRange = Math.abs(mainDownRange);
		this.subUpPercent = Math.abs(subUpPercent);
		this.subUpRange = Math.abs(subUpRange);
	}

	@Override
	public boolean checkCondition(StockInfo info, KLineTypeResult rlt, List<KLineUnit> kl,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		if(!StockHelper.checkKlineVaild(kl)){
			throw new StockException("kl is inVaild!");
		}
		
		KLineTypeResult ktr = StockHelper.getKlineType(kl);
		if(ktr.type!=KLineType.LHR){
			return false;
		}
		
		List<KLineUnit> sub_kl = StockHelper.getInterval(kl, ktr.HIGH, ktr.RIGHT);
		if(!StockHelper.checkKlineVaild(sub_kl)){
			return false;
		}
		
		KLineTypeResult sub_ktr = StockHelper.getKlineType(sub_kl);
		if(sub_ktr.type!=KLineType.HLR){
			return false;
		}
		
		if(!StockHelper.checkRange(ktr.LOW, ktr.HIGH, mainUpPercent, mainUpRange, true)){
			return false;
		}
		
		if(!StockHelper.checkRange(sub_ktr.HIGH, sub_ktr.LOW, mainDownPercent, mainDownRange, false)){
			return false;
		}
		
		if(!StockHelper.checkRange(sub_ktr.LOW, sub_ktr.RIGHT, subUpPercent, subUpRange, true)){
			return false;
		}
		
		return true;
	}

}
