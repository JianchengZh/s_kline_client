package com.zhangwei.stock.emu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.IBuy;
import com.zhangwei.stock.bs.ISell;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.bs.TradeUnit;
import com.zhangwei.stock.daygenerater.DayGenerater;
import com.zhangwei.stock.daygenerater.EmuTodayGenerater;
import com.zhangwei.stock.manager.EmuAssertManager;
import com.zhangwei.stock.manager.IAssertManager;
import com.zhangwei.stock.parallel.ParallelListener;
import com.zhangwei.stock.parallel.ParallelManager;
import com.zhangwei.stock.selector.ISelector;
import com.zhangwei.stock.selector.SimpleSelector;
import com.zhangwei.stock.strategy.BasicStrategy;
import com.zhangwei.stock.strategy.MyHighSellLowBuyStrategy;
import com.zhangwei.stock.strategy.MyWeakBuyStrategy;
import com.zhangwei.stock.task.BuyCheckTask;
import com.zhangwei.stock.task.ITaskSellResultCheck;
import com.zhangwei.stock.task.SellCheckTask;
import com.zhangwei.stock.task.StockParallelEmuTradeTask;
import com.zhangwei.stock.task.StockSerialEmuTradeTask;
import com.zhangwei.stock.task.StockUpdateTask;
import com.zhangwei.stock.task.ITaskBuyResultCheck;
import com.zhangwei.stock.tradesystem.EmuTradeSystem;
import com.zhangwei.stock.tradesystem.ITradeSystem;
import com.zhangwei.stock.transaction.EmuBuyTransaction;
import com.zhangwei.util.DateHelper;
import com.zhangwei.util.StockHelper;

public class ParallelEmuMarket implements ITaskBuyResultCheck, ITaskSellResultCheck/*, IBuy, ISell*/ {
	private static final String TAG = "ParallelEmuMarket";

	public static final String UID = "ParallelEmuMarket";
	
	private BasicStrategy bs;
	private EmuTodayGenerater dayGen;
	private EmuAssertManager assertManager;
	//private EmuTradeSystem tradeSystem;

	private HashMap<String, HoldUnit> holds;
	private HashMap<String, HoldUnit> candidateSellHolds;
	private HashMap<String, BuyPoint> candidateBuyPoints;

	private ISelector selector;
	
	public final static String order_key = "buy_date"; //"earn_percent desc", null

	public ParallelEmuMarket(){
		bs = new MyWeakBuyStrategy(UID);
		dayGen = new EmuTodayGenerater(UID, bs);
		//tradeSystem = EmuTradeSystem.getInstance();
		
		holds = new HashMap<String, HoldUnit>();
		candidateBuyPoints = new HashMap<String, BuyPoint>();
		candidateSellHolds = new HashMap<String, HoldUnit>();
		selector = new SimpleSelector();
	}
	
	public void run(){
		if(dayGen.checkOK()){
			int day = 0;
			
			StockManager sm = StockManager.getInstance();
			ArrayList<StockInfo> stocks = sm.FetchStockInfo(false, null, -1);
			ParallelManager pm = new ParallelManager();//ParallelManager.getInstance();
			
			day = dayGen.getToday();
			while(DateHelper.checkVaildDate(day)){

				//0. init
				candidateBuyPoints.clear();
				candidateSellHolds.clear();
				
				//1. check sell,得到要卖的候选股票
				for(Entry<String, HoldUnit> item : holds.entrySet()){
					pm.submitTask(new SellCheckTask(this, item.getValue(), bs, day));
				}
				pm.startTask(null, 8);
				pm.join();

				
				//2. check buy， ,得到要买的候选股票，不能再要卖的股票中，用selector进一步筛选
				for(StockInfo item : stocks){
					pm.submitTask(new BuyCheckTask(this, item, bs, day));
				}
				pm.startTask(null, 8);
				pm.join();
				//将candiates中的 去掉holds含有的元素,并用selector挑选最合适的
				StockHelper.removeHolds(candidateBuyPoints, holds);
				Map<String, BuyPoint> theBestBuy = selector.getBestBuyPoint(candidateBuyPoints, bs);
				
				//3. sell
				
				//4. buy
/*				if(theBestBuy!=null && theBestBuy.size()>0){
					ArrayList<BuyPoint> canBuys = assertManager.getCanBuys(theBestBuy);
					for(BuyPoint bp:canBuys){
						//tradeSystem.submitBuyTransaction(this, bp);
					}
				}*/
				
				assertManager.requestBuy(theBestBuy);
				assertManager.requestSell(candidateSellHolds);
				
				
				int tmpToday = dayGen.getToday();
				if(tmpToday == day){
					break;
				}else{
					day = tmpToday;
				}
				
			}
		}


	}
	
	public static void main(String[] args){
		
		ParallelEmuMarket se = new ParallelEmuMarket();
		se.run();
	}


	/**
	 * 回调，将符合Strategy的BuyPoint登记到candidateBuyPoints中
	 * */
	@Override
	public void check(BuyPoint bp) {
		// TODO Auto-generated method stub
		String key = bp.getKey();
		if(key!=null){
			candidateBuyPoints.put(key, bp);
		}

	}

	/**
	 * 回调，将符合Strategy的HoldUnit登记到candidateSellHolds中
	 * */
	@Override
	public void check(HoldUnit hu) {
		// TODO Auto-generated method stub
		String key = hu.getKey();
		if(key!=null){
			candidateSellHolds.put(key, hu);
		}
	}



}
