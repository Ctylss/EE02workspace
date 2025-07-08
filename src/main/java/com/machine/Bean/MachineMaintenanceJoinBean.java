package com.machine.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class MachineMaintenanceJoinBean implements Serializable {
    private int scheduleId;
    private int machineId;
    private String machineName; // 來自 machines 表
    private LocalDateTime scheduleDate;
    private String maintenanceDescription;
    private String maintenanceStatus;
    private int employeeId;
    private LocalDateTime createTime; // <-- 新增：建立時間
    private LocalDateTime updateTime; // <-- 新增：更新時間

    // 無參數建構子 (通常推薦)
    public MachineMaintenanceJoinBean() {
    }

    // 完整的建構子，包含所有從資料庫查詢到的欄位
    // 請根據您 findAllMaintenancesDetail 或 findMaintenanceDetailById 查詢的欄位調整
    public MachineMaintenanceJoinBean(int scheduleId, int machineId, String machineName,
                                      LocalDateTime scheduleDate, String maintenanceDescription,
                                      String maintenanceStatus, int employeeId,
                                      LocalDateTime createTime, LocalDateTime updateTime) { // <-- 新增 createTime 和 updateTime 參數
        this.scheduleId = scheduleId;
        this.machineId = machineId;
        this.machineName = machineName;
        this.scheduleDate = scheduleDate;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
        this.createTime = createTime; // <-- 設定 createTime
        this.updateTime = updateTime; // <-- 設定 updateTime
    }

    // 您在 MachineMaintenanceDao 中使用的 7 個參數建構子 (目前缺少 createTime, updateTime)
    // 雖然您在 DAO 中已經有一個 7 參數的建構子，但它沒有包含 createTime 和 updateTime。
    // 如果這個建構子是從資料庫查詢結果來填充的，且資料庫確實有這些欄位，那麼它應該包含進來。
    // 為了解決當前問題，我們可以先加入這個，但更完整的做法是讓 DAO 也查詢 createTime 和 updateTime。
    public MachineMaintenanceJoinBean(int scheduleId, int machineId, String machineName, LocalDateTime scheduleDate,
                                      String maintenanceDescription, String maintenanceStatus, int employeeId) {
        this.scheduleId = scheduleId;
        this.machineId = machineId;
        this.machineName = machineName;
        this.scheduleDate = scheduleDate;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
        // createTime 和 updateTime 將為 null，直到通過 setter 或其他建構子設置
    }


    // Getter 和 Setter 方法

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public LocalDateTime getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDateTime scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getMaintenanceDescription() {
        return maintenanceDescription;
    }

    public void setMaintenanceDescription(String maintenanceDescription) {
        this.maintenanceDescription = maintenanceDescription;
    }

    public String getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getCreateTime() { // <-- 新增 Getter
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) { // <-- 新增 Setter
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() { // <-- 新增 Getter
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) { // <-- 新增 Setter
        this.updateTime = updateTime;
    }
}