package com.personal.service.impl; // 確保 package 名稱和路徑一致

import com.personal.dao.EmployeeDao;
import com.personal.dao.impl.EmployeeDaoImpl;
import com.personal.model.Employee;
import com.personal.service.EmployeeService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeServiceImpl implements EmployeeService { // 確保類別名稱是 EmployeeServiceImpl 且正確 implements EmployeeService

    private static final Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class.getName());
    private EmployeeDao employeeDao;

    public EmployeeServiceImpl() {
        this.employeeDao = (EmployeeDao) new EmployeeDaoImpl(); // 確保使用正確的實作類別
    }

    @Override
    public boolean addEmployee(Employee employee) {
        LOGGER.log(Level.INFO, "EmployeeServiceImpl: 嘗試新增員工: {0}", employee.getEmployeeId());
        try {
            int generatedId = employeeDao.addEmployee(employee);
            return generatedId > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 新增員工時資料庫操作錯誤 for employee {0}: {1}", new Object[]{employee.getEmployeeId(), e.getMessage(), e});
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 新增員工時發生未預期錯誤 for employee {0}: {1}", new Object[]{employee.getEmployeeId(), e.getMessage(), e});
            return false;
        }
    }

    @Override
    public Employee getEmployeeById(int id) {
        LOGGER.log(Level.INFO, "EmployeeServiceImpl: 嘗試根據 ID 獲取員工: {0}", id);
        try {
            return employeeDao.getEmployeeById(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 根據 ID 獲取員工時資料庫操作錯誤 for ID {0}: {1}", new Object[]{id, e.getMessage(), e});
            return null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 根據 ID 獲取員工時發生未預期錯誤 for ID {0}: {1}", new Object[]{id, e.getMessage(), e});
            return null;
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        LOGGER.log(Level.INFO, "EmployeeServiceImpl: 嘗試獲取所有員工列表.");
        try {
            return employeeDao.getAllEmployees();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 獲取所有員工時資料庫操作錯誤: {0}", new Object[]{e.getMessage(), e});
            return Collections.emptyList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 獲取所有員工時發生未預期錯誤: {0}", new Object[]{e.getMessage(), e});
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        LOGGER.log(Level.INFO, "EmployeeServiceImpl: 嘗試更新員工 ID: {0}", employee.getId());
        try {
            return employeeDao.updateEmployee(employee);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 更新員工時資料庫操作錯誤 for ID {0}: {1}", new Object[]{employee.getId(), e.getMessage(), e});
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 更新員工時發生未預期錯誤 for ID {0}: {1}", new Object[]{employee.getId(), e.getMessage(), e});
            return false;
        }
    }

    @Override
    public boolean deleteEmployee(int id) {
        LOGGER.log(Level.INFO, "EmployeeServiceImpl: 嘗試刪除員工 ID: {0}", id);
        try {
            return employeeDao.deleteEmployee(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 刪除員工時資料庫操作錯誤 for ID {0}: {1}", new Object[]{id, e.getMessage(), e});
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 刪除員工時發生未預期錯誤 for ID {0}: {1}", new Object[]{id, e.getMessage(), e});
            return false;
        }
    }

    @Override
    public List<Employee> searchEmployees(String searchTerm) {
        LOGGER.log(Level.INFO, "EmployeeServiceImpl: 嘗試搜尋員工，關鍵字: {0}", searchTerm);
        try {
            return employeeDao.searchEmployees(searchTerm);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 搜尋員工時資料庫操作錯誤 for searchTerm '{0}': {1}", new Object[]{searchTerm, e.getMessage(), e});
            return Collections.emptyList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeServiceImpl: 搜尋員工時發生未預期錯誤 for searchTerm '{0}': {1}", new Object[]{searchTerm, e.getMessage(), e});
            return Collections.emptyList();
        }
    }
}
