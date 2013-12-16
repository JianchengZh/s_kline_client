package com.zhangwei.stock.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zhangwei.stock.Constants;
import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.IBuyCallBack;
import com.zhangwei.stock.bs.ISellCallBack;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.daygenerater.EmuStockDayGenerater;
import com.zhangwei.stock.manager.EmuAssertManager;
import com.zhangwei.stock.status.AssertStatus;
import com.zhangwei.stock.status.TradeUnitStatus;
import com.zhangwei.stock.status.TurtleRuleTaskStatus;
import com.zhangwei.stock.strategy.BasicStrategy;
import com.zhangwei.stock.strategy.TurtleRuleStrategy;
import com.zhangwei.util.DateHelper;
import com.zhangwei.util.StockHelper;

public class TurtleRuleTask implements StockTask, ISellCallBack, IBuyCallBack {
	
	private String stock_id;
	private int market_type;
	private BasicStrategy bs;
	private EmuStockDayGenerater dayGen;
	private EmuAssertManager assetManager;
	private TurtleRuleTaskStatus status;
	private Stock stock;
	private ArrayList<BuyPoint> to_buys;
	private ArrayList<HoldUnit> to_sells;
	
	public final static int InitMoney = 1000000;
	public final static int lossPercentFactor = 1;
	public final static int maxUnitFactor = 4;
	
	int today = 0;

	public TurtleRuleTask(String MarketID, String stock_id, int market_type){
		this.stock_id = stock_id;
		this.market_type = market_type;
		this.bs = new TurtleRuleStrategy(MarketID);
		this.dayGen = new EmuStockDayGenerater(stock_id, market_type);
		this.assetManager = new EmuAssertManager(InitMoney, bs.getBSTableName());
		this.status = TurtleRuleTaskStatus.EMPTY;
		this.stock = StockManager.getInstance().getStock(stock_id, market_type);
		
		assetManager.setSellCallBackListener(this);
		
		to_buys = new ArrayList<BuyPoint>();
		to_sells = new ArrayList<HoldUnit>();
	}

	@Override
	public void processTask() {
		// TODO Auto-generated method stub
		today = dayGen.getToday();
		while(DateHelper.checkVaildDate(today)){
			List<KLineUnit> kl = stock.generateNDayKlineToNow(Constants.BUYPOINT_PREFIX_LEN, today);
			if(kl!=null && kl.size()>=Constants.BUYPOINT_PREFIX_LEN){
				kl.remove(kl.size()-1);
				KLineUnit last = kl.get(kl.size()-1);
				
				//TR（实际范围）=max(H-L,H-PDC,PDC-L)
	            //calcN 采用前向复权方式
				int N = StockHelper.calcN(kl); //单位分
				
				//最大单位数=帐户的1%/(N×每点价值量)
				int VolatilityValue = N * 100; //单位元
				int N2 = StockHelper.calcN(kl); //单位分
				int maxUnitNum = InitMoney * lossPercentFactor / VolatilityValue;
				
				if(maxUnitNum<1){
					maxUnitNum = 1;//每单位至少要1手
				}
				
				if(status==TurtleRuleTaskStatus.EMPTY){
					if(bs.checkBuy(stock.info, kl, null)){
						ArrayList<BuyPoint> buys = getBuys(bs.getUID(), maxUnitFactor, maxUnitNum, N, last);
						//将各个买进的点都挂单
						to_buys.clear();
						to_buys.addAll(buys); // put it to tomorrow
						
						status = TurtleRuleTaskStatus.BUYING;
					}
					

				}else if(status==TurtleRuleTaskStatus.BUYING){
					//to do the work left
					if(to_buys.size()>0){
						for(BuyPoint elem : to_buys){
							assetManager.buy(elem);
						}
					}
					
					if(to_sells.size()>0){
						for(HoldUnit elem : to_sells){
							elem.sell_date = today;
							assetManager.sell(elem); //止损挂单
						}
					}
					
					if(bs.checkSell(stock.info, kl, null)){
						status = TurtleRuleTaskStatus.SELLING;
					}
					
					
				}else if(status==TurtleRuleTaskStatus.SELLING){
					to_buys.clear();
					
					if(to_sells.size()>0){
						for(HoldUnit elem : to_sells){
							elem.sell_date = today;
							elem.force_sell = true;
							assetManager.sell(elem); //离市
						}
					}
				}
			}
			

			
			today = dayGen.getToday();
		}
		
	}

	private ArrayList<BuyPoint> getBuys(String BSID, int maxunitfactor, int maxUnitNum, int N, KLineUnit last) {
		// TODO Auto-generated method stub
		ArrayList<BuyPoint> rlt = new ArrayList<BuyPoint>();
		for(int index=0; index<maxunitfactor; index++){
			rlt.add(new BuyPoint(BSID, stock_id, market_type, last.date, 0, last.close + index*N/2, maxUnitNum*100, last));
		}
		return rlt;
	}

	@Override
	public boolean onBuySucess(String stock_id, int market_type, int date,
			int buy_price, int buy_vol) {
		// TODO Auto-generated method stub
		Iterator<BuyPoint> iterator = to_buys.iterator();
		while(iterator.hasNext()){
			BuyPoint bp = iterator.next();
			if(bp.price==buy_price){
				iterator.remove();
				break;
			}
		}
		
		to_sells.add(new HoldUnit(stock_id, market_type, today, buy_price, buy_vol));
		
		if(status==TurtleRuleTaskStatus.EMPTY){
			status = TurtleRuleTaskStatus.BUYING;
		}

		
		return false;
	}

	@Override
	public boolean onBuyCancel(String stock_id, int market_type, int date,
			int buy_price, int buy_vol) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSellSucess(String stock_id, int market_type, int buy_date, int sell_date,
			int buy_price, int sell_price, int sell_vol) {
		// TODO Auto-generated method stub
		Iterator<HoldUnit> iterator = to_sells.iterator();
		while(iterator.hasNext()){
			HoldUnit hu = iterator.next();
			if(hu.buy_date==buy_date && hu.buy_price==buy_price){
				iterator.remove();
			}
		}
		
		if(to_sells.isEmpty()){
			status = TurtleRuleTaskStatus.EMPTY;
		}
		
		
		return false;
	}

	@Override
	public boolean onSellCancel(String stock_id, int market_type, int date,
			int sell_price, int sell_vol) {
		// TODO Auto-generated method stub
		return false;
	}


}
