package com.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.seckill.entity.Seckill;

public interface SeckillDao {

	/**
	 * reduce product number
	 * @param seckillId
	 * @param killTime
	 * @return if effect line number >=1 ,means update lines. if return 0 means reduce fail
	 */
	int reduceNumber( @Param("seckillId") long seckillId,@Param("killTime") Date killTime);

	/**
	 * query seckill product
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(@Param("seckillId")long seckillId);

	/**
	 * query products from seckill
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);

	void killByProcedure(Map<String,Object> paramMap);

}