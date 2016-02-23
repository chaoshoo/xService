/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月26日 下午3:05:48  
 */
package me.chaoshoo.xservice.socket.server.master;

import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.core.communication.ResponseProducer;
import me.chaoshoo.xservice.handler.UnkownMessageHandler;
import me.chaoshoo.xservice.handler.master.GetFileMessageHandler;
import me.chaoshoo.xservice.handler.master.PrepareFileMessageHandler;
import me.chaoshoo.xservice.handler.master.RequestServiceMessageHandler;
import me.chaoshoo.xservice.handler.master.SendFileMessageHandler;
import me.chaoshoo.xservice.handler.master.UpdateSlaveInfoMessageHandler;
import me.chaoshoo.xservice.response.SocketSyncMessageResponseProducer;
import me.chaoshoo.xservice.socket.server.SocketServerThread;

/**
 * @Title: SocketMasterServerThread.java 
 * @Package: me.chaoshoo.xservice.socket.server 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月26日 下午3:05:48 
 * @version:  
 */
public class SocketMasterServerThread extends SocketServerThread{
	
	private static final Logger log = LoggerFactory.getLogger(SocketMasterServerThread.class);

	/**
	 * @param socketChannel
	 * @param isMaster
	 */
	public SocketMasterServerThread(SocketChannel socketChannel) {
		super(socketChannel);
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.socket.server.SocketServerThread#createResponseProducer(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected ResponseProducer createResponseProducer(Message message) {
		return new SocketSyncMessageResponseProducer(socketChannel);
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.socket.server.SocketServerThread#createMessageHandler(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected MessageHandler createMessageHandler(Message message) {
		log.debug("Message command : {}", message.command());
		switch (message.command()) {
		case REQUEST_SERVICE:
			return new RequestServiceMessageHandler();
		case UPDATE_SLAVE_INFO:
			return new UpdateSlaveInfoMessageHandler();
		case PREPARE_FILE:
			return new PrepareFileMessageHandler();
		case SEND_FILE:
			return new SendFileMessageHandler();
		case GET_FILE:
			return new GetFileMessageHandler();
		default:
			return new UnkownMessageHandler();
		}
	}

}
