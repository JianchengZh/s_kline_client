package com.zhangwei.stock.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.zhangwei.stock.BS.HoldUnit;
import com.zhangwei.util.StockHelper;

public class EmuAssertManager implements AssertManagerMethods{
	private int money_left;
	private int total_asset_init;
	private HashMap<String, HoldUnit> holds;
	private ArrayList<HoldUnit> sold_holds;
	
	public EmuAssertManager(int money){
		this.money_left = money;
		this.total_asset_init = money;
		this.holds = new HashMap<String, HoldUnit>();
		this.sold_holds = new ArrayList<HoldUnit>();
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
	
	

}