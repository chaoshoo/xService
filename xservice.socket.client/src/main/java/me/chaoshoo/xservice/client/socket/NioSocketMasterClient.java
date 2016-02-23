/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月27日 下午4:06:40  
 */
package me.chaoshoo.xservice.client.socket;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.MasterClient;
import me.chaoshoo.xservice.core.Slave;
import me.chaoshoo.xservice.core.SlaveClient;
import me.chaoshoo.xservice.core.communication.Command;
import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.util.FileUtil;
import me.chaoshoo.xservice.util.StringUtil;
import me.chaoshoo.xservice.util.XserviceClientConfig;

/**
 * @Title: NioSocketMasterClient.java
 * @Package: me.chaoshoo.xservice.socket.client
 * @Description: master客户端socket模式下实现
 * @author: Hu Chao
 * @date: 2015年10月27日 下午4:06:40
 * @version:
 */
public class NioSocketMasterClient extends NioSocketClient implements MasterClient {

	private static final Logger log = LoggerFactory.getLogger(NioSocketMasterClient.class);

	/**
	 * @param ip
	 * @param port
	 */
	public NioSocketMasterClient() {
		super(XserviceClientConfig.masterServerIp(), XserviceClientConfig.masterServerPort());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.core.MasterClient#requestService(java.lang.String)
	 */
	@Override
	public SlaveClient requestService(String serviceName) throws CommunicationException {
		Message message = new Message().command(Command.REQUEST_SERVICE);
		message.param(Message.PARAM_SERVICE_NAME, serviceName);
		Message receiveMessage = syncSendMessage(message);
		if (receiveMessage.command().equals(Command.RESPONSE)
				&& receiveMessage.param(Message.PARAM_HANDLE_RESULT).equals(Message.HANDLE_RESULT_SUCCESS)) {
			String slaveIp = receiveMessage.param(Message.PARAM_SLAVE_IP);
			Integer slavePort = StringUtil.parseInteger(receiveMessage.param(Message.PARAM_SLAVE_PORT));
			if (null == slaveIp || null == slavePort) {
				return null;
			}
			return new NioSocketSlaveClient(slaveIp, slavePort);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.core.MasterClient#callService(java.lang.String,
	 * java.lang.String, java.lang.String, java.io.File, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Slave callService(String namespace, String serviceId, String serviceName, File file, String outputFileName,
			String param, String callbackClassName) throws CommunicationException {
		SlaveClient slave = requestService(serviceName);
		slave.sendFile(namespace, file);
		Map<String, String> params = new HashMap<String, String>();
		params.put(Message.PARAM_SERVICE_SRC_FILE_NAME, file.getName());
		params.put(Message.PARAM_SERVICE_DEST_FILE_NAME, outputFileName);
		params.put(Message.PARAM_CMD_COMMAND_PARAM, param);
		params.put(Message.PARAM_CALLBACK_CLASS_NAME, callbackClassName);
		slave.asyncCallService(namespace, serviceId, serviceName, params);
		NioSocketClient nsc = (NioSocketClient)slave;
		return new Slave(nsc.ip(), nsc.port());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.chaoshoo.xservice.core.MasterClient#sendFile(java.lang.String,
	 * java.io.File)
	 */
	@Override
	public Boolean sendFile(String namespace, File file) throws CommunicationException {
		if (!prepareFile(namespace, file)) {
			return false;
		}
		Long position = 0L;
		Message message = new Message(namespace, Command.SEND_FILE);
		message.file(file).filePosition(position).serviceId(String.valueOf(System.currentTimeMillis()));
		while (position < file.length()) {
			Message receiveMessage = syncSendMessage(message);
			if (receiveMessage.command().equals(Command.RESPONSE)
					&& receiveMessage.param(Message.PARAM_HANDLE_RESULT).equals(Message.HANDLE_RESULT_SUCCESS)) {
				Long newPosition = Long.parseLong(receiveMessage.param(Message.PARAM_FILE_POSITION));
				if (null == newPosition) {
					log.error("Exception happen when send file: {}, position: " + position, file.getAbsolutePath());
					throw new CommunicationException("The new position to send file in message could not be null");
				}
				position = newPosition;
				message.filePosition(position);
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.core.MasterClient#prepareFile(java.lang.String,
	 * java.io.File)
	 */
	@Override
	public Boolean prepareFile(String namespace, File file) throws CommunicationException {
		Message message = new Message(namespace, Command.PREPARE_FILE);
		message.file(file);
		Message receiveMessage = syncSendMessage(message);
		if (receiveMessage.command().equals(Command.RESPONSE)
				&& receiveMessage.param(Message.PARAM_HANDLE_RESULT).equals(Message.HANDLE_RESULT_SUCCESS)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.chaoshoo.xservice.core.MasterClient#getFile(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public File getFile(String namespace, String serviceId, String fileName) throws CommunicationException {
		// TODO Auto-generated method stub
		String desFilePath = XserviceClientConfig.destFileDir() + File.separator + namespace + File.separator
				+ serviceId + File.separator + fileName;
		File desFile = new File(desFilePath);
		if (!desFile.exists()) {
			desFile.getParentFile().mkdirs();
		} else {
			FileUtil.delete(desFile);
		}
		RandomAccessFile outputFile = null;
		FileChannel fileChannel = null;
		Message message = new Message(namespace, Command.GET_FILE);
		Long position = 0L;
		Long fileSize = 1L;
		message.serviceId(serviceId).param(Message.PARAM_FILE_NAME, fileName)
				.param(Message.PARAM_FILE_POSITION, position.toString());
		try{
			outputFile = new RandomAccessFile(desFile, "rw");
			while(position < fileSize) {
				Message receiveMessage = syncSendMessage(message);
				if (receiveMessage.command().equals(Command.SEND_FILE)
						&& receiveMessage.param(Message.PARAM_HANDLE_RESULT).equals(Message.HANDLE_RESULT_SUCCESS)) {
					fileSize = Long.valueOf(receiveMessage.param(Message.PARAM_FILE_SIZE));
					outputFile.setLength(fileSize);
					fileChannel = outputFile.getChannel();
					fileChannel.position(position);
					int size = fileChannel.write(receiveMessage.buffer());
					position += size;
					message.param(Message.PARAM_FILE_POSITION, position.toString());
					message.buffer().clear();
				}
			}
		} catch (Exception e) {
			throw new CommunicationException("exception occors when get file: ", e);
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
		return desFile;
	}
}
