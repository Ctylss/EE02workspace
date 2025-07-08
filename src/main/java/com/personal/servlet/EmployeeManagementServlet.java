package com.personal.servlet;

import java.io.IOException;
import java.io.PrintWriter; // 引入 PrintWriter
import java.sql.Date; // 導入 java.sql.Date
import java.text.SimpleDateFormat; // 引入 SimpleDateFormat
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// 引入 Jackson 相關類別
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.personal.model.Employee;
import com.personal.service.EmployeeService;
import com.personal.service.impl.EmployeeServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/employeeeManagement")
public class EmployeeManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(EmployeeManagementServlet.class.getName());

    private EmployeeService employeeService;
    private ObjectMapper objectMapper; // Jackson 的 ObjectMapper

    @Override
    public void init() throws ServletException {
        super.init();
        this.employeeService = new EmployeeServiceImpl();
        // 初始化 ObjectMapper
        objectMapper = new ObjectMapper();
        // 禁用將日期寫為時間戳，改為可讀的字串格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 設定日期格式，與前端的 <input type="date"> 期望的 "YYYY-MM-DD" 格式一致
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        LOGGER.log(Level.INFO, "EmployeeManagementServlet 已初始化，EmployeeService 和 ObjectMapper 已準備就緒.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 進入 doGet 方法.");
        request.setCharacterEncoding("UTF-8"); // 確保請求編碼
        response.setCharacterEncoding("UTF-8"); // 確保響應編碼
        response.setContentType("application/json;charset=UTF-8"); // 設定響應內容類型為 JSON

        HttpSession session = request.getSession(false);
        // 權限檢查：只有 'admin' 和 'personnel' 角色可以訪問此 Servlet
        if (session == null || session.getAttribute("loggedInUser") == null ||
            (!("admin".equals(session.getAttribute("userRole"))) && !("personnel".equals(session.getAttribute("userRole"))))) {
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doGet - 權限不足或未登入。");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "無權訪問此功能");
            return;
        }

        String action = request.getParameter("action");
        LOGGER.log(Level.INFO, "EmployeeManagementServlet: doGet - 獲取到的 action 參數: {0}", action);

        if (action == null || action.isEmpty()) {
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doGet - action 參數為 null 或空，發送 400 錯誤.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少操作參數");
            return;
        }

        try (PrintWriter out = response.getWriter()) { // 使用 try-with-resources 自動關閉 PrintWriter
            switch (action) {
                case "list":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 處理 list 動作.");
                    List<Employee> employees = employeeService.getAllEmployees();
                    // 使用 ObjectMapper 將 List<Employee> 轉換為 JSON 並寫入響應
                    objectMapper.writeValue(out, employees);
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 已返回 {0} 個員工數據.", employees.size());
                    break;

                case "search":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 處理 search 動作.");
                    String searchTerm = request.getParameter("searchTerm");
                    List<Employee> searchResults = employeeService.searchEmployees(searchTerm != null ? searchTerm : "");
                    // 使用 ObjectMapper 將 List<Employee> 轉換為 JSON 並寫入響應
                    objectMapper.writeValue(out, searchResults);
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 搜尋 '{0}'，返回 {1} 個結果.", new Object[]{searchTerm, searchResults.size()});
                    break;

                case "getEmployee":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 處理 getEmployee 動作.");
                    String idParam = request.getParameter("id");
                    if (idParam == null || idParam.isEmpty()) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少員工內部 ID (id)。");
                        return;
                    }
                    int employeeInternalId = Integer.parseInt(idParam);
                    Employee employee = employeeService.getEmployeeById(employeeInternalId);
                    if (employee != null) {
                        // 使用 ObjectMapper 將單個 Employee 轉換為 JSON 並寫入響應
                        objectMapper.writeValue(out, employee);
                        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 已獲取員工內部 ID {0} 的數據.", employeeInternalId);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "未找到員工內部 ID: " + employeeInternalId);
                        LOGGER.log(Level.WARNING, "EmployeeManagementServlet: 未找到員工內部 ID: {0}", employeeInternalId);
                    }
                    break;

                default:
                    LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doGet - 無效的操作: {0}", action);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無效的 GET 操作");
                    break;
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doGet - 無效的數字格式參數: {0}", new Object[]{e.getMessage(), e});
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無效的數字格式參數");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeManagementServlet: doGet - 處理請求時發生未預期錯誤: {0}", new Object[]{e.getMessage(), e});
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "伺服器內部錯誤: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 進入 doPost 方法.");
        request.setCharacterEncoding("UTF-8"); // 確保請求編碼
        response.setCharacterEncoding("UTF-8"); // 確保響應編碼
        response.setContentType("text/plain;charset=UTF-8"); // POST 響應通常是純文字或 JSON 狀態訊息

        HttpSession session = request.getSession(false);
        // 權限檢查：只有 'admin' 和 'personnel' 角色可以執行 POST 操作
        if (session == null || session.getAttribute("loggedInUser") == null ||
            (!("admin".equals(session.getAttribute("userRole"))) && !("personnel".equals(session.getAttribute("userRole"))))) {
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doPost - 權限不足或未登入。");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "無權訪問此功能");
            return;
        }

        String action = request.getParameter("action");
        LOGGER.log(Level.INFO, "EmployeeManagementServlet: doPost - 獲取到的 action 參數: {0}", action);

        if (action == null || action.isEmpty()) {
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doPost - action 參數為 null 或空，發送 400 錯誤.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少操作參數");
            return;
        }

        try (PrintWriter out = response.getWriter()) { // 使用 try-with-resources 自動關閉 PrintWriter
            Employee employee = parseEmployeeFromRequest(request, "update".equals(action) || "delete".equals(action)); // 解析員工數據

            switch (action) {
                case "add":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 處理 add 動作.");
                    if (employee == null) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "新增員工資料不完整。請檢查所有欄位。");
                        return;
                    }
                    boolean addSuccess = employeeService.addEmployee(employee);
                    if (addSuccess) {
                        out.write("success");
                        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 新增員工成功: {0}", employee.getEmployeeId());
                    } else {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "新增員工失敗。可能是員工編號重複。");
                        LOGGER.log(Level.WARNING, "EmployeeManagementServlet: 新增員工失敗: {0}", employee.getEmployeeId());
                    }
                    break;

                case "update":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 處理 update 動作.");
                    if (employee == null || employee.getId() == 0) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "更新員工資料不完整或缺少內部 ID。");
                        return;
                    }
                    boolean updateSuccess = employeeService.updateEmployee(employee);
                    if (updateSuccess) {
                        out.write("success");
                        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 更新員工成功: ID {0}", employee.getId());
                    } else {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "更新員工失敗。");
                        LOGGER.log(Level.WARNING, "EmployeeManagementServlet: 更新員工失敗: ID {0}", employee.getId());
                    }
                    break;

                case "delete":
                    LOGGER.log(Level.INFO, "EmployeeManagementServlet: 處理 delete 動作.");
                    if (employee == null || employee.getId() == 0) { // 這裡只需 ID
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "刪除員工缺少內部 ID。");
                        return;
                    }
                    boolean deleteSuccess = employeeService.deleteEmployee(employee.getId());
                    if (deleteSuccess) {
                        out.write("success");
                        LOGGER.log(Level.INFO, "EmployeeManagementServlet: 刪除員工成功: ID {0}", employee.getId());
                    } else {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "刪除員工失敗。");
                        LOGGER.log(Level.WARNING, "EmployeeManagementServlet: 刪除員工失敗: ID {0}", employee.getId());
                    }
                    break;

                default:
                    LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doPost - 無效的操作: {0}", action);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無效的 POST 操作");
                    break;
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doPost - 無效的數字格式參數: {0}", new Object[]{e.getMessage(), e});
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無效的數字格式參數");
        } catch (IllegalArgumentException e) { // 捕獲 parseEmployeeFromRequest 拋出的自定義異常
            LOGGER.log(Level.WARNING, "EmployeeManagementServlet: doPost - 請求參數錯誤: {0}", new Object[]{e.getMessage(), e});
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "EmployeeManagementServlet: doPost - 處理請求時發生未預期錯誤: {0}", new Object[]{e.getMessage(), e});
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "伺服器內部錯誤: " + e.getMessage());
        }
    }

    /**
     * 從 HttpServletRequest 解析 Employee 物件。
     *
     * @param request HttpServletRequest 物件
     * @param includeId 是否從請求中解析 'id' (用於更新或刪除操作)
     * @return 解析後的 Employee 物件，如果資料不完整則拋出 IllegalArgumentException。
     * @throws IllegalArgumentException 如果必要資料缺失或格式錯誤。
     */
    private Employee parseEmployeeFromRequest(HttpServletRequest request, boolean includeId) {
        Employee employee = new Employee();
        
        if (includeId) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    employee.setId(Integer.parseInt(idParam));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("員工內部 ID 格式無效。", e);
                }
            } else if (includeId) { // 如果需要 ID 但未提供
                throw new IllegalArgumentException("更新或刪除操作缺少員工內部 ID。");
            }
        }

        String employeeId = request.getParameter("employeeId");
        String name = request.getParameter("name");
        String department = request.getParameter("department");
        String position = request.getParameter("position");
        String hireDateStr = request.getParameter("hireDate");

        // 對於 POST 請求，所有這些欄位通常都是必要的 (除了 id 對於新增)
        // 僅在 add 和 update 操作時需要檢查所有欄位
        if (("add".equals(request.getParameter("action")) || "update".equals(request.getParameter("action"))) &&
            (employeeId == null || employeeId.isEmpty() ||
             name == null || name.isEmpty() ||
             department == null || department.isEmpty() ||
             position == null || position.isEmpty() ||
             hireDateStr == null || hireDateStr.isEmpty())) {
            throw new IllegalArgumentException("新增或更新員工資料不完整。請檢查所有欄位。");
        }

        employee.setEmployeeId(employeeId);
        employee.setName(name);
        employee.setDepartment(department);
        employee.setPosition(position);
        
        if (hireDateStr != null && !hireDateStr.isEmpty()) {
            try {
                employee.setHireDate(Date.valueOf(hireDateStr));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("入職日期格式無效。請使用YYYY-MM-DD 格式。", e);
            }
        }
        return employee;
    }

    // 這些方法將被移除，由 Jackson ObjectMapper 取代
    // private String convertEmployeeToJson(Employee employee) { ... }
    // private String convertEmployeesToJson(List<Employee> employees) { ... }
    // private String escapeJson(String text) { ... }
}
