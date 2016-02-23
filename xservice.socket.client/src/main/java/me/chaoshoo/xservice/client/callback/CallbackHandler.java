/**
 * Copyright:
 * All right reserved
 * Created on: 2015年7月16日 下午3:18:19  
 */
package me.chaoshoo.xservice.client.callback;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;  
import java.nio.channels.SocketChannel;  
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title: NioserverHandler.java 
 * @Package: me.chaoshoo.storage.server.socket 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年7月16日 下午3:18:19 
 * @version:  
 */ 
public class CallbackHandler {  
      
	private static final Logger log = LoggerFactory.getLogger(CallbackHandler.class);
	
    private ExecutorService executorService;
    
    public CallbackHandler(ExecutorService executorService) {
    	super();
    	this.executorService = executorService;
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
			CallbackThread thread = null;
			thread = new CallbackThread(socketChannel);				
			if (null != executorService) {
				executorService.execute(thread);
			} else {
				new Thread(thread).start();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}  
    
}  
