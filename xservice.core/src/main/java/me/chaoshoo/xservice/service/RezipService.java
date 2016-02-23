/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月19日 下午3:25:53  
 */
package me.chaoshoo.xservice.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.service.ServiceException;
import me.chaoshoo.xservice.core.service.trackable.TrackableService;
import me.chaoshoo.xservice.util.FileUtil;
import me.chaoshoo.xservice.util.StringUtil;

/**
 * @Title: RezipService.java 
 * @Package: me.chaoshoo.xservice.service 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月19日 下午3:25:53 
 * @version:  
 */
public class RezipService extends TrackableService {
	
	private static final Logger log = LoggerFactory.getLogger(RezipService.class);
	
	private static final String NAME = "rezip";
	
	private static final String DESCRIPTION = "rezip a file with fastest level";

	/**
	 * 
	 */
	public RezipService() {
		super();
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.service.Servicable#service(java.util.Map)
	 */
	@Override
	public File service(Message message) throws ServiceException {
		update(message.namespace(), message.serviceId(), NAME,  "percent:0");
		String srcFilePath = srcFileDirPath + File.separator
				+ message.namespace() + File.separator
				+ message.param(Message.PARAM_SERVICE_SRC_FILE_NAME);
		File srcFile = new File(srcFilePath);
		if (null == srcFile || !srcFile.exists()) {
			throw new ServiceException("The source file to service is missing: " + srcFile.getAbsolutePath());
		}
		String tempFilePath = tempFileDirPath + File.separator
				+ message.namespace() + File.separator
				+ message.serviceId() + File.separator
				+ message.param(Message.PARAM_SERVICE_SRC_FILE_NAME);
		File tempFile = new File(tempFilePath);
		if (tempFile.exists()) {
			FileUtil.delete(tempFile);
		}
		tempFile.mkdirs();
		String desFilePath = destFileDirPath + File.separator
				+ message.namespace() + File.separator
				+ message.serviceId() + File.separator
				+ message.param(Message.PARAM_SERVICE_DEST_FILE_NAME);
		File desFile = new File(desFilePath);
		if (!desFile.getParentFile().exists()) {
			desFile.getParentFile().mkdirs();
		}
		update(message.namespace(), message.serviceId(), NAME,  "percent:10");
		try {
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_STORE); // 压缩方式
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST); // 压缩级别
			ZipFile srcZipFile = new ZipFile(srcFile);
			srcZipFile.extractAll(tempFilePath);
			update(message.namespace(), message.serviceId(), NAME,  "percent:50");
			ZipFile desZipFile = new ZipFile(desFilePath);
			desZipFile.setFileNameCharset("GBK");
			if (tempFile.isDirectory()) {
				File[] subFiles = tempFile.listFiles();
				for (File subFile : subFiles) {
					if (subFile.isDirectory()) {
						desZipFile.addFolder(subFile, parameters);
					} else {
						desZipFile.addFile(subFile, parameters);
					}
				}
			} 
			update(message.namespace(), message.serviceId(), NAME, "percent:90");
			FileUtil.delete(tempFile.getParentFile());
			return desZipFile.getFile();
		} catch (ZipException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new ServiceException(e.getLocalizedMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.service.Servicable#name()
	 */
	@Override
	public String name() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.service.Servicable#description()
	 */
	@Override
	public String description() {
		return DESCRIPTION;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.service.Service#check(me.chaoshoo.xservice.core.communication.Message)
	 */
	@Override
	public Boolean check(Message message) throws ServiceException {
		if (StringUtil.isNullOrEmpty(message.namespace())) {
			throw new ServiceException("Missing namesapce of rezip the service call.");
		}
		if (StringUtil.isNullOrEmpty(message.serviceId())) {
			throw new ServiceException("Missing id of the rezip service call.");
		}
		if (StringUtil.isNullOrEmpty(message.param(Message.PARAM_SERVICE_SRC_FILE_NAME))) {
			throw new ServiceException("Missing src file name of the rezip service call.");
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see me.chaoshoo.xservice.core.service.trackable.TrackableService#percentFromMessage(java.lang.String)
	 */
	@Override
	public Integer percentFromMessage(String message) throws ServiceException {
		if (null == message) {
			return null;
		}
		String src = message.substring(message.lastIndexOf(":") + 1);
		return StringUtil.parseInteger(src);
	}

}
