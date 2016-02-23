/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月28日 下午4:59:18  
 */
package me.chaoshoo.xservice.core.service;

/**
 * @Title: ProcessKiller.java 
 * @Package: me.chaoshoo.xservice.core.service 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月28日 下午4:59:18 
 * @version:  
 */
public class ProcessKiller extends Thread {

	/**
	 * The process to kill.
	 */
	private Process process;

	/**
	 * Builds the killer.
	 * 
	 * @param process
	 *            The process to kill.
	 */
	public ProcessKiller(Process process) {
		this.process = process;
	}

	/**
	 * It kills the supplied process.
	 */
	public void run() {
		process.destroy();
	}

}
