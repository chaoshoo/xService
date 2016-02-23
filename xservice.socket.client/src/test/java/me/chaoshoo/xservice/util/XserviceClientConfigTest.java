/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月23日 下午5:33:07  
 */
package me.chaoshoo.xservice.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @Title: XserviceClientConfigTest.java 
 * @Package: me.chaoshoo.xservice.util 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月23日 下午5:33:07 
 * @version:  
 */
public class XserviceClientConfigTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceClientConfig#masterServerIp()}.
	 */
	@Test
	public void testMasterServerIp() {
		Assert.assertNotNull(XserviceClientConfig.masterServerIp());
	}

	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceClientConfig#masterServerPort()}.
	 */
	@Test
	public void testMasterServerPort() {
		Assert.assertNotNull(XserviceClientConfig.masterServerPort());
		Assert.assertTrue(XserviceClientConfig.masterServerPort() > 0);
	}

	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceClientConfig#tempFileDir()}.
	 */
	@Test
	public void testTempFileDir() {
		Assert.assertNotNull(XserviceClientConfig.tempFileDir());
	}

	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceClientConfig#destFileDir()}.
	 */
	@Test
	public void testDestFileDir() {
		Assert.assertNotNull(XserviceClientConfig.destFileDir());
	}

}
