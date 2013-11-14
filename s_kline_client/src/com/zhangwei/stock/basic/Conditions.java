package com.zhangwei.stock.basic;

import java.util.ArrayList;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;

/**
 * 先判断必要条件，若有一个为true，则直接返回true
 * 后判断充分条件, 若全部为true,才返回true
 * */
public class Conditions {
	
	ArrayList<Condition> necessaryConditions;  //必要条件集合 
	ArrayList<Condition> sufficientConditions; //充分条件集合

	public Conditions(){
		necessaryConditions = new ArrayList<Condition>();
		sufficientConditions = new ArrayList<Condition>();
	}
	
	public boolean check(StockInfo info, ArrayList<KLineUnit> kl, Point lastPoint){
		
		for(Condition c : necessaryConditions){
			if(c.checkCondition(info, kl, lastPoint)){
				return true;
			}
		}
		
		for(Condition c : sufficientConditions){
			if(!c.checkCondition(info, kl, lastPoint)){
				return false;
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
