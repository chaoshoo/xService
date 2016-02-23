/**
 * Copyright:
 * All right reserved
 * Created on: 2015年10月19日 下午2:22:00  
 */
package me.chaoshoo.xservice.response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import me.chaoshoo.xservice.core.communication.Message;
import me.chaoshoo.xservice.core.communication.ResponseProducer;

/**
 * @Title: HtmlCallbackReqponse.java
 * @Package: me.chaoshoo.xservice.response
 * @Description:
 * @author: Hu Chao
 * @date: 2015年10月19日 下午2:22:00
 * @version:
 */
public class HttpCallbackReqponseProducer implements ResponseProducer {
	
	private static final Logger log = LoggerFactory.getLogger(HttpCallbackReqponseProducer.class);

	private String callBackUrl;

	/**
	 * @param callBackUrl
	 */
	public HttpCallbackReqponseProducer(String callBackUrl) {
		super();
		this.callBackUrl = callBackUrl;
	}

	public static final String KEY_CALLBACK_DATA = "callback_data";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.chaoshoo.xservice.core.communication.ResponseProducer#response(java.
	 * util.Map)
	 */
	public void response(Message message) {
		Gson gson = new Gson();
		String paramsJson = gson.toJson(message.params());
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost(callBackUrl);
			List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
			httpParams.add(new BasicNameValuePair(KEY_CALLBACK_DATA, paramsJson));
			httppost.setEntity(new UrlEncodedFormEntity(httpParams, StandardCharsets.UTF_8));
			httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			log.error(e.getLocalizedMessage(), e);
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error(e.getLocalizedMessage(), e);
			}
		}
	}

}
