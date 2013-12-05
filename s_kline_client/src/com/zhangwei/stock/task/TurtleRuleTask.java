package com.zhangwei.stock.task;

import java.util.List;

import com.zhangwei.stock.Constants;
import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.daygenerater.EmuStockDayGenerater;
import com.zhangwei.stock.manager.EmuAssertManager;
import com.zhangwei.stock.status.AssertStatus;
import com.zhangwei.stock.strategy.BasicStrategy;
import com.zhangwei.stock.strategy.TurtleRuleStrategy;
import com.zhangwei.util.DateHelper;

public class TurtleRuleTask implements StockTask {
	
	private String stock_id;
	private int market_type;
	private BasicStrategy bs;
	private EmuStockDayGenerater dayGen;
	private EmuAssertManager assetManager;
	private AssertStatus status;
	private Stock stock;

	public TurtleRuleTask(String MarketID, String stock_id, int market_type){
		this.stock_id = stock_id;
		this.market_type = market_type;
		this.bs = new TurtleRuleStrategy(MarketID);
		this.dayGen = new EmuStockDayGenerater(stock_id, market_type);
		this.assetManager = new EmuAssertManager(1000000);
		this.status = AssertStatus.EMPTY;
		this.stock = StockManager.getInstance().getStock(stock_id, market_type);
	}

	@Override
	public void processTask() {
		// TODO Auto-generated method stub
		int today = dayGen.getToday();
		while(DateHelper.checkVaildDate(today)){
			List<KLineUnit> kl = stock.generateNDayKlineToNow(Constants.BUYPOINT_PREFIX_LEN, today);
			if(status==AssertStatus.EMPTY || status==AssertStatus.PART){
				if(bs.checkBuy(stock.info, kl, null)){
					BuyPoint buypoint;
					assetManager.buy(buypoint);
				}
				

			}
				
			
			if(status==AssertStatus.FULL || status==AssertStatus.PART){
				if(bs.checkSell(stock.info, kl, null)){
					SellPoint sellpoint;
					assetManager.sell(sellpoint);
				}
				

			}
			


		
			
			today = dayGen.getToday();
		}
		
	}

}
