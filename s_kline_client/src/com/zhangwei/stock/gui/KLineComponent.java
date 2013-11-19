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

	/**
	 * 
	 */
	private static final long serialVersionUID = -6866575016033251900L;
	
	public KLineComponent(List<KLineUnit> kl){
		//this.getSize();
		//this.setSize(600, 400);
		this.setPreferredSize(new Dimension(300, 200));
	}

	@Override
	public void paint(Graphics g) {
		int height = 200;
		int width = 120;
		g.setColor(Color.red);
		g.drawRect(10, 10, height, width);
		g.setColor(Color.gray);
		g.fillRect(11, 11, height, width);
		g.setColor(Color.red);
		g.drawOval(250, 20, height, width);
		g.setColor(Color.magenta);
		g.fillOval(249, 19, height, width);
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
