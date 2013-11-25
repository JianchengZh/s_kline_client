package com.zhangwei.stock;

public class Constants {
	public static final int BUYPOINT_PREFIX_LEN = 60;
	public static final int SELLPOINT_POSTFIX_LEN = 10;
	/**
	 * 卖出  印花税0.001 + 手续费0.0015 (先四舍五入再加和)
	 * 买入  印花税0 手续费0.0015 (四舍五入)
	 * 一次买卖需要支付 0.004  即0.4%， 实际模拟暂取1%
	 * */
	public static final int YONGJIN_PERCENT = 1; 

}
