package com.personnel.service.impl;

import com.personnel.dao.UserDAO;
import com.personnel.dao.impl.UserDAOImpl;
import com.personnel.model.User;
import com.personnel.service.UserService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    private UserDAO userDAO;

    public UserServiceImpl(Session session) {
        this.userDAO = new UserDAOImpl(session);
    }
 // 建構子：建立這個 Service 的時候，傳進一個 Hibernate 的 Session，
 // 用來初始化 DAO（後面會呼叫 DAO 去做資料庫操作）

    @Override
    public User loginUser(String username, String password) {
        LOGGER.log(Level.INFO, "UserServiceImpl: 嘗試登入使用者: {0}", username);
        User user = null;
        try {
            User storedUser = userDAO.getUserByUsername(username);

            if (storedUser != null) {
                // 警告：這裡直接比較明文密碼，非常不安全！
                // 在生產環境中，您應該比較雜湊後的密碼。
                if (password.equals(storedUser.getPassword())) {
                    user = storedUser;
                    LOGGER.log(Level.INFO, "UserServiceImpl: 使用者 '{0}' 登入成功，角色為 '{1}'.", new Object[]{username, user.getRole()});
                } else {
                    LOGGER.log(Level.WARNING, "UserServiceImpl: 使用者 '{0}' 密碼不匹配.", username);
                }
            } else {
                LOGGER.log(Level.WARNING, "UserServiceImpl: 使用者 '{0}' 不存在.", username);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "UserServiceImpl: 登入時資料庫操作錯誤 for 使用者 '{0}': {1}", new Object[]{username, e.getMessage()});
        } catch (Exception e) { // 捕捉其他可能的 runtime 錯誤
            LOGGER.log(Level.SEVERE, String.format("UserServiceImpl: 登入時發生未預期錯誤 for 使用者 '%s': %s", username, e.getMessage()), e);
        }
        return user;
    }

    @Override
    public boolean updateUserPassword(String username, String newPassword) {
        LOGGER.log(Level.INFO, "UserServiceImpl: 嘗試更新使用者 '{0}' 的密碼.", username);
        try {
            // 警告：在傳遞給 DAO 之前，應先對 newPassword 進行雜湊處理。
            // 例如：String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            // return userDAO.updatePassword(username, hashedPassword);
            
            // 調用 DAO 層進行實際的資料庫更新
            boolean success = userDAO.updatePassword(username, newPassword);
            if (success) {
                LOGGER.log(Level.INFO, "UserServiceImpl: 使用者 '{0}' 密碼更新操作結果: 成功.", username);
            } else {
                LOGGER.log(Level.WARNING, "UserServiceImpl: 使用者 '{0}' 密碼更新操作結果: 失敗 (DAO 返回 false).", username);
            }
            return success;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "UserServiceImpl: 更新使用者 '{0}' 密碼時資料庫操作錯誤: {1}", new Object[]{username, e.getMessage()});
            return false;
        } catch (Exception e) { // 捕捉其他可能的 runtime 錯誤
            LOGGER.log(Level.SEVERE, String.format("UserServiceImpl: 更新使用者 '%s' 密碼時發生未預期錯誤: %s", username, e.getMessage()), e);
            return false;
        }
    }

    @Override
    public List<String> getAllUsernames() {
        LOGGER.log(Level.INFO, "UserServiceImpl: 嘗試獲取所有使用者名稱列表.");
        try {
            return userDAO.getAllUsernames();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "UserServiceImpl: 獲取使用者名稱列表時資料庫操作錯誤: " + e.getMessage(), e);
            return Collections.emptyList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format("UserServiceImpl: 獲取使用者名稱列表時發生未預期錯誤: %s", e.getMessage()), e);
            return Collections.emptyList();
        }
    }
}