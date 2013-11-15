package com.zhangwei.stock.emu;

import java.util.concurrent.LinkedBlockingQueue;



public class ParallelManager  {
	private final int workNum = 8;
	private LinkedBlockingQueue<StockTask> fifo;
	private ParallelThread[] threads;
	private static ParallelManager ins;
	private ParallelManager(){
		fifo = new LinkedBlockingQueue<StockTask>();
		threads = new ParallelThread[workNum];
		for(ParallelThread thread : threads){
			thread = new ParallelThread();
			thread.start();
		}
		
	}
	
	public static ParallelManager getInstance(){
		if(ins==null){
			ins = new ParallelManager();
		}
		return ins;
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
			while(true) {
				try {
					StockTask task = fifo.take();
					task.processTask();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

}
