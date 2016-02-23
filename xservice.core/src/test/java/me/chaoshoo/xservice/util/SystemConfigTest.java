/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月22日 上午9:42:27  
 */
package me.chaoshoo.xservice.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Title: SystemConfigTest.java 
 * @Package: me.chaoshoo.xservice.util 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月22日 上午9:42:27 
 * @version:  
 */
public class SystemConfigTest {

	/**
	 * Test method for {@link me.chaoshoo.xservice.util.XserviceCoreConfig#systemNamespace()}.
	 */
	@Test
	public void testSystemNamespace() {
		Assert.assertNotNull(XserviceCoreConfig.systemNamespace());
	}

}
