package com.zhangwei.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhangwei.stock.ExRightUnit;
import com.zhangwei.stock.KLineUnit;
import com.zhangwei.util.Format;

public class Converter {
	public static ArrayList<KLineUnit> ListConvert2KLineUnit(List<Map<String, Object>> input){
		ArrayList<KLineUnit> ret = new ArrayList<KLineUnit>();
		if(input!=null && input.size()>0){
			for(Map<String, Object> item : input){
				int date = Format.parserInt(item.get("date").toString(), -1);
				int open = Format.parserInt(item.get("open").toString(), -1);
				int high = Format.parserInt(item.get("high").toString(), -1);
				int low = Format.parserInt(item.get("low").toString(), -1);
				int close = Format.parserInt(item.get("close").toString(), -1);
				int vol = Format.parserInt(item.get("vol").toString(), -1);
				int cje = Format.parserInt(item.get("cje").toString(), -1);
				KLineUnit ku = new KLineUnit(date, open, high, low, close, vol, cje);
				ret.add(ku);
			}
		}
		return ret;
	}
	
	public static ArrayList<ExRightUnit> ListConvert2ExRightUnit(List<Map<String, Object>> input){
		ArrayList<ExRightUnit> ret = new ArrayList<ExRightUnit>();
		if(input!=null && input.size()>0){
			for(Map<String, Object> item : input){
				int date = Format.parserInt(item.get("date").toString(), -1);
				int multi = Format.parserInt(item.get("multi_num").toString(), -1);
				int add = Format.parserInt(item.get("add_num").toString(), -1);

				ExRightUnit ku = new ExRightUnit(date, multi, add);
				ret.add(ku);
			}
		}
		return ret;
	}
	
	public static ArrayList<KLineUnit> ListMergeKLineUnit(ArrayList<KLineUnit> left, ArrayList<KLineUnit> right){
		ArrayList<KLineUnit> ret = new ArrayList<KLineUnit>();
		if(left!=null && left.size()>0){
			if(right!=null && right.size()>0){
				int index_l = 0;
				int index_r = 0;
				KLineUnit cur_l = null;
				KLineUnit cur_r = null;
				while(left.size()>index_l || right.size()>index_r){
					if(index_l>=left.size()){
						cur_l = null;
					}else{
						cur_l = left.get(index_l);
					}
					
					if(index_r>=right.size()){
						cur_r = null;
					}else{
						cur_r = right.get(index_r);
					}
					
					if(cur_l!=null && cur_r!=null){
						if(cur_l.date<=cur_r.date){
							ret.add(cur_l);
							index_l++;
						}else{
							ret.add(cur_r);
							index_r++;
						}
					}else if(cur_l==null && cur_r!=null){
						ret.add(cur_r);
						index_r++;
					}else if(cur_l!=null && cur_r==null){
						ret.add(cur_l);
						index_l++;
					}


				}
				
				return ret;
				
			}else{
				return left;
			}

		}else{
			if(right!=null && right.size()>0){
				return right;
			}else{
				return ret;
			}
		}

	}
	
	public static List<Object[]> ListConvertKLine2Object(List<KLineUnit> kl){
		List<Object[]> ret = new ArrayList<Object[]>();
		if(kl!=null && kl.size()>0){
			for(KLineUnit item : kl){
				Object[] elem = new Object[7];
				elem[0] = item.date;
				elem[1] = item.open;
				elem[2] = item.high;
				elem[3] = item.low;
				elem[4] = item.close;
				elem[5] = item.vol;
				elem[6] = item.cje;
				ret.add(elem);
			}
		}
		
		return ret;
	}
	
	public static List<Object[]> ListConvertExright2Object(List<ExRightUnit> rl){
		List<Object[]> ret = new ArrayList<Object[]>();
		if(rl!=null && rl.size()>0){
			for(ExRightUnit item : rl){
				Object[] elem = new Object[3];
				elem[0] = item.date;
				elem[1] = item.multi_num;
				elem[2] = item.add_num;

				ret.add(elem);
			}
		}
		
		return ret;
	}

}
