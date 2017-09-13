package com.seckill.service;


import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seckill.cache.BuyQueue;
import com.seckill.cache.RedisDao;
import com.seckill.dto.SeckillExcution;
import com.seckill.dto.SeckillInfo;


public class DealQueueThread  implements Callable<SeckillExcution>{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	RedisDao redisDao;

	private SeckillInfo  t;

	private SeckillService seckillService;

//	public static volatile  boolean excute = false;//�̵߳�Ĭ��ִ�б�־Ϊδִ��,������״̬
	
	public DealQueueThread(RedisDao redisDao,SeckillService seckillService,SeckillInfo t) {
		this.redisDao = redisDao;
		this.seckillService = seckillService;
		this.t = t;
//		this.queueName = name;
	}

//	public DealQueueThread(BuyQueue buyqueue,String name) {
//
//	}


//	@Override
	public void run() {
	/*	try {
			//��ʼ������������е�����,���ն��е�FIFO�Ĺ���,�ȴ����ȷ��뵽�����е�����
			while (null != buyqueue.peek(queueName)) {
				SeckillInfo buyreq = buyqueue.take(queueName);//ȡ�������е�����
				dealWithQueue(buyreq);//��������
			}
		} catch (InterruptedException e) {
			logger.error("DealQueueThread:", e);
		}*/
	}

	public  void dealWithQueue(SeckillInfo seckillInfo) {
		try {
			seckillService.execProcess(seckillInfo.getSeckillId(), seckillInfo.getUserPhone());
		} catch (Exception e) {
			logger.error("DealQueueThread dealWithQueue:", e);
		}
	}

	@Override
	public SeckillExcution call() throws Exception {
		// TODO Auto-generated method stub
		return seckillService.execProcess(t.getSeckillId(), t.getUserPhone());
	}

}
