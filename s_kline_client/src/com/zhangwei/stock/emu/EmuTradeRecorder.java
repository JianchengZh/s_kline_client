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
			String sql_replace_bs = "REPLACE INTO " + bs_table 
					+ "(stock_id, market_type, buy_date, sell_date, buy_price, sell_price, vol) values(" 
					+ "'" + buypoint.info.stock_id + "'," 
					+ buypoint.info.market_type + ","
					+ buypoint.date + "," 
					+ sellpoint.date + "," 
					+ buypoint.price + ","
					+ sellpoint.price + ","
					+ buypoint.vol + ");";
			bd.update(transID, sql_replace_bs);
			bd.commitTrans(transID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bd.rollbackTrans(transID);
		}
	}

}
