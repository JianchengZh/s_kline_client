package com.zhangwei.stock.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockInfo;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.BS.TradeUnit;


public class GuiManager {
	private JFrame frame;
	private List<TradeUnit> list;
	private int index;
	
	private static GuiManager ins;
	private GuiManager(){
		index = 0;
	}
	
	public static GuiManager getInstance(){
		if(ins==null){
			ins = new GuiManager();
		}
		
		return ins;
	}
	
	public void showResult(final List<TradeUnit> list, final int prefixNDay, final int posfixNDay){

		this.list = list;
		this.index = 0;
		

		// MyParser.Paser_dir(args[0]);
		Runnable runner = new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
				
				String title = ("Kline GUI");
				frame = new JFrame(title);
				TradeUnit tu = list.get(index);

		        StockManager sm = StockManager.getInstance();
		        Stock s = sm.getStock(tu.stock_id, tu.market_type);
		        List<KLineUnit> kl = s.getNDayKline(tu, prefixNDay, posfixNDay);

				KLinePanel stockPanel = new KLinePanel(kl, 420, 420);

				frame.add(stockPanel);

				frame.setSize(500, 500);
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocation(d.width / 4, 100);
				frame.setVisible(true);
			}
		};
		
		
		EventQueue.invokeLater(runner);
	
	}
	

	private final Insets insets = new Insets(0, 0, 0, 0);

	private void addComponent(Container container, Component component,
			int gridx, int gridy, int gridwidth, int gridheight, int anchor,
			int fill) {
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy,
				gridwidth, gridheight, 1.0, 1.0, anchor, fill, insets, 0, 0);
		container.add(component, gbc);

	}
}
