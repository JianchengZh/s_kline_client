package com.zhangwei.stock.task;

import java.util.Iterator;
import java.util.List;


import com.zhangwei.stock.Constants;
import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.emu.ParallelEmuMarket;
import com.zhangwei.stock.strategy.BasicStrategy;
import com.zhangwei.util.StockHelper;

public class StockParallelEmuTradeTask implements StockTask {

	private static final String TAG = "StockParallelEmuTradeTask";
	private StockInfo info;
	private BasicStrategy bs;

	public StockParallelEmuTradeTask(StockInfo info, BasicStrategy bs){
		this.info = info;
		this.bs = bs;
	}
	
	@Override
	public void processTask() {
		// TODO Auto-generated method stub
		StockManager sm = StockManager.getInstance();
		Stock stock = sm.getStock(info, false);
		stock.generateNDayKline(Constants.BUYPOINT_PREFIX_LEN, true, -1);

		
		List<KLineUnit> kl;

		BuyPointQueue queue = new BuyPointQueue();
		
		while((kl = stock.generateNDayKline(Constants.BUYPOINT_PREFIX_LEN, false, -1))!=null){

			List<KLineUnit> exRightKl = StockHelper.getForwardExrightKLine(kl);
			
			//check buy
			boolean canBuy = bs.checkBuy(info, exRightKl, null);
			if(canBuy){
				KLineUnit last = exRightKl.get(exRightKl.size()-1);
				int date = last.date;
				int price = last.close;

				boolean isUpBan = last.isUpBan(exRightKl.get(exRightKl.size()-2));
				if(!isUpBan){
/*					EmuBuyTransaction ebt = new EmuBuyTransaction();
					BuyPoint lastBuyPoint = new BuyPoint(bs.getUID(), info.stock_id, info.market_type, date, 0, price, 100, exRightKl.get(exRightKl.size()-2));
					ebt.buy(lastBuyPoint);
					queue.addBuyPoint(lastBuyPoint);*/
				}

			}
			
			//check sell
			if (queue.getBPList().size() > 0) {
				Iterator<BuyPoint> it = queue.getBPList().iterator();
				while (it.hasNext()) {
					BuyPoint bp = it.next();
					List<KLineUnit> kl_sell = stock.generateNDayKline(Constants.BUYPOINT_PREFIX_LEN, false, bp.date);
					List<KLineUnit> exRightKl_sell = StockHelper.getExrightKLine(kl_sell, bp.date);
					boolean canSell = bs.checkSell(info, exRightKl_sell, bp);
					if(canSell){
						KLineUnit last = exRightKl_sell.get(exRightKl_sell.size()-1);
						int date = last.date;
						int price = last.close;
						//Log.v(TAG, "SellPoint: stock:" + info.stock_id + ", date:" + date + ", price:" + price);
						boolean isDownBan = last.isDownBan(exRightKl_sell.get(exRightKl_sell.size()-2));
						if(!isDownBan){
/*							EmuSellTransaction est = new EmuSellTransaction();
							SellPoint lastSellPoint = new SellPoint(bs.getUID(), bp.stock_id, bp.market_type, date, 0, price, bp.vol, exRightKl.get(exRightKl.size()-2));
							HoldUnit hu = new HoldUnit(bp.stock_id, bp.market_type, bp.date, bp.price, bp.vol);
							est.sell(lastSellPoint, hu);*/
							
							//queue.removeBuyPoint(bp);
							it.remove();
						}
					}
					
					
				}

				
			}
			
			stock.nextPos();

		}
	}

}
