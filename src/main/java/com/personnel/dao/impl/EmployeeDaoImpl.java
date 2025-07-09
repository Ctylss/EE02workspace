package com.personnel.dao.impl;

import com.personnel.dao.EmployeeDao;
import com.personnel.model.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class EmployeeDaoImpl implements EmployeeDao {

    private static final Logger LOGGER = Logger.getLogger(EmployeeDaoImpl.class.getName());
    private Session session;

    public EmployeeDaoImpl(Session session) {//把資料庫的 Session 傳進來存起來
        this.session = session;
    }

    @Override
    public int addEmployee(Employee employee) throws SQLException {
        LOGGER.log(Level.INFO, "EmployeeDaoImpl: 準備新增員工: {0}", employee.toString());

        try {
            // 檢查員工編號是否已存在
            Query<Long> checkQuery = session.createQuery("SELECT COUNT(*) FROM Employee WHERE employeeId = :employeeId", Long.class);
            checkQuery.setParameter("employeeId", employee.getEmployeeId());
            Long count = checkQuery.uniqueResult();
            
            if (count != null && count > 0) {
                throw new SQLException("員工編號 " + employee.getEmployeeId() + " 已存在");
            }

            // 使用 persist 新增員工
            session.persist(employee);
            
            // 強制執行 SQL 並獲取生成的 ID
            session.flush();
            
            int generatedId = employee.getId();//Hibernate自動產生主鍵 ID
            LOGGER.log(Level.INFO, "EmployeeDaoImpl: 員工 {0} 新增成功，生成 ID: {1}", new Object[]{employee.getEmployeeId(), generatedId});
            
            return generatedId;
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 資料庫約束違反 - 可能是員工編號重複: {0}", e.getMessage());
            throw new SQLException("員工編號已存在或違反資料庫約束: " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 新增員工時發生未預期錯誤: {0}", e.getMessage());
            e.printStackTrace(); // 打印完整堆疊跟蹤
            throw new SQLException("新增員工時發生錯誤: " + e.getMessage(), e);
        }
    }

    @Override
    public Employee getEmployeeById(int id) throws SQLException {
        Employee employee = null;

        LOGGER.log(Level.INFO, "EmployeeDaoImpl: 查詢員工 ID: {0}", id);

        try {
            employee = session.find(Employee.class, id);

            if (employee != null) {
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 員工 ID {0} 已找到: {1}", new Object[]{id, employee.getName()});
            } else {
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 員工 ID {0} 未找到.", id);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 查詢員工By ID時發生錯誤 for ID {0}: {1}", new Object[]{id, e.getMessage()});
            throw new SQLException("查詢員工時發生錯誤", e);
        }
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();

        String hql = "FROM Employee ORDER BY employeeId";
        LOGGER.log(Level.INFO, "EmployeeDaoImpl: 執行獲取所有員工 HQL: {0}", hql);

        try {
            Query<Employee> query = session.createQuery(hql, Employee.class);
            employees = query.list();

            LOGGER.log(Level.INFO, "EmployeeDaoImpl: 獲取到 {0} 個員工.", employees.size());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 獲取所有員工時發生錯誤: {0}", e.getMessage());
            throw new SQLException("獲取所有員工時發生錯誤", e);
        }
        return employees;
    }

    @Override
    public boolean updateEmployee(Employee employee) throws SQLException {
        int rowsAffected = 0;

        LOGGER.log(Level.INFO, "EmployeeDaoImpl: 準備更新員工 ID: {0}", employee.getId());

        try {
            // 先檢查員工是否存在
            Employee existingEmployee = session.find(Employee.class, employee.getId());
            if (existingEmployee == null) {
                LOGGER.log(Level.WARNING, "EmployeeDaoImpl: 要更新的員工不存在 ID: {0}", employee.getId());
                return false;
            }

            // 檢查員工編號是否與其他員工衝突
            Query<Long> checkQuery = session.createQuery("SELECT COUNT(*) FROM Employee WHERE employeeId = :employeeId AND id != :id", Long.class);
            checkQuery.setParameter("employeeId", employee.getEmployeeId());
            checkQuery.setParameter("id", employee.getId());
            Long count = checkQuery.uniqueResult();
            
            if (count != null && count > 0) {
                throw new SQLException("員工編號 " + employee.getEmployeeId() + " 已被其他員工使用");
            }

            // 使用 merge 更新
            Employee mergedEmployee = session.merge(employee);
            session.flush();
            
            if (mergedEmployee != null) {
                rowsAffected = 1;
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 更新員工成功 ID: {0}", employee.getId());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 更新員工時發生錯誤 for ID {0}: {1}", new Object[]{employee.getId(), e.getMessage()});
            e.printStackTrace();
            throw new SQLException("更新員工時發生錯誤", e);
        }
        return rowsAffected > 0;
    }

    @Override
    public boolean deleteEmployee(int id) throws SQLException {
        LOGGER.log(Level.INFO, "EmployeeDaoImpl: 準備刪除員工 ID: {0}", id);

        try {
            Employee employee = session.find(Employee.class, id);
            if (employee != null) {
                session.remove(employee);
                session.flush();
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 刪除員工成功 ID: {0}", id);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "EmployeeDaoImpl: 要刪除的員工不存在 ID: {0}", id);
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 刪除員工時發生錯誤 for ID {0}: {1}", new Object[]{id, e.getMessage()});
            e.printStackTrace();
            throw new SQLException("刪除員工時發生錯誤", e);
        }
    }

    @Override
    public List<Employee> searchEmployees(String searchTerm) throws SQLException {
        List<Employee> employees = new ArrayList<>();

        String hql = "FROM Employee WHERE employeeId LIKE :searchTerm OR name LIKE :searchTerm OR department LIKE :searchTerm OR position LIKE :searchTerm ORDER BY employeeId";
        LOGGER.log(Level.INFO, "EmployeeDaoImpl: 執行搜尋員工 HQL for searchTerm: {0}", searchTerm);

        try {
            Query<Employee> query = session.createQuery(hql, Employee.class);
            String searchPattern = "%" + searchTerm + "%";
            query.setParameter("searchTerm", searchPattern);
            employees = query.list();

            LOGGER.log(Level.INFO, "EmployeeDaoImpl: 搜尋到 {0} 個員工.", employees.size());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 搜尋員工時發生錯誤 for searchTerm '{0}': {1}", new Object[]{searchTerm, e.getMessage()});
            throw new SQLException("搜尋員工時發生錯誤", e);
        }
        return employees;
    }
}