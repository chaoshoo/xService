/**
 * Copyright:
 * All right reserved
 * Created on: 2015年11月2日 下午2:28:24  
 */
package me.chaoshoo.xservice.client;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import me.chaoshoo.xservice.client.socket.NioSocketMasterClient;
import me.chaoshoo.xservice.core.MasterClient;
import me.chaoshoo.xservice.util.StringUtil;
import me.chaoshoo.xservice.util.XserviceClientConfig;

/**
 * @Title: ClientManagerServlet.java 
 * @Package: me.chaoshoo.xservice.client 
 * @Description: 
 * @author: Hu Chao
 * @date: 2015年11月2日 下午2:28:24 
 * @version:  
 */
public class ClientManagerServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4426446694561644660L;
	
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
		response.getWriter().println("<html>"); 
		response.getWriter().println("<head>"); 
		response.getWriter().println("</head>"); 
		response.getWriter().println("<body>"); 
		response.getWriter().println("<form action=\"/client\" enctype=\"multipart/form-data\" method=\"post\" >");
		response.getWriter().println("用户名：<input type=\"text\" name=\"namespace\"> <br/>  "); 
		response.getWriter().println("服务名：<input type=\"text\" name=\"serviceName\"> <br/>  "); 
		response.getWriter().println("上传文件：<input type=\"file\" name=\"file1\"><br/>  "); 
		response.getWriter().println("输出文件名：<input type=\"text\" name=\"output\"> <br/>  "); 
		response.getWriter().println("参数：<input type=\"text\" name=\"param\"> <br/>  "); 
		response.getWriter().println("参数：<input type=\"submit\" name=\"submit\"> <br/>  "); 
		response.getWriter().println("</form>"); 
		response.getWriter().println("</body>"); 
		response.getWriter().println("</html>"); 
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setDateHeader("Expires", -1);//IE
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
		response.setStatus(HttpServletResponse.SC_OK);
		
		String namespace = null;
		String serviceName = null;
		String output = null;
		String param = "";
		File file = null;
		request.setCharacterEncoding("utf-8");  //设置编码  
        //获得磁盘文件条目工厂  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        //获取文件需要上传到的路径  
        String path = XserviceClientConfig.tempFileDir();  
        factory.setRepository(new File(path));  
        factory.setSizeThreshold(1024*1024) ;  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        try {  
            List<FileItem> list = (List<FileItem>)upload.parseRequest(request);  
            for(FileItem item : list)  
            {  
                //获取表单的属性名字  
                String name = item.getFieldName();  
                //如果获取的 表单信息是普通的 文本 信息  
                if(item.isFormField())  
                {                 
                	if ("namespace".equals(name)) {
						namespace = item.getString();
					}
                	if ("serviceName".equals(name)) {
                		serviceName = item.getString();
					}
                	if ("output".equals(name)) {
                		output = item.getString();
					}
                	if ("param".equals(name)) {
                		param = item.getString();
					}
                }  
                //对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些  
                else  
                {  
                	DiskFileItem diskItem = (DiskFileItem)item;
                	file = diskItem.getStoreLocation();
                	if (StringUtil.isNullOrEmpty(output)) {
                		output = file.getName();
					}
                }  
            }  
            if (StringUtil.isNullOrEmpty(namespace)) {
        		response.getWriter().println("<h1>namespace could not be null or empty!</h1>"); 
			} 
            if (StringUtil.isNullOrEmpty(serviceName)) {
        		response.getWriter().println("<h1>serviceName could not be null or empty!</h1>"); 
			} 
            if (null == file || !file.exists()) {
        		response.getWriter().println("<h1>file could not be null or empty!</h1>"); 
			}
            String serviceId = String.valueOf(System.currentTimeMillis());
            MasterClient masterClient = new NioSocketMasterClient();
            masterClient.callService(namespace, serviceId, serviceName, file, output, param, "");
            response.getWriter().println("<h1>service: " + serviceId + " has been submitted!</h1>"); 
        } catch (FileUploadException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        catch (Exception e) {  
            // TODO Auto-generated catch block  
              
            //e.printStackTrace();  
        }  
	}

}
