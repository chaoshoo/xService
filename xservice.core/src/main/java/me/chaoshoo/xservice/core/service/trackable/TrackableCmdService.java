package me.chaoshoo.xservice.core.service.trackable;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.service.ProcessKiller;
import me.chaoshoo.xservice.core.service.ServiceException;
import me.chaoshoo.xservice.core.service.ServiceFactory;

/**
 * @Title: TrackableCmdService.java 
 * @Package: me.chaoshoo.xservice.core.service.trackable 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月28日 上午11:33:30 
 * @version:  
 */
public abstract class TrackableCmdService extends TrackableService{
	
	private static final Logger log = LoggerFactory.getLogger(TrackableCmdService.class);

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.service.Service#service(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	protected File service(Message message) throws ServiceException {
		String srcFilePath = srcFileDirPath + File.separator
				+ message.namespace() + File.separator
				+ message.param(Message.PARAM_SERVICE_SRC_FILE_NAME);
		File srcFile = new File(srcFilePath);
		if (null == srcFile || !srcFile.exists()) {
			throw new ServiceException("The source file to service is missing: " + srcFile.getAbsolutePath());
		}
		String desFilePath = destFileDirPath + File.separator
				+ message.namespace() + File.separator
				+ message.serviceId() + File.separator
				+ message.param(Message.PARAM_SERVICE_DEST_FILE_NAME);
		File desFile = new File(desFilePath);
		if (!desFile.getParentFile().exists()) {
			desFile.getParentFile().mkdirs();
		}
		Runtime runtime = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		try {
			String cmd = toCmd(message, srcFile, desFile);
			Process process = runtime.exec(cmd);// 启动另一个进程来执行命令
			ProcessKiller processKiller = new ProcessKiller(process);
			runtime.addShutdownHook(processKiller);
			log.info("Execute cmd: {}", cmd);
			Boolean result = recordProcess(message.namespace(), message.serviceId(), message.param(Message.PARAM_SERVICE_NAME), process);
			process.waitFor(); // 等待进程结束
			ServiceFactory.removeTrackInfo(message.namespace(), message.serviceId());
			log.info("Execute result for cmd: {} : " + result , cmd);
			if (process != null) {
				process.destroy();
				process = null;
			}
			runtime.removeShutdownHook(processKiller);
			return desFile;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			throw new ServiceException(e.getLocalizedMessage(), e);
		}
	}

	protected abstract String toCmd(Message message, File srcFile, File desFile);
	
	protected abstract Boolean recordProcess(String namespace, String serviceId, String serviceName, Process p) throws ServiceException;
}
