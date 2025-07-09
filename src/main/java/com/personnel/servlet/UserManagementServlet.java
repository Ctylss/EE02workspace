package com.personnel.servlet;

import com.personnel.service.UserService;
import com.personnel.service.impl.UserServiceImpl;
import com.example.dao.util.HibernateUtil; // 导入正确的 HibernateUtil 包

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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@MultipartConfig // 处理 multipart/form-data 请求必须加上此注解
@WebServlet("/userManagement")
public class UserManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(UserManagementServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "UserManagementServlet: 进入 doPost 方法.");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        // 关键：强制解析 multipart/form-data 请求的主体
        try {
            request.getParts(); 
            LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 已尝试解析请求 Parts.");
        } catch (ServletException e) {
            LOGGER.log(Level.SEVERE, "UserManagementServlet: doPost - 解析请求 Parts 时发生 ServletException: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "请求格式错误，无法解析 Parts: " + e.getMessage());
            return;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "UserManagementServlet: doPost - 解析请求 Parts 时发生 IOException: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误，无法解析请求: " + e.getMessage());
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null ||
            (!("admin".equals(session.getAttribute("userRole"))) && !("personnel".equals(session.getAttribute("userRole"))))) {
            LOGGER.log(Level.WARNING, "UserManagementServlet: doPost - 权限不足或未登入。Session ID: {0}, User: {1}, Role: {2}",
                new Object[]{session != null ? session.getId() : "N/A", session != null ? session.getAttribute("loggedInUser") : "N/A", session != null ? session.getAttribute("userRole") : "N/A"});
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "无权访问此功能");
            return;
        }

        LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 接收到的参数列表:");
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            // 不直接打印密码，只打印名称
            if ("newPassword".equals(paramName) || "confirmPassword".equals(paramName)) {
                LOGGER.log(Level.INFO, "   {0} = [PROTECTED]", paramName);
            } else {
                LOGGER.log(Level.INFO, "   {0} = {1}", new Object[]{paramName, request.getParameter(paramName)});
            }
        }
        LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 参数列表结束.");

        String action = request.getParameter("action");
        LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 获取到的 action 参数: {0}", action);

        if (action == null || action.isEmpty()) {
            LOGGER.log(Level.WARNING, "UserManagementServlet: doPost - action 参数为 null 或空，发送 400 错误.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少操作参数");
            return;
        }
        
        //Hibernate 的数据库 Session 跟交易控制
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session hibernateSession = factory.getCurrentSession();
        Transaction transaction = hibernateSession.getTransaction();

        try {
             // 开始一个数据库交易
            UserService userService = new UserServiceImpl(hibernateSession);

            switch (action) {
                case "getUsers": 
                    LOGGER.log(Level.WARNING, "UserManagementServlet: doPost - 错误：POST 收到 getUsers 动作，这应该是 GET 请求。");
                    response.getWriter().write("错误：此操作不应通过 POST 请求。");
                    break;

                case "updatePassword":
                    LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 处理 updatePassword 动作.");
                    String targetUsername = request.getParameter("targetUsername");
                    String newPassword = request.getParameter("newPassword");

                    LOGGER.log(Level.INFO, "UserManagementServlet: doPost - 目标用户名: {0}", targetUsername);
                    
                    if (targetUsername == null || newPassword == null || targetUsername.isEmpty() || newPassword.isEmpty()) {
                        LOGGER.log(Level.WARNING, "UserManagementServlet: doPost - 更新密码参数为空，发送 400 错误.");
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "帐号或新密码不能为空");
                        return;
                    }
                    
                    boolean success = userService.updateUserPassword(targetUsername, newPassword);
                    if (success) {
                        LOGGER.log(Level.INFO, "UserManagementServlet: 使用者 '{0}' 的密码已更新成功。", targetUsername);
                   
                        response.getWriter().write("success");
                    } else {
                        LOGGER.log(Level.WARNING, "UserManagementServlet: 更新使用者 '{0}' 密码失败（Service 层返回 false）。", targetUsername);
                        transaction.rollback(); // 更新失败就回滚交易
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "密码更新失败，请检查日志");
                    }
                    break;

                default:
                    LOGGER.log(Level.WARNING, "UserManagementServlet: doPost - 无效的操作: {0}，发送 400 错误.", action);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的操作");
                    break;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "UserManagementServlet: 调用 UserService 时发生未预期错误: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误： " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.log(Level.INFO, "UserManagementServlet: 进入 doGet 方法.");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null ||
            (!("admin".equals(session.getAttribute("userRole"))) && !("personnel".equals(session.getAttribute("userRole"))))) {
            LOGGER.log(Level.WARNING, "UserManagementServlet: doGet - 权限不足或未登入。Session ID: {0}, User: {1}, Role: {2}",
                new Object[]{session != null ? session.getId() : "N/A", session != null ? session.getAttribute("loggedInUser") : "N/A", session != null ? session.getAttribute("userRole") : "N/A"});
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "无权访问此功能");
            return;
        }

        String action = request.getParameter("action");
        LOGGER.log(Level.INFO, "UserManagementServlet: doGet - 获取到的 action 参数: {0}", action);

        if (action == null || action.isEmpty()) {
            LOGGER.log(Level.WARNING, "UserManagementServlet: doGet - action 参数为 null 或空，发送 400 错误.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少操作参数");
            return;
        }

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session hibernateSession = factory.getCurrentSession();
        Transaction transaction = hibernateSession.getTransaction();

        try {

            UserService userService = new UserServiceImpl(hibernateSession);

            if ("getUsers".equals(action)) {
                LOGGER.log(Level.INFO, "UserManagementServlet: doGet - 处理 getUsers 动作.");
                List<String> usernames = userService.getAllUsernames();
         
                response.getWriter().write(String.join(",", usernames));
            } else {
                LOGGER.log(Level.WARNING, "UserManagementServlet: doGet - 无效的 GET 操作: {0}，发送 400 错误.", action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "无效的 GET 操作");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.log(Level.SEVERE, "UserManagementServlet: doGet 时发生未预期错误: {0}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误： " + e.getMessage());
        }
    }
}
