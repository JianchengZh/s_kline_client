package com.zhangwei.stock.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.BS.TradeUnit;

public class KLinePanel extends JPanel implements KLineBtnListener{
    private DateRule columnView;
    private PriceRule rowView;
    private Stock s;
	private TradeUnit tu;
	
	private int index;
	//private int index_y;
	private KLineComponent klc;
	private List<TradeUnit> list;
	private int w;
	private int h;
	private CtrlPanel ctrlPanel;
	private TradeInfoListener tradeInfoListener;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1824501043506018932L;
	private static final String TAG = "StockPanel";
	

	public KLinePanel(CtrlPanel ctrlPanel, List<TradeUnit> list, int w, int h) {
		// TODO Auto-generated constructor stub
		this.ctrlPanel = ctrlPanel;
        StockManager sm = StockManager.getInstance();
        this.list = list;

    	index = 0;
    	//index_y = 0;
		this.tu = list.get(index);//.get(index_y);
		
        this.w = w;
        this.h = h;
        this.s = sm.getStock(tu.stock_id, tu.market_type);
		
        List<KLineUnit> kl = s.getNDayExRightKline(tu);
        columnView = new DateRule(w, kl);
        rowView = new PriceRule(h, kl);
        
		int columnUnit = columnView.getUnits();
		int rowUnit = rowView.getUnits();
		int lowest = rowView.getLowestPrice();
		int highest = rowView.getHighestPrice();
		int vol_max = rowView.getMaxVol();
		
		klc = new KLineComponent(kl, tu, w, h, rowView.getPriceHeight(), columnUnit, rowUnit, lowest, highest, vol_max);

        JScrollPane jp = new JScrollPane(klc);
      
        jp.setViewportBorder(BorderFactory.createLineBorder(Color.black));
        jp.setColumnHeaderView(columnView);
        jp.setRowHeaderView(rowView);
        
        add(jp, BorderLayout.CENTER);
        
        checkBtnVaild();
        
        
	}
	
	private void checkBtnVaild(){
		if(index<=0){
			index=0;
			ctrlPanel.setBtn1Enable(false);
			ctrlPanel.setBtn3Enable(false);
		}else{
			ctrlPanel.setBtn1Enable(true);
			ctrlPanel.setBtn3Enable(true);
		}
		
		if(index>=list.size()-1){
			index = list.size()-1;
			ctrlPanel.setBtn2Enable(false);
			ctrlPanel.setBtn4Enable(false);
		}else{
			ctrlPanel.setBtn2Enable(true);
			ctrlPanel.setBtn4Enable(true);
		}
		
/*		if(index_y<=0){
			ctrlPanel.setBtn3Enable(false);
		}else{
			ctrlPanel.setBtn3Enable(true);
		}*/
		
/*		if(index_y>=list.get(index_x).size()-1){
			ctrlPanel.setBtn4Enable(false);
		}else{
			ctrlPanel.setBtn4Enable(true);
		}*/
	}

	@Override
	public void onFirstTrade() {
		// TODO Auto-generated method stub
		//Log.v(TAG, "onPreStock");
		
        StockManager sm = StockManager.getInstance();

/*    	index--;
    	if(index<0){
    		index = 0;
    	}*/

        index = 0;
    	//index_y = 0;

		this.tu = list.get(index);//.get(index_y);
        this.s = sm.getStock(tu.stock_id, tu.market_type);

		
        List<KLineUnit> kl = s.getNDayExRightKline(tu);
        columnView.Update(w, kl);
        rowView.Update(h, kl);
        
		int columnUnit = columnView.getUnits();
		int rowUnit = rowView.getUnits();
		int lowest = rowView.getLowestPrice();
		int highest = rowView.getHighestPrice();
		int vol_max = rowView.getMaxVol();
		
		klc.Update(kl, tu, columnUnit, rowUnit, lowest, highest, vol_max);
		
        checkBtnVaild();
        
        if(tradeInfoListener!=null){
        	tradeInfoListener.onUpdate(tu);
        }
		
	}

	@Override
	public void onLastTrade() {
		// TODO Auto-generated method stub
		//Log.v(TAG, "onNxtStock");
		
        StockManager sm = StockManager.getInstance();

/*    	index++;
    	if(index>list.size()-1){
    		index = list.size()-1;
    	}*/
    	index = list.size()-1;

    	//index_y = 0;

		this.tu = list.get(index);//.get(index_y);
        this.s = sm.getStock(tu.stock_id, tu.market_type);

		
        List<KLineUnit> kl = s.getNDayExRightKline(tu);
        columnView.Update(w, kl);
        rowView.Update(h, kl);
        
		int columnUnit = columnView.getUnits();
		int rowUnit = rowView.getUnits();
		int lowest = rowView.getLowestPrice();
		int highest = rowView.getHighestPrice();
		int vol_max = rowView.getMaxVol();
		
		klc.Update(kl, tu, columnUnit, rowUnit, lowest, highest, vol_max);
		
        checkBtnVaild();
        
        if(tradeInfoListener!=null){
        	tradeInfoListener.onUpdate(tu);
        }
	}

	@Override
	public void onPreTrade() {
		// TODO Auto-generated method stub
		//Log.v(TAG, "onPreTrade");
        StockManager sm = StockManager.getInstance();

/*    	index_y--;
    	if(index_y<0){
    		index_y = 0;
    	}*/
        
        index--;

    	
		this.tu = list.get(index);//.get(index_y);
        this.s = sm.getStock(tu.stock_id, tu.market_type);

		
        List<KLineUnit> kl = s.getNDayExRightKline(tu);
        columnView.Update(w, kl);
        rowView.Update(h, kl);
        
		int columnUnit = columnView.getUnits();
		int rowUnit = rowView.getUnits();
		int lowest = rowView.getLowestPrice();
		int highest = rowView.getHighestPrice();
		int vol_max = rowView.getMaxVol();
		
		klc.Update(kl, tu, columnUnit, rowUnit, lowest, highest, vol_max);
		
        checkBtnVaild();
        
        if(tradeInfoListener!=null){
        	tradeInfoListener.onUpdate(tu);
        }
	}

	@Override
	public void onNxtTrade() {
		// TODO Auto-generated method stub
		//Log.v(TAG, "onNxtTrade");
        StockManager sm = StockManager.getInstance();
        
/*    	index_y++;
    	if(index_y>list.get(index_x).size()-1){
    		index_y = list.get(index_x).size()-1;
    	}*/

        index++;
		this.tu = list.get(index);//.get(index_y);
        this.s = sm.getStock(tu.stock_id, tu.market_type);

		
        List<KLineUnit> kl = s.getNDayExRightKline(tu);
        columnView.Update(w, kl);
        rowView.Update(h, kl);
        
		int columnUnit = columnView.getUnits();
		int rowUnit = rowView.getUnits();
		int lowest = rowView.getLowestPrice();
		int highest = rowView.getHighestPrice();
		int vol_max = rowView.getMaxVol();
		
		klc.Update(kl, tu, columnUnit, rowUnit, lowest, highest, vol_max);
		
        checkBtnVaild();
        
        if(tradeInfoListener!=null){
        	tradeInfoListener.onUpdate(tu);
        }
	}

	public void setNotify(TradeInfoListener tradeInfoListener) {
		// TODO Auto-generated method stub
		this.tradeInfoListener = tradeInfoListener;
	}

	
	public TradeUnit getTU(){
		return tu;
	}
	


}
