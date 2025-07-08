package com.machine.Dao;

import com.machine.Bean.MachineMaintenanceBean;
import com.machine.Bean.MachineMaintenanceJoinBean;
import com.machine.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MachineMaintenanceDao {

	// 新增保養排程
	public void insertMaintenance(MachineMaintenanceBean maintenance) throws Exception {
		String sql = "INSERT INTO machine_maintenance (machine_id, schedule_date, maintenance_description, maintenance_status, employee_id) VALUES (?, GETDATE(), ?, ?, ?)";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, maintenance.getMachineId());
		
			ps.setString(2, maintenance.getMaintenanceDescription());
			ps.setString(3, maintenance.getMaintenanceStatus());
			ps.setInt(4, maintenance.getEmployeeId());

			ps.executeUpdate();
		}
	}



	// 查詢所有保養排程
	public List<MachineMaintenanceJoinBean> findAllMaintenancesDetail() throws Exception {
		List<MachineMaintenanceJoinBean> list = new ArrayList<>();
		String sql = "SELECT r.schedule_id,r.machine_id ,m.machine_name,r.schedule_date,r.maintenance_description,r.maintenance_status,r.employee_id FROM machine_maintenance r JOIN machines m ON r.machine_id = m.machine_id ORDER BY schedule_id ";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int scheduleId = rs.getInt("schedule_id");
				int machineId = rs.getInt("machine_id");
				String machineName=rs.getString("machine_name");
				LocalDateTime scheduleDate = rs.getTimestamp("schedule_date").toLocalDateTime();
				String description = rs.getString("maintenance_description");
				String status = rs.getString("maintenance_status");
				int employeeId = rs.getInt("employee_id");

				MachineMaintenanceJoinBean maintenance = new MachineMaintenanceJoinBean(scheduleId, machineId,machineName, scheduleDate,
						description, status, employeeId);

				list.add(maintenance);
			}
		}
		return list;
	}
	public List<MachineMaintenanceBean> findAllMaintenances() throws Exception {
    List<MachineMaintenanceBean> list = new ArrayList<>();

    String sql = "SELECT schedule_id, machine_id, schedule_date, maintenance_status " +
                 "FROM machine_maintenance ORDER BY schedule_id";

    try (Connection conn = DBUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            int scheduleId = rs.getInt("schedule_id");
            int machineId = rs.getInt("machine_id");

            // 安全處理 schedule_date 可能為 null 的情況
            Timestamp ts = rs.getTimestamp("schedule_date");
            LocalDateTime scheduleDate = null;
            if (ts != null) {
                scheduleDate = ts.toLocalDateTime();
            } else {
                System.out.println("⚠️ Warning: schedule_date 為 NULL，schedule_id = " + scheduleId);
            }

            String status = rs.getString("maintenance_status");

            // 使用對應建構子，允許 scheduleDate 為 null
            MachineMaintenanceBean maintenance =
                new MachineMaintenanceBean(scheduleId, machineId, scheduleDate, status);

            list.add(maintenance);
        }

    } catch (Exception e) {
        System.err.println("❌ 查詢 machine_maintenance 時發生錯誤：");
        e.printStackTrace();
        throw e; // 向上拋出讓 Servlet 層能印 log
    }

    return list;
}
	public MachineMaintenanceJoinBean findMaintenanceDetailById(int scheduleId) throws Exception {
	    String sql = "SELECT r.schedule_id, r.machine_id, m.machine_name, r.schedule_date, " +
	                 "r.maintenance_description, r.maintenance_status, r.employee_id " +
	                 "FROM machine_maintenance r JOIN machines m ON r.machine_id = m.machine_id " +
	                 "WHERE r.schedule_id = ?";
	    
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, scheduleId);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return new MachineMaintenanceJoinBean(
	                    rs.getInt("schedule_id"),
	                    rs.getInt("machine_id"),
	                    rs.getString("machine_name"),
	                    rs.getTimestamp("schedule_date").toLocalDateTime(),
	                    rs.getString("maintenance_description"),
	                    rs.getString("maintenance_status"),
	                    rs.getInt("employee_id")
	                );
	            }
	        }
	    }
	    return null; // 沒查到資料
	}
	// 用於抓schedule_id
	public MachineMaintenanceBean findMaintenanceById(int scheduleId) throws Exception {
	    String sql = "SELECT schedule_id, machine_id, schedule_date, maintenance_description, maintenance_status, employee_id " +
	                 "FROM machine_maintenance WHERE schedule_id = ?";
	    
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, scheduleId);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return new MachineMaintenanceBean(
	                    rs.getInt("schedule_id"),
	                    rs.getInt("machine_id"),
	                    rs.getTimestamp("schedule_date").toLocalDateTime(),
	                    rs.getString("maintenance_description"),
	                    rs.getString("maintenance_status"),
	                    rs.getInt("employee_id")
	                );
	            }
	        }
	    }
	    return null; // 沒查到資料
	}



	// 更新保養排程
	public void updateMaintenance(MachineMaintenanceBean maintenance) throws Exception {
	    String sql = "UPDATE machine_maintenance SET machine_id = ?, schedule_date = GETDATE(), maintenance_description = ?, maintenance_status = ?, employee_id = ? WHERE schedule_id = ?";
	    try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, maintenance.getMachineId());
	        ps.setString(2, maintenance.getMaintenanceDescription());
	        ps.setString(3, maintenance.getMaintenanceStatus());
	        ps.setInt(4, maintenance.getEmployeeId());
	        ps.setInt(5, maintenance.getScheduleId());  // schedule_id 是 WHERE 條件，排最後沒錯

	        ps.executeUpdate();
	    }
	}

	// 刪除保養排程
	public void deleteMaintenance(int scheduleId) throws Exception {
		String sql = "DELETE FROM machine_maintenance WHERE schedule_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, scheduleId);
			ps.executeUpdate();
		}
	}
	

	

}
