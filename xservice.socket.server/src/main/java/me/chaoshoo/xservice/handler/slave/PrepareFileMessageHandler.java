/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月19日 下午6:43:33  
 */
package me.chaoshoo.xservice.handler.slave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.communication.HandleException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: PrepareFileMessageHandler.java
 * @Package: me.chaoshoo.xservice.handler
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月19日 下午6:43:33
 * @version:
 */
public class PrepareFileMessageHandler extends MessageHandler {
	
	private static final Logger log = LoggerFactory.getLogger(PrepareFileMessageHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.handler.MessageHandler#handleMessage(java.util.Map)
	 */
	public Message handleMessage(Message message) throws HandleException {
		String fileName = message.param(Message.PARAM_FILE_NAME);
		Long fileSize = Long.parseLong(message.param(Message.PARAM_FILE_SIZE));
		Message responseMessage = new Message();
		String srcFilePath = XserviceServerConfig.srcFileDirPath() + File.separator
				+ message.namespace() + File.separator + fileName;
		File srcFile = new File(srcFilePath);
		if (!srcFile.getParentFile().exists()) {
			srcFile.getParentFile().mkdirs();
		}
		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(srcFile, "rw");
			accessFile.setLength(fileSize);
			responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_SUCCESS);
			return responseMessage;
		} catch (FileNotFoundException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new HandleException(e.getLocalizedMessage(), e);
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new HandleException(e.getLocalizedMessage(), e);
		} finally {
			if (null != accessFile) {
				try {
					accessFile.close();
				} catch (IOException e) {
					log.error(e.getLocalizedMessage(), e);
					throw new HandleException(e.getLocalizedMessage(), e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#checkParam(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected Boolean checkParam(Message message) throws HandleException {
		if (null == message.namespace()) {
			throw new HandleException("The namespace of a message could not be null during handling, message: "
					+ message.toString());
		}
		if (null == message.param(Message.PARAM_FILE_NAME)) {
			throw new HandleException(
					"The file name of a message could not be null during handling send file message, message: "
							+ message.toString());
		}
		try {
			Long.valueOf(message.param(Message.PARAM_FILE_SIZE));			
		} catch(NumberFormatException e) {
			throw new HandleException("The file size of a message could not be null during handling send file message, message: " + message.toString());
		}
		return true;
	}

}
