package com.machine.Dao;

import com.machine.Bean.MachineFilesBean;
import com.machine.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineFilesDao {

    // ✅ 取得所有檔案資料（依照上傳時間排序）
    public List<MachineFilesBean> getAllFiles() throws Exception {
        List<MachineFilesBean> files = new ArrayList<>();
        String sql = "SELECT file_id, machine_id, file_name, file_path, upload_time FROM machine_files ORDER BY upload_time DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                MachineFilesBean file = new MachineFilesBean();
                file.setFileId(rs.getInt("file_id"));
                file.setMachineId(rs.getInt("machine_id"));
                file.setFileName(rs.getString("file_name"));
                file.setFilePath(rs.getString("file_path"));
                
                // 處理 Timestamp 轉 LocalDateTime
                Timestamp timestamp = rs.getTimestamp("upload_time");
                if (timestamp != null) {
                    file.setUploadTime(timestamp.toLocalDateTime());
                }
                
                files.add(file);
            }
        }
        return files;
    }

    // ✅ 根據檔案 ID 取得單一檔案資訊（詳細用）
    public MachineFilesBean getFileById(int fileId) throws Exception {
        String sql = "SELECT file_id, machine_id, file_name, file_path, upload_time FROM machine_files WHERE file_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, fileId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    MachineFilesBean file = new MachineFilesBean();
                    file.setFileId(rs.getInt("file_id"));
                    file.setMachineId(rs.getInt("machine_id"));
                    file.setFileName(rs.getString("file_name"));
                    file.setFilePath(rs.getString("file_path"));
                    
                    // 處理 Timestamp 轉 LocalDateTime
                    Timestamp timestamp = rs.getTimestamp("upload_time");
                    if (timestamp != null) {
                        file.setUploadTime(timestamp.toLocalDateTime());
                    }
                    
                    return file;
                }
            }
        }
        return null;
    }

    // ✅ 根據機台 ID 取得該機台所有的檔案資料
    public List<MachineFilesBean> getFilesByMachineId(int machineId) throws Exception {
        List<MachineFilesBean> files = new ArrayList<>();
        String sql = "SELECT file_id, machine_id, file_name, file_path, upload_time FROM machine_files WHERE machine_id = ? ORDER BY upload_time DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, machineId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MachineFilesBean file = new MachineFilesBean();
                    file.setFileId(rs.getInt("file_id"));
                    file.setMachineId(rs.getInt("machine_id"));
                    file.setFileName(rs.getString("file_name"));
                    file.setFilePath(rs.getString("file_path"));
                    
                    // 處理 Timestamp 轉 LocalDateTime
                    Timestamp timestamp = rs.getTimestamp("upload_time");
                    if (timestamp != null) {
                        file.setUploadTime(timestamp.toLocalDateTime());
                    }
                    
                    files.add(file);
                }
            }
        }
        return files;
    }

    // ✅ 新增一筆檔案資料
    public boolean addFile(MachineFilesBean file) throws Exception {
        // 使用 NOW() 而不是 GETDATE() (MySQL/PostgreSQL 語法)
        // 如果是 SQL Server，保持 GETDATE()
        String sql = "INSERT INTO machine_files (machine_id, file_name, file_path, upload_time) VALUES (?, ?, ?, GETDATE())";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, file.getMachineId());
            pstmt.setString(2, file.getFileName());
            pstmt.setString(3, file.getFilePath());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // ✅ 更新檔案資料（編輯用）
    public boolean updateFile(MachineFilesBean file) throws Exception {
        String sql = "UPDATE machine_files SET machine_id = ?, file_name = ?, file_path = ? WHERE file_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, file.getMachineId());
            pstmt.setString(2, file.getFileName());
            pstmt.setString(3, file.getFilePath());
            pstmt.setInt(4, file.getFileId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    // ✅ 更新檔案資料的重載方法（適用於 Servlet 直接呼叫）
    public boolean updateFile(int fileId, String fileName, String filePath, int machineId) throws Exception {
        String sql = "UPDATE machine_files SET machine_id = ?, file_name = ?, file_path = ? WHERE file_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, machineId);
            pstmt.setString(2, fileName);
            pstmt.setString(3, filePath);
            pstmt.setInt(4, fileId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // ✅ 根據 file_id 刪除一筆檔案資料
    public boolean deleteFile(int fileId) throws Exception {
        String sql = "DELETE FROM machine_files WHERE file_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, fileId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // ✅ 模糊搜尋：檔案名稱 或 機台名稱
    public List<MachineFilesBean> searchFiles(String keyword) throws Exception {
        List<MachineFilesBean> files = new ArrayList<>();
        String sql = "SELECT f.file_id, f.machine_id, f.file_name, f.file_path, f.upload_time " +
                     "FROM machine_files f " +
                     "LEFT JOIN machines m ON f.machine_id = m.machine_id " +
                     "WHERE f.file_name LIKE ? OR m.machine_name LIKE ? " +
                     "ORDER BY f.upload_time DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MachineFilesBean file = new MachineFilesBean();
                    file.setFileId(rs.getInt("file_id"));
                    file.setMachineId(rs.getInt("machine_id"));
                    file.setFileName(rs.getString("file_name"));
                    file.setFilePath(rs.getString("file_path"));
                    
                    // 處理 Timestamp 轉 LocalDateTime
                    Timestamp timestamp = rs.getTimestamp("upload_time");
                    if (timestamp != null) {
                        file.setUploadTime(timestamp.toLocalDateTime());
                    }
                    
                    files.add(file);
                }
            }
        }
        return files;
    }

    // ✅ 取得所有檔案，並包含機台資訊（適合後台文件總覽頁面）
    public List<MachineFilesBean> getFilesWithMachineInfo() throws Exception {
        List<MachineFilesBean> files = new ArrayList<>();
        String sql = "SELECT f.file_id, f.machine_id, f.file_name, f.file_path, f.upload_time, " +
                     "m.machine_name, m.serial_number " +
                     "FROM machine_files f " +
                     "LEFT JOIN machines m ON f.machine_id = m.machine_id " +
                     "ORDER BY f.upload_time DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                MachineFilesBean file = new MachineFilesBean();
                file.setFileId(rs.getInt("file_id"));
                file.setMachineId(rs.getInt("machine_id"));
                file.setFileName(rs.getString("file_name"));
                file.setFilePath(rs.getString("file_path"));
                
                // 處理 Timestamp 轉 LocalDateTime
                Timestamp timestamp = rs.getTimestamp("upload_time");
                if (timestamp != null) {
                    file.setUploadTime(timestamp.toLocalDateTime());
                }
                
                // 設定機台資訊（如果有的話）
                file.setMachineName(rs.getString("machine_name"));
                file.setSerialNumber(rs.getString("serial_number"));
                
                files.add(file);
            }
        }
        return files;
    }
    
    // ✅ 檢查檔案名稱是否已存在（相同機台下）
    public boolean isFileNameExists(int machineId, String fileName) throws Exception {
        String sql = "SELECT COUNT(*) FROM machine_files WHERE machine_id = ? AND file_name = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, machineId);
            pstmt.setString(2, fileName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    // ✅ 檢查檔案名稱是否已存在（排除特定檔案ID，用於更新時檢查）
    public boolean isFileNameExists(int machineId, String fileName, int excludeFileId) throws Exception {
        String sql = "SELECT COUNT(*) FROM machine_files WHERE machine_id = ? AND file_name = ? AND file_id != ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, machineId);
            pstmt.setString(2, fileName);
            pstmt.setInt(3, excludeFileId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    // ✅ 取得檔案統計資訊
    public int getTotalFileCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM machine_files";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    // ✅ 取得特定機台的檔案數量
    public int getFileCountByMachine(int machineId) throws Exception {
        String sql = "SELECT COUNT(*) FROM machine_files WHERE machine_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, machineId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}