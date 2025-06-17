CREATE TABLE departments (
    id INT IDENTITY(1,1) PRIMARY KEY,   -- 部門唯一識別碼，從1開始自動遞增
    name VARCHAR(50) NOT NULL UNIQUE    -- 部門名稱，必填且不能重複
);


SELECT * FROM departments;



CREATE TABLE employees (
    -- 基本識別
    id INT IDENTITY(1,1) PRIMARY KEY,       -- 員工唯一ID
    username VARCHAR(50) NOT NULL UNIQUE,   -- 登入帳號，唯一
    password_hash VARCHAR(255) NOT NULL,    -- 加密密碼，安全儲存
    
    -- 個人資訊
    full_name VARCHAR(100) NOT NULL,        -- 員工姓名
    phone VARCHAR(20),                      -- 聯絡電話
    email VARCHAR(100) UNIQUE,              -- 電子郵件，唯一
    position VARCHAR(50),                   -- 職位
    hire_date DATE,                         -- 到職日期
    
    -- 系統角色與權限
    role VARCHAR(20) DEFAULT 'employee',    -- 系統角色（員工/主管/管理員）
    department_id INT,                      -- 所屬部門（關聯到departments表）
    is_active BIT DEFAULT 1,               -- 帳號狀態（1=啟用，0=停用）
    
    -- 安全與登入
    last_login_time DATETIME,              -- 最後登入時間
    failed_login_attempts INT DEFAULT 0,   -- 登入失敗次數（防暴力破解）
    
    -- 關聯約束
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

SELECT * FROM employees;



CREATE TABLE login_logs (
    id INT IDENTITY(1,1) PRIMARY KEY,        -- 記錄唯一ID
    employee_id INT NOT NULL,                -- 登入的員工ID
    login_time DATETIME DEFAULT GETDATE(),   -- 登入時間，自動記錄當前時間
    FOREIGN KEY (employee_id) REFERENCES employees(id)  -- 關聯到員工表
);
SELECT * FROM login_logs;