/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月30日 下午4:10:21  
 */
package me.chaoshoo.xservice.client.callback;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.Server;
import me.chaoshoo.xservice.util.XserviceClientConfig;

/**
 * @Title: CallbackListener.java 
 * @Package: me.chaoshoo.xservice.socket.callback 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月30日 下午4:10:21 
 * @version:  
 */
public class CallbackListener implements Server{

	private static final Logger log = LoggerFactory.getLogger(CallbackListener.class);
	
	protected Selector selector = null;  
	protected ServerSocketChannel serverSocketChannel = null;  
	protected CallbackHandler handler = null;
	
	/**
	 * 
	 */
	public CallbackListener() {
		super();
        this.handler = new CallbackHandler(Executors.newFixedThreadPool(10));  
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.Server#start()
	 */
	@Override
	public Server start() throws Exception {
		selector  = Selector.open();  
        // 打开服务器套接字通道  
        serverSocketChannel = ServerSocketChannel.open();  
          
        // 调整通道的阻塞模式非阻塞  
        serverSocketChannel.configureBlocking(false);  
        serverSocketChannel.socket().setReuseAddress(true); 
        Integer listenPort = XserviceClientConfig.callbackPort();
        log.info("callback listener start at port: {}", listenPort.toString());
        serverSocketChannel.socket().bind(new InetSocketAddress(listenPort));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);  
        while(selector.select() > 0) {  
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();  
            while(it.hasNext()) {  
                SelectionKey key = it.next();  
                it.remove();  
                this.handler.excute(key);  
            }  
        }  
        return this;
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
}
