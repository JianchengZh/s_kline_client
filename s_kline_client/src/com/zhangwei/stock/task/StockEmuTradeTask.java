package com.zhangwei.stock.task;

import java.util.List;

import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;

public class StockEmuTradeTask implements StockTask {

	private static final String TAG = "StockEmuTradeTask";
	private StockInfo info;

	public StockEmuTradeTask(StockInfo info){
		this.info = info;
	}
	
	@Override
	public void processTask() {
		// TODO Auto-generated method stub
		StockManager sm = StockManager.getInstance();
		Stock stock = sm.getStock(info, false);
		stock.generateNDayKline(60, true);
		
		List<KLineUnit> kl;
		while((kl = stock.generateNDayKline(60, false))!=null){
			Log.v(TAG, "date:" + kl.get(0).date + ", kl.size:" + kl.size());
		}
	}

}
