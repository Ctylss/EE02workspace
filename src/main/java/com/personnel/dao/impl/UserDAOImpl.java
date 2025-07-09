package com.personnel.dao.impl;

import com.personnel.dao.UserDAO;
import com.personnel.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());
    private Session session;//Hibernate 資料庫的操作通道

    public UserDAOImpl(Session session) {//把資料庫的 Session 傳進來存起來
        this.session = session;
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        User user = null;

        String hql = "FROM User WHERE username = :username";
        LOGGER.log(Level.INFO, "UserDAOImpl: 執行查詢使用者ByUsername HQL: {0} with username: {1}", new Object[]{hql, username});

        try {
            LOGGER.log(Level.INFO, "UserDAOImpl: 已獲取 Hibernate Session.");
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);
            user = query.uniqueResult();

            if (user != null) {
                LOGGER.log(Level.INFO, "UserDAOImpl: 使用者 '{0}' 已找到，角色為 '{1}'. ID: {2}", new Object[]{username, user.getRole(), user.getId()});
            } else {
                LOGGER.log(Level.INFO, "UserDAOImpl: 使用者 '{0}' 未找到.", username);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserDAOImpl: 查詢使用者ByUsername時發生錯誤: {0}", e.getMessage());
            throw new SQLException("查詢使用者時發生錯誤", e);
        } finally {
            LOGGER.log(Level.INFO, "UserDAOImpl: 查詢使用者ByUsername操作完成.");
        }
        return user;
    }

    @Override
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        int rowsAffected = 0;

        String hql = "UPDATE User SET password = :newPassword WHERE username = :username";
        LOGGER.log(Level.INFO, "UserDAOImpl: 執行更新密碼HQL: {0} for username: {1}", new Object[]{hql, username});

        try {
            LOGGER.log(Level.INFO, "UserDAOImpl: 已獲取 Hibernate Session.");
            Query<?> query = session.createQuery(hql);
            query.setParameter("newPassword", newPassword); // 警告：這裡仍是明文密碼
            query.setParameter("username", username);
            rowsAffected = query.executeUpdate(); // 執行更新操作
            LOGGER.log(Level.INFO, "UserDAOImpl: 更新使用者 '{0}' 密碼影響的行數: {1}", new Object[]{username, rowsAffected});
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserDAOImpl: 更新密碼時發生錯誤 for '" + username + "': " + e.getMessage(), e);
            throw new SQLException("更新密碼時發生錯誤", e);
        } finally {
            LOGGER.log(Level.INFO, "UserDAOImpl: 更新密碼操作完成.");
        }
        return rowsAffected > 0;
    }

    @Override
    public List<String> getAllUsernames() throws SQLException {
        List<String> usernames = new ArrayList<>();

        String hql = "SELECT username FROM User ORDER BY username";
        LOGGER.log(Level.INFO, "UserDAOImpl: 執行獲取所有使用者名稱HQL: {0}", hql);

        try {
            LOGGER.log(Level.INFO, "UserDAOImpl: 已獲取 Hibernate Session.");
            Query<String> query = session.createQuery(hql, String.class);
            usernames = query.list();

            LOGGER.log(Level.INFO, "UserDAOImpl: 獲取到 {0} 個使用者名稱.", usernames.size());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserDAOImpl: 獲取使用者名稱列表時發生錯誤: {0}", e.getMessage());
            throw new SQLException("獲取使用者名稱列表時發生錯誤", e);
        } finally {
            LOGGER.log(Level.INFO, "UserDAOImpl: 獲取所有使用者名稱操作完成.");
        }
        return usernames;
    }
}