/**
 * Copyright:
 * All right reserved
 * Created on: 2015年7月16日 下午3:15:21  
 */
package me.chaoshoo.xservice.socket.server.slave;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.Server;
import me.chaoshoo.xservice.core.service.ServiceFactory;
import me.chaoshoo.xservice.socket.server.NioSocketServer;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: SocketFileServer.java 
 * @Package: me.chaoshoo.storage.server.socket 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年7月16日 下午3:15:21 
 * @version:  
 */
public class NioSocketSlaveServer extends NioSocketServer {
	
	private static final Logger log = LoggerFactory.getLogger(NioSocketSlaveServer.class);

	public NioSocketSlaveServer() throws IOException {  
        super();  
        ServiceFactory.init();
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
        Integer listenPort = XserviceServerConfig.slaveServerPort();
        log.info("slave server start at port: {}", listenPort.toString());
        serverSocketChannel.socket().bind(new InetSocketAddress(listenPort));  
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); 
        // 阻塞获取socket 
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
		super.stop();
		return this;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.socket.server.NioSocketServer#isMasterMode()
	 */
	@Override
	protected Boolean isMasterMode() {
		return false;
	}
	
}
