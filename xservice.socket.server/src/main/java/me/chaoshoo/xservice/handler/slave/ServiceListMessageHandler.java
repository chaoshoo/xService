/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月21日 下午3:15:15  
 */
package me.chaoshoo.xservice.handler.slave;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import me.chaoshoo.xservice.core.communication.HandleException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.core.service.ServiceFactory;
import me.chaoshoo.xservice.core.service.ServiceInfo;

/**
 * @Title: ServiceListMessageHandler.java 
 * @Package: me.chaoshoo.xservice.handler 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月21日 下午3:15:15 
 * @version:  
 */
public class ServiceListMessageHandler extends MessageHandler {

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#handleMessage(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	public Message handleMessage(Message message) throws HandleException {
		Message responseMessage = new Message();
		List<ServiceInfo> availableServices = ServiceFactory.availableServices();
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(availableServices);
			byte[] bytes = baos.toByteArray();
			String services = new String(bytes);
			responseMessage.param(Message.PARAM_SERVICE_LIST, services);
			responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_SUCCESS);
			return responseMessage;
		} catch (IOException ex) {
			throw new HandleException(ex.getMessage(), ex);
		} finally {
			try {
				oos.close();
				baos.close();
			} catch (Exception e) {
			} finally {
				baos = null;
				oos = null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#checkParam(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected Boolean checkParam(Message message) throws HandleException {
		return true;
	}

}
