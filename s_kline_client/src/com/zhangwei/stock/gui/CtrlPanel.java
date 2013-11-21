package com.zhangwei.stock.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.zhangwei.stock.BS.TradeUnit;

public class CtrlPanel extends JPanel implements ActionListener, TradeInfoListener {
	JLabel jl;
	JButton stockPreBtn;
	JButton stockNxtBtn;
	JButton tradePreBtn;
	JButton tradeNxtBtn;
	private KLineBtnListener listener;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2178905278510855189L;
	
	public CtrlPanel(){
		setLayout(new GridBagLayout());
        
/*		ShowPanel showPanel = new ShowPanel();
        ButtonPanel btnPanel = new ButtonPanel();*/
		
/*		addComponent(this, showPanel, 0, 0, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		addComponent(this, btnPanel, 1, 0, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);*/
		
		jl = new JLabel();
		stockPreBtn = new JButton("<<");
		stockNxtBtn = new JButton(">>");
		tradePreBtn = new JButton("<");
		tradeNxtBtn = new JButton(">");
		
		stockPreBtn.addActionListener(this);
		stockNxtBtn.addActionListener(this);
		tradePreBtn.addActionListener(this);
		tradeNxtBtn.addActionListener(this);
		
		addComponent(this, jl, 0, 0, 2, 2,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		addComponent(this, stockPreBtn, 2, 0, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		addComponent(this, stockNxtBtn, 3, 0, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		addComponent(this, tradePreBtn, 2, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		addComponent(this, tradeNxtBtn, 3, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		
		
		//setSize(420, 50);
		Dimension size = new Dimension(420, 50);
		setSize(size);
		setPreferredSize(size);
	}
	
	private static final Insets insets = new Insets(0,0,0,0);
	
	private static void addComponent(Container container, Component component,
		      int gridx, int gridy, int gridwidth, int gridheight, int anchor,
		      int fill) {
		    GridBagConstraints gbc = new GridBagConstraints(gridx, gridy,
		      gridwidth, gridheight, 1.0, 1.0, anchor, fill, insets, 0, 0);
		    container.add(component, gbc);
	}

	public void setNotify(KLineBtnListener listener) {
		// TODO Auto-generated method stub
		this.listener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmdID = e.getActionCommand();
		if("<<".equals(cmdID)){
			listener.onPreStock();
		}else if(">>".equals(cmdID)){
			listener.onNxtStock();
		}else if("<".equals(cmdID)){
			listener.onPreTrade();
		}else if(">".equals(cmdID)){
			listener.onNxtTrade();
		}
	}

	public void setBtn1Enable(boolean b) {
		// TODO Auto-generated method stub
		stockPreBtn.setEnabled(b);
	}
	
	public void setBtn2Enable(boolean b) {
		// TODO Auto-generated method stub
		stockNxtBtn.setEnabled(b);
	}
	
	public void setBtn3Enable(boolean b) {
		// TODO Auto-generated method stub
		tradePreBtn.setEnabled(b);
	}
	
	public void setBtn4Enable(boolean b) {
		// TODO Auto-generated method stub
		tradeNxtBtn.setEnabled(b);
	}

	@Override
	public void onUpdate(TradeUnit tu) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("<html><HTML><body style=color:red>");
		sb.append(tu.stock_id);
		sb.append(", buy date:");
		sb.append(tu.buy_date);
		sb.append(", sell date:");
		sb.append(tu.sell_date);
		sb.append("<br> buy_price:");
		sb.append(tu.buy_price);
		sb.append(", sell_price:");
		sb.append(tu.sell_price);
		sb.append("<br> earn(%):");
		sb.append(tu.getEarnPercent());
		sb.append(", vol:");
		sb.append(tu.vol);
		sb.append("</body></html>");
		jl.setText(sb.toString());
	}



}
