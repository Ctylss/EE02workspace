# EE02workspace
小組專案

🧱 主專案總結構
```js
management system/
├── packages/                        # ← 用 git subtree 拉進各模組
│   ├── employee/                    # 人員員工模組
│   ├── machine/                     # 設備機台模組
│   ├── workorder/                   # 工單模組
│   ├── material/                    # 生產物料模組
│   └── attendance/                  # 出缺勤/人資模組
│
├── db/
│   └── schema/
│       ├── create-employee-table.sql
│       ├── create-machine-table.sql
│       ├── create-workorder-table.sql
│       ├── create-material-table.sql
│       └── create-attendance-table.sql
│
├── index.js                         # 主後端入口，整合所有模組
├── README.md
└── .env

📦 各模組子專案架構範例
employee-module/
├── config/自己的設定檔案
│   └── db.config.js
│
├── controller/連接網頁的或是自己的方法
│   └── employee.controller.js
│
├── model/
│   └── employee.model.js
│
├── repository/
│   └── employee.repository.js
│
├── service/
│   └── employee.service.js
│
├── dto/
│   └── employee.dto.js
│
├── routes/
│   └── employee.routes.js
│
├── migrations/
│   └── create-employee-table.sql
│
├── index.js                        # 匯出 router 給主專案用
└── README.md
```

主專案整合這五個模組
 使用 git subtree 拉進子模組
```
git subtree add --prefix=packages/employee https://github.com/xxx/employee-module.git main --squash
git subtree add --prefix=packages/machine https://github.com/xxx/machine-module.git main --squash
```
# ...其餘三個模組

之後更新模組只需執行 git subtree pull
```
git subtree pull --prefix=packages/employee https://github.com/xxx/employee-module.git main --squash
```

主專案啟動整合範例

我還在想>:((((()
