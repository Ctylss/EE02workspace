    package com.personal.service; // 確保 package 名稱和路徑一致

    import java.util.List;

import com.personal.model.Employee;

    public interface EmployeeService { // 確保介面名稱是 EmployeeService

        boolean addEmployee(Employee employee);
        Employee getEmployeeById(int id);
        List<Employee> getAllEmployees();
        boolean updateEmployee(Employee employee);
        boolean deleteEmployee(int id);
        List<Employee> searchEmployees(String searchTerm);
    }
    