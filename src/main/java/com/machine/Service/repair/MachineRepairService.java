package com.machine.Service.repair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.machine.Bean.MachineRepairBean;
import com.machine.Dao.MachineRepairDao;
import com.machine.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MachineRepairService {
    
    public boolean insertRepair(MachineRepairBean repair) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            MachineRepairDao dao = new MachineRepairDao(session);
            
            if (repair.getRepairTime() == null) {
                repair.setRepairTime(LocalDateTime.now());
            }
            
            MachineRepairBean result = dao.insertRepair(repair);
            
            if (result != null) {
                transaction.commit();
                return true;
            } else {
                transaction.rollback();
                return false;
            }
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    public List<MachineRepairBean> machineRepairView() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineRepairDao dao = new MachineRepairDao(session);
            return dao.machineRepairView();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    public MachineRepairBean findRepairById(int repairId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineRepairDao dao = new MachineRepairDao(session);
            return dao.findRepairById(repairId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
    
    public boolean updateRepairStatus(int repairId, String newStatus) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            MachineRepairDao dao = new MachineRepairDao(session);
            
            boolean result = dao.persistRepairStatus(repairId, newStatus);
            
            if (result) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
            
            return result;
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    public List<MachineRepairBean> getRepairsByStatus(String status) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineRepairDao dao = new MachineRepairDao(session);
            return dao.findRepairsByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }

    public List<MachineRepairBean> getAllRepairsForAdmin() {
        return machineRepairView();
    }
    
    public List<MachineRepairBean> findRepairsByMachineId(int machineId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineRepairDao dao = new MachineRepairDao(session);
            return dao.findRepairsByMachineId(machineId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    public List<MachineRepairBean> findRepairsByStatus(String status) {
        return getRepairsByStatus(status);
    }
}