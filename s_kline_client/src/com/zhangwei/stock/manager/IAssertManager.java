package com.zhangwei.stock.manager;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import com.zhangwei.stock.Stock;
import com.zhangwei.stock.bs.HoldUnit;


public interface IAssertManager {
	public long getReport(int date);
	
	public boolean canBuy(Stock stock, int buy_price, int buy_vol);
	
	//public void buyIn(Stock stock, int date, int buy_price, int buy_vol);
	
	public boolean canSell(Stock stock, int date);
	
	//public void sellOut(Stock stock, int date, int sell_price, int sell_vol);
    
	public long getLeftMoney();
    
	public long getStockValue(int date);
	
	public long geteTotalAsset(int date);
	
	public Set<Entry<String, HoldUnit>> getHoldList();
	
	public void BuyDone(String stock_id, int market_type, int date, int buy_price, int buy_vol);
	
	public void SellDone(String stock_id, int market_type, int buy_date, int sell_date, int buy_price, int sell_price, int sell_vol);

}
