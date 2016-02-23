/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月26日 下午1:45:29  
 */
package me.chaoshoo.xservice.handler.master;

import me.chaoshoo.xservice.core.Slave;
import me.chaoshoo.xservice.core.communication.HandleException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;
import me.chaoshoo.xservice.socket.server.master.NioSocketMasterServer;
import me.chaoshoo.xservice.util.StringUtil;

/**
 * @Title: RequestServiceHandler.java 
 * @Package: me.chaoshoo.xservice.handler 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月26日 下午1:45:29 
 * @version:  
 */
public class RequestServiceMessageHandler extends MessageHandler {

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#checkParam(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected Boolean checkParam(Message message) throws HandleException {
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SERVICE_NAME))) {
			throw new HandleException(
					"The service name of a request service message could not be null, message id: "
							+ message.toString());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#handleMessage(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected Message handleMessage(Message message) throws HandleException {
		String serviceName = message.param(Message.PARAM_SERVICE_NAME);
		Slave slave = NioSocketMasterServer.master.avaliableSlave(serviceName);
		Message responseMessage = new Message();
		responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_SUCCESS);
		if (null != slave) {
			responseMessage.param(Message.PARAM_SLAVE_IP, slave.ip());
			responseMessage.param(Message.PARAM_SLAVE_PORT, slave.port().toString());			
		}
		return responseMessage;
	}

}
