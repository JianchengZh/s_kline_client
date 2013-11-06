package com.zhangwei.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.stock.MarketManager;

public class Client {		
	private static final String TAG = "Client";
	public final String create_table_prefix = "create table if not exists";

	public List<Map<String, Object>> GetStockList(){
		try {
			BaseDao dao = BaseDao.getInstance();
			String sql = "select * from stocklist";
			List<Map<String, Object>> list = dao.query(sql);

			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Map<String, Object>> GetIndexList(){
		try {
			BaseDao dao = BaseDao.getInstance();
			String sql = "select * from zhishulist";
			List<Map<String, Object>> list = dao.query(sql);

			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args){
		Client ci = new Client();
/*		List<Map<String, Object>> rlt = ci.GetStockList();
		ci.DumpData(rlt);*/
		
		MarketManager.getInstance().FetchStockInfo(false);
		MarketManager.getInstance().FetchStockInfo(true);
	}
	
	public void DumpData(List<Map<String, Object>> list){
		for(Map<String, Object> item : list){
			for(String key : item.keySet()){
				Log.v(TAG, key + ":" + item.get(key));
			}
			System.out.println();
		}
		
		Log.v(TAG, "total:" + list.size());
	}

}
