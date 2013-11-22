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
import com.zhangwei.stock.BS.TradeUnit;

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

	private TradeUnit tu;

	private int price_h;

	private int vol_max;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6866575016033251900L;
	
	public KLineComponent(List<KLineUnit> kl, TradeUnit tu, int w, int h, int price_h, int columnUnit, int rowUnit, int lowestPrice, int highestPrice, int vol_max){
		//this.getSize();
		//this.setSize(600, 400);
		this.setPreferredSize(new Dimension(w, h));
		this.tu = tu;
		this.kl = kl;
		this.w = w;
		this.h = h;
		this.price_h = price_h;
		
		this.columnUnit = columnUnit;
		this.rowUnit = rowUnit;
		
		this.lowestPrice = lowestPrice;
		this.highestPrice = highestPrice;
		this.vol_max = vol_max;
	}

	@Override
	public void paint(Graphics g) {

		for(int index=0; index<kl.size(); index++){
			int x = index*columnUnit;
			drawKlineElem(g, x, kl.get(index));
			drawVolElem(g, x, kl.get(index));
		}
		
		drawBSPoint(g, kl, tu);
		
		g.setColor(Color.BLACK);
		g.drawLine(0, price_h, w, price_h);
		
		
	}
	
    private void drawKlineElem(Graphics g, int x, KLineUnit kLineUnit) {
		// TODO Auto-generated method stub
		if(kLineUnit.isUp()>0){
			g.setColor(Color.red);
			g.fillRect(x, price_h - (kLineUnit.close-lowestPrice)*rowUnit/100, columnUnit, (kLineUnit.close - kLineUnit.open)*rowUnit/100);
		}else if(kLineUnit.isUp()<0){
			g.setColor(Color.blue);
			g.fillRect(x, price_h - (kLineUnit.open - lowestPrice)*rowUnit/100, columnUnit, (kLineUnit.open - kLineUnit.close)*rowUnit/100);
		}else{
			g.setColor(Color.BLACK);
			g.drawLine(x, price_h - (kLineUnit.open - lowestPrice)*rowUnit/100, x+columnUnit, price_h - (kLineUnit.open - lowestPrice)*rowUnit/100);
		}
		
		if(kLineUnit.high>kLineUnit.low){
			g.drawLine(x+columnUnit/2, price_h - (kLineUnit.high - lowestPrice)*rowUnit/100, x+columnUnit/2, price_h - (kLineUnit.low - lowestPrice)*rowUnit/100);
		}
	}
    
    private void drawVolElem(Graphics g, int x, KLineUnit kLineUnit) {
		// TODO Auto-generated method stub
    	int vol_len = (h-price_h) * kLineUnit.vol / vol_max;
		if(kLineUnit.isUp()>0){
			g.setColor(Color.red);
			g.fillRect(x, h-vol_len, columnUnit, vol_len);
		}else if(kLineUnit.isUp()<0){
			g.setColor(Color.blue);
			g.fillRect(x, h-vol_len, columnUnit, vol_len);
		}else{
			g.setColor(Color.BLACK);
			g.drawRect(x, h-vol_len, columnUnit, vol_len);
		}
		
	}
    
    private void drawBSPoint(Graphics g, List<KLineUnit> kl, TradeUnit tu) {
		// TODO Auto-generated method stub
		int index = 0;
		for(KLineUnit elem : kl){
			int x = index*columnUnit;
			
			if(elem.date==tu.buy_date){
				int y = price_h - (tu.buy_price - lowestPrice)*rowUnit/100;
				g.setColor(Color.GRAY);
				g.drawLine(x+columnUnit/2, price_h-10, x+columnUnit/2, y);
				g.setColor(Color.RED);
				g.drawString("B", x, price_h);
			}else if(elem.date==tu.sell_date){
				int y = price_h - (tu.sell_price - lowestPrice)*rowUnit/100;
				g.setColor(Color.GRAY);
				g.drawLine(x+columnUnit/2, price_h-10, x+columnUnit/2, y);
				g.setColor(Color.BLUE);
				g.drawString("S", x, price_h);
			}
			
			index++;
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

	public void Update(List<KLineUnit> kl, TradeUnit tu, int columnUnit,
			int rowUnit, int lowest, int highest, int vol_max) {
		// TODO Auto-generated method stub
		this.setPreferredSize(new Dimension(w, h));
		this.tu = tu;
		this.kl = kl;
		
		this.columnUnit = columnUnit;
		this.rowUnit = rowUnit;
		
		this.lowestPrice = lowest;
		this.highestPrice = highest;
		this.vol_max = vol_max;
		
		repaint();
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
