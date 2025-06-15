CREATE TABLE machines (
    id INT PRIMARY KEY IDENTITY(1,1),          -- ����ID
    name VARCHAR(50),                          -- �����W��
    mstatus VARCHAR(20),                        -- ���A�]�p�G�B�त/���פ��^
    location VARCHAR(50)                       -- ������m
);

CREATE TABLE machineService (
    id INT PRIMARY KEY IDENTITY(1,1),          -- �A�Ȭ���ID
    machineId INT,                             -- ���ݾ���ID
    type VARCHAR(20),                          -- �����]Repair �� Maintenance�^
    description TEXT,                          -- ���D�y�z / �O�i����
    serviceTime DATETIME,                      -- �^���ɶ� / �Ƶ{�ɶ�
    msstatus VARCHAR(20),                        -- ���A�]�p�GPending�BIn Progress�BCompleted�^
    employeeId INT,                            -- ���@�H��ID�]�i�s�H���޲z�Ҳա^
    FOREIGN KEY (machineId) REFERENCES machines(id)
);

CREATE TABLE machineFiles (
    id INT PRIMARY KEY IDENTITY(1,1),              -- �ɮ�ID
    machineId INT,                                 -- ���ݾ���ID
    fileName VARCHAR(100),                         -- �ɮצW��
    filePath VARCHAR(255),                         -- �x�s���|
    uploadTime DATETIME NOT NULL,				   -- �W�Ǯɶ�
    FOREIGN KEY (machineId) REFERENCES machines(id)
);

--CREATE TABLE machineLogs (
--    id INT PRIMARY KEY IDENTITY(1,1),              -- �����s���A�۰ʻ��W�D�
--    machineId INT NOT NULL,                         -- ���ݾ���ID�]�~��A�Ѧ�machines����id�^
--    logTime DATETIME NOT NULL DEFAULT GETDATE(),   -- �����ɶ��A�w�]�����e�ɶ�
--    temperature FLOAT,                              -- �������ɪ��ūס]���ס^
--    rpm INT,                                       -- �����C������t�]RPM�^
--    runStatus VARCHAR(20),                          -- �B�બ�A�]�Ҧp�GRunning�BIdle�BError�^
--    voltage FLOAT,                                  -- �q���]��S�^
--    currentAmp FLOAT,                               -- �q�y�]�w���^
--    FOREIGN KEY (machineId) REFERENCES machines(id)
--);
