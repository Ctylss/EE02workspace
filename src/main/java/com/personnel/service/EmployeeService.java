package com.personnel.service;

import java.util.List;

import com.personnel.model.Employee;

public interface EmployeeService {

    boolean addEmployee(Employee employee);
    Employee getEmployeeById(int id);
    List<Employee> getAllEmployees();
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(int id);
    List<Employee> searchEmployees(String searchTerm);
}