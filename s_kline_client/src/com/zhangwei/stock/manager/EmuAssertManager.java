package com.zhangwei.stock.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.zhangwei.stock.Stock;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.IBuy;
import com.zhangwei.stock.bs.IBuyCallBack;
import com.zhangwei.stock.bs.ISell;
import com.zhangwei.stock.bs.ISellCallBack;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.record.EmuTradeRecorder;
import com.zhangwei.stock.tradesystem.EmuTradeSystem;
import com.zhangwei.stock.tradesystem.ITradeSystem;
import com.zhangwei.util.StockHelper;

public class EmuAssertManager implements IAssertManager, IBuy, ISell{
	private int money_left;
	private int total_asset_init;
	private HashMap<String, HoldUnit> holds;
	private ArrayList<HoldUnit> sold_holds;
	private ITradeSystem tradeSystem;
	private ISellCallBack iSellCallBack;
	private IBuyCallBack iBuyCallBack;
	private EmuTradeRecorder emuTR;
	
	public EmuAssertManager(int money, String bsTableName){
		this.money_left = money;
		this.total_asset_init = money;
		this.holds = new HashMap<String, HoldUnit>();
		this.sold_holds = new ArrayList<HoldUnit>();
		this.tradeSystem = new EmuTradeSystem(this);
		this.emuTR = new EmuTradeRecorder(bsTableName);
		//updateAssetAndHoldsFromDZH();
	}
	
	
//	public void updateAssetAndHoldsFromDZH(){
//		
//	}

	@Override
	public boolean canBuy(Stock stock, int buy_price, int buy_vol) {
		// TODO Auto-generated method stub
		int stock_value = buy_price * buy_vol;
		int need_value = stock_value + StockHelper.calcCircaCost(stock_value, 15, 10000);
		
		if(money_left>need_value){
			HoldUnit elem = holds.get(stock.info.stock_id);
			if(elem==null){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public void buyIn(Stock stock, int date,
			int buy_price, int buy_vol) {
		// TODO Auto-generated method stub
		holds.put(stock.info.stock_id, new HoldUnit(stock.info.stock_id, stock.info.market_type, date, buy_price, buy_vol));
	}

	@Override
	public boolean canSell(Stock stock, int date) {
		// TODO Auto-generated method stub
		HoldUnit elem = holds.get(stock.info.stock_id);
		if(elem!=null && !elem.isSold() && elem.buyDate()<date){
			return true;
		}
		
		return false;
	}

	@Override
	public void sellOut(Stock stock, int date,
			int sell_price, int sell_vol) {
		// TODO Auto-generated method stub
		HoldUnit elem = holds.get(stock.info.stock_id);
		if(elem!=null){
			holds.remove(stock.info.stock_id);
			elem.sell(date, sell_price, sell_vol);
			sold_holds.add(elem);
		}
	}

	@Override
	public int getLeftMoney() {
		// TODO Auto-generated method stub
		return money_left;
	}

	@Override
	public int getStockValue(int date) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<Entry<String, HoldUnit>> getHoldList() {
		// TODO Auto-generated method stub
		return holds.entrySet();
	}

	@Override
	public int geteTotalAsset(int date) {
		// TODO Auto-generated method stub
		return getStockValue(date) + money_left;
	}


	public ArrayList<BuyPoint> getCanBuys(ArrayList<BuyPoint> theBestBuy) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public boolean sellCancel(SellPoint sp, HoldUnit hu) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean buy(Stock stock, BuyPoint buypoint) {
		// TODO Auto-generated method stub
		tradeSystem.submitBuyTransaction(stock, buypoint);
		return true;
	}

	@Override
	public boolean buyCancel(BuyPoint buypoint) {
		// TODO Auto-generated method stub
		return false;
	}


	public void requestBuy(Map<String, BuyPoint> buys) {
		// TODO Auto-generated method stub
		
	}


	public void requestSell(Map<String, HoldUnit> holds) {
		// TODO Auto-generated method stub
		
	}


	public boolean sell(Stock stock, HoldUnit elem) {
		// TODO Auto-generated method stub
		tradeSystem.submitSellTransaction(stock, elem);
		return false;
	}


	public void setSellCallBackListener(ISellCallBack iSellCallBack) {
		// TODO Auto-generated method stub
		this.iSellCallBack = iSellCallBack;
	}

	public void setBuyCallBackListener(IBuyCallBack iBuyCallBack) {
		// TODO Auto-generated method stub
		this.iBuyCallBack = iBuyCallBack;
	}


	@Override
	public void BuyDone(String stock_id, int market_type, int date,
			int buy_price, int buy_vol) {
		// TODO Auto-generated method stub
		emuTR.addBuy(stock_id, market_type, date, buy_price, buy_vol);
		if(iBuyCallBack!=null){
			iBuyCallBack.onBuySucess(stock_id, market_type, date, buy_price, buy_vol);
		}
		
	}


	@Override
	public void SellDone(String stock_id, int market_type, int buy_date,
			int sell_date, int buy_price, int sell_price, int sell_vol) {
		// TODO Auto-generated method stub
		emuTR.addSell(stock_id, market_type, sell_date, sell_price, sell_vol);
		if(iSellCallBack!=null){
			iSellCallBack.onSellSucess(stock_id, market_type, buy_date, sell_date, buy_price, sell_price, sell_vol);
		}
		
	}



}
