package com.mes.dao;

// 這個類別將處理與使用者相關的資料庫操作

public class UserDao {

    /**
     * 模擬使用者驗證。

     *
     * @param username 嘗試登入的使用者名稱
     * @param password 嘗試登入的密碼
     * @return 如果使用者名稱和密碼匹配（模擬），則返回 true；否則返回 false。
     */
    public boolean validateUser(String username, String password) {
        // --- 這是模擬資料庫驗證的程式碼 ---
 

      
        if ("admin".equals(username) && "12345".equals(password)) {
            System.out.println("模擬驗證成功：使用者 " + username);
            return true;
        }

        if ("user1".equals(username) && "password123".equals(password)) {
            System.out.println("模擬驗證成功：使用者 " + username);
            return true;
        }

        System.out.println("模擬驗證失敗：使用者 " + username);
        return false;
    }

   
}
