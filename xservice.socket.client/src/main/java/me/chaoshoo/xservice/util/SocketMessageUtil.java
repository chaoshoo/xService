/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月29日 下午2:35:21  
 */
package me.chaoshoo.xservice.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.socket.SocketMessage;

/**
 * @Title: SocketMessageUtil.java 
 * @Package: me.chaoshoo.xservice.util 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月29日 下午2:35:21 
 * @version:  
 */
public class SocketMessageUtil {
	
	private static final Logger log = LoggerFactory.getLogger(SocketMessageUtil.class);
	
	/**
	 * 发送同步message，阻塞，等待获取响应message
	 * @param message
	 * @param ip
	 * @param port
	 * @return
	 * @throws CommunicationException
	 */
	public static Message syncSendMessage(Message message, String ip, Integer port) throws CommunicationException {
		log.debug("Send sync message to {}", ip + ":" + port + ":" + message.toString());
		SocketChannel socketChannel = null;
		try {
			socketChannel = createSocketChannel(ip, port);
			SocketMessage socketMessage = new SocketMessage(socketChannel);
			socketMessage.write(message);
			socketChannel.shutdownOutput();
			SocketMessage receiveSocketMessage = new SocketMessage(socketChannel);
			Message receiveMessage = receiveSocketMessage.read();
			log.debug("Receive sync response message from {}", ip + ":" + port + ":" + receiveMessage.toString());
			return receiveMessage;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			throw new CommunicationException(e.getMessage(), e);
		} finally {
			try {
				socketChannel.close();
			} catch (IOException e) {
				socketChannel = null;
				log.error(e.getLocalizedMessage(), e);
				throw new CommunicationException(e.getMessage(), e);
			}
		}
	}

	/**
	 * 发送异步message，不阻塞，由服务器自行调用异步回调方法
	 * @param message
	 * @param ip
	 * @param port
	 * @throws CommunicationException
	 */
	public static void asyncSendMessage(Message message, String ip, Integer port) throws CommunicationException {
		log.debug("Send async message to {}", ip + ":" + port + ":" + message.toString());
		SocketChannel socketChannel = null;
		try {
			message.param(Message.PARAM_SYNC_TYPE, Message.SYNC_TYPE_ASYNC);
			socketChannel = createSocketChannel(ip, port);
			SocketMessage socketMessage = new SocketMessage(socketChannel);
			socketMessage.write(message);
			socketChannel.shutdownOutput();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			throw new CommunicationException(e.getMessage(), e);
		} finally {
			try {
				socketChannel.close();
			} catch (IOException e) {
				socketChannel = null;
				log.error(e.getLocalizedMessage(), e);
				throw new CommunicationException(e.getMessage(), e);
			}
		}
	}

	/**
	 * 创建socket channel链接
	 * @param ip
	 * @param port
	 * @return
	 * @throws IOException
	 */
	private static SocketChannel createSocketChannel(String ip, Integer port) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		SocketAddress socketAddress = new InetSocketAddress(ip, port);
		socketChannel.connect(socketAddress);
		return socketChannel;
	}
}
