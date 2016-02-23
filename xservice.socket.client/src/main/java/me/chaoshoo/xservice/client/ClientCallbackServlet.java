/**
 * Copyright:
 * All right reserved
 * Created on: 2015年11月3日 下午3:49:18  
 */
package me.chaoshoo.xservice.client;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.chaoshoo.xservice.client.socket.NioSocketMasterClient;
import me.chaoshoo.xservice.core.MasterClient;
import me.chaoshoo.xservice.core.callback.Callback;
import me.chaoshoo.xservice.core.callback.CallbackException;
import me.chaoshoo.xservice.core.callback.CallbackFactory;
import me.chaoshoo.xservice.core.communication.CommunicationException;
import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.response.HttpCallbackReqponseProducer;

/**
 * @Title: ClientCallbackServlet.java
 * @Package: me.chaoshoo.xservice.client
 * @Description:
 * @author: Hu Chao
 * @date: 2015年11月3日 下午3:49:18
 * @version:
 */
public class ClientCallbackServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(ClientCallbackServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -6082082248851930175L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String callbackData = req.getParameter(HttpCallbackReqponseProducer.KEY_CALLBACK_DATA);
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> params = gson.fromJson(callbackData, type);
		String namespace = params.get(Message.PARAM_NAMESPACE);
		String serviceId = params.get(Message.PARAM_SERVICE_ID);
		String fileName = params.get(Message.PARAM_FILE_NAME);
		String className = params.get(Message.PARAM_CALLBACK_CLASS_NAME);
		log.info("Get callback message for message: {}", namespace + ":" + serviceId + ":" + fileName + ":" + className);
		try {
			MasterClient masterClient = new NioSocketMasterClient();
			File result = masterClient.getFile(namespace, serviceId, fileName);
			Callback callback = CallbackFactory.build().createCallback(className);
			Message message = new Message();
			message.params().putAll(params);
			callback.callback(namespace, serviceId, result, message);
		} catch (CallbackException | CommunicationException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
