package com.zhangwei.stock.emu;

import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.IBuy;
import com.zhangwei.stock.BS.ISell;
import com.zhangwei.stock.BS.SellPoint;
import com.zhangwei.stock.BS.TradeSystem;

public class EmuTradeSystem implements TradeSystem{
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
	
	@Override
	public void submitBuyTransaction(IBuy buy){
		buy.onSucess();
	}

	@Override
	public void completeBuyTransaction(BuyPoint buypoint) {
		// TODO Auto-generated method stub
		records.addBuy(buypoint);
	}
	
	@Override
	public void submitSellTransaction(ISell sell){
		sell.onSucess();
	}

	@Override
	public void completeSellTransaction(BuyPoint buypoint, SellPoint sellpoint) {
		// TODO Auto-generated method stub
		records.addSell(buypoint, sellpoint);
	}

}
