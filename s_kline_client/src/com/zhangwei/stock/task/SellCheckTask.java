package com.zhangwei.stock.task;


import java.util.Iterator;
import java.util.List;


import com.zhangwei.stock.Constants;
import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockInfoManager;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.emu.ParallelEmuMarket;
import com.zhangwei.stock.strategy.BasicStrategy;
import com.zhangwei.stock.transaction.EmuBuyTransaction;
import com.zhangwei.stock.transaction.EmuSellTransaction;
import com.zhangwei.util.StockHelper;

public class SellCheckTask implements StockTask {

	private static final String TAG = "SellCheckTask";
	private BasicStrategy bs;
	private ITaskSellResultCheck taskResultCheck;
	private HoldUnit hu;

	public SellCheckTask(ITaskSellResultCheck taskResultCheck, HoldUnit hu, BasicStrategy bs, int today){
		this.hu = hu;
		this.bs = bs;
		this.taskResultCheck = taskResultCheck;
	}
	
	@Override
	public void processTask() {
		// TODO Auto-generated method stub
		StockManager sm = StockManager.getInstance();
		Stock stock = sm.getStock(hu.stock_id, hu.market_type);
		
		if(stock==null){
			return;
		}
		
		List<KLineUnit> kl_sell = stock.generateNDayKline(Constants.BUYPOINT_PREFIX_LEN, false, hu.buy_date);
		List<KLineUnit> exRightKl_sell = StockHelper.getExrightKLine(kl_sell, hu.buyDate());
		
		KLineUnit last = exRightKl_sell.get(exRightKl_sell.size()-1);
		KLineUnit last_2 = exRightKl_sell.get(exRightKl_sell.size()-2);
		BuyPoint bp = new BuyPoint(bs.getUID(), hu, last);
		
		boolean canSell = bs.checkSell(stock.info, exRightKl_sell, bp);
		if(canSell){
			int date = last.date;
			int price = last.close;
			//Log.v(TAG, "SellPoint: stock:" + info.stock_id + ", date:" + date + ", price:" + price);
			boolean isDownBan = last.isDownBan(exRightKl_sell.get(exRightKl_sell.size()-2));
			if(!isDownBan){
				//EmuSellTransaction est = new EmuSellTransaction();
				//SellPoint lastSellPoint = new SellPoint(bs.getUID(), stock.info, date, 0, price, bp.vol, last_2);
				//est.sell(stock.info, bp, lastSellPoint);
				taskResultCheck.check(hu);

			}
		}

	}

}
