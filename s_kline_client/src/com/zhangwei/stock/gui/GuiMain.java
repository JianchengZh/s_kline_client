package com.zhangwei.stock.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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


public class GuiMain {
	static JFrame frame;
	
	public static void main(final String args[]) {

		// MyParser.Paser_dir(args[0]);
		Runnable runner = new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
				
				String title = ("Kline GUI");
				frame = new JFrame(title);

		        StockManager sm = StockManager.getInstance();
		        Stock s = sm.getStock(new StockInfo("002572", 2, "SFY", -1, -1, "索菲亚"), false);
		        List<KLineUnit> kl = s.getNDayKline(60, 20131118);
		        
				StockPanel sp = new StockPanel(kl);

				JPanel jp = new JPanel(new GridBagLayout());
		        addComponent(jp, sp, 0, 0, 1, 0,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		        
				
				frame.add(jp, BorderLayout.CENTER);


				frame.setSize(600, 600);
				frame.setLocation(650, 20);
				frame.setVisible(true);
			}
		};
		
		
		EventQueue.invokeLater(runner);
	}

	private static final Insets insets = new Insets(0, 0, 0, 0);

	private static void addComponent(Container container, Component component,
			int gridx, int gridy, int gridwidth, int gridheight, int anchor,
			int fill) {
		GridBagConstraints gbc = new GridBagConstraints(gridx, gridy,
				gridwidth, gridheight, 1.0, 1.0, anchor, fill, insets, 0, 0);
		container.add(component, gbc);

	}
}
