/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月19日 下午5:07:21  
 */
package me.chaoshoo.xservice.client.socket;

import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.util.SocketMessageUtil;

/**
 * @Title: SocketClient.java
 * @Package: me.chaoshoo.xservice.socket
 * @Description: 客户端程序socket模式下抽象类定义
 * @author: Hu Chao
 * @date: 2015年10月19日 下午5:07:21
 * @version:
 */
public abstract class NioSocketClient {
	
	protected String ip;
	
	protected Integer port;
	
	/**
	 * @param ip
	 * @param port
	 */
	public NioSocketClient(String ip, Integer port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	protected void asyncSendMessage(Message message) throws CommunicationException {
		SocketMessageUtil.asyncSendMessage(message, ip, port);
	}

	protected Message syncSendMessage(Message message) throws CommunicationException {
		return  SocketMessageUtil.syncSendMessage(message, ip, port);
	}
	public String ip() {
		return this.ip;
	}

	public Integer port() {
		return this.port;
	}

}
