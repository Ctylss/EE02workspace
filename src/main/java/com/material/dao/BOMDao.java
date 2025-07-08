package com.material.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.material.model.BOM;

public class BOMDao extends InventoryConnectionPool {

	// 新增商品物料比例
	public void insertBOM(BOM bom) {
	    String sql = "INSERT INTO bill_of_material (product_id, material_id, quantity_per_unit) VALUES (?, ?, ?)";
	    try (
	        Connection conn = ds.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql)
	    ) {
	        ps.setInt(1, bom.getProductId());
	        ps.setInt(2, bom.getMaterialId());
	        ps.setBigDecimal(3, bom.getQuantityPerUnit());
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        System.err.println("BOMDao.insertBOM 出錯： " + e.getMessage());
	    }
	}
	
	// 驗證新增商品
	public boolean existsById(int productId) {
		  String sql="SELECT * FROM Product WHERE product_id=?";
		  try (Connection conn = ds.getConnection();
		       PreparedStatement ps = conn.prepareStatement(sql)) {
		    ps.setInt(1,productId);
		    try (ResultSet rs=ps.executeQuery()) {
		      return rs.next();
		    }
		  } catch(Exception e){ throw new RuntimeException(e);}
		}

	
	// 查詢多筆商品 ById
	public List<BOM> findBOMByProductId(Integer searchProductId) {
	    List<BOM> productBom = new ArrayList<>();
	    String sql = "SELECT bom_id, product_id, material_id, quantity_per_unit FROM bill_of_material WHERE product_id = ?";
	    try (
	        Connection conn = ds.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql)
	    ) {
	        ps.setInt(1, searchProductId);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                productBom.add(new BOM(
	                	rs.getInt("bom_id"),
	                    rs.getInt("product_id"),
	                    rs.getInt("material_id"),
	                    rs.getBigDecimal("quantity_per_unit")
	                ));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return productBom;
	}
	
	// 模糊查詢多筆商品ByProductName(bomQuery)
	public List<BOM> findBOMByName(String keyword) {
	    List<BOM> list = new ArrayList<>();
	    String sql =
	        "SELECT DISTINCT b.product_id, p.product_name, p.category " +
	        "FROM bill_of_material b " +
	        "JOIN Product p ON b.product_id = p.product_id " +
	        "WHERE LOWER(p.product_name) LIKE ? " +
	        "ORDER BY b.product_id";
	    try (
	        Connection conn = ds.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql)
	    ) {
	        ps.setString(1, "%" + keyword + "%");
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                list.add(new BOM(
	                    rs.getInt("product_id"),
	                    rs.getString("product_name"),
	                    rs.getString("category")
	                ));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	} //end 模糊查詢多筆ByProductName
	
	// 查詢商品用料ByProductId(prodBomQuery)
	public List<BOM> findProdBomByProdId(Integer productId){
		List<BOM> list = new ArrayList<>();
		String sql = "SELECT b.material_id material_id, b.quantity_per_unit quantity_per_unit, m.material_name material_name "
				   + "FROM bill_of_material b JOIN material m "
				   + "ON b.material_id = m.material_id "
				   + "WHERE b.product_id = ? "
				   + "ORDER BY b.material_id ";
		 try (
			        Connection conn = ds.getConnection();
			        PreparedStatement ps = conn.prepareStatement(sql)
			    ) {
			        ps.setInt(1, productId);
			        try (ResultSet rs = ps.executeQuery()) {
			            while (rs.next()) {
			                list.add(new BOM(
			                    rs.getInt("material_id"),
			                    rs.getString("material_name"),
			                    rs.getBigDecimal("quantity_per_unit")
			                ));
			            }
			        }
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
		
		return list;
	}
	
	// 刪除商品架構
	public void removeBOM(Integer productId) {
	    String sql = "DELETE FROM bill_of_material WHERE product_id = ?";
	    try (
	        Connection conn = ds.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql)
	    ) {
	        ps.setInt(1, productId);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}//end remove
	
	// 更新商品架構
	public void updateBOM(BOM bom) {
	    String sql = 
	        "UPDATE bill_of_material " +
	        "SET material_id = ?, quantity_per_unit = ? " +
	        "WHERE bom_id = ?";
	    try (
	        Connection conn = ds.getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql)
	    ) {
	        ps.setInt(1, bom.getMaterialId());
	        ps.setBigDecimal(2, bom.getQuantityPerUnit());
	        ps.setInt(3, bom.getBomId());
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        System.err.println("BOMDao.updateBOM 出錯： " + e.getMessage());
	    }
	}//end update
	
}
