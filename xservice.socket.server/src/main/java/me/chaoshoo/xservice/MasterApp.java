package me.chaoshoo.xservice;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.Server;
import me.chaoshoo.xservice.socket.server.master.MasterDaemonThread;
import me.chaoshoo.xservice.socket.server.master.MasterManagerServlet;
import me.chaoshoo.xservice.socket.server.master.NioSocketMasterServer;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: MasterApp.java
 * @Package:
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月27日 下午2:41:18
 * @version:
 */
public class MasterApp {

	private static final Logger log = LoggerFactory.getLogger(MasterApp.class);

	public static void main(String[] args) {
		startMaster(args);
	}

	public static void startMaster(String[] args) {
		new Thread() {
			public void run() {
				Server server = null;
				try {
					server = new NioSocketMasterServer().start();
				} catch (Exception e) {
					log.error(e.getLocalizedMessage(), e);
				} finally {
					if (null != server) {
						server.stop();
					}
				}
			}
		}.start();
		// 启动master守护进程定时更新数据的线程
		Thread masterDaemonThread = new MasterDaemonThread();
		masterDaemonThread.setDaemon(true);
		masterDaemonThread.start();

		new Thread() {
			public void run() {
				try {
					// 启动嵌入式jetty运行监控页面
					log.info("start jetty for master at port: {}", XserviceServerConfig.masterJettyPort());
					org.eclipse.jetty.server.Server jettyServer = new org.eclipse.jetty.server.Server(XserviceServerConfig.masterJettyPort());
					ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
					context.setContextPath("/");
					jettyServer.setHandler(context);
					context.addServlet(new ServletHolder(new MasterManagerServlet()), "/master");
					jettyServer.start();
					jettyServer.join();
				} catch (Exception e) {
					log.error(e.getLocalizedMessage(), e);
				}
			}
		}.start();
	}
}
