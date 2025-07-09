package com.machine.Dao;

import com.machine.Bean.MachinesBean;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class MachinesDao {
    private Session session;
    
    public MachinesDao(Session session) {
        this.session = session;
    }

    // 新增機器
    public void insertMachine(MachinesBean machine) throws Exception {
        if (machine != null) {
            session.persist(machine);
        }
    }

    // 依機器ID查詢
    public MachinesBean findMachineById(int machineId) throws Exception {
        return session.find(MachinesBean.class, machineId);
    }

    // 查詢全部機器
    public List<MachinesBean> findAllMachines() throws Exception {
        String hql = "FROM MachinesBean ORDER BY machineId";
        Query<MachinesBean> query = session.createQuery(hql, MachinesBean.class);
        return query.getResultList();
    }

    // 更新機器資料
    public void updateMachine(MachinesBean machine) throws Exception {
        session.merge(machine);
    }

    // 刪除機器
    public void deleteMachine(int machineId) throws Exception {
        MachinesBean machine = session.find(MachinesBean.class, machineId);
        if (machine != null) {
            session.remove(machine);
        }
    }

    // 依狀態查詢機器
    public List<MachinesBean> findMachinesByStatus(String status) throws Exception {
        String hql = "FROM MachinesBean WHERE mstatus = :status ORDER BY machineId";
        Query<MachinesBean> query = session.createQuery(hql, MachinesBean.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    // 分頁查詢
    public List<MachinesBean> findMachinesByStatusWithPage(String status, int offset, int limit) throws Exception {
        String hql = "FROM MachinesBean WHERE mstatus = :status ORDER BY machineId";
        Query<MachinesBean> query = session.createQuery(hql, MachinesBean.class);
        query.setParameter("status", status);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    // 依 serial_number 模糊查詢機器
    public List<MachinesBean> findMachinesBySerialNumber(String serialNumberPattern) throws Exception {
        String hql = "FROM MachinesBean WHERE serialNumber LIKE :pattern ORDER BY machineId";
        Query<MachinesBean> query = session.createQuery(hql, MachinesBean.class);
        query.setParameter("pattern", "%" + serialNumberPattern + "%");
        return query.getResultList();
    }

    public MachinesBean findMachineBySerialNumber(String serialNumber) throws Exception {
        String hql = "FROM MachinesBean WHERE serialNumber = :serialNumber";
        Query<MachinesBean> query = session.createQuery(hql, MachinesBean.class);
        query.setParameter("serialNumber", serialNumber);
        List<MachinesBean> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}