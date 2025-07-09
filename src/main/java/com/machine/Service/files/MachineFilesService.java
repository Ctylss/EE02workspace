package com.machine.Service.files;

import com.machine.Bean.MachineFilesBean;
import com.machine.Dao.MachineFilesDao;
import com.machine.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MachineFilesService {
    
    public List<MachineFilesBean> getAllFiles() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineFilesDao dao = new MachineFilesDao(session);
            return dao.findAllFiles();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    public MachineFilesBean getFileById(int fileId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineFilesDao dao = new MachineFilesDao(session);
            return dao.findFileById(fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }
    
    public List<MachineFilesBean> getFilesByMachineId(int machineId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineFilesDao dao = new MachineFilesDao(session);
            return dao.findFilesByMachineId(machineId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    public List<MachineFilesBean> searchFiles(String keyword) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineFilesDao dao = new MachineFilesDao(session);
            if (keyword == null || keyword.trim().isEmpty()) {
                return dao.findAllFiles();
            }
            return dao.searchFiles(keyword.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    public List<MachineFilesBean> getFilesWithMachineInfo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            MachineFilesDao dao = new MachineFilesDao(session);
            return dao.findFilesWithMachineInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }
    
    public boolean addFile(MachineFilesBean file) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            MachineFilesDao dao = new MachineFilesDao(session);
            
            if (file.getUploadTime() == null) {
                file.setUploadTime(LocalDateTime.now());
            }
            
            MachineFilesBean result = dao.addFile(file);
            
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
    
    public boolean updateFile(MachineFilesBean file) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            MachineFilesDao dao = new MachineFilesDao(session);
            
            MachineFilesBean result = dao.persistFile(file);
            
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
    
    public boolean updateFile(int fileId, String fileName, String filePath, int machineId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            MachineFilesDao dao = new MachineFilesDao(session);
            MachineFilesBean originalFile = dao.findFileById(fileId);
            if (originalFile == null) {
                return false;
            }
            
            MachineFilesBean file = new MachineFilesBean();
            file.setFileId(fileId);
            file.setFileName(fileName);
            file.setFilePath(filePath);
            file.setMachineId(machineId);
            file.setUploadTime(originalFile.getUploadTime());
            
            return updateFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
    
    public boolean deleteFile(int fileId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            MachineFilesDao dao = new MachineFilesDao(session);
            
            boolean result = dao.removeFile(fileId);
            
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
}