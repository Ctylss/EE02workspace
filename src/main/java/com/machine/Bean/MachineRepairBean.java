package com.machine.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class MachineRepairBean implements Serializable{
    private int repairId;
    private int machineId;
    private String repairDescription;
    private LocalDateTime repairTime;
    private String repairStatus;
    private int employeeId;

    // 建構子
    public MachineRepairBean(int repairId, int machineId, String repairDescription,
                             LocalDateTime repairTime, String repairStatus, int employeeId) {
        this.repairId = repairId;
        this.machineId = machineId;
        this.repairDescription = repairDescription;
        this.repairTime = repairTime;
        this.repairStatus = repairStatus;
        this.employeeId = employeeId;
    }

    // getter 和 setter
    public int getRepairId() {
        return repairId;
    }

    public void setRepairId(int repairId) {
        this.repairId = repairId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public String getRepairDescription() {
        return repairDescription;
    }

    public void setRepairDescription(String repairDescription) {
        this.repairDescription = repairDescription;
    }

    public LocalDateTime getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(LocalDateTime repairTime) {
        this.repairTime = repairTime;
    }

    public String getRepairStatus() {
        return repairStatus;
    }

    public void setRepairStatus(String repairStatus) {
        this.repairStatus = repairStatus;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

	public MachineRepairBean(int machineId, String repairDescription, LocalDateTime repairTime, String repairStatus,
			int employeeId) {
		super();
		this.machineId = machineId;
		this.repairDescription = repairDescription;
		this.repairTime = repairTime;
		this.repairStatus = repairStatus;
		this.employeeId = employeeId;
	}

	
}
