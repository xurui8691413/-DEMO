package com.seckill.service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {
	
	private volatile boolean isShutdown = false;
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ConcurrentLinkedQueue<String> msgQueue = new ConcurrentLinkedQueue<>();
	ExecutorService executorService = 	Executors.newScheduledThreadPool(5);
	
	private LogHelper(){
		
	}
	
	
	private static  class LogHelperFactory{
		private static final LogHelper instance = new LogHelper();
	}
	
	public static LogHelper getInstance(){
		return LogHelperFactory.instance;
	}
	public  void log (String message , Class<?> classObj){
		msgQueue.add(message);
	}
	
	
	
	private class  LoggerThread extends Thread
	   {
	      public void run()
	      {
	            try
	            {
	              while(true)
	              {
	                 synchronized(LogHelper.this)
					{
					   if(isShutdown && msgQueue.size() == 0)          //如果服务已经关闭并且消息队列中已经没有剩余的消息，则关闭日志线程
					      break;
					   logger.debug(msgQueue.poll());
					}
	                
	              }
	            }catch(Exception ex){
	            	logger.error(ex.getMessage());
	            }
	      }
	  }
	   
}
