/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月16日 下午2:38:33  
 */
package me.chaoshoo.xservice.core.service;

import java.io.Serializable;

/**
 * @Title: ServiceInfo.java 
 * @Package: me.chaoshoo.xservice.core.service 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月16日 下午2:38:33 
 * @version:  
 */
public class ServiceInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7222362000520331056L;

	private String name;
	
	private String className;
	
	private String description;
	
	private Integer runningCount;

	/**
	 * @param name
	 * @param jarPath
	 * @param className
	 */
	public ServiceInfo(String name, String className,String description, Integer runningCount) {
		super();
		this.name = name;
		this.className = className;
		this.description = null == description ? "" : description;
		this.runningCount = 0;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	};
	
	/**
	 * @return the runningCount
	 */
	public Integer runningCount () {
		return runningCount;
	}
	
}
