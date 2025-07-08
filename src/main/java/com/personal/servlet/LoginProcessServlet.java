package com.personal.servlet;

import com.personal.model.User;
import com.personal.service.UserService;
import com.personal.service.impl.UserServiceImpl;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/loginProcess") // 對應 login.jsp 中表單的 action
public class LoginProcessServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(LoginProcessServlet.class.getName());

    private UserService userService; // 聲明 UserService 介面

    // 在 Servlet 初始化時實例化 UserService
    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserServiceImpl(); // 這裡直接實例化，實際框架會用依賴注入
        LOGGER.log(Level.INFO, "LoginProcessServlet 已初始化，UserService 已準備就緒.");
    }

    @Override // 覆寫 HttpServlet 的 doPost 方法
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        LOGGER.log(Level.INFO, "接收到登入請求 - 使用者名稱: {0}", username);

        // 調用業務邏輯層的 loginUser 方法進行驗證
        User user = userService.loginUser(username, password);

        if (user != null) { // 登入成功
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user.getUsername()); // 將使用者名稱存入 Session
            session.setAttribute("userRole", user.getRole());       // 將使用者角色存入 Session (從資料庫獲取)

            LOGGER.log(Level.INFO, "使用者 '{0}' 登入成功，角色為 '{1}'. 重定向到儀表板.", new Object[]{user.getUsername(), user.getRole()});
            response.sendRedirect(request.getContextPath() + "/dashboard/dashboard.jsp"); // 重定向到儀表板頁面
        } else { // 登入失敗
            LOGGER.log(Level.WARNING, "使用者 '{0}' 登入失敗 (使用者名或密碼錯誤). 重定向回登入頁.", username);
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=invalid"); // 重定向回登入頁面，並帶上錯誤訊息參數
        }
    }

    // 您可能還會需要 doGet 方法，例如用於處理登出後的重定向
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 通常 doGet 不直接處理登入提交，但可以在這裡提供一些提示或重定向回登入頁
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
