package com.zhangwei.stock.emu;

import android.util.Log;

import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.SellPoint;

public class EmuTradeRecorder {

	private static final String TAG = "EmuTradeRecorder";

	public void addBuy(BuyPoint buypoint) {
		// TODO Auto-generated method stub
		Log.v(TAG, buypoint.toString());
	}

	public void addSell(SellPoint sellpoint) {
		// TODO Auto-generated method stub
		Log.v(TAG, sellpoint.toString());
	}

}
