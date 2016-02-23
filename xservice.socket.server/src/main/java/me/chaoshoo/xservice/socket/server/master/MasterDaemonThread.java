/**
 * Copyright:
 * All right reserved
 * Created on: 2015年11月2日 上午10:07:46  
 */
package me.chaoshoo.xservice.socket.server.master;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.Master;
import me.chaoshoo.xservice.core.Slave;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: MasterDaemonThread.java
 * @Package: me.chaoshoo.xservice.socket.server.master
 * @Description:
 * @author: Hu Chao
 * @date: 2015年11月2日 上午10:07:46
 * @version:
 */
public class MasterDaemonThread extends Thread {

	private static final Logger log = LoggerFactory.getLogger(MasterDaemonThread.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			log.debug("Begin master daemon thread loop!");
			try {
				Master master = NioSocketMasterServer.master;
				List<Slave> slaves = master.slaves();
				log.debug("Slave count in master daemon thread loop: {}", slaves.size());
				for (Slave slave : slaves) {
					log.debug("Slave: " + slave.ip() + ":" + slave.port() + " update time add interval: {}",
							slave.updateTime() + XserviceServerConfig.masterUpdateTimeInterval());
					log.debug("Slave: " + slave.ip() + ":" + slave.port() + " current time: {}",
							System.currentTimeMillis());
					if (slave.updateTime() + XserviceServerConfig.masterUpdateTimeInterval() < System
							.currentTimeMillis()) {
						NioSocketMasterServer.master.removeSlave(slave);
					}
				}
				sleep(XserviceServerConfig.masterUpdateTimeInterval());
				log.debug("End master daemon thread loop!");
			} catch (Exception e) {
			}
		}
	}
}
