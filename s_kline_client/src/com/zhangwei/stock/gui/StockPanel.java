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


import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;

public class StockPanel extends JPanel {
    private DateRule columnView;
    private PriceRule rowView;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1824501043506018932L;
	
	public StockPanel(List<KLineUnit> kl){
		super(new BorderLayout());
		
		KLineComponent klc = new KLineComponent(kl);

/*		int preferredSizeWidth = 450;
		int preferredSizeHeight = 110;
        
		setPreferredSize(new Dimension(preferredSizeWidth, preferredSizeHeight));*/
        
        JScrollPane jp = new JScrollPane(klc);
        
        columnView = new DateRule(420, kl);
        rowView = new PriceRule(450, kl);
        
/*        columnView.setPreferredWidth(420);
        rowView.setPreferredHeight(450);*/
        
        //jp.setPreferredSize(new Dimension(300, 250));
        jp.setViewportBorder(BorderFactory.createLineBorder(Color.black));
        jp.setColumnHeaderView(columnView);
        jp.setRowHeaderView(rowView);
        
        add(jp, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
	}
	
	
	public static void main(String[] args){
        JFrame frame = new JFrame( "Stock Panel" );
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

        StockManager sm = StockManager.getInstance();
        Stock s = sm.getStock(new StockInfo("002572", 2, "SFY", -1, -1, "索菲亚"), false);
        List<KLineUnit> kl = s.getNDayKline(60, 20131118);
        StockPanel stockPanel = new StockPanel(kl);
        
/*        JScrollPane scrollPane = new JScrollPane( stockPanel );
        scrollPane.getViewport().setBackground( Color.white );*/
        frame.add( stockPanel );

        //frame.add( codePanel );

        frame.setSize( 500, 500 );
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation( d.width / 4, 100  );
        frame.setVisible( true );
	}

}
