package com.machine.Dao;

import com.machine.Bean.MachineMaintenanceBean;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.util.List;

public class MachineMaintenanceDao {
    private Session session;
    
    public MachineMaintenanceDao(Session session) {
        this.session = session;
    }

    public MachineMaintenanceBean insertMaintenance(MachineMaintenanceBean maintenance) {
        if (maintenance != null) {
            if (maintenance.getScheduleDate() == null) {
                maintenance.setScheduleDate(LocalDateTime.now());
            }
            session.persist(maintenance);
            return maintenance;
        }
        return null;
    }

    public List<MachineMaintenanceBean> findAllMaintenances() {
        String hql = "FROM MachineMaintenanceBean ORDER BY scheduleId";
        Query<MachineMaintenanceBean> query = session.createQuery(hql, MachineMaintenanceBean.class);
        return query.getResultList();
    }

    public List<MachineMaintenanceBean> findAllMaintenancesDetail() {
        String hql = "FROM MachineMaintenanceBean m LEFT JOIN FETCH m.machine ORDER BY m.scheduleId";
        Query<MachineMaintenanceBean> query = session.createQuery(hql, MachineMaintenanceBean.class);
        return query.getResultList();
    }

    public MachineMaintenanceBean findMaintenanceDetailById(int scheduleId) {
        String hql = "FROM MachineMaintenanceBean m LEFT JOIN FETCH m.machine WHERE m.scheduleId = :scheduleId";
        Query<MachineMaintenanceBean> query = session.createQuery(hql, MachineMaintenanceBean.class);
        query.setParameter("scheduleId", scheduleId);
        List<MachineMaintenanceBean> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public MachineMaintenanceBean findMaintenanceById(int scheduleId) {
        return session.find(MachineMaintenanceBean.class, scheduleId);
    }

    public MachineMaintenanceBean persistMaintenance(MachineMaintenanceBean maintenance) {
        MachineMaintenanceBean resultBean = session.find(MachineMaintenanceBean.class, maintenance.getScheduleId());
        if (resultBean != null) {
            resultBean.setMachineId(maintenance.getMachineId());
            resultBean.setScheduleDate(LocalDateTime.now());
            resultBean.setMaintenanceDescription(maintenance.getMaintenanceDescription());
            resultBean.setMaintenanceStatus(maintenance.getMaintenanceStatus());
            resultBean.setEmployeeId(maintenance.getEmployeeId());
            return session.merge(resultBean);
        }
        return null;
    }

    public boolean removeMaintenance(int scheduleId) {
        MachineMaintenanceBean maintenance = session.find(MachineMaintenanceBean.class, scheduleId);
        if (maintenance != null) {
            session.remove(maintenance);
            return true;
        }
        return false;
    }
  
}