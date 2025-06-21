package com.machine.Service.machine;

import java.util.List;

import com.machine.Bean.MachinesBean;
import com.machine.Dao.MachinesDao;

public class MachinesService {
	 private MachinesDao machinesDao = new MachinesDao();
	 //查詢所有機台
	 public List<MachinesBean> findAllMachines() throws Exception {	 
		 return machinesDao.findAllMachines();
	    }

	    // 依機台ID取得單筆資料
	    public MachinesBean findMachineById(int machineId) throws Exception {
	    	
	    	
	        return machinesDao.findMachineById(machineId);
	    }

}
