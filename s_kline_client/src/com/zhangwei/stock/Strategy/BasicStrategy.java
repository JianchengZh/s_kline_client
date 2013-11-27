package com.zhangwei.stock.Strategy;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.BS.SellPoint;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.condition.ICondition;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.StockHelper;

/**
 * 先判断必要条件，若有一个为true，则直接返回true
 * 后判断充分条件, 若全部为true,才返回true
 * */
public abstract class BasicStrategy {
	
	private static final String TAG = "BasicStrategy";
	public ArrayList<ICondition> buyBigYesConditions;  //独享条件集合 
	public ArrayList<ICondition> buyLittleYesConditions; //组合条件集合
	
	public ArrayList<ICondition> buyBigNoConditions;  //独享条件集合 
	//public ArrayList<ICondition> buyLittleNoConditions; //组合条件集合
	
	public ArrayList<ICondition> sellBigYesConditions;  //独享条件集合 
	public ArrayList<ICondition> sellLittleYesConditions; //组合条件集合
	
	public ArrayList<ICondition> sellBigNoConditions;  //独享条件集合 
	//public ArrayList<ICondition> sellLittleNoConditions; //组合条件集合
	
	private long serialVersionUID;
	private String MarketID;
	private String BStable;

	public BasicStrategy(String MarketID, long serialversionuid){
		this.MarketID = MarketID;
		this.serialVersionUID = serialversionuid;
		buyBigYesConditions = new ArrayList<ICondition>();
		buyLittleYesConditions = new ArrayList<ICondition>();
		
		sellBigYesConditions = new ArrayList<ICondition>();
		sellLittleYesConditions = new ArrayList<ICondition>();
		
		buyBigNoConditions = new ArrayList<ICondition>();
		//buyLittleNoConditions = new ArrayList<ICondition>();
		
		sellBigNoConditions = new ArrayList<ICondition>();
		//sellLittleNoConditions = new ArrayList<ICondition>();
	}
	
	public String getBSTableName(){
		return BStable;
	}
	
	public String getUID(){
		return MarketID + String.valueOf(serialVersionUID);
	}
	
	public boolean checkBuy(StockInfo info, List<KLineUnit> kl, SellPoint lastPoint){
		KLineTypeResult rlt = null;
		try {
			rlt = StockHelper.getKlineType(kl);
		} catch (StockException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(ICondition c : buyBigYesConditions){
			try {
				if(c.checkCondition(info, rlt, kl, lastPoint)){
					return true;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(ICondition c : buyBigNoConditions){
			try {
				if(c.checkCondition(info, rlt, kl, lastPoint)){
					return false;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(buyLittleYesConditions.size()>0){
			for(ICondition c : buyLittleYesConditions){
				try {
					if(!c.checkCondition(info, rlt, kl, lastPoint)){
						return false;
					}
				} catch (StockException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}else{
			return false;
		}

		

	}
	
	public boolean checkSell(StockInfo info, List<KLineUnit> kl, BuyPoint lastPoint){
		KLineTypeResult rlt = null;
		try {
			rlt = StockHelper.getKlineType(kl);
		} catch (StockException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(ICondition c : sellBigYesConditions){
			try {
				if(c.checkCondition(info, rlt, kl, lastPoint)){
					return true;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(ICondition c : sellBigNoConditions){
			try {
				if(c.checkCondition(info, rlt, kl, lastPoint)){
					return false;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(sellLittleYesConditions.size()>0){
			for(ICondition c : sellLittleYesConditions){
				try {
					if(!c.checkCondition(info, rlt, kl, lastPoint)){
						return false;
					}
				} catch (StockException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return true;
		}else{
			return false;
		}

	}
	
	public boolean isEmpty(){
		if(buyBigYesConditions.size()==0 && buyLittleYesConditions.size()==0 && buyBigNoConditions.size()==0){
			return true;
		}
		
		if(sellBigYesConditions.size()==0 && sellLittleYesConditions.size()==0 && sellBigNoConditions.size()==0){
			return true;
		}
		
		return false;
	}
	
	public void addBuyBigCondition(ICondition c, boolean yes){
		if(yes){
			buyBigYesConditions.remove(c);
			buyBigYesConditions.add(c);
		}else{
			buyBigNoConditions.remove(c);
			buyBigNoConditions.add(c);
		}

	}
	
	public void addBuyLittleCondition(ICondition c){
		buyLittleYesConditions.remove(c);
		buyLittleYesConditions.add(c);

	}
	
	public void addSellBigCondition(ICondition c, boolean yes){
		if(yes){
			sellBigYesConditions.remove(c);
			sellBigYesConditions.add(c);
		}else{
			sellBigNoConditions.remove(c);
			sellBigNoConditions.add(c);
		}

	}
	
	public void addSellLittleCondition(ICondition c){
		sellLittleYesConditions.remove(c);
		sellLittleYesConditions.add(c);

	}

	public void init() {
		// TODO Auto-generated method stub

		cleanUp();
		BaseDao bd = BaseDao.getInstance();
		BStable = "BS_" + getUID();
		
		Log.v(TAG, getClass().getName() + " - init, BStable:" + BStable);
		try {

			String sql_create_table_bs = "CREATE TABLE IF NOT EXISTS " 
			                  + BStable  
			                  + "\n(stock_id VARCHAR(6) NOT NULL, market_type INT, buy_date INT NOT NULL, sell_date INT, buy_price INT, sell_price INT, vol INT, earn_percent INT, PRIMARY KEY  (stock_id, buy_date))ENGINE=InnoDB DEFAULT CHARSET=utf8;";

			bd.exec(sql_create_table_bs);
			
			String sql_create_index_bs = "CREATE INDEX  buy_date ON " + BStable + " (buy_date);";
			bd.exec(sql_create_index_bs);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void cleanUp(){
		BaseDao bd = BaseDao.getInstance();
		String BStable = "BS_" + getUID();
		try {
			
			String sql_clean_index_bs = "DROP INDEX buy_date ON " + BStable;

			bd.exec(sql_clean_index_bs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			String sql_clean_table_bs = "DROP TABLE " + BStable;

			bd.exec(sql_clean_table_bs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

	}

}
