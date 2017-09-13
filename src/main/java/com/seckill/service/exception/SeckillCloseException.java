package com.seckill.service.exception;

import com.seckill.service.enums.SeckillError;

public class SeckillCloseException extends SeckillException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SeckillCloseException(SeckillError error) {
		super(error);
	}

	public SeckillCloseException(SeckillError error, Throwable cause) {
		super(error, cause);
	}
}
