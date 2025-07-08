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
		// 檢查出廠編號是否已存在
		MachinesBean existing = machinesDao.findMachineBySerialNumber(machine.getSerialNumber());
		if (existing != null) {
			throw new IllegalArgumentException("出廠編號已存在，請勿重複新增");
		}

		// 呼叫 DAO 寫入資料庫
		machinesDao.insertMachine(machine);
	}
	
	public void deleteMachine(int machineId) throws Exception {
	    // 先找出該機台
	    MachinesBean machine = machinesDao.findMachineById(machineId);
	    if (machine == null) {
	        throw new IllegalArgumentException("找不到該機台資料，無法刪除");
	    }
	    
	    // 檢查狀態，避免刪除運行中的機台
	    if ("運行中".equals(machine.getMstatus())) {
	        throw new IllegalStateException("運行中的機台不可刪除，請先停止機台");
	    }
	    
	    // 通過檢查，執行刪除
	    machinesDao.deleteMachine(machineId);
	}
	public void updateMachine(MachinesBean machine) throws Exception {
	    if (machine.getMachineName() == null || machine.getMachineName().isEmpty()) {
	        throw new IllegalArgumentException("機台名稱不可為空");
	    }
	    if (machine.getSerialNumber() == null || machine.getSerialNumber().isEmpty()) {
	        throw new IllegalArgumentException("出廠編號不可為空");
	    }

	    // 可選擇加檢查 serialNumber 是否重複（不包含自己）
	    MachinesBean existing = machinesDao.findMachineBySerialNumber(machine.getSerialNumber());
	    if (existing != null && existing.getMachineId() != machine.getMachineId()) {
	        throw new IllegalArgumentException("出廠編號已存在，請勿重複");
	    }

	    machinesDao.updateMachine(machine);
	}

}
