/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月21日 下午2:53:28  
 */
package me.chaoshoo.xservice.handler.slave;

import me.chaoshoo.xservice.core.communication.HandleException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.core.service.ServiceFactory;
import me.chaoshoo.xservice.core.service.trackable.ServiceTrackInfo;
import me.chaoshoo.xservice.util.StringUtil;

/**
 * @Title: ServiceStatusMessageHandler.java 
 * @Package: me.chaoshoo.xservice.handler 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月21日 下午2:53:28 
 * @version:  
 */
public class ServiceStatusMessageHandler extends MessageHandler{

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#handleMessage(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	public Message handleMessage(Message message) throws HandleException {
		Message responseMessage = new Message();
		responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_SUCCESS);
		ServiceTrackInfo trackInfo = ServiceFactory.trackInfo(message.namespace(), message.serviceId());
		if (null != trackInfo) {
			responseMessage.param(Message.PARAM_SERVICE_NAME, trackInfo.serviceName());
			responseMessage.param(Message.PARAM_SERVICE_PERCENT, trackInfo.percent().toString());
			responseMessage.param(Message.PARAM_SERVICE_MESSAGE, trackInfo.message());
		}
		return responseMessage;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#checkParam(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected Boolean checkParam(Message message) throws HandleException {
		if (StringUtil.isNullOrEmpty(message.namespace())) {
			throw new HandleException("The namespace of a service status message could not be null or empty!");
		}
		if (StringUtil.isNullOrEmpty(message.serviceId())) {
			throw new HandleException("The service id of a service status message could not be null or empty!");
		}
		return true;
	}
}
