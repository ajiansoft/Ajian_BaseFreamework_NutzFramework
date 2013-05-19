package com.nutz.framework.base.exception;
/**   
 * BusinessException 概要说明  
 * 业务异常   
 */
@SuppressWarnings("serial")
public class BusinessException extends Exception {
	
	public BusinessException() {
		super();
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
