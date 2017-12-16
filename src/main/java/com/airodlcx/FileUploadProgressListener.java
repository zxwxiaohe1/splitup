package com.airodlcx;

import org.apache.commons.fileupload.ProgressListener;

import javax.servlet.http.HttpSession;

public class FileUploadProgressListener implements ProgressListener {
	private HttpSession session;

	public FileUploadProgressListener() {  }  
	
    public FileUploadProgressListener(HttpSession session) {
        this.session=session;  
        FileUploadMessageModel status = new FileUploadMessageModel();
        session.setAttribute("upload_ps", status);  
    }  
	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
		FileUploadMessageModel status = (FileUploadMessageModel) session.getAttribute("upload_ps");
		status.setFileUploadTotalSize(pContentLength/10000);
		status.setFileUploadSize(pBytesRead/10000);
		System.out.println("pBytesRead,pBytesRead==="+pBytesRead+","+pContentLength);
		session.setAttribute("upload_ps", status);
	}

}
