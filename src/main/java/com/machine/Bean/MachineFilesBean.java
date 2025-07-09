package com.machine.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "machine_files")
public class MachineFilesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private int fileId;

    @Column(name = "file_name", length = 100)
    private String fileName;

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "machine_id")
    private int machineId;

    @Column(name = "upload_time", nullable = false)
    private LocalDateTime uploadTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", referencedColumnName = "machine_id", insertable = false, updatable = false)
    private MachinesBean machine;

    @Transient
    private String machineName;

    @Transient
    private String serialNumber;

    // --- 建構子 ---

    public MachineFilesBean() {
    }

    public MachineFilesBean(String fileName, String filePath, int machineId, LocalDateTime uploadTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.machineId = machineId;
        this.uploadTime = uploadTime;
    }

    public MachineFilesBean(int fileId, String fileName, String filePath, int machineId, LocalDateTime uploadTime) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.machineId = machineId;
        this.uploadTime = uploadTime;
    }

    // --- Getter & Setter ---

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

    public MachinesBean getMachine() {
        return machine;
    }

    public void setMachine(MachinesBean machine) {
        this.machine = machine;
    }

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

    // --- 額外功能方法 ---

    public String getFormattedUploadTime() {
        if (uploadTime == null)
            return "";
        return uploadTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getDetailedUploadTime() {
        if (uploadTime == null)
            return "";
        return uploadTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
    }

    public String getUploadDate() {
        if (uploadTime == null)
            return "";
        return uploadTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getUploadTimeOnly() {
        if (uploadTime == null)
            return "";
        return uploadTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String getFileExtension() {
        if (fileName == null || !fileName.contains("."))
            return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    public boolean isImageFile() {
        String ext = getFileExtension();
        return ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") ||
                ext.equals("gif") || ext.equals("bmp") || ext.equals("webp");
    }

    public boolean isDocumentFile() {
        String ext = getFileExtension();
        return ext.equals("pdf") || ext.equals("doc") || ext.equals("docx") ||
                ext.equals("xls") || ext.equals("xlsx") || ext.equals("ppt") ||
                ext.equals("pptx") || ext.equals("txt");
    }

    public boolean isVideoFile() {
        String ext = getFileExtension();
        return ext.equals("mp4") || ext.equals("avi") || ext.equals("mov") ||
                ext.equals("wmv") || ext.equals("flv") || ext.equals("mkv");
    }

    public String getFileTypeIcon() {
        if (isImageFile())
            return "🖼️";
        else if (isDocumentFile())
            return "📄";
        else if (isVideoFile())
            return "🎬";
        else
            return "📁";
    }
}
