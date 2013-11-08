package com.zhangwei.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.mysql.Converter;
import com.zhangwei.util.DateHelper;

public class Kline {
	public ArrayList<KLineUnit> kline_list;
	public ArrayList<ExRightUnit> ex_rights;
	
	//最后联网查询的时间，而不是最后一个Kline的点，如果是下午3点前扫描的，算昨天。
	//如果联网失败，不更新
	//如果联网成功，结果为空，更新
	//如果联网成功，结果不为空，写入mysql失败，不更新
	//如果联网成功，结果不为空，写入mysql成功，更新
	int last_scan_day; 
	
	public Kline(StockInfo stockInfo){
		kline_list = null;
		ex_rights = null;
		last_scan_day = -1;
		
		
		try {
			//data_kline_<stock_id>_<market_type>
			//data_exrights_<stock_id>_<market_type>

			BaseDao dao = BaseDao.getInstance();
			
			String kline_table = "data_kline_" + stockInfo.stock_id + "_" + stockInfo.market_type;
			String exright_table = "data_exrights_" + stockInfo.stock_id + "_" + stockInfo.market_type;

			String sql_create_table_kline = "CREATE TABLE IF NOT EXISTS " +  kline_table + "\n(date INT PRIMARY KEY, open INT, high INT, low INT, close INT, vol INT, cje INT )ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			String sql_create_table_exright = "CREATE TABLE IF NOT EXISTS " + exright_table + "\n(date INT PRIMARY KEY, multi_num INT, add_num INT)ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			
			if(dao.exec(sql_create_table_kline) && dao.exec(sql_create_table_exright)){
				
			}

			String sql_kline = "select * from data_kline_" + stockInfo.stock_id + "_" + stockInfo.market_type;
			String sql_exrights = "select * from data_exrights_" + stockInfo.stock_id + "_" + stockInfo.market_type;
					
			List<Map<String, Object>> list_kline = dao.query(sql_kline);
			List<Map<String, Object>> list_exrights = dao.query(sql_exrights);
			
			kline_list = Converter.ListConvert2KLineUnit(list_kline);
			ex_rights = Converter.ListConvert2ExRightUnit(list_exrights);
			
			if(kline_list!=null && kline_list.size()>0){
				last_scan_day = kline_list.get(kline_list.size()-1).date;
			}
			
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public boolean outOfDate() {
		// TODO Auto-generated method stub
		if(kline_list!=null){
			if(last_scan_day>0 && last_scan_day>=DateHelper.Yesteray()){
				return false;
			}else{
				return true;
			}
		}
		return true;
	}
}
