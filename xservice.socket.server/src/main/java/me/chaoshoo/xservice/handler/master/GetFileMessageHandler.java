/**
 * Copyright:
 * All right reserved
 * Created on: 2015年11月3日 下午2:36:21  
 */
package me.chaoshoo.xservice.handler.master;

import java.io.File;

import me.chaoshoo.xservice.core.communication.Command;
import me.chaoshoo.xservice.core.communication.HandleException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.util.StringUtil;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: GetFileMessageHandler.java 
 * @Package: me.chaoshoo.xservice.handler.master 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年11月3日 下午2:36:21 
 * @version:  
 */
public class GetFileMessageHandler extends MessageHandler {

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#checkParam(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected Boolean checkParam(Message message) throws HandleException {
		if (StringUtil.isNullOrEmpty(message.namespace())) {
			throw new HandleException("The namespace of a message could not be null during handling, message id: " + message.toString());
		}
		if (StringUtil.isNullOrEmpty(message.serviceId())) {
			throw new HandleException("The service id of a message could not be null during handling, message id: " + message.toString());
		}
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_FILE_NAME))) {
			throw new HandleException("The file name of a message could not be null during handling send file message, message id: " + message.toString());
		}
		try {
			Long.valueOf(message.param(Message.PARAM_FILE_POSITION));			
		} catch(NumberFormatException e) {
			throw new HandleException("The file position of a message could not be null during handling send file message, message id: " + message.toString());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#handleMessage(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected Message handleMessage(Message message) throws HandleException {
		String namespace = message.namespace();
		String serviceId = message.serviceId();
		String fileName = message.param(Message.PARAM_FILE_NAME);
		Long position =	Long.valueOf(message.param(Message.PARAM_FILE_POSITION));	
		String srcFilePath = XserviceServerConfig.masterFileStore() + File.separator
				+ message.namespace() + File.separator + message.serviceId() + File.separator + fileName;
		File srcFile = new File(srcFilePath);
		Message responseMessage = new Message().command(Command.SEND_FILE);
		responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_SUCCESS);
		responseMessage.namespace(namespace).serviceId(serviceId).file(srcFile).filePosition(position);
		return responseMessage;
	}

}
