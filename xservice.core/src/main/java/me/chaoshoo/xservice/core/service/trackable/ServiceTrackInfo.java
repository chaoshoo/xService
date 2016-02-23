/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月27日 上午10:50:07  
 */
package me.chaoshoo.xservice.core.service.trackable;

/**
 * @Title: ServiceTrackInfo.java 
 * @Package: me.chaoshoo.xservice.core.service.trackable 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月27日 上午10:50:07 
 * @version:  
 */
public class ServiceTrackInfo {
	
	private String namespace;

	private String serviceId;
	
	private String serviceName;
	
	private String description;

	private Integer percent = 0;

	private StringBuffer message;
	
	/**
	 * @param namespace
	 * @param serviceId
	 */
	public ServiceTrackInfo(String namespace, String serviceId, String serviceName, String description) {
		super();
		this.namespace = namespace;
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.description = description;
		message = new StringBuffer();
	}

	public String message() {
		return this.message.toString();
	}

	public ServiceTrackInfo apendMessage(String message) {
		this.message.append(message);
		return this;
	}
	
	public ServiceTrackInfo percent(Integer percent) {
		if (null == percent) {
			return this;
		}
		this.percent = percent;
		return this;
	}
	
	public Integer percent() {
		return this.percent;
	}
	
	public String namespace() {
		return this.namespace;
	}
	
	public String serviceId() {
		return this.serviceId;
	}
	
	public String serviceName() {
		return this.serviceName;
	}
	
	public String description() {
		return this.description;
	}
	
	public String key() {
		return this.namespace + ":" + this.serviceId;
	}
	
	public static String createKey(String namespace, String serviceId) {
		return namespace + ":" + serviceId;
	}
}
