package com.zhangwei.stock;

public class KLineUnit {
	public KLineUnit(int date, int open_price, int high_price, int low_price,
			int close, int vol, int cje) {
		// TODO Auto-generated constructor stub
		this.Date = date;
		this.open = open_price;
		this.high = high_price;
		this.low = low_price;
		this.close = close;
		this.vol = vol;
		this.cje = cje;
	}
	
	public String toString(){
		return "date:" + Date + ", close:" + close + ", vol:" + vol;
	}

	// Date:20121012 open:958 high:983 low:936 close:941 vol:314393 cje:30009
	int Date; // 20121012
	int open; // 开盘价*100(分)
	int high; // 最高价*100（分）
	int low; // 最低价*100（分）
	int close; // 收盘价*100（分）
	int vol; // 交易量（手）
	int cje; // 交易额（万元）
}

