/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月22日 上午9:58:24  
 */
package me.chaoshoo.xservice.service;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.ServerTestConfig;
import me.chaoshoo.xservice.core.communication.Command;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.service.Service;
import me.chaoshoo.xservice.util.FileUtil;
import me.chaoshoo.xservice.util.XserviceServerConfig;

/**
 * @Title: RezipServiceTest.java
 * @Package: me.chaoshoo.xservice.service
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月22日 上午9:58:24
 * @version:
 */
public class RezipServiceTest {

	private static final Logger log = LoggerFactory.getLogger(RezipServiceTest.class);

	private Service service;

	private Message message;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		String fileName = ServerTestConfig.testZipFileName();
		FileUtil.copy(new File(ServerTestConfig.testFileDirPath() + File.separator + fileName),
				new File(XserviceServerConfig.srcFileDirPath() + File.separator + ServerTestConfig.testNamespace() + File.separator + fileName));
		service = new RezipService().srcFileDirPath(XserviceServerConfig.srcFileDirPath())
				.tempFileDirPath(XserviceServerConfig.tempFileDirPath())
				.destFileDirPath(XserviceServerConfig.destFileDirPath());
		message = new Message(ServerTestConfig.testNamespace(), Command.CALL_SERVICE).serviceId(String.valueOf(System.currentTimeMillis()))
			.param(Message.PARAM_FILE_NAME, fileName);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		String fileName = ServerTestConfig.testZipFileName();
		new File(XserviceServerConfig.srcFileDirPath() + File.separator + ServerTestConfig.testNamespace() + File.separator + fileName).delete();
		File file = new File(XserviceServerConfig.destFileDirPath() + File.separator + fileName + "_t");
		if (file.exists()) {
			FileUtil.delete(file);
		}
		service = null;
		message = null;
	}

	/**
	 * Test method for
	 * {@link me.chaoshoo.xservice.service.RezipService#executeService(me.chaoshoo.xservice.core.communication.Message)}
	 * .
	 */
	@Test
	public void testService() {
		try {
			Assert.assertNotNull(service.executeService(message));
			String fileName = ServerTestConfig.testZipFileName();
			File file = new File(XserviceServerConfig.destFileDirPath() + File.separator + message.namespace() + File.separator + message.serviceId() + File.separator + fileName);
			Assert.assertTrue(file.exists());
			File upZiipFile = FileUtil.unZip(file.getAbsolutePath(), file.getAbsolutePath() + "_t");
			Assert.assertNotNull(upZiipFile);
			Assert.assertTrue(upZiipFile.exists());
			File tempFile = new File(XserviceServerConfig.tempFileDirPath() + File.separator + fileName);
			Assert.assertFalse(tempFile.exists());
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for {@link me.chaoshoo.xservice.service.RezipService#name()}
	 * .
	 */
	@Test
	public void testName() {
		Assert.assertNotNull(service.name());
		Assert.assertFalse(service.name().isEmpty());
	}

	/**
	 * Test method for
	 * {@link me.chaoshoo.xservice.service.RezipService#description()}.
	 */
	@Test
	public void testDescription() {
		Assert.assertNotNull(service.description());
		Assert.assertFalse(service.description().isEmpty());
	}

}
