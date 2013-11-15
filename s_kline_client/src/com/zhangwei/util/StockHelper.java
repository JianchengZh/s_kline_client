package com.zhangwei.util;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.MarketManager;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.basic.KLineTypeResult;
import com.zhangwei.stock.basic.StockException;

public class StockHelper {
	private static final String TAG = "StockHelper";
	
	public static List<KLineUnit> getInterval(List<KLineUnit> kl, KLineUnit elem1, KLineUnit elem2){
		int index1 = kl.indexOf(elem1);
		int index2 = kl.indexOf(elem2);
		if(index1<0 || index2<0){
			return null;
		}
		
		if(index1<index2){
			return kl.subList(index1, index2+1);
		}else{
			return kl.subList(index2, index1+1);
		}
	}
	
	public static KLineTypeResult getKlineType(List<KLineUnit> kl) throws StockException{
		if(kl==null || kl.size()<2){
			throw new StockException("kl is null or size<2");
		}
		
		KLineUnit low = null;
		KLineUnit high = null;
		
		for(KLineUnit elem : kl){
			if(low==null || low.close>elem.close){
				low = elem;
			}
			
			if(high==null || high.close<high.close){
				high = elem;
			}
		}
		
		KLineTypeResult ret = new KLineTypeResult(kl.get(0), low, high, kl.get(kl.size()-1));
		
		return ret;
	} 

	public static ArrayList<KLineUnit> findExRightPoints(ArrayList<KLineUnit> kl){
		ArrayList<KLineUnit> ret = new ArrayList<>();
		if(kl!=null && kl.size()>1){
			KLineUnit last = kl.get(0);
			for(int i=1; i<kl.size(); i++){
				KLineUnit cur = kl.get(i);
				int fudu = calcExrightFactor(last, cur);
				if(fudu!=0){
					ret.add(last);
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * 从指定date开始，到kl最后一天，计算这之间的复权因子
	 * 若date找不到，且date在kl范围中，则使用离date最近的左端点
	 * */
	public static int getExrightFactor(List<KLineUnit> kl, int lastDate){
		KLineUnit ret = binSearch(kl, lastDate, -1);
		if(ret!=null){
			int index = kl.indexOf(ret);
			return getExrightFactor(kl.subList(index, kl.size()-1));
		}else{
			return getExrightFactor(kl);
		}
		
	}
	
	public static int getExrightFactor(List<KLineUnit> kl){
		int factorRet = 100;
		if(kl!=null && kl.size()>1){
			KLineUnit last = null;
			for(KLineUnit cur : kl){
				if(last!=null && !last.equals(cur)){
					factorRet = factorRet * calcExrightFactor(last, cur) / 100;
				}
				
				last = cur;
			}
		}
		
		return factorRet;
	}
	
	public static int calcExrightFactor(KLineUnit left, KLineUnit right){
		if(left.date<right.date){
			int fudu = 1000 * left.close / right.open /10;
			if(fudu>115){
				return fudu;
			}else{
				return 100;
			}
		}else{
			int fudu = 1000 * right.close / left.open /10;
			if(fudu>115){
				return fudu;
			}else{
				return 100;
			}
		}
	}
	
	/**
	 * @param allowBorder <0 在找不到的情况下，允许使用左边最近的元素代替； >0 右边； ==0 不允许，直接返回-1
	 * */
	public static KLineUnit binSearch(List<KLineUnit> kl, int giveDate, int allowBorder){
		if(kl!=null && kl.size()>0){
			KLineUnit left = kl.get(0);
			KLineUnit right = kl.get(kl.size()-1);
			
/*			if((giveDate<left.date && giveDate<right.date) || (giveDate>left.date && giveDate>right.date)){
				return null;
			}*/
			
			if(kl.size()==1){
				if(left.date==giveDate){
					return left;
				}else if((left.date>giveDate && allowBorder>0) || (left.date<giveDate && allowBorder<0)){
					return left;
				}else{
					return null;
				}
			}else{
				int midsize = kl.size() / 2;
				int midIndex = midsize - 1;
				KLineUnit midleft = kl.get(midIndex);
				KLineUnit midright = kl.get(midIndex+1);
				
				if(midleft.date>=giveDate){ //搜索左半部分
					return binSearch(kl.subList(0, midIndex+1), giveDate, allowBorder);
				}else if(midright.date<=giveDate){//搜索右半部分
					return binSearch(kl.subList(midIndex+1, kl.size()), giveDate, allowBorder);
				}else{ //落到 midleft 和 midright之间
					if(allowBorder==0){
						return null;
					}else if(allowBorder<0){ //使用midleft
						return midleft;
					}else{ //使用midright
						return midright;
					}
				}

			}
		}else{
			return null;
		}
	}
	
	public static void main(String[] args){
		MarketManager mm = MarketManager.getInstance();
		Stock s = mm.getStock(new StockInfo("600031", 1, "SYZG", -1, -1, "三一重工"), false);
		//Stock s2 = mm.getStock(new StockInfo("002572", 2, "SFY", -1, -1, "索菲亚"), false);
		
		int exrightFactor = getExrightFactor(s.line.kline_list, 20100614);
		Log.v(TAG, "exrightFactor: " + exrightFactor);
		
		int exrightFactor2 = getExrightFactor(s.line.kline_list, 20131115);
		Log.v(TAG, "exrightFactor2: " + exrightFactor2);
		
		int exrightFactor3 = getExrightFactor(s.line.kline_list, 20130618);
		Log.v(TAG, "exrightFactor3: " + exrightFactor3);
	}

	/**
	 *  elem1 elem2顺序可以任意
	 *  Percent , Range >0
	 * */
	public static boolean checkRange(KLineUnit elem1, KLineUnit elem2, int Percent, int Range, boolean up) {
		// TODO Auto-generated method stub
		int fudu = 0;
		if(elem1.date<elem2.date){
			fudu = (elem2.close * 100 / elem1.close) -1;
		}else{
			fudu = (elem1.close * 100 / elem2.close) -1;
		}
		
		if(up){
			if(Range>0){
				return (fudu>=Percent) && (fudu<=Percent+Range);
			}else{
				return (fudu>=Percent);
			}
		}else{
			fudu = -fudu;
			if(Range>0){
				return (fudu>=Percent) && (fudu<=Percent+Range);
			}else{
				return (fudu>=Percent);
			}
		}

	}
	
	public static boolean checkKlineVaild(List<KLineUnit> kl){
		if(kl==null || kl.size()<2){
			return false;
		}
		
		return true;
	}

}
