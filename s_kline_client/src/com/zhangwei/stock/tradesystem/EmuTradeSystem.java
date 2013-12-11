package com.zhangwei.stock.tradesystem;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.zhangwei.mysql.BaseDao;
import com.zhangwei.stock.Constants;
import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.bs.BuyPoint;
import com.zhangwei.stock.bs.HoldUnit;
import com.zhangwei.stock.bs.IBuy;
import com.zhangwei.stock.bs.ISell;
import com.zhangwei.stock.bs.SellPoint;
import com.zhangwei.stock.bs.TradeUnit;
import com.zhangwei.stock.gui.GuiManager;
import com.zhangwei.stock.manager.IAssertManager;
import com.zhangwei.stock.record.EmuTradeRecorder;
import com.zhangwei.util.Format;
import com.zhangwei.util.StockHelper;

public class EmuTradeSystem implements ITradeSystem{
	private static final String TAG = "EmuTradeSystem";
	EmuTradeRecorder records;
	IAssertManager iam;
	//private static EmuTradeSystem ins;
	public EmuTradeSystem(IAssertManager iam, String bsTableName){
		this.records = new EmuTradeRecorder(bsTableName);
		this.iam = iam;
	}
	
/*	public static EmuTradeSystem getInstance(){
		if(ins==null){
			ins = new EmuTradeSystem();
		}
		
		return ins;
	}*/
	
	public EmuTradeSystem() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void submitBuyTransaction(IBuy buy, BuyPoint buypoint){
		//buy.onBuySucess(buypoint);
	}

	
	@Override
	public void submitSellTransaction(ISell sell, SellPoint sellpoint, HoldUnit hu){
		//sell.onSellSucess(sellpoint, hu);
	}


	public List<TradeUnit> getTradeInfo(String uid, int type, String order_key) {
		// TODO Auto-generated method stub
		BaseDao bd = BaseDao.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from BS_");
		sb.append(uid);
		sb.append(" where sell_date!=0");
		if(type>0){
			sb.append(" and buy_price<sell_price");
		}else if(type<0){
			sb.append(" and buy_price>sell_price");
		}
		
		if(order_key!=null && !order_key.equals("")){
			sb.append(" order by ");
			sb.append(order_key);
		}
		
		sb.append(";");
		
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
		
		return rlt;
	}
		

	/**
	 * select buy_date,avg(earn_percent),count(*) from bs_7287298140871 group by buy_date;
	 * */
	public void Report(List<TradeUnit> rlt) {
		// TODO Auto-generated method stub
		Log.v(TAG, "Report rlt.size:" + rlt.size());
		
		Stock sh_index = StockManager.getInstance().getStock("000001", 3);
		Stock sz_index = StockManager.getInstance().getStock("399001", 4);

		
		long totalNum = 0;
		long earnNum = 0;
		long lossNum = 0;
		long earnPercentExpectSum = 0;
		long lossPercentExpectSum = 0;
		long totalPercentExpectSum = 0;
		BigInteger totalPercentExpectProduct = new BigInteger("100");
		long minEarnPercent = Long.MAX_VALUE;
		long maxEarnPercent = Long.MIN_VALUE;
		long minEarnPercentExpectSum = Long.MAX_VALUE;
		BigInteger minEarnPercentExpectProduct = new BigInteger("100");
		int emptyCount = 0;
		

		TradeUnit last = rlt.get(0);
		int percent_sum = last.getEarnPercent();
		int same_day_size = 1;
		
		rlt.add(new TradeUnit(null, -1, -1, -1, -1, -1, -1)); //add EOF
		
		for(int index=1; index<rlt.size(); index++){
			TradeUnit elem = rlt.get(index);
			if(last.buy_date==elem.buy_date){ //same day, add it
				int earnPercent_t = elem.getEarnPercent();
				percent_sum += earnPercent_t;
				same_day_size++;
				continue;
			}
			
			if(same_day_size==1){
				percent_sum = elem.getEarnPercent();
				same_day_size = 1;
				last = elem;
				continue;
			}
			
			int earnPercent = percent_sum/same_day_size;
			
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
			
			totalPercentExpectProduct = totalPercentExpectProduct.multiply(new BigInteger(String.valueOf(100 + earnPercent))).divide(new BigInteger(String.valueOf(100)));
			//totalPercentExpectProduct = totalPercentExpectProduct * (100 + earnPercent) / 100;
			
			if(minEarnPercentExpectSum>totalPercentExpectSum/totalNum){
				minEarnPercentExpectSum=totalPercentExpectSum/totalNum;
			}
			
			if(minEarnPercentExpectProduct.compareTo(totalPercentExpectProduct)>0){
				minEarnPercentExpectProduct=totalPercentExpectProduct;
			}
			
			KLineUnit sh_kline_unit = StockHelper.binSearch(sh_index.line.kline_list, last.buy_date, 0);
			
			if(sh_kline_unit!=null){
				Log.v(null, "earnPercent(" + same_day_size + "):" + earnPercent + ", earnPercentExpectSum:" + earnPercentExpectSum/totalNum + ", totalPercentExpectProduct:" + totalPercentExpectProduct + " date:" + last.buy_date + " 沪指：" + sh_kline_unit.toString());
			}else{
				Log.v(null, "earnPercent(" + same_day_size+ "):" + earnPercent + ", earnPercentExpectSum:" + earnPercentExpectSum/totalNum + ", totalPercentExpectProduct:" + totalPercentExpectProduct + " date:" + last.buy_date);
			}
			
		
			if(minEarnPercentExpectProduct.compareTo(new BigInteger("50"))<=0){
				Log.v(null, "爆仓(低于50)" + emptyCount + "次！重置minEarnPercentExpectProduct=100");
				emptyCount++;
				minEarnPercentExpectProduct = new BigInteger("100");
				totalPercentExpectProduct = new BigInteger("100");
			}
			
			percent_sum = elem.getEarnPercent();
			same_day_size = 1;
			last = elem;

		}
		
		double totalPercentExpectSum_double = (double)totalPercentExpectSum / totalNum;
		double earnPercentExpectSum_double = (double)earnPercentExpectSum / earnNum;
		double lossPercentExpectSum_double = (double)lossPercentExpectSum / lossNum;
				
		Log.v(null, "===================Report!=========================");
		Log.v(null, "=== Total Trades:" + totalNum);
		Log.v(null, "=== 爆仓(低于50):" + emptyCount);
		Log.v(null, "=== earnNum:" + earnNum + ", percent:" + earnNum * 100 / totalNum + ", 期望:" + earnPercentExpectSum_double);
		Log.v(null, "=== lossNum:" + lossNum + ", percent:" + lossNum * 100 / totalNum + ", 期望:" + lossPercentExpectSum_double);
		
		Log.v(null, "=== 累计加和盈利(percent):" + totalPercentExpectSum_double);
		Log.v(null, "=== 最低时的加和盈利(percent):" + minEarnPercentExpectSum);
		
		Log.v(null, "=== 累计乘积盈利(percent):" + totalPercentExpectProduct);
		Log.v(null, "=== 最低时的乘积盈利(percent):" + minEarnPercentExpectProduct);
		
		Log.v(null, "=== 单次最小盈利(percent):" + minEarnPercent);
		Log.v(null, "=== 单次最大盈利(percent):" + maxEarnPercent);
		Log.v(null, "===================Report!=========================");
		rlt.remove(rlt.size()-1);
		GuiManager.getInstance().showResult(rlt);
	}

	@Override
	public void completeBuyTransaction(String stock_id, int market_type,
			int date, int buy_price, int buy_vol) {
		// TODO Auto-generated method stub
		records.addBuy(stock_id, market_type, date, buy_price, buy_vol);
		iam.BuyDone(stock_id, market_type, date, buy_price, buy_vol);
	}

	@Override
	public void completeSellTransaction(String stock_id, int market_type,
			int date, int sell_price, int sell_vol) {
		// TODO Auto-generated method stub
		records.addSell(stock_id, market_type, date, sell_price, sell_vol);
	}

}
