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
	
	private int index_x;
	private int index_y;
	private KLineComponent klc;
	private List<List<TradeUnit>> list;
	private int w;
	private int h;
	private CtrlPanel ctrlPanel;
	private TradeInfoListener tradeInfoListener;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1824501043506018932L;
	private static final String TAG = "StockPanel";
	
/*	public KLinePanel(Stock s, TradeUnit tu, int w, int h){
		super(new BorderLayout());
		this.s = s;
		this.tu = tu;
		
        List<KLineUnit> kl = s.getNDayKline(tu);
        columnView = new DateRule(w, kl);
        rowView = new PriceRule(h, kl);
        
		int columnUnit = columnView.getUnits();
		int rowUnit = rowView.getUnits();
		int lowest = rowView.getLowestPrice();
		int highest = rowView.getHighestPrice();
		KLineComponent klc = new KLineComponent(kl, tu, w, h, columnUnit, rowUnit, lowest, highest);

       
        JScrollPane jp = new JScrollPane(klc);
      
        jp.setViewportBorder(BorderFactory.createLineBorder(Color.black));
        jp.setColumnHeaderView(columnView);
        jp.setRowHeaderView(rowView);
        
        add(jp, BorderLayout.CENTER);
        //setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        //setPreferredSize(new Dimension(w, h));
        //setSize(w, h);
	}*/

	public KLinePanel(CtrlPanel ctrlPanel, List<List<TradeUnit>> list, int w, int h) {
		// TODO Auto-generated constructor stub
		this.ctrlPanel = ctrlPanel;
        StockManager sm = StockManager.getInstance();
        this.list = list;

    	index_x = 0;
    	index_y = 0;
		this.tu = list.get(index_x).get(index_y);
		
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
		if(index_x<=0){
			ctrlPanel.setBtn1Enable(false);
		}else{
			ctrlPanel.setBtn1Enable(true);
		}
		
		if(index_x>=list.size()-1){
			ctrlPanel.setBtn2Enable(false);
		}else{
			ctrlPanel.setBtn2Enable(true);
		}
		
		if(index_y<=0){
			ctrlPanel.setBtn3Enable(false);
		}else{
			ctrlPanel.setBtn3Enable(true);
		}
		
		if(index_y>=list.get(index_x).size()-1){
			ctrlPanel.setBtn4Enable(false);
		}else{
			ctrlPanel.setBtn4Enable(true);
		}
	}

	@Override
	public void onPreStock() {
		// TODO Auto-generated method stub
		//Log.v(TAG, "onPreStock");
		
        StockManager sm = StockManager.getInstance();

    	index_x--;
    	if(index_x<0){
    		index_x = 0;
    	}

    	index_y = 0;

		this.tu = list.get(index_x).get(index_y);
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
	public void onNxtStock() {
		// TODO Auto-generated method stub
		//Log.v(TAG, "onNxtStock");
		
        StockManager sm = StockManager.getInstance();

    	index_x++;
    	if(index_x>list.size()-1){
    		index_x = list.size()-1;
    	}

    	index_y = 0;

		this.tu = list.get(index_x).get(index_y);
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

    	index_y--;
    	if(index_y<0){
    		index_y = 0;
    	}

    	
		this.tu = list.get(index_x).get(index_y);
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
        
    	index_y++;
    	if(index_y>list.get(index_x).size()-1){
    		index_y = list.get(index_x).size()-1;
    	}

		this.tu = list.get(index_x).get(index_y);
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
