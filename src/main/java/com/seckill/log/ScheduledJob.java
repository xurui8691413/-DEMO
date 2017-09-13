package com.seckill.log;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class ScheduledJob extends QuartzJobBean{

	
	LogWriterServiceImpl logWriterService;
	
	public LogWriterServiceImpl getLogWriterService() {
		return logWriterService;
	}

	public void setLogWriterService(LogWriterServiceImpl logWriterService) {
		this.logWriterService = logWriterService;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
	//	logWriterService.wirteLogMessage();
	}

}
