package com.zhangwei.stock;

import java.util.ArrayList;

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
