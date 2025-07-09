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

@SuppressWarnings("serial")
@Entity
@Table(name = "machine_maintenance")
public class MachineMaintenanceBean implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private int scheduleId;

    @Column(name = "machine_id")
    private int machineId;

    @Column(name = "schedule_date")
    private LocalDateTime scheduleDate;

    @Column(name = "maintenance_description", columnDefinition = "TEXT")
    private String maintenanceDescription;

    @Column(name = "maintenance_status", length = 20)
    private String maintenanceStatus;

    @Column(name = "employee_id")
    private int employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", referencedColumnName = "machine_id", insertable = false, updatable = false)
    private MachinesBean machine;

    // Transient 欄位
    @Transient
    private String machineName;

    @Transient
    private String serialNumber;

    // 無參數建構子
    public MachineMaintenanceBean() {
    }

    // 完整建構子
    public MachineMaintenanceBean(int scheduleId, int machineId, LocalDateTime scheduleDate,
            String maintenanceDescription, String maintenanceStatus,
            int employeeId, MachinesBean machine, String machineName, String serialNumber) {
        this.scheduleId = scheduleId;
        this.machineId = machineId;
        this.scheduleDate = scheduleDate;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
        this.machine = machine;
        this.machineName = machineName;
        this.serialNumber = serialNumber;
    }

    // 原有的建構子
    public MachineMaintenanceBean(int machineId, LocalDateTime scheduleDate, String maintenanceDescription,
            String maintenanceStatus, int employeeId) {
        this.machineId = machineId;
        this.scheduleDate = scheduleDate;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
    }

    public MachineMaintenanceBean(int scheduleId, int machineId, LocalDateTime scheduleDate,
            String maintenanceDescription, String maintenanceStatus, int employeeId) {
        this.scheduleId = scheduleId;
        this.machineId = machineId;
        this.scheduleDate = scheduleDate;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
    }

    public MachineMaintenanceBean(int scheduleId, int machineId, String maintenanceDescription,
            String maintenanceStatus, int employeeId) {
        this.scheduleId = scheduleId;
        this.machineId = machineId;
        this.maintenanceDescription = maintenanceDescription;
        this.maintenanceStatus = maintenanceStatus;
        this.employeeId = employeeId;
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

    // 格式化日期的方法
    public String getFormattedScheduleDate() {
        if (scheduleDate == null)
            return "";
        return scheduleDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getDetailedScheduleDate() {
        if (scheduleDate == null)
            return "";
        return scheduleDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
    }

    // toString 方法（排除 machine 以避免循環引用）
    @Override
    public String toString() {
        return "MachineMaintenanceBean{" +
                "scheduleId=" + scheduleId +
                ", machineId=" + machineId +
                ", scheduleDate=" + scheduleDate +
                ", maintenanceDescription='" + maintenanceDescription + '\'' +
                ", maintenanceStatus='" + maintenanceStatus + '\'' +
                ", employeeId=" + employeeId +
                ", machineName='" + machineName + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                '}';
    }

    // equals 和 hashCode 方法
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MachineMaintenanceBean that = (MachineMaintenanceBean) o;
        return scheduleId == that.scheduleId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(scheduleId);
    }
}