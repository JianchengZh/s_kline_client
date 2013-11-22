package com.zhangwei.stock.condition;

import java.util.ArrayList;
import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.StockHelper;

public class DetectBigChangeCondition implements ICondition {
	int percent;
	int count;
	
	/**
	 * @param percent -10 - 10
	 * @param count 0:出现0次， n：出现情况大于等于n次
	 * @param volPercent 出现的时候
	 * */
	public DetectBigChangeCondition(int percent, int count, int volPercent){
		this.percent = percent;
		this.count = count;
	}

	@Override
	public boolean checkCondition(StockInfo info, KLineTypeResult rlt, List<KLineUnit> kl,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		if(!StockHelper.checkKlineVaild(kl)){
			throw new StockException("kl is inVaild!");
		}
		
		if(kl.size()-1<percent){
			throw new StockException("kl's size is too small!");
		}
		
		KLineUnit elem = StockHelper.binSearch(kl, lastPoint.date, 1);

		if(elem!=null){
			int index = kl.indexOf(elem);
			if(kl.size()-index>percent){
				return true;
			}else{
				return false;
			}
		}else{
			throw new StockException("lastPoint.date not in kl list");
		}
	}

}
