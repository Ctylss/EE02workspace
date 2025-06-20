package com.machine.Bean;

import java.time.LocalDateTime;

public class MachineRepairBean {
	  private int repairId;              // 維修紀錄ID
	    private int machineId;             // 所屬機器ID
	    private String repairDescription;  // 維修或異常問題描述
	    private LocalDateTime repairTime;  // 維修時間
	    private String repairStatus;       // 維修狀態
	    private int employeeId; //員工編號 
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
		public MachineRepairBean(String repairDescription, LocalDateTime repairTime, String repairStatus,
				int employeeId) {
			super();
			this.repairDescription = repairDescription;
			this.repairTime = repairTime;
			this.repairStatus = repairStatus;
			this.employeeId = employeeId;
		}
		public MachineRepairBean(int repairId, int machineId, String repairDescription, LocalDateTime repairTime,
				String repairStatus, int employeeId) {
			super();
			this.repairId = repairId;
			this.machineId = machineId;
			this.repairDescription = repairDescription;
			this.repairTime = repairTime;
			this.repairStatus = repairStatus;
			this.employeeId = employeeId;
		}
		

}
