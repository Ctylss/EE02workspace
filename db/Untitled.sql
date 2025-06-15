CREATE TABLE [machines] (
  [id] int PRIMARY KEY IDENTITY(1, 1),
  [name] varchar(50),
  [mstatus] varchar(20),
  [location] varchar(50)
)
GO

CREATE TABLE [machineService] (
  [id] int PRIMARY KEY IDENTITY(1, 1),
  [machineId] int,
  [type] varchar(20),
  [description] text,
  [serviceTime] datetime,
  [msstatus] varchar(20),
  [employeeId] int
)
GO

CREATE TABLE [machineFiles] (
  [id] int PRIMARY KEY IDENTITY(1, 1),
  [machineId] int,
  [fileName] varchar(100),
  [filePath] varchar(255),
  [uploadTime] datetime
)
GO

CREATE TABLE [machineLogs] (
  [id] int PRIMARY KEY IDENTITY(1, 1),
  [machineId] int,
  [logTime] datetime,
  [temperature] float,
  [rpm] int,
  [runStatus] varchar(20),
  [voltage] float,
  [currentAmp] float
)
GO

ALTER TABLE [machineService] ADD FOREIGN KEY ([machineId]) REFERENCES [machines] ([id])
GO

ALTER TABLE [machineFiles] ADD FOREIGN KEY ([machineId]) REFERENCES [machines] ([id])
GO

ALTER TABLE [machineLogs] ADD FOREIGN KEY ([machineId]) REFERENCES [machines] ([id])
GO
