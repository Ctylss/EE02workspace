package com.machine.Bean;

import java.time.LocalDateTime;

public class MachineMaintenanceBean {
	private int scheduleId; // 保養排程ID
	private int machineId; // 所屬機器ID
	private LocalDateTime scheduleDate; // 預定保養日期
	private String maintenanceDescription; // 保養說明
	private String maintenanceStatus; // 保養狀態
	private int employeeId; // 保養負責人員ID

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

	public MachineMaintenanceBean(int machineId, String maintenanceDescription, String maintenanceStatus,
			int employeeId) {
		super();
		this.machineId = machineId;
		this.maintenanceDescription = maintenanceDescription;
		this.maintenanceStatus = maintenanceStatus;
		this.employeeId = employeeId;
	}

	public MachineMaintenanceBean(String maintenanceDescription, String maintenanceStatus, int employeeId) {
		super();
		this.maintenanceDescription = maintenanceDescription;
		this.maintenanceStatus = maintenanceStatus;
		this.employeeId = employeeId;
	}

}
