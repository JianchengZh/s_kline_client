package com.zhangwei.stock.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.zhangwei.stock.KLineUnit;

public class KLineComponent extends JComponent /*implements Scrollable*/{
    /**
     * The preferred size that the CodePanel needs to paint all of the code
     */
    private Dimension preferredScrollableViewportSize;

    /**
     * Denotes if anything changed in the code that necessitates a change in the
     * viewport size, such as a new line added or a line of code has grown longer
     * than the current longest
     */
    private boolean preferredScrollableViewportSizeChanged = false;

	private int columnUnit;

	private int rowUnit;

	private List<KLineUnit> kl;

	private int w;

	private int h;

	private int lowestPrice;

	private int highestPrice;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6866575016033251900L;
	
	public KLineComponent(List<KLineUnit> kl, int w, int h, int columnUnit, int rowUnit, int lowestPrice, int highestPrice){
		//this.getSize();
		//this.setSize(600, 400);
		this.setPreferredSize(new Dimension(w, h));
		this.kl = kl;
		this.w = w;
		this.h = h;
		
		this.columnUnit = columnUnit;
		this.rowUnit = rowUnit;
		
		this.lowestPrice = lowestPrice;
		this.highestPrice = highestPrice;
	}

	@Override
	public void paint(Graphics g) {
/*		int height = 200;
		int width = 120;
		g.setColor(Color.red);
		g.drawRect(10, 10, height, width);
		g.setColor(Color.gray);
		g.fillRect(11, 11, height, width);
		g.setColor(Color.red);
		g.drawOval(250, 20, height, width);
		g.setColor(Color.magenta);
		g.fillOval(249, 19, height, width);*/
		for(int index=0; index<kl.size(); index++){
			int x = index*columnUnit;
			drawKlineElem(g, x, kl.get(index));
		}
	}
	
    private void drawKlineElem(Graphics g, int x, KLineUnit kLineUnit) {
		// TODO Auto-generated method stub
		if(kLineUnit.isUp()>0){
			g.setColor(Color.red);
			g.fillRect(x, h - (kLineUnit.close-lowestPrice)*rowUnit/100, columnUnit, (kLineUnit.close - kLineUnit.open)*rowUnit/100);
		}else if(kLineUnit.isUp()<0){
			g.setColor(Color.blue);
			g.fillRect(x, h - (kLineUnit.open - lowestPrice)*rowUnit/100, columnUnit, (kLineUnit.open - kLineUnit.close)*rowUnit/100);
		}else{
			g.setColor(Color.BLACK);
			g.drawLine(x, h - (kLineUnit.open - lowestPrice)*rowUnit/100, x+columnUnit, h - (kLineUnit.open - lowestPrice)*rowUnit/100);
		}
		
		if(kLineUnit.high>kLineUnit.low){
			g.drawLine(x+columnUnit/2, h - (kLineUnit.high - lowestPrice)*rowUnit/100, x+columnUnit/2, h - (kLineUnit.low - lowestPrice)*rowUnit/100);
		}
	}

	/**
     * Computes the dimension required to paint this document
     * @return
     */
    private Dimension computeDimension()
    {
        // Handle the initial case
        if( this.preferredScrollableViewportSize == null )
        {
            this.preferredScrollableViewportSize = new Dimension();
            this.preferredScrollableViewportSizeChanged = true;
        }

        if( this.preferredScrollableViewportSizeChanged )
        {
            // Compute the height


            // Find the longest line


            // Build and return a new Dimension object
            this.preferredScrollableViewportSize.setSize( preferredScrollableViewportSize.width, preferredScrollableViewportSize.height );

            // Update our boolean flag
            this.preferredScrollableViewportSizeChanged = false;
        }
        return this.preferredScrollableViewportSize;
    }
	

/*	@Override
	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
        Dimension d = this.computeDimension();
        //System.out.println( "getPreferredScrollableViewportSize: " + d );
        return d;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
        //System.out.println( "getScrollableUnitIncrement: " + visibleRect + ", " + orientation + ", " + direction );
        if( orientation == SwingConstants.HORIZONTAL )
        {
            // Return the width of a single character
            return 7;
        }
        else
        {
            // Return the height of a single line
            return 7;//this.lineSpacing;
        }
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		// TODO Auto-generated method stub
        //System.out.println( "getScrollableBlockIncrement: " + visibleRect + ", " + orientation + ", " + direction );
        if( orientation == SwingConstants.HORIZONTAL )
        {
            // Return the full width of the rectangle
            return visibleRect.width;
        }
        else
        {
            // Return the height of the visibleRect mod-ed by the line spacing so that
            // we'll move by full line blocks
            int height = visibleRect.height;// - ( visibleRect.height % this.lineSpacing );
            //System.out.println( "Return: " + height );
            return height;
        }
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		// TODO Auto-generated method stub
		return false;
	}*/

}
