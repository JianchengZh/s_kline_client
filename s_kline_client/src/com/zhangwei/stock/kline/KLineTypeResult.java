package com.zhangwei.stock.kline;

import java.util.List;

import com.zhangwei.stock.KLineUnit;

public class KLineTypeResult {

	public KLineType type;
	public KLineUnit LEFT;
	public KLineUnit LOW;
	public KLineUnit HIGH;
	public KLineUnit RIGHT;
	
	public KLineTypeResult(KLineUnit LEFT, KLineUnit LOW, KLineUnit HIGH, KLineUnit RIGHT){
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
	}
}
