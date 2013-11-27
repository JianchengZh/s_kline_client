package com.zhangwei.stock.task;

import java.util.List;

import android.util.Log;

import com.zhangwei.stock.Constants;
import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.Point;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.strategy.BasicStrategy;
import com.zhangwei.stock.strategy.MyHighSellLowBuyStrategy;
import com.zhangwei.stock.tradesystem.EmuTradeSystem;
import com.zhangwei.stock.transaction.EmuBuyTransaction;
import com.zhangwei.stock.transaction.EmuSellTransaction;
import com.zhangwei.util.StockHelper;

public class StockSerialEmuTradeTask implements StockTask {

	private static final String TAG = "StockSerialEmuTradeTask";
	private StockInfo info;
	private BasicStrategy bs;

	public StockSerialEmuTradeTask(StockInfo info, BasicStrategy bs){
		this.info = info;
		this.bs = bs;
	}
	
	@Override
	public void processTask() {
		// TODO Auto-generated method stub
		StockManager sm = StockManager.getInstance();
		Stock stock = sm.getStock(info, false);
		stock.generateNDayKline(60, true, -1);
		
		//BasicStrategy bs = new MyHighSellLowBuyStrategy();
		
		List<KLineUnit> kl;
		int status = 0; //0 empty  1 hold
		BuyPoint lastBuyPoint = null;
		SellPoint lastSellPoint = null;
		int lastBuyDate = -1;
		while((kl = stock.generateNDayKline(Constants.BUYPOINT_PREFIX_LEN, false, lastBuyDate))!=null){
			
			if(status==0){
				List<KLineUnit> exRightKl = StockHelper.getForwardExrightKLine(kl);
				boolean canBuy = bs.checkBuy(info, exRightKl, lastSellPoint);
				if(canBuy){
					KLineUnit last = exRightKl.get(exRightKl.size()-1);
					int date = last.date;
					int price = last.close;
					//Log.v(TAG, "BuyPoint: stock:" + info.stock_id + ", date:" + date + ", price:" + price);

					
					boolean isUpBan = last.isUpBan(exRightKl.get(exRightKl.size()-2));
					if(!isUpBan){
						EmuBuyTransaction ebt = new EmuBuyTransaction();
						lastBuyPoint = new BuyPoint(bs.getUID(), info, date, 0, price, 100, exRightKl.get(exRightKl.size()-2));
						lastBuyDate = lastBuyPoint.date;
						ebt.buy(info, lastBuyPoint);
						status = 1;
					}

				}
				
			}else if(status==1){
				List<KLineUnit> exRightKl = StockHelper.getExrightKLine(kl, lastBuyDate);
				boolean canSell = bs.checkSell(info, exRightKl, lastBuyPoint);
				if(canSell){
					KLineUnit last = exRightKl.get(exRightKl.size()-1);
					int date = last.date;
					int price = last.close;
					//Log.v(TAG, "SellPoint: stock:" + info.stock_id + ", date:" + date + ", price:" + price);
					boolean isDownBan = last.isDownBan(exRightKl.get(exRightKl.size()-2));
					if(!isDownBan){
						EmuSellTransaction est = new EmuSellTransaction();
						lastSellPoint = new SellPoint(bs.getUID(), lastBuyPoint.info, date, 0, price, lastBuyPoint.vol, exRightKl.get(exRightKl.size()-2));
						est.sell(info, lastBuyPoint, lastSellPoint);
						
						status = 0;
					}
					

				}
			}
			
			stock.nextPos();
		}
	}

}
