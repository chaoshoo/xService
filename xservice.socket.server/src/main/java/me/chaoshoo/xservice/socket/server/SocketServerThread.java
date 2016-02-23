/**
 * Copyright:
 * All right reserved
 * Created on: 2015年9月17日 下午5:59:53  
 */
package me.chaoshoo.xservice.socket.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.communication.HandleException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.core.communication.ResponseProducer;
import me.chaoshoo.xservice.socket.SocketMessage;

/**
 * @Title: SocketServerThread.java
 * @Package: me.chaoshoo.storage.socket
 * @Description:
 * @author: Hu Chao
 * @date: 2015年9月17日 下午5:59:53
 * @version:
 */
public abstract class SocketServerThread implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(SocketServerThread.class);

	protected SocketChannel socketChannel;
	
	public SocketServerThread(SocketChannel socketChannel) {
		super();
		this.socketChannel = socketChannel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		execute();
	}

	private void execute() {
		Message responseMessage = null;
		Message message = null;
		ResponseProducer responseProducer = null;
		MessageHandler handler = null;
		try {
			message = new SocketMessage(socketChannel).read();
			log.debug("Message content: {}", message.toString());
			responseProducer = createResponseProducer(message);
			handler = createMessageHandler(message);
			responseMessage = handler.handle(message);
		} catch(CommunicationException | IOException e) {
			responseMessage = new Message();
			responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_EXCEPTION);
			responseMessage.param(Message.HANDLE_RESULT_EXCEPTION_MESSAGE, e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		} catch (HandleException e) {
			responseMessage = new Message();
			responseMessage.param(Message.PARAM_NAMESPACE, message.namespace());
			responseMessage.param(Message.PARAM_SERVICE_ID, message.serviceId());
			responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_EXCEPTION);
			responseMessage.param(Message.HANDLE_RESULT_EXCEPTION_MESSAGE, e.getLocalizedMessage());
			log.error(e.getLocalizedMessage(), e);
		} finally {
			if (null != responseProducer) {
				responseProducer.response(responseMessage);
			}
			try {
				socketChannel.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	protected abstract ResponseProducer createResponseProducer(Message message);
	
	protected abstract MessageHandler createMessageHandler (Message message);
	
}
