CREATE TABLE machines (
    id INT PRIMARY KEY IDENTITY(1,1),          -- 機器ID
    name VARCHAR(50),                          -- 機器名稱
    mstatus VARCHAR(20),                        -- 狀態（如：運轉中/維修中）
    location VARCHAR(50)                       -- 機器位置
);

CREATE TABLE machineService (
    id INT PRIMARY KEY IDENTITY(1,1),          -- 服務紀錄ID
    machineId INT,                             -- 所屬機器ID
    type VARCHAR(20),                          -- 類型（Repair 或 Maintenance）
    description TEXT,                          -- 問題描述 / 保養說明
    serviceTime DATETIME,                      -- 回報時間 / 排程時間
    msstatus VARCHAR(20),                        -- 狀態（如：Pending、In Progress、Completed）
    employeeId INT,                            -- 維護人員ID（可連人員管理模組）
    FOREIGN KEY (machineId) REFERENCES machines(id)
);

CREATE TABLE machineFiles (
    id INT PRIMARY KEY IDENTITY(1,1),              -- 檔案ID
    machineId INT,                                 -- 所屬機器ID
    fileName VARCHAR(100),                         -- 檔案名稱
    filePath VARCHAR(255),                         -- 儲存路徑
    uploadTime DATETIME NOT NULL,				   -- 上傳時間
    FOREIGN KEY (machineId) REFERENCES machines(id)
);

--CREATE TABLE machineLogs (
--    id INT PRIMARY KEY IDENTITY(1,1),              -- 紀錄編號，自動遞增主鍵
--    machineId INT NOT NULL,                         -- 所屬機器ID（外鍵，參考machines表的id）
--    logTime DATETIME NOT NULL DEFAULT GETDATE(),   -- 紀錄時間，預設為當前時間
--    temperature FLOAT,                              -- 機器當時的溫度（攝氏度）
--    rpm INT,                                       -- 機器每分鐘轉速（RPM）
--    runStatus VARCHAR(20),                          -- 運轉狀態（例如：Running、Idle、Error）
--    voltage FLOAT,                                  -- 電壓（伏特）
--    currentAmp FLOAT,                               -- 電流（安培）
--    FOREIGN KEY (machineId) REFERENCES machines(id)
--);
