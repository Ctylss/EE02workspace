package com.machine.Dao;

import com.machine.Bean.MachineRepairBean;
import com.machine.Bean.MachineRepairJoinBean;
import com.machine.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MachineRepairDao {

	// 新增維修紀錄（repair_time 使用資料庫 GETDATE() 自動產生）
	public void insertRepair(MachineRepairBean repair) throws Exception {
		String sql = "INSERT INTO machine_repair (machine_id, repair_description, repair_time, repair_status, employee_id) VALUES (?, ?, GETDATE(), ?, ?)";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, repair.getMachineId());
			ps.setString(2, repair.getRepairDescription());
			ps.setString(3, repair.getRepairStatus());
			ps.setInt(4, repair.getEmployeeId());

			ps.executeUpdate();
		}
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

				MachineRepairBean repair = new MachineRepairBean(id, machineId, description, repairTime, status,
						employeeId);
				list.add(repair);
			}
		}
		return list;
	}

	// 更新維修紀錄
	public void updateRepair(MachineRepairBean repair) throws Exception {
		String sql = "UPDATE machine_repair SET machine_id = ?, repair_description = ?, repair_time = ?, repair_status = ?, employee_id = ? WHERE repair_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, repair.getMachineId());
			ps.setString(2, repair.getRepairDescription());
			ps.setTimestamp(3, Timestamp.valueOf(repair.getRepairTime()));
			ps.setString(4, repair.getRepairStatus());
			ps.setInt(5, repair.getEmployeeId());
			ps.setInt(6, repair.getRepairId());

			ps.executeUpdate();
		}
	}

	//
	public List<MachineRepairJoinBean> machineRepairView() throws Exception {
		List<MachineRepairJoinBean> list = new ArrayList<>();
		String sql = "SELECT r.repair_id, r.machine_id, m.machine_name, r.repair_description, "
				+ "r.repair_time, r.repair_status, r.employee_id "
				+ "FROM machine_repair r JOIN machines m ON r.machine_id = m.machine_id "
				+ "ORDER BY r.repair_time DESC";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int id = rs.getInt("repair_id");
				int machineId = rs.getInt("machine_id");
				String machineName = rs.getString("machine_name");
				String description = rs.getString("repair_description");
				LocalDateTime repairTime = rs.getTimestamp("repair_time").toLocalDateTime();
				String status = rs.getString("repair_status");
				int employeeId = rs.getInt("employee_id");

				MachineRepairJoinBean repair = new MachineRepairJoinBean(id, machineId, machineName, description,
						repairTime, status, employeeId);

				list.add(repair);
			}
		}
		return list;
	}

	public MachineRepairJoinBean findRepairById(int repairId) throws Exception {
		String sql = "SELECT r.repair_id, r.machine_id, m.machine_name, r.repair_description, "
				+ "r.repair_time, r.repair_status, r.employee_id "
				+ "FROM machine_repair r JOIN machines m ON r.machine_id = m.machine_id " + "WHERE r.repair_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, repairId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new MachineRepairJoinBean(rs.getInt("repair_id"), rs.getInt("machine_id"),
							rs.getString("machine_name"), rs.getString("repair_description"),
							rs.getTimestamp("repair_time").toLocalDateTime(), rs.getString("repair_status"),
							rs.getInt("employee_id"));
				}
			}
		}
		return null;
	}

	// 只更新狀態，比 updateRepair()
	public void updateRepairStatus(int repairId, String Status) throws Exception {
		String sql = "UPDATE machine_repair SET repair_status = ? WHERE repair_id = ?";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, Status);
			ps.setInt(2, repairId);

			ps.executeUpdate();
		}
	}

	// 管理員可能想只看「待處理」的記錄
	public List<MachineRepairJoinBean> findRepairsByStatus(String status) throws Exception {
		List<MachineRepairJoinBean> list = new ArrayList<>();
		String sql = "SELECT r.repair_id, r.machine_id, m.machine_name, r.repair_description, "
				+ "r.repair_time, r.repair_status, r.employee_id "
				+ "FROM machine_repair r JOIN machines m ON r.machine_id = m.machine_id " + "WHERE r.repair_status = ? "
				+ "ORDER BY r.repair_time DESC";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, status);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					MachineRepairJoinBean repair = new MachineRepairJoinBean(rs.getInt("repair_id"),
							rs.getInt("machine_id"), rs.getString("machine_name"), rs.getString("repair_description"),
							rs.getTimestamp("repair_time").toLocalDateTime(), rs.getString("repair_status"),
							rs.getInt("employee_id"));
					list.add(repair);
				}
			}
		}
		return list;
	}

   //用機台id查詢
	public List<MachineRepairJoinBean> findRepairsByMachineId(int machineId) throws Exception {
		List<MachineRepairJoinBean> list = new ArrayList<>();
		String sql = "SELECT r.repair_id, r.machine_id, m.machine_name, r.repair_description, "
				+ "r.repair_time, r.repair_status, r.employee_id "
				+ "FROM machine_repair r JOIN machines m ON r.machine_id = m.machine_id " + "WHERE r.machine_id = ? "
				+ "ORDER BY r.repair_time DESC";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, machineId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) { // 改成 while，取得所有記錄
					MachineRepairJoinBean repair = new MachineRepairJoinBean(rs.getInt("repair_id"),
							rs.getInt("machine_id"), rs.getString("machine_name"), rs.getString("repair_description"),
							rs.getTimestamp("repair_time").toLocalDateTime(), rs.getString("repair_status"),
							rs.getInt("employee_id"));
					list.add(repair);
				}
			}
		}
		return list;
	}

}
