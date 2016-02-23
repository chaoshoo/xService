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
public class XserviceCoreConfig {

	private static final Logger log = LoggerFactory.getLogger(XserviceCoreConfig.class);
	
	private static final String CONFIG_FILE = "xservice_core_config.properties";

	private static InputStream inputStream;

	private static Properties p;
	
	private static final String SYSTEM_NAMESPACE = "_system_";
	
	private static final String CALLBACK_FACTORY_CLASS_NAME = "callback.factory.name";
	
	static {
		try {
			inputStream = XserviceCoreConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
			p = new Properties();
			p.load(inputStream);
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	public static final String systemNamespace() {
		return SYSTEM_NAMESPACE;
	}
	
	public static final String callbackFactoryClassName() {
		return p.getProperty(CALLBACK_FACTORY_CLASS_NAME);
	}
}
