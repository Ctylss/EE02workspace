package com.machine.Service.machine;

import java.util.List;

import com.machine.Bean.MachinesBean;
import com.machine.Dao.MachinesDao;

public class MachinesService {
	private MachinesDao machinesDao = new MachinesDao();

	// 查詢所有機台
	public List<MachinesBean> findAllMachines() throws Exception {
		return machinesDao.findAllMachines();
	}

	// 依機台ID取得單筆資料
	public MachinesBean findMachineById(int machineId) throws Exception {

		return machinesDao.findMachineById(machineId);
	}

	public void insertMachine(MachinesBean machine) throws Exception {
		if (machine.getMachineName() == null || machine.getMachineName().isEmpty()) {
			throw new IllegalArgumentException("機台名稱不可為空");
		}
		if (machine.getSerialNumber() == null || machine.getSerialNumber().isEmpty()) {
			throw new IllegalArgumentException("出廠編號不可為空");
		}

		if (machine.getMstatus() == null || machine.getMstatus().isEmpty()) {
			machine.setMstatus("待機");
			machinesDao.insertMachine(machine);
		}

	}
}
