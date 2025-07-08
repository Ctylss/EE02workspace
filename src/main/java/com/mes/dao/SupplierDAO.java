package com.mes.dao;
import com.mes.bean.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
	
	// 取得所有尚未下架的供應商(active = 1)
    public List<Supplier> getActiveSuppliers(Connection conn) throws Exception {
        String sql = "SELECT * FROM Supplier WHERE active = 1 ORDER BY supplier_id";
        List<Supplier> list = new ArrayList<>(); //list裝查詢出來的每一筆供應商資料

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {  //執行 SQL 查詢並回傳結果到 rs
            while (rs.next()) { //只要還有下一筆資料，就進入迴圈
                Supplier s = new Supplier();// SQL Server 不分大小寫
                s.setSupplierId(rs.getInt("supplier_id")); //從目前的資料列中取出 SQL資料表中的 supplier_id 欄位值 再轉整數存到s
                s.setSupplierName(rs.getString("supplier_name"));
                s.setPm(rs.getString("pm"));
                s.setSupplierPhone(rs.getString("supplier_phone"));
                s.setSupplierEmail(rs.getString("supplier_email"));
                s.setSupplierAddress(rs.getString("supplier_address"));
                list.add(s); //把每一筆查詢結果組成一個s物件 並存放在list
            }
        }
        return list;//回傳所有查到的供應商資料
    }

    // 下架供應商（將 active 設為 0）
    public void deactivateSupplier(Connection conn, int supplierId) throws Exception {
        String sql = "UPDATE Supplier SET active = 0 WHERE supplier_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);
            stmt.executeUpdate();
        }
    }
    
    //新增供應商
    public void insertSupplier(Connection conn,String name, String pm, String phone, String email, String address) throws SQLException {
    	String sql = "INSERT INTO Supplier (supplier_name, pm, supplier_phone, supplier_email, supplier_address, active) VALUES (?, ?, ?, ?, ?, 1)";
    	try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    		 stmt.setString(1, name);
    		 stmt.setString(2, pm);
    		 stmt.setString(3, phone);
    		 stmt.setString(4, email);
    		 stmt.setString(5, address);
    		 stmt.executeUpdate();
    	 }
    }
    
    //更新供應商
    public boolean updateSupplier(Connection conn, int id, String name, String pm, String phone, String email, String address) throws Exception {
        String sql = "UPDATE Supplier SET supplier_name=?, pm=?, supplier_phone=?, supplier_email=?, supplier_address=? WHERE supplier_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, pm);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, address);
            stmt.setInt(6, id);
            return stmt.executeUpdate() > 0; //有成功更新 至少 1 筆資料，就回傳 true；否則 false
        }
    }
    
    public Supplier getSupplierById(Connection conn, int supplierId) throws Exception {
        String sql = "SELECT * FROM Supplier WHERE supplier_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setSupplierId(rs.getInt("supplier_id"));
                    supplier.setSupplierName(rs.getString("supplier_name"));
                    supplier.setPm(rs.getString("pm"));
                    supplier.setSupplierPhone(rs.getString("supplier_phone"));
                    supplier.setSupplierEmail(rs.getString("supplier_email"));
                    supplier.setSupplierAddress(rs.getString("supplier_address"));
                    return supplier;
                } else {
                    return null;
                }
            }
        }
    }


}//end

