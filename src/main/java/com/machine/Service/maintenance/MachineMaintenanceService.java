package com.machine.Service.maintenance;

import java.util.List;

import com.machine.Bean.MachineMaintenanceBean;
import com.machine.Bean.MachineMaintenanceJoinBean;
import com.machine.Dao.MachineMaintenanceDao;

public class MachineMaintenanceService {
    private MachineMaintenanceDao maintenanceDao = new MachineMaintenanceDao();
    
    // 查詢所有保養資料（簡化版）
    public List<MachineMaintenanceBean> findAllMaintenances() throws Exception {
        return maintenanceDao.findAllMaintenances();
    }
    
    // 查詢所有保養資料（詳細版，包含機台名稱）
    public List<MachineMaintenanceJoinBean> findAllMaintenancesDetail() throws Exception {
        return maintenanceDao.findAllMaintenancesDetail();
    }
    
    // 根據 ID 查詢單筆保養詳細資料
    public MachineMaintenanceJoinBean findMaintenanceDetailById(int scheduleId) throws Exception {
        return maintenanceDao.findMaintenanceDetailById(scheduleId);
    }
    
    // 新增保養排程
    public void insertMaintenance(MachineMaintenanceBean maintenance) throws Exception {
        maintenanceDao.insertMaintenance(maintenance);
    }
    
    // 更新保養排程
    public void updateMaintenance(MachineMaintenanceBean maintenance) throws Exception {
        maintenanceDao.updateMaintenance(maintenance);
    }
    public void deleteMaintenance(int scheduleId) throws Exception{
    	maintenanceDao.deleteMaintenance(scheduleId);
    }
    public MachineMaintenanceBean findMaintenanceById(int scheduleId) throws Exception {
        return maintenanceDao.findMaintenanceById(scheduleId);
    }
}