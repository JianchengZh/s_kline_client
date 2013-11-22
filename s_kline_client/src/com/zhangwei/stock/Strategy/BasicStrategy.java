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
	public ArrayList<ICondition> buyBigConditions;  //独享条件集合 
	public ArrayList<ICondition> buyLittleConditions; //组合条件集合
	
	public ArrayList<ICondition> sellBigConditions;  //独享条件集合 
	public ArrayList<ICondition> sellLittleConditions; //组合条件集合
	
	private long serialVersionUID;

	public BasicStrategy(long serialversionuid){
		this.serialVersionUID = serialversionuid;
		buyBigConditions = new ArrayList<ICondition>();
		buyLittleConditions = new ArrayList<ICondition>();
		
		sellBigConditions = new ArrayList<ICondition>();
		sellLittleConditions = new ArrayList<ICondition>();
	}
	
	public String getUID(){
		return String.valueOf(serialVersionUID);
	}
	
	public boolean checkBuy(StockInfo info, List<KLineUnit> kl, SellPoint lastPoint){
		KLineTypeResult rlt = null;
		try {
			rlt = StockHelper.getKlineType(kl);
		} catch (StockException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(ICondition c : buyBigConditions){
			try {
				if(c.checkCondition(info, rlt, kl, lastPoint)){
					return true;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(buyLittleConditions.size()>0){
			for(ICondition c : buyLittleConditions){
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
		
		for(ICondition c : sellBigConditions){
			try {
				if(c.checkCondition(info, rlt, kl, lastPoint)){
					return true;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(sellLittleConditions.size()>0){
			for(ICondition c : sellLittleConditions){
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
		if(buyBigConditions.size()==0 && buyLittleConditions.size()==0){
			return false;
		}
		
		if(sellBigConditions.size()==0 && sellLittleConditions.size()==0){
			return false;
		}
		
		return true;
	}
	
	public void addBuyNecessaryCondition(ICondition c){
		buyBigConditions.remove(c);
		buyBigConditions.add(c);
	}
	
	public void addBuySufficientCondition(ICondition c){
		buyLittleConditions.remove(c);
		buyLittleConditions.add(c);
	}
	
	public void addSellNecessaryCondition(ICondition c){
		sellBigConditions.remove(c);
		sellBigConditions.add(c);
	}
	
	public void addSellSufficientCondition(ICondition c){
		sellLittleConditions.remove(c);
		sellLittleConditions.add(c);
	}

	public void init() {
		// TODO Auto-generated method stub

		cleanUp();
		BaseDao bd = BaseDao.getInstance();
		String BStable = "BS_" + getUID();
		
		Log.v(TAG, getClass().getName() + " - init, BStable:" + BStable);
		try {

			String sql_create_table_bs = "CREATE TABLE IF NOT EXISTS " 
			                  + BStable  
			                  + "\n(stock_id VARCHAR(6) NOT NULL, market_type INT, buy_date INT NOT NULL, sell_date INT, buy_price INT, sell_price INT, vol INT, PRIMARY KEY  (stock_id, buy_date))ENGINE=InnoDB DEFAULT CHARSET=utf8;";

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
