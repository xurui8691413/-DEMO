package com.seckill.cache;

import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.seckill.entity.Seckill;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Repository
public class RedisDao {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ShardedJedisPool shardedJedisPool;

	/*public RedisDao(String ip, int port) {
		HashMap map = new HashMap();
		CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
		logger.info("---------------------------------ip:{},port:{}",ip,port);
		this.port = port;
		this.ip = ip;
	}*/

	//Serialize function
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

	public Seckill getSeckill(long seckillId) {
		//redis operate
		ShardedJedis jedis =null;
		try {
			jedis = shardedJedisPool.getResource();
			String key = "seckill:" + seckillId;
			//����redis�ڲ�û��ʵ�����л�����,����jdk�Դ���implaments Serializable�Ƚ���,��Ӱ�첢��,�����Ҫʹ�õ��������л�����.
			byte[] bytes = jedis.get(key.getBytes());
			if(null != bytes){
				Seckill seckill = schema.newMessage();
				ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
				//reSerialize
				return seckill;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally{
			if(null != jedis){
				jedis.close();
			}
		}

		return null;
	}

	public String putSeckill(Seckill seckill) {
		//set Object(seckill) ->Serialize -> byte[]
		ShardedJedis jedis = null;
		try{
			jedis = shardedJedisPool.getResource();
			String key = "seckill:"+seckill.getSeckillId();
			byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
			//time out  cache
			int timeout = 60*60;
			String result = jedis.setex(key.getBytes(),timeout,bytes);
			return result;
		}catch (Exception e){
			logger.error(e.getMessage(),e);
		}finally{
			if(null != jedis){
				jedis.close();
			}
		}
		return null;
	}
}
