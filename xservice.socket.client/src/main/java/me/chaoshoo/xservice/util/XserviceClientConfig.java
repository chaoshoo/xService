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
public class XserviceClientConfig {

	private static final Logger log = LoggerFactory.getLogger(XserviceClientConfig.class);
	
	private static final String CONFIG_FILE = "xservice_client_config.properties";

	private static InputStream inputStream;

	private static Properties p;
	
	private static final String KEY_MASTER_SERVER_PORT = "master.server.port";

	private static final String KEY_MASTER_SERVER_IP = "master.server.ip";
	
	private static final Integer DEFAULT_MASTER_PORT = 9190;
	
	private static final Integer DEFAULT_CALLBACK_PORT = 9192;
	
	private static final String KEY_WORK_DIR = "work.file.dir";
	
	private static final String KEY_CALLBACK_TYPE = "callback.type";

	private static final String KEY_CALLBACK_PORT = "callback.port";

	private static final String KEY_CALLBACK_IP = "callback.ip";
	
	private static final String KEY_CALLBACK_URL = "callback.url";
	static {
		try {
			inputStream = XserviceClientConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
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
		} catch(NumberFormatException e) {
			return DEFAULT_MASTER_PORT;
		}
	}
	
	public static final String tempFileDir() {
		String path = null;
		if (null != p.getProperty(KEY_WORK_DIR)) {
			path =  p.getProperty(KEY_WORK_DIR) + File.separator + "client_temp_dir";
		} else {
			path = System.getProperty("user.dir") + File.separator + "client_temp_dir";
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}
	
	public static final String destFileDir() {
		String path = null;
		if (null != p.getProperty(KEY_WORK_DIR)) {
			path =  p.getProperty(KEY_WORK_DIR) + File.separator + "client_dest_dir";
		} else {
			path = System.getProperty("user.dir") + File.separator + "client_dest_dir";
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}
	
	public static final String callbackIp() {
		return p.getProperty(KEY_CALLBACK_IP);
	}
	
	public static final Integer callbackPort() {
		try {
			return Integer.valueOf(p.getProperty(KEY_CALLBACK_PORT));
		} catch(NumberFormatException e) {
			return DEFAULT_CALLBACK_PORT;
		}
	}
	
	public static final String callbackUrl() {
		return p.getProperty(KEY_CALLBACK_URL);
	}
	
	public static final String callbackType() {
		return p.getProperty(KEY_CALLBACK_TYPE);
	}
}
