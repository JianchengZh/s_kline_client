package com.zhangwei.stock.emu;

import com.zhangwei.stock.MarketManager;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;

public class EmuMarket {
	public static void main(String[] args){
		MarketManager mm = MarketManager.getInstance();
		Stock s = mm.getStock(new StockInfo("600031", 1, "SYZG", -1, -1, "三一重工"), false);
		Stock s2 = mm.getStock(new StockInfo("002572", 2, "SFY", -1, -1, "索菲亚"), false);
		s.getNDayExRightKline(10, 0);
	}

}
