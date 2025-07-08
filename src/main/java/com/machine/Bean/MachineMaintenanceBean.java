package com.machine.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class MachineMaintenanceBean implements Serializable {
    private int scheduleId; // 保養排程ID
    private int machineId; // 所屬機器ID
    private LocalDateTime scheduleDate; // 預定保養日期
    private String maintenanceDescription; // 保養說明
    private String maintenanceStatus; // 保養狀態
    private int employeeId; // 保養負責人員ID
    private LocalDateTime createTime; // 新增：建立時間
    private LocalDateTime updateTime; // 新增：更新時間

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

    // 新增：createTime 的 Getter 和 Setter
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    // 新增：updateTime 的 Getter 和 Setter
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    // 無參數建構子 (通常是必需的，尤其是在從 ResultSet 映射時)
    public MachineMaintenanceBean() {
    }

    // 新增此建構子：匹配 findMaintenanceById 方法中的 6 個參數
    public MachineMaintenanceBean(int scheduleId, int machineId, LocalDateTime scheduleDate,
                                  String maintenanceDescription, String maintenanceStatus, int employeeId) {
        super();
        this.scheduleId = scheduleId;
        this.machineId = machineId;
        this.scheduleDate = scheduleDate;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
    }

    // 您之前新增的 8 個參數建構子
    public MachineMaintenanceBean(int scheduleId, int machineId, LocalDateTime scheduleDate,
                                  String maintenanceDescription, String maintenanceStatus, int employeeId,
                                  LocalDateTime createTime, LocalDateTime updateTime) {
        super();
        this.scheduleId = scheduleId;
        this.machineId = machineId;
        this.scheduleDate = scheduleDate;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // 其他建構子 (保持不變)
    public MachineMaintenanceBean(int machineId, String maintenanceDescription, String maintenanceStatus,
                                  int employeeId, LocalDateTime createTime, LocalDateTime updateTime) {
        super();
        this.machineId = machineId;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public MachineMaintenanceBean(int scheduleId, int machineId, LocalDateTime scheduleDate, String maintenanceStatus) {
        super();
        this.scheduleId = scheduleId;
        this.machineId = machineId;
        this.scheduleDate = scheduleDate;
        this.maintenanceStatus = maintenanceStatus;
    }

    public MachineMaintenanceBean(int machineId, LocalDateTime scheduleDate, String maintenanceDescription,
                                  String maintenanceStatus, int employeeId) {
        super();
        this.machineId = machineId;
        this.scheduleDate = scheduleDate;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
    }

    public MachineMaintenanceBean(int scheduleId, int machineId, String maintenanceDescription,
                                  String maintenanceStatus, int employeeId) {
        super();
        this.scheduleId = scheduleId;
        this.machineId = machineId;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
    }
}