package com.seckill.service.exception;

import com.seckill.service.enums.SeckillError;

public class RepeatKillException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Deprecated
	public RepeatKillException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
	public RepeatKillException(SeckillError error) {
		super(error.getMessage());
	}
	
	public RepeatKillException(SeckillError error, Throwable cause) {
		super(error.getMessage(), cause);
	}
	
}
