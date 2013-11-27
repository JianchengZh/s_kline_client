package com.zhangwei.stock.condition;

import java.util.ArrayList;
import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.bs.Point;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.StockHelper;

public class DetectBigChangeCondition implements ICondition {
	int percent;
	int count;
	int volPercent;
	boolean ban;
	
	/**
	 * @param percent -10 - 10
	 * @param count 0:出现0次， n：出现情况大于等于n次
	 * @param volPercent 出现的时候成交量占平均的比例 0 - unlimit,若<0则禁用该条件
	 * @param ban 是否考虑板，若为true，则为板时不考虑volPercent条件
	 * */
	public DetectBigChangeCondition(int percent, int count, int volPercent, boolean ban){
		this.percent = percent;
		this.count = count;
		this.volPercent = volPercent;
		this.ban = ban;
	}

	@Override
	public boolean checkCondition(StockInfo info, KLineTypeResult rlt, List<KLineUnit> kl,
			Point lastPoint) throws StockException {
		// TODO Auto-generated method stub
		if(!StockHelper.checkKlineVaild(kl)){
			throw new StockException("kl is inVaild!");
		}

		int countOK = 0; //满足条件的次数
		KLineUnit last = null;
		for(KLineUnit elem : kl){
			if(last!=null){
				if(percent>0){
					if(ban){
						if(elem.isUpBan(last)){
							countOK++;
						}
					}else{
						if(StockHelper.compare(last, elem) > percent){
							if(volPercent>0){
								if(volPercent>100 && elem.vol/rlt.vol_average>volPercent){
									countOK++;
								}else if(volPercent<100 && elem.vol/rlt.vol_average<volPercent){
									countOK++;
								}
							}else{
								countOK++;
							}

							
						}
					}
					
				}else{
					if(ban){
						if(elem.isDownBan(last)){
							countOK++;
						}
					}else{
						if(StockHelper.compare(last, elem) < percent){
							if(volPercent>0){
								if(volPercent>100 && elem.vol/rlt.vol_average>volPercent){
									countOK++;
								}else if(volPercent<100 && elem.vol/rlt.vol_average<volPercent){
									countOK++;
								}
							}else{
								countOK++;
							}
						}
					}
				}
			}
			
			last = elem;
		}
		
		return countOK>=count;

	}

}
