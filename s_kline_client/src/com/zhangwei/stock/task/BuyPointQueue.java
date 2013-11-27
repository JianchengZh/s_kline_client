package com.zhangwei.stock.task;

import java.util.ArrayList;

import com.zhangwei.stock.bs.BuyPoint;

public class BuyPointQueue {
	ArrayList<BuyPoint> pendingBuyList;
	
	public BuyPointQueue(){
		pendingBuyList = new ArrayList<BuyPoint>();
	}
	
	public synchronized ArrayList<BuyPoint> getBPList(){
		return pendingBuyList;
	}
	
	
	public synchronized void addBuyPoint(BuyPoint bp){
		pendingBuyList.add(bp);
	}
	
	public synchronized void removeBuyPoint(BuyPoint bp){
		pendingBuyList.remove(bp);
	}

}
