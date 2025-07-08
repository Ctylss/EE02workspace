package com.material.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.material.model.BOM;
import com.material.model.Material;

public class MaterialDao extends InventoryConnectionPool {

	// 新增物料
    public void createMaterial(Material material) {
        String sql = "INSERT INTO material (material_name, unit, material_description, location,"
                   + " stock_current, stock_in_shipping, stock_reserved, stock_available)"
                   + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, material.getMaterialName());
            ps.setString(2, material.getUnit());
            ps.setString(3, material.getMaterialDescription());
            ps.setString(4, material.getLocation());
            ps.setBigDecimal(5, BigDecimal.ZERO);
            ps.setBigDecimal(6, BigDecimal.ZERO);
            ps.setBigDecimal(7, BigDecimal.ZERO);
            ps.setBigDecimal(8, BigDecimal.ZERO);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //end create
	
	// 查詢單筆物料庫存 ById
    public List<Material> findMaterialById(Integer id) {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT material_id, material_name, location, "
                   + "stock_available, stock_current, unit, material_description "
                   + "FROM material WHERE material_id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Material(
                        rs.getInt("material_id"),
                        rs.getString("material_name"),
                        rs.getString("location"),
                        rs.getBigDecimal("stock_available"),
                        rs.getBigDecimal("stock_current"),
                        rs.getString("unit"),
                        rs.getString("material_description")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
	
	// 查詢多筆物料庫存 BySql模糊查詢
    public List<Material> findMaterialByName(String keyword) {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT * FROM material WHERE material_name LIKE ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Material(
                        rs.getInt("material_id"),
                        rs.getString("material_name"),
                        rs.getString("location"),
                        rs.getBigDecimal("stock_available"),
                        rs.getBigDecimal("stock_current"),
                        rs.getString("unit"),
                        rs.getString("material_description")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } //end 模糊查詢多筆
	
	// 查詢多筆物料ByLoc
    public List<Material> findMaterialByLocation(String keyword) {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT * FROM material WHERE location LIKE ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Material(
                        rs.getInt("material_id"),
                        rs.getString("material_name"),
                        rs.getString("location"),
                        rs.getBigDecimal("stock_available"),
                        rs.getBigDecimal("stock_current"),
                        rs.getString("unit"),
                        rs.getString("material_description")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } // end searchByLoc
	
	// 更新物料
    public void updateMaterial(Material material) {
        String sql = "UPDATE material SET material_name = ?, unit = ?, material_description = ?,"
                   + " location = ? WHERE material_id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, material.getMaterialName());
            ps.setString(2, material.getUnit());
            ps.setString(3, material.getMaterialDescription());
            ps.setString(4, material.getLocation());
            ps.setInt(5, material.getMaterialId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("updateMaterial 出錯：" + e.getMessage());
        }
    }//end update
} 
