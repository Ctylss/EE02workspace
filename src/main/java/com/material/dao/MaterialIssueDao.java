package com.material.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.material.model.MaterialIssue;

public class MaterialIssueDao extends InventoryConnectionPool{
	// 新增出庫紀錄
    public void saveIssue(MaterialIssue mi) {
        String sql = "INSERT INTO material_issue (work_order_id, material_id, quantity, issue_date, description)" +
                     " VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mi.getWorkOrderId());
            ps.setInt(2, mi.getMaterialId());
            ps.setBigDecimal(3, mi.getQuantity());
            ps.setDate(4, mi.getIssueDate());
            ps.setString(5, mi.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	// 自動帶入物料IdsBy工單ID
    public List<Integer> findMaterialIdsByWorkOrderId(int workOrderId) {
        List<Integer> materialIds = new ArrayList<>();
        String sql = "SELECT bom.material_id " 
                   + "FROM work_order wo "
        		   + "JOIN bill_of_material bom ON wo.product_id = bom.product_id "
        		   + "JOIN material m ON bom.material_id = m.material_id "
        		   + "WHERE wo.work_order_id = ?";

        try( Connection conn = ds.getConnection();
    	        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workOrderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    materialIds.add(rs.getInt("material_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("findMaterialIdsByWorkOrderId 發生錯誤！");
            e.printStackTrace();
        }
        return materialIds;
    }
	
	// 查詢單筆紀錄By工單ID
	public List<MaterialIssue> findMIByWorkOrderId(int workOrderId) {
	    ArrayList<MaterialIssue> miList = new ArrayList<MaterialIssue>();
	    String sql = "SELECT * "
	               + "FROM material_issue "
	               + "WHERE work_order_id = ?";

	   try(	Connection conn = ds.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				) { 
	        ps.setInt(1, workOrderId);
	        
	        try(ResultSet rs = ps.executeQuery()){
		        while (rs.next()) {
		            MaterialIssue mi = new MaterialIssue();
		            mi.setIssueId(rs.getInt("issue_id"));
		            mi.setWorkOrderId(rs.getInt("work_order_id"));
		            mi.setMaterialId(rs.getInt("material_id"));
		            mi.setQuantity(rs.getBigDecimal("quantity"));
		            mi.setIssueDate(rs.getDate("issue_date"));
		            mi.setDescription(rs.getString("description"));

		            miList.add(mi);
		        }

	        }

	    } catch (SQLException e) {
	        System.err.println("查詢 material_issue 時發生錯誤！");
	        e.printStackTrace();
	    }

	    return miList;
	}
	
    // 查詢多筆紀錄 By 物料 ID
    public List<MaterialIssue> findMIByMaterialId(int materialId) {
        List<MaterialIssue> list = new ArrayList<>();
        String sql = "SELECT * FROM material_issue WHERE material_id = ?";
    	try(	Connection conn = ds.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql);
    			) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialIssue mi = mapRowToMaterialIssue(rs);
                    list.add(mi);
                }
            }
        } catch (SQLException e) {
            System.err.println("findByMaterialId 發生錯誤！");
            e.printStackTrace();
        }
        return list;
    }

    // 模糊查詢多筆紀錄 By 敘述 (description LIKE %keyword%)
    public List<MaterialIssue> findMIByDescription(String keyword) {
        List<MaterialIssue> list = new ArrayList<>();
        String sql = "SELECT * FROM material_issue WHERE description LIKE ?";

    	try(	Connection conn = ds.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql);
    			) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialIssue mi = mapRowToMaterialIssue(rs);
                    list.add(mi);
                }
            }
        } catch (SQLException e) {
            System.err.println("findByDescription 發生錯誤！");
            e.printStackTrace();
        }
        return list;
    }

    // 查詢多筆紀錄 By 出庫日期區間 (issue_date BETWEEN start AND end)
    public List<MaterialIssue> findMIByIssueDateRange(Date startDate, Date endDate) {
        List<MaterialIssue> list = new ArrayList<>();
        String sql = "SELECT * FROM material_issue WHERE issue_date BETWEEN ? AND ?";

    	try(	Connection conn = ds.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql);
    			) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialIssue mi = mapRowToMaterialIssue(rs);
                    list.add(mi);
                }
            }
        } catch (SQLException e) {
            System.err.println("findByIssueDateRange 發生錯誤！");
            e.printStackTrace();
        }
        return list;
    }

    // 共用的 ResultSet → JavaBean 映射方法
    private MaterialIssue mapRowToMaterialIssue(ResultSet rs) throws SQLException {
        MaterialIssue mi = new MaterialIssue();
        mi.setIssueId(rs.getInt("issue_id"));
        mi.setWorkOrderId(rs.getInt("work_order_id"));
        mi.setMaterialId(rs.getInt("material_id"));
        mi.setQuantity(rs.getBigDecimal("quantity"));
        mi.setIssueDate(rs.getDate("issue_date"));
        mi.setDescription(rs.getString("description"));
        return mi;
    }

}
