package com.zhangwei.stock.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.IBuy;
import com.zhangwei.stock.bs.ISell;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.tradesystem.EmuTradeSystem;
import com.zhangwei.stock.tradesystem.ITradeSystem;
import com.zhangwei.util.StockHelper;

public class EmuAssertManager implements IAssertManager, IBuy, ISell{
	private int money_left;
	private int total_asset_init;
	private HashMap<String, HoldUnit> holds;
	private ArrayList<HoldUnit> sold_holds;
	private ITradeSystem tradeSystem;
	
	public EmuAssertManager(int money){
		this.money_left = money;
		this.total_asset_init = money;
		this.holds = new HashMap<String, HoldUnit>();
		this.sold_holds = new ArrayList<HoldUnit>();
		this.tradeSystem = EmuTradeSystem.getInstance();
		//updateAssetAndHoldsFromDZH();
	}
	
	
//	public void updateAssetAndHoldsFromDZH(){
//		
//	}

	@Override
	public boolean canBuy(String stock_id, int market_type, int buy_price, int buy_vol) {
		// TODO Auto-generated method stub
		int stock_value = buy_price * buy_vol;
		int need_value = stock_value + StockHelper.calcCircaCost(stock_value, 15, 10000);
		
		if(money_left>need_value){
			HoldUnit elem = holds.get(stock_id);
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
	public void buyIn(String stock_id, int market_type, int date,
			int buy_price, int buy_vol) {
		// TODO Auto-generated method stub
		holds.put(stock_id, new HoldUnit(stock_id, market_type, date, buy_price, buy_vol));
	}

	@Override
	public boolean canSell(String stock_id, int market_type, int date) {
		// TODO Auto-generated method stub
		HoldUnit elem = holds.get(stock_id);
		if(elem!=null && !elem.isSold() && elem.buyDate()<date){
			return true;
		}
		
		return false;
	}

	@Override
	public void sellOut(String stock_id, int market_type, int date,
			int sell_price, int sell_vol) {
		// TODO Auto-generated method stub
		HoldUnit elem = holds.get(stock_id);
		if(elem!=null){
			holds.remove(stock_id);
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
	public boolean sell(SellPoint sp, HoldUnit hu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sellCancel(SellPoint sp, HoldUnit hu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSellSucess(SellPoint sp, HoldUnit hu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSellCancel(SellPoint sp, HoldUnit hu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean buy(BuyPoint buypoint) {
		// TODO Auto-generated method stub
		tradeSystem.submitBuyTransaction(this, buypoint);
		return true;
	}

	@Override
	public boolean buyCancel(BuyPoint buypoint) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBuySucess(BuyPoint buypoint) {
		// TODO Auto-generated method stub
		tradeSystem.completeBuyTransaction(buypoint);
		return true;
	}

	@Override
	public boolean onBuyCancel(BuyPoint buypoint) {
		// TODO Auto-generated method stub
		return false;
	}


	public void requestBuy(Map<String, BuyPoint> theBestBuy) {
		// TODO Auto-generated method stub
		
	}


	public void requestSell(Map<String, HoldUnit> candidateSellHolds) {
		// TODO Auto-generated method stub
		
	}

}
