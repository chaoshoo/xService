/**
 * Copyright:
 * All right reserved
 * Created on: 2015年7月16日 下午3:18:19  
 */
package me.chaoshoo.xservice.socket.server;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;  
import java.nio.channels.SocketChannel;  
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.socket.server.master.SocketMasterServerThread;
import me.chaoshoo.xservice.socket.server.slave.SocketSlaveServerThread;

/**
 * @Title: NioserverHandler.java 
 * @Package: me.chaoshoo.storage.server.socket 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年7月16日 下午3:18:19 
 * @version:  
 */ 
public class NioServerHandler {  
      
	private static final Logger log = LoggerFactory.getLogger(NioServerHandler.class);
	
	private Boolean isMaster = false;
    
    private ExecutorService executorService;
    
    public NioServerHandler(ExecutorService executorService) {
    	super();
    	this.executorService = executorService;
    }
    
    public NioServerHandler isMaster(Boolean isMaster) {
    	this.isMaster = isMaster;
    	return this;
    }

	/** 
     * 处理socket链接，封装成对象直接从socket channel读取和写入 
     * @param serverSocketChannel 
     */  
	public void excute(SelectionKey key) {
		try {
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
			SocketChannel socketChannel = serverSocketChannel.accept(); // 等待客户端连接  
			log.info("Accept connetion from {}", socketChannel.socket().getInetAddress().getHostAddress());
			SocketServerThread serverThread = null;
			if (isMaster) {
				serverThread = new SocketMasterServerThread(socketChannel);
			} else {
				serverThread = new SocketSlaveServerThread(socketChannel);				
			}
			if (null != executorService) {
				executorService.execute(serverThread);
			} else {
				new Thread(serverThread).start();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}  
    
}  
