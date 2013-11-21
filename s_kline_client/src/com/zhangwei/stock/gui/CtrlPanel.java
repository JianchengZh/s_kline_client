package com.zhangwei.stock.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

public class CtrlPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2178905278510855189L;
	
	public CtrlPanel(){
		setLayout(new GridBagLayout());
        
		ShowPanel showPanel = new ShowPanel();
        ButtonPanel btnPanel = new ButtonPanel();
		
		addComponent(this, showPanel, 0, 0, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		addComponent(this, btnPanel, 1, 0, 1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		
		//setSize(420, 50);
	}
	
	private static final Insets insets = new Insets(0,0,0,0);
	
	private static void addComponent(Container container, Component component,
		      int gridx, int gridy, int gridwidth, int gridheight, int anchor,
		      int fill) {
		    GridBagConstraints gbc = new GridBagConstraints(gridx, gridy,
		      gridwidth, gridheight, 1.0, 1.0, anchor, fill, insets, 0, 0);
		    container.add(component, gbc);
	}

}
