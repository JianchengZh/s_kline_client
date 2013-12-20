package com.zhangwei.stock.emu;

import java.util.ArrayList;

import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.parallel.ParallelManager;
import com.zhangwei.stock.task.BuyCheckTask;
import com.zhangwei.stock.task.TurtleRuleTask;

public class EmuTurtleRuleMarket {
	private static final String TAG = "EmuTurtleRuleMarket";

	public static final String UID = "EmuTurtleRuleMarket";

	public void run(){
		StockManager sm = StockManager.getInstance();
		ArrayList<StockInfo> stockinfos = sm.FetchStockInfo(false, null, -1);
		ParallelManager pm = new ParallelManager();//ParallelManager.getInstance();
		int count=1;
		for(StockInfo item : stockinfos){
			if(count==0){
				break;
			}
			count--;
			pm.submitTask(new TurtleRuleTask(UID, item.stock_id, item.market_type));
		}
		pm.startTask(null, 1);
		pm.join();
		
	}
	
	public static void main(String[] args){
		
		EmuTurtleRuleMarket er = new EmuTurtleRuleMarket();
		er.run();
	}
}
