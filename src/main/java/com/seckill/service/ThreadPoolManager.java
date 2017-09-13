package com.seckill.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seckill.cache.RedisDao;
import com.seckill.dto.SeckillExcution;
import com.seckill.dto.SeckillInfo;

@Component
public class ThreadPoolManager<T extends SeckillExcution>  {
	//	private static ThreadPoolManager tpm = new ThreadPoolManager();

	// 线程池维护线程的最少数量
	private final static int CORE_POOL_SIZE = 4;

	// 线程池维护线程的最大数量
	private final static int MAX_POOL_SIZE = 10;

	// 线程池维护线程所允许的空闲时间
	private final static int KEEP_ALIVE_TIME = 0;

	// 线程池所使用的缓冲队列大小
	private final static int WORK_QUEUE_SIZE = 100;


	@Autowired
	private RedisDao redisDao;
	// 消息缓冲队列

	//	private final List<FutureTask> msgQueue = new <FutureTask>(KEEP_ALIVE_TIME);

	BlockingQueue<FutureTask<SeckillExcution>> msgQueue = new ArrayBlockingQueue<FutureTask<SeckillExcution>>(WORK_QUEUE_SIZE);
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());

	//	private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());

//	private int workerNum;
	public ThreadPoolManager() {
		//this.workerNum = CORE_POOL_SIZE;
		//initializeWorkers(this.workerNum);
	}

	//	private void initializeWorkers(int num) {
	//		for (int i = 0; i < num; i++) {
	//			Worker worker = new Worker();
	//			//添加到工作者线程的列表
	//			workers.add(worker);
	//			//启动工作者线程
	//			Thread thread = new Thread(worker);
	//			thread.start();
	//		}
	//	}
	final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
				CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());

	/*public void execute(Runnable r){
		threadPool.execute(r);

	}*/
	/*public boolean execute(FutureTask r,long seckillId) throws Exception{
		//		threadPool.execute(r);
		logger.debug("remaining Capacity of message queue is " + msgQueue.size());

		if(redisDao.getSeckill(seckillId).getSeckillNo()>0){
			msgQueue.put(r);
		}else{
			Collection<FutureTask> c = new ArrayList<>();
			msgQueue.drainTo(c);
			for(FutureTask task : c){
				task.cancel(true);
			}
			return false;
		}

		try {
			//			threadPool.setThreadFactory(threadFactory);
			threadPool.submit(msgQueue.take());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		return true;

	}*/

	public SeckillExcution execute(SeckillInfo t,RedisDao redisDao,SeckillService seckillService) throws InterruptedException, ExecutionException {
		
		FutureTask<SeckillExcution> future = new FutureTask<SeckillExcution>(new Worker(t, redisDao, seckillService));
		if (future != null) {
			//根据线程的"等待/通知机制"这里必须对jobs加锁
			msgQueue.put(future);
		}
		threadPool.execute(new Worker());
		return future.get();
	}

	//	public void cancle(Runnable runnable) {
	//		if(threadPool != null) {
	//			threadPool.getQueue().remove(runnable);
	//		}
	//	}

	class Worker implements Runnable,Callable<SeckillExcution> {
		// 表示是否运行该worker
		private volatile boolean cancel = false;

		RedisDao redisDao;

		private SeckillInfo  t;

		private SeckillService seckillService;
		
		public Worker() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Worker(SeckillInfo t,RedisDao redisDao,SeckillService seckillService ) {
			this.redisDao = redisDao;
			this.seckillService = seckillService;
			this.t = t;
//			this.queueName = name;
		}
		
		public void run() {

			while (!cancel) {
				try {
					FutureTask<SeckillExcution> job = msgQueue.take();
					if (job != null) {
						job.run();
					}
					

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage(),e);
				}


			}
		}

		// 终止该线程
		public void cancel() {
			cancel = true;
		}

		@Override
		public SeckillExcution call() throws Exception {
			// TODO Auto-generated method stub
			if(!cancelWorkIfSeckillEnd(t.getSeckillId())){
				return seckillService.execProcess(t.getSeckillId(), t.getUserPhone());
			}
			throw new CancellationException();
			
		}
		
		private boolean cancelWorkIfSeckillEnd(Long  seckillId){
			if(redisDao.getSeckill(seckillId).getSeckillNo()<1){
				Collection<FutureTask<SeckillExcution>> c = new ArrayList<>();
				msgQueue.drainTo(c);
				for(FutureTask<SeckillExcution> task : c){
					task.cancel(true);
				}
				return true;
			}
			return false;
		}
	}

	/*private void increaseThread(){
		boolean needIncrease = true;
		for(Worker o : workers){
			if(!o.wait){
				needIncrease = false;
				break;
			}
		}

		if(needIncrease){
			synchronized (workers) {
				if (1 + this.workerNum > MAX_POOL_SIZE) {
					return;
				}
				initializeWorkers(1);
				this.workerNum += 1;
			}
		}
	}*/

}
