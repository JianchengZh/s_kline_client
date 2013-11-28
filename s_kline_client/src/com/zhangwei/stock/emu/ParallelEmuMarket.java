package com.zhangwei.stock.emu;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.bs.TradeUnit;
import com.zhangwei.stock.daygenerater.DayGenerater;
import com.zhangwei.stock.daygenerater.EmuTodayGenerater;
import com.zhangwei.stock.manager.EmuAssertManager;
import com.zhangwei.stock.manager.IAssertManager;
import com.zhangwei.stock.parallel.ParallelListener;
import com.zhangwei.stock.parallel.ParallelManager;
import com.zhangwei.stock.strategy.BasicStrategy;
import com.zhangwei.stock.strategy.MyHighSellLowBuyStrategy;
import com.zhangwei.stock.strategy.MyWeakBuyStrategy;
import com.zhangwei.stock.task.StockParallelEmuTradeTask;
import com.zhangwei.stock.task.StockSerialEmuTradeTask;
import com.zhangwei.stock.task.StockUpdateTask;
import com.zhangwei.stock.tradesystem.EmuTradeSystem;
import com.zhangwei.stock.tradesystem.ITradeSystem;
import com.zhangwei.util.DateHelper;
import com.zhangwei.util.StockHelper;

public class ParallelEmuMarket implements ParallelListener {
	private static final String TAG = "ParallelEmuMarket";

	public static final String UID = "ParallelEmuMarket";
	
	private BasicStrategy bs;
	private EmuTodayGenerater dayGen;
	private EmuAssertManager assertManager;
	private EmuTradeSystem tradeSystem;
	
	public final static String order_key = "buy_date"; //"earn_percent desc", null

	public ParallelEmuMarket(){
		bs = new MyWeakBuyStrategy(UID);
		dayGen = new EmuTodayGenerater(UID, bs);
		tradeSystem = EmuTradeSystem.getInstance();
	}
	
	public void run(){
		if(dayGen.checkOK()){
			int day = 0;
			do{
				day = dayGen.getToday();
				
				StockManager sm = StockManager.getInstance();
				ArrayList<StockInfo> stocks = sm.FetchStockInfo(false, null, -1);
				ParallelManager pm = new ParallelManager();//ParallelManager.getInstance();
				for(StockInfo item : stocks){
					pm.submitTask(new StockParallelEmuTradeTask(item, bs));
				}
				pm.startTask(this, 8);
				pm.join();
				
			}while(DateHelper.checkVaildDate(day));
		}


	}
	
	public static void main(String[] args){
		
		ParallelEmuMarket se = new ParallelEmuMarket();
		se.run();
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onComplete ");
	}

}
