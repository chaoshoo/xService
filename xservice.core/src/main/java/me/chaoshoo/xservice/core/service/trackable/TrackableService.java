/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月27日 上午9:48:39  
 */
package me.chaoshoo.xservice.core.service.trackable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.chaoshoo.xservice.core.service.Service;
import me.chaoshoo.xservice.core.service.ServiceException;

/**
 * @Title: TrackableService.java
 * @Package: me.chaoshoo.xservice.core.service
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月27日 上午9:48:39
 * @version:
 */
public abstract class TrackableService extends Service {

	private List<ServiceTrackListener> listeners = null;

	public TrackableService() {
		super();
		if (null == listeners) {
			listeners = new ArrayList<ServiceTrackListener>();
		}
		listeners.add(new DefaultTrackListerner());
	}

	public void addListener(ServiceTrackListener listener) {
		listeners.add(listener);
	}

	public void update(String namespace, String serviceId, String serviceName, String updateMessage) throws ServiceException {
		for (Iterator<ServiceTrackListener> iterator = listeners.iterator(); iterator.hasNext();) {
			ServiceTrackListener serviceTrackListener = (ServiceTrackListener) iterator.next();
			serviceTrackListener.onUpdate(namespace, serviceId, serviceName, updateMessage, percentFromMessage(updateMessage));
		}
	}
	
	public abstract Integer percentFromMessage (String message) throws ServiceException;
	
}
