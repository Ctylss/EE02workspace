    package com.personal.dao; // 確保 package 名稱和路徑一致

    import java.sql.SQLException;
    import java.util.List;

import com.personal.model.Employee;

    public interface EmployeeDao { // 確保介面名稱是 EmployeeDao

        int addEmployee(Employee employee) throws SQLException;
        Employee getEmployeeById(int id) throws SQLException;
        List<Employee> getAllEmployees() throws SQLException;
        boolean updateEmployee(Employee employee) throws SQLException;
        boolean deleteEmployee(int id) throws SQLException;
        List<Employee> searchEmployees(String searchTerm) throws SQLException;
    }
    