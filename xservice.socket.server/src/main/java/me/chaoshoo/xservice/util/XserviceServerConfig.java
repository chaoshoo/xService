/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月15日 下午5:56:37  
 */
package me.chaoshoo.xservice.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title: SystemConfig.java
 * @Package: me.chaoshoo.xservice.util
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月15日 下午5:56:37
 * @version:
 */
public class XserviceServerConfig {

	private static final Logger log = LoggerFactory.getLogger(XserviceServerConfig.class);

	private static final String CONFIG_FILE = "xservice_server_config.properties";

	private static InputStream inputStream;

	private static Properties p;

	private static final String KEY_SERVER_TYPE = "server.type";

	private static final String KEY_MASTER_SERVER_PORT = "master.server.port";

	private static final String KEY_MASTER_SERVER_IP = "master.server.ip";

	private static final String KEY_SLAVE_SERVER_PORT = "slave.server.port";

	private static final String KEY_SLAVE_SERVER_IP = "slave.server.ip";
	
	private static final String  KEY_SLAVE_JETTY_PORT = "slave.jetty.port";
	
	private static final String  KEY_MASTER_JETTY_PORT = "master.jetty.port";

	private static final Integer DEFAULT_SLAVE_PORT = 9191;

	private static final Integer DEFAULT_MASTER_PORT = 9190;

	private static final Integer DEFAULT_SLAVE_JETTY_PORT = 9181;

	private static final Integer DEFAULT_MASTER_JETTY_PORT = 9180;

	public static final String DEFAULT_SERVER_TYPE = "slave";

	private static final String SLAVE_TIME_INTERVAL = "slave.time.interval";

	private static final Integer DEFAULT_SLAVE_TIME_INTERVAL = 1000 * 10 * 60;

	private static final String SERVER_TYPE_MASTER = "master";

	private static final String MASTER_TIME_INTERVAL = "master.time.interval";

	private static final Integer DEFAULT_MASTER_TIME_INTERVAL = DEFAULT_SLAVE_TIME_INTERVAL * 2;

	private static final String KEY_MANAGER_LISTEN_PORT = "manager.listen.port";

	private static final Integer DEFAULT_MANAGER_LISTEN_PORT = 9199;
	
	private static final String KEY_WORK_DIR = "work.file.dir";

	static {
		try {
			inputStream = XserviceCoreConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
			p = new Properties();
			p.load(inputStream);
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	public static final String masterServerIp() {
		return p.getProperty(KEY_MASTER_SERVER_IP);
	}

	public static final Integer masterServerPort() {
		try {
			return Integer.valueOf(p.getProperty(KEY_MASTER_SERVER_PORT));
		} catch (NumberFormatException e) {
			return DEFAULT_MASTER_PORT;
		}
	}

	public static final String slaveServerIp() {
		return p.getProperty(KEY_SLAVE_SERVER_IP);
	}

	public static final Integer slaveServerPort() {
		try {
			return Integer.valueOf(p.getProperty(KEY_SLAVE_SERVER_PORT));
		} catch (NumberFormatException e) {
			return DEFAULT_SLAVE_PORT;
		}
	}

	public static final Integer slaveJettyPort() {
		try {
			return Integer.valueOf(p.getProperty(KEY_SLAVE_JETTY_PORT));
		} catch (NumberFormatException e) {
			return DEFAULT_SLAVE_JETTY_PORT;
		}
	}

	public static final Integer masterJettyPort() {
		try {
			return Integer.valueOf(p.getProperty(KEY_MASTER_JETTY_PORT));
		} catch (NumberFormatException e) {
			return DEFAULT_MASTER_JETTY_PORT;
		}
	}

	public static final Boolean isMasterServer() {
		return SERVER_TYPE_MASTER.equals(p.getProperty(KEY_SERVER_TYPE));
	}

	public static final Integer slaveUpdateTimeInterval() {
		try {
			return Integer.valueOf(p.getProperty(SLAVE_TIME_INTERVAL));
		} catch (NumberFormatException e) {
			return DEFAULT_SLAVE_TIME_INTERVAL;
		}
	}

	public static final Integer masterUpdateTimeInterval() {
		try {
			return Integer.valueOf(p.getProperty(MASTER_TIME_INTERVAL));
		} catch (NumberFormatException e) {
			return DEFAULT_MASTER_TIME_INTERVAL;
		}
	}

	public static final Integer managerListenPort() {
		try {
			return Integer.valueOf(p.getProperty(KEY_MANAGER_LISTEN_PORT));
		} catch (NumberFormatException e) {
			return DEFAULT_MANAGER_LISTEN_PORT;
		}
	}
	
	public static final String masterFileStore() {
		String path = null;
		if (null != p.getProperty(KEY_WORK_DIR)) {
			path =  p.getProperty(KEY_WORK_DIR) + File.separator + "master_file_store";
		} else {
			path = System.getProperty("user.dir") + File.separator + "master_file_store";
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}
	
	public static final String tempFileDirPath() {
		String path = null;
		if (null != p.getProperty(KEY_WORK_DIR)) {
			path =  p.getProperty(KEY_WORK_DIR) + File.separator + "slave_temp";
		} else {
			path = System.getProperty("user.dir") + File.separator + "slave_temp";
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}
	
	public static final String srcFileDirPath() {
		String path = null;
		if (null != p.getProperty(KEY_WORK_DIR)) {
			path =  p.getProperty(KEY_WORK_DIR) + File.separator + "slave_src";
		} else {
			path = System.getProperty("user.dir") + File.separator + "slave_src";
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}
	
	public static final String destFileDirPath() {
		String path = null;
		if (null != p.getProperty(KEY_WORK_DIR)) {
			path =  p.getProperty(KEY_WORK_DIR) + File.separator + "slave_dest";
		} else {
			path = System.getProperty("user.dir") + File.separator + "slave_dest";
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

}
