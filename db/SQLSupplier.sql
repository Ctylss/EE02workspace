--供應商
CREATE TABLE Supplier (
    supplierId INT PRIMARY KEY IDENTITY(1,1),         -- 供應商的唯一識別碼，主鍵，自動從 1 開始遞增
    supplierName NVARCHAR(100) NOT NULL,              -- 供應商名稱，不能為空
    pm NVARCHAR(50),                                  -- 負責窗口（業務、聯絡人）
    supplierPhone NVARCHAR(20),                       -- 聯絡電話
    supplierEmail NVARCHAR(100),                      -- 聯絡信箱
    supplierAddress NVARCHAR(200)                     -- 公司地址
);

--物料
CREATE TABLE Material (
    materialId INT PRIMARY KEY IDENTITY(1,1),         -- 物料ID，主鍵，自動遞增
    materialName NVARCHAR(100) NOT NULL,              -- 物料名稱（例如 PCB 板），必填
    unit NVARCHAR(20) NOT NULL,                       -- 單位（如「片」、「顆」、「組」），必填
    price DECIMAL(10,2) CHECK (price >= 0),           -- 預設單價（最多10位數，小數2位，金額不可為負）
    materialDescription NVARCHAR(200)                 -- 物料說明（例如：高階GPU顯示晶片），可空值
);

--訂單
CREATE TABLE PurchaseOrder (
    orderId INT PRIMARY KEY IDENTITY(1000,1),         -- 訂單ID，自 1000 開始遞增，作為主鍵
    supplierId INT NOT NULL,                          -- 供應商ID（外鍵），不可為空
    orderDate DATE DEFAULT GETDATE(),                 -- 下單日期，預設為當天日期
    orderStatus NVARCHAR(20) DEFAULT 'PENDING',       -- 訂單狀態，預設為待處理（PENDING）
    subTotal DECIMAL(12,2),                           -- 訂單小計（總金額），可為空（後續可由程式計算）

    FOREIGN KEY (supplierId) REFERENCES Supplier(supplierId)  -- 與 Supplier 表建立外鍵關聯
);

--訂單明細
CREATE TABLE PurchaseOrderItem (
    order_item_id INT PRIMARY KEY IDENTITY(1,1),       -- 明細項目的唯一 ID，自動遞增主鍵
    orderId INT NOT NULL,                              -- 所屬的訂單 ID（外鍵），不可為空
    materialId INT NOT NULL,                           -- 採購的物料 ID（外鍵），不可為空
    quantity INT CHECK (quantity > 0),                 -- 採購數量，必須 > 0
    unitPrice DECIMAL(10,2) CHECK (unitPrice >= 0),    -- 當時下單的單價（與 Material.price 不一定相同）

    FOREIGN KEY (orderId) REFERENCES PurchaseOrder(orderId),   -- 關聯到訂單主檔
    FOREIGN KEY (materialId) REFERENCES Material(materialId)   -- 關聯到物料表
);


-- 供應商
INSERT INTO Supplier (supplierName, pm, supplierPhone, supplierEmail, supplierAddress)
VALUES 
('華通PCB', '陳小姐', '02-12345678', 'sales@huatong.com.tw', '新竹市光復路一段');

-- 物料
INSERT INTO Material (materialName, unit, price, materialDescription)
VALUES 
('PCB板', '片', 100.00, '高階顯卡用印刷電路板'),
('顯示晶片 NT78', '顆', 1200.00, '高效能 GPU 晶片'),
('匯流排接口', '組', 50.00, 'PCIe 接頭用於顯卡連接');

-- 主訂單
INSERT INTO PurchaseOrder (supplierId, orderDate,orderStatus)
VALUES (1, GETDATE(), 'PENDING');

-- 明細（假設上面自動產生的 order_id = 1000，material_id = 1）
INSERT INTO PurchaseOrderItem (orderId, materialId, quantity, unitPrice)
VALUES (1000, 1, 1000, 100.00);


--範例搜尋
SELECT 
    po.orderId AS 訂單編號,
    s.supplierName AS 供應商名稱,
    po.orderDate AS 下單日期,
    po.orderStatus AS 訂單狀態,
    m.materialName AS 物料名稱,
    poi.quantity AS 數量,
    poi.unitPrice AS 價格,
    (poi.quantity * poi.unitPrice) AS 總計
FROM PurchaseOrder po
JOIN Supplier s ON po.supplierId = s.supplierId
JOIN PurchaseOrderItem poi ON po.orderId = poi.orderId
JOIN Material m ON poi.materialId = m.materialId
ORDER BY po.orderId;

---刪除表格
DROP TABLE Supplier
DROP TABLE Material
DROP TABLE PurchaseOrder
DROP TABLE PurchaseOrderItem