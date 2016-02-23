/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月28日 上午10:31:11  
 */
package me.chaoshoo.xservice.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.service.ServiceException;
import me.chaoshoo.xservice.core.service.trackable.TrackableCmdService;
import me.chaoshoo.xservice.util.CmdServiceConfig;
import me.chaoshoo.xservice.util.StringUtil;

/**
 * @Title: OfficeService.java
 * @Package: me.chaoshoo.xservice.service
 * @Description:
 * @author: sunlin
 * @date: 2015年11月24日 上午10:06:34
 * @version:
 */
public class OfficeService extends TrackableCmdService {

	private static final Logger log = LoggerFactory.getLogger(OfficeService.class);

	private static final String NAME = "office";

	private static final String DESCRIPTION = "office provide office document format transform service";

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.chaoshoo.xservice.core.service.trackable.TrackableService#
	 * percentFromMessage(java.lang.String)
	 */
	@Override
	public Integer percentFromMessage(String message) {
		return 50;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.core.service.Service#check(me.chaoshoo.xservice
	 * .core.communication.Message)
	 */
	@Override
	protected Boolean check(Message message) throws ServiceException {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.chaoshoo.xservice.core.service.Service#name()
	 */
	@Override
	public String name() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.chaoshoo.xservice.core.service.Service#description()
	 */
	@Override
	public String description() {
		return DESCRIPTION;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.service.trackable.TrackableCmdService#recordProcess(java.lang.Process)
	 */
	@Override
	protected Boolean recordProcess(String namespace, String serviceId, String serviceName, Process p) throws ServiceException {
		BufferedReader reader = null;
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(p.getErrorStream());
			reader = new BufferedReader(isr);
			String line;
			while ((line = reader.readLine()) != null) {
				try{
					log.debug("Process message: {}", line);
					update(namespace, serviceId, serviceName, line);
				} catch(Exception e) {
					continue;
				}
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					reader = null;
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					isr = null;
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.service.trackable.TrackableCmdService#toCmd(me.chaoshoo.xservice.core.communication.Message, java.io.File, java.io.File)
	 */
	@Override
	protected String toCmd(Message message, File srcFile, File desFile) {
		StringBuffer sb = new StringBuffer();
		message.param(message.PARAM_CMD_COMMAND_PARAM);
		String cmdCommandPath = CmdServiceConfig.cmdPath(NAME);
		String cmdCommandParams = null == message.param(Message.PARAM_CMD_COMMAND_PARAM) ? "" : message
				.param(Message.PARAM_CMD_COMMAND_PARAM);
		sb.append(cmdCommandPath).append(" ");
		sb.append(cmdCommandParams).append(" ");
		sb.append(srcFile.getAbsolutePath()).append(" ");
		sb.append(desFile.getAbsolutePath());
		return sb.toString();
	}
	
}
