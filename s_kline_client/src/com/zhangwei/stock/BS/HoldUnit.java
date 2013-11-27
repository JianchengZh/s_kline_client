package com.zhangwei.stock.BS;

import com.zhangwei.util.StockHelper;

/**
 * 真实的价格和数量，不复权，TradeUnit的价格为复权后的价格
 * */
public class HoldUnit {
	private String stock_id;
	private int market_type;
	private int buy_date; 
	private int buy_price;  //买入价 分
	private int buy_vol;  //考虑复权因素，buy_vol可能和sell_vol不一样
	private int buy_yongjin; //买入佣金,单位 分
	
	private boolean sold; //是否卖出
	//public int hold_price; //当前的价格
	//public int hold_vol;   //当前的数量
	
	private int sell_date;
	private int sell_price;
	private int sell_vol;
	private int sell_yongjin; //买入佣金,单位 分
	
	public HoldUnit(BuyPoint bp, int vol){
		this.stock_id = bp.info.stock_id;
		this.market_type = bp.info.market_type;
		this.buy_date = bp.date;
		this.buy_price = bp.price;
		this.buy_vol = vol;
		//this.buy_yongjin = (150 * buy_price * buy_vol + 50000)/ 100000;
		this.buy_yongjin = StockHelper.calcCircaCost(buy_price * buy_vol, 15, 10000);
	}
	
	/**
	 * 1. 真实系统从券商更新
	 * 2. 模拟系统不考虑
	 * */
/*	public void update(int now_vol){
		//this.hold_price = now_price;
		this.hold_vol = now_vol;
	}*/
	
	/**
	 * 全部卖出, 卖出  印花税0.001 + 手续费0.0015
	 * */
	public void sell(int sell_date, int sell_price, int sell_vol){
		this.sell_date = sell_date;
		this.sell_price = sell_price;
		this.sell_vol = sell_vol;
		//this.hold_price = 0;
		//this.hold_vol = 0;
		
		int sell_value = sell_price*sell_vol;
		sell_yongjin = StockHelper.calcCircaCost(sell_value, 1, 1000) + StockHelper.calcCircaCost(sell_value, 15, 10000);
		sold = true;
	}
	
	/**
	 *  得到买入成本 ， 买入  印花税0 手续费0.0015
	 *  买入佣金在买入时（构造函数），就计算出来
	 * */
	public int getBuyCost(){
		return buy_price * buy_vol + buy_yongjin;
	}
	
	/**
	 * 得到卖出的金额，卖出佣金在卖出时，计算出来
	 * */
	public int getSellAsset(){
		if(sold){
			return sell_price*sell_vol - sell_yongjin;
		}else{
			return -1;
		}
	}
	
	/**
	 * 获得盈利百分比（单位：%），考虑交易佣金
	 * */
	public int getEarnPercent(){
		if(sold){
			int sold_value = getSellAsset();
			int bought_value = getBuyCost();
			return (sold_value-bought_value)*100/bought_value;
		}else{
			return -1;
		}
	}
	
	/**
	 * 获得盈利金额（单位：分），考虑交易佣金
	 * */
	public int getEarnMoney(){
		if(sold){
			int sold_value = getSellAsset();
			int bought_value = getBuyCost();
			return (sold_value-bought_value);
		}else{
			return -1;
		}
	}

}
