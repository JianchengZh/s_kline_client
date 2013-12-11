package com.zhangwei.stock.record;

import java.sql.SQLException;

import android.util.Log;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.SellPoint;

public class EmuTradeRecorder {

	private static final String TAG = "EmuTradeRecorder";
	private String stragyID;
	
	public EmuTradeRecorder(String BStable){
		cleanUp(BStable);
		BaseDao bd = BaseDao.getInstance();
		
		Log.v(TAG, getClass().getName() + " - init, BStable:" + BStable);
		try {

			String sql_create_table_bs = "CREATE TABLE IF NOT EXISTS " 
			                  + BStable  
			                  + "\n(stock_id VARCHAR(6) NOT NULL, market_type INT, buy_total INT, sell_total INT, vol INT, PRIMARY KEY stock_id)ENGINE=InnoDB DEFAULT CHARSET=utf8;";

			bd.exec(sql_create_table_bs);
			
			//String sql_create_index_bs = "CREATE INDEX  buy_date ON " + BStable + " (buy_date);";
			//bd.exec(sql_create_index_bs);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void cleanUp(String BStable){
		BaseDao bd = BaseDao.getInstance();
/*		try {
			
			String sql_clean_index_bs = "DROP INDEX buy_date ON " + BStable;

			bd.exec(sql_clean_index_bs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
		try {
			String sql_clean_table_bs = "DROP TABLE " + BStable;

			bd.exec(sql_clean_table_bs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

	}

	public void addBuy(String stock_id, int market_type,
			int date, int buy_price, int buy_vol) {
		// TODO Auto-generated method stub
		//Log.v(TAG, buypoint.toString());
		BaseDao bd = BaseDao.getInstance();
		String transID = bd.beginTrans();
		try {


			String bs_table = "BS_" + stragyID;
			String sql_replace_bs = "REPLACE INTO " + bs_table 
					+ "(stock_id, market_type, buy_date, sell_date, buy_price, sell_price, vol) values(" 
					+ "'" + stock_id + "'," 
					+ market_type + ","
					+ date + "," 
					+ "0," 
					+ buy_price + ","
					+ "0,"
					+ buy_vol + ");";
			bd.update(transID, sql_replace_bs);
			bd.commitTrans(transID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bd.rollbackTrans(transID);
		}
	}

	public void addSell(String stock_id, int market_type,
			int date, int sell_price, int sell_vol) {
		// TODO Auto-generated method stub
		//Log.v(TAG, sellpoint.toString());
		BaseDao bd = BaseDao.getInstance();
		String transID = bd.beginTrans();
		try {
			String bs_table = "BS_" + stragyID;
			StringBuilder sql_replace_bs = new StringBuilder();
			sql_replace_bs.append("REPLACE INTO ");
			sql_replace_bs.append(bs_table);
			sql_replace_bs.append("(stock_id, market_type, buy_date, sell_date, buy_price, sell_price, vol, earn_percent) values('");  
			sql_replace_bs.append(stock_id);
			sql_replace_bs.append( "'," );
			sql_replace_bs.append(market_type); 
			sql_replace_bs.append(",");
/*			sql_replace_bs.append(hu.buy_date);
			sql_replace_bs.append(","); 
			sql_replace_bs.append(sellpoint.date);
			sql_replace_bs.append(","); 
			sql_replace_bs.append(hu.buy_price);
			sql_replace_bs.append(","); 
			sql_replace_bs.append(sellpoint.price);
			sql_replace_bs.append(","); 
			sql_replace_bs.append(hu.buy_vol);
			sql_replace_bs.append(","); 
			sql_replace_bs.append((sellpoint.price*sellpoint.vol-hu.buy_price*hu.buy_vol)*100/(hu.buy_price*hu.buy_vol) );
			sql_replace_bs.append(");");*/
			
			bd.update(transID, sql_replace_bs.toString());
			bd.commitTrans(transID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bd.rollbackTrans(transID);
		}
	}

}
