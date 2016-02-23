/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月26日 下午2:43:45  
 */
package me.chaoshoo.xservice.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @Title: Master.java
 * @Package: me.chaoshoo.xservice.core
 * @Description: master服务器数据对象
 * @author: Hu Chao
 * @date: 2015年10月26日 下午2:43:45
 * @version:
 */
public class Master {
	
	private String ip;

	private Integer port;

	/**
	 * @param ip
	 * @param port
	 */
	public Master(String ip, Integer port) {
		super();
		this.ip = ip;
		this.port = port;
		this.slaves = new HashMap<String, Slave>();
	}
	
	Map<String, Slave> slaves;

	/**
	 * @return the slave
	 */
	public Slave avaliableSlave(String serviceName) {
		Slave result = null;
		Set<Entry<String, Slave>> set = this.slaves.entrySet();
		Iterator<Entry<String, Slave>> iter = set.iterator();
		while(iter.hasNext()) {
			Entry<String, Slave> entry = iter.next();
			Slave slave = entry.getValue();
			if (!slave.avaliableForService(serviceName)) {
				continue;
			}
			if (null == result || result.serviceAlive() > slave.serviceAlive()) {
				result = slave;
			}
		}
		return result;
	}

	/**
	 * @param slaves the slaves to set
	 */
	public Master slave(Slave slave) {
		this.slaves.put(slave.ip() + ":" + slave.port() , slave);
		return this;
	}

	public Master removeSlave(Slave slave) {
		this.slaves.remove(slave.ip() + ":" + slave.port());
		return this;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Slave> slaves() {
		List<Slave> result = new ArrayList<Slave>();
		Set<Entry<String, Slave>> set = this.slaves.entrySet();
		Iterator<Entry<String, Slave>> iter = set.iterator();
		while(iter.hasNext()) {
			Entry<String, Slave> entry = iter.next();
			Slave slave = entry.getValue();
			result.add(slave);
		}
		return result;
	}

	/**
	 * @return the ip
	 */
	public String ip() {
		return ip;
	}

	/**
	 * @return the port
	 */
	public Integer port() {
		return port;
	}
}
