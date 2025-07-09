package com.machine.Dao;

import com.machine.Bean.MachineRepairBean;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.util.List;

public class MachineRepairDao {
    private Session session;
    
    public MachineRepairDao(Session session) {
        this.session = session;
    }

    public MachineRepairBean insertRepair(MachineRepairBean repair) {
        if (repair != null) {
            if (repair.getRepairTime() == null) {
                repair.setRepairTime(LocalDateTime.now());
            }
            session.persist(repair);
            return repair;
        }
        return null;
    }

    public List<MachineRepairBean> findAllRepairs() {
        String hql = "FROM MachineRepairBean ORDER BY repairTime DESC";
        Query<MachineRepairBean> query = session.createQuery(hql, MachineRepairBean.class);
        return query.getResultList();
    }

    public MachineRepairBean persistRepair(MachineRepairBean repair) {
        MachineRepairBean resultBean = session.find(MachineRepairBean.class, repair.getRepairId());
        if (resultBean != null) {
            resultBean.setMachineId(repair.getMachineId());
            resultBean.setRepairDescription(repair.getRepairDescription());
            resultBean.setRepairTime(repair.getRepairTime());
            resultBean.setRepairStatus(repair.getRepairStatus());
            resultBean.setEmployeeId(repair.getEmployeeId());
            return session.merge(resultBean);
        }
        return null;
    }

    public List<MachineRepairBean> machineRepairView() {
        String hql = "FROM MachineRepairBean r LEFT JOIN FETCH r.machine ORDER BY r.repairTime DESC";
        Query<MachineRepairBean> query = session.createQuery(hql, MachineRepairBean.class);
        return query.getResultList();
    }

    public MachineRepairBean findRepairById(int repairId) {
        String hql = "FROM MachineRepairBean r LEFT JOIN FETCH r.machine WHERE r.repairId = :repairId";
        Query<MachineRepairBean> query = session.createQuery(hql, MachineRepairBean.class);
        query.setParameter("repairId", repairId);
        List<MachineRepairBean> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public boolean persistRepairStatus(int repairId, String status) {
        String hql = "UPDATE MachineRepairBean SET repairStatus = :status WHERE repairId = :repairId";
        Query query = session.createQuery(hql);
        query.setParameter("status", status);
        query.setParameter("repairId", repairId);
        int result = query.executeUpdate();
        return result > 0;
    }

    public List<MachineRepairBean> findRepairsByStatus(String status) {
        String hql = "FROM MachineRepairBean r LEFT JOIN FETCH r.machine WHERE r.repairStatus = :status ORDER BY r.repairTime DESC";
        Query<MachineRepairBean> query = session.createQuery(hql, MachineRepairBean.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<MachineRepairBean> findRepairsByMachineId(int machineId) {
        String hql = "FROM MachineRepairBean r LEFT JOIN FETCH r.machine WHERE r.machineId = :machineId ORDER BY r.repairTime DESC";
        Query<MachineRepairBean> query = session.createQuery(hql, MachineRepairBean.class);
        query.setParameter("machineId", machineId);
        return query.getResultList();
    }
}