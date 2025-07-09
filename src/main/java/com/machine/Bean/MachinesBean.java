package com.machine.Bean;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

@SuppressWarnings("serial")
@Entity
@Table(name = "machines")
public class MachinesBean implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_id")
    private int machineId;

    @Column(name = "machine_name", length = 50)
    private String machineName;

    @Column(name = "serial_number", length = 50, unique = true)
    private String serialNumber;

    @Column(name = "mstatus", length = 20)
    private String mstatus;

    @Column(name = "machine_location", length = 50)
    private String machineLocation;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MachineRepairBean> repairRecords;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MachineMaintenanceBean> maintenanceSchedules;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MachineFilesBean> files;

    // 無參數建構子 - JPA 需要
    public MachinesBean() {
    }

    // 完整建構子
    public MachinesBean(int machineId, String machineName, String serialNumber, String mstatus,
            String machineLocation, List<MachineRepairBean> repairRecords,
            List<MachineMaintenanceBean> maintenanceSchedules, List<MachineFilesBean> files) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.serialNumber = serialNumber;
        this.mstatus = mstatus;
        this.machineLocation = machineLocation;
        this.repairRecords = repairRecords;
        this.maintenanceSchedules = maintenanceSchedules;
        this.files = files;
    }

    // 原有的建構子
    public MachinesBean(String machineName, String serialNumber, String mstatus, String machineLocation) {
        this.machineName = machineName;
        this.serialNumber = serialNumber;
        this.mstatus = mstatus;
        this.machineLocation = machineLocation;
    }

    public MachinesBean(int machineId, String machineName, String serialNumber, String mstatus,
            String machineLocation) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.serialNumber = serialNumber;
        this.mstatus = mstatus;
        this.machineLocation = machineLocation;
    }

    // Getter 和 Setter 方法
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMstatus() {
        return mstatus;
    }

    public void setMstatus(String mstatus) {
        this.mstatus = mstatus;
    }

    public String getMachineLocation() {
        return machineLocation;
    }

    public void setMachineLocation(String machineLocation) {
        this.machineLocation = machineLocation;
    }

    public List<MachineRepairBean> getRepairRecords() {
        return repairRecords;
    }

    public void setRepairRecords(List<MachineRepairBean> repairRecords) {
        this.repairRecords = repairRecords;
    }

    public List<MachineMaintenanceBean> getMaintenanceSchedules() {
        return maintenanceSchedules;
    }

    public void setMaintenanceSchedules(List<MachineMaintenanceBean> maintenanceSchedules) {
        this.maintenanceSchedules = maintenanceSchedules;
    }

    public List<MachineFilesBean> getFiles() {
        return files;
    }

    public void setFiles(List<MachineFilesBean> files) {
        this.files = files;
    }

    // toString 方法（排除關聯集合以避免循環引用）
    @Override
    public String toString() {
        return "MachinesBean{" +
                "machineId=" + machineId +
                ", machineName='" + machineName + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", mstatus='" + mstatus + '\'' +
                ", machineLocation='" + machineLocation + '\'' +
                '}';
    }

    // equals 和 hashCode 方法
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MachinesBean that = (MachinesBean) o;
        return machineId == that.machineId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(machineId);
    }
}