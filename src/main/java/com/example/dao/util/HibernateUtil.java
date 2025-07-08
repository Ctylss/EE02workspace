package com.example.dao.util;

// 确保导入了所有需要映射的实体类
import com.mes.bean.Supplier; // <-- 新增：导入 Supplier 类
import com.mes.bean.Material; // <-- 新增：导入 Material 类
import com.example.model.Product; // 保持不变，Product 已经存在

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for Hibernate SessionFactory management.
 * Provides a single SessionFactory instance for the application.
 */
public class HibernateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);
    private static StandardServiceRegistry serviceRegistry;
    private static SessionFactory sessionFactory;

    static {
        try {
            // 从 hibernate.cfg.xml 载入配置
            serviceRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml") // 加载 hibernate.cfg.xml
                    .build();

            // 建立 MetadataSources
            MetadataSources metadataSources = new MetadataSources(serviceRegistry);

            // ====== 关键一步：添加所有映射的实体类 =======
            // 您必须明确告诉 Hibernate 哪些类是实体
            metadataSources.addAnnotatedClass(Product.class); // 保持不变
            metadataSources.addAnnotatedClass(Supplier.class); // **新增：添加 Supplier 实体**
            metadataSources.addAnnotatedClass(Material.class); // **新增：添加 Material 实体**

            Metadata metadata = metadataSources.getMetadataBuilder().build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();
            LOGGER.info("Hibernate SessionFactory initialized successfully.");

        } catch (Exception e) {
            LOGGER.error("Failed to initialize Hibernate SessionFactory.", e);
            if (serviceRegistry != null) {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
            }
            throw new ExceptionInInitializerError(e); // 将异常包装起来
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            LOGGER.info("Hibernate SessionFactory closed.");
        }
        if (serviceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
            LOGGER.info("Hibernate ServiceRegistry destroyed.");
        }
    }

    // 辅助方法：回滚事务
    public static void rollbackTransaction(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
                LOGGER.warn("Transaction rolled back successfully.");
            } catch (Exception e) {
                LOGGER.error("Error during transaction rollback.", e);
            }
        }
    }

    public static Session openSession() {
        if (sessionFactory == null) {
            LOGGER.error("SessionFactory is null. Ensure HibernateUtil is initialized.");
            throw new IllegalStateException("Hibernate SessionFactory is not initialized.");
        }
        return sessionFactory.openSession();
    }
}
