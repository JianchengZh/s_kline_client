package com.zhangwei.stock;

import java.sql.SQLException;
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
import com.zhangwei.stock.emu.ParallelManager;
import com.zhangwei.stock.task.StockUpdateTask;
import com.zhangwei.util.Format;

public class StockManager {
	private static final String TAG = "StockManager";
	private static StockManager ins;
	private ConcurrentLinkedHashMap<String, Stock> cache;
	
	//
	private StockManager(){		
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
	
	public static StockManager getInstance(){
		if(ins==null){
			ins = new StockManager();
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
					int market = Format.parserInt(item.get("market_type").toString(), -1);
					String quick = (String) item.get("quick");
					int start = Format.parserInt(item.get("start").toString(), -1);/*(Integer)item.get("start");*/
					int last = Format.parserInt(item.get("last").toString(), -1);/*(Integer)item.get("last");*/
							
					StockInfo si = new StockInfo(stock_id, market, quick, start, last, name);
					rlt.add(si);
				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Log.v(TAG, "FetchStockInfo - index:" + index + ", result num:" + rlt.size());
		
		return rlt;
	}
	
	public synchronized Stock getStock(StockInfo info, boolean force){
		Log.v(TAG, "getStock - stock_id:" + info.stock_id + ", force:" + force);
		//先内存
		Stock stock = cache.get(info.getKey());
		
		//再sql
		if(stock==null){
			stock = new Stock(info);
			cache.put(info.getKey(), stock);
		}
		
		//最后网络
		if(stock.outOfDate()){
			cache.remove(info.getKey());
			stock.updateSQL(force); //sync NET and persit to SQL
			
			stock = new Stock(info); //reload from SQL
			cache.put(info.getKey(), stock);
		}
		
		return stock;
	}
	
	public static void main(String[] args){
		StockManager sm = StockManager.getInstance();
		ArrayList<StockInfo> stocks = sm.FetchStockInfo(false);
		for(StockInfo item : stocks){
			//sm.getStock(item, false);
			ParallelManager.getInstance().submitTask(new StockUpdateTask(item));
		}
		//Stock s = mm.getStock(new StockInfo("600031", 1, "SYZG", -1, -1, "三一重工"));
		//Stock s2 = mm.getStock(new StockInfo("002572", 2, "SFY", -1, -1, "索菲亚"));
	}
	
	public void createTable(StockInfo info){
		Log.v(TAG, "createTable - In, stock_id:" + info.stock_id);
		BaseDao dao = BaseDao.getInstance();
		String kline_table = "data_kline_" + info.stock_id + "_" + info.market_type;
		String exright_table = "data_exrights_" + info.stock_id + "_" + info.market_type;
		
		String sql_create_table_kline = "CREATE TABLE IF NOT EXISTS " +  kline_table + "\n(date INT PRIMARY KEY, open INT, high INT, low INT, close INT, vol INT, cje INT )ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		String sql_create_table_exright = "CREATE TABLE IF NOT EXISTS " + exright_table + "\n(date INT PRIMARY KEY, multi_num INT, add_num INT)ENGINE=InnoDB DEFAULT CHARSET=utf8;";
		
		Log.v(TAG, "sql_create_table_kline");
		try {
			dao.exec(sql_create_table_kline);
			dao.exec(sql_create_table_exright);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.v(TAG, "createTable - Out");

	}

}
