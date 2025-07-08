package com.personal.dao;

import java.sql.SQLException; // 處理資料庫操作可能拋出的異常

import com.personal.model.User;

/**
 * UserDAO (Data Access Object) Interface.
 * 定義了對使用者資料進行持久化操作的合約。
 * 實現類將負責與資料庫進行實際的互動。
 */
public interface UserDAO {

    /**
     * 根據使用者名稱查詢使用者。
     * 用於登入驗證時獲取使用者的詳細資訊（包括密碼和角色）。
     *
     * @param username 要查詢的使用者名稱。
     * @return 如果找到，則返回對應的 User 物件；如果未找到，則返回 null。
     * @throws SQLException 如果在資料庫操作過程中發生錯誤。
     */
    User getUserByUsername(String username) throws SQLException;

    /**
     * 更新指定使用者的密碼。
     * 用於人事部門或管理員修改使用者密碼。
     *
     * @param username 要更新密碼的使用者名稱。
     * @param newPassword 新密碼 (注意：實際應用中應為雜湊值)。
     * @return 如果更新成功返回 true，否則返回 false。
     * @throws SQLException 如果在資料庫操作過程中發生錯誤。
     */
    boolean updatePassword(String username, String newPassword) throws SQLException;

    /**
     * 獲取所有使用者名稱的列表。
     * 用於帳號管理介面的下拉選單。
     *
     * @return 所有使用者名稱的 List。
     * @throws SQLException 如果在資料庫操作過程中發生錯誤。
     */
    java.util.List<String> getAllUsernames() throws SQLException;

    // 您可以在此處根據需要添加其他 CRUD 操作，例如：
    // void addUser(User user) throws SQLException;
    // void deleteUser(int userId) throws SQLException;
    // List<User> getAllUsers() throws SQLException;
}
