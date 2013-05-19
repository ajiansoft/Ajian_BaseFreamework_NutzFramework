package com.nutz.framework.base.exception;


/**   
 * SystemException 概要说明  
 * 系统异常   
 */
@SuppressWarnings("serial")
public class SystemException extends RuntimeException {
	
	public SystemException() {
		super();
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemException(String message) {
		super(message);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}
}
