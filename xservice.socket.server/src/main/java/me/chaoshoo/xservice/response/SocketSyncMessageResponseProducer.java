/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月19日 下午3:10:13  
 */
package me.chaoshoo.xservice.response;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.communication.Command;
import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.ResponseProducer;
import me.chaoshoo.xservice.socket.SocketMessage;

/**
 * @Title: MessageResponseProducer.java
 * @Package: me.chaoshoo.xservice.response
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月19日 下午3:10:13
 * @version:
 */
public class SocketSyncMessageResponseProducer implements ResponseProducer {

	private static final Logger log = LoggerFactory.getLogger(SocketSyncMessageResponseProducer.class);

	private SocketChannel socketChannel;

	/**
	 * @param socketChannel
	 */
	public SocketSyncMessageResponseProducer(SocketChannel socketChannel) {
		super();
		this.socketChannel = socketChannel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.core.communication.ResponseProducer#response(java.
	 * util.Map)
	 */
	@Override
	public void response(Message message) {
		if (null == message.command()) {
			message.command(Command.RESPONSE);			
		}
		SocketMessage socketMessage = new SocketMessage(socketChannel);
		try {
			socketMessage.write(message);
		} catch (IOException | CommunicationException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

}
