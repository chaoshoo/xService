/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月16日 下午1:50:05  
 */
package me.chaoshoo.xservice.core.communication;

/**
 * @Title: MessageHandler.java 
 * @Package: me.chaoshoo.xservice.core.protocol 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月16日 下午1:50:05 
 * @version:  
 */
public abstract class MessageHandler {
	
	/**
	 * 检查消息是否还有必要的参数，如果必要的参数确实则抛出异常
	 * @param message
	 * @return
	 * @throws HandleException
	 */
	protected abstract Boolean checkParam(Message message) throws HandleException;

	/**
	 * 处理消息的逻辑
	 * @param message
	 * @return
	 * @throws HandleException
	 */
	protected abstract Message handleMessage(Message message) throws HandleException;

	/**
	 * 消息处理方法，包括参数验证与逻辑处理
	 * @param message
	 * @return
	 * @throws HandleException
	 */
	public Message handle(Message message) throws HandleException {
		this.checkParam(message);
		return handleMessage(message);
	}
	
}
