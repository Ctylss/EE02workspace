package com.personal.servlet;

import com.personal.service.UserService;
import com.personal.service.impl.UserServiceImpl;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig // 處理 multipart/form-data 請求必須加上此註解
@WebServlet("/userManagement")
public class UserManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UserManagementServlet.class.getName());

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserServiceImpl();
        LOGGER.log(Level.INFO, "UserManagementServlet 已初始化，UserService 已準備就緒.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "UserManagementServlet: 進入 doPost 方法.");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        // 關鍵：強制解析 multipart/form-data 請求的主體
        try {
            request.getParts(); 
            LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 已嘗試解析請求 Parts.");
        } catch (ServletException e) {
            LOGGER.log(Level.SEVERE, "UserManagementServlet: doPost - 解析請求 Parts 時發生 ServletException: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "請求格式錯誤，無法解析 Parts: " + e.getMessage());
            return;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "UserManagementServlet: doPost - 解析請求 Parts 時發生 IOException: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "伺服器內部錯誤，無法解析請求: " + e.getMessage());
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null ||
            (!("admin".equals(session.getAttribute("userRole"))) && !("personnel".equals(session.getAttribute("userRole"))))) {
            LOGGER.log(Level.WARNING, "UserManagementServlet: doPost - 權限不足或未登入。Session ID: {0}, User: {1}, Role: {2}",
                new Object[]{session != null ? session.getId() : "N/A", session != null ? session.getAttribute("loggedInUser") : "N/A", session != null ? session.getAttribute("userRole") : "N/A"});
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "無權訪問此功能");
            return;
        }

        LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 接收到的參數列表:");
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            // 不直接打印密碼，只打印名稱
            if ("newPassword".equals(paramName) || "confirmPassword".equals(paramName)) {
                LOGGER.log(Level.INFO, "  {0} = [PROTECTED]", paramName);
            } else {
                LOGGER.log(Level.INFO, "  {0} = {1}", new Object[]{paramName, request.getParameter(paramName)});
            }
        }
        LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 參數列表結束.");

        String action = request.getParameter("action");
        LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 獲取到的 action 參數: {0}", action);

        if (action == null || action.isEmpty()) {
            LOGGER.log(Level.WARNING, "UserManagementServlet: doPost - action 參數為 null 或空，發送 400 錯誤.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少操作參數");
            return;
        }

        switch (action) {
            case "getUsers": 
                LOGGER.log(Level.WARNING, "UserManagementServlet: doPost - 錯誤：POST 收到 getUsers 動作，這應該是 GET 請求。");
                response.getWriter().write("錯誤：此操作不應通過 POST 請求。");
                break;

            case "updatePassword":
                LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 處理 updatePassword 動作.");
                String targetUsername = request.getParameter("targetUsername");
                String newPassword = request.getParameter("newPassword");

                LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 目標用戶名: {0}", targetUsername);
                
                if (targetUsername == null || newPassword == null || targetUsername.isEmpty() || newPassword.isEmpty()) {
                    LOGGER.log(Level.WARNING, "UserManagementServlet: doPost - 更新密碼參數為空，發送 400 錯誤.");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "帳號或新密碼不能為空");
                    return;
                }
                
                try {
                    boolean success = userService.updateUserPassword(targetUsername, newPassword);
                    if (success) {
                        LOGGER.log(Level.INFO, "UserManagementServlet: 使用者 '{0}' 的密碼已更新成功。", targetUsername);
                        response.getWriter().write("success");
                    } else {
                        LOGGER.log(Level.WARNING, "UserManagementServlet: 更新使用者 '{0}' 密碼失敗（Service 層返回 false）。", targetUsername);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "密碼更新失敗，請檢查日誌");
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "UserManagementServlet: 調用 updateUserPassword 時發生未預期錯誤: {0}", e.getMessage());
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "伺服器內部錯誤： " + e.getMessage());
                }
                break;

            default:
                LOGGER.log(Level.WARNING, "UserManagementServlet: doPost - 無效的操作: {0}，發送 400 錯誤.", action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無效的操作");
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "UserManagementServlet: 進入 doGet 方法.");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null ||
            (!("admin".equals(session.getAttribute("userRole"))) && !("personnel".equals(session.getAttribute("userRole"))))) {
            LOGGER.log(Level.WARNING, "UserManagementServlet: doGet - 權限不足或未登入。Session ID: {0}, User: {1}, Role: {2}",
                new Object[]{session != null ? session.getId() : "N/A", session != null ? session.getAttribute("loggedInUser") : "N/A", session != null ? session.getAttribute("userRole") : "N/A"});
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "無權訪問此功能");
            return;
        }

        String action = request.getParameter("action");
        LOGGER.log(Level.INFO, "UserManagementServlet: doGet - 獲取到的 action 參數: {0}", action);

        if (action == null || action.isEmpty()) {
            LOGGER.log(Level.WARNING, "UserManagementServlet: doGet - action 參數為 null 或空，發送 400 錯誤.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少操作參數");
            return;
        }

        if ("getUsers".equals(action)) {
            LOGGER.log(Level.INFO, "UserManagementServlet: doGet - 處理 getUsers 動作.");
            List<String> usernames = userService.getAllUsernames();
            response.getWriter().write(String.join(",", usernames));
        } else {
            LOGGER.log(Level.WARNING, "UserManagementServlet: doGet - 無效的 GET 操作: {0}，發送 400 錯誤.", action);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "無效的 GET 操作");
        }
    }
}
