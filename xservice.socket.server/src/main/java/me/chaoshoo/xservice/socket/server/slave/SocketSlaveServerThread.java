/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月26日 下午3:07:47  
 */
package me.chaoshoo.xservice.socket.server.slave;

import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.core.communication.ResponseProducer;
import me.chaoshoo.xservice.handler.UnkownMessageHandler;
import me.chaoshoo.xservice.handler.slave.CallServiceMessageHandler;
import me.chaoshoo.xservice.handler.slave.PrepareFileMessageHandler;
import me.chaoshoo.xservice.handler.slave.SendFileMessageHandler;
import me.chaoshoo.xservice.handler.slave.ServiceListMessageHandler;
import me.chaoshoo.xservice.handler.slave.ServiceStatusMessageHandler;
import me.chaoshoo.xservice.response.HttpCallbackReqponseProducer;
import me.chaoshoo.xservice.response.SocketAsyncMessageResponseProducer;
import me.chaoshoo.xservice.response.SocketSyncMessageResponseProducer;
import me.chaoshoo.xservice.socket.server.SocketServerThread;

/**
 * @Title: SocketSlaveServerThread.java 
 * @Package: me.chaoshoo.xservice.socket 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月26日 下午3:07:47 
 * @version:  
 */
public class SocketSlaveServerThread extends SocketServerThread{

	private static final Logger log = LoggerFactory.getLogger(SocketSlaveServerThread.class);
	
	/**
	 * @param socketChannel
	 * @param isMaster
	 */
	public SocketSlaveServerThread(SocketChannel socketChannel) {
		super(socketChannel);
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.socket.server.SocketServerThread#createResponseProducer(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected ResponseProducer createResponseProducer(Message message) {
		if (null == message.param(Message.PARAM_CALLBACK_TYPE)) {
			return new SocketSyncMessageResponseProducer(socketChannel);
		} else {
			switch (message.param(Message.PARAM_CALLBACK_TYPE)) {
			case Message.CALLBACK_TYPE_HTTP:
				return new HttpCallbackReqponseProducer(message.param(Message.CALLBACK_HTTP_URL));
			case Message.CALLBACK_TYPE_MESSAGE:
				if (Message.SYNC_TYPE_ASYNC.equals(message.param(Message.PARAM_SYNC_TYPE))) {
					return new SocketAsyncMessageResponseProducer(
							message.param(Message.CALLBACK_SOCKET_IP),
							message.param(Message.CALLBACK_SOCKET_PORT));
				}
				return new SocketSyncMessageResponseProducer(socketChannel);
			default:
				return new SocketSyncMessageResponseProducer(socketChannel);
			}
		}
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.socket.server.SocketServerThread#createMessageHandler(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected MessageHandler createMessageHandler(Message message) {
		log.debug("Message command : {}", message.command());
		switch (message.command()) {
		case SERVICE_STATUS:
			return new ServiceStatusMessageHandler();
		case SERVICE_LIST:
			return new ServiceListMessageHandler();
		case PREPARE_FILE:
			return new PrepareFileMessageHandler();
		case SEND_FILE:
			return new SendFileMessageHandler();
		case CALL_SERVICE:
			return new CallServiceMessageHandler();
		default:
			return new UnkownMessageHandler();
		}
	}

}
