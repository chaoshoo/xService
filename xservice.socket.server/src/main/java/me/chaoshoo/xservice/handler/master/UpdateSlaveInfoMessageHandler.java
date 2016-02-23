/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月26日 下午2:26:13  
 */
package me.chaoshoo.xservice.handler.master;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.chaoshoo.xservice.core.Slave;
import me.chaoshoo.xservice.core.communication.HandleException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.core.service.ServiceInfo;
import me.chaoshoo.xservice.socket.server.master.NioSocketMasterServer;
import me.chaoshoo.xservice.util.StringUtil;

/**
 * @Title: UpdateSlaveInfoMessageHandler.java 
 * @Package: me.chaoshoo.xservice.handler 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月26日 下午2:26:13 
 * @version:  
 */
public class UpdateSlaveInfoMessageHandler extends MessageHandler {

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#checkParam(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected Boolean checkParam(Message message) throws HandleException {
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SLAVE_IP))) {
			throw new HandleException(
					"The salve ip of a update salve message could not be null, message id: "
							+ message.toString());
		}
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SLAVE_PORT))
				|| null == StringUtil.parseInteger(message.param(Message.PARAM_SLAVE_PORT))) {
			throw new HandleException(
					"The salve port of a update salve message could not be null, message id: "
							+ message.toString());
		}
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SERVICE_LIST))) {
			throw new HandleException(
					"The service list of a update salve message could not be null, message id: "
							+ message.toString());
		}
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SERVICE_ALIVE))
				|| null == StringUtil.parseInteger(message.param(Message.PARAM_SERVICE_ALIVE))) {
			throw new HandleException(
					"The service alive count of a update salve message could not be null, message id: "
							+ message.toString());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#handleMessage(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected Message handleMessage(Message message) throws HandleException {
		Slave slave = new Slave(message.param(Message.PARAM_SLAVE_IP), StringUtil.parseInteger(message
				.param(Message.PARAM_SLAVE_PORT))).updateTime(System.currentTimeMillis());
		slave.serviceAlive(StringUtil.parseInteger(message.param(Message.PARAM_SERVICE_ALIVE)));
		String services = message.param(Message.PARAM_SERVICE_LIST);
		Gson gson = new Gson();
		List<ServiceInfo> serviceInfoList = gson.fromJson(services, new TypeToken<List<ServiceInfo>>(){}.getType()); 
        slave.setServiceList(serviceInfoList);
		NioSocketMasterServer.master.slave(slave);
		Message responseMessage = new Message();
		responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_UNKNOW);
		return responseMessage;
	}

}
