/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月27日 下午4:01:16  
 */
package me.chaoshoo.xservice.core;

import java.io.File;

import me.chaoshoo.xservice.core.communication.CommunicationException;

/**
 * @Title: MasterClient.java
 * @Package: me.chaoshoo.xservice.core
 * @Description: master服务器客户端接口定义
 * @author: Hu Chao
 * @date: 2015年10月27日 下午4:01:16
 * @version:
 */
public interface MasterClient {

	/**
	 * 向服务器请求可用的服务slave
	 * 
	 * @param serviceName
	 * @return 可用的slave信息，如果没有可用的则返回null
	 * @throws CommunicationException
	 */
	SlaveClient requestService(String serviceName) throws CommunicationException;

	/**
	 * 向服务发送文件
	 * 
	 * @param namespace
	 * @param file
	 * @return 发送成功返回true，否则返回false
	 * @throws CommunicationException
	 */
	Boolean sendFile(String namespace, File file) throws CommunicationException;

	Boolean prepareFile(String namespace, File file) throws CommunicationException;

	Slave callService(String namespace, String serviceId, String serviceName, File file, String outputFileName,
			String param, String callbackClassName) throws CommunicationException;
	
	File getFile(String namespace, String serviceId, String fileName) throws CommunicationException;

}
