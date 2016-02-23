/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月15日 下午4:59:35  
 */
package me.chaoshoo.xservice.core.communication;

/**
 * @Title: MessageConstructionException.java 
 * @Package: me.chaoshoo.xservice.core.protocol.message.socket 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月15日 下午4:59:35 
 * @version:  
 */
public class CommunicationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2281122637931198296L;

	public CommunicationException(String msg) {
		super(msg);
	}
	
	public CommunicationException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	
	public CommunicationException(Throwable throwable) {
		super(throwable);
	}
}
