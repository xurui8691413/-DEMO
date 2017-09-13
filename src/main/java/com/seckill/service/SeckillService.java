package com.seckill.service;

import java.util.List;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExcution;
import com.seckill.entity.Seckill;
import com.seckill.service.exception.RepeatKillException;
import com.seckill.service.exception.SeckillCloseException;
import com.seckill.service.exception.SeckillException;

public interface SeckillService {

	/**
	 * get all seckill
	 * @return
	 */
	List<Seckill> getSeckillList();

	/**
	 * get seckill by id
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);

	/**
	 * expose seckill url when seckill start,else expose system time and kill time
	 * @param seckillId
	 */
	Exposer exposeSeckillUrl(long seckillId);

	/**
	 * execute seckill
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	SeckillExcution executeSeckill(long seckillId,long userPhone,String md5) throws SeckillException,RepeatKillException,SeckillCloseException;
	public SeckillExcution execProcess(long seckillId, long userPhone);

	SeckillExcution executeSeckillProcedure(long seckillId,long userPhone,String md5) throws SeckillException,RepeatKillException,SeckillCloseException;
}
