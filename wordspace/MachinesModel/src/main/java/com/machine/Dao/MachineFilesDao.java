package com.machine.Dao;

import com.machine.Bean.MachineFilesBean;
import com.machine.util.DBUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MachineFilesDao {

	// 新增檔案記錄
	public void insertFile(MachineFilesBean file) throws Exception {
		String sql = "INSERT INTO machine_files (machine_id, file_name, file_path, upload_time) VALUES (?, ?, ?, GETDATE())";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, file.getMachineId());
			ps.setString(2, file.getFileName());
			ps.setString(3, file.getFilePath());

			ps.executeUpdate();
		}
	}

	// 查詢單筆資料 by file_id
	public MachineFilesBean findFilesById(int fileId) throws Exception {
		String sql = "SELECT * FROM machine_files WHERE file_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, fileId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int id = rs.getInt("file_id");
					int machineId = rs.getInt("machine_id");
					String fileName = rs.getString("file_name");
					String filePath = rs.getString("file_path");
					LocalDateTime uploadTime = rs.getTimestamp("upload_time").toLocalDateTime();

					return new MachineFilesBean(id, machineId, fileName, filePath, uploadTime);
				}
			}
		}
		return null;
	}

	// 查詢所有檔案（依上傳時間排序）
	public List<MachineFilesBean> findAllFiles() throws Exception {
		List<MachineFilesBean> list = new ArrayList<>();
		String sql = "SELECT * FROM machine_files ORDER BY upload_time DESC";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				int id = rs.getInt("file_id");
				int machineId = rs.getInt("machine_id");
				String fileName = rs.getString("file_name");
				String filePath = rs.getString("file_path");
				LocalDateTime uploadTime = rs.getTimestamp("upload_time").toLocalDateTime();

				MachineFilesBean file = new MachineFilesBean(id, machineId, fileName, filePath, uploadTime);
				list.add(file);
			}
		}
		return list;
	}

	// 刪除檔案 by file_id
	public void deleteFilesById(int fileId) throws Exception {
		String sql = "DELETE FROM machine_files WHERE file_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, fileId);
			ps.executeUpdate();
		}
	}

	// 依照機台id查詢
	public List<MachineFilesBean> findFilesByMachineId(int machineId) throws Exception {
		List<MachineFilesBean> list = new ArrayList<>();
		String sql = "SELECT * FROM machine_files WHERE machine_id = ? ORDER BY upload_time DESC";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, machineId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("file_id");
					int mId = rs.getInt("machine_id");
					String fileName = rs.getString("file_name");
					String filePath = rs.getString("file_path");
					LocalDateTime uploadTime = rs.getTimestamp("upload_time").toLocalDateTime();

					MachineFilesBean file = new MachineFilesBean(id, mId, fileName, filePath, uploadTime);
					list.add(file);
				}
			}
		}
		return list;
	}

	// 更新檔案
	public void updateFile(MachineFilesBean file) throws Exception {
		String sql = "UPDATE machine_files SET machine_id = ?, file_name = ?, file_path = ? WHERE file_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, file.getMachineId());
			ps.setString(2, file.getFileName());
			ps.setString(3, file.getFilePath());
			ps.setInt(4, file.getFileId());

			ps.executeUpdate();
		}
	}

	// 根據檔案名稱模糊查詢
	public List<MachineFilesBean> findFilesByFileName(String keyword) throws Exception {
		List<MachineFilesBean> list = new ArrayList<>();
		String sql = "SELECT * FROM machine_files WHERE file_name LIKE ? ORDER BY upload_time DESC";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, "%" + keyword + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt("file_id");
					int machineId = rs.getInt("machine_id");
					String fileName = rs.getString("file_name");
					String filePath = rs.getString("file_path");
					LocalDateTime uploadTime = rs.getTimestamp("upload_time").toLocalDateTime();

					MachineFilesBean file = new MachineFilesBean(id, machineId, fileName, filePath, uploadTime);
					list.add(file);
				}
			}
		}
		return list;
	}
}
