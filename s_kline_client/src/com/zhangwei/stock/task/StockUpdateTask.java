package com.zhangwei.stock.task;

import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;

public class StockUpdateTask implements StockTask{
	private StockInfo si;

	public StockUpdateTask(StockInfo si){
		this.si = si;
	}

	@Override
	public void processTask() {
		// TODO Auto-generated method stub
		StockManager sm = StockManager.getInstance();
		sm.getStock(si, false);
	}

}
