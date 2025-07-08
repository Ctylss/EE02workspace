    package com.personal.dao.impl; // 確保 package 名稱和路徑一致

    import com.example.dao.util.DBUtil;
import com.personal.dao.EmployeeDao;
import com.personal.model.Employee;

import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.logging.Level;
    import java.util.logging.Logger;

    public class EmployeeDaoImpl implements EmployeeDao { // 確保類別名稱是 EmployeeDaoImpl 並且正確 implements EmployeeDao

        private static final Logger LOGGER = Logger.getLogger(EmployeeDaoImpl.class.getName());

        @Override
        public int addEmployee(Employee employee) throws SQLException {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            int generatedId = -1;

            String sql = "INSERT INTO Employees (employee_id, name, department, position, hire_date) VALUES (?, ?, ?, ?, ?)";
            LOGGER.log(Level.INFO, "EmployeeDaoImpl: 執行新增員工 SQL: {0} for employee: {1}", new Object[]{sql, employee.getEmployeeId()});

            try {
                conn = DBUtil.getConnection();
                pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                pstmt.setString(1, employee.getEmployeeId());
                pstmt.setString(2, employee.getName());
                pstmt.setString(3, employee.getDepartment());
                pstmt.setString(4, employee.getPosition());
                pstmt.setDate(5, employee.getHireDate());

                int rowsAffected = pstmt.executeUpdate();
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 新增員工影響的行數: {0}", rowsAffected);

                if (rowsAffected > 0) {
                    rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        LOGGER.log(Level.INFO, "EmployeeDaoImpl: 員工 {0} 新增成功，生成 ID: {1}", new Object[]{employee.getEmployeeId(), generatedId});
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 新增員工時資料庫操作錯誤 for employee: {0}. Error: {1}", new Object[]{employee.getEmployeeId(), e.getMessage(), e});
                throw e;
            } finally {
                DBUtil.close(conn, pstmt, rs);
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 新增員工資源已關閉.");
            }
            return generatedId;
        }

        @Override
        public Employee getEmployeeById(int id) throws SQLException {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            Employee employee = null;

            String sql = "SELECT id, employee_id, name, department, position, hire_date FROM Employees WHERE id = ?";
            LOGGER.log(Level.INFO, "EmployeeDaoImpl: 執行查詢員工By ID SQL: {0} for ID: {1}", new Object[]{sql, id});

            try {
                conn = DBUtil.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    employee = new Employee();
                    employee.setId(rs.getInt("id"));
                    employee.setEmployeeId(rs.getString("employee_id"));
                    employee.setName(rs.getString("name"));
                    employee.setDepartment(rs.getString("department"));
                    employee.setPosition(rs.getString("position"));
                    employee.setHireDate(rs.getDate("hire_date"));
                    LOGGER.log(Level.INFO, "EmployeeDaoImpl: 員工 ID {0} 已找到: {1}", new Object[]{id, employee.getName()});
                } else {
                    LOGGER.log(Level.INFO, "EmployeeDaoImpl: 員工 ID {0} 未找到.", id);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 查詢員工By ID時資料庫操作錯誤 for ID {0}: {1}", new Object[]{id, e.getMessage(), e});
                throw e;
            } finally {
                DBUtil.close(conn, pstmt, rs);
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 查詢員工By ID資源已關閉.");
            }
            return employee;
        }

        @Override
        public List<Employee> getAllEmployees() throws SQLException {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Employee> employees = new ArrayList<>();

            String sql = "SELECT id, employee_id, name, department, position, hire_date FROM Employees ORDER BY employee_id";
            LOGGER.log(Level.INFO, "EmployeeDaoImpl: 執行獲取所有員工 SQL: {0}", sql);

            try {
                conn = DBUtil.getConnection();
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setId(rs.getInt("id"));
                    employee.setEmployeeId(rs.getString("employee_id"));
                    employee.setName(rs.getString("name"));
                    employee.setDepartment(rs.getString("department"));
                    employee.setPosition(rs.getString("position"));
                    employee.setHireDate(rs.getDate("hire_date"));
                    employees.add(employee);
                }
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 獲取到 {0} 個員工.", employees.size());
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 獲取所有員工時資料庫操作錯誤: {0}", new Object[]{e.getMessage(), e});
                throw e;
            } finally {
                DBUtil.close(conn, pstmt, rs);
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 獲取所有員工資源已關閉.");
            }
            return employees;
        }

        @Override
        public boolean updateEmployee(Employee employee) throws SQLException {
            Connection conn = null;
            PreparedStatement pstmt = null;
            int rowsAffected = 0;

            String sql = "UPDATE Employees SET employee_id = ?, name = ?, department = ?, position = ?, hire_date = ? WHERE id = ?";
            LOGGER.log(Level.INFO, "EmployeeDaoImpl: 執行更新員工 SQL: {0} for employee ID: {1}", new Object[]{sql, employee.getId()});

            try {
                conn = DBUtil.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, employee.getEmployeeId());
                pstmt.setString(2, employee.getName());
                pstmt.setString(3, employee.getDepartment());
                pstmt.setString(4, employee.getPosition());
                pstmt.setDate(5, employee.getHireDate());
                pstmt.setInt(6, employee.getId());

                rowsAffected = pstmt.executeUpdate();
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 更新員工 ID {0} 影響的行數: {1}", new Object[]{employee.getId(), rowsAffected});
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 更新員工時資料庫操作錯誤 for ID {0}: {1}", new Object[]{employee.getId(), e.getMessage(), e});
                throw e;
            } finally {
                DBUtil.close(conn, pstmt);
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 更新員工資源已關閉.");
            }
            return rowsAffected > 0;
        }

        @Override
        public boolean deleteEmployee(int id) throws SQLException {
            Connection conn = null;
            PreparedStatement pstmt = null;
            int rowsAffected = 0;

            String sql = "DELETE FROM Employees WHERE id = ?";
            LOGGER.log(Level.INFO, "EmployeeDaoImpl: 執行刪除員工 SQL: {0} for ID: {1}", new Object[]{sql, id});

            try {
                conn = DBUtil.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                rowsAffected = pstmt.executeUpdate();
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 刪除員工 ID {0} 影響的行數: {1}", new Object[]{id, rowsAffected});
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 刪除員工時資料庫操作錯誤 for ID {0}: {1}", new Object[]{id, e.getMessage(), e});
                throw e;
            } finally {
                DBUtil.close(conn, pstmt);
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 刪除員工資源已關閉.");
            }
            return rowsAffected > 0;
        }

        @Override
        public List<Employee> searchEmployees(String searchTerm) throws SQLException {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            List<Employee> employees = new ArrayList<>();

            String sql = "SELECT id, employee_id, name, department, position, hire_date FROM Employees " +
                        "WHERE employee_id LIKE ? OR name LIKE ? OR department LIKE ? OR position LIKE ? ORDER BY employee_id";
            LOGGER.log(Level.INFO, "EmployeeDaoImpl: 執行搜尋員工 SQL: {0} for searchTerm: {1}", new Object[]{sql, searchTerm});

            try {
                conn = DBUtil.getConnection();
                pstmt = conn.prepareStatement(sql);
                String searchPattern = "%" + searchTerm + "%";
                pstmt.setString(1, searchPattern);
                pstmt.setString(2, searchPattern);
                pstmt.setString(3, searchPattern);
                pstmt.setString(4, searchPattern);
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setId(rs.getInt("id"));
                    employee.setEmployeeId(rs.getString("employee_id"));
                    employee.setName(rs.getString("name"));
                    employee.setDepartment(rs.getString("department"));
                    employee.setPosition(rs.getString("position"));
                    employee.setHireDate(rs.getDate("hire_date"));
                    employees.add(employee);
                }
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 搜尋到 {0} 個員工.", employees.size());
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "EmployeeDaoImpl: 搜尋員工時資料庫操作錯誤 for searchTerm '{0}': {1}", new Object[]{searchTerm, e.getMessage(), e});
                throw e;
            } finally {
                DBUtil.close(conn, pstmt, rs);
                LOGGER.log(Level.INFO, "EmployeeDaoImpl: 搜尋員工資源已關閉.");
            }
            return employees;
        }
    }
    