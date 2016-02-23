/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月27日 上午10:24:17  
 */
package me.chaoshoo.xservice.core.service.trackable;

import me.chaoshoo.xservice.core.service.ServiceFactory;

/**
 * @Title: DefaultTrackListerner.java 
 * @Package: me.chaoshoo.xservice.core.service.trackable 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月27日 上午10:24:17 
 * @version:  
 */
public class DefaultTrackListerner implements ServiceTrackListener{

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.service.trackable.ServiceTrackListener#onUpdate(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onUpdate(String namespace, String serviceId, String serviceName, String updateMessage, Integer percent) {
		ServiceTrackInfo trackInfo = ServiceFactory.trackInfo(namespace, serviceId);
		if (null == trackInfo) {
			trackInfo = new ServiceTrackInfo(namespace, serviceId, serviceName, ServiceFactory.serviceInfo(serviceName).getDescription());
		}
		trackInfo.apendMessage(updateMessage);
		if (null != percent) {
			trackInfo.percent(percent);
		}
		ServiceFactory.trackInfo(trackInfo);
	}

}
