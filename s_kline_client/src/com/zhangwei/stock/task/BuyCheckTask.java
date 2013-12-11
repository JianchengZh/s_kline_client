package com.zhangwei.stock.task;



import java.util.List;


import com.zhangwei.stock.Constants;
import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.bs.BuyPoint;

import com.zhangwei.stock.strategy.BasicStrategy;


import com.zhangwei.util.StockHelper;

public class BuyCheckTask implements StockTask {

	private static final String TAG = "StockParallelEmuTradeTask";
	private StockInfo info;
	private BasicStrategy bs;
	private ITaskBuyResultCheck taskResultCheck;
	private int today;

	public BuyCheckTask(ITaskBuyResultCheck taskResultCheck, StockInfo info, BasicStrategy bs, int today){
		this.info = info;
		this.bs = bs;
		this.taskResultCheck = taskResultCheck;
		this.today = today;
	}
	
	@Override
	public void processTask() {
		// TODO Auto-generated method stub
		StockManager sm = StockManager.getInstance();
		Stock stock = sm.getStock(info, false);
		List<KLineUnit> kl = stock.generateNDayKlineToNow(Constants.BUYPOINT_PREFIX_LEN, today);
		if(kl==null || kl.size()<Constants.BUYPOINT_PREFIX_LEN){
			return;
		}
		
		List<KLineUnit> exRightKl = StockHelper.getForwardExrightKLine(kl);
		
		//check buy
		boolean canBuy = bs.checkBuy(info, exRightKl, null);
		if(canBuy){
			KLineUnit last = exRightKl.get(exRightKl.size()-1);
			int date = last.date;
			int price = last.close;

			boolean isUpBan = last.isUpBan(exRightKl.get(exRightKl.size()-2));
			if(!isUpBan){
				//EmuBuyTransaction ebt = new EmuBuyTransaction();
				BuyPoint bp = new BuyPoint(bs.getUID(), info.stock_id, info.market_type, date, 0, price, 0, exRightKl.get(exRightKl.size()-2));
				//ebt.buy(info, lastBuyPoint);
				taskResultCheck.check(bp);
			}

		}

	}

}
