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
}

