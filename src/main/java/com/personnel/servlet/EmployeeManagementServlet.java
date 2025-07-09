package com.personnel.servlet;

import java.io.IOException;
import java.io.PrintWriter; // 引入 PrintWriter
import java.sql.Date; // 导入 java.sql.Date
import java.text.SimpleDateFormat; // 引入 SimpleDateFormat
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// 引入 Jackson 相关类别
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.personnel.model.Employee;
import com.personnel.service.EmployeeService;
import com.personnel.service.impl.EmployeeServiceImpl;
import com.example.dao.util.HibernateUtil; // 导入正确的 HibernateUtil 包

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@WebServlet("/employeeManagement")
public class EmployeeManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EmployeeManagementServlet.class.getName());

    private ObjectMapper objectMapper; // Jackson 的 ObjectMapper

    @Override
    public void init() throws ServletException {
        super.init();
        // 初始化 ObjectMapper
        objectMapper = new ObjectMapper();
        // 禁用将日期写为时间戳，改为可读的字串格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 设定日期格式，与前端的 <input type="date"> 期望的 "YYYY-MM-DD" 格式一致
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        LOGGER.log(Level.INFO, "EmployeeManagementServlet 已初始化，ObjectMapper 已准备就绪.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 进入 doGet 方法.");
        request.setCharacterEncoding("UTF-8"); // 确保请求编码
        response.setCharacterEncoding("UTF-8"); // 确保响应编码
        response.setContentType("application/json;charset=UTF-8"); // 设定响应内容类型为 JSON

        HttpSession session = request.getSession(false);
        // 权限检查：只有 'admin' 和 'personnel' 角色可以访问此 Servlet
        if (session == null || session.getAttribute("loggedInUser") == null ||
            (!("admin".equals(session.getAttribute("userRole"))) && !("personnel".equals(session.getAttribute("userRole"))))) {
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doGet - 权限不足或未登入。");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "无权访问此功能");
            return;
        }

        String action = request.getParameter("action");
        LOGGER.log(Level.INFO, "EmployeeManagementServlet: doGet - 获取到的 action 参数: {0}", action);

        if (action == null || action.isEmpty()) {
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doGet - action 参数为 null 或空，发送 400 错误.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少操作参数");
            return;
        }

        SessionFactory factory = HibernateUtil.getSessionFactory();// 取得 Hibernate 的 SessionFactory
        Session hibernateSession = factory.getCurrentSession(); // 取得目前的 Session
        Transaction transaction = hibernateSession.getTransaction();// 用于交易控制

        try (PrintWriter out = response.getWriter()) { // 使用 try-with-resources 自动关闭 PrintWriter
            
            EmployeeService employeeService = new EmployeeServiceImpl(hibernateSession);

            switch (action) {
                case "list":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 处理 list 动作.");
                    List<Employee> employees = employeeService.getAllEmployees();
                    // 使用 ObjectMapper 将 List<Employee> 转换为 JSON 并写入响应
                    objectMapper.writeValue(out, employees);
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 已返回 {0} 个员工数据.", employees.size());
                    break;

                case "search":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 处理 search 动作.");
                    String searchTerm = request.getParameter("searchTerm");
                    List<Employee> searchResults = employeeService.searchEmployees(searchTerm != null ? searchTerm : "");
                    // 使用 ObjectMapper 将 List<Employee> 转换为 JSON 并写入响应
                    objectMapper.writeValue(out, searchResults);
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 搜寻 '{0}'，返回 {1} 个结果.", new Object[]{searchTerm, searchResults.size()});
                    break;

                case "getEmployee":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 处理 getEmployee 动作.");
                    String idParam = request.getParameter("id");
                    if (idParam == null || idParam.isEmpty()) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少员工内部 ID (id)。");
                        return;
                    }
                    int employeeInternalId = Integer.parseInt(idParam);
                    Employee employee = employeeService.getEmployeeById(employeeInternalId);
                    if (employee != null) {
                        // 使用 ObjectMapper 将单个 Employee 转换为 JSON 并写入响应
                        objectMapper.writeValue(out, employee);
                        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 已获取员工内部 ID {0} 的数据.", employeeInternalId);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "未找到员工内部 ID: " + employeeInternalId);
                        LOGGER.log(Level.WARNING, "EmployeeManagementServlet: 未找到员工内部 ID: {0}", employeeInternalId);
                    }
                    break;

                default:
                    LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doGet - 无效的操作: {0}", action);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的 GET 操作");
                    break;
            }
            
            
        } catch (NumberFormatException e) {// 处理 ID 格式错误
            if (transaction != null) transaction.rollback();// 回滚交易
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doGet - 无效的数字格式参数: {0}", new Object[]{e.getMessage(), e});
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的数字格式参数");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            LOGGER.log(Level.SEVERE, "EmployeeManagementServlet: doGet - 处理请求时发生未预期错误: {0}", new Object[]{e.getMessage(), e});
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 进入 doPost 方法.");
        request.setCharacterEncoding("UTF-8"); // 确保请求编码
        response.setCharacterEncoding("UTF-8"); // 确保响应编码
        response.setContentType("text/plain;charset=UTF-8"); // POST 响应通常是纯文字或 JSON 状态讯息

        HttpSession session = request.getSession(false);
        // 权限检查：只有 'admin' 和 'personnel' 角色可以操作
        if (session == null || session.getAttribute("loggedInUser") == null ||
            (!("admin".equals(session.getAttribute("userRole"))) && !("personnel".equals(session.getAttribute("userRole"))))) {
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doPost - 权限不足或未登入。");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "无权访问此功能");
            return;
        }

        String action = request.getParameter("action");
        LOGGER.log(Level.INFO, "EmployeeManagementServlet: doPost - 获取到的 action 参数: {0}", action);

        if (action == null || action.isEmpty()) {
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doPost - action 参数为 null 或空，发送 400 错误.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少操作参数");
            return;
        }

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session hibernateSession = factory.getCurrentSession();
        Transaction transaction = hibernateSession.getTransaction();

        try (PrintWriter out = response.getWriter()) { // 使用 try-with-resources 自动关闭 PrintWriter
          
            EmployeeService employeeService = new EmployeeServiceImpl(hibernateSession);
            
            Employee employee = parseEmployeeFromRequest(request, "update".equals(action) || "delete".equals(action)); // 解析员工数据

            switch (action) {
                case "add":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 处理 add 动作.");
                    if (employee == null) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "新增员工资料不完整。请检查所有栏位。");
                        return;
                    }
                    boolean addSuccess = employeeService.addEmployee(employee);
                    if (addSuccess) {
                       
                        out.write("success");
                        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 新增员工成功: {0}", employee.getEmployeeId());
                    } else {
                        transaction.rollback();
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "新增员工失败。可能是员工编号重复。");
                        LOGGER.log(Level.WARNING, "EmployeeManagementServlet: 新增员工失败: {0}", employee.getEmployeeId());
                    }
                    break;

                case "update":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 处理 update 动作.");
                    if (employee == null || employee.getId() == 0) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "更新员工资料不完整或缺少内部 ID。");
                        return;
                    }
                    boolean updateSuccess = employeeService.updateEmployee(employee);
                    if (updateSuccess) {
                        
                        out.write("success");
                        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 更新员工成功: ID {0}", employee.getId());
                    } else {
                        transaction.rollback();
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "更新员工失败。");
                        LOGGER.log(Level.WARNING, "EmployeeManagementServlet: 更新员工失败: ID {0}", employee.getId());
                    }
                    break;

                case "delete":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 处理 delete 动作.");
                    if (employee == null || employee.getId() == 0) { // 这里只需 ID
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "删除员工缺少内部 ID。");
                        return;
                    }
                    boolean deleteSuccess = employeeService.deleteEmployee(employee.getId());
                    if (deleteSuccess) {
                     
                        out.write("success");
                        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 删除员工成功: ID {0}", employee.getId());
                    } else {
                        transaction.rollback();
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "删除员工失败。");
                        LOGGER.log(Level.WARNING, "EmployeeManagementServlet: 删除员工失败: ID {0}", employee.getId());
                    }
                    break;

                default:
                    LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doPost - 无效的操作: {0}", action);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的 POST 操作");
                    break;
            }
        } catch (NumberFormatException e) {
            if (transaction != null) transaction.rollback();
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doPost - 无效的数字格式参数: {0}", new Object[]{e.getMessage(), e});
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的数字格式参数");
        } catch (IllegalArgumentException e) { // 捕获 parseEmployeeFromRequest 抛出的自定义异常
            if (transaction != null) transaction.rollback();
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doPost - 请求参数错误: {0}", new Object[]{e.getMessage(), e});
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            LOGGER.log(Level.SEVERE, "EmployeeManagementServlet: doPost - 处理请求时发生未预期错误: {0}", new Object[]{e.getMessage(), e});
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误: " + e.getMessage());
        }
    }

    /**
     * 从 HttpServletRequest 解析 Employee 物件。
     *
     * @param request HttpServletRequest 物件
     * @param includeId 是否从请求中解析 'id' (用于更新或删除操作)
     * @return 解析后的 Employee 物件，如果资料不完整则抛出 IllegalArgumentException。
     * @throws IllegalArgumentException 如果必要资料缺失或格式错误。
     */
    private Employee parseEmployeeFromRequest(HttpServletRequest request, boolean includeId) {
        Employee employee = new Employee();
        
        if (includeId) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    employee.setId(Integer.parseInt(idParam));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("员工内部 ID 格式无效。", e);
                }
            } else if (includeId) { // 如果需要 ID 但未提供
                throw new IllegalArgumentException("更新或删除操作缺少员工内部 ID。");
            }
        }

        String employeeId = request.getParameter("employeeId");
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String position = request.getParameter("position");
        String hireDateStr = request.getParameter("hireDate");

        // 对于 POST 请求，所有这些栏位通常都是必要的 (除了 id 对于新增)
        // 仅在 add 和 update 操作时需要检查所有栏位
        if (("add".equals(request.getParameter("action")) || "update".equals(request.getParameter("action"))) &&
            (employeeId == null || employeeId.isEmpty() ||
             name == null || name.isEmpty() ||
             department == null || department.isEmpty() ||
             position == null || position.isEmpty() ||
             hireDateStr == null || hireDateStr.isEmpty())) {
            throw new IllegalArgumentException("新增或更新员工资料不完整。请检查所有栏位。");
        }

        employee.setEmployeeId(employeeId);
        employee.setName(name);
        employee.setDepartment(department);
        employee.setPosition(position);
        
        if (hireDateStr != null && !hireDateStr.isEmpty()) {
            try {
                employee.setHireDate(Date.valueOf(hireDateStr));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("入职日期格式无效。请使用YYYY-MM-DD 格式。", e);
            }
        }
        return employee;
    }
}
