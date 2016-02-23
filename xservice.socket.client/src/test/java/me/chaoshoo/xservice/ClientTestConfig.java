/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月22日 上午10:16:35  
 */
package me.chaoshoo.xservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.util.XserviceCoreConfig;

/**
 * @Title: TestConfig.java 
 * @Package: me.chaoshoo.xservice 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月22日 上午10:16:35 
 * @version:  
 */
public class ClientTestConfig {
private static final Logger log = LoggerFactory.getLogger(ClientTestConfig.class);
	
	private static final String CONFIG_FILE = "test.properties";

	private static final String KEY_FILE_TEST_DIR = "dir.file.test";
	
	private static final String KEY_ZIP_FILE_NAME = "file.zip.name";
	
	private static final String KEY_SERVICE_NAME = "service.name";
	
	private static final String KEY_SERVICE_CMD_PARAMS = "service.cmd.params";
	
	private static final String KEY_TEST_CALLBACK_CLASS_NAME = "callback.class.name";
	
	private static final String TEST_NAMESPACE = "_test_";
	
	private static final String TEST_CALLBACK_IP = "127.0.0.1";
	
	private static final Integer TEST_CALLBACK_PORT = 9092;

	private static InputStream inputStream;

	private static Properties p;
	
	static {
		try {
			inputStream = XserviceCoreConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
			p = new Properties();
			p.load(inputStream);
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	public static String testFileDirPath() {
		return p.getProperty(KEY_FILE_TEST_DIR);
	}
	
	public static String testZipFileName() {
		return p.getProperty(KEY_ZIP_FILE_NAME);
	}
	
	public static String testServiceName() {
		return p.getProperty(KEY_SERVICE_NAME);
	}
	
	public static String testNamespace() {
		return TEST_NAMESPACE;
	}
	
	public static String testServiceCmdParams() {
		return p.getProperty(KEY_SERVICE_CMD_PARAMS);
	}
	
	public static String testCallbackIp() {
		return TEST_CALLBACK_IP;
	}
	
	public static Integer testCallbackPort() {
		return TEST_CALLBACK_PORT;
	}
	
	public static String testCallbackClassName() {
		return p.getProperty(KEY_TEST_CALLBACK_CLASS_NAME);
	}
}
