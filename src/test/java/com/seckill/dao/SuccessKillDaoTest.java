package com.seckill.dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.seckill.entity.SuccessKill;

import org.junit.Assert;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKillDaoTest {
	
	
	@Resource
	private SuccessKillDao successKillDao;


	@Test
	public void testInsertSuccessKill() {
		int sucess = successKillDao.insertSuccessKill(1, 15962437262L);
		Assert.assertArrayEquals("insert failed", new int[]{1}, new int []{sucess});
	}

	@Test
	public void testQueryByIdWithSeckill() {
		SuccessKill o = successKillDao.queryByIdWithSeckill(1, 15962437262L);
		Assert.assertArrayEquals("query failed", new Object[]{15962437262L,"1000ÔªÃëÉ±iPhone6"}, new Object []{o.getUserPhone(),o.getSeckill().getName()});
		
	}

}
