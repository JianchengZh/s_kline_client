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

	/**
	 * 
	 */
	private static final long serialVersionUID = -1824501043506018932L;
	private static final String TAG = "StockPanel";
	
	public KLinePanel(Stock s, TradeUnit tu, int w, int h){
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
	}

	@Override
	public void onPreStock() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onPreStock");
		
	}

	@Override
	public void onNxtStock() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onNxtStock");
	}

	@Override
	public void onPreTrade() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onPreTrade");
	}

	@Override
	public void onNxtTrade() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onNxtTrade");
	}
	


}
