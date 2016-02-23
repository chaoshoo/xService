/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月16日 下午2:16:29  
 */
package me.chaoshoo.xservice.core.service;

import java.io.File;

import me.chaoshoo.xservice.core.communication.Message;

/**
 * @Title: Servicable.java 
 * @Package: me.chaoshoo.xservice.core.service 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月16日 下午2:16:29 
 * @version:  
 */
public abstract class Service {
	
	protected String srcFileDirPath;
	
	protected String tempFileDirPath;
	
	protected String destFileDirPath;

	/**
	 * 对消息进行服务逻辑
	 * @param message
	 * @return
	 * @throws ServiceException
	 */
	protected abstract File service(Message message) throws ServiceException;
	
	/**
	 * 对需要服务的消息进行必要的前期检查
	 * @param message
	 * @return
	 * @throws ServiceException
	 */
	protected abstract Boolean check(Message message) throws ServiceException;
	
	/**
	 * 返回服务的名称
	 * @return
	 */
	public abstract String name();
	
	/**
	 * 返回服务的描述信息
	 * @return
	 */
	public abstract String description();
	
	/**
	 * 检查逻辑包装函数，子类可以对此函数进行重写扩展
	 * @param message
	 * @return
	 * @throws ServiceException
	 */
	protected Boolean doCheck(Message message) throws ServiceException {
		return check(message);
	}
	
	/**
	 * 服务逻辑包装函数，子类可以对此函数进行重写扩展
	 * @param message
	 * @return
	 * @throws ServiceException
	 */
	protected File doService(Message message) throws ServiceException {
		return service(message);
	}
	
	/**
	 * 服务对外调用，启动服务逻辑
	 * @param message
	 * @return
	 * @throws ServiceException
	 */
	public File executeService(Message message) throws ServiceException {
		if(!this.doCheck(message)){
			return null;
		}
		return this.doService(message);
	}
	
	public Service srcFileDirPath(String srcFileDirPath) {
		this.srcFileDirPath = srcFileDirPath;
		return this;
	}

	public Service tempFileDirPath(String tempFileDirPath) {
		this.tempFileDirPath = tempFileDirPath;
		return this;
	}

	public Service destFileDirPath(String destFileDirPath) {
		this.destFileDirPath = destFileDirPath;
		return this;
	}
	
}
