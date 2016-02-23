/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月23日 下午5:06:21  
 */
package me.chaoshoo.xservice.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @Title: XserviceServerConfigTest.java 
 * @Package: me.chaoshoo.xservice.util 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月23日 下午5:06:21 
 * @version:  
 */
public class XserviceServerConfigTest {

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
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceServerConfig#masterServerIp()}.
	 */
	@Test
	public void testMasterServerIp() {
		Assert.assertNotNull(XserviceServerConfig.masterServerIp());
	}

	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceServerConfig#masterServerPort()}.
	 */
	@Test
	public void testMasterServerPort() {
		Assert.assertNotNull(XserviceServerConfig.masterServerPort());
		Assert.assertTrue(XserviceServerConfig.masterServerPort() > 0);
	}

	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceServerConfig#slaveServerIp()}.
	 */
	@Test
	public void testSlaveServerIp() {
		Assert.assertNotNull(XserviceServerConfig.slaveServerIp());
	}

	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceServerConfig#slaveServerPort()}.
	 */
	@Test
	public void testSlaveServerPort() {
		Assert.assertNotNull(XserviceServerConfig.slaveServerPort());
		Assert.assertTrue(XserviceServerConfig.slaveServerPort() > 0);
	}

	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceServerConfig#isMasterServer()}.
	 */
	@Test
	public void testIsMasterServer() {
		Assert.assertNotNull(XserviceServerConfig.isMasterServer());
	}
	
	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceServerConfig#slaveUpdateTimeInterval()}.
	 */
	@Test
	public void testSlaveUpdateGap() {
		Assert.assertNotNull(XserviceServerConfig.slaveUpdateTimeInterval());
		Assert.assertTrue(XserviceServerConfig.slaveUpdateTimeInterval() > 0);
	}

	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceServerConfig#isMasterServer()}.
	 */
	@Test
	public void testMasterFileStore() {
		Assert.assertNotNull(XserviceServerConfig.masterFileStore());
	}

}
