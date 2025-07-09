package com.personnel.servlet;

import com.personnel.model.User;
import com.personnel.service.UserService;
import com.personnel.service.impl.UserServiceImpl;
import com.example.dao.util.HibernateUtil; // 确保导入的是正确的包

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction; // 仍然需要 Transaction 类，用于回滚

@WebServlet("/loginProcess") // 对映 login.jsp 中表单的 action
public class LoginProcessServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(LoginProcessServlet.class.getName());

    @Override // 覆写 HttpServlet 的 doPost 方法
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        LOGGER.log(Level.INFO, "接收到登入请求 - 使用者名称: {0}", username);

        // 获取由 OpenSessionViewFilter 管理的当前 Session
        // 不需要再次调用 beginTransaction()，因为 Filter 已经处理了
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        // 事务由 Filter 开启，这里直接获取它
        Transaction transaction = session.getTransaction(); 

        try {
            // 建立 UserService 实例
            UserService userService = new UserServiceImpl(session);
            // 调用业务逻辑层的 loginUser 方法进行验证
            User user = userService.loginUser(username, password);

            if (user != null) { // 登入成功
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("loggedInUser", user.getUsername()); // 将使用者名称存入 Session
                httpSession.setAttribute("userRole", user.getRole());       // 将使用者角色存入 Session (从资料库获取)

                LOGGER.log(Level.INFO, "使用者 '{0}' 登入成功，角色为 '{1}'. 重定向到仪表板.", new Object[]{user.getUsername(), user.getRole()});
                
                // 成功时，由 Filter 负责提交事务，这里不需要手动提交
                // transaction.commit(); // 移除此行
                response.sendRedirect(request.getContextPath() + "/dashboard/dashboard.jsp"); // 重定向到仪表板页面
            } else { // 登入失败
                LOGGER.log(Level.WARNING, "使用者 '{0}' 登入失败 (使用者名或密码错误). 重定向回登入页.", username);
                
                // 失败时，由 Filter 负责回滚事务，这里不需要手动提交
                // transaction.commit(); // 移除此行
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=invalid"); // 重定向回登入页面，并带上错误讯息参数
            }
        } catch (Exception e) {
            // 如果发生异常，确保事务被回滚（由 Filter 处理，但这里可以作为备用）
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // 仍然保留回滚，以防万一 Filter 未能捕获
            }
            LOGGER.log(Level.SEVERE, "登入处理时发生错误: {0}", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=system");
        }
    }

    // 您可能还会需要 doGet 方法，例如用于处理登出后的重定向
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 通常 doGet 不直接处理登入提交，但可以在这里提供一些提示或重定向回登入页
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}
