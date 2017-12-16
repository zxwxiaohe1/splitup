package com.airodlcx;

public class FileUploadMessageModel {
	/**文件已经上传大小*/
	private long fileUploadSize;
	/**文件总大小大小*/
	private long fileUploadTotalSize;
	/**文件上传状态*/
	private String flag;
	/**反馈信息*/
	private String message;
	
	public void setFileUploadSize(int fileUploadSize) {
		this.fileUploadSize = fileUploadSize;
	}
	public void setFileUploadTotalSize(int fileUploadTotalSize) {
		this.fileUploadTotalSize = fileUploadTotalSize;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getFileUploadSize() {
		return fileUploadSize;
	}
	public void setFileUploadSize(long fileUploadSize) {
		this.fileUploadSize = fileUploadSize;
	}
	public long getFileUploadTotalSize() {
		return fileUploadTotalSize;
	}
	public void setFileUploadTotalSize(long fileUploadTotalSize) {
		this.fileUploadTotalSize = fileUploadTotalSize;
	}
}
