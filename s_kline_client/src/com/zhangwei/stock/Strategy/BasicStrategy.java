package com.zhangwei.stock.Strategy;

import java.util.ArrayList;
import java.util.List;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.Point;
import com.zhangwei.stock.BS.SellPoint;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.condition.ICondition;

/**
 * 先判断必要条件，若有一个为true，则直接返回true
 * 后判断充分条件, 若全部为true,才返回true
 * */
public abstract class BasicStrategy {
	
	public ArrayList<ICondition> buyBigConditions;  //独享条件集合 
	public ArrayList<ICondition> buyLittleConditions; //组合条件集合
	
	public ArrayList<ICondition> sellBigConditions;  //独享条件集合 
	public ArrayList<ICondition> sellLittleConditions; //组合条件集合

	public BasicStrategy(){
		buyBigConditions = new ArrayList<ICondition>();
		buyLittleConditions = new ArrayList<ICondition>();
		
		sellBigConditions = new ArrayList<ICondition>();
		sellLittleConditions = new ArrayList<ICondition>();
	}
	
	public boolean checkBuy(StockInfo info, List<KLineUnit> kl, SellPoint lastPoint){
		
		for(ICondition c : buyBigConditions){
			try {
				if(c.checkCondition(info, kl, lastPoint)){
					return true;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(buyLittleConditions.size()>0){
			for(ICondition c : buyLittleConditions){
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
		}else{
			return false;
		}

		

	}
	
	public boolean checkSell(StockInfo info, List<KLineUnit> kl, BuyPoint lastPoint){
		
		for(ICondition c : sellBigConditions){
			try {
				if(c.checkCondition(info, kl, lastPoint)){
					return true;
				}
			} catch (StockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(sellLittleConditions.size()>0){
			for(ICondition c : sellLittleConditions){
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
		}else{
			return false;
		}

	}
	
	public boolean isEmpty(){
		if(buyBigConditions.size()==0 && buyLittleConditions.size()==0){
			return false;
		}
		
		if(sellBigConditions.size()==0 && sellLittleConditions.size()==0){
			return false;
		}
		
		return true;
	}
	
	public void addBuyNecessaryCondition(ICondition c){
		buyBigConditions.remove(c);
		buyBigConditions.add(c);
	}
	
	public void addBuySufficientCondition(ICondition c){
		buyLittleConditions.remove(c);
		buyLittleConditions.add(c);
	}
	
	public void addSellNecessaryCondition(ICondition c){
		sellBigConditions.remove(c);
		sellBigConditions.add(c);
	}
	
	public void addSellSufficientCondition(ICondition c){
		sellLittleConditions.remove(c);
		sellLittleConditions.add(c);
	}

}
