package com.example.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement; // 引入 PreparedStatement 以便統一關閉
import java.util.logging.Level;
import java.util.logging.Logger; // 引入 Logger

public class DBUtil {

    // 使用 Logger 進行日誌記錄
    private static final Logger LOGGER = Logger.getLogger(DBUtil.class.getName());

    // 資料庫連接 URL，建議將敏感資訊（如用戶名、密碼、實際的伺服器名稱）
    // 放在外部配置檔（例如：properties檔案或環境變數）中，而不是硬編碼在程式碼裡。
    // 為了範例，這裡保留硬編碼，但請務必在生產環境中避免這樣做。

    // 如果您的 SQL Server 實例名稱是 SQLEXPRESS，則連接字串中需要 'localhost\\SQLEXPRESS'。
    // 如果是預設實例，則直接 'localhost' 或伺服器 IP 即可。
    // 確保 'databaseName=jdbcDB' 是您實際的資料庫名稱。
    // encrypt=true;trustServerCertificate=true; 對於開發環境可能是必要的，生產環境建議配置真正的 SSL 憑證。
private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=jdbcDB;encrypt=true;trustServerCertificate=true;";


    // 設定預設的測試使用者名稱和密碼
    // **警告：請勿在生產環境中使用硬編碼的敏感憑證！**
    // 考慮使用環境變數、配置檔案或安全的主機配置來管理這些。
    private static final String USER = "testuser"; // <-- 請替換為您的實際資料庫用戶名
    private static final String PASS = "password"; // <-- 請替換為您的實際資料庫密碼

    static {
        try {
            // 加載 SQL Server JDBC 驅動
            // Class.forName() 在現代 JDBC 版本（JDBC 4.0 及更高版本）中通常不是必須的
            // 因為驅動管理器會自動發現服務提供者，但保留它無害，有時甚至有益。
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            LOGGER.log(Level.INFO, "SQL Server JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "SQL Server JDBC Driver not found! Make sure the JAR is in your classpath.", e);
            // 在驅動無法加載時，應用程式無法工作，所以拋出 RuntimeException 是合理的。
            throw new RuntimeException("Failed to load SQL Server JDBC driver.", e);
        }
    }

    /**
     * 獲取資料庫連接。
     * @return 一個資料庫連接物件。
     * @throws SQLException 如果資料庫連接失敗。
     */
    public static Connection getConnection() throws SQLException {
        LOGGER.log(Level.INFO, "Attempting to connect to database: {0}", DB_URL);
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        LOGGER.log(Level.INFO, "Database connection established successfully.");
        return connection;
    }

    /**
     * 關閉所有資料庫資源 (Connection, Statement/PreparedStatement, ResultSet)。
     * 建議在 try-with-resources 語句中使用，如果無法使用，則使用此方法。
     *
     * @param conn 要關閉的 Connection 物件。
     * @param stmt 要關閉的 Statement 或 PreparedStatement 物件。
     * @param rs 要關閉的 ResultSet 物件。
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        // 從內到外關閉資源，以避免資源洩漏。
        // ResultSet -> Statement/PreparedStatement -> Connection
        if (rs != null) {
            try {
                rs.close();
                LOGGER.log(Level.FINE, "ResultSet closed.");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing ResultSet: {0}", e.getMessage());
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
                LOGGER.log(Level.FINE, "Statement closed.");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing Statement: {0}", e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
                LOGGER.log(Level.INFO, "Database Connection closed.");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing Connection: {0}", e.getMessage());
            }
        }
    }

    /**
     * 關閉資料庫連接和 Statement/PreparedStatement。
     * @param conn 要關閉的 Connection 物件。
     * @param stmt 要關閉的 Statement 或 PreparedStatement 物件。
     */
    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null); // 呼叫三參數的 close 方法
    }

    /**
     * 關閉資料庫連接。
     * @param conn 要關閉的 Connection 物件。
     */
    public static void closeConnection(Connection conn) {
        close(conn, null, null); // 呼叫三參數的 close 方法
    }
}
