/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月26日 下午3:05:48  
 */
package me.chaoshoo.xservice.client.callback;

import java.io.File;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.client.socket.NioSocketMasterClient;
import me.chaoshoo.xservice.core.MasterClient;
import me.chaoshoo.xservice.core.callback.Callback;
import me.chaoshoo.xservice.core.callback.CallbackFactory;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.socket.SocketMessage;

/**
 * @Title: SocketMasterServerThread.java
 * @Package: me.chaoshoo.xservice.socket.server
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月26日 下午3:05:48
 * @version:
 */
public class CallbackThread extends Thread {

	private static final Logger log = LoggerFactory.getLogger(CallbackThread.class);

	protected SocketChannel socketChannel;

	/**
	 * @param socketChannel
	 */
	public CallbackThread(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Message message = null;
		try {
			message = new SocketMessage(socketChannel).read();
			String namespace = message.namespace();
			String serviceId = message.serviceId();
			String fileName = message.param(Message.PARAM_FILE_NAME);
			String className = message.param(Message.PARAM_CALLBACK_CLASS_NAME);
			log.info("Get callback message for message: {}", namespace + ":" + serviceId + ":" + fileName + ":" + className);
			MasterClient masterClient = new NioSocketMasterClient();
			File result = masterClient.getFile(namespace, serviceId, fileName);
			Callback callback = CallbackFactory.build().createCallback(className);
			callback.callback(namespace, serviceId, result, message);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

}
