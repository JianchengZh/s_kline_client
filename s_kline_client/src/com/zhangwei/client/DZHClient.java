package com.zhangwei.client;

import android.util.Log;

import com.android.dazhihui.Globe;
import com.android.dazhihui.http.Request;
import com.android.dazhihui.http.Response;
import com.android.dazhihui.http.StructRequest;
import com.android.dazhihui.socket.SocketClient;
import com.zhangwei.dzh.API;

public class DZHClient {

	
	public DZHClient(String aHostIp, int aHostPort) {	
		Globe.serHangIP2= aHostIp;// "114.80.158.20";
		Globe.serHangPort= aHostPort; //12345;
	}
	
	public Response sendRequest(String stock, int date) {
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
				len = buf.length;
				if(len>0){
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
				byte[] kline_input = resp.getData(2944);
				API.PrintKline(kline_input);
				
				byte[] exrights_input = resp.getData(2958);
				API.PrintExrights(exrights_input);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		



		
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
		try {

			DZHClient remoteFileClient = new DZHClient("114.80.158.20", 12345);

			Response resp = remoteFileClient.sendRequest("SZ002572", 0);
			Response resp2 = remoteFileClient.sendRequest("SH600031", 20121015);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

}
