package com.zhangwei.stock.record;

import com.zhangwei.util.StockHelper;

public class TradeRecord {
	public String stock_id;
	public int market_type;
	public int buy_total;
	public int sell_total;
	public int vol_total;
	public int vol_cansell;
	
	/**
	 * 从sql中加载时调用，恢复以前的数据到内存
	 * <br>注意：该数据可能有误，需要进行guosen校对
	 * */
	public TradeRecord(String stock_id, int market_type, int buy_total,
			int sell_total, int vol, int vol_cansell) {
		// TODO Auto-generated constructor stub
		this.stock_id = stock_id;
		this.market_type = market_type;
		this.buy_total = buy_total;
		this.sell_total = sell_total;
		this.vol_total = vol;
		this.vol_cansell = vol_cansell;
	}
	
	/**
	 * 第一次买入时调用
	 * */
	public TradeRecord(String stock_id, int market_type, int buy_price,
			int buy_vol) {
		// TODO Auto-generated constructor stub
		this.stock_id = stock_id;
		this.market_type = market_type;
		this.buy_total = buy_price*buy_vol;
		this.buy_total += StockHelper.calcCircaCost(buy_total, 15, 10000);
		this.sell_total = 0;
		this.vol_total = buy_vol;
		this.vol_cansell = 0;
	}


	/**
	 * 返回买入的总花费（单位分），成本加佣金
	 * */
	public int buyIn(int buy_price, int buy_vol) {
		// TODO Auto-generated method stub
		int value = buy_price * buy_vol;
		value += StockHelper.calcCircaCost(value, 15, 10000);
		this.buy_total += value;
		this.vol_total += buy_vol;
		
		return value;
	}
	
	/**
	 * 返回卖出的总收入（单位分），卖出资金减佣金
	 * */
	public int sellOut(int sell_price, int sell_vol) {
		// TODO Auto-generated method stub
		int value = sell_price * sell_vol;
		int yongjin = StockHelper.calcCircaCost(value, 15, 10000) + StockHelper.calcCircaCost(value, 1, 1000);
		this.sell_total += value - yongjin;
		this.vol_total -= sell_vol;
		this.vol_cansell -= sell_vol;
		return value - yongjin;
	}
	
	/**
	 * 返回可卖出的股票数量，单位股
	 * */
	public int canSell(){
		return vol_cansell;
	}
	
	/**
	 * <br>每天开盘后，交易前进行一次持仓数量校对，应对扩股情况
	 * <br>同时将可卖数量重置
	 * */
	public void adjust_vol(int vol){
		this.vol_total = vol;
		this.vol_cansell = vol;
	}
}
