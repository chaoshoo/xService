/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月16日 下午2:06:24  
 */
package me.chaoshoo.xservice.handler;

import me.chaoshoo.xservice.core.communication.HandleException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.MessageHandler;

/**
 * @Title: UnkownMessageHandler.java 
 * @Package: me.chaoshoo.xservice.handler.impl 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月16日 下午2:06:24 
 * @version:  
 */
public class UnkownMessageHandler extends MessageHandler {

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.handler.MessageHandler#handleMessage(java.util.Map)
	 */
	public Message handleMessage(Message message) {
		Message responseMessage = new Message();
		responseMessage.param(Message.PARAM_HANDLE_RESULT, Message.HANDLE_RESULT_UNKNOW);
		return responseMessage;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.communication.MessageHandler#checkParam(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected Boolean checkParam(Message message) throws HandleException {
		return true;
	}

}
