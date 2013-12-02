package com.zhangwei.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.github.jamm.MemoryMeter;

import android.util.Log;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EntryWeigher;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
/*import com.zhangwei.cache.MemoryMeter;*/
import com.zhangwei.mysql.BaseDao;
import com.zhangwei.util.Format;

public class StockInfoManager {
	private static final String TAG = "StockInfoManager";
	private static StockInfoManager ins;
	private ConcurrentLinkedHashMap<String, StockInfo> cache;
	
	//
	private StockInfoManager(){		
		cache = new ConcurrentLinkedHashMap.Builder<String, StockInfo>()
				.maximumWeightedCapacity(1* 1024 * 1024 * 1024) // 4GB
			    .weigher(memoryUsageWeigher)
			    .listener(listener)
			    .build();
		
		ArrayList<StockInfo> stock_infos = FetchStockInfo(false, null, -1);
		if(stock_infos!=null && stock_infos.size()>0){
			for(StockInfo elem : stock_infos){
				cache.put(elem.getKey(), elem);
			}

		}
		
		ArrayList<StockInfo> index_infos = FetchStockInfo(true, null, -1);
		if(index_infos!=null && index_infos.size()>0){
			for(StockInfo elem : index_infos){
				cache.put(elem.getKey(), elem);
			}

		}
	}
	
	private EntryWeigher<String, StockInfo> memoryUsageWeigher = new EntryWeigher<String, StockInfo>() {
		  final MemoryMeter meter = new MemoryMeter();

		  @Override 
		  public int weightOf(String key, StockInfo value) {
		    long bytes = meter.measure(key) + meter.measure(value);
			  //long bytes = value.fileSize;//

		    return (int) Math.min(bytes, Integer.MAX_VALUE);
		  }
	};
	
	private EvictionListener<String, StockInfo> listener = new EvictionListener<String, StockInfo>() {
		  @Override 
		  public void onEviction(String key, StockInfo value) {
			    //rm the key(file name)
			    Log.e(TAG, "Evicted(delete) key=" + key);
		  }
	};
	//
	
	public static StockInfoManager getInstance(){
		if(ins==null){
			ins = new StockInfoManager();
		}
		
		return ins;
	}
	
	public synchronized ArrayList<StockInfo> FetchStockInfo(boolean index, String arg_stock_id, int arg_market_type){
		ArrayList<StockInfo> rlt = new ArrayList<StockInfo>();
		
		try {
			BaseDao dao = BaseDao.getInstance();	
			
			StringBuilder sql = new StringBuilder();
			if(index){
				sql.append("select * from zhishulist");
			}else{
				sql.append("select * from stocklist");
			}
			
			boolean hasStockID = false;
			if(arg_stock_id!=null && !arg_stock_id.equals("")){
				sql.append(" where stock_id='" + arg_stock_id + "'");
				hasStockID = true;
			}
			
			if(arg_market_type>=0){
				if(hasStockID){
					sql.append(" and market_type=" + arg_market_type);
				}else{
					sql.append(" where market_type=" + arg_market_type);
				}
			}
			
			List<Map<String, Object>> list = dao.query(sql.toString());
			
			if(list!=null && list.size()>0){
				for(Map<String, Object> item : list){
					String stock_id =  (String) item.get("stock_id");
					String name = (String) item.get("name");
					int market = Format.parserInt(item.get("market_type").toString(), -1);
					String quick = (String) item.get("quick");
					int start = Format.parserInt(item.get("start").toString(), -1);/*(Integer)item.get("start");*/
					int last = Format.parserInt(item.get("last").toString(), -1);/*(Integer)item.get("last");*/
					int scan = Format.parserInt(item.get("scan").toString(), -1);
					StockInfo si = new StockInfo(stock_id, market, quick, start, last, name, scan);
					rlt.add(si);
				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Log.v(TAG, "FetchStockInfo - index:" + index + ", result num:" + rlt.size());
		
		return rlt;
	}
	
	
	public synchronized StockInfo getStockInfo(String stock_id, int market_type) {
		// TODO Auto-generated method stub

		if(stock_id==null || market_type<=0){

			return null;
		}

		String key =  stock_id + "." + market_type;
		
		//先内存
		StockInfo info = cache.get(key);
		
		//再sql
		if(info==null){
			StockInfoManager sm = StockInfoManager.getInstance();
			ArrayList<StockInfo> infos = null;
			if(market_type==1 || market_type==2){
				infos = sm.FetchStockInfo(false, stock_id, market_type);
			}else{
				infos = sm.FetchStockInfo(true, stock_id, market_type);
			}
			
			if(infos!=null && infos.size()==1){
				info = infos.get(0);
				cache.put(info.getKey(), info);
			}else{
				return null;
			}

		}
		
		
		return info;
	}
	
	
	public static void main(String[] args){
		StockInfoManager sm = StockInfoManager.getInstance();

	}
	




}
