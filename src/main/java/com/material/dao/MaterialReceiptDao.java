package com.material.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.material.model.MaterialReceipt;

public class MaterialReceiptDao extends InventoryConnectionPool {

	// 新增入庫紀錄
	public void saveReceipt(MaterialReceipt materialReceipt) {
		
        String sql = "INSERT INTO material_receipt "
                   + "(order_id, material_id, quantity, unit, receipt_date, "
                   + "supplier_id, supplier_name, description, location) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";


    	try(	Connection conn = ds.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql);
    			) {
			ps.setInt(1, materialReceipt.getOrderId());
			   ps.setInt(2, materialReceipt.getMaterialId());
			   ps.setBigDecimal(3, materialReceipt.getQuantity());
			   ps.setString(4, materialReceipt.getUnit());
			   ps.setDate(5, materialReceipt.getReceiptDate());

			   // supplier_id 處理：若為 0 視為 NULL
			   if (materialReceipt.getSupplierId() == 0) {
			       ps.setNull(6, java.sql.Types.INTEGER);
			   } else {
			       ps.setInt(6, materialReceipt.getSupplierId());
			   }

			   // supplier_name 處理
			   if (materialReceipt.getSupplierName() == null || materialReceipt.getSupplierName().trim().isEmpty()) {
			       ps.setNull(7, java.sql.Types.NVARCHAR);
			   } else {
			       ps.setString(7, materialReceipt.getSupplierName());
			   }

			   // description 處理
			   if (materialReceipt.getDescription() == null || materialReceipt.getDescription().trim().isEmpty()) {
			       ps.setNull(8, java.sql.Types.NVARCHAR);
			   } else {
			       ps.setString(8, materialReceipt.getDescription());
			   }

			   // location 處理
			   if (materialReceipt.getLocation() == null || materialReceipt.getLocation().trim().isEmpty()) {
			       ps.setNull(9, java.sql.Types.NVARCHAR);
			   } else {
			       ps.setString(9, materialReceipt.getLocation());
			   }

			   // 執行插入
			   ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} //end saveMR
	
	// 查詢 自動帶入採單內容By採購單ID
	public List<MaterialReceipt> autoCompleteByOrderId(Integer orderId) {
	    List<MaterialReceipt> mrList = new ArrayList<>();

	    String sql = "SELECT poi.material_id material_id, poi.quantity quantity, "
	               + "m.unit unit, m.location location, "
	               + "po.supplier_id supplier_id, s.supplier_name supplier_name "
	               + "FROM purchase_order_item poi "
	               + "JOIN material m ON poi.material_id = m.material_id "
	               + "JOIN purchase_order po ON poi.order_id = po.order_id "
	               + "JOIN supplier s ON po.supplier_id = s.supplier_id "
	               + "WHERE poi.order_id = ?";

	    
		try(	Connection conn = ds.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				) {
//	    	PreparedStatement ps = conn.prepareStatement(sql)
	        ps.setInt(1, orderId);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	            	MaterialReceipt mr = new MaterialReceipt();
	                mr.setOrderId(orderId);
	                mr.setMaterialId(rs.getInt("material_id"));
	                mr.setQuantity(rs.getBigDecimal("quantity"));
	                mr.setUnit(rs.getString("unit"));
	                mr.setLocation(rs.getString("location"));
	                mr.setSupplierId(rs.getInt("supplier_id"));
	                mr.setSupplierName(rs.getString("supplier_name"));
	                mrList.add(mr);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("查詢入庫資料時發生錯誤！");
	        e.printStackTrace();
	    }

	    return mrList;
	}

	
	// 查詢單筆入庫紀錄By採購單ID
	public List<MaterialReceipt> findMRByOrderId(int orderId){
		ArrayList<MaterialReceipt> mrList = new ArrayList<MaterialReceipt>();
		String sql = "SELECT * "
				   + "FROM material_receipt "
				   + "WHERE order_id=?";

		try(	Connection conn = ds.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				) {
            ps.setInt(1, orderId);
            try(ResultSet rs = ps.executeQuery()){            	
            	while (rs.next()) {
            		MaterialReceipt mr = new MaterialReceipt();
            		mr.setReceiptId(rs.getInt("receipt_id"));
            		mr.setOrderId(rs.getInt("order_id"));
            		mr.setMaterialId(rs.getInt("material_id"));
            		mr.setQuantity(rs.getBigDecimal("quantity"));
            		mr.setUnit(rs.getString("unit"));
            		mr.setReceiptDate(rs.getDate("receipt_date"));
            		mr.setSupplierId(rs.getInt("supplier_id"));
            		mr.setSupplierName(rs.getString("supplier_name"));
            		mr.setDescription(rs.getString("description"));
            		mr.setLocation(rs.getString("location"));
            		
            		mrList.add(mr);
            	}
            
            }

        } catch (SQLException e) {
            System.err.println("查詢 material_receipt 時發生錯誤！");
            e.printStackTrace();
        }		
		return mrList;
	}
	
	// 查詢多筆入庫紀錄By物料ID
	public List<MaterialReceipt> findMRByMatarialId(int materialId){
		ArrayList<MaterialReceipt> mrList = new ArrayList<MaterialReceipt>();
		String sql = "SELECT * "
				+ "FROM material_receipt "
				+ "WHERE material_id=?";
		


    	try(	Connection conn = ds.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql);
    			) {
            ps.setInt(1, materialId);
            
            try(ResultSet rs = ps.executeQuery()){
            	while (rs.next()) {
            		MaterialReceipt mr = new MaterialReceipt();
            		mr.setReceiptId(rs.getInt("receipt_id"));
            		mr.setOrderId(rs.getInt("order_id"));
            		mr.setMaterialId(rs.getInt("material_id"));
            		mr.setQuantity(rs.getBigDecimal("quantity"));
            		mr.setUnit(rs.getString("unit"));
            		mr.setReceiptDate(rs.getDate("receipt_date"));
            		mr.setSupplierId(rs.getInt("supplier_id"));
            		mr.setSupplierName(rs.getString("supplier_name"));
            		mr.setDescription(rs.getString("description"));
            		mr.setLocation(rs.getString("location"));
            		
            		mrList.add(mr);
            	}            	
            }

        } catch (SQLException e) {
            System.err.println("查詢 material_receipt 發生錯誤！");
            e.printStackTrace();
        }
		
		return mrList;
	}
	
	// 查詢多筆入庫紀錄By供應商ID
	public List<MaterialReceipt> findMRSupplierId(int supplierId){
		ArrayList<MaterialReceipt> mrList = new ArrayList<MaterialReceipt>();
		String sql = "SELECT * "
				   + "FROM material_receipt "
				   + "WHERE supplier_id=?";

    	try(	Connection conn = ds.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql);
    			) {
            ps.setInt(1, supplierId);

            try(ResultSet rs = ps.executeQuery()){
            	while (rs.next()) {
            		MaterialReceipt mr = new MaterialReceipt();
            		mr.setReceiptId(rs.getInt("receipt_id"));
            		mr.setOrderId(rs.getInt("order_id"));
            		mr.setMaterialId(rs.getInt("material_id"));
            		mr.setQuantity(rs.getBigDecimal("quantity"));
            		mr.setUnit(rs.getString("unit"));
            		mr.setReceiptDate(rs.getDate("receipt_date"));
            		mr.setSupplierId(rs.getInt("supplier_id"));
            		mr.setSupplierName(rs.getString("supplier_name"));
            		mr.setDescription(rs.getString("description"));
            		mr.setLocation(rs.getString("location"));
            		
            		mrList.add(mr);
            	}            	
            }

        } catch (SQLException e) {
            System.err.println("查詢 material_receipt 資料時發生錯誤！");
            e.printStackTrace();
        }
		return mrList;
	}

	// 模糊查詢多筆入庫紀錄By敘述欄位
	public List<MaterialReceipt> findDesByWords(String keyword){
		ArrayList<MaterialReceipt> mrList = new ArrayList<MaterialReceipt>();
		String sql = "SELECT * "
				   + "FROM material_receipt "
				   + "WHERE description LIKE ?";

    	try(	Connection conn = ds.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql);
    			) {

			ps.setString(1,"%"+keyword+"%");

            try(ResultSet rs = ps.executeQuery()){
            	while (rs.next()) {
            		MaterialReceipt mr = new MaterialReceipt();
            		mr.setReceiptId(rs.getInt("receipt_id"));
            		mr.setOrderId(rs.getInt("order_id"));
            		mr.setMaterialId(rs.getInt("material_id"));
            		mr.setQuantity(rs.getBigDecimal("quantity"));
            		mr.setUnit(rs.getString("unit"));
            		mr.setReceiptDate(rs.getDate("receipt_date"));
            		mr.setSupplierId(rs.getInt("supplier_id"));
            		mr.setSupplierName(rs.getString("supplier_name"));
            		mr.setDescription(rs.getString("description"));
            		mr.setLocation(rs.getString("location"));
            		
            		mrList.add(mr);
            	}            	
            }

        } catch (SQLException e) {
            System.err.println("查詢 material_receipt 資料時發生錯誤！");
            e.printStackTrace();
        }
		return mrList;
	}

    // -------- 期末分割線 ---------
	
	// 模糊查詢多筆入庫紀錄By物料名稱

	// 查詢多筆入庫紀錄By日期範圍

	// 模糊查詢多筆入庫紀錄By供應商名稱

	
	
}
