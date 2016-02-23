/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月26日 上午10:37:33  
 */
package me.chaoshoo.xservice.core;

/**
 * @Title: Server.java 
 * @Package: me.chaoshoo.xservice.core 
 * @Description: 服务器接口定义
 * @author: Hu Chao
 * @date: 2015年10月26日 上午10:37:33 
 * @version:  
 */
public interface Server {

	Server start() throws Exception ;
	
	Server stop();
	
}
