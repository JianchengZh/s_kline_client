package com.zhangwei.stock.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.stock.BS.TradeUnit;
import com.zhangwei.stock.emu.FParallelEmuMarket;
import com.zhangwei.util.Format;

public class EmuTodayGenerater implements DayGenerater{
	int index;
	ArrayList<Integer> todays;
	
	public EmuTodayGenerater(String bs_table_name){
		index = 0;
		todays = new ArrayList<Integer>();
		//select buy_date,avg(earn_percent) from bs_pem7287298140871 group by buy_date HAVING COUNT(buy_date) > 0
		BaseDao bd = BaseDao.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("select buy_date from bs_table_name");
		sb.append(" group by buy_date");
		sb.append(";");
		
		try {
			List<Map<String, Object>> list = bd.query(sb.toString());
			
			if(list!=null && list.size()>0){
				for(Map<String, Object> item : list){
					int buy_date = Format.parserInt(item.get("buy_date").toString(), -1);/*(Integer)item.get("start");*/
					todays .add(buy_date);
				}

			}
			

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			FParallelEmuMarket se = new FParallelEmuMarket();
			se.run(false);
		}
	
	}

	@Override
	public int getToday() {
		// TODO Auto-generated method stub
		return todays.get(index);
	}

}
