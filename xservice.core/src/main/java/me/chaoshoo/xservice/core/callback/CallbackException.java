/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月30日 下午6:21:15  
 */
package me.chaoshoo.xservice.core.callback;

/**
 * @Title: CallbackException.java 
 * @Package: me.chaoshoo.xservice.core.callback 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月30日 下午6:21:15 
 * @version:  
 */
public class CallbackException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7204831942853639346L;

	public CallbackException() {
		super();
	}
	
	public CallbackException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public CallbackException(String message) {
		super(message);
	}
	
	public CallbackException(Throwable throwable) {
		super(throwable);
	}
}
