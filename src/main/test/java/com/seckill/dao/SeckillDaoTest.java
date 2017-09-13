package com.seckill.dao;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.seckill.entity.Seckill;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	@Resource
	private SeckillDao seckillDao;

	@Test
	public void testReduceNumber() {
		
		  Calendar calendar=Calendar.getInstance();
//	       calendar.set(2017, 5, 27);  // 
	       calendar.set(2017, 4, 28,12,00,00); 
	       Date date=calendar.getTime();// 
		System.out.printf("Update count is %d",seckillDao.reduceNumber(1, date));;
	}

	@Test
	public void testQueryById() {
		long id = 1;
		Seckill o =seckillDao.queryById(id);
		System.out.println(o.getName());
	}

	@Test
	public void testQueryAll() {
		List<Seckill> lst =seckillDao.queryAll(0, 100);
		
		for(Seckill o: lst){
			System.out.println(o);
		}
	}
//
//	@Test
//	public void testKillByProcedure() {
//		fail("Not yet implemented");
//	}

}
