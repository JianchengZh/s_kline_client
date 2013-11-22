package com.zhangwei.stock.gui;

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 


import java.awt.*;
import java.util.List;

import javax.swing.*;

import android.util.Log;

import com.zhangwei.stock.KLineUnit;
import com.zhangwei.stock.basic.StockException;
import com.zhangwei.stock.kline.KLineTypeResult;
import com.zhangwei.util.DateHelper;
import com.zhangwei.util.StockHelper;

/* Rule.java is used by ScrollDemo.java. */

public class PriceRule extends JComponent {
    public static final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int SIZE = 35;
    public static final int SIZE_PERCENT_FACTOR = 4;

    public int orientation;
    private int increment;
    private int price_units;
    private int vol_units;
    private List<KLineUnit> kl;
	private int size; //total pixels of the width
	private int lowestPrice;
	private int highestPrice;
	private int vol_size;
	private int price_size;
	private int vol_max;

    public PriceRule(int size, List<KLineUnit> kl) {
    	this.size = size;
    	vol_size = size/SIZE_PERCENT_FACTOR;
    	price_size = size - vol_size;
    	this.kl = kl;
        orientation = VERTICAL;
        setIncrementAndUnits();
    }
    
    public void Update(int size, List<KLineUnit> kl){
    	this.size = size;
    	this.kl = kl;
    	vol_size = size/SIZE_PERCENT_FACTOR;
    	price_size = size - vol_size;
        orientation = VERTICAL;
        setIncrementAndUnits();
        
        repaint();
    }

    public void setIsMetric() {
        setIncrementAndUnits();
        repaint();
    }

    private void setIncrementAndUnits() {
    	try {
			KLineTypeResult ret = StockHelper.getKlineType(kl);
			this.vol_max = ret.vol_max;
			this.lowestPrice = ret.lowest_price;
			this.highestPrice = ret.highest_price;
			int h = highestPrice - lowestPrice;
	        price_units = price_size * 100 / h ; //每一元对应多少像素
	        price_size = price_units * h /100; 
	        increment = price_units;
	        
	        /*vol_units = vol_size * 100 / ret.vol_max;*/
	        
	        if(increment<1){
	        	Log.e("******PriceRule****", "increment:" + increment + ", price_size:"  + price_size + ", units:" + price_units);
	        }
		} catch (StockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	 setPreferredSize(new Dimension(SIZE, size));

    }


    public int getIncrement() {
        return increment;
    }
    
    public int getUnits(){
    	return price_units;
    }
    
    public int getLowestPrice(){
    	return lowestPrice;
    }
    
    public int getHighestPrice(){
    	return highestPrice;
    }

/*    public void setPreferredHeight(int ph) {
        setPreferredSize(new Dimension(SIZE, ph));
    }

    public void setPreferredWidth(int pw) {
        setPreferredSize(new Dimension(pw, SIZE));
    }*/

    protected void paintComponent(Graphics g) {
        Rectangle drawHere = g.getClipBounds();

        // Fill clipping area with dirty brown/orange.
        g.setColor(new Color(230, 163, 4));
        g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

        // Do the ruler labels in a small font that's black.
        g.setFont(new Font("SansSerif", Font.BOLD, 10));
        g.setColor(Color.black);

        // Some vars we need.
        int end = 0;
        int start = 0;
        int tickLength = 0;
        String text = null;

        // Use clipping bounds to calculate first and last tick locations.
        if (orientation == HORIZONTAL) {
            start = (drawHere.x / increment) * increment;
            end = (((drawHere.x + drawHere.width - vol_size) / increment) + 1)
                  * increment;
        } else {
            start = (drawHere.y / increment) * increment;
            end = (((drawHere.y + drawHere.height - vol_size) / increment) + 1)
                  * increment;
        }

        // Make a special case of 0 to display the number
        // within the rule and draw a units label.
        if (start == 0) {
            text = "Price";
            tickLength = 2;
            if (orientation == HORIZONTAL) {
                g.drawLine(0, SIZE-1, 0, SIZE-tickLength-1);
                g.drawString(text, 2, 21);
            } else {
                g.drawLine(SIZE-1, 0, SIZE-tickLength-1, 0);
                g.drawString(text, 9, 10);
            }
            text = null;
            start = increment;
        }

        // ticks and labels
        for (int i = start; i < end; i += increment) {
            if (i % price_units == 0)  {
                tickLength = 1;
                text = String.valueOf((end-i-1)*100/price_units + lowestPrice)/*Integer.toString(i/units)*/;
            } else {
                tickLength = 0;
                text = null;
            }

            if (tickLength != 0) {
                if (orientation == HORIZONTAL) {
                    g.drawLine(i, SIZE-1, i, SIZE-tickLength-1);
                    if (text != null)
                        g.drawString(text, i-3, 21);
                } else {
                    g.drawLine(SIZE-1, i, SIZE-tickLength-1, i);
                    if (text != null)
                        g.drawString(text, 9, i+3);
                }
            }
        }
        
        g.drawLine(SIZE-2, price_size+vol_size/2, SIZE, price_size+vol_size/2);
        text = String.valueOf(vol_max/2);
        g.drawString(text, drawHere.x, price_size+vol_size/2+4);
    }

	public int getPriceHeight() {
		// TODO Auto-generated method stub
		return price_size;
	}

	public int getMaxVol() {
		// TODO Auto-generated method stub
		return vol_max;
	}
}

