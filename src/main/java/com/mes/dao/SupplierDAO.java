package com.mes.dao;
import com.mes.bean.*;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class SupplierDAO {

	// 取得所有尚未下架的供應商(active = 1)
    public List<Supplier> getActiveSuppliers(Session session) {
        String sql = "FROM Supplier WHERE active = true ORDER BY supplierId";
        Query<Supplier> query = session.createQuery(sql,Supplier.class); 
        return query.getResultList();
    }

    // 下架供應商（將 active 設為 0）
    public void deactivateSupplier(Session session, int supplierId) {
       Supplier supplier = session.find(Supplier.class , supplierId);
        if(supplier != null){
            supplier.setActive(false);
            session.merge(supplier);
        }       
    }
    
    //新增供應商
    public void insertSupplier(Session session,String name, String pm, String phone, String email, String address)  {
    	Supplier s = new Supplier();
    		 s.setSupplierName(name);
    		 s.setPm(pm);
    		 s.setSupplierPhone(phone);
    		 s.setSupplierEmail(email);
    		 s.setSupplierAddress(address);
             s.setActive(true); //預設為1啟用
    		 session.persist(s);	
    }
    
    //更新供應商
    public boolean updateSupplier(Session session, int id, String name, String pm, String phone, String email, String address) {
       Supplier supplier = session.find(Supplier.class, id);
       if(supplier != null){
             supplier.setSupplierName(name);
    		 supplier.setPm(pm);
    		 supplier.setSupplierPhone(phone);
    		 supplier.setSupplierEmail(email);
    		 supplier.setSupplierAddress(address);
             session.merge(supplier);
             return true;
       }
       return false;             
    }
    //查詢單筆ID
    public Supplier getSupplierById(Session session, int supplierId) {
        return session.find(Supplier.class, supplierId);
    }
}//end

