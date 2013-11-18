package com.zhangwei.client;

import java.io.InputStream;
import java.util.Properties;

import android.util.Log;

import com.android.dazhihui.Globe;
import com.android.dazhihui.http.Request;
import com.android.dazhihui.http.Response;
import com.android.dazhihui.http.StructRequest;
import com.android.dazhihui.socket.SocketClient;
import com.zhangwei.dzh.API;

public class DZHClient {

	private static final String TAG = "DZHClient";
	private static DZHClient ins;
	private DZHClient() {	
		try {
			InputStream in = DZHClient.class.getClassLoader().getResourceAsStream("db.properties");
			Properties prop = System.getProperties();

			prop.load(in);
			String ip = (String) prop.get("dzh.ip");
			int port = Integer.parseInt(prop.get("dzh.port").toString());
			Globe.serHangIP2= ip;// "114.80.158.20";
			Globe.serHangPort= port; //12345;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static DZHClient getInstance(){
		if(ins==null){
			ins = new DZHClient();
		}
		
		return ins;
	}
	
	/**
	 *  每次取150个有效数据
	 *  date 为0 算今天  
	 *  date不为0，不包括这一天
	 * */
	public Response sendRequest(String stock, int date) {
		//Log.v(TAG, "sendRequest - IN, stock:" + stock + ", date:" + date);
		final int totalTry = 4;
		SocketClient sc = new SocketClient();
		Response resp = new Response();
		Request req = getReq(stock, date);
		
		try {
			sc.connect();			
			sc.sendData(req.getContent(), req.getContent().length);
			
			Thread.sleep(100);
			int len = 0;
			int tryNum = 0;
			do{
				byte[] buf = sc.readResponse();
				if(buf!=null && buf.length>0){
					resp.analysisData(buf);
					break;
				}else{
					tryNum++;
					if(tryNum>totalTry){
						break;
					}else{
						Thread.sleep(1000*tryNum);
					}

				}

			}while(true);
			
			sc.close();
			
			if(resp!=null && resp.responseCode==0 ){
				System.out.println("responseCode:" + resp.responseCode);
				//byte[] kline_input = resp.getData(2944);
				//API.PrintKline(kline_input);
				
				//byte[] exrights_input = resp.getData(2958);
				//API.PrintExrights(exrights_input);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Log.v(TAG, "sendRequest - OUT");
		
		return resp;
	}
	
	public Request getReq(String stockCode, int date){
	    StructRequest[] arrayOfStructRequest = new StructRequest[2];
	    arrayOfStructRequest[0] = new StructRequest(2944);
	    arrayOfStructRequest[0].writeString(stockCode);
	    arrayOfStructRequest[0].writeByte(7);
	    arrayOfStructRequest[0].writeInt(date);
	    arrayOfStructRequest[0].writeShort(150);
	    
	    arrayOfStructRequest[1] = new StructRequest(2958);
	    arrayOfStructRequest[1].writeString(stockCode);
	    arrayOfStructRequest[1].writeByte(0);
	    
	    return new Request(arrayOfStructRequest, 2100);
	}
	
	
	public static void main(String[] args) {
		DZHClient remoteFileClient = DZHClient.getInstance();
		Response resp = remoteFileClient.sendRequest("SZ002572", 0);
		Response resp2 = remoteFileClient.sendRequest("SH600031", 20121015);

		
	}

}
