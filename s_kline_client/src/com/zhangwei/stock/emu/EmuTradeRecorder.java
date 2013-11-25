package com.zhangwei.stock.emu;

import java.sql.SQLException;

import android.util.Log;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.SellPoint;

public class EmuTradeRecorder {

	private static final String TAG = "EmuTradeRecorder";

	public void addBuy(BuyPoint buypoint) {
		// TODO Auto-generated method stub
		Log.v(TAG, buypoint.toString());
		BaseDao bd = BaseDao.getInstance();
		String transID = bd.beginTrans();
		try {


			String bs_table = "BS_" + buypoint.BSID;
			String sql_replace_bs = "REPLACE INTO " + bs_table 
					+ "(stock_id, market_type, buy_date, sell_date, buy_price, sell_price, vol) values(" 
					+ "'" + buypoint.info.stock_id + "'," 
					+ buypoint.info.market_type + ","
					+ buypoint.date + "," 
					+ "0," 
					+ buypoint.price + ","
					+ "0,"
					+ buypoint.vol + ");";
			bd.update(transID, sql_replace_bs);
			bd.commitTrans(transID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bd.rollbackTrans(transID);
		}
	}

	public void addSell(BuyPoint buypoint, SellPoint sellpoint) {
		// TODO Auto-generated method stub
		Log.v(TAG, sellpoint.toString());
		BaseDao bd = BaseDao.getInstance();
		String transID = bd.beginTrans();
		try {
			String bs_table = "BS_" + buypoint.BSID;
			StringBuilder sql_replace_bs = new StringBuilder();
			sql_replace_bs.append("REPLACE INTO ");
			sql_replace_bs.append(bs_table);
			sql_replace_bs.append("(stock_id, market_type, buy_date, sell_date, buy_price, sell_price, vol, earn_percent) values('");  
			sql_replace_bs.append(buypoint.info.stock_id);
			sql_replace_bs.append( "'," );
			sql_replace_bs.append(buypoint.info.market_type); 
			sql_replace_bs.append(",");
			sql_replace_bs.append(buypoint.date);
			sql_replace_bs.append(","); 
			sql_replace_bs.append(sellpoint.date);
			sql_replace_bs.append(","); 
			sql_replace_bs.append(buypoint.price);
			sql_replace_bs.append(","); 
			sql_replace_bs.append(sellpoint.price);
			sql_replace_bs.append(","); 
			sql_replace_bs.append(buypoint.vol);
			sql_replace_bs.append(","); 
			sql_replace_bs.append((sellpoint.price-buypoint.price)*100/buypoint.price );
			sql_replace_bs.append(");");
			
			bd.update(transID, sql_replace_bs.toString());
			bd.commitTrans(transID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bd.rollbackTrans(transID);
		}
	}

}
