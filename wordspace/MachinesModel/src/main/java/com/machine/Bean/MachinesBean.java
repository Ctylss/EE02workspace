package com.machine.Bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MachinesBean implements Serializable {
	private int machineId;
	private String machineName;
	private String mstatus;
	private String machineLocation;
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
	public MachinesBean(int machineId, String machineName, String mstatus, String machineLocation) {
		super();
		this.machineId = machineId;
		this.machineName = machineName;
		this.mstatus = mstatus;
		this.machineLocation = machineLocation;
	}
	public MachinesBean(String machineName, String mstatus, String machineLocation) {
		super();
		this.machineName = machineName;
		this.mstatus = mstatus;
		this.machineLocation = machineLocation;
	}
	
}
