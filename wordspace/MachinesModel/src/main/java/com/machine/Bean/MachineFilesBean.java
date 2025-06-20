package com.machine.Bean;

import java.time.LocalDateTime;

public class MachineFilesBean {
	 private int fileId;               // 檔案ID
	    private int machineId;            // 所屬機器ID
	    private String fileName;          // 檔案名稱
	    private String filePath;          // 儲存路徑
	    private LocalDateTime uploadTime; // 上傳時間
		public int getFileId() {
			return fileId;
		}
		public void setFileId(int fileId) {
			this.fileId = fileId;
		}
		public int getMachineId() {
			return machineId;
		}
		public void setMachineId(int machineId) {
			this.machineId = machineId;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public LocalDateTime getUploadTime() {
			return uploadTime;
		}
		public void setUploadTime(LocalDateTime uploadTime) {
			this.uploadTime = uploadTime;
		}
		public MachineFilesBean(int fileId, int machineId, String fileName, String filePath, LocalDateTime uploadTime) {
			super();
			this.fileId = fileId;
			this.machineId = machineId;
			this.fileName = fileName;
			this.filePath = filePath;
			this.uploadTime = uploadTime;
		}
		public MachineFilesBean(String fileName, String filePath, LocalDateTime uploadTime) {
			super();
			this.fileName = fileName;
			this.filePath = filePath;
			this.uploadTime = uploadTime;
		}
		public MachineFilesBean(int machineId, String fileName, String filePath, LocalDateTime uploadTime) {
			super();
			this.machineId = machineId;
			this.fileName = fileName;
			this.filePath = filePath;
			this.uploadTime = uploadTime;
		}
	    
	    
}
