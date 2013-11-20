package com.zhangwei.stock.emu;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.Strategy.BasicStrategy;
import com.zhangwei.stock.Strategy.MyHighSellLowBuyStrategy;
import com.zhangwei.stock.task.StockEmuTradeTask;
import com.zhangwei.stock.task.StockUpdateTask;

public class SerialEmuMarket {
	private static final String TAG = "SerialEmuMarket";

	public static void main(String[] args){
		StockManager sm = StockManager.getInstance();
/*		Stock s = mm.getStock(new StockInfo("600031", 1, "SYZG", -1, -1, "三一重工"), false);
		Stock s2 = mm.getStock(new StockInfo("002572", 2, "SFY", -1, -1, "索菲亚"), false);
		s.getNDayExRightKline(1000, 0);*/
		BasicStrategy bs = new MyHighSellLowBuyStrategy();
		bs.init();
		ArrayList<StockInfo> stocks = sm.FetchStockInfo(false);
		for(StockInfo item : stocks){
			ParallelManager.getInstance().submitTask(new StockEmuTradeTask(item, bs));
		}
		
		EmuTradeSystem.getInstance().getReport(bs.getUID());
		
		
	}

}
