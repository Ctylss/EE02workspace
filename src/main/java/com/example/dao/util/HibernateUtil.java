package com.example.dao.util;

import com.example.model.Product; // 確保這裡導入了 Product 類

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
            // 從 hibernate.cfg.xml 載入配置
            serviceRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml") // 加載 hibernate.cfg.xml
                    .build();

            // 建立 MetadataSources
            MetadataSources metadataSources = new MetadataSources(serviceRegistry);

            // ====== 關鍵一步：添加映射的實體類 =======
            // 您必須明確告訴 Hibernate 哪些類是實體
            metadataSources.addAnnotatedClass(Product.class); // <-- 確保您的 Product.class 在這裡被添加了

            Metadata metadata = metadataSources.getMetadataBuilder().build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();
            LOGGER.info("Hibernate SessionFactory initialized successfully.");

        } catch (Exception e) {
            LOGGER.error("Failed to initialize Hibernate SessionFactory.", e);
            if (serviceRegistry != null) {
                StandardServiceRegistryBuilder.destroy(serviceRegistry);
            }
            throw new ExceptionInInitializerError(e); // 將異常包裝起來
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

    // 輔助方法：回滾事務
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
