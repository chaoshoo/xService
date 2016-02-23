/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月16日 下午2:34:14  
 */
package me.chaoshoo.xservice.core.service;

/**
 * @Title: ServiceException.java 
 * @Package: me.chaoshoo.xservice.core.exception 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月16日 下午2:34:14 
 * @version:  
 */
public class ServiceException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7290998545124965824L;

	public ServiceException(String msg) {
		super(msg);
	}
	
	public ServiceException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
	public ServiceException(Throwable throwable) {
		super(throwable);
	}
}
