package com.zhangwei.util;

import android.util.Log;

public class Format {
	private static final String TAG = "Format";

	public static int parserInt(String num, int defaultRet){
		int ret = defaultRet;
		try{
			ret = Integer.valueOf(num);
		}catch(NumberFormatException e){
			/*e.printStackTrace();*/
			//Log.e(TAG, "parserInt error:" + num);
		}
		
		return ret;

	}

}
