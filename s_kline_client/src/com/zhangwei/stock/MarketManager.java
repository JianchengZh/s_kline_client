package com.zhangwei.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EntryWeigher;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import com.zhangwei.cache.MemoryMeter;
import com.zhangwei.mysql.BaseDao;
import com.zhangwei.util.Format;

public class MarketManager {
	private static final String TAG = "MarketManager";
	private static MarketManager ins;
	private ConcurrentLinkedHashMap<String, Stock> cache;
	
	//
	private MarketManager(){		
		cache = new ConcurrentLinkedHashMap.Builder<String, Stock>()
				.maximumWeightedCapacity(100 * 1024 * 1024) // 100 MB, internal storage, not memory
			    .weigher(memoryUsageWeigher)
			    .listener(listener)
			    .build();
	}
	
	private EntryWeigher<String, Stock> memoryUsageWeigher = new EntryWeigher<String, Stock>() {
		  final MemoryMeter meter = new MemoryMeter();

		  @Override 
		  public int weightOf(String key, Stock value) {
		    long bytes = meter.measure(key) + meter.measure(value);
			  //long bytes = value.fileSize;//

		    return (int) Math.min(bytes, Integer.MAX_VALUE);
		  }
	};
	
	private EvictionListener<String, Stock> listener = new EvictionListener<String, Stock>() {
		  @Override 
		  public void onEviction(String key, Stock value) {
			    //rm the key(file name)
			    Log.e(TAG, "Evicted(delete) key=" + key);
		  }
	};
	//
	
	public static MarketManager getInstance(){
		if(ins==null){
			ins = new MarketManager();
		}
		
		return ins;
	}
	
	public synchronized ArrayList<StockInfo> FetchStockInfo(boolean index){
		ArrayList<StockInfo> rlt = new ArrayList<StockInfo>();
		
		try {
			BaseDao dao = BaseDao.getInstance();	
			
			String sql = null;
			if(index){
				sql = "select * from zhishulist";
			}else{
				sql = "select * from stocklist";
			}
			
			List<Map<String, Object>> list = dao.query(sql);		
			
			if(list!=null && list.size()>0){
				for(Map<String, Object> item : list){
					String stock_id =  (String) item.get("stock_id");
					String name = (String) item.get("name");
					int market = Format.parserInt((String) item.get("market_type"), -1);
					String quick = (String) item.get("quick");
					int start = Format.parserInt((String) item.get("start"), -1);
					int last = Format.parserInt((String) item.get("last"), -1);
							
					StockInfo si = new StockInfo(stock_id, market, quick, start, last, name);
					rlt.add(si);
				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.v(TAG, "FetchStockInfo - index:" + index + ", result num:" + rlt.size());
		
		return rlt;
	}
	
	public synchronized Stock getStock(StockInfo info){
		Stock stock = cache.get(info.getKey());
		if(stock==null){
			stock = new Stock(info);
			cache.put(info.getKey(), stock);
		}else if(stock.outOfDate()){
			cache.remove(info.getKey());
			stock.update();
			cache.put(info.getKey(), stock);
		}
		
		return stock;
	}

}
