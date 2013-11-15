package com.zhangwei.stock.basic;

import java.util.ArrayList;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.condition.ICondition;

/**
 * 先判断必要条件，若有一个为true，则直接返回true
 * 后判断充分条件, 若全部为true,才返回true
 * */
public class Strategy {
	
	ArrayList<ICondition> necessaryConditions;  //必要条件集合 
	ArrayList<ICondition> sufficientConditions; //充分条件集合

	public Strategy(){
		necessaryConditions = new ArrayList<ICondition>();
		sufficientConditions = new ArrayList<ICondition>();
	}
	
	public boolean check(StockInfo info, ArrayList<KLineUnit> kl, Point lastPoint){
		
		for(ICondition c : necessaryConditions){
			try {
				if(c.checkCondition(info, kl, lastPoint)){
					return true;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(ICondition c : sufficientConditions){
			try {
				if(!c.checkCondition(info, kl, lastPoint)){
					return false;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	public boolean isEmpty(){
		if(necessaryConditions.size()==0 && sufficientConditions.size()==0){
			return false;
		}
		
		return true;
	}
	
	public void addNecessaryCondition(ICondition c){
		necessaryConditions.remove(c);
		necessaryConditions.add(c);
	}
	
	public void addSufficientCondition(ICondition c){
		sufficientConditions.remove(c);
		sufficientConditions.add(c);
	}

}
