package com.mes.dao;

import com.mes.bean.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    // 取得所有 active = 1 的物料清單
    public List<Material> getActiveMaterials(Connection conn) throws Exception {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT * FROM Material WHERE active = 1 ORDER BY material_id";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Material m = new Material();
                m.setMaterialId(rs.getInt("material_id"));
                m.setMaterialName(rs.getString("material_name"));
                m.setUnit(rs.getString("unit"));
                m.setMaterialDescription(rs.getString("material_description"));
                m.setLocation(rs.getString("location"));
                m.setStockCurrent(rs.getBigDecimal("stock_current"));
                m.setStockReserved(rs.getBigDecimal("stock_reserved"));
                m.setStockInShipping(rs.getBigDecimal("stock_in_shipping"));
                m.setSafetyStock(rs.getInt("safety_stock"));
                m.setReorderLevel(rs.getInt("reorder_level"));
                m.setActive(rs.getBoolean("active"));
                
                list.add(m);
            }
        }

        return list;
    }
}
