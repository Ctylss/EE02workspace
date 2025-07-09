package com.personnel.dao.util;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.example.dao.util.HibernateUtil; // 确保导入的是正确的包
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = {"/*"})// 拦截所有请求
//@WebFilter(urlPatterns = {"/user/*","/test/*"})
public class OpenSessionViewFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取 SessionFactory
        SessionFactory factory = HibernateUtil.getSessionFactory();
        // 获取当前会话（Session），这依赖于 hibernate.cfg.xml 中的 current_session_context_class=thread 配置
        Session session = factory.getCurrentSession();
        
        try {
            session.beginTransaction(); // 开始一个事务
            System.out.println("Transaction Begin");
            
            chain.doFilter(request, response);   // 执行 Servlet
            
            session.getTransaction().commit(); // 提交事务
            System.out.println("Transaction Commit");
        }catch(Exception e) {
            // 如果发生异常，回滚事务
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
                System.out.println("Transaction Rollback");
            }
            // 记录详细的错误信息，而不仅仅是消息
            e.printStackTrace(); // 打印堆栈追踪到控制台
            System.out.println("Error during filter processing: " + e.getMessage());
            throw new ServletException("Error processing request", e); // 重新抛出异常，让容器处理
        }finally {
            // 在这里不需要手动关闭 session，因为 getCurrentSession() 管理的 session 会自动在事务结束后关闭
            // 只有当使用 openSession() 时才需要手动关闭 session.close()
            System.out.println("Session Closed (managed by Hibernate)");
        }
    }

}
