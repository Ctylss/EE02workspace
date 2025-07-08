package com.machine.Dao;

import com.machine.Bean.MachinesBean;
import com.machine.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachinesDao {

    // 新增機器（加入 serial_number）
    public void insertMachine(MachinesBean machine) throws Exception {
        String sql = "INSERT INTO machines (machine_name, serial_number, mstatus, machine_location) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, machine.getMachineName());
            ps.setString(2, machine.getSerialNumber());
            ps.setString(3, machine.getMstatus());
            ps.setString(4, machine.getMachineLocation());
            ps.executeUpdate();
        }
    }

    // 依機器ID查詢
    public MachinesBean findMachineById(int machineId) throws Exception {
        String sql = "SELECT * FROM machines WHERE machine_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, machineId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("machine_id");
                    String name = rs.getString("machine_name");
                    String serialNumber = rs.getString("serial_number");
                    String status = rs.getString("mstatus");
                    String location = rs.getString("machine_location");

                    return new MachinesBean(id, name, serialNumber, status, location);
                }
            }
        }
        return null;
    }

    // 查詢全部機器
    public List<MachinesBean> findAllMachines() throws Exception {
        List<MachinesBean> list = new ArrayList<>();
        String sql = "SELECT * FROM machines ORDER BY machine_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("machine_id");
                String name = rs.getString("machine_name");
                String serialNumber = rs.getString("serial_number");
                String status = rs.getString("mstatus");
                String location = rs.getString("machine_location");

                MachinesBean machine = new MachinesBean(id, name, serialNumber, status, location);
                list.add(machine);
            }
        }
        return list;
    }

    // 更新機器資料
    public void updateMachine(MachinesBean machine) throws Exception {
        String sql = "UPDATE machines SET machine_name = ?, serial_number = ?, mstatus = ?, machine_location = ? WHERE machine_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, machine.getMachineName());
            ps.setString(2, machine.getSerialNumber());
            ps.setString(3, machine.getMstatus());
            ps.setString(4, machine.getMachineLocation());
            ps.setInt(5, machine.getMachineId());
            ps.executeUpdate();
        }
    }

    // 刪除機器
    public void deleteMachine(int machineId) throws Exception {
        String sql = "DELETE FROM machines WHERE machine_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, machineId);
            ps.executeUpdate();
        }
    }

    // 依狀態查詢機器
    public List<MachinesBean> findMachinesByStatus(String status) throws Exception {
        List<MachinesBean> list = new ArrayList<>();
        String sql = "SELECT * FROM machines WHERE mstatus = ? ORDER BY machine_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("machine_id");
                    String name = rs.getString("machine_name");
                    String serialNumber = rs.getString("serial_number");
                    String mstatus = rs.getString("mstatus");
                    String location = rs.getString("machine_location");

                    MachinesBean machine = new MachinesBean(id, name, serialNumber, mstatus, location);
                    list.add(machine);
                }
            }
        }
        return list;
    }

    // 分頁查詢
    public List<MachinesBean> findMachinesByStatusWithPage(String status, int offset, int limit) throws Exception {
        List<MachinesBean> list = new ArrayList<>();
        String sql = "SELECT * FROM machines WHERE mstatus = ? ORDER BY machine_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("machine_id");
                    String name = rs.getString("machine_name");
                    String serialNumber = rs.getString("serial_number");
                    String mstatus = rs.getString("mstatus");
                    String location = rs.getString("machine_location");
                    MachinesBean machine = new MachinesBean(id, name, serialNumber, mstatus, location);
                    list.add(machine);
                }
            }
        }
        return list;
    }
 // 依 serial_number 模糊查詢機器
    public List<MachinesBean> findMachinesBySerialNumber(String serialNumberPattern) throws Exception {
        List<MachinesBean> list = new ArrayList<>();
        String sql = "SELECT * FROM machines WHERE serial_number LIKE ? ORDER BY machine_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + serialNumberPattern + "%");  // 模糊查詢用 % 包裹字串
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("machine_id");
                    String name = rs.getString("machine_name");
                    String serialNumber = rs.getString("serial_number");
                    String mstatus = rs.getString("mstatus");
                    String location = rs.getString("machine_location");

                    MachinesBean machine = new MachinesBean(id, name, serialNumber, mstatus, location);
                    list.add(machine);
                }
            }
        }
        return list;
    }
 // ... 其他方法略 ...

    public MachinesBean findMachineBySerialNumber(String serialNumber) throws Exception {
        String sql = "SELECT * FROM machines WHERE serial_number = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, serialNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 這裡要帶 machine_id，否則 Service 判斷會失效
                    return new MachinesBean(
                        rs.getInt("machine_id"),
                        rs.getString("machine_name"),
                        rs.getString("serial_number"),
                        rs.getString("mstatus"),
                        rs.getString("machine_location")
                    );
                }
            }
        }
        return null;
    }

}
