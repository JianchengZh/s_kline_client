package com.zhangwei.stock.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.zhangwei.stock.Stock;
import com.zhangwei.stock.StockManager;
import com.zhangwei.stock.BS.TradeUnit;

public class ButtonPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8646233061019977432L;
	
	public ButtonPanel(){
		setLayout(new GridBagLayout());
        
		JButton stockPreBtn = new JButton("<<");
		JButton stockNxtBtn = new JButton(">>");
		JButton tradePreBtn = new JButton("<");
		JButton tradeNxtBtn = new JButton(">");
		
		
		addComponent(this, stockPreBtn, 0, 0, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		addComponent(this, stockNxtBtn, 1, 0, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		addComponent(this, tradePreBtn, 0, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		addComponent(this, tradeNxtBtn, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		
		//setSize(100,50);
	}

	private static final Insets insets = new Insets(0,0,0,0);
	
	private static void addComponent(Container container, Component component,
		      int gridx, int gridy, int gridwidth, int gridheight, int anchor,
		      int fill) {
		    GridBagConstraints gbc = new GridBagConstraints(gridx, gridy,
		      gridwidth, gridheight, 1.0, 1.0, anchor, fill, insets, 0, 0);
		    container.add(component, gbc);
	}
	
	public static void main(String[] args){
		Runnable runner = new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
				
				String title = ("Kline GUI");
				JFrame frame = new JFrame(title);
				ButtonPanel bp = new ButtonPanel();
				
				frame.add(bp);

				frame.setSize(500, 500);
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocation(d.width / 4, 100);
				frame.setVisible(true);
			}
		};
		
		
		EventQueue.invokeLater(runner);
	}
}
