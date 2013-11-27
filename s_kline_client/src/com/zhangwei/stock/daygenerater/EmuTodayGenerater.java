package com.zhangwei.stock.daygenerater;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.stock.emu.PreEmuProcesser;
import com.zhangwei.stock.strategy.BasicStrategy;
import com.zhangwei.util.Format;

public class EmuTodayGenerater implements DayGenerater{
	int index;
	ArrayList<Integer> todays;
	
	public EmuTodayGenerater(BasicStrategy bs){
		index = -1;

		//select buy_date,avg(earn_percent) from bs_pem7287298140871 group by buy_date HAVING COUNT(buy_date) > 0
		StringBuilder sb = new StringBuilder();
		sb.append("select buy_date from "); 
		sb.append(bs.getBSTableName());
		sb.append(" group by buy_date");
		sb.append(";");
		
		int tryCount = 2;
		for(int index=0; index<tryCount; index++){
			try {
				todays = new ArrayList<Integer>();
				QuerySQL(sb.toString());
				index=0;
				break;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				PreEmuProcesser se = new PreEmuProcesser();
				se.run(false);
			}
		}

	
	}
	
	public void QuerySQL(String sql) throws SQLException{
		BaseDao bd = BaseDao.getInstance();
		List<Map<String, Object>> list = bd.query(sql);
		
		if(list!=null && list.size()>0){
			for(Map<String, Object> item : list){
				int buy_date = Format.parserInt(item.get("buy_date").toString(), -1);/*(Integer)item.get("start");*/
				todays .add(buy_date);
			}
		}
	}
	

	@Override
	public int getToday() {
		// TODO Auto-generated method stub
		if(index>=0 && index<todays.size()){
			int ret =  todays.get(index);
			index++;
			return ret;
		}else{
			return -1;
		}

	}

}
