package com.personnel.dao;

import java.sql.SQLException;
import java.util.List;

import com.personnel.model.Employee;

public interface EmployeeDao {

    int addEmployee(Employee employee) throws SQLException;
    Employee getEmployeeById(int id) throws SQLException;
    List<Employee> getAllEmployees() throws SQLException;
    boolean updateEmployee(Employee employee) throws SQLException;
    boolean deleteEmployee(int id) throws SQLException;
    List<Employee> searchEmployees(String searchTerm) throws SQLException;
}