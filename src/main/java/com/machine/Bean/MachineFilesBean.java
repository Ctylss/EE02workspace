package com.machine.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MachineFilesBean {
    private int fileId;
    private String fileName;
    private String filePath;
    private int machineId;
    private LocalDateTime uploadTime;
    
    // 可選的擴充屬性 (如果需要顯示機台資訊)
    private String machineName;
    private String serialNumber;
    
    // 建構子
    public MachineFilesBean() {}
    
    public MachineFilesBean(int fileId, String fileName, String filePath, int machineId, LocalDateTime uploadTime) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.machineId = machineId;
        this.uploadTime = uploadTime;
    }
    
    // === 格式化方法 (給 JSP 使用) ===
    
    /**
     * 為 JSP 提供格式化的上傳時間字串 (yyyy-MM-dd HH:mm)
     */
    public String getFormattedUploadTime() {
        if (uploadTime == null) {
            return "";
        }
        return uploadTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    /**
     * 為 JSP 提供詳細格式化的上傳時間字串 (yyyy年MM月dd日 HH:mm:ss)
     */
    public String getDetailedUploadTime() {
        if (uploadTime == null) {
            return "";
        }
        return uploadTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
    }
    
    /**
     * 為 JSP 提供簡單日期格式 (yyyy-MM-dd)
     */
    public String getUploadDate() {
        if (uploadTime == null) {
            return "";
        }
        return uploadTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    /**
     * 為 JSP 提供時間格式 (HH:mm:ss)
     */
    public String getUploadTimeOnly() {
        if (uploadTime == null) {
            return "";
        }
        return uploadTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    
    // === 標準 Getter 和 Setter ===
    
    public int getFileId() {
        return fileId;
    }
    
    public void setFileId(int fileId) {
        this.fileId = fileId;
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
    
    public int getMachineId() {
        return machineId;
    }
    
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
    
    public LocalDateTime getUploadTime() {
        return uploadTime;
    }
    
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }
    
    // === 擴充屬性 (機台資訊) ===
    
    public String getMachineName() {
        return machineName;
    }
    
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }
    
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    // === 輔助方法 ===
    
    /**
     * 取得檔案副檔名
     */
    public String getFileExtension() {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
    
    /**
     * 檢查是否為圖片檔案
     */
    public boolean isImageFile() {
        String ext = getFileExtension();
        return ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || 
               ext.equals("gif") || ext.equals("bmp") || ext.equals("webp");
    }
    
    /**
     * 檢查是否為文件檔案
     */
    public boolean isDocumentFile() {
        String ext = getFileExtension();
        return ext.equals("pdf") || ext.equals("doc") || ext.equals("docx") || 
               ext.equals("xls") || ext.equals("xlsx") || ext.equals("ppt") || 
               ext.equals("pptx") || ext.equals("txt");
    }
    
    /**
     * 檢查是否為影片檔案
     */
    public boolean isVideoFile() {
        String ext = getFileExtension();
        return ext.equals("mp4") || ext.equals("avi") || ext.equals("mov") || 
               ext.equals("wmv") || ext.equals("flv") || ext.equals("mkv");
    }
    
    /**
     * 取得檔案類型圖示 (用於前端顯示)
     */
    public String getFileTypeIcon() {
        if (isImageFile()) {
            return "🖼️";
        } else if (isDocumentFile()) {
            return "📄";
        } else if (isVideoFile()) {
            return "🎬";
        } else {
            return "📁";
        }
    }
    
    // === toString 方法 (除錯用) ===
    
    @Override
    public String toString() {
        return "MachineFilesBean{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", machineId=" + machineId +
                ", uploadTime=" + uploadTime +
                ", machineName='" + machineName + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                '}';
    }
    
    // === equals 和 hashCode 方法 ===
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MachineFilesBean that = (MachineFilesBean) obj;
        return fileId == that.fileId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(fileId);
    }
}