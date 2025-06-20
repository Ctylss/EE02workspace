package com.machine.Dao;

import com.machine.Bean.MachineRepairBean;
import com.machine.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MachineRepairDao {

    // 新增維修紀錄（repair_time 使用資料庫 GETDATE() 自動產生）
    public void insertRepair(MachineRepairBean repair) throws Exception {
        String sql = "INSERT INTO machine_repair (machine_id, repair_description, repair_time, repair_status, employee_id) VALUES (?, ?, GETDATE(), ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, repair.getMachineId());
            ps.setString(2, repair.getRepairDescription());
            ps.setString(3, repair.getRepairStatus());
            ps.setInt(4, repair.getEmployeeId());

            ps.executeUpdate();
        }
    }

    // 根據維修紀錄ID查詢單筆
    public MachineRepairBean findRepairById(int repairId) throws Exception {
        String sql = "SELECT * FROM machine_repair WHERE repair_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, repairId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("repair_id");
                    int machineId = rs.getInt("machine_id");
                    String description = rs.getString("repair_description");
                    LocalDateTime repairTime = rs.getTimestamp("repair_time").toLocalDateTime();
                    String status = rs.getString("repair_status");
                    int employeeId = rs.getInt("employee_id");

                    return new MachineRepairBean(id, machineId, description, repairTime, status, employeeId);
                }
            }
        }
        return null;
    }

    // 查詢所有維修紀錄，依時間倒序
    public List<MachineRepairBean> findAllRepairs() throws Exception {
        List<MachineRepairBean> list = new ArrayList<>();
        String sql = "SELECT * FROM machine_repair ORDER BY repair_time DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("repair_id");
                int machineId = rs.getInt("machine_id");
                String description = rs.getString("repair_description");
                LocalDateTime repairTime = rs.getTimestamp("repair_time").toLocalDateTime();
                String status = rs.getString("repair_status");
                int employeeId = rs.getInt("employee_id");

                MachineRepairBean repair = new MachineRepairBean(id, machineId, description, repairTime, status, employeeId);
                list.add(repair);
            }
        }
        return list;
    }

    // 更新維修紀錄
    public void updateRepair(MachineRepairBean repair) throws Exception {
        String sql = "UPDATE machine_repair SET machine_id = ?, repair_description = ?, repair_time = ?, repair_status = ?, employee_id = ? WHERE repair_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, repair.getMachineId());
            ps.setString(2, repair.getRepairDescription());
            ps.setTimestamp(3, Timestamp.valueOf(repair.getRepairTime()));
            ps.setString(4, repair.getRepairStatus());
            ps.setInt(5, repair.getEmployeeId());
            ps.setInt(6, repair.getRepairId());

            ps.executeUpdate();
        }
    }

    // 刪除維修紀錄
    public void deleteRepair(int repairId) throws Exception {
        String sql = "DELETE FROM machine_repair WHERE repair_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, repairId);
            ps.executeUpdate();
        }
    }
}
