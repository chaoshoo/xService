/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月21日 下午2:17:55  
 */
package me.chaoshoo.xservice.core.service;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chaoshoo.xservice.core.service.trackable.ServiceTrackInfo;
import me.chaoshoo.xservice.core.service.trackable.TrackableCmdService;
import me.chaoshoo.xservice.util.CmdServiceConfig;

/**
 * @Title: ServiceFactory.java
 * @Package: me.chaoshoo.xservice.core.service
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月21日 下午2:17:55
 * @version:
 */
public class ServiceFactory {

	private static final Logger log = LoggerFactory.getLogger(ServiceFactory.class);

	private static Map<String, ServiceInfo> serviceMap = new HashMap<String, ServiceInfo>();

	private static Map<String, ServiceTrackInfo> trackMap = new HashMap<String, ServiceTrackInfo>();

	/**
	 * 获取当前运行的可追踪服务信息总数
	 * 
	 * @return
	 */
	public static Integer aliveTrackInfoSize() {
		return trackMap.size();
	}

	/**
	 * 返回可追鐘服務映射map
	 */
	public static Map<String, ServiceTrackInfo> trackMap() {
		return trackMap;
	}

	/**
	 * 移除一个可追踪服务信息
	 * 
	 * @param namespace
	 * @param serviceId
	 * @return
	 */
	public static ServiceTrackInfo removeTrackInfo(String namespace, String serviceId) {
		return trackMap.remove(ServiceTrackInfo.createKey(namespace, serviceId));
	}

	/**
	 * 获取一个可追踪服务信息
	 * 
	 * @param namespace
	 * @param serviceId
	 * @return
	 */
	public static ServiceTrackInfo trackInfo(String namespace, String serviceId) {
		return trackMap.get(ServiceTrackInfo.createKey(namespace, serviceId));
	}

	/**
	 * 增加一个可追踪服务信息
	 * 
	 * @param info
	 */
	public static void trackInfo(ServiceTrackInfo info) {
		trackMap.put(info.key(), info);
	}

	/**
	 * 获取可用的服务列表
	 * 
	 * @return
	 */
	public static List<ServiceInfo> availableServices() {
		List<ServiceInfo> result = new ArrayList<ServiceInfo>();
		Iterator<Entry<String, ServiceInfo>> iter = serviceMap.entrySet().iterator();
		while (iter.hasNext()) {
			result.add(iter.next().getValue());
		}
		return result;
	}

	/**
	 * 初始化服务工厂
	 * 
	 * @param dirPath
	 */
	public static void init() {
		String packageName = "me.chaoshoo.xservice.service";
		String packageDirName = packageName.replace(".", "/");
		try {
			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				log.debug("Package scan url: {}", url);
				String protocol = url.getProtocol();
				log.debug("Package scan protocol: {}", protocol);
				log.debug("Package scan protocol result: {}", !"file".equals(protocol) && !"jar".equals(protocol));
				if ("file".equals(protocol)) {
					String dirPath = URLDecoder.decode(url.getFile(), "UTF-8");
					File classDir = new File(dirPath);
					if (!classDir.isDirectory()) {
						continue;
					}
					for (File classFile : classDir.listFiles()) {
						log.debug("Package scan file path: {}", classFile.getAbsolutePath());
						String className = packageName + "."
								+ classFile.getName().substring(0, classFile.getName().length() - 6);
						try {
							Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(className);
							Object instance = c.newInstance();
							if (instance instanceof Service) {
								Service service = (Service) instance;
								if (service instanceof TrackableCmdService) {
									if (!CmdServiceConfig.vaild(service.name())) {
										continue;
									}
								}
								ServiceInfo info = new ServiceInfo(service.name(), className, service.description(), 0);
								serviceMap.put(info.getName(), info);
							}
						} catch (Exception e) {
							continue;
						}
					}
				} else if ("jar".equals(protocol)) {
					// 读取jar文件
					JarURLConnection conn = (JarURLConnection) url.openConnection();
					JarFile jarfile = conn.getJarFile();
					log.debug("Package scan jar file: {}", jarfile.getName());
					Enumeration<JarEntry> entries = jarfile.entries();
					while (entries.hasMoreElements()) {
						JarEntry jarEntry = (JarEntry) entries.nextElement();
						String fileName = jarEntry.getName();
						log.info("Package scan jar file class file name: {}", fileName);
						if (jarEntry.isDirectory()) {
							continue;
						}
						// .class
						if (fileName.endsWith(".class")) {
							String className = fileName.substring(0, fileName.indexOf('.')).replace('/', '.');
							if (className.startsWith(packageName)) {
								try {
									Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(className);
									Object instance = c.newInstance();
									if (instance instanceof Service) {
										Service service = (Service) instance;
										if (service instanceof TrackableCmdService) {
											if (!CmdServiceConfig.vaild(service.name())) {
												continue;
											}
										}
										ServiceInfo info = new ServiceInfo(service.name(), className,
												service.description(), 0);
										serviceMap.put(info.getName(), info);
									}
								} catch (Exception e) {
									continue;
								}
							}
						}
					}
				}

			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 返回一个服务信息
	 * 
	 * @param serviceName
	 * @return
	 */
	public static ServiceInfo serviceInfo(String serviceName) {
		return serviceMap.get(serviceName);
	}

	/**
	 * 创建一个服务实例
	 * 
	 * @param name
	 * @return
	 * @throws ServiceException
	 */
	public static Service createService(String name, String srcFileDirPath, String tempFileDirPath,
			String destFileDirPath) throws ServiceException {
		ServiceInfo info = serviceMap.get(name);
		try {
			Service service = (Service) (ServiceFactory.class.getClassLoader().loadClass(info.getClassName())
					.newInstance());
			service.srcFileDirPath(srcFileDirPath).tempFileDirPath(tempFileDirPath).destFileDirPath(destFileDirPath);
			return service;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			log.error(e.getLocalizedMessage(), e);
			throw new ServiceException(e.getLocalizedMessage(), e);
		}
	}

}
