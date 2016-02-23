package me.chaoshoo.xservice;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.Server;
import me.chaoshoo.xservice.socket.server.slave.NioSocketSlaveServer;
import me.chaoshoo.xservice.socket.server.slave.SlaveDaemonThread;
import me.chaoshoo.xservice.socket.server.slave.SlaveManagerServlet;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: MasterApp.java
 * @Package:
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月27日 下午2:41:18
 * @version:
 */
public class SlaveApp {

	private static final Logger log = LoggerFactory.getLogger(SlaveApp.class);

	public static void main(String[] args) {
		startSlave(args);
	}

	public static void startSlave(String[] args) {
		new Thread() {
			public void run() {
				Server server = null;
				try {
					// 启动slave守护进程定时向master发送更新数据的线程
					Thread slaveDeamonThread = new SlaveDaemonThread();
					slaveDeamonThread.setDaemon(true);
					slaveDeamonThread.start();
					server = new NioSocketSlaveServer().start();
				} catch (Exception e) {
					log.error(e.getLocalizedMessage(), e);
				} finally {
					if (null != server) {
						server.stop();
					}
				}
			}
		}.start();

		// 启动slave守护进程定时向master发送更新数据的线程
		Thread slaveDeamonThread = new SlaveDaemonThread();
		slaveDeamonThread.setDaemon(true);
		slaveDeamonThread.start();

		new Thread() {
			public void run() {
				try {
					// 启动嵌入式jetty运行监控页面
					log.info("start jetty for slave at port: {}", XserviceServerConfig.slaveJettyPort());
					org.eclipse.jetty.server.Server jettyServer = new org.eclipse.jetty.server.Server(XserviceServerConfig.slaveJettyPort());
					ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
					context.setContextPath("/");
					jettyServer.setHandler(context);
					context.addServlet(new ServletHolder(new SlaveManagerServlet()), "/slave");
					jettyServer.start();
					jettyServer.join();
				} catch (Exception e) {
					log.error(e.getLocalizedMessage(), e);
				}
			}
		}.start();
	}
}
