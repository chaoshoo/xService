/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月23日 上午8:27:07  
 */
package me.chaoshoo.xservice.response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.communication.Command;
import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.ResponseProducer;
import me.chaoshoo.xservice.socket.SocketMessage;

/**
 * @Title: SocketAsyncMessageResponseProducer.java 
 * @Package: me.chaoshoo.xservice.response 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月23日 上午8:27:07 
 * @version:  
 */
public class SocketAsyncMessageResponseProducer implements ResponseProducer {

	private static final Logger log = LoggerFactory.getLogger(SocketAsyncMessageResponseProducer.class);

	private String callbackIp;
	
	private String callbackPort;

	/**
	 * @param remoteIp
	 * @param remotePort
	 */
	public SocketAsyncMessageResponseProducer(String callbackIp, String callbackPort) {
		super();
		this.callbackIp = callbackIp;
		this.callbackPort = callbackPort;
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
		SocketChannel socketChannel = null;;
		try {
			socketChannel = SocketChannel.open();
			SocketAddress socketAddress = new InetSocketAddress(callbackIp, Integer.valueOf(callbackPort));
			socketChannel.connect(socketAddress);
			if (null == message.command()) {
				message.command(Command.RESPONSE);			
			}
			SocketMessage socketMessage = new SocketMessage(socketChannel);
			socketMessage.write(message);
			socketChannel.shutdownOutput();
		} catch (IOException | CommunicationException e) {
			log.error(e.getLocalizedMessage(), e);
		} finally {
			try {
				socketChannel.close();
			} catch (IOException e) {
				log.error(e.getLocalizedMessage(), e);
				socketChannel = null;
			}
		}
	}
}
