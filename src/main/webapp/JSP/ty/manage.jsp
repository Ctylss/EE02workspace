<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>員工管理系統</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

/* 通用樣式 */
body {
    font-family: 'Inter', sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f4f7f6;
    color: #333;
    display: flex;
    min-height: 100vh;
}

.app-wrapper {
    display: flex;
    width: 100%;
}

/* 側邊欄樣式 */
.main-sidebar {
    width: 250px;
    background-color: #2c3e50;
    color: #ecf0f1;
    padding: 20px 0;
    box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
    border-top-right-radius: 15px;
    border-bottom-right-radius: 15px;
    height: 100vh;
    position: sticky;
    top: 0;
    left: 0;
}

.sidebar-brand {
    text-align: center;
    margin-bottom: 30px;
    padding: 0 15px;
}

.brand-link {
    display: flex;
    align-items: center;
    justify-content: center;
    text-decoration: none;
    color: #ecf0f1;
    font-size: 1.5em;
    font-weight: bold;
    padding: 10px 0;
    background-color: #34495e;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    transition: background-color 0.3s ease;
}

.brand-link:hover {
    background-color: #4a667b;
}

.brand-link i {
    margin-right: 10px;
    font-size: 1.8em;
    color: #1abc9c;
}

.sidebar-nav {
    flex-grow: 1;
}

.sidebar-menu {
    list-style: none;
    padding: 0;
    margin: 0;
}

.menu-item {
    position: relative;
    margin-bottom: 5px;
}

.menu-link {
    display: flex;
    align-items: center;
    padding: 12px 20px;
    color: #ecf0f1;
    text-decoration: none;
    font-size: 1em;
    transition: background-color 0.3s ease, color 0.3s ease;
    border-radius: 8px;
    margin: 0 10px;
}

.menu-link:hover, .menu-link.active {
    background-color: #1abc9c;
    color: #fff;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}

.menu-link i {
    margin-right: 15px;
    font-size: 1.2em;
}

.submenu-always-open {
    list-style: none;
    padding: 0;
    margin-top: 5px;
    background-color: #34495e;
    border-radius: 8px;
    margin: 5px 15px 10px 15px;
    box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

.submenu-always-open li {
    margin: 0;
}

.submenu-link {
    display: block;
    padding: 10px 25px;
    color: #bdc3c7;
    text-decoration: none;
    font-size: 0.95em;
    transition: background-color 0.3s ease, color 0.3s ease;
    border-radius: 6px;
    margin: 0 5px;
}

.submenu-link:hover {
    background-color: #2ecc71;
    color: #fff;
}

/* 主內容區域樣式 */
.main-right-content-wrapper {
    flex-grow: 1;
    display: flex;
    flex-direction: column;
}

.main-header {
    background-color: #fff;
    padding: 15px 30px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: sticky;
    top: 0;
    z-index: 1000;
    border-bottom-left-radius: 15px;
    border-bottom-right-radius: 15px;
    margin: 10px 10px 0 10px;
}

.header-left h1 {
    margin: 0;
    font-size: 1.5em;
    color: #2c3e50;
}

.header-right .user-info {
    display: flex;
    align-items: center;
    font-size: 1em;
    color: #555;
}

.header-right .user-info i {
    margin-right: 8px;
    color: #2980b9;
}

.actual-page-content {
    flex-grow: 1;
    padding: 20px;
    overflow-y: auto;
    margin: 10px;
    background-color: #ffffff;
    border-radius: 15px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.main-content {
    padding: 20px;
}

/* 員工管理特定樣式 */
.container {
    max-width: 100%;
    margin: 0 auto;
    padding: 20px;
    background-color: #ffffff;
    border-radius: 12px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
}

h1 {
    text-4xl font-bold text-gray-800 mb-6 text-center;
    color: #2c3e50;
    margin-bottom: 25px;
    text-align: center;
    font-size: 2em;
    font-weight: 600;
}

/* 用戶信息和搜索區域 */
.user-info-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 25px;
    padding: 15px 20px;
    background-color: #e3f2fd;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.user-info-text {
    color: #1565c0;
    font-size: 1.1em;
    font-weight: 500;
}

.logout-link {
    color: #1565c0;
    text-decoration: none;
    font-weight: 600;
    transition: color 0.3s ease;
}

.logout-link:hover {
    color: #0d47a1;
}

.search-and-add-section {
    margin-bottom: 25px;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    align-items: center;
    gap: 15px;
}

.search-controls {
    display: flex;
    flex: 1;
    gap: 10px;
    min-width: 300px;
}

.search-input {
    flex: 1;
    padding: 12px 15px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 1em;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.search-input:focus {
    border-color: #2980b9;
    box-shadow: 0 0 0 3px rgba(41, 128, 185, 0.2);
    outline: none;
}

.btn {
    padding: 12px 20px;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-size: 1em;
    font-weight: 600;
    transition: all 0.3s ease;
    display: inline-flex;
    align-items: center;
    gap: 8px;
    text-decoration: none;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.btn-primary {
    background-color: #2980b9;
    color: white;
}

.btn-primary:hover {
    background-color: #3498db;
}

.btn-secondary {
    background-color: #95a5a6;
    color: white;
}

.btn-secondary:hover {
    background-color: #7f8c8d;
}

.btn-success {
    background-color: #27ae60;
    color: white;
}

.btn-success:hover {
    background-color: #2ecc71;
}

.btn-warning {
    background-color: #f39c12;
    color: white;
    padding: 8px 12px;
    font-size: 0.9em;
}

.btn-warning:hover {
    background-color: #e67e22;
}

.btn-danger {
    background-color: #e74c3c;
    color: white;
    padding: 8px 12px;
    font-size: 0.9em;
}

.btn-danger:hover {
    background-color: #c0392b;
}

/* 表格樣式 */
.table-container {
    overflow-x: auto;
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.employee-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 0.95em;
}

.employee-table thead {
    background-color: #f8f9fa;
}

.employee-table th {
    padding: 15px 12px;
    text-align: left;
    font-weight: 600;
    color: #495057;
    border-bottom: 2px solid #dee2e6;
    text-transform: uppercase;
    font-size: 0.85em;
    letter-spacing: 0.5px;
}

.employee-table th:first-child {
    border-top-left-radius: 12px;
}

.employee-table th:last-child {
    border-top-right-radius: 12px;
}

.employee-table td {
    padding: 12px;
    border-bottom: 1px solid #e9ecef;
    color: #495057;
}

.employee-table tbody tr {
    transition: background-color 0.2s ease;
}

.employee-table tbody tr:hover {
    background-color: #f8f9fa;
}

.employee-table tbody tr:last-child td {
    border-bottom: none;
}

.action-buttons {
    display: flex;
    gap: 8px;
}

/* Modal 樣式 */
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: none;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal-overlay.show {
    display: flex;
}

.modal-content {
    background-color: white;
    border-radius: 12px;
    padding: 30px;
    width: 90%;
    max-width: 500px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.modal-header {
    margin-bottom: 25px;
    text-align: center;
}

.modal-title {
    font-size: 1.5em;
    font-weight: 600;
    color: #2c3e50;
    margin: 0;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    margin-bottom: 8px;
    font-weight: 600;
    color: #555;
}

.form-group input {
    width: 100%;
    padding: 12px 15px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 1em;
    box-sizing: border-box;
    transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.form-group input:focus {
    border-color: #2980b9;
    box-shadow: 0 0 0 3px rgba(41, 128, 185, 0.2);
    outline: none;
}

.form-group input:read-only {
    background-color: #f8f9fa;
    color: #6c757d;
}

.modal-actions {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    margin-top: 25px;
}

/* 消息提示 */
.message-box {
    position: fixed;
    bottom: 20px;
    right: 20px;
    padding: 15px 25px;
    border-radius: 8px;
    color: white;
    font-weight: 600;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    z-index: 1001;
    display: none;
    min-width: 300px;
}

.message-box.success {
    background-color: #27ae60;
}

.message-box.error {
    background-color: #e74c3c;
}

.message-box.info {
    background-color: #3498db;
}

.message-box.show {
    display: block;
    animation: slideIn 0.3s ease;
}

@keyframes slideIn {
    from {
        transform: translateX(100%);
        opacity: 0;
    }
    to {
        transform: translateX(0);
        opacity: 1;
    }
}

/* 確認對話框 */
.confirm-modal .modal-content {
    max-width: 400px;
    text-align: center;
}

.confirm-message {
    margin: 20px 0;
    font-size: 1.1em;
    color: #555;
}

.confirm-actions {
    display: flex;
    justify-content: center;
    gap: 15px;
    margin-top: 25px;
}

/* 載入狀態 */
.loading-row {
    text-align: center;
    color: #6c757d;
    font-style: italic;
}

.error-row {
    text-align: center;
    color: #e74c3c;
    font-weight: 600;
}

.empty-row {
    text-align: center;
    color: #6c757d;
}

/* 響應式設計 */
@media (max-width: 768px) {
    body {
        flex-direction: column;
    }
    
    .main-sidebar {
        width: 100%;
        height: auto;
        border-radius: 0;
        position: relative;
    }
    
    .main-header, .actual-page-content {
        margin: 0;
        border-radius: 0;
    }
    
    .search-and-add-section {
        flex-direction: column;
        align-items: stretch;
    }
    
    .search-controls {
        min-width: auto;
    }
    
    .user-info-section {
        flex-direction: column;
        align-items: flex-start;
        gap: 10px;
    }
    
    .table-container {
        border-radius: 0;
    }
    
    .employee-table {
        font-size: 0.85em;
    }
    
    .employee-table th,
    .employee-table td {
        padding: 8px 6px;
    }
    
    .action-buttons {
        flex-direction: column;
        gap: 4px;
    }
    
    .modal-content {
        margin: 10px;
        padding: 20px;
    }
}

/* Custom scrollbar */
::-webkit-scrollbar {
    width: 8px;
    height: 8px;
}

::-webkit-scrollbar-track {
    background: #e0e0e0;
    border-radius: 10px;
}

::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 10px;
}

::-webkit-scrollbar-thumb:hover {
    background: #555;
}
</style>
</head>
<body>
    <%
        // 檢查使用者是否已登入
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        String userRole = (String) session.getAttribute("userRole");

        // 將 userRole 設置為 Request 屬性，以便 common/sidebar.jsp 可以訪問
        request.setAttribute("userRole", userRole);

        // 僅允許 'admin' 和 'personnel' 訪問此頁面
        if (loggedInUser == null || (!"admin".equals(userRole) && !"personnel".equals(userRole))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
    %>

    <div class="app-wrapper">
        <aside class="main-sidebar" role="complementary" aria-label="側邊欄">
            <jsp:include page="/common/sidebar.jsp" />
        </aside>

        <div class="main-right-content-wrapper">
            <div class="actual-page-content">
                <div class="main-content">
                    <div class="container">
                        <h1>員工管理</h1>

                        <div class="user-info-section">
                            <p class="user-info-text">
                                您好，<span id="loggedInUser">載入中...</span> (<span id="userRole">載入中...</span>)
                            </p>
                            <a href="<%=request.getContextPath()%>/logoutProcess" class="logout-link">登出</a>
                        </div>

                        <div class="search-and-add-section">
                            <div class="search-controls">
                                <input type="text" id="searchTerm" placeholder="搜尋員工編號/姓名/部門/職位..." class="search-input">
                                <button id="searchButton" class="btn btn-primary">
                                    <i class="fas fa-search"></i>搜尋
                                </button>
                                <button id="resetSearchButton" class="btn btn-secondary">
                                    <i class="fas fa-redo"></i>重置
                                </button>
                            </div>
                            <button id="addEmployeeButton" class="btn btn-success">
                                <i class="fas fa-plus-circle"></i>新增員工
                            </button>
                        </div>

                        <div class="table-container">
                            <table class="employee-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>員工編號</th>
                                        <th>姓名</th>
                                        <th>部門</th>
                                        <th>職位</th>
                                        <th>入職日期</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody id="employeeTableBody">
                                    <tr>
                                        <td colspan="7" class="loading-row">載入員工數據...</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <div id="employeeModal" class="modal-overlay">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h2 id="modalTitle" class="modal-title">新增員工</h2>
                                </div>
                                <form id="employeeForm">
                                    <div class="form-group">
                                        <label for="employeeId">員工編號:</label>
                                        <input type="text" id="employeeId" name="employeeId" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="name">姓名:</label>
                                        <input type="text" id="name" name="name" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="department">部門:</label>
                                        <input type="text" id="department" name="department" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="position">職位:</label>
                                        <input type="text" id="position" name="position" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="hireDate">入職日期:</label>
                                        <input type="date" id="hireDate" name="hireDate" required>
                                    </div>
                                    <input type="hidden" id="employeeInternalId" name="id">

                                    <div class="modal-actions">
                                        <button type="button" id="closeModalButton" class="btn btn-secondary">取消</button>
                                        <button type="submit" id="submitEmployeeButton" class="btn btn-primary">提交</button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <div id="messageBox" class="message-box">
                            這是一個訊息！
                        </div>

                        <div id="confirmModal" class="modal-overlay confirm-modal">
                            <div class="modal-content">
                                <h3 class="modal-title">確認操作</h3>
                                <p id="confirmMessage" class="confirm-message">您確定要刪除這名員工嗎？</p>
                                <div class="confirm-actions">
                                    <button id="confirmCancelButton" class="btn btn-secondary">取消</button>
                                    <button id="confirmDeleteButton" class="btn btn-danger">刪除</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- 頁面底部內容的 footer.jsp --%>
                <jsp:include page="/common/footer.jsp" />
            </div>
        </div>
    </div>

    <script>
        // 從 JSP Session 獲取使用者名稱和角色
        const loggedInUser = "<%=session.getAttribute("loggedInUser") != null ? session.getAttribute("loggedInUser") : "Guest"%>";
        const userRole = "<%=session.getAttribute("userRole") != null ? session.getAttribute("userRole") : "guest"%>";

        document.getElementById('loggedInUser').textContent = loggedInUser;
        document.getElementById('userRole').textContent = userRole;

        const employeeTableBody = document.getElementById('employeeTableBody');
        const addEmployeeButton = document.getElementById('addEmployeeButton');
        const searchButton = document.getElementById('searchButton');
        const resetButton = document.getElementById('resetSearchButton');
        const searchTermInput = document.getElementById('searchTerm');
        const employeeModal = document.getElementById('employeeModal');
        const closeModalButton = document.getElementById('closeModalButton');
        const employeeForm = document.getElementById('employeeForm');
        const modalTitle = document.getElementById('modalTitle');
        const submitEmployeeButton = document.getElementById('submitEmployeeButton');
        const messageBox = document.getElementById('messageBox');

        // Custom Confirm Modal Elements
        const confirmModal = document.getElementById('confirmModal');
        const confirmMessage = document.getElementById('confirmMessage');
        const confirmCancelButton = document.getElementById('confirmCancelButton');
        const confirmDeleteButton = document.getElementById('confirmDeleteButton');
        let currentDeleteId = null;

        let isEditMode = false;

        // 檢查使用者角色以啟用/禁用功能
        const isAdminOrPersonnel = (userRole === 'admin' || userRole === 'personnel');
        if (!isAdminOrPersonnel) {
            addEmployeeButton.style.display = 'none';
        }

        // --- 訊息提示框功能 ---
        function showMessage(message, type = 'info') {
            messageBox.textContent = message;
            messageBox.className = 'message-box ' + type + ' show';
            setTimeout(() => {
                messageBox.classList.remove('show');
            }, 3000);
        }

        // --- 清空表單 ---
        function clearForm() {
            employeeForm.reset();
            document.getElementById('employeeInternalId').value = '';
            document.getElementById('employeeId').readOnly = false;
        }

        // --- 開啟/關閉 Modal ---
        function openModal(isEdit = false, employeeData = null) {
            isEditMode = isEdit;
            clearForm();
            if (isEdit) {
                modalTitle.textContent = '編輯員工';
                submitEmployeeButton.textContent = '更新';
                document.getElementById('employeeInternalId').value = employeeData.id;
                document.getElementById('employeeId').value = employeeData.employeeId;
                document.getElementById('name').value = employeeData.name;
                document.getElementById('department').value = employeeData.department;
                document.getElementById('position').value = employeeData.position;
                document.getElementById('hireDate').value = employeeData.hireDate;
                document.getElementById('employeeId').readOnly = true;
            } else {
                modalTitle.textContent = '新增員工';
                submitEmployeeButton.textContent = '提交';
            }
            employeeModal.classList.add('show');
        }

        function closeModal() {
            employeeModal.classList.remove('show');
        }

        // --- 開啟/關閉 Confirm Modal ---
        function showConfirmModal(message, onConfirm) {
            confirmMessage.textContent = message;
            confirmModal.classList.add('show');

            const handleConfirm = () => {
                onConfirm();
                confirmModal.classList.remove('show');
                confirmDeleteButton.removeEventListener('click', handleConfirm);
                confirmCancelButton.removeEventListener('click', handleCancel);
            };

            const handleCancel = () => {
                confirmModal.classList.remove('show');
                confirmDeleteButton.removeEventListener('click', handleConfirm);
                confirmCancelButton.removeEventListener('click', handleCancel);
            };

            confirmDeleteButton.addEventListener('click', handleConfirm);
            confirmCancelButton.addEventListener('click', handleCancel);
        }

        // --- 渲染員工列表 ---
        function renderEmployees(employees) {
            employeeTableBody.innerHTML = ''; // 清空現有行

            if (employees.length === 0) {
                employeeTableBody.innerHTML = '<tr><td colspan="7" class="empty-row">沒有找到員工數據。</td></tr>';
                return;
            }

            employees.forEach(employee => {
                const row = employeeTableBody.insertRow();
                row.insertCell().textContent = employee.id;
                row.insertCell().textContent = employee.employeeId;
                row.insertCell().textContent = employee.name;
                row.insertCell().textContent = employee.department;
                row.insertCell().textContent = employee.position;
                row.insertCell().textContent = employee.hireDate; // 確保日期格式正確

                const actionCell = row.insertCell();
                actionCell.classList.add('action-buttons');

                // 編輯按鈕
                const editButton = document.createElement('button');
                editButton.innerHTML = '<i class="fas fa-edit"></i> 編輯';
                editButton.classList.add('btn', 'btn-warning');
                editButton.addEventListener('click', () => openModal(true, employee));
                actionCell.appendChild(editButton);

                // 刪除按鈕
                const deleteButton = document.createElement('button');
                deleteButton.innerHTML = '<i class="fas fa-trash-alt"></i> 刪除';
                deleteButton.classList.add('btn', 'btn-danger');
                deleteButton.addEventListener('click', () => {
                    showConfirmModal(`您確定要刪除員工 "${employee.name}" (${employee.employeeId}) 嗎？`, () => {
                        deleteEmployee(employee.id);
                    });
                });
                actionCell.appendChild(deleteButton);
            });
             // 根據角色顯示/隱藏操作按鈕
            if (!isAdminOrPersonnel) {
                document.querySelectorAll('.action-buttons .btn').forEach(button => {
                    button.style.display = 'none';
                });
            }
        }

     // --- 載入員工列表 ---
        async function fetchEmployees(searchTermParam) { // 將參數名稱改為 searchTermParam 避免與內部的 searchTerm 混淆
            // 確保 searchTerm 總是一個字串，即使是 undefined 或 null 傳入
            const searchTerm = typeof searchTermParam === 'string' ? searchTermParam : ''; 
            
            console.log("fetchEmployees: 開始獲取員工數據, 搜尋詞:", searchTerm);
            employeeTableBody.innerHTML = '<tr><td colspan="7" class="loading-row">載入中...</td></tr>';
            
            // 確保使用 JSP 的 getContextPath() 來動態獲取應用程式上下文路徑
            // 硬編碼的 '/PersonnelWebProject' 是不正確的，請替換為 JSP 語法
            let url = '<%=request.getContextPath()%>/employeeManagement?action=list';
            if (searchTerm.trim() !== '') { // 現在 searchTerm 已經確保是字串，可以安全使用 trim()
                url = '<%=request.getContextPath()%>/employeeManagement?action=search&searchTerm=' + encodeURIComponent(searchTerm.trim());
            }
            console.log("fetchEmployees: 請求 URL:", url);

            try {
                const response = await fetch(url);
                console.log("fetchEmployees: 收到響應，狀態碼:", response.status);

                if (!response.ok) {
                    const errorText = await response.text();
                    console.error('HTTP 錯誤! 狀態:', response.status, '響應文本:', errorText);
                    if (response.status === 403) { //
                         showMessage('權限不足，無法獲取員工列表。', 'error');
                         employeeTableBody.innerHTML = '<tr><td colspan="7" class="error-row">權限不足</td></tr>';
                         return;
                    }
                    throw new Error('HTTP 錯誤! 狀態: ' + response.status + ' - ' + errorText);
                }

                const rawResponseText = await response.text();
                console.log('從伺服器收到的原始響應文本:', rawResponseText);

                let employees;
                try {
                    employees = JSON.parse(rawResponseText);
                    console.log('JSON 解析成功，員工數據:', employees);
                } catch (jsonParseError) {
                    console.error('JSON 解析失敗:', jsonParseError);
                    showMessage('JSON 數據解析失敗，請檢查伺服器響應。', 'error');
                    employeeTableBody.innerHTML = '<tr><td colspan="7" class="error-row">數據格式錯誤</td></tr>';
                    return;
                }
                
                renderEmployees(employees);
            } catch (error) {
                console.error('獲取員工數據失敗:', error);
                showMessage('獲取員工數據失敗: ' + error.message, 'error');
                employeeTableBody.innerHTML = '<tr><td colspan="7" class="error-row">載入失敗</td></tr>';
            }
        }
        // --- 提交表單（新增或更新）---
        employeeForm.addEventListener('submit', async (event) => {
            event.preventDefault();

            const params = new URLSearchParams();
            
            // 添加表單字段
            params.append('employeeId', document.getElementById('employeeId').value);
            params.append('name', document.getElementById('name').value);
            params.append('department', document.getElementById('department').value);
            params.append('position', document.getElementById('position').value);
            params.append('hireDate', document.getElementById('hireDate').value);
            
            if (isEditMode) {
                params.append('action', 'update');
                const hiddenId = document.getElementById('employeeInternalId').value;
                if (hiddenId) {
                    params.append('id', hiddenId);
                }
            } else {
                params.append('action', 'add');
            }

            // 調試輸出
            console.log('=== URLSearchParams 調試信息 ===');
            for (let [key, value] of params.entries()) {
                console.log(key + ': ' + value);
            }
            console.log('isEditMode:', isEditMode);
            console.log('===========================');

            try {
                const response = await fetch('<%=request.getContextPath()%>/employeeManagement', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: params.toString()
                });

                console.log('響應狀態碼:', response.status);
                console.log('響應是否OK:', response.ok);

                if (!response.ok) {
                    const errorText = await response.text();
                    console.error('表單提交 HTTP 錯誤! 狀態:', response.status, '響應文本:', errorText);
                    throw new Error('操作失敗: ' + errorText);
                }

                const responseText = await response.text();
                console.log('原始響應文本:', `"${responseText}"`);
                console.log('響應文本長度:', responseText.length);
                console.log('trim後的響應:', `"${responseText.trim()}"`);

                // 檢查後端返回的 "success"
                if (responseText.trim() === 'success') { //
                    showMessage((isEditMode ? '更新' : '新增') + '員工成功！', 'success');
                    closeModal();
                    fetchEmployees();
                    console.log((isEditMode ? '更新' : '新增') + '員工成功！');
                } else {
                    showMessage('操作異常，響應: ' + responseText, 'error');
                    console.log((isEditMode ? '更新' : '新增') + '員工異常響應: ' + responseText);
                }
            } catch (error) {
                console.error((isEditMode ? '更新' : '新增') + '員工失敗:', error);
                showMessage((isEditMode ? '更新' : '新增') + '員工失敗: ' + error.message, 'error');
            }
        });

        // --- 刪除員工 ---
        async function deleteEmployee(id) {
            console.log("deleteEmployee: 嘗試刪除員工 ID:", id);
            try {
                const params = new URLSearchParams();
                params.append('action', 'delete');
                params.append('id', id);

                const response = await fetch('<%=request.getContextPath()%>/employeeManagement', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: params.toString()
                });
                
                console.log("deleteEmployee: 收到刪除響應，狀態碼:", response.status);
                console.log("deleteEmployee: 響應是否OK:", response.ok);

                if (!response.ok) {
                    const errorText = await response.text();
                    console.error('刪除 HTTP 錯誤! 狀態:', response.status, '響應文本:', errorText);
                    throw new Error('刪除失敗: ' + errorText);
                }

                const responseText = await response.text();
                console.log('刪除原始響應文本:', `"${responseText}"`);
                console.log('刪除響應文本長度:', responseText.length);
                console.log('刪除trim後的響應:', `"${responseText.trim()}"`);

                // 檢查後端返回的 "success"
                if (responseText.trim() === 'success') { //
                    showMessage('員工刪除成功！', 'success');
                    fetchEmployees();
                    console.log('員工刪除成功！');
                } else {
                    showMessage('刪除異常，響應: ' + responseText, 'error');
                    console.log('員工刪除異常響應: ' + responseText);
                }
            } catch (error) {
                console.error('刪除員工失敗:', error);
                showMessage('刪除員工失敗: ' + error.message, 'error');
            }
        }

        // --- 事件監聽器 ---
        addEmployeeButton.addEventListener('click', () => openModal(false));
        closeModalButton.addEventListener('click', closeModal);
        searchButton.addEventListener('click', () => fetchEmployees(searchTermInput.value));
        resetButton.addEventListener('click', () => {
            searchTermInput.value = '';
            fetchEmployees();
        });

        // 點擊模態框外部關閉
        employeeModal.addEventListener('click', (event) => {
            if (event.target === employeeModal) {
                closeModal();
            }
        });

        confirmModal.addEventListener('click', (event) => {
            if (event.target === confirmModal) {
                confirmModal.classList.remove('show');
            }
        });

        // Enter 鍵搜索
        searchTermInput.addEventListener('keypress', (event) => {
            if (event.key === 'Enter') {
                fetchEmployees(searchTermInput.value);
            }
        });

        // 初始載入員工列表
        window.onload = fetchEmployees;
    </script>
</body>
</html>