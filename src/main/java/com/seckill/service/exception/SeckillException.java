package com.seckill.service.exception;

import com.seckill.service.enums.SeckillError;

public class SeckillException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeckillException(SeckillError error) {
		super(error.getMessage());
	}

	@Deprecated
	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}
	public SeckillException(SeckillError error, Throwable cause) {
		super(error.getMessage(), cause);
	}
	
	public SeckillException(SeckillError error, String message) {
		super(error.getFormatMessage(message));
	}
	
}
