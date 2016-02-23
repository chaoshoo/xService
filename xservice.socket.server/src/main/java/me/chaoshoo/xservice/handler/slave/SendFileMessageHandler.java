/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月19日 上午10:42:56  
 */
package me.chaoshoo.xservice.handler.slave;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import me.chaoshoo.xservice.core.communication.HandleException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.util.FileUtil;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: SendFileMessageHandler.java
 * @Package: me.chaoshoo.xservice.handler
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月19日 上午10:42:56
 * @version:
 */
public class SendFileMessageHandler extends MessageHandler {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.handler.MessageHandler#handleMessage(java.util.Map)
	 */
	public Message handleMessage(Message message) throws HandleException {
		String fileName = message.param(Message.PARAM_FILE_NAME);
		Long position = Long.valueOf(message.param(Message.PARAM_FILE_POSITION));			
		Message responseMessage = new Message();
		RandomAccessFile outputFile = null;
		FileChannel fileChannel = null;
		try {
			String srcFilePath = XserviceServerConfig.srcFileDirPath() + File.separator
					+ message.namespace() + File.separator + fileName;
			File srcFile = new File(srcFilePath);
			String tempFilePath = XserviceServerConfig.srcFileDirPath() + File.separator
					+ message.namespace() + File.separator + message.serviceId() + File.separator + fileName;
			File tempFile = new File(tempFilePath);
			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdirs();
			}
			outputFile = new RandomAccessFile(tempFile,"rw");
			fileChannel = outputFile.getChannel();
			fileChannel.position(position);
			int size = fileChannel.write(message.buffer());
			fileChannel.close();
			outputFile.close();
			responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_SUCCESS);
			responseMessage.param(Message.PARAM_FILE_POSITION, String.valueOf(position + size));
			if (position + size == srcFile.length()) {
				FileUtil.copy(tempFile, srcFile);
				FileUtil.delete(tempFile);
				if (tempFile.getParentFile().listFiles().length == 0) {
					FileUtil.delete(tempFile.getParentFile());
				}
			}
			return responseMessage;
		} catch (Exception e) {
			throw new HandleException("exception occors when handle message", e);
		} finally {
			if (null != fileChannel) {
				try {
					fileChannel.close();
				} catch (IOException e) {
				} finally  {
					fileChannel = null;
				}
			}
			if (null != outputFile) {
				try {
					outputFile.close();
				} catch (IOException e) {
				} finally  {
					outputFile = null;
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
			throw new HandleException("The namespace of a message could not be null during handling, message id: " + message.toString());
		}
		if (null == message.param(Message.PARAM_FILE_NAME)) {
			throw new HandleException("The file name of a message could not be null during handling send file message, message id: " + message.toString());
		}
		try {
			Long.valueOf(message.param(Message.PARAM_FILE_POSITION));			
		} catch(NumberFormatException e) {
			throw new HandleException("The file position of a message could not be null during handling send file message, message id: " + message.toString());
		}
		return true;
	}
}
