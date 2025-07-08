package com.machine.Service.repair;

import java.util.List;

import com.machine.Bean.MachineRepairBean;
import com.machine.Bean.MachineRepairJoinBean;
import com.machine.Dao.MachineRepairDao;

public class MachineRepairService {
	private MachineRepairDao machineRepairDao =new MachineRepairDao();
	
	public void insertRepair(MachineRepairBean repair) throws Exception {
		
		 machineRepairDao.insertRepair(repair);
	}
	public  List<MachineRepairJoinBean> machineRepairView() throws Exception{
		return machineRepairDao.machineRepairView();
		
	}
	public MachineRepairJoinBean findRepairById(int repairId) throws Exception {
		return machineRepairDao.findRepairById(repairId);
	}
	
	// 1. 更新報修狀態
	public void updateRepairStatus(int repairId, String newStatus) throws Exception {
	    machineRepairDao.updateRepairStatus(repairId, newStatus);
	}

	// 2. 依狀態查詢報修記錄  
	public List<MachineRepairJoinBean> getRepairsByStatus(String status) throws Exception {
	    return machineRepairDao.findRepairsByStatus(status);
	}

	// 3. 後台專用：取得所有記錄（與前台相同，但可以加入權限控制）
	public List<MachineRepairJoinBean> getAllRepairsForAdmin() throws Exception {
	    return machineRepairDao.machineRepairView();
	}
	
	public List<MachineRepairJoinBean> findRepairsByMachineId(int machineId) throws Exception {
	    return machineRepairDao.findRepairsByMachineId(machineId);
	}
	public List<MachineRepairJoinBean> findRepairsByStatus(String status) throws Exception{
		return machineRepairDao.findRepairsByStatus(status);
	}
}
