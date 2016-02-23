/**
 * Copyright:
 * All right reserved
 * Created on: 2015年7月14日 下午5:55:33  
 */
package me.chaoshoo.xservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * @Title: FileUtil.java
 * @Package: me.chaoshoo.storage.util
 * @Description:
 * @author: Hu Chao
 * @date: 2015年7月14日 下午5:55:33
 * @version:
 */
public class FileUtil {

	private static byte[] createChecksum(File file) throws Exception {
		InputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[1024 * 1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	private static String createMD5FromByteChecksum(byte[] bytes) {
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	
	/**
	 * 生成文件md5字符串
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String getMD5Checksum(File file) throws Exception {
		if (file.isDirectory()) {
			StringBuffer buffer = new StringBuffer();
			File[] children = file.listFiles();
			for (File child : children) {
				if (child.isDirectory()) {
					buffer.append(getMD5Checksum(child));
				} else {
					byte[] b = createChecksum(child);
					buffer.append(createMD5FromByteChecksum(b));
				}
			}
			return StringUtil.MD5(buffer.toString());
		} else {
			return createMD5FromByteChecksum(createChecksum(file));
		}
	}

	private static File copyFile(File resFile, File desFile, Boolean isOverWrite) throws IOException {
		if (!resFile.exists()) {
			return null;
		}
		if (desFile.exists() && !isOverWrite) {
			return null;
		} else if (desFile.exists() && !isOverWrite) {
			desFile.delete();
		} else {
			desFile.getParentFile().mkdirs();
		}
		FileInputStream fis = new FileInputStream(resFile);
		FileChannel inputChannel = fis.getChannel();
		FileOutputStream fos = new FileOutputStream(desFile);
		FileChannel outputChannel = fos.getChannel();
		ByteBuffer fileBuffer = ByteBuffer.allocate(1024 * 1024);
		while (inputChannel.read(fileBuffer) >= 0) {
			fileBuffer.flip();
			outputChannel.write(fileBuffer);
			fileBuffer.clear();
		}
		outputChannel.close();
		fos.close();
		inputChannel.close();
		fis.close();
		return desFile;
	}

	private static File copyToDir(File resFile, File desDir, Boolean isOverWrite) throws IOException {
		if (desDir.exists() && !desDir.isDirectory()) {
			return null;
		}
		return copyFile(resFile, new File(desDir.getAbsolutePath() + File.separator + resFile.getName()),
				isOverWrite);
	}
	
	private static File copyDir(File resFile, File desFile, Boolean isOverWrite) throws IOException {
		if (!resFile.isDirectory()) {
			return null;
		}
		if (desFile.exists() && !desFile.isDirectory()) {
			return null;
		}
		if (!desFile.exists()) {
			desFile.mkdirs();
		}
		for (File file : resFile.listFiles()) {
			if (file.isFile()) {
				copyToDir(file, desFile, isOverWrite);
			} else if (file.isDirectory()) {
				copyDir(file, new File(desFile.getAbsolutePath() + File.separator + file.getName()),
						isOverWrite);
			}
		}
		return desFile;
	}

	/**
	 * 复制文件或文件夹，isOverWrite参数确认是否覆盖。
	 * resFile参数是文件，desFile参数不存在或是文件时，覆盖。desFile参数是文件夹时，复制入文件夹。
	 * resFile参数是文件夹，则复制文件夹至desFile参数指定的位置。
	 * @param resFile
	 * @param desFile
	 * @param isOverWrite
	 * @return
	 * @throws IOException
	 */
	public static File copy(File resFile, File desFile, Boolean isOverWrite) throws IOException {
		if (!resFile.exists()) {
			return null;
		}
		if (resFile.isFile() && !desFile.exists()) {
			return copyFile(resFile, desFile, isOverWrite);
		} else if (resFile.isFile() && desFile.isFile()) {
			return copyFile(resFile, desFile, isOverWrite);
		} else if (resFile.isFile() && desFile.isDirectory()) {
			return copyToDir(resFile, desFile, isOverWrite);
		} else if (resFile.isDirectory()) {
			return copyDir(resFile, desFile, isOverWrite);
		}
		return null;
	}

	/**
	 * 复制文件或文件夹，覆盖原有文件。
	 * resFile参数是文件，desFile参数不存在或是文件时，覆盖。desFile参数是文件夹时，复制入文件夹。
	 * resFile参数是文件夹，则复制文件夹至desFile参数指定的位置。
	 * @param resFile
	 * @param desFile
	 * @param isOverWrite
	 * @return
	 * @throws IOException
	 */
	public static File copy(File resFile, File desFile) throws IOException {
		return copy(resFile, desFile, true);
	}

	/**
	 * 清空指定文件夹
	 * @param desDir
	 * @return
	 */
	public static Boolean deleteFileInDir(File desDir) {
		if (!desDir.isDirectory()) {
			return true;
		}
		for (File file : desDir.listFiles()) {
			if (file.isDirectory()) {
				deleteFileInDir(file);
			}
			file.delete();
		}
		return true;
	}
	
	/**
	 * 删除文件或文件夹
	 * @param file
	 * @return
	 */
	public static Boolean delete(File file) {
		if (file.isDirectory()) {
			deleteFileInDir(file);
		}
		return file.delete();
	}

	private static File unZipSubFile(String srcZipFilePath, String subFilePath, String desFilePath) throws IOException {
		File desFile = new File(desFilePath);
		if (!desFile.exists()) {
			desFile.getParentFile().mkdirs();
		}
		try {
			ZipFile zipFile = new ZipFile(srcZipFilePath);  
			zipFile.setFileNameCharset("GBK");      
			zipFile.extractFile(subFilePath, desFilePath);
		} catch (ZipException ze) {
			throw new IOException(ze.getMessage());
		}
		return desFile;
	}

	private static File unZipFile(String srcZipFilePath, String desDirPath) throws IOException {
		File desDir = new File(desDirPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		try {
			ZipFile zipFile = new ZipFile(srcZipFilePath); 
			zipFile.setFileNameCharset("GBK");
			zipFile.extractAll(desDirPath);
		} catch (ZipException ze) {
			throw new IOException(ze.getMessage());
		}
		return desDir;
	}

	/**
	 * 解压文件，将srcZipFilePath参数指定的压缩文件中由subFilePath参数作为相对路径指定的子文件解压值desFilePath参数指定的路径目录下。
	 * 目录结构说明如下：假设srcZipFilePath参数zip文件根目录下有1.txt文件，desFilePath参数指定路径为c:\temp,则1.txt解压值c:\temp\1.txt。
	 * @param srcZipFilePath
	 * @param subFilePath
	 * @param desFilePath
	 * @return
	 * @throws IOException
	 */
	public static File unZip(String srcZipFilePath, String subFilePath, String desFilePath) throws IOException {
		if (null == subFilePath) {
			return unZipFile(srcZipFilePath, desFilePath);
		} else {
			return unZipSubFile(srcZipFilePath, subFilePath, desFilePath);
		}
	}

	/**
	 * 解压文件，将srcZipFilePath参数指定的压缩文件解压值desFilePath参数指定的路径目录下
	 * 目录结构说明如下：假设srcZipFilePath参数zip文件根目录下有1.txt文件，desFilePath参数指定路径为c:\temp,则1.txt解压值c:\temp\1.txt
	 * @param srcZipFilePath
	 * @param subFilePath
	 * @param desFilePath
	 * @return
	 * @throws IOException
	 */
	public static File unZip(String srcZipFilePath, String desFilePath) throws IOException {
		return unZipFile(srcZipFilePath, desFilePath);
	}

	/**
	 * 压缩文件，将srcFilePath参数指定的文件压缩至desFilePath参数指定的压缩文件下
	 * 目录结构说明如下：假设srcFilePath参数参数指定路径为c:\temp，含有子文件c:\temp\1.txt，则压缩文件只含有1.txt
	 * @param srcFilePath
	 * @param zipFilePath
	 * @throws IOException
	 */
	public static void zip(String srcFilePath, String desFilePath) throws IOException {
		File srcFile = new File(srcFilePath);
		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_STORE); // 压缩方式
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST); // 压缩级别
		try {
			ZipFile zipFile = new ZipFile(desFilePath);
			zipFile.setFileNameCharset("GBK");
			if (srcFile.isDirectory()) {
				File[] subFiles = srcFile.listFiles();
				for (File subFile : subFiles) {
					if (subFile.isDirectory()) {
						zipFile.addFolder(subFile, parameters);
					} else {
						zipFile.addFile(subFile, parameters);
					}
				}
			} else {
				zipFile.addFile(srcFile, parameters);
			}
		} catch (ZipException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	public static Boolean checkFileChecksum(File file, String desMd5FilePath) {
		if (null == file || !file.exists()) {
			return false;
		}
		if (!file.isFile()) {
			return false;
		} else {
			File md5File = new File(desMd5FilePath);
			if (!md5File.exists()) {
				return false;
			}
			try {
				String fileChecksum = FileUtil.getMD5Checksum(file);
				ByteBuffer buffer = ByteBuffer.allocate(32);
				FileInputStream fis = new FileInputStream(md5File);
				FileChannel fileChannel = fis.getChannel();
				int size = fileChannel.read(buffer);
				if (size >= 0) {
					buffer.flip();
					byte[] bytes = new byte[size];
					buffer.get(bytes);
					String checksum = new String(bytes);
					if (!fileChecksum.equals(checksum)) {
						fileChannel.close();
						fis.close();
						return false;
					}
					buffer.clear();
				}
				fileChannel.close();
				fis.close();
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

}
