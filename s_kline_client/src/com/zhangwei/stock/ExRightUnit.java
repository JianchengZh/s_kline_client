package com.zhangwei.stock;

public class ExRightUnit {
	public int date;
	public int multi;
	public int add;
	
	
	public ExRightUnit(int date, int multi, int add) {
		// TODO Auto-generated constructor stub
		this.date = date;
		this.multi = multi;
		this.add = add;
	}
	
	public String toString(){
		return "date:" + date + ", multi:" + multi + ", add:" + add;
	}

}
