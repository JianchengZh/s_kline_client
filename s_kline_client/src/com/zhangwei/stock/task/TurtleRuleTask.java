package com.zhangwei.stock.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.util.Log;

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
	private Set<BuyPoint> to_buys;
	private Set<HoldUnit> to_sells;
	
	private Set<BuyPoint> tmp_del_buys;
	private Set<HoldUnit> tmp_add_sells;
	private Set<HoldUnit> tmp_del_sells;
	
	public final static int InitMoney = 1000000;
	public final static int lossPercentFactor = 1;
	public final static int maxUnitFactor = 4;
	private static final String TAG = "TurtleRuleTask";
	
	int today = 0;

	public TurtleRuleTask(String MarketID, String stock_id, int market_type){
		this.stock_id = stock_id;
		this.market_type = market_type;
		this.bs = new TurtleRuleStrategy(MarketID);

		this.assetManager = new EmuAssertManager(InitMoney, bs.getBSTableName());
		this.status = TurtleRuleTaskStatus.EMPTY;
		this.stock = StockManager.getInstance().getStock(stock_id, market_type);
		this.dayGen = new EmuStockDayGenerater(stock);
		
		this.bs.init();
		assetManager.setSellCallBackListener(this);
		assetManager.setBuyCallBackListener(this);
		
		to_buys = new HashSet<BuyPoint>();
		to_sells = new HashSet<HoldUnit>();
		
		tmp_del_buys = new HashSet<BuyPoint>();
		tmp_add_sells = new HashSet<HoldUnit>();
		tmp_del_sells = new HashSet<HoldUnit>();
	}

	@Override
	public void processTask() {
		// TODO Auto-generated method stub
		today = dayGen.getToday();
		while(DateHelper.checkVaildDate(today)){
			List<KLineUnit> kl = stock.generateNDayKlineToNow(Constants.BUYPOINT_PREFIX_LEN, today);
			if(kl!=null && kl.size()>=Constants.BUYPOINT_PREFIX_LEN){
				List<KLineUnit> kl_last = kl.subList(0, kl.size()-1);
				KLineUnit last = kl_last.get(kl_last.size()-1);
				
				//TR（实际范围）=max(H-L,H-PDC,PDC-L)
	            //calcN 采用前向复权方式
				int N = StockHelper.calcN(kl_last); //单位分
				
				if(N<=0){
					today = dayGen.getToday();
					continue;
				}
				
				//最大单位数=帐户的1%/(N×每点价值量)
				int VolatilityValue = N * 100; //单位分
				

				
				int maxUnitNum = InitMoney * lossPercentFactor / VolatilityValue;
				
				if(maxUnitNum<1){
					maxUnitNum = 1;//每单位至少要1手
				}
				
				//del tmp
				if(tmp_del_buys.size()>0){
					to_buys.removeAll(tmp_del_buys);
					tmp_del_buys.clear();
				}

				if(tmp_add_sells.size()>0){
					to_sells.addAll(tmp_add_sells);
					tmp_add_sells.clear();
				}

				if(tmp_del_sells.size()>0){
					to_sells.removeAll(tmp_del_sells);
					tmp_del_sells.clear();
				}
				
				if(to_buys.size()==0 && to_sells.size()==0){
					status = TurtleRuleTaskStatus.EMPTY;
				}

				
				if(to_buys!=null && to_buys.size()>0){
					for(BuyPoint  elem : to_buys){
						elem.date = today;
					}
				}
				
				if(to_sells!=null && to_sells.size()>0){
					for(HoldUnit  elem : to_sells){
						elem.sell_date = today;
					}
				}
				
				if(status==TurtleRuleTaskStatus.EMPTY){
					if(bs.checkBuy(stock.info, kl_last, null)){
						ArrayList<BuyPoint> buys = getBuys(bs.getUID(), maxUnitFactor, maxUnitNum, N, last);
						//将各个买进的点都挂单
						to_buys.clear();
						to_buys.addAll(buys); // put it to tomorrow
						
						status = TurtleRuleTaskStatus.BUYING;
					}
					

				}else if(status==TurtleRuleTaskStatus.BUYING){
					//to do the work left
					if(to_buys.size()>0){
						Iterator<BuyPoint> iterator = to_buys.iterator();
						while(iterator.hasNext()){
							BuyPoint elem = iterator.next();
							assetManager.buy(stock, elem);
						}
					}
					
					if(to_sells.size()>0){
						Iterator<HoldUnit> iterator = to_sells.iterator();
						while(iterator.hasNext()){
							HoldUnit elem = iterator.next();
							assetManager.sell(stock, elem);
						}
					}
					
					if(bs.checkSell(stock.info, kl_last, null)){
						status = TurtleRuleTaskStatus.SELLING;
					}
					
					
				}else if(status==TurtleRuleTaskStatus.SELLING){
					to_buys.clear();
					
					if(to_sells.size()>0){
						for(HoldUnit elem : to_sells){
							elem.sell_date = today;
							elem.force_sell = true;
							assetManager.sell(stock, elem); //离市
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
		Log.v(TAG, "onBuySucess - stock_id:" + stock_id + ", market_type:" + market_type + ", date:" + date + ", buy_price:" + buy_price + ", vol" + buy_vol);
		Iterator<BuyPoint> iterator = to_buys.iterator();
		while(iterator.hasNext()){
			BuyPoint bp = iterator.next();
			if(bp.price==buy_price){
				tmp_del_buys.add(bp);
				break;
			}
		}
		
		tmp_add_sells.add(new HoldUnit(stock_id, market_type, today, buy_price, buy_vol));
		//to_sells.add(new HoldUnit(stock_id, market_type, today, buy_price, buy_vol));
		
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
		Log.v(TAG, "onBuySucess - stock_id:" + stock_id + ", market_type:" + market_type + ", date:" + sell_date + ", sell_price:" + sell_price + ", vol" + sell_vol);
		Iterator<HoldUnit> iterator = to_sells.iterator();
		while(iterator.hasNext()){
			HoldUnit hu = iterator.next();
			if(hu.buy_date==buy_date && hu.buy_price==buy_price){
				//iterator.remove();
				tmp_del_sells.add(hu);
				break;
			}
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
