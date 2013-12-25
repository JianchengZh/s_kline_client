package com.zhangwei.stock.condition;

import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.bs.Point;
import com.zhangwei.stock.kline.KLineType;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.StockHelper;

public class BankerCostApproachCondition implements ICondition {
	int percent;

	private int value;
	
	public BankerCostApproachCondition(int percent){
		this.percent = Math.abs(percent);
	}

	@Override
	public boolean checkCondition(StockInfo info, KLineTypeResult rlt, List<KLineUnit> kl_arg,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		if(!StockHelper.checkKlineVaild(kl_arg)){
			throw new StockException("kl_arg is inVaild!");
		}
		
		List<KLineUnit> kl = StockHelper.getForwardExrightKLine(kl_arg);
		
		int nowPrice = kl.get(kl.size()-1).close;
		
		KLineTypeResult ktr = StockHelper.getKlineType(kl);
		
		int chengben = getBossAveragePrice(kl, ktr.vol_average);
		
		/**
		 * value: 大于0表示跌幅的百分比  小于0无意义
		 * */
		if(chengben<=0 || chengben<nowPrice){
			value = -1;
			return false;
		}else{
			value = 100 - nowPrice*100/chengben;
			return true;
		}
		
	}

	/**
	 * @return 大于0表示庄家成本价 小于0表示庄家已出逃
	 * */
	public static int getBossAveragePrice(List<KLineUnit> kl, int vol_average) {
		// TODO Auto-generated method stub
		long sum_total = 0; //庄家累计买入资金 单位分
		long vol_total = 0; //庄家累计买入数量 单位股
		int lastDayAvePrice = -1;
		for(KLineUnit elem : kl){
			if(elem.vol>vol_average){
				if(lastDayAvePrice>0){
					int todayAvePrice = getAveragePrice(elem);
					if(todayAvePrice>lastDayAvePrice){
						sum_total +=  elem.vol * todayAvePrice;
						vol_total += elem.vol;
					}else if(todayAvePrice<lastDayAvePrice){
						sum_total -=  elem.vol * todayAvePrice;
						vol_total -= elem.vol;
					}
					
				}

			}
			
			lastDayAvePrice = getAveragePrice(elem);
		}
		
		if(vol_total>0 && sum_total>0){
			return (int)(sum_total/vol_total);
		}else{
			return -1;
		}
	}

	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	
	public static int getAveragePrice(KLineUnit ku){
		return (ku.open+ku.high+ku.low+ku.close)/4;
	}

}
