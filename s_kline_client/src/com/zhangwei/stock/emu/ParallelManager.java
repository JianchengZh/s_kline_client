package com.zhangwei.stock.emu;

import java.util.concurrent.LinkedBlockingQueue;

import javax.sql.rowset.Joinable;

import com.zhangwei.stock.task.StockTask;



public class ParallelManager  {
	private int workNum;
	private LinkedBlockingQueue<StockTask> fifo;
	private ParallelThread[] threads;
	private ParallelListener pl;
	private static ParallelManager ins;
	private ParallelManager(){
		fifo = new LinkedBlockingQueue<StockTask>();

	}
	
	public static ParallelManager getInstance(){
		if(ins==null){
			ins = new ParallelManager();
		}
		return ins;
	}
	
	
	
	public void startTask(ParallelListener pl, int workNum){
		if(workNum<1){
			workNum = 8;
		}
		this.pl = pl;
		this.workNum = workNum;
		threads = new ParallelThread[workNum];
		for(ParallelThread thread : threads){
			thread = new ParallelThread();
			thread.start();
		}
	}
	
	public void submitTask(StockTask t){
		try {
			fifo.put(t);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public class ParallelThread extends Thread{
		@Override
		public void run(){
			while(!fifo.isEmpty()) {
				try {
					StockTask task = fifo.take();
					task.processTask();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			synchronized (ParallelManager.this) {
				workNum--;
				if(workNum<1 && pl!=null){
					pl.onComplete();
				}
			}

		}
	}

}
