package com.seckill.service.enums;

public enum SeckillError {

	SECKILL_REPEATED("SECKILL_ERR_001", "Seckill request was repeated."),
	SECKILL_CLOSED("SECKILL_ERR_002", "Seckill activity has been ended"),
	SECKILL_REWRITE("SECKILL_ERR_003", "Seckill request data was rewrited"),
	SECKILL_INTERNAL_ERR("SECKILL_ERR_500", "seckill inner error: #");

	  private String code = null;
	  private String message = null;

	  private SeckillError(String code, String message) {

	    this.code = code;
	    this.message = message;
	  }

	  public static String getErrorMessage(String code) {

	        for (SeckillError e : SeckillError.values()) {
	            if (e.getCode().equals(code))
	                return e.getMessage();
	        }

	        return null; // nothing found
	    }

	  public String getCode() {
		return code;
	}


	public String getMessage() {
	        return message;
	  }

	  
	  @Override
	  public String toString() {
	    return code + ": " + message;
	  }
	  
	  public String getFormatMessage(String message){
		  return message.replace("#", message);
	  }
}
