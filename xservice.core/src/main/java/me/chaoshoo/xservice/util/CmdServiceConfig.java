/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月15日 下午5:56:37  
 */
package me.chaoshoo.xservice.util;

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
public class CmdServiceConfig {

	private static final Logger log = LoggerFactory.getLogger(CmdServiceConfig.class);
	
	private static final String CONFIG_FILE = "xservice_cmd_service_config.properties";

	private static InputStream inputStream;

	private static Properties p;
	
	static {
		try {
			inputStream = CmdServiceConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
			p = new Properties();
			p.load(inputStream);
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	public static final String cmdPath(String name) {
		return  p.getProperty(name);
	}
	
	public static Boolean vaild(String name) {
		return p.containsKey(name);
	}
	
}
