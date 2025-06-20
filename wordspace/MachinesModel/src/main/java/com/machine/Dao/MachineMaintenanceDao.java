package com.machine.Dao;

import com.machine.Bean.MachineMaintenanceBean;
import com.machine.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MachineMaintenanceDao {

    // 新增保養排程
    public void insertMaintenance(MachineMaintenanceBean maintenance) throws Exception {
        String sql = "INSERT INTO machine_maintenance (machine_id, schedule_date, maintenance_description, maintenance_status, employee_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maintenance.getMachineId());
            ps.setTimestamp(2, Timestamp.valueOf(maintenance.getScheduleDate()));
            ps.setString(3, maintenance.getMaintenanceDescription());
            ps.setString(4, maintenance.getMaintenanceStatus());
            ps.setInt(5, maintenance.getEmployeeId());

            ps.executeUpdate();
        }
    }

    // 根據排程ID查詢單筆資料
    public MachineMaintenanceBean findMaintenanceById(int scheduleId) throws Exception {
        String sql = "SELECT * FROM machine_maintenance WHERE schedule_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("schedule_id");
                    int machineId = rs.getInt("machine_id");
                    LocalDateTime scheduleDate = rs.getTimestamp("schedule_date").toLocalDateTime();
                    String description = rs.getString("maintenance_description");
                    String status = rs.getString("maintenance_status");
                    int employeeId = rs.getInt("employee_id");

                    return new MachineMaintenanceBean(id, machineId, scheduleDate, description, status, employeeId);
                }
            }
        }
        return null;
    }

    // 查詢所有保養排程
    public List<MachineMaintenanceBean> findAllMaintenances() throws Exception {
        List<MachineMaintenanceBean> list = new ArrayList<>();
        String sql = "SELECT * FROM machine_maintenance ORDER BY schedule_date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int scheduleId = rs.getInt("schedule_id");
                int machineId = rs.getInt("machine_id");
                LocalDateTime scheduleDate = rs.getTimestamp("schedule_date").toLocalDateTime();
                String description = rs.getString("maintenance_description");
                String status = rs.getString("maintenance_status");
                int employeeId = rs.getInt("employee_id");

                MachineMaintenanceBean maintenance = new MachineMaintenanceBean(
                    scheduleId, machineId, scheduleDate, description, status, employeeId
                );

                list.add(maintenance);
            }
        }
        return list;
    }

    // 更新保養排程
    public void updateMaintenance(MachineMaintenanceBean maintenance) throws Exception {
        String sql = "UPDATE machine_maintenance SET machine_id = ?, schedule_date = ?, maintenance_description = ?, maintenance_status = ?, employee_id = ? WHERE schedule_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maintenance.getMachineId());
            ps.setTimestamp(2, Timestamp.valueOf(maintenance.getScheduleDate()));
            ps.setString(3, maintenance.getMaintenanceDescription());
            ps.setString(4, maintenance.getMaintenanceStatus());
            ps.setInt(5, maintenance.getEmployeeId());
            ps.setInt(6, maintenance.getScheduleId());

            ps.executeUpdate();
        }
    }

    // 刪除保養排程
    public void deleteMaintenance(int scheduleId) throws Exception {
        String sql = "DELETE FROM machine_maintenance WHERE schedule_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);
            ps.executeUpdate();
        }
    }
}
