/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月16日 下午2:04:14  
 */
package me.chaoshoo.xservice.handler.slave;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.communication.Command;
import me.chaoshoo.xservice.core.communication.HandleException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.core.service.Service;
import me.chaoshoo.xservice.core.service.ServiceException;
import me.chaoshoo.xservice.core.service.ServiceFactory;
import me.chaoshoo.xservice.util.SocketMessageUtil;
import me.chaoshoo.xservice.util.StringUtil;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: CallServiceMessageHandler.java
 * @Package: me.chaoshoo.xservice.handler.impl
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月16日 下午2:04:14
 * @version:
 * 
 * 
 */
public class CallServiceMessageHandler extends MessageHandler {

	private static final Logger log = LoggerFactory.getLogger(CallServiceMessageHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.handler.MessageHandler#handleMessage(java.util.Map)
	 */
	public Message handleMessage(Message message) throws HandleException {
		String serviceName = message.param(Message.PARAM_SERVICE_NAME);
		try {
			Message responseMessage = new Message();
			responseMessage.params(message.params());
			Service service = ServiceFactory.createService(serviceName, XserviceServerConfig.srcFileDirPath(),
					XserviceServerConfig.tempFileDirPath(), XserviceServerConfig.destFileDirPath());
			File desFile = service.executeService(message);
			if (null != desFile) {
				Message masterMessage = new Message(message.namespace(), Command.PREPARE_FILE);
				masterMessage.file(desFile).serviceId(message.serviceId());
				String masterIp = XserviceServerConfig.masterServerIp();
				Integer masterPort = XserviceServerConfig.masterServerPort();
				try {
					Message receiveMessage = SocketMessageUtil.syncSendMessage(masterMessage, masterIp, masterPort);
					if (!receiveMessage.command().equals(Command.RESPONSE)
							|| !receiveMessage.param(Message.PARAM_HANDLE_RESULT).equals(Message.HANDLE_RESULT_SUCCESS)) {
						throw new HandleException("The service result file could not be put into file store!");
					}
					Long position = 0L;
					masterMessage = new Message(message.namespace(), Command.SEND_FILE).serviceId(message.serviceId());
					masterMessage.file(desFile).filePosition(position);
					while (position < desFile.length()) {
						receiveMessage = SocketMessageUtil.syncSendMessage(masterMessage, masterIp, masterPort);
						if (receiveMessage.command().equals(Command.RESPONSE)
								&& receiveMessage.param(Message.PARAM_HANDLE_RESULT).equals(
										Message.HANDLE_RESULT_SUCCESS)) {
							Long newPosition = Long.parseLong(receiveMessage.param(Message.PARAM_FILE_POSITION));
							if (null == newPosition) {
								log.error("Exception happen when send file: {}, position: " + position,
										desFile.getAbsolutePath());
								throw new HandleException("The new position to send file in message could not be null");
							}
							position = newPosition;
							masterMessage.filePosition(position);
						}
					}
				} catch (Exception e) {
					log.error(e.getLocalizedMessage(), e);
				}
				responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_SUCCESS);
			} else {
				responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_UN_SUCCESS);
			}
			return responseMessage;
		} catch (ServiceException e) {
			throw new HandleException(e.getLocalizedMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.core.communication.MessageHandler#checkParam(cn
	 * .com.nd.hb.xservice.core.communication.Message)
	 */
	@Override
	protected Boolean checkParam(Message message) throws HandleException {
		if (null == message.namespace()) {
			throw new HandleException(
					"The namespace of a call service message could not be null during handling, message: "
							+ message.toString());
		}
		if (null == message.serviceId()) {
			throw new HandleException(
					"The service id of a call service message could not be null during handling, message: "
							+ message.toString());
		}
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SERVICE_NAME))) {
			throw new HandleException("The service name of a call service message could not be null or empty!");
		}
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SERVICE_SRC_FILE_NAME))) {
			throw new HandleException("The src file name of a call service message could not be null or empty!");
		}
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SERVICE_DEST_FILE_NAME))) {
			message.param(Message.PARAM_SERVICE_DEST_FILE_NAME, message.param(Message.PARAM_SERVICE_SRC_FILE_NAME));
		}
		return true;
	}

}
