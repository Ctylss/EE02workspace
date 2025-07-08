package com.mes.dao;

import com.mes.bean.Material;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class MaterialDAO {

    // 取得所有 active = 1 的物料清單
    public List<Material> getActiveMaterials(Session session) {      
        // **修改這裡：將 "FROM Material" 改為 "FROM MESMaterial"**
        String sql = "FROM MESMaterial WHERE active = true ORDER BY materialId";
        Query<Material> query = session.createQuery(sql, Material.class);
        return query.getResultList();
    }
}