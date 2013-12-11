package com.zhangwei.stock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.mysql.Converter;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.util.DateHelper;
import com.zhangwei.util.StockHelper;

public class Kline {
	private static final String TAG = "Kline";
	public List<KLineUnit> kline_list;
	public List<ExRightUnit> ex_rights;
	
	private int pos;
	
	//最后联网查询的时间，而不是最后一个Kline的点，如果是下午3点前扫描的，算昨天。
	//如果联网失败，不更新
	//如果联网成功，结果为空，更新
	//如果联网成功，结果不为空，写入mysql失败，不更新
	//如果联网成功，结果不为空，写入mysql成功，更新
	//public  int last_scan_day;
	private int nDay; 
	
	public Kline(StockInfo stockInfo){
		kline_list = null;
		ex_rights = null;
		//last_scan_day = -1;
		pos = 0;
		nDay = 0;
		
		
		try {
			//data_kline_<stock_id>_<market_type>
			//data_exrights_<stock_id>_<market_type>

			BaseDao dao = BaseDao.getInstance();
			
/*			String kline_table = "data_kline_" + stockInfo.stock_id + "_" + stockInfo.market_type;
			String exright_table = "data_exrights_" + stockInfo.stock_id + "_" + stockInfo.market_type;

			String sql_create_table_kline = "CREATE TABLE IF NOT EXISTS " +  kline_table + "\n(date INT PRIMARY KEY, open INT, high INT, low INT, close INT, vol INT, cje INT )ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			String sql_create_table_exright = "CREATE TABLE IF NOT EXISTS " + exright_table + "\n(date INT PRIMARY KEY, multi_num INT, add_num INT)ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			
			if(dao.exec(sql_create_table_kline) && dao.exec(sql_create_table_exright)){
				
			}*/

			String sql_kline = "select * from data_kline_" + stockInfo.stock_id + "_" + stockInfo.market_type;
			String sql_exrights = "select * from data_exrights_" + stockInfo.stock_id + "_" + stockInfo.market_type;
					
			List<Map<String, Object>> list_kline = dao.query(sql_kline);
			kline_list = Converter.ListConvert2KLineUnit(list_kline);
			
			if(!stockInfo.index){
				List<Map<String, Object>> list_exrights = dao.query(sql_exrights);
				ex_rights = Converter.ListConvert2ExRightUnit(list_exrights);
			}
			
			String transID = dao.beginTrans();
			try{
				persit2sql_info(transID, stockInfo, kline_list);
				dao.commitTrans(transID);
			}catch(SQLException e){
				dao.rollbackTrans(transID);
			}
			
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

/*	public boolean outOfDate() {
		// TODO Auto-generated method stub
		if(kline_list!=null){
			if(DateHelper.checkVaildDate(last_scan_day)){
				if(DateHelper.Hour()>=15){
					return last_scan_day<DateHelper.Today();
				}else{
					return last_scan_day<DateHelper.Yesteray();
				}
			}

		}
		return true;
	}*/
	
	public void persit2sql_kline(String transId, StockInfo info, List<KLineUnit> kl, List<ExRightUnit> rl) throws SQLException{
		//Log.v(TAG, "persit2sql_kline - IN");
		BaseDao dao = BaseDao.getInstance();
		String kline_table = "data_kline_" + info.stock_id + "_" + info.market_type;
		String exright_table = "data_exrights_" + info.stock_id + "_" + info.market_type;
		
		String sql_replace_kline = "REPLACE INTO " + kline_table + "(date, open, high, low, close, vol, cje) values(?,?,?,?,?,?,?)";
		String sql_replace_exright = "REPLACE INTO " + exright_table + "(date, multi_num, add_num) values(?,?,?)";
		
		//Log.v(TAG, "batchUpdate sql_replace_kline");
		dao.batchUpdate(transId, sql_replace_kline, Converter.ListConvertKLine2Object(kl));
		
		//Log.v(TAG, "batchUpdate sql_replace_exright");
		if(!info.index){
			dao.batchUpdate(transId, sql_replace_exright, Converter.ListConvertExright2Object(rl));
		}
		
		//Log.v(TAG, "persit2sql_kline - Out");
	}
	
	
	public void persit2sql_info(String transId, StockInfo info, List<KLineUnit> kl) throws SQLException {
		// TODO Auto-generated method stub
		BaseDao dao = BaseDao.getInstance();
		if(kl!=null && kl.size()>0){
			int start = kl.get(0).date;
			int last = kl.get(kl.size()-1).date;
			
			if(DateHelper.checkVaildDate(info.start)){
				if(start<info.start){
					info.start = start;
				}else{
					start = info.start;
				}
			}else{
				info.start = start;
			}

			if(DateHelper.checkVaildDate(info.last)){
				if(last>info.last){
					info.last = last;
				}else{
					last = info.last;
				}
			}else{
				info.last = last;
			}

			
			String sql_update_part = " SET start=" + start + " , last=" + last + " WHERE stock_id='" + info.stock_id + "' AND market_type='" + info.market_type + "';";

			if(info.index){
				dao.update(transId, "UPDATE zhishulist" + sql_update_part);
			}else{
				dao.update(transId, "UPDATE stocklist" + sql_update_part);
			}
		}

	}
	
	/*
	 *
	 * arrayOfInt[j][1] = ((arrayOfInt[j][1] * this.exRightsMulti[i4] + 100 * this.exRightsAdd[i4]) / 10000);
	 * arrayOfInt[j][2] = ((arrayOfInt[j][2] * this.exRightsMulti[i4] + 100 * this.exRightsAdd[i4]) / 10000);
	 * arrayOfInt[j][3] = ((arrayOfInt[j][3] * this.exRightsMulti[i4] + 100 * this.exRightsAdd[i4]) / 10000);
	 * arrayOfInt[j][4] = ((arrayOfInt[j][4] * this.exRightsMulti[i4] + 100 * this.exRightsAdd[i4]) / 10000); 
	 * */
	public List<KLineUnit> getExRightKline(){
		return kline_list;
		
	}

	public void reset(int nDay) {
		// TODO Auto-generated method stub
		pos = 0;
		this.nDay = nDay;
	}

	public List<KLineUnit> generateNDayKline(int nDay, int lastBuyDate) {
		// TODO Auto-generated method stub
		
		if(kline_list==null){
			return null;
		}
		
		if(nDay<5){
			this.nDay = 60;
		}else{
			this.nDay = nDay;
		}
		
		int indexFrom = pos;
		int index_buy = indexFrom;
		int indexTo;
		if(DateHelper.checkVaildDate(lastBuyDate)){
			KLineUnit lastBP = StockHelper.binSearch(kline_list, lastBuyDate, 0);
			index_buy = kline_list.indexOf(lastBP);
			if(index_buy<0){
				index_buy = indexFrom;
			}
		}

		
		if(pos + nDay>kline_list.size()){
			return null;
		}else{
			indexTo = pos + nDay;
			indexFrom = Math.min(indexFrom, index_buy);
			//pos++;
			return kline_list.subList(indexFrom, indexTo);
		}

	}
	
	public void nextPos(){
		pos++;
	}

	/**
	 * 从now（指定日期开始，往前nDay记录， 不改变pos值
	 * <br>若now非法或没有对应元素，返回null
	 * <br>若不足nDay元素，有多少返回多少，
	 * */
	public List<KLineUnit> generateNDayKlineToNow(int nDay, int now) {
		// TODO Auto-generated method stub
		if(kline_list==null){
			return null;
		}
		
		if(nDay<5){
			this.nDay = 60;
		}else{
			this.nDay = nDay;
		}
		
		int index_buy = -1;

		if(DateHelper.checkVaildDate(now)){
			KLineUnit lastBP = StockHelper.binSearch(kline_list, now, 0);
			index_buy = kline_list.indexOf(lastBP);
			if(index_buy>0 && index_buy<kline_list.size()){
				int indexTo = index_buy;
				int indexFrom = Math.max(0, index_buy-nDay);

				return kline_list.subList(indexFrom, indexTo+1);
			}
			

		}
		
		return null;

	}

	public int getDate(int index) {
		// TODO Auto-generated method stub
		if(kline_list==null || index<0 || index>=kline_list.size()){
			return -1;
		}

		KLineUnit elem = kline_list.get(index);
		if(elem==null){
			return -1;
		}
		
		return elem.date;
	}

	public KLineUnit getKlineUnit(int date) {
		// TODO Auto-generated method stub
		return StockHelper.binSearch(kline_list, date, 0);
	}

	
}
