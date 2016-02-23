/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月30日 下午5:18:57  
 */
package me.chaoshoo.xservice.core.callback;

import me.chaoshoo.xservice.util.XserviceCoreConfig;

/**
 * @Title: CallbackFactory.java 
 * @Package: me.chaoshoo.xservice.core.callback 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月30日 下午5:18:57 
 * @version:  
 */
public interface CallbackFactory {

	public static CallbackFactory build() {
		try {
			Object instance = CallbackFactory.class.getClassLoader().loadClass(XserviceCoreConfig.callbackFactoryClassName()).newInstance();
			if (instance instanceof CallbackFactory) {
				return (CallbackFactory) instance;
			}
		} catch (Exception e) {
			
		}
		return new DefaultCallbackFactory();
	}
	
	Callback createCallback(String className);
	
}
