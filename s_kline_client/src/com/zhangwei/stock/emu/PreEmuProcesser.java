package com.zhangwei.stock.emu;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.bs.TradeUnit;
import com.zhangwei.stock.parallel.ParallelListener;
import com.zhangwei.stock.parallel.ParallelManager;
import com.zhangwei.stock.strategy.BasicStrategy;
import com.zhangwei.stock.strategy.MyHighSellLowBuyStrategy;
import com.zhangwei.stock.strategy.MyWeakBuyStrategy;
import com.zhangwei.stock.task.StockParallelEmuTradeTask;
import com.zhangwei.stock.task.StockSerialEmuTradeTask;
import com.zhangwei.stock.task.StockUpdateTask;
import com.zhangwei.stock.tradesystem.EmuTradeSystem;

public class PreEmuProcesser implements ParallelListener {
	private static final String TAG = "PreEmuProcesser";
	public  BasicStrategy bs;
	public static final String UID = "PEM";
	
	public final static String order_key = "buy_date"; //"earn_percent desc", null

	public void run(boolean flag){
		
		//bs = new MyHighSellLowBuyStrategy();
		bs = new MyWeakBuyStrategy(UID);
		EmuTradeSystem es = EmuTradeSystem.getInstance();
		List<TradeUnit> rlt = es.getTradeInfo(bs.getUID(), 0, order_key);
		//List<TradeUnit> rlt = es.getTradeInfo(se.bs.getUID(), 0, "earn_percent desc");
		if(flag && rlt!=null && rlt.size()>0){
			es.Report(rlt);
		}else{
			bs.init();
			
			StockManager sm = StockManager.getInstance();
			ArrayList<StockInfo> stocks = sm.FetchStockInfo(false, null, -1);
			ParallelManager pm = ParallelManager.getInstance();
			for(StockInfo item : stocks){
				pm.submitTask(new StockParallelEmuTradeTask(item, bs));
			}
			pm.startTask(this, 8);
			pm.join();
			Log.v(TAG, "+++++++++++++++++join Done!+++++++++++++++");
		}
		
	}
	
	public static void main(String[] args){

		PreEmuProcesser se = new PreEmuProcesser();
		se.run(false);
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onComplete ");
/*		EmuTradeSystem es = EmuTradeSystem.getInstance();
		List<TradeUnit> ret = es.getTradeInfo(bs.getUID(), 0, order_key);
		es.Report(ret);*/
	}

}
