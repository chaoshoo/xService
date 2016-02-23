/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月26日 上午10:36:46  
 */
package me.chaoshoo.xservice.socket.server.master;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.Master;
import me.chaoshoo.xservice.core.Server;
import me.chaoshoo.xservice.socket.server.NioSocketServer;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: NioSocketMasterServer.java 
 * @Package: me.chaoshoo.xservice.socket.server 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月26日 上午10:36:46 
 * @version:  
 */
public class NioSocketMasterServer extends NioSocketServer {
	
	private static final Logger log = LoggerFactory.getLogger(NioSocketMasterServer.class);
	
	public static Master master = null;

	public NioSocketMasterServer() throws IOException {  
        super();  
    } 
    
	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.Server#start()
	 */
	@Override
	public Server start() throws Exception {
		master = new Master("127.0.0.1", XserviceServerConfig.masterServerPort()); 
		selector  = Selector.open();  
        // 打开服务器套接字通道  
        serverSocketChannel = ServerSocketChannel.open();  
          
        // 调整通道的阻塞模式非阻塞  
        serverSocketChannel.configureBlocking(false);  
        serverSocketChannel.socket().setReuseAddress(true); 
        Integer listenPort = XserviceServerConfig.masterServerPort();
        log.info("master server start at port: {}", listenPort.toString());
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
		super.stop();
		master = null;
		return this;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.socket.server.NioSocketServer#isMasterMode()
	 */
	@Override
	protected Boolean isMasterMode() {
		return true;
	}
	
}