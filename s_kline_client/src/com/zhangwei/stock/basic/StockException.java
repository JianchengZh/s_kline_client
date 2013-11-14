package com.zhangwei.stock.basic;

public class StockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -633894810348565753L;
	
	public StockException(String msg){
		super(msg);
	}
	
	@Override
	public String toString(){
		return super.getMessage();
	}

}
