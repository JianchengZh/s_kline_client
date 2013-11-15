package com.zhangwei.stock.basic;

import java.util.ArrayList;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;

/**
 * 先判断必要条件，若有一个为true，则直接返回true
 * 后判断充分条件, 若全部为true,才返回true
 * */
public class Strategy {
	
	ArrayList<Condition> necessaryConditions;  //必要条件集合 
	ArrayList<Condition> sufficientConditions; //充分条件集合

	public Strategy(){
		necessaryConditions = new ArrayList<Condition>();
		sufficientConditions = new ArrayList<Condition>();
	}
	
	public boolean check(StockInfo info, ArrayList<KLineUnit> kl, Point lastPoint){
		
		for(Condition c : necessaryConditions){
			try {
				if(c.checkCondition(info, kl, lastPoint)){
					return true;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(Condition c : sufficientConditions){
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
	
	public void addNecessaryCondition(Condition c){
		necessaryConditions.remove(c);
		necessaryConditions.add(c);
	}
	
	public void addSufficientCondition(Condition c){
		sufficientConditions.remove(c);
		sufficientConditions.add(c);
	}

}
