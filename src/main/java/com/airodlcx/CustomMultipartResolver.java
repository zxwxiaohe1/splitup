package com.airodlcx;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


public class CustomMultipartResolver extends CommonsMultipartResolver {
	private HttpServletRequest request;
	
	@Override
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
        ServletFileUpload upload = new ServletFileUpload(fileItemFactory);  
        upload.setSizeMax(-1);  
        if (request != null) {  
        	HttpSession session = request.getSession();
        	FileUploadProgressListener progressListener = new FileUploadProgressListener(session);
            upload.setProgressListener(progressListener);  
        }  
        return upload;  
    }

	@Override
	public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request)
			throws MultipartException {
		this.request = request;
		return super.resolveMultipart(request);
	}

	@Override
	public MultipartParsingResult parseRequest(HttpServletRequest request)
			throws MultipartException {
		HttpSession session = request.getSession(); 
		String encoding = determineEncoding(request);
		FileUpload fileUpload = prepareFileUpload(encoding);
		FileUploadProgressListener progressListener = new FileUploadProgressListener(session);
		fileUpload.setProgressListener(progressListener);  
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			return parseFileItems(fileItems, encoding);
		} catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
		} catch (FileUploadException ex) {
			throw new MultipartException("Could not parse multipart servlet request", ex);
		}
	} 
	
}	
