package com.seckill.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.FutureTask;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seckill.cache.BuyQueue;
import com.seckill.cache.RedisDao;
import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKillDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExcution;
import com.seckill.dto.SeckillInfo;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKill;
import com.seckill.service.enums.SeckillError;
import com.seckill.service.enums.SeckillStatusEnum;
import com.seckill.service.exception.RepeatKillException;
import com.seckill.service.exception.SeckillCloseException;
import com.seckill.service.exception.SeckillException;

@Service
@PropertySource(value = "classpath:salt.properties")
public class SeckillServiceImpl implements SeckillService{

	private BuyQueue<SeckillExcution> buyQueue = new BuyQueue<SeckillExcution>();

	@Autowired
	private SuccessKillDao successKillDao;
	@Autowired
	private SeckillDao seckillDao;
	@Value("${salt}")
	public String salt;
	@Autowired
	private RedisDao redisDao; 
	@Autowired
	private ThreadPoolManager threadPoolManager;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	/**
	 * expose seckill url when seckill start,else expose system time and kill time
	 * @param seckillId
	 */
	public Exposer exposeSeckillUrl(long seckillId) {
		Seckill seckill = redisDao.getSeckill(seckillId);
		if(null == seckill || seckill.getSeckillNo()==0) {
			seckill = getById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			}
			else{
				redisDao.putSeckill(seckill);
			}
		}
		Date createTime = seckill.getCreateTime();
		Date endTime = seckill.getEndTime();
		Date currentTime = new Date();
		//seckill success
		if(currentTime.after(createTime) && currentTime.before(endTime)){
			//conversion String to special String (can't reverse)
			String md5 = getMd5(seckillId);
			return new Exposer(true,md5,seckillId);
		}
		else{
			return new Exposer(false,seckillId,currentTime.getTime(),createTime.getTime(),endTime.getTime());
		}
	}

	/*public Exposer exposeSeckillUrl(long seckillId) {
		Seckill seckill = seckillDao.queryById(seckillId);
		if(null == seckill) {
			seckill = getById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			}

		}
		Date createTime = seckill.getCreateTime();
		Date endTime = seckill.getEndTime();
		Date currentTime = new Date();
		//seckill success
		if(currentTime.after(createTime) && currentTime.before(endTime)){
			//conversion String to special String (can't reverse)
			String md5 = getMd5(seckillId);
			return new Exposer(true,md5,seckillId);
		}
		else{
			return new Exposer(false,seckillId,currentTime.getTime(),createTime.getTime(),endTime.getTime());
		}
	}*/

	private String getMd5(long seckillId){
		String base = seckillId+"/"+salt;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	  //rollback when runtimeException happend
	public SeckillExcution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		if(md5 == null || !md5.equals(getMd5(seckillId))){
			throw new SeckillException(SeckillError.SECKILL_REWRITE);
		}
		Seckill  seckill= redisDao.getSeckill(seckillId);
		if(seckill.getSeckillNo()<1){
			throw new SeckillCloseException(SeckillError.SECKILL_CLOSED); 
		}
		SeckillInfo seckillInfo = new SeckillInfo(seckill.getName(), seckill.getSeckillNo(), seckillId, userPhone);
		/*try {
			int sucess = buyQueue.put(seckill.getName(), seckillInfo);
			if(sucess<1){
				return new SeckillExcution(seckillId, SeckillStatusEnum.WAIT);
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			throw new SeckillException(SeckillError.SECKILL_INTERNAL_ERR,e1.getMessage());
		}*/
		
		
//		 DealQueueThread dealQueue = new DealQueueThread(redisDao, this, seckill.getName());
//		 ThreadPoolManager.newInstance().execute(dealQueue);
		 /*Callable<SeckillExcution> callable = new  DealQueueThread(redisDao, this, seckillInfo);
		 FutureTask<SeckillExcution> future = new FutureTask<SeckillExcution>(callable);*/
//		 try {
//				int sucess = buyQueue.put(seckill.getName(), seckillInfo,future);
//				if(sucess<1){
//					return new SeckillExcution(seckillId, SeckillStatusEnum.WAIT);
//				}
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				throw new SeckillException(SeckillError.SECKILL_INTERNAL_ERR,e1.getMessage());
//			}
		
	     logger.info("Thread.activeCount()="+Thread.activeCount());
		try {
//			threadPoolManager.execute(future);
			return threadPoolManager.execute(seckillInfo, redisDao, this);
//			if(!threadPoolManager.execute(future)){
//				return new SeckillExcution(seckillId, SeckillStatusEnum.WAIT);
//			}
//			return future.get();
		}catch (RepeatKillException|SeckillException e) {
			// TODO Auto-generated catch block
			throw e; 
		}catch(CancellationException e){
			return new SeckillExcution(seckillId, SeckillStatusEnum.END);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			 if (e.getCause() instanceof RepeatKillException ) {
				throw (RepeatKillException)e.getCause();
				
			}else if(e.getCause() instanceof SeckillException){
				throw (SeckillException)e.getCause();
			}
			 logger.error(e.getMessage(),e.getCause());
			throw new SeckillException(SeckillError.SECKILL_INTERNAL_ERR,e); 
		}
		
//		return new SeckillExcution(seckillId, SeckillStatusEnum.SUCCESS);
		
	}

	@Transactional
	public SeckillExcution execProcess(long seckillId, long userPhone){
		if(redisDao.getSeckill(seckillId).getSeckillNo()<1){
			throw new SeckillCloseException(SeckillError.SECKILL_CLOSED);
		}
		Date currentTime = new Date();
		try {
			//record purchase message
			int insertCount = successKillDao.insertSuccessKill(seckillId, userPhone);
			if(insertCount<=0){
				//repeat seckill
				throw new RepeatKillException(SeckillError.SECKILL_REPEATED);
			}
			else{
				//execute seckill:1.reduce product 2.record purchase message    //�ȵ���Ʒ����
				int updateCount = seckillDao.reduceNumber(seckillId,currentTime);
				//do not update for record
				if(updateCount<=0){
					throw new SeckillCloseException(SeckillError.SECKILL_CLOSED);
				}
				else{
					redisDao.putSeckill(seckillDao.queryById(seckillId));
					//��ɱ�ɹ�
					SuccessKill successKill = successKillDao.queryByIdWithSeckill(seckillId,userPhone);
					return new SeckillExcution(seckillId, SeckillStatusEnum.SUCCESS,successKill);
				}

			}


		}catch (SeckillCloseException|RepeatKillException e){
			throw e;
		}catch (Exception e){
			//rollback
			logger.error(e.getMessage(),e.getCause());
			throw new SeckillException(SeckillError.SECKILL_INTERNAL_ERR,e.getMessage());
		}
	}
	public SeckillExcution executeSeckillProcedure(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		if(md5 == null || md5.equals(getMd5(seckillId))){
			return new SeckillExcution(seckillId,SeckillStatusEnum.DATA_REWRITE);
		}
		Date time = new Date();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("seckillId",seckillId);
		map.put("phone",userPhone);
		map.put("killTime",time);
		map.put("result",null);
		try {
			seckillDao.killByProcedure(map);
			int result = MapUtils.getInteger(map,"result",-2);
			if(result == 1){
				SuccessKill successKill = successKillDao.queryByIdWithSeckill(seckillId,userPhone);
				return new SeckillExcution(seckillId,SeckillStatusEnum.SUCCESS,successKill);
			}
			else{
				return new SeckillExcution(seckillId,SeckillStatusEnum.statusOf(result));
			} 
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return new SeckillExcution(seckillId,SeckillStatusEnum.INNER_ERROR);
		}
	}

}