package com.machine.Dao;

import com.machine.Bean.MachineFilesBean;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class MachineFilesDao {
    private Session session;
    
    public MachineFilesDao(Session session) {
        this.session = session;
    }

    public List<MachineFilesBean> findAllFiles() {
        String hql = "FROM MachineFilesBean ORDER BY uploadTime DESC";
        Query<MachineFilesBean> query = session.createQuery(hql, MachineFilesBean.class);
        return query.getResultList();
    }

    public MachineFilesBean findFileById(int fileId) {
        return session.find(MachineFilesBean.class, fileId);
    }

    public List<MachineFilesBean> findFilesByMachineId(int machineId) {
        String hql = "FROM MachineFilesBean WHERE machineId = :machineId ORDER BY uploadTime DESC";
        Query<MachineFilesBean> query = session.createQuery(hql, MachineFilesBean.class);
        query.setParameter("machineId", machineId);
        return query.getResultList();
    }

    public MachineFilesBean addFile(MachineFilesBean file) {
        if (file != null) {
            session.persist(file);
            return file;
        }
        return null;
    }

    public MachineFilesBean persistFile(MachineFilesBean file) {
        MachineFilesBean resultBean = session.find(MachineFilesBean.class, file.getFileId());
        if (resultBean != null) {
            resultBean.setFileName(file.getFileName());
            resultBean.setFilePath(file.getFilePath());
            resultBean.setMachineId(file.getMachineId());
            return session.merge(resultBean);
        }
        return null;
    }

    public boolean removeFile(int fileId) {
        MachineFilesBean file = session.find(MachineFilesBean.class, fileId);
        if (file != null) {
            session.remove(file);
            return true;
        }
        return false;
    }

    public List<MachineFilesBean> searchFiles(String keyword) {
        String hql = "SELECT f FROM MachineFilesBean f LEFT JOIN MachinesBean m ON f.machineId = m.machineId " +
                     "WHERE f.fileName LIKE :keyword OR m.machineName LIKE :keyword ORDER BY f.uploadTime DESC";
        Query<MachineFilesBean> query = session.createQuery(hql, MachineFilesBean.class);
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }

    public List<MachineFilesBean> findFilesWithMachineInfo() {
        String hql = "FROM MachineFilesBean f LEFT JOIN FETCH f.machine ORDER BY f.uploadTime DESC";
        Query<MachineFilesBean> query = session.createQuery(hql, MachineFilesBean.class);
        return query.getResultList();
    }
    
  
  
    

    
}