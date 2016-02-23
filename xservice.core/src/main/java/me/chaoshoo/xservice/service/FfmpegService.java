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
 * @Title: FfmepgService.java
 * @Package: me.chaoshoo.xservice.service
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月28日 上午10:31:11
 * @version:
 */
public class FfmpegService extends TrackableCmdService {

	private static final Logger log = LoggerFactory.getLogger(FfmpegService.class);

	private static final String NAME = "ffmpeg";

	private static final String DESCRIPTION = "ffmpeg provide video transform service";

	private long duration = 0L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.chaoshoo.xservice.core.service.trackable.TrackableService#
	 * percentFromMessage(java.lang.String)
	 */
	@Override
	public Integer percentFromMessage(String message) {
		if (StringUtil.isNullOrEmpty(message)) {
			return null;
		}
		String messageTrim = message.trim();
		log.debug("Message from process: {} ", messageTrim);
		if (messageTrim.startsWith("Duration:")) {
			try {
				String t1 = messageTrim.substring(messageTrim.indexOf("Duration:") + 9);
				String t2 = t1.substring(0, t1.indexOf(".")).trim();
				log.debug("Total duration data: {} @" + message.toString(), t2);
				String[] time = t2.split(":");
				int hour = Integer.parseInt(time[0].trim());
				log.debug("Total duration hour: {} @" + message.toString(), hour);
				int min = Integer.parseInt(time[1].trim());
				log.debug("Total duration minute: {} @" + message.toString(), min);
				int sec = Integer.parseInt(time[2].trim());
				log.debug("Total duration sec: {} @" + message.toString(), sec);
				duration = hour * 3600 + min * 60 + sec;
				log.debug("Total duration time: {} @" + message.toString(), duration);
				return 0;
			} catch (Exception e) {
				log.error("Total duration parses error: {}", message);
			}
		} else if (messageTrim.startsWith("frame=")) {
			try {
				String t1 = messageTrim.substring(messageTrim.indexOf("time=") + 5);
				String t2 = t1.substring(0, t1.indexOf(".")).trim();
				log.debug("Current transformed data: {} @" + message.toString(), t2);
				String[] time = t2.split(":");
				int hour = Integer.parseInt(time[0].trim());
				log.debug("Current transformed hour: {} @" + message.toString(), hour);
				int min = Integer.parseInt(time[1].trim());
				log.debug("Current transformed minute: {} @" + message.toString(), min);
				int sec = Integer.parseInt(time[2].trim());
				log.debug("Current transformed sec: {} @" + message.toString(), sec);
				long now = hour * 3600 + min * 60 + sec;
				int percent = (int) (now * 100 / duration);
				log.debug("Current transformed time and percent: {} @" + message.toString(), now + ":" + percent);
				return percent;
			} catch (Exception e) {
				log.error("Total duration parse error: {}", message);
			}
		}
		return null;
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
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SERVICE_SRC_FILE_NAME))) {
			throw new ServiceException("Cmd src file name could not be null for cmd service");
		}
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SERVICE_DEST_FILE_NAME))) {
			throw new ServiceException("Cmd dest file name could not be null for cmd service");
		}
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
		String cmdCommandPath = CmdServiceConfig.cmdPath(NAME);
		String cmdCommandParams = null == message.param(Message.PARAM_CMD_COMMAND_PARAM) ? "" : message
				.param(Message.PARAM_CMD_COMMAND_PARAM);
		sb.append(cmdCommandPath).append(" ");
		if (cmdCommandParams.contains(" -i ")) {
			String prefix = cmdCommandParams.substring(0, cmdCommandParams.indexOf(" -i ") + 4);
			sb.append(prefix);
			sb.append(srcFile.getAbsolutePath()).append(" ");
			String suffix = cmdCommandParams.substring(cmdCommandParams.indexOf(" -i ") + 4);
			sb.append(suffix).append(" ");
			sb.append(desFile.getAbsolutePath());
		} else {
			sb.append(cmdCommandParams).append(" ");
			sb.append(srcFile.getAbsolutePath()).append(" ");
			sb.append(desFile.getAbsolutePath());
		}
		return sb.toString();
	}
	
}
