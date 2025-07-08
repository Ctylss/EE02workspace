package com.machine.Service.files;

import com.machine.Bean.MachineFilesBean;
import com.machine.Dao.MachineFilesDao;
import com.machine.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MachineFilesService {
    
    private MachineFilesDao machineFilesDao = new MachineFilesDao();
    
    public MachineFilesService() {
        this.machineFilesDao = new MachineFilesDao();
    }
    
    // ======================== 新增：機台驗證方法 ========================
    
    /**
     * 檢查機台 ID 是否存在
     */
    public boolean isValidMachineId(int machineId) {
        String sql = "SELECT COUNT(*) FROM machines WHERE machine_id = ?";
        System.out.println("=== Service 層驗證機台 ID ===");
        System.out.println("檢查機台 ID: " + machineId);
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, machineId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    boolean exists = rs.getInt(1) > 0;
                    System.out.println("機台 ID " + machineId + " 存在: " + exists);
                    return exists;
                }
            }
        } catch (SQLException e) {
            System.err.println("檢查機台 ID 時發生 SQL 錯誤: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("檢查機台 ID 時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    /**
     * 取得可用的機台 ID 列表（用於錯誤提示）
     */
    public List<Integer> getAvailableMachineIds() {
        List<Integer> machineIds = new ArrayList<>();
        String sql = "SELECT machine_id FROM machines ORDER BY machine_id";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                machineIds.add(rs.getInt("machine_id"));
            }
            System.out.println("可用的機台 ID: " + machineIds);
        } catch (Exception e) {
            System.err.println("取得機台 ID 列表時發生錯誤: " + e.getMessage());
            e.printStackTrace();
        }
        return machineIds;
    }
    
    /**
     * 取得機台資訊（用於顯示機台名稱）
     */
    public String getMachineInfo(int machineId) {
        String sql = "SELECT machine_name, serial_number FROM machines WHERE machine_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, machineId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String machineName = rs.getString("machine_name");
                    String serialNumber = rs.getString("serial_number");
                    return "機台名稱: " + machineName + ", 序號: " + serialNumber;
                }
            }
        } catch (Exception e) {
            System.err.println("取得機台資訊時發生錯誤: " + e.getMessage());
        }
        return "未知機台";
    }
    
    // ======================== 改善的新增檔案方法 ========================
    
    public boolean addFile(MachineFilesBean file) {
        System.out.println("=== Service 層檔案新增開始 ===");
        System.out.println("即將插入的檔案資料: " + file.toString());
        
        try {
            // 1. 驗證檔案資料
            String validationError = validateFile(file);
            if (validationError != null) {
                System.err.println("❌ 資料驗證失敗: " + validationError);
                throw new RuntimeException("資料驗證失敗: " + validationError);
            }
            
            // 2. 檢查機台 ID 是否存在
            if (!isValidMachineId(file.getMachineId())) {
                List<Integer> availableIds = getAvailableMachineIds();
                String errorMsg = "機台 ID " + file.getMachineId() + " 不存在於系統中。可用的機台 ID: " + availableIds;
                System.err.println("❌ " + errorMsg);
                throw new RuntimeException(errorMsg);
            }
            
            // 3. 顯示機台資訊
            String machineInfo = getMachineInfo(file.getMachineId());
            System.out.println("✅ 機台驗證通過 - " + machineInfo);
            
            // 4. 檢查檔案名稱是否重複
            if (isFileExists(file.getFileName(), file.getMachineId())) {
                String errorMsg = "機台 ID " + file.getMachineId() + " 下已存在檔案名稱: " + file.getFileName();
                System.err.println("❌ " + errorMsg);
                throw new RuntimeException(errorMsg);
            }
            
            System.out.println("✅ 檔案名稱檢查通過");
            
            // 5. 呼叫 DAO 進行資料庫插入
            System.out.println("🔄 準備呼叫 DAO 進行插入...");
            boolean result = machineFilesDao.addFile(file);
            
            if (result) {
                System.out.println("✅ Service 層確認：檔案新增成功!");
                System.out.println("📁 檔案路徑: " + file.getFilePath());
            } else {
                System.err.println("❌ Service 層確認：DAO 回傳 false，插入失敗");
            }
            
            return result;
            
        } catch (SQLException e) {
            System.err.println("❌ SQL 錯誤: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            
            // 特別處理外鍵約束錯誤
            if (e.getMessage().contains("FOREIGN KEY constraint") || 
                e.getMessage().contains("FK__machine_f__machi")) {
                List<Integer> availableIds = getAvailableMachineIds();
                throw new RuntimeException("機台 ID " + file.getMachineId() + " 不存在於系統中。可用的機台 ID: " + availableIds);
            }
            
            throw new RuntimeException("資料庫操作失敗: " + e.getMessage());
            
        } catch (RuntimeException e) {
            // 重新拋出 RuntimeException（包含我們自定義的驗證錯誤）
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Service 層其他錯誤: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("檔案新增失敗: " + e.getMessage());
        }
    }
    
    // ======================== 查詢相關方法 ========================
    
    public List<MachineFilesBean> getAllFiles() {
        try {
            return machineFilesDao.getAllFiles();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public MachineFilesBean getFileById(int fileId) {
        try {
            return machineFilesDao.getFileById(fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<MachineFilesBean> getFilesByMachineId(int machineId) {
        try {
            return machineFilesDao.getFilesByMachineId(machineId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<MachineFilesBean> searchFiles(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return getAllFiles();
            }
            return machineFilesDao.searchFiles(keyword.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<MachineFilesBean> getFilesWithMachineInfo() {
        try {
            return machineFilesDao.getFilesWithMachineInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // ======================== 其他新增檔案方法 ========================
    
    public boolean addFile(String fileName, String filePath, int machineId) {
        try {
            MachineFilesBean file = new MachineFilesBean();
            file.setFileName(fileName);
            file.setFilePath(filePath);
            file.setMachineId(machineId);
            file.setUploadTime(LocalDateTime.now());
            
            return addFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ======================== 更新檔案方法 ========================
    
    public boolean updateFile(MachineFilesBean file) {
        try {
            // 驗證檔案資料
            String validationError = validateFile(file);
            if (validationError != null) {
                System.err.println("更新檔案驗證失敗: " + validationError);
                return false;
            }
            
            // 檢查機台 ID 是否存在
            if (!isValidMachineId(file.getMachineId())) {
                System.err.println("更新檔案時機台 ID 不存在: " + file.getMachineId());
                return false;
            }
            
            return machineFilesDao.updateFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateFile(int fileId, String fileName, String filePath, int machineId) {
        try {
            // 取得原始檔案以保留上傳時間
            MachineFilesBean originalFile = getFileById(fileId);
            if (originalFile == null) {
                return false;
            }
            
            MachineFilesBean file = new MachineFilesBean();
            file.setFileId(fileId);
            file.setFileName(fileName);
            file.setFilePath(filePath);
            file.setMachineId(machineId);
            file.setUploadTime(originalFile.getUploadTime());
            
            return updateFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ======================== 刪除檔案方法 ========================
    
    public boolean deleteFile(int fileId) {
        try {
            return machineFilesDao.deleteFile(fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ======================== 輔助方法 ========================
    
    public boolean isFileExists(String fileName, int machineId) {
        try {
            List<MachineFilesBean> files = machineFilesDao.getFilesByMachineId(machineId);
            for (MachineFilesBean file : files) {
                if (file.getFileName().equals(fileName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public String validateFile(MachineFilesBean file) {
        if (file.getFileName() == null || file.getFileName().trim().isEmpty()) {
            return "檔案名稱不能為空";
        }
        
        if (file.getFilePath() == null || file.getFilePath().trim().isEmpty()) {
            return "檔案路徑不能為空";
        }
        
        if (file.getMachineId() <= 0) {
            return "請選擇有效的機台";
        }
        
        if (file.getFileName().length() > 255) {
            return "檔案名稱長度不能超過 255 字元";
        }
        
        if (file.getFilePath().length() > 500) {
            return "檔案路徑長度不能超過 500 字元";
        }
        
        return null; // 驗證通過
    }
}