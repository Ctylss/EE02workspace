package com.personnel.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/logoutProcess") // 設定 Servlet 的 URL 映射
public class LogoutProcessServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(LogoutProcessServlet.class.getName());

    @Override // 覆寫 HttpServlet 的 doGet 方法
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("LogoutProcessServlet: 進入 doGet 方法 (處理登出請求)."); // Debug: 進入方法
        HttpSession session = request.getSession(false); // 嘗試獲取現有 Session，如果沒有則不創建

        if (session != null) {
            String username = (String) session.getAttribute("loggedInUser");
            session.invalidate(); // 使 Session 失效，移除所有 Session 屬性
            LOGGER.log(Level.INFO, "使用者 '{0}' 已成功登出，Session 已失效.", username);
        } else {
            LOGGER.log(Level.INFO, "嘗試登出，但未找到活動 Session.");
        }

        // 重定向回登入頁面
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override // 覆寫 HttpServlet 的 doPost 方法
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 通常登出是透過 GET 請求觸發的，但為了兼容性，也可以將 POST 導向 doGet
        doGet(request, response);
    }
}
