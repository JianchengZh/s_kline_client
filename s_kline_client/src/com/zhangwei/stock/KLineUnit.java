package com.zhangwei.stock;

public class KLineUnit {
	public KLineUnit(int date, int open, int high, int low,
			int close, int vol, int cje) {
		// TODO Auto-generated constructor stub
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.vol = vol;
		this.cje = cje;
	}
	
	public KLineUnit(KLineUnit cur, int factorRet) {
		// TODO Auto-generated constructor stub
		this.date = cur.date;
		this.open = cur.open * factorRet / 100;
		this.high = cur.high * factorRet / 100;
		this.low = cur.low * factorRet / 100;
		this.close = cur.close * factorRet / 100;
		this.vol = cur.vol * 100 / factorRet;
		this.cje = cur.cje;
	}
	
	public int isUp(){
		return close-open;
	}

	@Override
	public String toString(){
		return "date:" + date + ", close:" + close + ", vol:" + vol;
	}

	// Date:20121012 open:958 high:983 low:936 close:941 vol:314393 cje:30009
	public int date; // 20121012
	public int open; // 开盘价*100(分)
	public int high; // 最高价*100（分）
	public int low; // 最低价*100（分）
	public int close; // 收盘价*100（分）
	public int vol; // 交易量（手）
	public int cje; // 交易额（万元）
	
	
	public boolean isUpBan(KLineUnit last) {
		// TODO Auto-generated method stub
		int banUpPrice1 = (last.close * 1050 + 500) / 1000;
		int banUpPrice2 = (last.close * 1100 + 500) / 1000;
		
		//极端情况先处理
		if(open==high && open==low && open==close && open>last.close){
			return true;
		}
		
		if(close!=high){
			return false;
		}
		
		if(close!=banUpPrice1 && close!=banUpPrice2){
			return false;
		}
		
		return true;
	}
	
	public boolean isDownBan(KLineUnit last) {
		// TODO Auto-generated method stub
		int banDownPrice1 = (last.close * 950 + 500) / 1000;
		int banDownPrice2 = (last.close * 900 + 500) / 1000;
		
		if(open==high && open==low && open==close && open<last.close){
			return true;
		}
		
		if(close!=low){
			return false;
		}
		
		if(close!=banDownPrice1 && close!=banDownPrice2){
			return false;
		}
		
		return true;
	}

	public boolean canBuy(int price) {
		// TODO Auto-generated method stub
		if(low>0 && price>low && price<=high){
			return true;
		}
		
		return false;
	}
	
	public boolean canSell(int price) {
		// TODO Auto-generated method stub
		if(low>0 && price>=low && price<high){
			return true;
		}
		
		return false;
	}
}

