package com.seckill.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


public class LogWriterServiceImpl {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void wirteLogMessage(){
		logger.debug("Everything is ok");
	}
}
