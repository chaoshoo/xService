/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月26日 上午10:43:09  
 */
package me.chaoshoo.xservice.socket.server;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.Server;

/**
 * @Title: NioSocketServer.java 
 * @Package: me.chaoshoo.xservice.socket.server 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月26日 上午10:43:09 
 * @version:  
 */
public abstract class NioSocketServer implements Server{
	
	private static final Logger log = LoggerFactory.getLogger(NioSocketServer.class);
	
	protected Selector selector = null;  
	protected ServerSocketChannel serverSocketChannel = null;  
    protected NioServerHandler handler = null; 

	public NioSocketServer() throws IOException {  
        super();  
        this.handler = new NioServerHandler(Executors.newFixedThreadPool(10)).isMaster(isMasterMode());    
    } 
    

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.Server#stop()
	 */
	@Override
	public Server stop() {
		try {
    		if (null != selector) {
				selector.close();
			}
    		if (null != serverSocketChannel) {
				serverSocketChannel.close();
			}
    	} catch (Exception e){
    		log.error(e.getMessage(), e);
    	} finally {
    		selector = null;
    		serverSocketChannel = null;
    	}
		return this;
	}

	protected abstract Boolean isMasterMode(); 
    
}
