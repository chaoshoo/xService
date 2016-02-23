/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月16日 上午10:33:30  
 */
package me.chaoshoo.xservice.core.communication;

import java.io.IOException;

/**
 * @Title: Sendable.java 
 * @Package: me.chaoshoo.xservice.core.protocol 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月16日 上午10:33:30 
 * @version:  
 */
public interface Communicatable {
	
	public static final Integer INTEGER_LENGTH_CAPACITY = 4;
	
	public static final Integer LONG_LENGTH_CAPACITY = 8;
	
	/**
	 * 输出消息
	 * @param message
	 * @throws IOException
	 * @throws CommunicationException
	 */
	void write(Message message) throws IOException, CommunicationException ;
	
	/**
	 * 读取消息并返回
	 * @return
	 * @throws IOException
	 * @throws CommunicationException
	 */
	Message read() throws IOException, CommunicationException ;
	
}
