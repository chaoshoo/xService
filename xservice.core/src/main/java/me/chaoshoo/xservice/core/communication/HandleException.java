/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月16日 下午1:40:45  
 */
package me.chaoshoo.xservice.core.communication;

/**
 * @Title: HandleException.java 
 * @Package: me.chaoshoo.xservice.core.protocol 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月16日 下午1:40:45 
 * @version:  
 */
public class HandleException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5738450234221126463L;

	public HandleException(String msg) {
		super(msg);
	}
	
	public HandleException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
	public HandleException(Throwable throwable) {
		super(throwable);
	}
}
