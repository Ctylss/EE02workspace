package com.personal.dao.impl;

import com.example.dao.util.DBUtil;
import com.personal.dao.UserDAO;
import com.personal.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());

    @Override
    public User getUserByUsername(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        String sql = "SELECT id, username, password, role FROM Users WHERE username = ?";
        LOGGER.log(Level.INFO, "UserDAOImpl: 執行查詢使用者ByUsername SQL: {0} with username: {1}", new Object[]{sql, username});

        try {
            conn = DBUtil.getConnection();
            LOGGER.log(Level.INFO, "UserDAOImpl: 已獲取資料庫連接.");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                LOGGER.log(Level.INFO, "UserDAOImpl: 使用者 '{0}' 已找到，角色為 '{1}'. ID: {2}", new Object[]{username, user.getRole(), user.getId()});
            } else {
                LOGGER.log(Level.INFO, "UserDAOImpl: 使用者 '{0}' 未找到.", username);
            }
        } catch (SQLException e) {
            // 修正日誌語句：當只有一個佔位符，且最後一個參數是異常時，直接傳遞異常
            LOGGER.log(Level.SEVERE, "UserDAOImpl: 查詢使用者ByUsername時資料庫操作錯誤: {0}", e);
            throw e;
        } finally {
            DBUtil.close(conn, pstmt, rs);
            LOGGER.log(Level.INFO, "UserDAOImpl: 查詢使用者ByUsername資源已關閉.");
        }
        return user;
    }

    @Override
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rowsAffected = 0;

        String sql = "UPDATE Users SET password = ? WHERE username = ?";
        LOGGER.log(Level.INFO, "UserDAOImpl: 執行更新密碼SQL: {0} for username: {1}", new Object[]{sql, username});

        try {
            conn = DBUtil.getConnection();
            LOGGER.log(Level.INFO, "UserDAOImpl: 已獲取資料庫連接.");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword); // 警告：這裡仍是明文密碼
            pstmt.setString(2, username);
            rowsAffected = pstmt.executeUpdate(); // 執行更新操作
            LOGGER.log(Level.INFO, "UserDAOImpl: 更新使用者 '{0}' 密碼影響的行數: {1}", new Object[]{username, rowsAffected});
        } catch (SQLException e) {
            // 修正日誌語句：當有多個佔位符，且最後一個參數是異常時，將其他參數放入 Object[]
            LOGGER.log(Level.SEVERE, "UserDAOImpl: 更新密碼時資料庫操作錯誤 for '" + username + "': " + e.getMessage(), e);
            throw e;
        } finally {
            DBUtil.close(conn, pstmt);
            LOGGER.log(Level.INFO, "UserDAOImpl: 更新密碼資源已關閉.");
        }
        return rowsAffected > 0;
    }

    @Override
    public List<String> getAllUsernames() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<String> usernames = new ArrayList<>();

        String sql = "SELECT username FROM Users ORDER BY username";
        LOGGER.log(Level.INFO, "UserDAOImpl: 執行獲取所有使用者名稱SQL: {0}", sql);

        try {
            conn = DBUtil.getConnection();
            LOGGER.log(Level.INFO, "UserDAOImpl: 已獲取資料庫連接.");
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
            LOGGER.log(Level.INFO, "UserDAOImpl: 獲取到 {0} 個使用者名稱.", usernames.size());
        } catch (SQLException e) {
            // 修正日誌語句
            LOGGER.log(Level.SEVERE, "UserDAOImpl: 獲取使用者名稱列表時資料庫操作錯誤: {0}", e);
            throw e;
        } finally {
            DBUtil.close(conn, pstmt, rs);
            LOGGER.log(Level.INFO, "UserDAOImpl: 獲取所有使用者名稱資源已關閉.");
        }
        return usernames;
    }
}
