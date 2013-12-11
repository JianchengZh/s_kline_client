package com.zhangwei.stock.record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.util.Format;

public class EmuTradeRecorder {

	private static final String TAG = "EmuTradeRecorder";
	private String BStable;
	
	public EmuTradeRecorder(String BStable){
		this.BStable = BStable;
		
		cleanUp(BStable);
		BaseDao bd = BaseDao.getInstance();
		
		Log.v(TAG, getClass().getName() + " - init, BStable:" + BStable);
		try {

			String sql_create_table_bs = "CREATE TABLE IF NOT EXISTS " 
			                  + BStable  
			                  + "\n(stock_id VARCHAR(6) NOT NULL, market_type INT, buy_total INT, sell_total INT, vol INT, vol_cansell INT, PRIMARY KEY stock_id)ENGINE=InnoDB DEFAULT CHARSET=utf8;";

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
		TradeRecord tr = getTradeRecord(stock_id);
		if(tr==null){
			tr = new TradeRecord(stock_id, market_type, buy_price, buy_vol);
		}
		
		tr.buyIn(buy_price, buy_vol);
		
		putTradeRecord(tr);
		
	}

	public void addSell(String stock_id, int market_type,
			int date, int sell_price, int sell_vol) {
		// TODO Auto-generated method stub
		//Log.v(TAG, sellpoint.toString());
		TradeRecord tr = getTradeRecord(stock_id);
		if(tr==null){
			Log.v(TAG, "TradeRecord is null! - " + stock_id + "." + market_type);
			return;
		}
		
		tr.sellOut(sell_price, sell_vol);
		
		putTradeRecord(tr);
	}
	
	public TradeRecord getTradeRecord(String arg_stock_id){
		ArrayList<TradeRecord> rlt = new ArrayList<TradeRecord>();
		
		try {
			BaseDao dao = BaseDao.getInstance();	
			
			StringBuilder sql = new StringBuilder();
			sql.append("select * from ");
			sql.append(BStable);

			if(arg_stock_id!=null && !arg_stock_id.equals("")){
				sql.append(" where stock_id='" + arg_stock_id + "'");
			}			
			
			List<Map<String, Object>> list = dao.query(sql.toString());

			if(list!=null && list.size()>0){
				for(Map<String, Object> item : list){
					String stock_id =  (String) item.get("stock_id");
					int market_type = Format.parserInt(item.get("market_type").toString(), -1);
					int buy_total = Format.parserInt(item.get("buy_total").toString(), -1);/*(Integer)item.get("start");*/
					int sell_total = Format.parserInt(item.get("sell_total").toString(), -1);/*(Integer)item.get("last");*/
					int vol = Format.parserInt(item.get("vol").toString(), -1);
					int vol_cansell = Format.parserInt(item.get("vol_cansell").toString(), -1);
					TradeRecord si = new TradeRecord(stock_id, market_type, buy_total, sell_total, vol, vol_cansell);
					rlt.add(si);
				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(rlt.size()==0){
			return null;
		}else{
			return rlt.get(0);
		}
	}
	
	public void putTradeRecord(TradeRecord tr){
		BaseDao bd = BaseDao.getInstance();	
		String transID = bd.beginTrans();
		try {

			StringBuilder sql_replace_bs = new StringBuilder();
			sql_replace_bs.append("REPLACE INTO ");
			sql_replace_bs.append(BStable);
			sql_replace_bs.append("(stock_id, market_type, buy_total, sell_total, vol) values('");  
			sql_replace_bs.append(tr.stock_id);
			sql_replace_bs.append( "'," );
			sql_replace_bs.append(tr.market_type); 
			sql_replace_bs.append(",");
			sql_replace_bs.append(tr.buy_total); 
			sql_replace_bs.append(",");
			sql_replace_bs.append(tr.sell_total); 
			sql_replace_bs.append(",");
			sql_replace_bs.append(tr.vol_total); 
			sql_replace_bs.append(");");
			bd.update(transID, sql_replace_bs.toString());
			bd.commitTrans(transID);
		} catch (Exception e) {
			e.printStackTrace();
			bd.rollbackTrans(transID);
		}
		

	}

}
