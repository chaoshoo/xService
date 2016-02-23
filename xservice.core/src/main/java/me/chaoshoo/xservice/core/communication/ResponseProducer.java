/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月19日 下午2:17:58  
 */
package me.chaoshoo.xservice.core.communication;

/**
 * @Title: CallbackProducer.java 
 * @Package: me.chaoshoo.xservice.core.communication 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月19日 下午2:17:58 
 * @version:  
 */
public interface ResponseProducer {
	
	/**
	 * 使用返回的参数键值对进行需要的响应 
	 * @param params
	 */
	void response(Message responseMessage);
}
