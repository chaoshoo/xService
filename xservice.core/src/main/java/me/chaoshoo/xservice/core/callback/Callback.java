/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月30日 下午2:44:34  
 */
package me.chaoshoo.xservice.core.callback;

import java.io.File;

import me.chaoshoo.xservice.core.communication.Message;

/**
 * @Title: Callback.java 
 * @Package: me.chaoshoo.xservice.core 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月30日 下午2:44:34 
 * @version:  
 */
public interface Callback {

	Boolean callback(String namespace, String serviceId, File file, Message message) throws CallbackException;
	
}
