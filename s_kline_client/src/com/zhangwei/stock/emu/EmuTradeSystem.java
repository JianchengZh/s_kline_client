package com.zhangwei.stock.emu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.stock.Constants;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.BS.BuyPoint;
import com.zhangwei.stock.BS.IBuy;
import com.zhangwei.stock.BS.ISell;
import com.zhangwei.stock.BS.SellPoint;
import com.zhangwei.stock.BS.TradeSystem;
import com.zhangwei.stock.BS.TradeUnit;
import com.zhangwei.stock.gui.GuiManager;
import com.zhangwei.util.Format;

public class EmuTradeSystem implements TradeSystem{
	private static final String TAG = "EmuTradeSystem";
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

	public List<TradeUnit> getTradeInfo(String uid, int type) {
		// TODO Auto-generated method stub
		BaseDao bd = BaseDao.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from BS_" + uid + " where sell_date!=0");
		if(type>0){
			sb.append(" and buy_price<sell_price");
		}else if(type<0){
			sb.append(" and buy_price>sell_price");
		}
		List<TradeUnit> rlt = new ArrayList<TradeUnit>();
		
		try {
			List<Map<String, Object>> list = bd.query(sb.toString());
			
			if(list!=null && list.size()>0){
				for(Map<String, Object> item : list){
					String stock_id =  (String) item.get("stock_id");
					int market = Format.parserInt(item.get("market_type").toString(), -1);
					int buy_date = Format.parserInt(item.get("buy_date").toString(), -1);/*(Integer)item.get("start");*/
					int sell_date = Format.parserInt(item.get("sell_date").toString(), -1);/*(Integer)item.get("last");*/
					int buy_price = Format.parserInt(item.get("buy_price").toString(), -1);/*(Integer)item.get("start");*/
					int sell_price = Format.parserInt(item.get("sell_price").toString(), -1);/*(Integer)item.get("last");*/
					int vol = Format.parserInt(item.get("vol").toString(), -1);/*(Integer)item.get("last");*/
					
					TradeUnit si = new TradeUnit(stock_id, market, buy_date, sell_date, buy_price, sell_price, vol);

					rlt .add(si);
				}

			}
			

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
/*		if(rlt!=null && rlt.size()>0){
			Report(rlt);
		}*/
		
		return rlt;
	}

	public void Report(List<TradeUnit> rlt) {
		// TODO Auto-generated method stub
		Log.v(TAG, "Report rlt.size:" + rlt.size());

		
		int totalNum = 0;
		int earnNum = 0;
		int lossNum = 0;
		int earnPercentExpectSum = 0;
		int lossPercentExpectSum = 0;
		int totalPercentExpectSum = 0;
		int totalPercentExpectProduct = 100;
		int minEarnPercent = 100000;
		int maxEarnPercent = 0;
		int minEarnPercentExpectSum = 100000;
		int minEarnPercentExpectProduct = 100000;
		
		for(TradeUnit elem : rlt){
			int earnPercent = elem.getEarnPercent();
			if(minEarnPercent>earnPercent){
				minEarnPercent = earnPercent;
			}
			
			if(maxEarnPercent<earnPercent){
				maxEarnPercent = earnPercent;
			}
			
			if(earnPercent>0){
				earnNum++;
				earnPercentExpectSum = earnPercentExpectSum + earnPercent;
			}else if(earnPercent<0){
				lossNum++;
				lossPercentExpectSum = lossPercentExpectSum + earnPercent;
			}
			
			totalNum++;

			totalPercentExpectSum= totalPercentExpectSum + earnPercent;
			totalPercentExpectProduct = totalPercentExpectProduct * (100 + earnPercent) / 100;
			
			if(minEarnPercentExpectSum>totalPercentExpectSum/totalNum){
				minEarnPercentExpectSum=totalPercentExpectSum/totalNum;
			}
			
			if(minEarnPercentExpectProduct>totalPercentExpectProduct){
				minEarnPercentExpectProduct=totalPercentExpectProduct;
			}
			
			//Log.v(null, "earnPercent:" + earnPercent + ", earnPercentExpectSum:" + earnPercentExpectSum/totalNum + ", earnPercentExpectProduct:" + earnPercentExpectProduct);
		}
		
		double totalPercentExpectSum_double = (double)totalPercentExpectSum / totalNum;
		double earnPercentExpectSum_double = (double)earnPercentExpectSum / earnNum;
		double lossPercentExpectSum_double = (double)lossPercentExpectSum / lossNum;
				
		Log.v(null, "===================Report!=========================");
		Log.v(null, "=== Total Trades:" + totalNum);
		Log.v(null, "=== earnNum:" + earnNum + ", percent:" + earnNum * 100 / totalNum + ", 期望:" + earnPercentExpectSum_double);
		Log.v(null, "=== lossNum:" + lossNum + ", percent:" + lossNum * 100 / totalNum + ", 期望:" + lossPercentExpectSum_double);
		
		Log.v(null, "=== 累计加和盈利(percent):" + totalPercentExpectSum_double);
		Log.v(null, "=== 最低时的加和盈利(percent):" + minEarnPercentExpectSum);
		
		Log.v(null, "=== 累计乘积盈利(percent):" + totalPercentExpectProduct);
		Log.v(null, "=== 最低时的乘积盈利(percent):" + minEarnPercentExpectProduct);
		
		Log.v(null, "=== 单次最小盈利(percent):" + minEarnPercent);
		Log.v(null, "=== 单次最大盈利(percent):" + maxEarnPercent);
		Log.v(null, "===================Report!=========================");
		
		GuiManager.getInstance().showResult(rlt);
	}

}
