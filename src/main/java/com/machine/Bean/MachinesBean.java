package com.machine.Bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MachinesBean implements Serializable {
    private int machineId;
    private String machineName;
    private String serialNumber;      // 新增欄位：出廠編號
    private String mstatus;
    private String machineLocation;

    public MachinesBean() {
        super();
    }

    public MachinesBean(int machineId, String machineName, String serialNumber, String mstatus, String machineLocation) {
        super();
        this.machineId = machineId;
        this.machineName = machineName;
        this.serialNumber = serialNumber;
        this.mstatus = mstatus;
        this.machineLocation = machineLocation;
    }

    public MachinesBean(String machineName, String serialNumber, String mstatus, String machineLocation) {
        super();
        this.machineName = machineName;
        this.serialNumber = serialNumber;
        this.mstatus = mstatus;
        this.machineLocation = machineLocation;
    }
 
    

    // getter & setter
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

    @Override
    public String toString() {
        return "MachinesBean [machineId=" + machineId + ", machineName=" + machineName + ", serialNumber=" + serialNumber
                + ", mstatus=" + mstatus + ", machineLocation=" + machineLocation + "]";
    }
}
