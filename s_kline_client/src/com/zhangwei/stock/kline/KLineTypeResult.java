package com.zhangwei.stock.kline;

import java.util.List;

import com.zhangwei.stock.KLineUnit;

public class KLineTypeResult {

	public KLineType type;
	public KLineUnit LEFT;
	public KLineUnit LOW;
	public KLineUnit HIGH;
	public KLineUnit RIGHT;
	public int lowest_price;
	public int highest_price;
	public int vol_max;
	public int vol_min;
	public int vol_average;
	public int price_average;
	
	public KLineTypeResult(KLineUnit LEFT, KLineUnit LOW, KLineUnit HIGH, KLineUnit RIGHT, int highest_price, int lowest_price, int price_average, int vol_max, int vol_min, int vol_average){
		if(LOW.date<HIGH.date){
			if(LOW.date==LEFT.date){
				if(HIGH.date==RIGHT.date){
					type = KLineType.LH;
				}else{
					type = KLineType.LHR;
				}
			}else{
				if(HIGH.date==RIGHT.date){
					type = KLineType.LLH;
				}else{
					type = KLineType.LLHR;
				}
			}
		}else{
			if(HIGH.date==LEFT.date){
				if(LOW.date==RIGHT.date){
					type = KLineType.HL;
				}else{
					type = KLineType.HLR;
				}
			}else{
				if(LOW.date==RIGHT.date){
					type = KLineType.LHL;
				}else{
					type = KLineType.LHLR;
				}
			}
			
		}
		
		this.LEFT = LEFT;
		this.LOW = LOW;
		this.HIGH = HIGH;
		this.RIGHT = RIGHT;
		
		this.highest_price = highest_price;
		this.lowest_price = lowest_price;
		this.price_average = price_average;
		this.vol_max = vol_max;
		this.vol_min = vol_min;
		this.vol_average = vol_average;
	}
}
