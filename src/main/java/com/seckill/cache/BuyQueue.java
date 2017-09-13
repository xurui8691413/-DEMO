package com.seckill.cache;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

import com.seckill.dto.SeckillInfo;

public class BuyQueue<T> {

	@SuppressWarnings("rawtypes")
	private Map<String,BlockingQueue> queueMap = new ConcurrentHashMap<>();

	/*@SuppressWarnings("unchecked")
	public  int  put (String name ,SeckillInfo t ) throws InterruptedException{
		if(!isQueueExisted(name)){
			synchronized (this) {
				if(!isQueueExisted(name)){
					BlockingQueue<SeckillInfo> queue =new ArrayBlockingQueue<SeckillInfo> (t.getStoreNum());
					queueMap.put(t.getName(), queue);
				}
			}

		}
		if(queueMap.get(name).remainingCapacity()>0){
			queueMap.get(name).put(t);
		}else{
			return 0;
		}

		return 1;

	}

	public  SeckillInfo take (String name) throws InterruptedException{

		return (SeckillInfo) (isQueueExisted(name) ?queueMap.get(name).take():null);
	}*/
	
	@SuppressWarnings("unchecked")
	public  int  put (String name ,SeckillInfo t,FutureTask<T> task ) throws InterruptedException{
		if(!isQueueExisted(name)){
			synchronized (this) {
				if(!isQueueExisted(name)){
					BlockingQueue<FutureTask<T>> queue =new ArrayBlockingQueue<FutureTask<T>> (t.getStoreNum());
					queueMap.put(t.getName(), queue);
				}
			}

		}
		if(queueMap.get(name).remainingCapacity()>0){
			queueMap.get(name).put(task);
		}else{
			return 0;
		}

		return 1;

	}

	@SuppressWarnings("unchecked")
	public  T take (String name) throws InterruptedException{

		return (T) (isQueueExisted(name) ?queueMap.get(name).take():null);
	}
	
	public SeckillInfo peek(String name){
		return (SeckillInfo) (isQueueExisted(name) ?queueMap.get(name).peek():null);
	}

	public boolean isQueueExisted(String name){
		return null != queueMap.get(name) && queueMap.get(name).size()>=0;
	}
}
