package com.personnel.service;

import java.util.List;

import com.personnel.model.User;

/**
 * UserService (Business Service Layer) Interface.
 * 定義了使用者相關的業務操作。
 * 例如：使用者登入、密碼修改、使用者資訊管理等。
 */
public interface UserService {

    /**
     * 驗證使用者登入。
     * 這是業務層的登入方法，它會調用 DAO 層來檢查使用者名稱和密碼。
     *
     * @param username 使用者輸入的使用者名稱。
     * @param password 使用者輸入的密碼。
     * @return 如果登入成功，返回包含使用者角色和基本資訊的 User 物件；如果登入失敗，返回 null。
     */
    User loginUser(String username, String password);

    /**
     * 更新指定使用者的密碼。
     *
     * @param username 要更新密碼的使用者名稱。
     * @param newPassword 新密碼 (這裡傳入的應該是明文，業務層負責雜湊，然後傳給 DAO)。
     * @return 如果更新成功返回 true，否則返回 false。
     */
    boolean updateUserPassword(String username, String newPassword);

    /**
     * 獲取所有使用者名稱的列表。
     *
     * @return 所有使用者名稱的 List。
     */
    List<String> getAllUsernames();

    // 您可以在此處添加其他業務邏輯，例如：
    // boolean registerUser(User user);
    // User getUserProfile(String username);
}