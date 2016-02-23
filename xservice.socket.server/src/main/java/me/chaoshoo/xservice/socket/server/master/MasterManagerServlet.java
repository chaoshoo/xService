/**
 * Copyright:
 * All right reserved
 * Created on: 2015年11月2日 上午10:38:22  
 */
package me.chaoshoo.xservice.socket.server.master;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chaoshoo.xservice.core.Slave;
import me.chaoshoo.xservice.core.service.ServiceInfo;

/**
 * @Title: MastetManagerServlet.java
 * @Package: me.chaoshoo.xservice.socket.server.master
 * @Description:
 * @author: Hu Chao
 * @date: 2015年11月2日 上午10:38:22
 * @version:
 */
public class MasterManagerServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -102390270722331100L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);//IE
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<h1>Running slaves:</h1>"); 
		List<Slave> slaves = NioSocketMasterServer.master.slaves();
		for (Slave slave : slaves) {
			response.getWriter().println("<h3>" + slave.ip() + ":" + slave.port() + "</h3>");  
			response.getWriter().println("<table border=\"1\"  >");
			response.getWriter().println("<tr>");
			response.getWriter().println("<th>service name:</th>");  
			response.getWriter().println("<th>service description:</th>"); 
			response.getWriter().println("<th>running count:</th>");
			response.getWriter().println("</tr>"); 
			List<ServiceInfo> infos = slave.serviceList();
			for (ServiceInfo info : infos) {
				response.getWriter().println("<tr>");
				response.getWriter().println("<td>");
				response.getWriter().println(info.getName());
				response.getWriter().println("</td>");
				response.getWriter().println("<td>");
				response.getWriter().println(info.getDescription());
				response.getWriter().println("</td>");
				response.getWriter().println("<td>");
				response.getWriter().println(info.runningCount());
				response.getWriter().println("</td>");
				response.getWriter().println("</tr>");
			}
			response.getWriter().println("</table>");
		}
	}

}
