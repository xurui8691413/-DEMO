package com.seckill.service;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.DigestUtils;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExcution;
import com.seckill.entity.Seckill;
import com.seckill.service.exception.RepeatKillException;
import com.seckill.service.exception.SeckillCloseException;

import net.sf.json.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
"classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String BASE_URI="http://localhost:8082/seckill/";
	@Resource
	SeckillService seckillService;

	@Test
	public void testGetSeckillList() throws Exception {
		List<Seckill> seckills = seckillService.getSeckillList();
		logger.info("1.seckills={}",seckills);
	}

	@Test
	public void testGetById() throws Exception {
		long id = 1000l;
		Seckill seckill = seckillService.getById(id);
		logger.info("2.seckill={}",seckill);
	}

	@Test
	public void testSeckillLogic() throws Exception {
		long id = 1002l;
		Exposer exposer = seckillService.exposeSeckillUrl(id);
		if(exposer.isExposed()){
			logger.info("exposer={}",exposer);
			String md5 = exposer.getMd5();
			long phone = 18868831752l;
			try{
				SeckillExcution seckillExcution = seckillService.executeSeckill(id, phone, md5);
				logger.info("seckillExcuation={}"+seckillExcution);
			}catch (RepeatKillException|SeckillCloseException e){
				logger.error(e.getMessage());
			}
		}
		else{
			//seckill don't start
			logger.warn("exposer={}",exposer);
		}
	}

	@Test
	public void testExecuteSeckill() throws Exception {
		//		long id = 1002l;
		//		long phone = 18868831752l;
		//		try{
		//			String md5 = "2bf959ac10da022a1bc9a7c98c4b42c8";
		//			SeckillExcution seckillExcution = seckillService.executeSeckill(id, phone, md5);
		//			logger.info("seckillExcuation={}"+seckillExcution);
		//		}catch (RepeatKillException e){
		//			logger.error(e.getMessage());
		//		}catch (SeckillCloseException e){
		//			logger.error(e.getMessage());
		//		}

	}
	@Test
	public void executeSeckillProcedure(){
		long seckillId = 1004;
		long phone = 18868831756l;
		Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
		if(exposer.isExposed()){
			String md5 = exposer.getMd5();
			SeckillExcution seckillExcution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
			logger.info("============="+seckillExcution.getStatusInfo());
		}
	}

	//	@Test
	public static void main (String [] args){
		final ClientConfig cc = getClientConfig();
		final Client client = ClientBuilder.newClient(cc);
		//	        WebResource service = client.resource(getBaseURI());  
		//	        final Book newBook = new Book("Java Restful Web Service实战-" + System.nanoTime());
		//	        final Entity<Book> bookEntity = Entity.entity(newBook, MediaType.APPLICATION_JSON_TYPE);
		final Entity<String> bookEntity = Entity.entity("12345678919", MediaType.TEXT_HTML);
		//	        System.out.println(client.target(BASE_URI).path("2").path("26cd2786d28444eb586c2b652e5bffbc").path("execution").getUri());
		System.out.println(getNRandomNum(11));
		System.out.println(String.valueOf((10000000000L*(Math.random()))));
		Response s = client.target(BASE_URI).path("2").path("26cd2786d28444eb586c2b652e5bffbc").path("execution").request("application/json;charset=UTF-8").cookie(new Cookie("killPhone", getNRandomNum(11))).post(Entity.entity(null, "application/json;charset=UTF-8"));
//		Response s= client.target(BASE_URI).path("2").path("26cd2786d28444eb586c2b652e5bffbc").path("execution").path(getNRandomNum(11)).request("application/json;charset=UTF-8").post(Entity.entity(null, "application/json;charset=UTF-8"));
		JSONObject json  =  JSONObject.fromObject(s);
		System.out.println(json);
		System.out.println(s.readEntity(String.class)); 
		for(int i=0;i<4000;i++){
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Response s= client.target(BASE_URI).path("2").path("26cd2786d28444eb586c2b652e5bffbc").path("execution").request("application/json;charset=UTF-8").cookie(new Cookie("killPhone", getNRandomNum(11))).post(Entity.entity(null, "application/json;charset=UTF-8"));
					//					       System.out.println(s.getEntity()); 
					System.out.println(s.readEntity(String.class)); 
				}
			}).start();
		}

	}

	public static String getNRandomNum(int digit) {  

		int maxDigit = 18;  
		double operand;  
		StringBuffer sb = new StringBuffer();  

		/* 
		 * 如果超过最大位数.则每maxDigit(默认18)位计算一次随机数. 
		 */  
		if (digit > maxDigit) {  
			operand = Math.pow(10, maxDigit - 1);  

			for (int i=0; i<digit / maxDigit; i++) {  
				long num = (long) (Math.random() * 9 * operand + operand);  
				sb = sb.append(num);  
			}  

			operand = digit % maxDigit;  
			if (operand != 0)  
				sb = sb.append(getNRandomNum((int) operand));  
		}  
		else {  
			operand = Math.pow(10, digit - 1);  
			long num = (long) (Math.random() * 9 * operand + operand);  
			sb = sb.append(num);  
		}  

		return sb.toString();  
	}  

	private String getMd5(long seckillId){
		String base = seckillId+"/"+"dfsdfdsf7JHJJsada*&asdasd";
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	private static ClientConfig getClientConfig() {
		final ClientConfig cc = new ClientConfig();
		/*2.5-*/
		//cc.register(new HttpBasicAuthFilter("caroline", "zhang"));
		/*2.5+*/
		//	        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().nonPreemptive().credentials("caroline", "zhang").build();
		//	        cc.register(feature);
		return cc;
	}

}
