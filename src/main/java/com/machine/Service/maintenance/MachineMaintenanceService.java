package com.machine.Service.maintenance;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.machine.Bean.MachineMaintenanceBean;
import com.machine.Dao.MachineMaintenanceDao;
import com.machine.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MachineMaintenanceService {
    
    public List<MachineMaintenanceBean> findAllMaintenances() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineMaintenanceDao dao = new MachineMaintenanceDao(session);
            return dao.findAllMaintenances();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    public List<MachineMaintenanceBean> findAllMaintenancesDetail() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineMaintenanceDao dao = new MachineMaintenanceDao(session);
            return dao.findAllMaintenancesDetail();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    public MachineMaintenanceBean findMaintenanceDetailById(int scheduleId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineMaintenanceDao dao = new MachineMaintenanceDao(session);
            return dao.findMaintenanceDetailById(scheduleId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
    
    public boolean insertMaintenance(MachineMaintenanceBean maintenance) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            MachineMaintenanceDao dao = new MachineMaintenanceDao(session);
            
            if (maintenance.getScheduleDate() == null) {
                maintenance.setScheduleDate(LocalDateTime.now());
            }
            
            MachineMaintenanceBean result = dao.insertMaintenance(maintenance);
            
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
    
    public boolean updateMaintenance(MachineMaintenanceBean maintenance) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            MachineMaintenanceDao dao = new MachineMaintenanceDao(session);
            
            MachineMaintenanceBean result = dao.persistMaintenance(maintenance);
            
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
    
    public boolean deleteMaintenance(int scheduleId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            MachineMaintenanceDao dao = new MachineMaintenanceDao(session);
            
            boolean result = dao.removeMaintenance(scheduleId);
            
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
    
    public MachineMaintenanceBean findMaintenanceById(int scheduleId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineMaintenanceDao dao = new MachineMaintenanceDao(session);
            return dao.findMaintenanceById(scheduleId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
}