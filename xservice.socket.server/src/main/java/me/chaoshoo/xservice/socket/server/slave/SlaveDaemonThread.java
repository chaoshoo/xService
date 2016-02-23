/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月27日 下午2:45:56  
 */
package me.chaoshoo.xservice.socket.server.slave;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import me.chaoshoo.xservice.core.communication.Command;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.service.ServiceFactory;
import me.chaoshoo.xservice.core.service.ServiceInfo;
import me.chaoshoo.xservice.util.SocketMessageUtil;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: SlaveUpdateThread.java
 * @Package: me.chaoshoo.xservice.socket.server.slave
 * @Description: slave发送更新数据的线程
 * @author: Hu Chao
 * @date: 2015年10月27日 下午2:45:56
 * @version:
 */
public class SlaveDaemonThread extends Thread {
	
	private static final Logger log = LoggerFactory.getLogger(SlaveDaemonThread.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (true) {
			Message message = new Message().command(Command.UPDATE_SLAVE_INFO);
			String slaveIp = XserviceServerConfig.slaveServerIp();
			Integer slavePort = XserviceServerConfig.slaveServerPort();
			log.debug("Slave info as ip:port:alive: {}", slaveIp + ":" + slavePort + ":" + ServiceFactory.aliveTrackInfoSize());
			message.param(Message.PARAM_SLAVE_IP, slaveIp)
					.param(Message.PARAM_SLAVE_PORT, slavePort.toString())
					.param(Message.PARAM_SERVICE_ALIVE, ServiceFactory.aliveTrackInfoSize().toString());
			List<ServiceInfo> availableServices = ServiceFactory.availableServices();
			Gson gson = new Gson();
			String servicesJson = gson.toJson(availableServices);
			message.param(Message.PARAM_SERVICE_LIST, servicesJson);
			String masterIp = XserviceServerConfig.masterServerIp();
			Integer masterPort = XserviceServerConfig.masterServerPort();
			try {
				SocketMessageUtil.asyncSendMessage(message, masterIp, masterPort);
				sleep(XserviceServerConfig.slaveUpdateTimeInterval());
			} catch (Exception e) {
				log.error(e.getLocalizedMessage(), e);
			}
		}
	}

}
