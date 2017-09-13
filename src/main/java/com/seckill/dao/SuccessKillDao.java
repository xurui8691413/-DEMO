package com.seckill.dao;

import org.apache.ibatis.annotations.Param;

import com.seckill.entity.SuccessKill;

public interface SuccessKillDao {

	/**
	 * insert success killed detail,can filter repeat
	 * @param seckillId
	 * @param userPhone
	 * @return insert lines
	 */
	int insertSuccessKill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);

	/**
	 * query successKill with seckill by Id
	 * @param seckillId
	 * @return
	 */
	SuccessKill queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);



}
