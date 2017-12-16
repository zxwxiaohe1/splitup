package com.airodlcx;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

//import org.apache.commons.beanutils.BeanUtils;

/**
 * Servlet implementation class UploadVideo
 */
public class CheckChumServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckChumServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		 String fileName = request.getParameter("fileName");  
		 String fileMd5 = request.getParameter("fileMd5");    
	        String chunk = request.getParameter("chunk");    
	        String chunkSize = request.getParameter("chunkSize");  
	        String guid = request.getParameter("guid");
			
	        String path = request.getSession().getServletContext().getRealPath("/upload");
	        File checkFile = new File(path+"/"+guid+"/"+chunk);  
	        
            response.setContentType("text/html;charset=utf-8");    
            //检查文件是否存在，且大小是否一致    
            if(checkFile.exists() && checkFile.length()==Integer.parseInt(chunkSize)){    
                //上传过    
                try {  
                    response.getWriter().write("{\"ifExist\":1}");  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }    
            }else{    
                //没有上传过    
                try {  
                    response.getWriter().write("{\"ifExist\":0}");  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }    
            }    
	}
}
