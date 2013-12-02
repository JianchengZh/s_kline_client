package com.zhangwei.stock.emu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.TradeUnit;
import com.zhangwei.stock.daygenerater.EmuTodayGenerater;
import com.zhangwei.stock.daygenerater.RealTodayGenerater;
import com.zhangwei.stock.gui.GuiManager;
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
import com.zhangwei.stock.task.ITaskBuyResultCheck;
import com.zhangwei.util.DateHelper;
import com.zhangwei.util.StockHelper;

public class TodayMarket implements ITaskBuyResultCheck {
	private static final String TAG = "TodayMarket";

	public static final String UID = "TodayMarket";
	
	private BasicStrategy bs;
	private RealTodayGenerater dayGen;
	//private EmuAssertManager assertManager;
	//private EmuTradeSystem tradeSystem;

	private HashMap<String, HoldUnit> holds;
	private HashMap<String, HoldUnit> candidateSellHolds;
	private HashMap<String, BuyPoint> candidateBuyPoints;

	private ISelector selector;
	
	public final static String order_key = "buy_date"; //"earn_percent desc", null

	public TodayMarket(){
		bs = new MyWeakBuyStrategy(UID);
		bs.init();
		dayGen = new RealTodayGenerater();
		//tradeSystem = EmuTradeSystem.getInstance();
		
		holds = new HashMap<String, HoldUnit>();
		candidateBuyPoints = new HashMap<String, BuyPoint>();
		selector = new SimpleSelector();
	}
	
	public void run(){

		int day = 0;
		
		StockManager sm = StockManager.getInstance();
		ArrayList<StockInfo> stocks = sm.FetchStockInfo(false, null, -1);
		ParallelManager pm = new ParallelManager();//ParallelManager.getInstance();
		
		day = dayGen.getToday();
		if(DateHelper.checkVaildDate(day)){

			//0. init
			candidateBuyPoints.clear();
			
			//1. check sell,得到要卖的候选股票
			
			//2. check buy， ,得到要买的候选股票，不能再要卖的股票中，用selector进一步筛选
			for(StockInfo item : stocks){
				pm.submitTask(new BuyCheckTask(this, item, bs, day));
			}
			pm.startTask(null, 8);
			pm.join();

			ArrayList<TradeUnit> rlt = new ArrayList<>();
			if(candidateBuyPoints!=null && candidateBuyPoints.size()>0){
				for(Entry<String, BuyPoint>  elem : candidateBuyPoints.entrySet()){
					BuyPoint bp = elem.getValue();
					TradeUnit tu = new TradeUnit(bp.stock_id, bp.market_type, bp.date, -1, bp.price, -1, 0);
					rlt.add(tu);
				}
				
				GuiManager.getInstance().showResult(rlt);
			}

			
		}
	


	}
	
	public static void main(String[] args){
		
		TodayMarket se = new TodayMarket();
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

}
