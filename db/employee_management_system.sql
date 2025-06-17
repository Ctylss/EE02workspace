CREATE TABLE departments (
    id INT IDENTITY(1,1) PRIMARY KEY,   -- �����ߤ@�ѧO�X�A�q1�}�l�۰ʻ��W
    name VARCHAR(50) NOT NULL UNIQUE    -- �����W�١A����B���୫��
);


SELECT * FROM departments;



CREATE TABLE employees (
    -- ���ѧO
    id INT IDENTITY(1,1) PRIMARY KEY,       -- ���u�ߤ@ID
    username VARCHAR(50) NOT NULL UNIQUE,   -- �n�J�b���A�ߤ@
    password_hash VARCHAR(255) NOT NULL,    -- �[�K�K�X�A�w���x�s
    
    -- �ӤH��T
    full_name VARCHAR(100) NOT NULL,        -- ���u�m�W
    phone VARCHAR(20),                      -- �p���q��
    email VARCHAR(100) UNIQUE,              -- �q�l�l��A�ߤ@
    position VARCHAR(50),                   -- ¾��
    hire_date DATE,                         -- ��¾���
    
    -- �t�Ψ���P�v��
    role VARCHAR(20) DEFAULT 'employee',    -- �t�Ψ���]���u/�D��/�޲z���^
    department_id INT,                      -- ���ݳ����]���p��departments��^
    is_active BIT DEFAULT 1,               -- �b�����A�]1=�ҥΡA0=���Ρ^
    
    -- �w���P�n�J
    last_login_time DATETIME,              -- �̫�n�J�ɶ�
    failed_login_attempts INT DEFAULT 0,   -- �n�J���Ѧ��ơ]���ɤO�}�ѡ^
    
    -- ���p����
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

SELECT * FROM employees;



CREATE TABLE login_logs (
    id INT IDENTITY(1,1) PRIMARY KEY,        -- �O���ߤ@ID
    employee_id INT NOT NULL,                -- �n�J�����uID
    login_time DATETIME DEFAULT GETDATE(),   -- �n�J�ɶ��A�۰ʰO����e�ɶ�
    FOREIGN KEY (employee_id) REFERENCES employees(id)  -- ���p����u��
);
SELECT * FROM login_logs;