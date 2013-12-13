package com.zhangwei.stock.manager;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import com.zhangwei.stock.bs.HoldUnit;


public interface IAssertManager {
	public boolean canBuy(String stock_id, int market_type, int buy_price, int buy_vol);
	
	public void buyIn(String stock_id, int market_type, int date, int buy_price, int buy_vol);
	
	public boolean canSell(String stock_id, int market_type, int date);
	
	public void sellOut(String stock_id, int market_type, int date, int sell_price, int sell_vol);
    
	public int getLeftMoney();
    
	public int getStockValue(int date);
	
	public int geteTotalAsset(int date);
	
	public Set<Entry<String, HoldUnit>> getHoldList();
	
	public void BuyDone(String stock_id, int market_type, int date, int buy_price, int buy_vol);
	
	public void SellDone(String stock_id, int market_type, int buy_date, int sell_date, int buy_price, int sell_price, int sell_vol);

}
