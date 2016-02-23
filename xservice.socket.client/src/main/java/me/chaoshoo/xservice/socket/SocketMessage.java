/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月15日 下午3:54:00  
 */
package me.chaoshoo.xservice.socket;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.chaoshoo.xservice.core.communication.Command;
import me.chaoshoo.xservice.core.communication.Communicatable;
import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.communication.Message;

/**
 * @Title: SocketMessage.java
 * @Package: me.chaoshoo.xservice.core.protocol.message.socket
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月15日 下午3:54:00
 * @version:
 */
public class SocketMessage implements Communicatable {

	private static final Logger log = LoggerFactory.getLogger(SocketMessage.class);

	protected SocketChannel socketChannel;

	/**
	 * @param socketChannel
	 */
	public SocketMessage(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	/**
	 * 把message写入socket channel
	 */
	public void write(Message message) throws IOException, CommunicationException {
		if (null == this.socketChannel) {
			throw new CommunicationException("The channel of a message could not be null!");
		}
		if (null == message.command()) {
			throw new CommunicationException("The command of a message could not be null!");
		}
		message.buffer().putInt(message.command().value());// 命令名
		Gson gson = new Gson();
		String paramsJson = gson.toJson(message.params());
		message.buffer().putInt(paramsJson.getBytes().length);// 参数json序列化长度
		message.buffer().put(paramsJson.getBytes());// 参数json序列化
		// 如果不是发送文件，这里就可以返回了，如果要发送文件，则继续添加文件内容
		if (!message.command().equals(Command.SEND_FILE)) {
			message.buffer().flip();
			socketChannel.write(message.buffer());
			message.buffer().clear();
			return;
		}

		// 发送文件时往buffer写入文件片段
		if (null == message.file()) {
			throw new CommunicationException("The file of a send file message could not be null!");
		}
		if (null == message.param(Message.PARAM_FILE_NAME)) {
			throw new CommunicationException("The file name of a send file message could not be null!");
		}
		if (null == message.param(Message.PARAM_FILE_SIZE)) {
			throw new CommunicationException("The file size of a send file message could not be null!");
		}
		if (null == message.param(Message.PARAM_FILE_POSITION)) {
			throw new CommunicationException("The file position of a send file message could not be null!");
		}
		FileInputStream fis = null;
		FileChannel fileChannel = null;
		try {
			fis = new FileInputStream(message.file());
			fileChannel = fis.getChannel();
			fileChannel.position(Long.parseLong(message.param(Message.PARAM_FILE_POSITION)));
			fileChannel.read(message.buffer());
			message.buffer().flip();
			socketChannel.write(message.buffer());
			message.buffer().clear();
		} catch (Exception e) {
			throw new CommunicationException("Exception happens when create file byte buffer: "
					+ e.getLocalizedMessage(), e);
		} finally {
			try {
				if (null != fileChannel) {
					fileChannel.close();
				}
				if (null != fis) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 从socket channel读取message，如果是发送文件的message，将文件片段写入src文件夹
	 */
	public Message read() throws IOException, CommunicationException {
		Message message = new Message();
		if (null == this.socketChannel) {
			throw new CommunicationException("The channel of a message could not be null!");
		}

		while(socketChannel.read(message.buffer()) > 0) {
			log.debug("Message buffer info: {}", message.buffer());
		}
		message.buffer().flip();

		// 参数json序列化长度
		int paramsJsonLength = 0;

		byte[] bytes = null;

		message.command(Command.valueOf(message.buffer().getInt()));
		if (null == message.command()) {
			throw new CommunicationException("The command of a message could not be null!");
		}

		paramsJsonLength = message.buffer().getInt();
		bytes = new byte[paramsJsonLength];
		message.buffer().get(bytes);
		String paramsJson = new String(bytes);
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> params = gson.fromJson(paramsJson, type);
		message.params(params);

		return message;
	}
}
