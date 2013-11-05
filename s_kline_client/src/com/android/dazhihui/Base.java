package com.android.dazhihui;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.dazhihui.http.HttpHandler;
import com.android.dazhihui.http.HttpListener;
import com.android.dazhihui.http.Request;
import com.android.dazhihui.http.Response;
import com.android.dazhihui.socket.SocketHandler;
import com.android.dazhihui.socket.SocketListener;
import com.android.dazhihui.trade.n.TradeHelper;
import com.android.dazhihui.util.Functions;
import com.zhangwei.dzh.API;
/*import com.android.dazhihui.view.BrowserScreen;
import com.android.dazhihui.view.CashRankingScreen;
import com.android.dazhihui.view.FutruesScreen;
import com.android.dazhihui.view.InitScreen;
import com.android.dazhihui.view.KlineScreen;
import com.android.dazhihui.view.Level2RankingScreen;
import com.android.dazhihui.view.MinuteScreen;
import com.android.dazhihui.view.PartstaticScreen;
import com.android.dazhihui.view.StockListScreen;
import com.android.dazhihui.view.StockMineListScreen;
import com.android.dazhihui.view.StockPondScreen;*/
import java.util.Hashtable;

public class Base
  implements HttpListener, SocketListener, Runnable
{
  private static final int delayMillis = 100;
  private static final String TAG = "Base";
  //public WindowsManager application;
  protected Hashtable<String, Request> autoHash = new Hashtable();
  public HttpHandler httpHandler = null;
  public boolean isRunning;
  protected RefreshHandler myRefreshHandler = null;
  private long newFlashTime = 0L;
  private long oldFlashTime = 0L;
  protected int[] requestInfo = null;
  protected Request socketFirstRequest = null;
  public SocketHandler socketHandler = null;


  public void cancelRequest()
  {
    this.requestInfo = null;
    this.httpHandler.cancelAccess();
  }

  public void cleanUp()
  {
    Functions.Log("Base@@@", "base socket cleanup");
    if (this.socketHandler != null)
    {
      this.socketHandler.cleanup();
      this.socketHandler = null;
    }
    if (this.httpHandler == null)
      return;
    this.httpHandler.destroy();
    this.httpHandler = null;
  }

  public void completed(Response paramResponse)
  {
	    Log.e(TAG, "completed");
		byte[] kline_input = paramResponse.getData(2944);
		API.PrintKline(kline_input);
		
		byte[] exrights_input = paramResponse.getData(2958);
		API.PrintExrights(exrights_input);
  }

  public void connectSuccess(SocketHandler paramSocketHandler)
  {
    if (this.socketFirstRequest == null)
      return;
    paramSocketHandler.sendData(this.socketFirstRequest.getContent());
  }

  public void delAutoRequest(int paramInt)
  {
    this.autoHash.remove(paramInt);
  }

  public void delAutoRequest(Request paramRequest)
  {
    if (paramRequest == null)
      return;
    this.autoHash.remove(paramRequest.getScreenId());
  }

  public void exception(Exception paramException, HttpHandler paramHttpHandler)
  {}

  public void exception(Exception paramException, SocketHandler paramSocketHandler)
  {
    Functions.Log("exception socket cleanup");
    paramSocketHandler.cleanup();
    exception(new Exception("timeout"), this.httpHandler);
  }

  public Hashtable<String, Request> getAutoHash()
  {
    return this.autoHash;
  }


  public void removeHandler()
  {
    this.isRunning = false;
    this.myRefreshHandler = null;
  }

  public void run()
  {
    while (true)
    {
      if (!(this.isRunning))
        return;
      try
      {
        this.myRefreshHandler.removeMessages(0);
        Message localMessage = this.myRefreshHandler.obtainMessage(0, 1, 1, "Message order");
        this.myRefreshHandler.sendMessage(localMessage);
        Thread.sleep(100L);
      }
      catch (Exception localException)
      {
      }
    }
  }

  public void sendRequest(Request paramRequest)
  {
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = paramRequest.getScreenId();
    arrayOfInt[1] = paramRequest.getCommId();
    this.requestInfo = arrayOfInt;
    if (paramRequest.getServerKind() == 0)
    {
      this.socketFirstRequest = paramRequest;
      if (this.socketHandler == null)
      {
        Functions.Log("Base@@@", "socketHandler new");
        this.socketHandler = new SocketHandler(this);
        this.socketHandler.connect();
        return;
      }
      if (!(this.socketHandler.isSocketAvailable()))
      {
        Functions.Log("Base@@@", "socketHandler connect");
        this.socketHandler.connect();
        return;
      }
      Functions.Log("Base@@@", "socketHandler sendData");
      this.socketHandler.sendData(paramRequest.getContent());
      return;
    }
    if (this.httpHandler == null)
      this.httpHandler = new HttpHandler(this);
    Functions.Log("Base@@@", "httpHandler send");
    this.httpHandler.sendRequest(paramRequest);
  }

  public void sendRequest(Request paramRequest, int paramInt)
  {
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = paramRequest.getScreenId();
    arrayOfInt[1] = paramRequest.getCommId();
    this.requestInfo = arrayOfInt;
    if (this.httpHandler == null)
        this.httpHandler = new HttpHandler(this);
      this.httpHandler.sendRequest(paramRequest, paramInt);
  }


  public void startHandler()
  {
    this.isRunning = true;
    this.myRefreshHandler = new RefreshHandler();
    new Thread(this).start();
    this.oldFlashTime = 0L;
    this.newFlashTime = 0L;
  }

  public void timeout(SocketHandler paramSocketHandler)
  {
    paramSocketHandler.cleanup();
    exception(new Exception("sockettimeout"), this.httpHandler);
  }

  public class RefreshHandler extends Handler
  {
    public void handleMessage(Message paramMessage)
    {
    	//Log.e(TAG, "RefreshHandler - handleMessage");
    }
  }
}

