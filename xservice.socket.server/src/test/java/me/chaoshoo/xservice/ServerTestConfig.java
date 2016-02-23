package me.chaoshoo.xservice;

/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月22日 上午10:16:35  
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.util.XserviceCoreConfig;

/**
 * @Title: ServerTestConfig.java 
 * @Package: me.chaoshoo.xservice 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月22日 上午10:16:35 
 * @version:  
 */
public class ServerTestConfig {
private static final Logger log = LoggerFactory.getLogger(ServerTestConfig.class);
	
	private static final String CONFIG_FILE = "test.properties";

	private static final String KEY_FILE_TEST_DIR = "dir.file.test";
	
	private static final String KEY_ZIP_FILE_NAME = "file.zip.name";
	
	private static final String TEST_NAMESPACE = "_test_";
	
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
	
	public static String testNamespace() {
		return TEST_NAMESPACE;
	}
}
