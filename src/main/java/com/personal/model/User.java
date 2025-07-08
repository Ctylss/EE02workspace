package com.personal.model;

import java.io.Serializable; // 實現 Serializable 以便在 Session 中傳輸

/**
 * User Bean (Model) Class.
 * 用於表示應用程式中的使用者資訊。
 * 包含使用者名稱、密碼和角色。
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本 UID

    private int id; // 使用者 ID
    private String username; // 使用者名稱
    private String password; // 密碼 (注意：實際應用中應為雜湊值)
    private String role;     // 角色 (例如: admin, personnel, machine 等)

    // 預設建構子
    public User() {
    }

    // 帶所有參數的建構子，通常用於從資料庫讀取資料後創建物件
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // 帶部分參數的建構子，通常用於插入新使用者時 (ID 通常由資料庫自動生成)
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // --- Getter 和 Setter 方法 ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // 覆寫 toString 方法，便於日誌輸出和除錯
    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", password='[PROTECTED]'" + // 密碼不應在 toString 中直接暴露
               ", role='" + role + '\'' +
               '}';
    }

    // 覆寫 equals 和 hashCode 方法，以便在集合中正確比較物件
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
               username.equals(user.username); // 通常基於 ID 或唯一業務鍵比較
    }

    @Override
    public int hashCode() {
        // 通常基於 ID 或唯一業務鍵生成 hashCode
        return java.util.Objects.hash(id, username);
    }
}
