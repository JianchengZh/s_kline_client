package com.zhangwei.stock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.android.dazhihui.http.Response;
import com.android.dazhihui.http.StructResponse;
import com.zhangwei.client.DZHClient;
import com.zhangwei.mysql.BaseDao;
import com.zhangwei.mysql.Converter;
import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.TradeUnit;
import com.zhangwei.util.DateHelper;
import com.zhangwei.util.StockHelper;

public class Stock {
	
	private static final String TAG = "Stock";
	public StockInfo info;
	public Kline line;
	
	public  Stock(StockInfo info){
		this.info = info;
		line = new Kline(info);
	}
	
	public String getKey(){
		if(info!=null){
			return info.getKey();
		}else{
			return null;
		}

	}

	public boolean outOfDate() {
		// TODO Auto-generated method stub
		if(line!=null && info!=null){
			return info.outOfDate();
		}
		return true;
	}

	public void updateSQL(boolean force) {
		// TODO Auto-generated method stub
		if(line==null || force){
			fetchFromDZH(0, 0);
		}else if(info.outOfDate()){
			fetchFromDZH(info.getScanDay(), 0);
			//fetchFromDZH(20131110, 0);
		}
		
	}
	
	
	/**
	 *  @param begin 开始时间点，包括；若为0则一直回溯
	 *  @param end 结束时间点，不包括；若为0则从今天（包括）回溯
	 * */
	public void fetchFromDZH(int begin, int end){
		Log.v(TAG, "FetchFromDZH - begin:" +  begin + ", end:" + end);
		ArrayList<KLineUnit> kl = new ArrayList<KLineUnit>();
		ArrayList<ExRightUnit> rl = new ArrayList<ExRightUnit>();
		
		DZHClient remoteFileClient = DZHClient.getInstance();
		int last = end;
		boolean hasOnce = true;
		
		//从最近往前回溯，每次150条记录
		int num = 0;
		int firstDate = 0;
		do{
			Response resp = remoteFileClient.sendRequest(info.getDZHStockID(), last);

			ArrayList<KLineUnit> kl_tmp = convertResp2Kline(resp);
			
			num = kl_tmp.size();
			
			kl_tmp.addAll(kl);
			kl = kl_tmp;
			
			if(hasOnce){
				ArrayList<ExRightUnit> rl_tmp = convertResp2ExRights(resp);
				rl_tmp.addAll(rl);
				rl = rl_tmp;
				hasOnce = false;
			}
			
			if(num>0){
				firstDate = kl_tmp.get(0).date;
				last = firstDate;
			}
			
		}while(num>=150 && firstDate>begin);

		if(kl!=null && kl.size()>0){
			//Log.v(TAG, "KL fetched: " + kl.size() + ", start:" + kl.get(0).date + ", last:" + kl.get(kl.size()-1).date);
		}

		
		if(rl!=null && rl.size()>0){
			//Log.v(TAG, "RL fetched: " + rl.size() + ", start:" + rl.get(0).date + ", last:" + rl.get(rl.size()-1).date);
		}

				
		BaseDao dao = BaseDao.getInstance();
		int index = 0;


		//Log.v(TAG, "FetchFromDZH - beginTrans");
		
		if(kl!=null && kl.size()>0){
			final int size = 100;
			String transID = dao.beginTrans();
			int endIndex = kl.size();
			try{
				do{
					List<KLineUnit> k_t = null;
					if(index+size<=endIndex){
						k_t = kl.subList(index, index+size);
					}else{
						k_t = kl.subList(index, endIndex);
					}
					line.persit2sql_kline(transID, info, k_t, rl);
					index+=size;
				}while(index<=endIndex);
				
				line.persit2sql_info(transID, info, kl);
				
				StringBuilder sql_update_scan = new StringBuilder();
				if(info.index){
					sql_update_scan.append("UPDATE zhishulist SET scan=");
				}else{
					sql_update_scan.append("UPDATE stocklist SET scan=");
				}
				
				sql_update_scan.append(DateHelper.TodayHour());
				sql_update_scan.append(" WHERE stock_id='");
				sql_update_scan.append(info.stock_id);
				sql_update_scan.append("' AND market_type=");
				sql_update_scan.append(info.market_type);
				sql_update_scan.append(";");
				
				dao.update(transID, sql_update_scan.toString());
				
				dao.commitTrans(transID);
			}catch(SQLException e){
				dao.rollbackTrans(transID);
			}
		}
		
		//Log.v(TAG, "FetchFromDZH - Out");
	}	
	
/*	private void persit2sql_info(String transId, StockInfo info, ArrayList<KLineUnit> kl) throws SQLException {
		// TODO Auto-generated method stub
		BaseDao dao = BaseDao.getInstance();
		
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
			dao.update("UPDATE zhishulist" + sql_update_part);
		}else{
			dao.update("UPDATE stocklist" + sql_update_part);
		}
	}*/

/*	private void persit2sql_kline(String transId, StockInfo info, ArrayList<KLineUnit> kl, ArrayList<ExRightUnit> rl) throws SQLException{
		BaseDao dao = BaseDao.getInstance();
		String kline_table = "data_kline_" + info.stock_id + "_" + info.market_type;
		String exright_table = "data_exrights_" + info.stock_id + "_" + info.market_type;
		
		String sql_create_table_kline = "CREATE TABLE IF NOT EXISTS " +  kline_table + "\n(date INT PRIMARY KEY, open INT, high INT, low INT, close INT, vol INT, cje INT )ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		String sql_create_table_exright = "CREATE TABLE IF NOT EXISTS " + exright_table + "\n(date INT PRIMARY KEY, multi_num INT, add_num INT)ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		
		dao.exec(sql_create_table_kline);
		dao.exec(sql_create_table_exright);
		
		
		String sql_replace_kline = "REPLACE INTO " + kline_table + "(date, open, high, low, close, vol, cje) values(?,?,?,?,?,?,?)";
		String sql_replace_exright = "REPLACE INTO " + exright_table + "(date, multi_num, add_num) values(?,?,?)";
		
		dao.batchUpdate(sql_replace_kline, Converter.ListConvertKLine2Object(kl));
		dao.batchUpdate(sql_replace_exright, Converter.ListConvertExright2Object(rl));
	}*/
	
	private ArrayList<KLineUnit> convertResp2Kline(Response resp){
		ArrayList<KLineUnit> kl = new ArrayList<KLineUnit>();
		byte[] kline_input = resp.getData(2944);

		//byte[] kline_input = {0,-106,0,1,42,51,1,-54,1,0,0,-42,1,0,0,-57,1,0,0,-46,1,0,0,-50,-89,0,0,-61,7,0,0,2,42,51,1,-47,1,0,0,1,2,0,0,-47,1,0,0,1,2,0,0,108,-24,3,0,0,50,0,0,3,42,51,1,12,2,0,0,31,2,0,0,1,2,0,0,15,2,0,0,61,-121,6,0,116,88,0,0,4,42,51,1,8,2,0,0,15,2,0,0,-11,1,0,0,-8,1,0,0,-65,-32,2,0,-109,37,0,0,7,42,51,1,-9,1,0,0,-7,1,0,0,-22,1,0,0,-10,1,0,0,-44,100,1,0,-66,17,0,0,8,42,51,1,-12,1,0,0,-3,1,0,0,-42,1,0,0,-28,1,0,0,124,-7,1,0,-66,24,0,0,9,42,51,1,-30,1,0,0,-26,1,0,0,-52,1,0,0,-40,1,0,0,61,34,1,0,-80,13,0,0,10,42,51,1,-42,1,0,0,-2,1,0,0,-52,1,0,0,-35,1,0,0,125,-51,1,0,124,22,0,0,11,42,51,1,-35,1,0,0,-23,1,0,0,-41,1,0,0,-34,1,0,0,27,44,1,0,112,14,0,0,14,42,51,1,-37,1,0,0,-35,1,0,0,-52,1,0,0,-48,1,0,0,120,75,1,0,102,15,0,0,15,42,51,1,-49,1,0,0,-44,1,0,0,-59,1,0,0,-50,1,0,0,-54,-13,0,0,53,11,0,0,16,42,51,1,-49,1,0,0,-30,1,0,0,-54,1,0,0,-33,1,0,0,-23,64,1,0,41,15,0,0,17,42,51,1,-31,1,0,0,-9,1,0,0,-35,1,0,0,-20,1,0,0,-17,4,2,0,96,25,0,0,18,42,51,1,-23,1,0,0,-12,1,0,0,-28,1,0,0,-22,1,0,0,-7,-10,0,0,32,12,0,0,21,42,51,1,-25,1,0,0,-18,1,0,0,-30,1,0,0,-20,1,0,0,-79,-50,0,0,18,10,0,0,22,42,51,1,-21,1,0,0,-20,1,0,0,-36,1,0,0,-30,1,0,0,110,-80,0,0,122,8,0,0,23,42,51,1,-31,1,0,0,-26,1,0,0,-36,1,0,0,-33,1,0,0,115,-109,0,0,24,7,0,0,24,42,51,1,-24,1,0,0,-12,1,0,0,-41,1,0,0,-32,1,0,0,-12,-7,1,0,-100,24,0,0,25,42,51,1,-36,1,0,0,-25,1,0,0,-38,1,0,0,-34,1,0,0,47,5,1,0,-123,12,0,0,97,42,51,1,-37,1,0,0,-12,1,0,0,-50,1,0,0,-22,1,0,0,-116,120,1,0,45,18,0,0,98,42,51,1,-27,1,0,0,-20,1,0,0,-29,1,0,0,-27,1,0,0,-35,-36,0,0,-61,10,0,0,99,42,51,1,-26,1,0,0,-7,1,0,0,-29,1,0,0,-14,1,0,0,-24,65,2,0,-65,28,0,0,104,42,51,1,-22,1,0,0,2,2,0,0,-27,1,0,0,-3,1,0,0,-92,23,2,0,-17,26,0,0,105,42,51,1,-7,1,0,0,0,2,0,0,-10,1,0,0,-7,1,0,0,-49,127,1,0,107,19,0,0,106,42,51,1,-11,1,0,0,-7,1,0,0,-25,1,0,0,-20,1,0,0,-68,44,1,0,-28,14,0,0,107,42,51,1,-17,1,0,0,-13,1,0,0,-23,1,0,0,-21,1,0,0,14,-90,0,0,49,8,0,0,108,42,51,1,-22,1,0,0,-18,1,0,0,-37,1,0,0,-35,1,0,0,35,28,1,0,-78,13,0,0,111,42,51,1,-40,1,0,0,-32,1,0,0,-44,1,0,0,-35,1,0,0,-58,-111,0,0,-25,6,0,0,112,42,51,1,-40,1,0,0,-28,1,0,0,-41,1,0,0,-29,1,0,0,51,117,0,0,-99,5,0,0,113,42,51,1,-29,1,0,0,-15,1,0,0,-32,1,0,0,-17,1,0,0,-91,-37,0,0,-76,10,0,0,114,42,51,1,-25,1,0,0,-6,1,0,0,-28,1,0,0,-19,1,0,0,-104,100,1,0,-89,17,0,0,115,42,51,1,-12,1,0,0,-5,1,0,0,-20,1,0,0,-10,1,0,0,-51,-125,1,0,115,19,0,0,118,42,51,1,-16,1,0,0,-5,1,0,0,-20,1,0,0,-14,1,0,0,121,11,1,0,80,13,0,0,119,42,51,1,-14,1,0,0,-9,1,0,0,-35,1,0,0,-31,1,0,0,120,15,1,0,69,13,0,0,120,42,51,1,-31,1,0,0,-19,1,0,0,-34,1,0,0,-21,1,0,0,50,-68,0,0,42,9,0,0,121,42,51,1,-22,1,0,0,-22,1,0,0,-40,1,0,0,-36,1,0,0,-80,34,1,0,-19,13,0,0,122,42,51,1,-34,1,0,0,-32,1,0,0,-46,1,0,0,-45,1,0,0,-69,-81,0,0,76,8,0,0,-58,42,51,1,-46,1,0,0,-46,1,0,0,-57,1,0,0,-54,1,0,0,1,-87,0,0,-64,7,0,0,-57,42,51,1,-52,1,0,0,-32,1,0,0,-52,1,0,0,-44,1,0,0,-104,-64,0,0,8,9,0,0,-54,42,51,1,-46,1,0,0,3,2,0,0,-46,1,0,0,-13,1,0,0,-84,-121,2,0,36,32,0,0,-53,42,51,1,-22,1,0,0,-13,1,0,0,-22,1,0,0,-17,1,0,0,-46,50,1,0,37,15,0,0,-52,42,51,1,-18,1,0,0,-14,1,0,0,-26,1,0,0,-19,1,0,0,-3,52,1,0,55,15,0,0,-51,42,51,1,-20,1,0,0,-9,1,0,0,-26,1,0,0,-12,1,0,0,-52,-88,1,0,7,21,0,0,-50,42,51,1,-12,1,0,0,15,2,0,0,-15,1,0,0,1,2,0,0,1,12,3,0,44,40,0,0,-47,42,51,1,-1,1,0,0,1,2,0,0,-16,1,0,0,-10,1,0,0,-62,-81,1,0,-82,21,0,0,-46,42,51,1,-3,1,0,0,31,2,0,0,-8,1,0,0,9,2,0,0,98,18,4,0,-109,54,0,0,-45,42,51,1,7,2,0,0,20,2,0,0,0,2,0,0,18,2,0,0,14,27,2,0,60,28,0,0,-44,42,51,1,11,2,0,0,32,2,0,0,10,2,0,0,17,2,0,0,26,-118,2,0,-113,34,0,0,-43,42,51,1,13,2,0,0,19,2,0,0,4,2,0,0,13,2,0,0,123,-43,1,0,-112,24,0,0,-40,42,51,1,9,2,0,0,46,2,0,0,8,2,0,0,34,2,0,0,92,89,3,0,20,46,0,0,-39,42,51,1,27,2,0,0,74,2,0,0,26,2,0,0,62,2,0,0,-2,66,4,0,62,61,0,0,-38,42,51,1,56,2,0,0,67,2,0,0,43,2,0,0,50,2,0,0,55,97,2,0,112,34,0,0,-37,42,51,1,45,2,0,0,62,2,0,0,40,2,0,0,46,2,0,0,66,29,2,0,-113,30,0,0,-36,42,51,1,46,2,0,0,89,2,0,0,43,2,0,0,86,2,0,0,82,-94,3,0,96,54,0,0,-33,42,51,1,82,2,0,0,104,2,0,0,74,2,0,0,88,2,0,0,8,-62,2,0,94,42,0,0,-32,42,51,1,82,2,0,0,85,2,0,0,58,2,0,0,79,2,0,0,-84,-8,1,0,-120,29,0,0,-31,42,51,1,77,2,0,0,93,2,0,0,71,2,0,0,86,2,0,0,-116,-105,1,0,55,24,0,0,-30,42,51,1,82,2,0,0,90,2,0,0,68,2,0,0,71,2,0,0,6,-85,1,0,20,25,0,0,-29,42,51,1,71,2,0,0,78,2,0,0,45,2,0,0,49,2,0,0,-90,-103,2,0,-32,37,0,0,43,43,51,1,52,2,0,0,61,2,0,0,43,2,0,0,51,2,0,0,-43,-123,1,0,7,22,0,0,44,43,51,1,48,2,0,0,51,2,0,0,24,2,0,0,32,2,0,0,-5,117,1,0,117,20,0,0,45,43,51,1,28,2,0,0,38,2,0,0,20,2,0,0,29,2,0,0,-118,-19,0,0,-25,12,0,0,46,43,51,1,32,2,0,0,32,2,0,0,20,2,0,0,21,2,0,0,101,-19,0,0,-59,12,0,0,47,43,51,1,23,2,0,0,28,2,0,0,7,2,0,0,12,2,0,0,98,2,1,0,-89,13,0,0,53,43,51,1,5,2,0,0,5,2,0,0,-22,1,0,0,-9,1,0,0,90,23,1,0,5,14,0,0,54,43,51,1,-6,1,0,0,15,2,0,0,-9,1,0,0,11,2,0,0,80,-3,0,0,44,13,0,0,57,43,51,1,11,2,0,0,27,2,0,0,3,2,0,0,17,2,0,0,13,19,1,0,-115,14,0,0,58,43,51,1,17,2,0,0,20,2,0,0,4,2,0,0,19,2,0,0,92,-61,0,0,64,10,0,0,59,43,51,1,16,2,0,0,17,2,0,0,1,2,0,0,12,2,0,0,97,-90,0,0,-83,8,0,0,60,43,51,1,-110,1,0,0,-101,1,0,0,-121,1,0,0,-119,1,0,0,-28,-108,0,0,-2,5,0,0,61,43,51,1,125,1,0,0,-122,1,0,0,117,1,0,0,126,1,0,0,48,-7,0,0,-124,9,0,0,64,43,51,1,126,1,0,0,-127,1,0,0,92,1,0,0,95,1,0,0,24,53,1,0,60,11,0,0,65,43,51,1,94,1,0,0,102,1,0,0,60,1,0,0,92,1,0,0,5,76,1,0,65,11,0,0,66,43,51,1,94,1,0,0,122,1,0,0,84,1,0,0,109,1,0,0,-40,-106,1,0,-83,14,0,0,67,43,51,1,108,1,0,0,117,1,0,0,84,1,0,0,96,1,0,0,-98,-65,1,0,20,16,0,0,68,43,51,1,86,1,0,0,127,1,0,0,85,1,0,0,110,1,0,0,33,-37,1,0,87,17,0,0,-115,43,51,1,109,1,0,0,122,1,0,0,104,1,0,0,119,1,0,0,85,105,1,0,113,13,0,0,-114,43,51,1,126,1,0,0,-99,1,0,0,116,1,0,0,-99,1,0,0,-66,101,6,0,77,66,0,0,-113,43,51,1,-93,1,0,0,-58,1,0,0,-101,1,0,0,-58,1,0,0,114,-11,8,0,-81,98,0,0,-112,43,51,1,-66,1,0,0,-22,1,0,0,-72,1,0,0,-25,1,0,0,77,56,11,0,-60,-121,0,0,-111,43,51,1,-32,1,0,0,-27,1,0,0,-54,1,0,0,-52,1,0,0,105,-95,8,0,-79,104,0,0,-108,43,51,1,-62,1,0,0,-32,1,0,0,-82,1,0,0,-67,1,0,0,84,40,8,0,-98,95,0,0,-107,43,51,1,-71,1,0,0,-22,1,0,0,-71,1,0,0,-22,1,0,0,15,54,8,0,38,99,0,0,-106,43,51,1,13,2,0,0,27,2,0,0,11,2,0,0,27,2,0,0,24,91,3,0,-7,45,0,0,-105,43,51,1,18,2,0,0,55,2,0,0,6,2,0,0,42,2,0,0,-64,43,16,0,24,-28,0,0,-104,43,51,1,38,2,0,0,66,2,0,0,31,2,0,0,34,2,0,0,-127,25,11,0,95,-98,0,0,-101,43,51,1,31,2,0,0,57,2,0,0,26,2,0,0,47,2,0,0,-39,112,9,0,-98,-123,0,0,-100,43,51,1,38,2,0,0,38,2,0,0,18,2,0,0,30,2,0,0,-62,56,8,0,78,113,0,0,-99,43,51,1,28,2,0,0,45,2,0,0,12,2,0,0,23,2,0,0,-38,85,8,0,-112,115,0,0,-98,43,51,1,13,2,0,0,54,2,0,0,4,2,0,0,48,2,0,0,-48,24,11,0,-101,-103,0,0,-97,43,51,1,41,2,0,0,104,2,0,0,39,2,0,0,66,2,0,0,21,71,15,0,47,-26,0,0,-94,43,51,1,43,2,0,0,87,2,0,0,28,2,0,0,67,2,0,0,6,113,10,0,-46,-103,0,0,-93,43,51,1,67,2,0,0,76,2,0,0,45,2,0,0,62,2,0,0,52,-36,7,0,115,114,0,0,-92,43,51,1,53,2,0,0,61,2,0,0,27,2,0,0,46,2,0,0,38,38,8,0,-104,115,0,0,-91,43,51,1,43,2,0,0,43,2,0,0,-4,1,0,0,-2,1,0,0,-106,10,7,0,113,95,0,0,-90,43,51,1,-3,1,0,0,17,2,0,0,-15,1,0,0,2,2,0,0,95,-30,4,0,10,64,0,0,-87,43,51,1,0,2,0,0,0,2,0,0,-38,1,0,0,-37,1,0,0,52,-33,4,0,55,61,0,0,-86,43,51,1,-34,1,0,0,-27,1,0,0,-57,1,0,0,-38,1,0,0,54,88,5,0,66,64,0,0,-85,43,51,1,-38,1,0,0,-22,1,0,0,-47,1,0,0,-46,1,0,0,-68,-71,4,0,-60,57,0,0,-15,43,51,1,-43,1,0,0,-9,1,0,0,-45,1,0,0,-10,1,0,0,-117,28,7,0,117,89,0,0,-14,43,51,1,-10,1,0,0,2,2,0,0,-21,1,0,0,-19,1,0,0,38,125,5,0,127,70,0,0,-11,43,51,1,-23,1,0,0,-15,1,0,0,-26,1,0,0,-15,1,0,0,86,-101,2,0,-42,32,0,0,-10,43,51,1,-17,1,0,0,11,2,0,0,-19,1,0,0,-3,1,0,0,-119,-12,4,0,-62,64,0,0,-9,43,51,1,-7,1,0,0,-4,1,0,0,-23,1,0,0,-20,1,0,0,64,-66,3,0,118,47,0,0,-8,43,51,1,-20,1,0,0,-13,1,0,0,-27,1,0,0,-16,1,0,0,-15,71,2,0,-49,28,0,0,-7,43,51,1,-17,1,0,0,-9,1,0,0,-36,1,0,0,-27,1,0,0,110,-100,2,0,-113,32,0,0,-4,43,51,1,-28,1,0,0,-19,1,0,0,-33,1,0,0,-22,1,0,0,-92,-41,2,0,60,35,0,0,-3,43,51,1,-22,1,0,0,-15,1,0,0,-31,1,0,0,-21,1,0,0,98,-95,2,0,18,33,0,0,-2,43,51,1,-22,1,0,0,28,2,0,0,-27,1,0,0,28,2,0,0,-47,30,8,0,76,107,0,0,-1,43,51,1,48,2,0,0,75,2,0,0,29,2,0,0,48,2,0,0,-69,93,13,0,27,-64,0,0,0,44,51,1,35,2,0,0,55,2,0,0,11,2,0,0,14,2,0,0,116,-39,7,0,-46,109,0,0,3,44,51,1,6,2,0,0,56,2,0,0,-12,1,0,0,47,2,0,0,-76,17,7,0,-90,98,0,0,4,44,51,1,39,2,0,0,71,2,0,0,30,2,0,0,59,2,0,0,59,-47,8,0,107,127,0,0,5,44,51,1,52,2,0,0,95,2,0,0,46,2,0,0,72,2,0,0,36,-125,9,0,-52,-114,0,0,6,44,51,1,74,2,0,0,92,2,0,0,52,2,0,0,63,2,0,0,29,73,8,0,19,123,0,0,7,44,51,1,58,2,0,0,67,2,0,0,29,2,0,0,47,2,0,0,72,15,6,0,-4,86,0,0,10,44,51,1,52,2,0,0,87,2,0,0,40,2,0,0,76,2,0,0,-128,127,7,0,-64,110,0,0,11,44,51,1,69,2,0,0,91,2,0,0,61,2,0,0,80,2,0,0,5,-86,7,0,-26,115,0,0,12,44,51,1,68,2,0,0,79,2,0,0,54,2,0,0,56,2,0,0,5,62,6,0,-28,91,0,0,13,44,51,1,51,2,0,0,61,2,0,0,36,2,0,0,44,2,0,0,68,-23,3,0,-36,55,0,0,14,44,51,1,42,2,0,0,68,2,0,0,31,2,0,0,39,2,0,0,25,30,5,0,122,73,0,0,86,44,51,1,28,2,0,0,47,2,0,0,18,2,0,0,43,2,0,0,-10,-65,2,0,-127,38,0,0,87,44,51,1,39,2,0,0,56,2,0,0,34,2,0,0,50,2,0,0,122,-77,2,0,-105,38,0,0,88,44,51,1,46,2,0,0,51,2,0,0,33,2,0,0,34,2,0,0,21,67,3,0,12,46,0,0,89,44,51,1,33,2,0,0,54,2,0,0,28,2,0,0,50,2,0,0,111,15,4,0,-11,57,0,0,90,44,51,1,48,2,0,0,72,2,0,0,46,2,0,0,58,2,0,0,37,97,5,0,-20,78,0,0,93,44,51,1,57,2,0,0,115,2,0,0,45,2,0,0,115,2,0,0,91,4,11,0,19,-87,0,0,94,44,51,1,118,2,0,0,-85,2,0,0,106,2,0,0,-122,2,0,0,-96,9,13,0,78,-42,0,0,95,44,51,1,-120,2,0,0,-115,2,0,0,98,2,0,0,115,2,0,0,6,11,7,0,93,113,0,0,96,44,51,1,109,2,0,0,-83,2,0,0,106,2,0,0,-109,2,0,0,51,-87,8,0,-6,-111,0,0,97,44,51,1,-123,2,0,0,-62,2,0,0,123,2,0,0,-88,2,0,0,53,0,9,0,-16,-102,0,0,100,44,51,1,-98,2,0,0,-43,2,0,0,-103,2,0,0,-49,2,0,0,60,78,9,0,-125,-88,0,0,101,44,51,1,-54,2,0,0,-22,2,0,0,-83,2,0,0,-66,2,0,0,-112,18,9,0,-12,-90,0,0,102,44,51,1,-75,2,0,0,-33,2,0,0,-75,2,0,0,-43,2,0,0,-74,74,6,0,-90,114,0,0,107,44,51,1,-41,2,0,0,30,3,0,0,-54,2,0,0,30,3,0,0,109,-52,9,0,-33,-65,0,0,108,44,51,1,30,3,0,0,90,3,0,0,14,3,0,0,43,3,0,0,69,-115,10,0,-11,-36,0,0,109,44,51,1,32,3,0,0,78,3,0,0,5,3,0,0,11,3,0,0,60,-119,8,0,98,-80,0,0,110,44,51,1,3,3,0,0,27,3,0,0,-18,2,0,0,-8,2,0,0,40,-72,5,0,-34,112,0,0,111,44,51,1,-5,2,0,0,15,3,0,0,-35,2,0,0,-20,2,0,0,-49,-58,4,0,-89,92,0,0,114,44,51,1,-23,2,0,0,37,3,0,0,-23,2,0,0,33,3,0,0,-123,-24,5,0,24,119,0,0,-64,44,51,1,48,3,0,0,72,3,0,0,28,3,0,0,56,3,0,0,-112,115,6,0,70,-121,0,0,-63,44,51,1,50,3,0,0,78,3,0,0,39,3,0,0,75,3,0,0,-37,-7,5,0,1,127,0,0,-62,44,51,1,112,3,0,0,-97,3,0,0,92,3,0,0,99,3,0,0,-24,-69,10,0,-74,-9,0,0,-61,44,51,1,83,3,0,0,-125,3,0,0,39,3,0,0,126,3,0,0,92,-3,8,0,39,-58,0,0,-58,44,51,1,112,3,0,0,-119,3,0,0,94,3,0,0,112,3,0,0,32,8,5,0,20,114,0,0,-57,44,51,1,105,3,0,0,126,3,0,0,91,3,0,0,98,3,0,0,-64,56,4,0,-113,94,0,0,-56,44,51,1,91,3,0,0,95,3,0,0,29,3,0,0,34,3,0,0,51,-50,5,0,-117,121,0,0,-55,44,51,1,39,3,0,0,55,3,0,0,-18,2,0,0,2,3,0,0,11,-120,4,0,50,91,0,0,-54,44,51,1,2,3,0,0,30,3,0,0,-7,2,0,0,20,3,0,0,-21,-29,2,0,39,58,0,0,-51,44,51,1,21,3,0,0,65,3,0,0,-1,2,0,0,56,3,0,0,86,-75,4,0,-44,97,0,0};
		if(kline_input!=null && kline_input.length>0){
			StructResponse sr = new StructResponse(kline_input);

			int b = sr.readByte();
			int len = sr.readShort();
			
			for(int i=0; i<len; i++){
				int date = sr.readInt();
				int open_price = sr.readInt();
				int high_price = sr.readInt();
				int low_price = sr.readInt();
				int close = sr.readInt();
				int vol = sr.readInt();
				int cje = sr.readInt();
				
				kl.add(new KLineUnit(date, open_price, high_price, low_price, close, vol, cje));
			}
		}else{
			Log.v(TAG, "no kline data! ");
		}

	
		return kl;
	}
	
	private ArrayList<ExRightUnit> convertResp2ExRights(Response resp){
		ArrayList<ExRightUnit> rl = new ArrayList<ExRightUnit>();
		byte[] exright_input = resp.getData(2958);

		//byte[] kline_input = {0,-106,0,1,42,51,1,-54,1,0,0,-42,1,0,0,-57,1,0,0,-46,1,0,0,-50,-89,0,0,-61,7,0,0,2,42,51,1,-47,1,0,0,1,2,0,0,-47,1,0,0,1,2,0,0,108,-24,3,0,0,50,0,0,3,42,51,1,12,2,0,0,31,2,0,0,1,2,0,0,15,2,0,0,61,-121,6,0,116,88,0,0,4,42,51,1,8,2,0,0,15,2,0,0,-11,1,0,0,-8,1,0,0,-65,-32,2,0,-109,37,0,0,7,42,51,1,-9,1,0,0,-7,1,0,0,-22,1,0,0,-10,1,0,0,-44,100,1,0,-66,17,0,0,8,42,51,1,-12,1,0,0,-3,1,0,0,-42,1,0,0,-28,1,0,0,124,-7,1,0,-66,24,0,0,9,42,51,1,-30,1,0,0,-26,1,0,0,-52,1,0,0,-40,1,0,0,61,34,1,0,-80,13,0,0,10,42,51,1,-42,1,0,0,-2,1,0,0,-52,1,0,0,-35,1,0,0,125,-51,1,0,124,22,0,0,11,42,51,1,-35,1,0,0,-23,1,0,0,-41,1,0,0,-34,1,0,0,27,44,1,0,112,14,0,0,14,42,51,1,-37,1,0,0,-35,1,0,0,-52,1,0,0,-48,1,0,0,120,75,1,0,102,15,0,0,15,42,51,1,-49,1,0,0,-44,1,0,0,-59,1,0,0,-50,1,0,0,-54,-13,0,0,53,11,0,0,16,42,51,1,-49,1,0,0,-30,1,0,0,-54,1,0,0,-33,1,0,0,-23,64,1,0,41,15,0,0,17,42,51,1,-31,1,0,0,-9,1,0,0,-35,1,0,0,-20,1,0,0,-17,4,2,0,96,25,0,0,18,42,51,1,-23,1,0,0,-12,1,0,0,-28,1,0,0,-22,1,0,0,-7,-10,0,0,32,12,0,0,21,42,51,1,-25,1,0,0,-18,1,0,0,-30,1,0,0,-20,1,0,0,-79,-50,0,0,18,10,0,0,22,42,51,1,-21,1,0,0,-20,1,0,0,-36,1,0,0,-30,1,0,0,110,-80,0,0,122,8,0,0,23,42,51,1,-31,1,0,0,-26,1,0,0,-36,1,0,0,-33,1,0,0,115,-109,0,0,24,7,0,0,24,42,51,1,-24,1,0,0,-12,1,0,0,-41,1,0,0,-32,1,0,0,-12,-7,1,0,-100,24,0,0,25,42,51,1,-36,1,0,0,-25,1,0,0,-38,1,0,0,-34,1,0,0,47,5,1,0,-123,12,0,0,97,42,51,1,-37,1,0,0,-12,1,0,0,-50,1,0,0,-22,1,0,0,-116,120,1,0,45,18,0,0,98,42,51,1,-27,1,0,0,-20,1,0,0,-29,1,0,0,-27,1,0,0,-35,-36,0,0,-61,10,0,0,99,42,51,1,-26,1,0,0,-7,1,0,0,-29,1,0,0,-14,1,0,0,-24,65,2,0,-65,28,0,0,104,42,51,1,-22,1,0,0,2,2,0,0,-27,1,0,0,-3,1,0,0,-92,23,2,0,-17,26,0,0,105,42,51,1,-7,1,0,0,0,2,0,0,-10,1,0,0,-7,1,0,0,-49,127,1,0,107,19,0,0,106,42,51,1,-11,1,0,0,-7,1,0,0,-25,1,0,0,-20,1,0,0,-68,44,1,0,-28,14,0,0,107,42,51,1,-17,1,0,0,-13,1,0,0,-23,1,0,0,-21,1,0,0,14,-90,0,0,49,8,0,0,108,42,51,1,-22,1,0,0,-18,1,0,0,-37,1,0,0,-35,1,0,0,35,28,1,0,-78,13,0,0,111,42,51,1,-40,1,0,0,-32,1,0,0,-44,1,0,0,-35,1,0,0,-58,-111,0,0,-25,6,0,0,112,42,51,1,-40,1,0,0,-28,1,0,0,-41,1,0,0,-29,1,0,0,51,117,0,0,-99,5,0,0,113,42,51,1,-29,1,0,0,-15,1,0,0,-32,1,0,0,-17,1,0,0,-91,-37,0,0,-76,10,0,0,114,42,51,1,-25,1,0,0,-6,1,0,0,-28,1,0,0,-19,1,0,0,-104,100,1,0,-89,17,0,0,115,42,51,1,-12,1,0,0,-5,1,0,0,-20,1,0,0,-10,1,0,0,-51,-125,1,0,115,19,0,0,118,42,51,1,-16,1,0,0,-5,1,0,0,-20,1,0,0,-14,1,0,0,121,11,1,0,80,13,0,0,119,42,51,1,-14,1,0,0,-9,1,0,0,-35,1,0,0,-31,1,0,0,120,15,1,0,69,13,0,0,120,42,51,1,-31,1,0,0,-19,1,0,0,-34,1,0,0,-21,1,0,0,50,-68,0,0,42,9,0,0,121,42,51,1,-22,1,0,0,-22,1,0,0,-40,1,0,0,-36,1,0,0,-80,34,1,0,-19,13,0,0,122,42,51,1,-34,1,0,0,-32,1,0,0,-46,1,0,0,-45,1,0,0,-69,-81,0,0,76,8,0,0,-58,42,51,1,-46,1,0,0,-46,1,0,0,-57,1,0,0,-54,1,0,0,1,-87,0,0,-64,7,0,0,-57,42,51,1,-52,1,0,0,-32,1,0,0,-52,1,0,0,-44,1,0,0,-104,-64,0,0,8,9,0,0,-54,42,51,1,-46,1,0,0,3,2,0,0,-46,1,0,0,-13,1,0,0,-84,-121,2,0,36,32,0,0,-53,42,51,1,-22,1,0,0,-13,1,0,0,-22,1,0,0,-17,1,0,0,-46,50,1,0,37,15,0,0,-52,42,51,1,-18,1,0,0,-14,1,0,0,-26,1,0,0,-19,1,0,0,-3,52,1,0,55,15,0,0,-51,42,51,1,-20,1,0,0,-9,1,0,0,-26,1,0,0,-12,1,0,0,-52,-88,1,0,7,21,0,0,-50,42,51,1,-12,1,0,0,15,2,0,0,-15,1,0,0,1,2,0,0,1,12,3,0,44,40,0,0,-47,42,51,1,-1,1,0,0,1,2,0,0,-16,1,0,0,-10,1,0,0,-62,-81,1,0,-82,21,0,0,-46,42,51,1,-3,1,0,0,31,2,0,0,-8,1,0,0,9,2,0,0,98,18,4,0,-109,54,0,0,-45,42,51,1,7,2,0,0,20,2,0,0,0,2,0,0,18,2,0,0,14,27,2,0,60,28,0,0,-44,42,51,1,11,2,0,0,32,2,0,0,10,2,0,0,17,2,0,0,26,-118,2,0,-113,34,0,0,-43,42,51,1,13,2,0,0,19,2,0,0,4,2,0,0,13,2,0,0,123,-43,1,0,-112,24,0,0,-40,42,51,1,9,2,0,0,46,2,0,0,8,2,0,0,34,2,0,0,92,89,3,0,20,46,0,0,-39,42,51,1,27,2,0,0,74,2,0,0,26,2,0,0,62,2,0,0,-2,66,4,0,62,61,0,0,-38,42,51,1,56,2,0,0,67,2,0,0,43,2,0,0,50,2,0,0,55,97,2,0,112,34,0,0,-37,42,51,1,45,2,0,0,62,2,0,0,40,2,0,0,46,2,0,0,66,29,2,0,-113,30,0,0,-36,42,51,1,46,2,0,0,89,2,0,0,43,2,0,0,86,2,0,0,82,-94,3,0,96,54,0,0,-33,42,51,1,82,2,0,0,104,2,0,0,74,2,0,0,88,2,0,0,8,-62,2,0,94,42,0,0,-32,42,51,1,82,2,0,0,85,2,0,0,58,2,0,0,79,2,0,0,-84,-8,1,0,-120,29,0,0,-31,42,51,1,77,2,0,0,93,2,0,0,71,2,0,0,86,2,0,0,-116,-105,1,0,55,24,0,0,-30,42,51,1,82,2,0,0,90,2,0,0,68,2,0,0,71,2,0,0,6,-85,1,0,20,25,0,0,-29,42,51,1,71,2,0,0,78,2,0,0,45,2,0,0,49,2,0,0,-90,-103,2,0,-32,37,0,0,43,43,51,1,52,2,0,0,61,2,0,0,43,2,0,0,51,2,0,0,-43,-123,1,0,7,22,0,0,44,43,51,1,48,2,0,0,51,2,0,0,24,2,0,0,32,2,0,0,-5,117,1,0,117,20,0,0,45,43,51,1,28,2,0,0,38,2,0,0,20,2,0,0,29,2,0,0,-118,-19,0,0,-25,12,0,0,46,43,51,1,32,2,0,0,32,2,0,0,20,2,0,0,21,2,0,0,101,-19,0,0,-59,12,0,0,47,43,51,1,23,2,0,0,28,2,0,0,7,2,0,0,12,2,0,0,98,2,1,0,-89,13,0,0,53,43,51,1,5,2,0,0,5,2,0,0,-22,1,0,0,-9,1,0,0,90,23,1,0,5,14,0,0,54,43,51,1,-6,1,0,0,15,2,0,0,-9,1,0,0,11,2,0,0,80,-3,0,0,44,13,0,0,57,43,51,1,11,2,0,0,27,2,0,0,3,2,0,0,17,2,0,0,13,19,1,0,-115,14,0,0,58,43,51,1,17,2,0,0,20,2,0,0,4,2,0,0,19,2,0,0,92,-61,0,0,64,10,0,0,59,43,51,1,16,2,0,0,17,2,0,0,1,2,0,0,12,2,0,0,97,-90,0,0,-83,8,0,0,60,43,51,1,-110,1,0,0,-101,1,0,0,-121,1,0,0,-119,1,0,0,-28,-108,0,0,-2,5,0,0,61,43,51,1,125,1,0,0,-122,1,0,0,117,1,0,0,126,1,0,0,48,-7,0,0,-124,9,0,0,64,43,51,1,126,1,0,0,-127,1,0,0,92,1,0,0,95,1,0,0,24,53,1,0,60,11,0,0,65,43,51,1,94,1,0,0,102,1,0,0,60,1,0,0,92,1,0,0,5,76,1,0,65,11,0,0,66,43,51,1,94,1,0,0,122,1,0,0,84,1,0,0,109,1,0,0,-40,-106,1,0,-83,14,0,0,67,43,51,1,108,1,0,0,117,1,0,0,84,1,0,0,96,1,0,0,-98,-65,1,0,20,16,0,0,68,43,51,1,86,1,0,0,127,1,0,0,85,1,0,0,110,1,0,0,33,-37,1,0,87,17,0,0,-115,43,51,1,109,1,0,0,122,1,0,0,104,1,0,0,119,1,0,0,85,105,1,0,113,13,0,0,-114,43,51,1,126,1,0,0,-99,1,0,0,116,1,0,0,-99,1,0,0,-66,101,6,0,77,66,0,0,-113,43,51,1,-93,1,0,0,-58,1,0,0,-101,1,0,0,-58,1,0,0,114,-11,8,0,-81,98,0,0,-112,43,51,1,-66,1,0,0,-22,1,0,0,-72,1,0,0,-25,1,0,0,77,56,11,0,-60,-121,0,0,-111,43,51,1,-32,1,0,0,-27,1,0,0,-54,1,0,0,-52,1,0,0,105,-95,8,0,-79,104,0,0,-108,43,51,1,-62,1,0,0,-32,1,0,0,-82,1,0,0,-67,1,0,0,84,40,8,0,-98,95,0,0,-107,43,51,1,-71,1,0,0,-22,1,0,0,-71,1,0,0,-22,1,0,0,15,54,8,0,38,99,0,0,-106,43,51,1,13,2,0,0,27,2,0,0,11,2,0,0,27,2,0,0,24,91,3,0,-7,45,0,0,-105,43,51,1,18,2,0,0,55,2,0,0,6,2,0,0,42,2,0,0,-64,43,16,0,24,-28,0,0,-104,43,51,1,38,2,0,0,66,2,0,0,31,2,0,0,34,2,0,0,-127,25,11,0,95,-98,0,0,-101,43,51,1,31,2,0,0,57,2,0,0,26,2,0,0,47,2,0,0,-39,112,9,0,-98,-123,0,0,-100,43,51,1,38,2,0,0,38,2,0,0,18,2,0,0,30,2,0,0,-62,56,8,0,78,113,0,0,-99,43,51,1,28,2,0,0,45,2,0,0,12,2,0,0,23,2,0,0,-38,85,8,0,-112,115,0,0,-98,43,51,1,13,2,0,0,54,2,0,0,4,2,0,0,48,2,0,0,-48,24,11,0,-101,-103,0,0,-97,43,51,1,41,2,0,0,104,2,0,0,39,2,0,0,66,2,0,0,21,71,15,0,47,-26,0,0,-94,43,51,1,43,2,0,0,87,2,0,0,28,2,0,0,67,2,0,0,6,113,10,0,-46,-103,0,0,-93,43,51,1,67,2,0,0,76,2,0,0,45,2,0,0,62,2,0,0,52,-36,7,0,115,114,0,0,-92,43,51,1,53,2,0,0,61,2,0,0,27,2,0,0,46,2,0,0,38,38,8,0,-104,115,0,0,-91,43,51,1,43,2,0,0,43,2,0,0,-4,1,0,0,-2,1,0,0,-106,10,7,0,113,95,0,0,-90,43,51,1,-3,1,0,0,17,2,0,0,-15,1,0,0,2,2,0,0,95,-30,4,0,10,64,0,0,-87,43,51,1,0,2,0,0,0,2,0,0,-38,1,0,0,-37,1,0,0,52,-33,4,0,55,61,0,0,-86,43,51,1,-34,1,0,0,-27,1,0,0,-57,1,0,0,-38,1,0,0,54,88,5,0,66,64,0,0,-85,43,51,1,-38,1,0,0,-22,1,0,0,-47,1,0,0,-46,1,0,0,-68,-71,4,0,-60,57,0,0,-15,43,51,1,-43,1,0,0,-9,1,0,0,-45,1,0,0,-10,1,0,0,-117,28,7,0,117,89,0,0,-14,43,51,1,-10,1,0,0,2,2,0,0,-21,1,0,0,-19,1,0,0,38,125,5,0,127,70,0,0,-11,43,51,1,-23,1,0,0,-15,1,0,0,-26,1,0,0,-15,1,0,0,86,-101,2,0,-42,32,0,0,-10,43,51,1,-17,1,0,0,11,2,0,0,-19,1,0,0,-3,1,0,0,-119,-12,4,0,-62,64,0,0,-9,43,51,1,-7,1,0,0,-4,1,0,0,-23,1,0,0,-20,1,0,0,64,-66,3,0,118,47,0,0,-8,43,51,1,-20,1,0,0,-13,1,0,0,-27,1,0,0,-16,1,0,0,-15,71,2,0,-49,28,0,0,-7,43,51,1,-17,1,0,0,-9,1,0,0,-36,1,0,0,-27,1,0,0,110,-100,2,0,-113,32,0,0,-4,43,51,1,-28,1,0,0,-19,1,0,0,-33,1,0,0,-22,1,0,0,-92,-41,2,0,60,35,0,0,-3,43,51,1,-22,1,0,0,-15,1,0,0,-31,1,0,0,-21,1,0,0,98,-95,2,0,18,33,0,0,-2,43,51,1,-22,1,0,0,28,2,0,0,-27,1,0,0,28,2,0,0,-47,30,8,0,76,107,0,0,-1,43,51,1,48,2,0,0,75,2,0,0,29,2,0,0,48,2,0,0,-69,93,13,0,27,-64,0,0,0,44,51,1,35,2,0,0,55,2,0,0,11,2,0,0,14,2,0,0,116,-39,7,0,-46,109,0,0,3,44,51,1,6,2,0,0,56,2,0,0,-12,1,0,0,47,2,0,0,-76,17,7,0,-90,98,0,0,4,44,51,1,39,2,0,0,71,2,0,0,30,2,0,0,59,2,0,0,59,-47,8,0,107,127,0,0,5,44,51,1,52,2,0,0,95,2,0,0,46,2,0,0,72,2,0,0,36,-125,9,0,-52,-114,0,0,6,44,51,1,74,2,0,0,92,2,0,0,52,2,0,0,63,2,0,0,29,73,8,0,19,123,0,0,7,44,51,1,58,2,0,0,67,2,0,0,29,2,0,0,47,2,0,0,72,15,6,0,-4,86,0,0,10,44,51,1,52,2,0,0,87,2,0,0,40,2,0,0,76,2,0,0,-128,127,7,0,-64,110,0,0,11,44,51,1,69,2,0,0,91,2,0,0,61,2,0,0,80,2,0,0,5,-86,7,0,-26,115,0,0,12,44,51,1,68,2,0,0,79,2,0,0,54,2,0,0,56,2,0,0,5,62,6,0,-28,91,0,0,13,44,51,1,51,2,0,0,61,2,0,0,36,2,0,0,44,2,0,0,68,-23,3,0,-36,55,0,0,14,44,51,1,42,2,0,0,68,2,0,0,31,2,0,0,39,2,0,0,25,30,5,0,122,73,0,0,86,44,51,1,28,2,0,0,47,2,0,0,18,2,0,0,43,2,0,0,-10,-65,2,0,-127,38,0,0,87,44,51,1,39,2,0,0,56,2,0,0,34,2,0,0,50,2,0,0,122,-77,2,0,-105,38,0,0,88,44,51,1,46,2,0,0,51,2,0,0,33,2,0,0,34,2,0,0,21,67,3,0,12,46,0,0,89,44,51,1,33,2,0,0,54,2,0,0,28,2,0,0,50,2,0,0,111,15,4,0,-11,57,0,0,90,44,51,1,48,2,0,0,72,2,0,0,46,2,0,0,58,2,0,0,37,97,5,0,-20,78,0,0,93,44,51,1,57,2,0,0,115,2,0,0,45,2,0,0,115,2,0,0,91,4,11,0,19,-87,0,0,94,44,51,1,118,2,0,0,-85,2,0,0,106,2,0,0,-122,2,0,0,-96,9,13,0,78,-42,0,0,95,44,51,1,-120,2,0,0,-115,2,0,0,98,2,0,0,115,2,0,0,6,11,7,0,93,113,0,0,96,44,51,1,109,2,0,0,-83,2,0,0,106,2,0,0,-109,2,0,0,51,-87,8,0,-6,-111,0,0,97,44,51,1,-123,2,0,0,-62,2,0,0,123,2,0,0,-88,2,0,0,53,0,9,0,-16,-102,0,0,100,44,51,1,-98,2,0,0,-43,2,0,0,-103,2,0,0,-49,2,0,0,60,78,9,0,-125,-88,0,0,101,44,51,1,-54,2,0,0,-22,2,0,0,-83,2,0,0,-66,2,0,0,-112,18,9,0,-12,-90,0,0,102,44,51,1,-75,2,0,0,-33,2,0,0,-75,2,0,0,-43,2,0,0,-74,74,6,0,-90,114,0,0,107,44,51,1,-41,2,0,0,30,3,0,0,-54,2,0,0,30,3,0,0,109,-52,9,0,-33,-65,0,0,108,44,51,1,30,3,0,0,90,3,0,0,14,3,0,0,43,3,0,0,69,-115,10,0,-11,-36,0,0,109,44,51,1,32,3,0,0,78,3,0,0,5,3,0,0,11,3,0,0,60,-119,8,0,98,-80,0,0,110,44,51,1,3,3,0,0,27,3,0,0,-18,2,0,0,-8,2,0,0,40,-72,5,0,-34,112,0,0,111,44,51,1,-5,2,0,0,15,3,0,0,-35,2,0,0,-20,2,0,0,-49,-58,4,0,-89,92,0,0,114,44,51,1,-23,2,0,0,37,3,0,0,-23,2,0,0,33,3,0,0,-123,-24,5,0,24,119,0,0,-64,44,51,1,48,3,0,0,72,3,0,0,28,3,0,0,56,3,0,0,-112,115,6,0,70,-121,0,0,-63,44,51,1,50,3,0,0,78,3,0,0,39,3,0,0,75,3,0,0,-37,-7,5,0,1,127,0,0,-62,44,51,1,112,3,0,0,-97,3,0,0,92,3,0,0,99,3,0,0,-24,-69,10,0,-74,-9,0,0,-61,44,51,1,83,3,0,0,-125,3,0,0,39,3,0,0,126,3,0,0,92,-3,8,0,39,-58,0,0,-58,44,51,1,112,3,0,0,-119,3,0,0,94,3,0,0,112,3,0,0,32,8,5,0,20,114,0,0,-57,44,51,1,105,3,0,0,126,3,0,0,91,3,0,0,98,3,0,0,-64,56,4,0,-113,94,0,0,-56,44,51,1,91,3,0,0,95,3,0,0,29,3,0,0,34,3,0,0,51,-50,5,0,-117,121,0,0,-55,44,51,1,39,3,0,0,55,3,0,0,-18,2,0,0,2,3,0,0,11,-120,4,0,50,91,0,0,-54,44,51,1,2,3,0,0,30,3,0,0,-7,2,0,0,20,3,0,0,-21,-29,2,0,39,58,0,0,-51,44,51,1,21,3,0,0,65,3,0,0,-1,2,0,0,56,3,0,0,86,-75,4,0,-44,97,0,0};
		if(exright_input!=null && exright_input.length>0){
			StructResponse sr = new StructResponse(exright_input);
			int len = sr.readShort();
			
			for(int i=0; i<len; i++){
				int date = sr.readInt();
				int multi_num = sr.readInt();
				int add_num = sr.readInt();

				rl.add(new ExRightUnit(date, multi_num, add_num));
				/*Log.e(TAG, "Date:" + date + " multi:" + multi + " add:" + add );*/
			}
		}else{
			Log.e(TAG, "no exrights data!");
		}

	
		return rl;
	}
	
	/**
	 * 得到复权后的K线
	 * 
	 * @param numDay 多少个交易日
	 * @param startDate 从哪个日期开始（包括,若没有使用右边最近的），-1从上市开始取， 0取最新的， 20130312普通格式
	 * 
	 * */
	public List<KLineUnit> getNDayExRightKline(int numDay, int endDate){
		List<KLineUnit> kl = getNDayKline(numDay, endDate);
		if(kl!=null){
			return StockHelper.getExrightKLine(kl, -1);
		}else{
			return null;
		}
	}
	
	/**
	 * 得到原始的K线
	 * 
	 * @param numDay 多少个交易日
	 * @param endDate 从哪个日期截至（包括,若没有使用左边最近的），-1从上市开始取， 0取最新的， 20130312普通格式
	 * 
	 * */
	public List<KLineUnit> getNDayKline(int numDay, int endDate){
		List<KLineUnit> kl = line.getExRightKline();
		List<KLineUnit> ret = null;
		if(!StockHelper.checkKlineVaild(kl)){
			return null;
		}
		
		if(numDay>kl.size()){
			numDay = kl.size();
		}
		
		if(numDay<1){
			numDay = 1;
		}
		
		if(endDate<0){
			ret = kl.subList(0, numDay);
			return ret;
		}else if(endDate==0){
			return kl.subList(kl.size()-numDay, kl.size());
		}else{
			KLineUnit elem = StockHelper.binSearch(kl, endDate, -1);
			if(elem!=null){
				int indexTo = kl.indexOf(elem)+1;
				int indexFrom = indexTo - numDay;
				if(indexTo>kl.size()){
					indexTo = kl.size();
				}
				return kl.subList(indexFrom, indexTo);
			}else{
				return null;
			}

		}
	}
	
	/**
	 * 得到复权的K线
	 * 
	 * @param numDay 多少个交易日
	 * @param endDate 从哪个日期截至（包括,若没有使用左边最近的），-1从上市开始取， 0取最新的， 20130312普通格式
	 * 
	 * */
	public List<KLineUnit> getNDayExRightKline(TradeUnit tu) {
		List<KLineUnit> kl = getNDayKline(tu);
		if(kl!=null){
			return StockHelper.getExrightKLine(kl, tu.buy_date);
		}else{
			return null;
		}
	}
	
	/**
	 * 得到原始的K线
	 * 
	 * @param numDay 多少个交易日
	 * @param endDate 从哪个日期截至（包括,若没有使用左边最近的），-1从上市开始取， 0取最新的， 20130312普通格式
	 * 
	 * */
	public List<KLineUnit> getNDayKline(TradeUnit tu) {
		// TODO Auto-generated method stub
		int prefixNDay = Constants.BUYPOINT_PREFIX_LEN;
		int posfixNDay = Constants.SELLPOINT_POSTFIX_LEN;
		
		KLineUnit buy = StockHelper.binSearch(line.kline_list, tu.buy_date, 0);
		KLineUnit sell = StockHelper.binSearch(line.kline_list, tu.sell_date, 0);
		int buy_index = line.kline_list.indexOf(buy);
		int sell_index = line.kline_list.indexOf(sell);
		int indexFrom = 0;
		int indexTo = 0;
		if(buy_index>prefixNDay){
			indexFrom=buy_index-prefixNDay;
		}else{
			indexFrom = 0;
		}
		if(sell_index+posfixNDay<line.kline_list.size()){
			indexTo = sell_index+posfixNDay;
		}else{
			indexTo = line.kline_list.size();
		}
		return line.kline_list.subList(indexFrom, indexTo);
	}
	
	public static void main(String[] args){
		StockInfo info = new StockInfo("600031", 1, "SYZG", 0, 0, "三一重工", 2013112216);
		Stock s = new Stock(info);
		s.fetchFromDZH(20121106, 20131106);
	}

	/**
	 * @param Nday 指定返回的list有几天
	 * @param reset 初始化pos
	 * @param lastBuyPoint 
	 * */
	public List<KLineUnit> generateNDayKline(int NDay, boolean reset, int lastBuyDate) {
		// TODO Auto-generated method stub
		if(NDay<5){
			NDay = 60;
		}
		
		if(reset){
			if(line!=null){
				line.reset(NDay);
			}
			
			return null;
		}else{
			return line.generateNDayKline(NDay, lastBuyDate);
		}
	}
	
	public void nextPos(){
		line.nextPos();
	}


}
