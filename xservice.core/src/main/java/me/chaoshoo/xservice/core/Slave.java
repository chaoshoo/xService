/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月26日 下午2:39:19  
 */
package me.chaoshoo.xservice.core;

import java.util.ArrayList;
import java.util.List;

import me.chaoshoo.xservice.core.service.ServiceInfo;

/**
 * @Title: Slave.java 
 * @Package: me.chaoshoo.xservice.core 
 * @Description: slave服务器数据对象
 * @author: Hu Chao
 * @date: 2015年10月26日 下午2:39:19 
 * @version:  
 */
public class Slave {

	private String ip;
	
	private Integer port;
	
	List<ServiceInfo> serviceList;
	
	private Long updateTime;
	
	/**
	 * @return the updateTime
	 */
	public Long updateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public Slave updateTime(Long updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	/**
	 * @return the serviceAlive
	 */
	public Integer serviceAlive() {
		return serviceAlive;
	}

	/**
	 * @param serviceAlive the serviceAlive to set
	 */
	public Slave serviceAlive(Integer serviceAlive) {
		this.serviceAlive = serviceAlive;
		return this;
	}
	
	private Integer serviceAlive;

	/**
	 * @param ip
	 * @param port
	 */
	public Slave(String ip, Integer port) {
		super();
		this.ip = ip;
		this.port = port;
		this.serviceList = new ArrayList<ServiceInfo>();
	}

	/**
	 * @return the ip
	 */
	public String ip() {
		return ip;
	}

	/**
	 * @return the port
	 */
	public Integer port() {
		return port;
	}

	/**
	 * @return the serviceList
	 */
	public List<ServiceInfo> serviceList() {
		return serviceList;
	}

	/**
	 * @param serviceNameList the serviceNameList to set
	 */
	public Slave setServiceList(List<ServiceInfo> serviceList) {
		this.serviceList = serviceList;
		return this;
	}

	public Boolean avaliableForService(String serviceName) {
		for (ServiceInfo service : serviceList) {
			if (serviceName.equals(service.getName())) {
				return true;
			}
		}
		return false;
	}
	
}
