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
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
	//private List<List<TradeUnit>> list;
	private List<TradeUnit> list;

	
	private static GuiManager ins;
	private GuiManager(){
	}
	
	public static GuiManager getInstance(){
		if(ins==null){
			ins = new GuiManager();
		}
		
		return ins;
	}
	
	public void showResult(final List<TradeUnit> list_arg){
/*		this.list = new ArrayList<>();
		String stock_id = null;
		int index_x = -1;
		for(TradeUnit tu : list_arg){
			if(!tu.stock_id.equals(stock_id)){
				this.list.add(new ArrayList<TradeUnit>());
				index_x++;
			}
			stock_id = tu.stock_id;
			this.list.get(index_x).add(tu);
			
		}*/
		this.list = list_arg;


		// MyParser.Paser_dir(args[0]);
		Runnable runner = new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
				
				String title = ("Kline GUI");
				frame = new JFrame(title);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
/*				TradeUnit tu = list.get(index_x).get(index_y);*/

		        StockManager sm = StockManager.getInstance();
		        //Stock s = sm.getStock(tu.stock_id, tu.market_type);

		        JPanel jp = new JPanel();
		        jp.setLayout(new GridBagLayout());
		        frame.setContentPane(jp);
		        
		        //frame.getContentPane().setLayout(new GridBagLayout());
				CtrlPanel ctrlPanel = new CtrlPanel();
				KLinePanel stockPanel = new KLinePanel(ctrlPanel, list, 420, 420);

				//func
				ctrlPanel.setNotify(stockPanel);
				stockPanel.setNotify(ctrlPanel);
				
				ctrlPanel.onUpdate(stockPanel.getTU());
				
				
				//UI
				addComponent(jp, stockPanel, 0, 0, 0, 8,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
				addComponent(jp, ctrlPanel, 0, 8, 0, 1,
		                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
				
/*				frame.add(stockPanel);
				frame.add(ctrlPanel);*/
				/*frame.setSize(500, 500);*/
				frame.setSize(new Dimension(700, 600));
				Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocation(d.width / 4, 50);
				frame.setVisible(true);
				frame.setResizable(false);
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
