package com.zhangwei.stock.emu;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.TradeUnit;
import com.zhangwei.stock.Strategy.BasicStrategy;
import com.zhangwei.stock.Strategy.MyHighSellLowBuyStrategy;
import com.zhangwei.stock.Strategy.MyWeakBuyStrategy;
import com.zhangwei.stock.task.StockEmuTradeTask;
import com.zhangwei.stock.task.StockUpdateTask;

public class SerialEmuMarket implements ParallelListener {
	private static final String TAG = "SerialEmuMarket";
	public  BasicStrategy bs;

	public static void main(String[] args){
		
		SerialEmuMarket se = new SerialEmuMarket();
		//se.bs = new MyHighSellLowBuyStrategy();
		se.bs = new MyWeakBuyStrategy();
		EmuTradeSystem es = EmuTradeSystem.getInstance();
		List<TradeUnit> rlt = es.getTradeInfo(se.bs.getUID(), -1);
		if(rlt!=null && rlt.size()>0){
			es.Report(rlt);
		}else{
			se.bs.init();
			
			StockManager sm = StockManager.getInstance();
			ArrayList<StockInfo> stocks = sm.FetchStockInfo(false, null, -1);
			ParallelManager pm = ParallelManager.getInstance();
			for(StockInfo item : stocks){
				pm.submitTask(new StockEmuTradeTask(item, se.bs));
			}
			pm.startTask(se, 8);
		}
		


		
		
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onComplete ");
		EmuTradeSystem es = EmuTradeSystem.getInstance();
		List<TradeUnit> ret = es.getTradeInfo(bs.getUID(), -1);
		es.Report(ret);
	}

}
