package com.zhangwei.stock.task;

import java.util.List;

import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.BS.SellPoint;
import com.zhangwei.stock.Strategy.BasicStrategy;
import com.zhangwei.stock.Strategy.MyHighSellLowBuyStrategy;
import com.zhangwei.stock.emu.EmuBuyTransaction;
import com.zhangwei.stock.emu.EmuSellTransaction;
import com.zhangwei.stock.emu.EmuTradeSystem;
import com.zhangwei.util.StockHelper;

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
		
		BasicStrategy bs = new MyHighSellLowBuyStrategy();
		
		List<KLineUnit> kl;
		int status = 0; //0 empty  1 hold
		BuyPoint lastBuyPoint = null;
		SellPoint lastSellPoint = null;
		while((kl = stock.generateNDayKline(60, false))!=null){
			List<KLineUnit> exRightKl = StockHelper.getExrightKLine(kl);
			if(status==0){
				boolean canBuy = bs.checkBuy(info, exRightKl, lastSellPoint);
				if(canBuy){
					KLineUnit last = exRightKl.get(exRightKl.size()-1);
					int date = last.date;
					int price = last.close;
					//Log.v(TAG, "BuyPoint: stock:" + info.stock_id + ", date:" + date + ", price:" + price);
					EmuBuyTransaction ebt = new EmuBuyTransaction();
					ebt.buy(info, date, 0, price, 100);
					lastBuyPoint = new BuyPoint(info, date, 0, price, 100);
					status = 1;
				}
				
			}else if(status==1){
				boolean canSell = bs.checkSell(info, exRightKl, lastBuyPoint);
				if(canSell){
					KLineUnit last = exRightKl.get(exRightKl.size()-1);
					int date = last.date;
					int price = last.close;
					//Log.v(TAG, "SellPoint: stock:" + info.stock_id + ", date:" + date + ", price:" + price);
					EmuSellTransaction est = new EmuSellTransaction();
					est.sell(info, date, 0, price, 100);
					lastSellPoint = new SellPoint(info, date, 0, price, 100);
					status = 0;
				}
			}
		}
	}

}