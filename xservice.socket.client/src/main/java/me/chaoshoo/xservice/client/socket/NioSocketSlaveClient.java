/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月27日 下午4:20:54  
 */
package me.chaoshoo.xservice.client.socket;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.SlaveClient;
import me.chaoshoo.xservice.core.communication.Command;
import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.service.trackable.ServiceTrackInfo;
import me.chaoshoo.xservice.util.StringUtil;
import me.chaoshoo.xservice.util.XserviceClientConfig;

/**
 * @Title: NioSocketSlaveClient.java
 * @Package: me.chaoshoo.xservice.socket.client
 * @Description: slave客户端socket模式下实现
 * @author: Hu Chao
 * @date: 2015年10月27日 下午4:20:54
 * @version:
 */
public class NioSocketSlaveClient extends NioSocketClient implements SlaveClient {

	private static final Logger log = LoggerFactory.getLogger(NioSocketSlaveClient.class);

	/**
	 * @param ip
	 * @param port
	 */
	public NioSocketSlaveClient(String ip, Integer port) {
		super(ip, port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.core.SlaveClient#serviceStatus(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ServiceTrackInfo serviceStatus(String namespace, String serviceId) throws CommunicationException {
		Message message = new Message().command(Command.SERVICE_STATUS).namespace(namespace).serviceId(serviceId);
		Message receiveMessage = syncSendMessage(message);
		if (receiveMessage.command().equals(Command.RESPONSE)
				&& receiveMessage.param(Message.PARAM_HANDLE_RESULT).equals(Message.HANDLE_RESULT_SUCCESS)) {
			String serviceName = receiveMessage.param(Message.PARAM_SERVICE_NAME);
			if (null == serviceName) {
				return null;
			}
			String serviceMessage = receiveMessage.param(Message.PARAM_SERVICE_MESSAGE);
			Integer servicePercent = StringUtil.parseInteger(receiveMessage.param(Message.PARAM_SERVICE_PERCENT));
			String serviceDescription = receiveMessage.param(Message.PARAM_SERVICE_DESCRIPTION);
			return new ServiceTrackInfo(namespace, serviceId, serviceName, serviceDescription).apendMessage(
					serviceMessage).percent(servicePercent);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.core.SlaveClient#syncCallService(java.lang.String,
	 * java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public Boolean syncCallService(String namespace, String id, String serviceName, Map<String, String> params)
			throws CommunicationException {
		Message message = new Message(namespace, Command.CALL_SERVICE).serviceId(id)
				.param(Message.PARAM_SERVICE_NAME, serviceName).params(params);
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
	 * @see me.chaoshoo.xservice.core.SlaveClient#prepareFile(java.lang.String,
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
	 * @see me.chaoshoo.xservice.core.SlaveClient#sendFile(java.lang.String,
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
				message.buffer().clear();
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.core.SlaveClient#asyncCallServiceForHttpCallback
	 * (java.lang.String, java.lang.String, java.lang.String, java.util.Map,
	 * java.lang.String)
	 */
	@Override
	public void asyncCallService(String namespace, String serviceId, String serviceName, Map<String, String> params) throws CommunicationException {
		Message message = new Message(namespace, Command.CALL_SERVICE).serviceId(serviceId)
				.param(Message.PARAM_SERVICE_NAME, serviceName).params(params);
		if (Message.CALLBACK_TYPE_MESSAGE.equals(XserviceClientConfig.callbackType())) {
			String callbackIp = XserviceClientConfig.callbackIp();
			if (StringUtil.isNullOrEmpty(callbackIp)) {
				throw new CommunicationException(
						"Callback ip could not be null or empty for aync call with socket meesage callback!");
			}
			String callbackPort = XserviceClientConfig.callbackPort().toString();
			if (StringUtil.isNullOrEmpty(callbackIp)) {
				throw new CommunicationException(
						"Callback port could not be null or empty for aync call with socket meesage callback!");
			}
			message.param(Message.PARAM_CALLBACK_TYPE, Message.CALLBACK_TYPE_MESSAGE)
					.param(Message.CALLBACK_SOCKET_IP, callbackIp).param(Message.CALLBACK_SOCKET_PORT, callbackPort);
		} else {
			String callbackUrl = XserviceClientConfig.callbackUrl();
			if (StringUtil.isNullOrEmpty(callbackUrl)) {
				throw new CommunicationException(
						"Callback url could not be null or empty for aync call with socket meesage callback!");
			}
			message.param(Message.PARAM_CALLBACK_TYPE, Message.CALLBACK_TYPE_HTTP).param(Message.CALLBACK_HTTP_URL,
					callbackUrl);
		}
		asyncSendMessage(message);
	}

}
