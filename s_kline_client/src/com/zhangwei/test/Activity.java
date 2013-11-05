package com.zhangwei.test;

import com.android.dazhihui.Base;
import com.android.dazhihui.Globe;
import com.zhangwei.dzh.API;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;

public class Activity extends Thread  {
	public static final int TICK_COUNT = 0x123456;
	private static final String TAG = "Activity";
	private Handler handler;
	public Activity(){


	}
	
	public Handler getHandler(){
		return handler;
	}

	/**
	 * @param args
	 */
	public  void run() {
		// TODO Auto-generated method stub
        Looper.prepare();

        handler = new Handler() {
            public void handleMessage(Message msg) {
        		switch(msg.what){
        		case TICK_COUNT:
        			Log.w(TAG, "tick count:" + System.currentTimeMillis());
        			handler.sendEmptyMessageDelayed(TICK_COUNT, 10000);
        			break;
        		}
        		return;
            }
        };


        //handler.sendEmptyMessage(TICK_COUNT);

        Globe.base = new Base();
        Globe.base.startHandler();

        API.sendKline("SZ002572", 20101015); //000001
        //API.sendKline("SH000001", 20131015); 
        //API.sendKline("SZ399001", 20131015); 
        Looper.loop();
	}

/*	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case TICK_COUNT:
			Log.w(TAG, "tick count:" + System.currentTimeMillis());
			handler.sendEmptyMessageDelayed(TICK_COUNT, 1000);
			break;
		}
		return false;
	}*/

}
