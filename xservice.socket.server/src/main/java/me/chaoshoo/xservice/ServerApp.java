package me.chaoshoo.xservice;

import me.chaoshoo.xservice.util.XserviceServerConfig;

public class ServerApp {
	public static void main(String[] args) {
		if (XserviceServerConfig.isMasterServer()) {
			MasterApp.startMaster(args);
		} else {
			SlaveApp.startSlave(args);
		}
	}
}
