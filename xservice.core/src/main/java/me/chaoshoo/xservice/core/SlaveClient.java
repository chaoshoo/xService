/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月27日 下午4:03:41  
 */
package me.chaoshoo.xservice.core;

import java.io.File;
import java.util.Map;

import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.service.trackable.ServiceTrackInfo;

/**
 * @Title: SlaveClient.java
 * @Package: me.chaoshoo.xservice.core
 * @Description: slave服务器客户端接口定义
 * @author: Hu Chao
 * @date: 2015年10月27日 下午4:03:41
 * @version:
 */
public interface SlaveClient {

	/**
	 * 获取运行中的服务状态数据对象
	 * 
	 * @param namespace
	 * @param serviceId
	 * @return 服务正在运行时返回对应的数据对象，如果服务没有开始运行或者完成，返回null
	 * @throws CommunicationException
	 */
	ServiceTrackInfo serviceStatus(String namespace, String serviceId) throws CommunicationException;

	/**
	 * 异步调用服务，不阻塞，有服务器自行调用http回调方法
	 * 
	 * @param namespace
	 * @param id
	 * @param serviceName
	 * @param params
	 * @param callbackUrl
	 * @throws CommunicationException
	 */
	void asyncCallService(String namespace, String serviceId, String serviceName, Map<String, String> params)
			throws CommunicationException;

	/**
	 * 同步调用服务，阻塞，等待服务器返回信息
	 * 
	 * @param namespace
	 * @param id
	 * @param serviceName
	 * @param params
	 * @return 服务器响应message
	 * @throws CommunicationException
	 */
	Boolean syncCallService(String namespace, String serviceId, String serviceName, Map<String, String> params)
			throws CommunicationException;

	/**
	 * 向服务器做准备发送文件的请求，在服务器上开辟文件大小的一块空间等待写入内容
	 * 
	 * @param namespace
	 * @param file
	 * @return 服务器正确完成操作返回true，否则返回false
	 * @throws CommunicationException
	 */
	Boolean prepareFile(String namespace, File file) throws CommunicationException;

	/**
	 * 向服务发送文件
	 * 
	 * @param namespace
	 * @param file
	 * @return 发送成功返回true，否则返回false
	 * @throws CommunicationException
	 */
	Boolean sendFile(String namespace, File file) throws CommunicationException;
}
