/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月27日 上午10:05:29  
 */
package me.chaoshoo.xservice.core.service.trackable;

/**
 * @Title: ServiceTrackListener.java 
 * @Package: me.chaoshoo.xservice.core.service 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月27日 上午10:05:29 
 * @version:  
 */
public interface ServiceTrackListener {
	
	/**
	 * 更新服务进度信息
	 * @param namespace
	 * @param serviceId
	 * @param serviceName
	 * @param updateMessage
	 * @param percent
	 */
	void onUpdate(String namespace, String serviceId, String serviceName, String updateMessage, Integer percent);
	
}
