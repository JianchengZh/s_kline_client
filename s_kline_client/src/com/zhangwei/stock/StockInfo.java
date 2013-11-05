package com.zhangwei.stock;

public class StockInfo {
	public String stock_id; //600031
	public boolean index; //false: normal stock   true: index
	public int market; //1 普通上证股 2普通深证股 3上证index 4深证index
	
	public int start; //交易日期 左端点 20061013
	public int last; //交易日期 右端点 20131105
	public String name; //utf-8, 股票名称 如ST*昌鱼、 三一重工
	public String quick; //快捷键  STCY

}
