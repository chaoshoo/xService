/**
 * Copyright:
 * All right reserved
 * Created on: 2015年11月2日 下午1:55:04  
 */
package me.chaoshoo.xservice.socket.server.slave;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chaoshoo.xservice.core.service.ServiceFactory;
import me.chaoshoo.xservice.core.service.trackable.ServiceTrackInfo;

/**
 * @Title: SlaveManagerServlet.java 
 * @Package: me.chaoshoo.xservice.socket.server.slave 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年11月2日 下午1:55:04 
 * @version:  
 */
public class SlaveManagerServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8457960171118535243L;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);//IE
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<h1>Running services:</h1>"); 
		Map<String, ServiceTrackInfo> trackMap = ServiceFactory.trackMap();
		Iterator<Entry<String, ServiceTrackInfo>> iter = trackMap.entrySet().iterator();
		response.getWriter().println("<table border=\"1\"  >");
		response.getWriter().println("<tr>");
		response.getWriter().println("<th>namespace:</th>");  
		response.getWriter().println("<th>service id:</th>"); 
		response.getWriter().println("<th>service name:</th>"); 
		response.getWriter().println("<th>service description:</th>"); 
		response.getWriter().println("<th>current finish percent:</th>");
		response.getWriter().println("<th>message:</th>"); 
		response.getWriter().println("</tr>"); 
		while (iter.hasNext()) {
			Entry<String, ServiceTrackInfo> entry = iter.next();
			ServiceTrackInfo info = entry.getValue();
				response.getWriter().println("<tr>");
				response.getWriter().println("<td>");
				response.getWriter().println(info.namespace());
				response.getWriter().println("</td>");
				response.getWriter().println("<td>");
				response.getWriter().println(info.serviceId());
				response.getWriter().println("</td>");
				response.getWriter().println("<td>");
				response.getWriter().println(info.serviceName());
				response.getWriter().println("</td>");
				response.getWriter().println("<td>");
				response.getWriter().println(info.description());
				response.getWriter().println("</td>");
				response.getWriter().println("<td>");
				response.getWriter().println(info.percent());
				response.getWriter().println("</td>");
				response.getWriter().println("<td>");
				response.getWriter().println(info.message());
				response.getWriter().println("</td>");
		}
		response.getWriter().println("</tr>");
		response.getWriter().println("</table>");
	}

}
