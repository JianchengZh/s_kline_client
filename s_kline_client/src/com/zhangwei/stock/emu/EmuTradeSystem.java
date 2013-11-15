package com.zhangwei.stock.emu;

import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.SellPoint;

public class EmuTradeSystem {
	EmuTradeRecorder records;
	private static EmuTradeSystem ins;
	private EmuTradeSystem(){
		records = new EmuTradeRecorder();
	}
	
	public static EmuTradeSystem getInstance(){
		if(ins==null){
			ins = new EmuTradeSystem();
		}
		
		return ins;
	}
	
	public void submitBuyTransaction(EmuBuyTransaction buy){
		buy.onSucess();
	}

	public void completeBuyTransaction(BuyPoint buypoint) {
		// TODO Auto-generated method stub
		records.addBuy(buypoint);
	}
	
	public void submitSellTransaction(EmuSellTransaction sell){
		sell.onSucess();
	}

	public void completeSellTransaction(SellPoint sellpoint) {
		// TODO Auto-generated method stub
		records.addSell(sellpoint);
	}

}
