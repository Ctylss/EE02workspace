package com.machine.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class MachineRepairJoinBean implements Serializable{
    private int repairId;
    private int machineId;
    private String machineName;
    private String repairDescription;
    private LocalDateTime repairTime;
    private String repairStatus;
    private int employeeId;
 
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

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
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

	public MachineRepairJoinBean(int repairId, int machineId, String machineName, String repairDescription,
			LocalDateTime repairTime, String repairStatus, int employeeId) {
		super();
		this.repairId = repairId;
		this.machineId = machineId;
		this.machineName = machineName;
		this.repairDescription = repairDescription;
		this.repairTime = repairTime;
		this.repairStatus = repairStatus;
		this.employeeId = employeeId;
	}

	public MachineRepairJoinBean() {
		super();
	}
	


}
