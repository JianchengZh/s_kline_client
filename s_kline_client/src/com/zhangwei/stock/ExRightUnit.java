package com.zhangwei.stock;

public class ExRightUnit {
	public int date;
	public int multi_num;
	public int add_num;
	
	
	public ExRightUnit(int date, int multi_num, int add_num) {
		// TODO Auto-generated constructor stub
		this.date = date;
		this.multi_num = multi_num;
		this.add_num = add_num;
	}
	
	public String toString(){
		return "date:" + date + ", multi_num:" + multi_num + ", add_num:" + add_num;
	}

}
