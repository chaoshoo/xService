/**
 * Copyright:
 * All right reserved
 * Created on: 2015年11月2日 下午2:18:15  
 */
package me.chaoshoo.xservice;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.client.ClientCallbackServlet;
import me.chaoshoo.xservice.client.ClientManagerServlet;
import me.chaoshoo.xservice.client.callback.CallbackListener;
import me.chaoshoo.xservice.core.Server;

/**
 * @Title: App.java 
 * @Package: me.chaoshoo.xservice 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年11月2日 下午2:18:15 
 * @version:  
 */
public class App {

	private static final Logger log = LoggerFactory.getLogger(App.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread() {
			public void run() {
				Server server = null;
				try {
					server = new CallbackListener().start();
				} catch (Exception e) {
					log.error(e.getLocalizedMessage(), e);
				} finally {
					if (null != server) {
						server.stop();
					}
				}
			}
		}.start();
		
		new Thread() {
			public void run() {
				try {
					// 启动嵌入式jetty运行监控页面
					org.eclipse.jetty.server.Server jettyServer = new org.eclipse.jetty.server.Server(9197);
					ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
					context.setContextPath("/");
					jettyServer.setHandler(context);
					context.addServlet(new ServletHolder(new ClientManagerServlet()), "/client");
					context.addServlet(new ServletHolder(new ClientCallbackServlet()), "/callback");
					jettyServer.start();
					jettyServer.join();
				} catch (Exception e) {
					log.error(e.getLocalizedMessage(), e);
				}
			}
		}.start();

	}

}
