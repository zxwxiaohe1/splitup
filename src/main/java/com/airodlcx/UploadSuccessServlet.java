package com.airodlcx;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UploadSuccessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadSuccessServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getSession().getServletContext().getRealPath("/upload");

		String guid = request.getParameter("guid");

		String fileName = request.getParameter("fileName");
		
		System.out.println("start...!guid="+guid+";fileName="+fileName);
		/**
		 * 进行文件合并
		 */
		File file = new File(path+"/"+guid);

		new File("E://upload"+"/"+guid).mkdirs();
		/**
		 * 进行文件合并
		 */
		File newFile = new File("E://upload"+"/"+guid+"/"+fileName);
		FileOutputStream outputStream = new FileOutputStream(newFile, true);//文件追加写入
		
		byte[] byt = new byte[10*1024*1024];
		int len;
		FileInputStream temp = null;//分片文件
		File[] childs = new File(path+"/"+guid).listFiles();
		for(int i = 0 ; i<childs.length ; i++){
			temp = new FileInputStream(childs[i]);
			while((len = temp.read(byt))!=-1){
				//System.out.println(len);
				outputStream.write(byt, 0, len);
			}
		}
		/**
		 * 当所有追加写入都写完  才可以关闭流
		 */
		outputStream.flush();
		outputStream.close();
		temp.close();

		System.out.println("success!guid="+guid+";fileName="+fileName);
		
	}

}
