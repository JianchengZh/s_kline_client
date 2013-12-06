package com.zhangwei.stock.task;

import java.util.ArrayList;
import java.util.List;

import com.zhangwei.stock.Constants;
import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.daygenerater.EmuStockDayGenerater;
import com.zhangwei.stock.manager.EmuAssertManager;
import com.zhangwei.stock.status.AssertStatus;
import com.zhangwei.stock.strategy.BasicStrategy;
import com.zhangwei.stock.strategy.TurtleRuleStrategy;
import com.zhangwei.util.DateHelper;
import com.zhangwei.util.StockHelper;

public class TurtleRuleTask implements StockTask {
	
	private String stock_id;
	private int market_type;
	private BasicStrategy bs;
	private EmuStockDayGenerater dayGen;
	private EmuAssertManager assetManager;
	private AssertStatus status;
	private Stock stock;
	private ArrayList<BuyPoint> to_buys;
	private ArrayList<HoldUnit> to_sells;
	private boolean out_market;
	
	public final static int InitMoney = 1000000;

	public TurtleRuleTask(String MarketID, String stock_id, int market_type){
		this.stock_id = stock_id;
		this.market_type = market_type;
		this.bs = new TurtleRuleStrategy(MarketID);
		this.dayGen = new EmuStockDayGenerater(stock_id, market_type);
		this.assetManager = new EmuAssertManager(InitMoney);
		this.status = AssertStatus.EMPTY;
		this.stock = StockManager.getInstance().getStock(stock_id, market_type);
		
		to_buys = new ArrayList<>();
		to_sells = new ArrayList<>();
		out_market = false;
	}

	@Override
	public void processTask() {
		// TODO Auto-generated method stub
		int today = dayGen.getToday();
		while(DateHelper.checkVaildDate(today)){
			List<KLineUnit> kl = stock.generateNDayKlineToNow(Constants.BUYPOINT_PREFIX_LEN, today);
			KLineUnit last = kl.get(kl.size()-1);
			
			//TR（实际范围）=max(H-L,H-PDC,PDC-L)

			int N = StockHelper.calcN(kl);
			
			//最大单位=帐户的1%/(N×每点价值量)
			int VolatilityValue = N * last.close; //单位元
			int maxUnitNum = InitMoney /100 / VolatilityValue;
			
			if(maxUnitNum<1){
				maxUnitNum = 1;
			}
			
			//to do the work left
			if(to_buys.size()>0){
				for(BuyPoint elem : to_buys){
					assetManager.buy(elem);
				}
			}
			
			if(to_sells.size()>0){
				for(HoldUnit elem : to_sells){
					if(out_market){
						elem.to_sell_price = last.open;
					}
					assetManager.sell(elem); //止损挂单 或 离市
				}
			}

			if(status==AssertStatus.EMPTY || status==AssertStatus.PART){
				if(bs.checkBuy(stock.info, kl, null)){
					BuyPoint buypoint = new BuyPoint(bs.getUID(), stock_id, today, today, 0, last.close, last.vol, last);
					to_buys.add(buypoint); // put it to tomorrow
				}
				

			}
				
			
			if(status==AssertStatus.FULL || status==AssertStatus.PART){
				if(bs.checkSell(stock.info, kl, null)){
					out_market = true;
				}
				

			}
			


		
			
			today = dayGen.getToday();
		}
		
	}

}
