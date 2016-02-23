/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月22日 下午2:16:58  
 */
package me.chaoshoo.xservice.socket;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.ClientTestConfig;
import me.chaoshoo.xservice.client.socket.NioSocketMasterClient;
import me.chaoshoo.xservice.core.MasterClient;
import me.chaoshoo.xservice.core.SlaveClient;
import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.service.trackable.ServiceTrackInfo;

/**
 * @Title: NioSocketClientTest.java
 * @Package: me.chaoshoo.xservice.socket
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月22日 下午2:16:58
 * @version:
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NioSocketClientTest {

	private static final Logger log = LoggerFactory.getLogger(NioSocketClientTest.class);

	private MasterClient masterClient;

	private SlaveClient slaveClient;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		masterClient = new NioSocketMasterClient();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		masterClient = null;
		slaveClient = null;
	}

	/**
	 * Test method for
	 * {@link me.chaoshoo.xservice.client.socket.NioSocketClient#requestService(java.lang.String)}
	 * .
	 */
//	@Test
	public void test1RequestService() {
		try {
			slaveClient = masterClient.requestService(ClientTestConfig.testServiceName());
			Assert.assertNotNull(slaveClient);
		} catch (CommunicationException e) {
			log.error(e.getLocalizedMessage(), e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link me.chaoshoo.xservice.client.socket.NioSocketClient#prepareFile(java.lang.String, java.io.File)}
	 * .
	 */
//	@Test
	public void test2PrepareFile() {
		File testZipFile = new File(ClientTestConfig.testFileDirPath() + File.separator
				+ ClientTestConfig.testZipFileName());
		try {
			slaveClient = masterClient.requestService(ClientTestConfig.testServiceName());
			Assert.assertTrue(slaveClient.prepareFile(ClientTestConfig.testNamespace(), testZipFile));
		} catch (CommunicationException e) {
			log.error(e.getLocalizedMessage(), e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link me.chaoshoo.xservice.client.socket.NioSocketClient#sendFile(java.lang.String, java.io.File)}
	 * .
	 */
//	@Test
	public void test3SendFile() {
		File testFile = new File(ClientTestConfig.testFileDirPath() + File.separator
				+ ClientTestConfig.testZipFileName());
		try {
			slaveClient = masterClient.requestService(ClientTestConfig.testServiceName());
			Assert.assertTrue(slaveClient.sendFile(ClientTestConfig.testNamespace(), testFile));
		} catch (CommunicationException e) {
			log.error(e.getLocalizedMessage(), e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link me.chaoshoo.xservice.client.socket.NioSocketClient#syncCallService(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 */
//	@Test
	public void test4SyncCallService() {
		File testZipFile = new File(ClientTestConfig.testFileDirPath() + File.separator
				+ ClientTestConfig.testZipFileName());
		String serviceId = String.valueOf(System.currentTimeMillis());
		try {
			slaveClient = masterClient.requestService(ClientTestConfig.testServiceName());
			Assert.assertTrue(slaveClient.sendFile(ClientTestConfig.testNamespace(), testZipFile));
			Map<String, String> params = new HashMap<String, String>();
			params.put(Message.PARAM_SERVICE_SRC_FILE_NAME, ClientTestConfig.testZipFileName());
			params.put(Message.PARAM_SERVICE_DEST_FILE_NAME, ClientTestConfig.testZipFileName());
			params.put(Message.PARAM_CMD_COMMAND_PARAM, ClientTestConfig.testServiceCmdParams());
			Assert.assertTrue(slaveClient.syncCallService(ClientTestConfig.testNamespace(), serviceId,
					ClientTestConfig.testServiceName(), params));
		} catch (CommunicationException e) {
			log.error(e.getLocalizedMessage(), e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link me.chaoshoo.xservice.client.socket.NioSocketClient#serviceStatus(java.lang.String)}
	 * .
	 */
	//@Test
	public void test5ServiceStatus() {
		File testZipFile = new File(ClientTestConfig.testFileDirPath() + File.separator
				+ ClientTestConfig.testZipFileName());
		Map<String, String> params = new HashMap<String, String>();
		params.put(Message.PARAM_SERVICE_SRC_FILE_NAME, ClientTestConfig.testZipFileName());
		params.put(Message.PARAM_SERVICE_DEST_FILE_NAME, ClientTestConfig.testZipFileName());
		params.put(Message.PARAM_CMD_COMMAND_PARAM, ClientTestConfig.testServiceCmdParams());
		try {
			String serviceId = String.valueOf(System.currentTimeMillis());
			slaveClient = masterClient.requestService(ClientTestConfig.testServiceName());
			slaveClient.sendFile(ClientTestConfig.testNamespace(), testZipFile);
			slaveClient.asyncCallService(ClientTestConfig.testNamespace(), serviceId,
					ClientTestConfig.testServiceName(), params);
			Thread.sleep(1000 * 10);
			ServiceTrackInfo info = slaveClient.serviceStatus(ClientTestConfig.testNamespace(), serviceId);
			Assert.assertNotNull(info);
			Assert.assertNotNull(info.message());
			Assert.assertNotNull(info.serviceName());
			Assert.assertNotNull(info.percent());
		} catch (CommunicationException e) {
			log.error(e.getLocalizedMessage(), e);
			Assert.fail(e.getLocalizedMessage());
		} catch (InterruptedException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	
	@Test
	public void test6ServiceStatus() {
		File testZipFile = new File(ClientTestConfig.testFileDirPath() + File.separator
				+ ClientTestConfig.testZipFileName());
		Map<String, String> params = new HashMap<String, String>();
		params.put(Message.PARAM_SERVICE_SRC_FILE_NAME, "dd.docx");
		params.put(Message.PARAM_SERVICE_DEST_FILE_NAME, "ddddd.pdf");
		params.put(Message.PARAM_CMD_COMMAND_PARAM, "word2pdf");
		params.put("test", "test test test");
		
		params.put(Message.PARAM_CALLBACK_CLASS_NAME, "me.chaoshoo.zyk.xservice.service.OfficeService");
		try {
			String serviceId = String.valueOf(System.currentTimeMillis());
			slaveClient = masterClient.requestService("office");
			slaveClient.sendFile(ClientTestConfig.testNamespace(), new File("D:\\dd.docx"));
			slaveClient.asyncCallService(ClientTestConfig.testNamespace(), serviceId,
					"office", params);
			Thread.sleep(100 * 10);
			ServiceTrackInfo info = slaveClient.serviceStatus(ClientTestConfig.testNamespace(), serviceId);
			Assert.assertNotNull(info);
			Assert.assertNotNull(info.message());
			Assert.assertNotNull(info.serviceName());
			Assert.assertNotNull(info.percent());
		} catch (CommunicationException e) {
			log.error(e.getLocalizedMessage(), e);
			Assert.fail(e.getLocalizedMessage());
		} catch (InterruptedException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	

}
