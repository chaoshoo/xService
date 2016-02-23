/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月15日 下午3:16:39  
 */
package me.chaoshoo.xservice.core.communication;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @Title: Message.java 
 * @Package: me.chaoshoo.xservice.core 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年10月15日 下午3:16:39 
 * @version:  
 */
public class Message {

	private static final Integer MESSAGE_BUFFER_SIZE = 1024 * 1024;
	
	public static final String COMMA = ",";
	
	public static final String SEMICOLON = ";";
	
	// for update slave info
	public static final String PARAM_SLAVE_IP = "slave_ip";
	
	public static final String PARAM_SLAVE_PORT = "slave_port";
	
	public static final String PARAM_SERVICE_LIST = "service_list";
	
	public static final String PARAM_SERVICE_ALIVE = "service_alive";
	
	// for service call
	public static final String PARAM_SERVICE_NAME = "service_name";

	public static final String PARAM_SERVICE_SRC_FILE_NAME= "src_file_name";

	public static final String PARAM_SERVICE_DEST_FILE_NAME= "dest_file_name";

	// for cmd service call
	public static final String PARAM_CMD_COMMAND_PARAM= "cmd_command_param";
	
	// for service status track
	public static final String PARAM_SERVICE_PERCENT = "service_percent";
	
	public static final String PARAM_SERVICE_MESSAGE = "service_messsage";
	
	public static final String PARAM_SERVICE_DESCRIPTION = "service_description";
	
	// for send file
	public static final String PARAM_FILE_NAME = "file_name";

	public static final String PARAM_FILE_SIZE = "file_size";

	public static final String PARAM_FILE_POSITION = "file_position";
	
	// for response
	public static final String PARAM_HANDLE_RESULT = "handle_result";
	
	public static final String HANDLE_RESULT_UNKNOW = "unknow";
	
	public static final String HANDLE_RESULT_SUCCESS = "success";
	
	public static final String HANDLE_RESULT_UN_SUCCESS = "unsuccess";
	
	public static final String HANDLE_RESULT_EXCEPTION = "exception";
	
	public static final String HANDLE_RESULT_EXCEPTION_MESSAGE = "exception_message";

	// for callback type definition
	public static final String PARAM_CALLBACK_TYPE = "callback_type";

	public static final String PARAM_CALLBACK_CLASS_NAME= "callback_class_name";

	public static final String CALLBACK_TYPE_HTTP = "_http_";
	
	public static final String CALLBACK_HTTP_URL = "callback_http_url";
	
	public static final String CALLBACK_TYPE_MESSAGE = "_message_";
	
	public static final String CALLBACK_SOCKET_IP= "callback_socket_ip";

	public static final String CALLBACK_SOCKET_PORT= "callback_socket_port";
	
	// forl sync type definition
	public static final String PARAM_SYNC_TYPE = "sync_type";
	
	public static final String SYNC_TYPE_SYNC = "sync";
	
	public static final String SYNC_TYPE_ASYNC = "async";

	// for general
	public static final String PARAM_NAMESPACE = "namespace";

	public static final String PARAM_SERVICE_ID = "service_id";
	
	protected Map<String, String> params;
	
	protected Command command;

	protected File file;

	protected ByteBuffer buffer;

	/**
	 * 
	 */
	public Message() {
		this.params = new HashMap<String, String>();
		this.buffer = ByteBuffer.allocate(MESSAGE_BUFFER_SIZE);
	}

	/**
	 * @param namespace
	 * @param messageId
	 * @param command
	 */
	public Message(String namespace, Command command) {
		super();
		this.command = command;
		this.params = new HashMap<String, String>();
		this.buffer = ByteBuffer.allocate(MESSAGE_BUFFER_SIZE);
		this.param(PARAM_NAMESPACE, namespace);
	}

	/**
	 * @return the namespace
	 */
	public String namespace() {
		return this.param(PARAM_NAMESPACE);
	}

	/**
	 * @param namespace the namespace to set
	 */
	public Message namespace(String namespace) {
		this.param(PARAM_NAMESPACE, namespace);
		return this;
	}

	/**
	 * @return the serviceId
	 */
	public String serviceId() {
		return this.param(PARAM_SERVICE_ID);
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public Message serviceId(String messageId) {
		this.param(PARAM_SERVICE_ID, messageId);
		return this;
	}

	/**
	 * @return the command
	 */
	public Command command() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public Message command(Command command) {
		this.command = command;
		return this;
	}

	public Message param(String key, String value){
		if (null == this.params) {
			this.params = new HashMap<String, String>();
		}
		params.put(key, value);
		return this;
	}
	
	public String param(String key) {
		if (null == this.params) {
			return null;
		}
		return this.params.get(key);
	}
	
	public Map<String, String> params() {
		return this.params;
	}
	
	public Message params(Map<String, String> params) {
		if (null == params) {
			return this;
		}
		Set<Entry<String, String>> set=params.entrySet();
        Iterator<Entry<String, String>> it=set.iterator();
        while(it.hasNext()){
            Entry<String, String> entry = it.next();
        	this.params.put(entry.getKey(), entry.getValue());
        }
		return this;
	}
	
	public Message file(File file) {
		if (null != file) {
			this.file = file;
			this.param(PARAM_FILE_NAME, this.file.getName());
			this.param(PARAM_FILE_SIZE, String.valueOf(this.file.length()));
		}
		return this;
	}
	
	public File file() {
		return this.file;
	}

	public Message filePosition(Long position) {
		if (null != this.file) {
			this.param(PARAM_FILE_POSITION, String.valueOf(position));
		}
		return this;
	}
	
	public ByteBuffer buffer() {
		return this.buffer;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("command=").append(this.command).append(";");
		sb.append("params=[");
		Iterator<Entry<String, String>> iter = this.params.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
		}
		sb.append("]");
		sb.append(super.toString());
		return sb.toString();
	}
	
}
