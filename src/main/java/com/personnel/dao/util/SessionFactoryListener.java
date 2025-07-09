package com.personnel.dao.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import com.example.dao.util.HibernateUtil; // 确保导入的是正确的包

@WebListener
public class SessionFactoryListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 在 Web 应用关闭时关闭 SessionFactory
        HibernateUtil.closeSessionFactory();
        System.out.println("Session Factory Closed");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 在 Web 应用启动时初始化 SessionFactory
        // 简单调用 getSessionFactory() 会触发 HibernateUtil 的静态初始化块
        HibernateUtil.getSessionFactory();
        System.out.println("Session Factory Created");
    }

}
