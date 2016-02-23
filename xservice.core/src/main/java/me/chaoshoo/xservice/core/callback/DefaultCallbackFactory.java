/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月30日 下午5:20:01  
 */
package me.chaoshoo.xservice.core.callback;

/**
 * @Title: DefaultCallbackFactory.java 
 * @Package: me.chaoshoo.xservice.core.callback 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月30日 下午5:20:01 
 * @version:  
 */
public class DefaultCallbackFactory implements CallbackFactory{

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.callback.CallbackFactory#createCallback(java.lang.String)
	 */
	@Override
	public Callback createCallback(String className) {
		// TODO Auto-generated method stub
		try {
			Class<?> clazz = DefaultCallbackFactory.class.getClassLoader().loadClass(className);
			Object instance = clazz.newInstance();
			if (instance instanceof Callback) {
				return (Callback)instance;
			}			
		} catch (Exception e) {
			return null;
		} 
		return null;
	}

}
