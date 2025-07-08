<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>員工管理系統</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
        body {
            font-family: 'Inter', sans-serif;
            background-color: #f3f4f6; /* Light gray background */
        }
        /* Custom scrollbar for better aesthetics */
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
    <!-- Font Awesome CDN for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body class="p-4 bg-gray-100 min-h-screen flex items-center justify-center">
    <div class="container mx-auto p-6 bg-white shadow-lg rounded-xl max-w-6xl w-full">
        <h1 class="text-4xl font-bold text-gray-800 mb-6 text-center">員工管理</h1>

        <!-- User Info and Logout -->
        <div class="flex justify-between items-center mb-6 p-4 bg-blue-100 rounded-lg shadow-sm">
            <p class="text-blue-800 text-lg">
                您好，<span id="loggedInUser" class="font-semibold">載入中...</span> (<span id="userRole" class="font-semibold">載入中...</span>)
            </p>
            <a href="<%= request.getContextPath() %>/logout" class="text-blue-700 hover:text-blue-900 font-medium">登出</a>
        </div>

        <!-- Search and Add Section -->
        <div class="mb-6 flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0 md:space-x-4">
            <div class="flex w-full md:w-2/3 space-x-2">
                <input type="text" id="searchTerm" placeholder="搜尋員工編號/姓名/部門/職位..."
                       class="flex-1 p-3 border border-gray-300 rounded-lg focus:ring-blue-500 focus:border-blue-500 shadow-sm">
                <button id="searchButton"
                        class="px-5 py-3 bg-blue-600 text-white font-semibold rounded-lg shadow hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition ease-in-out duration-150">
                    <i class="fas fa-search mr-2"></i>搜尋
                </button>
                <button id="resetSearchButton"
                        class="px-5 py-3 bg-gray-400 text-white font-semibold rounded-lg shadow hover:bg-gray-500 focus:outline-none focus:ring-2 focus:ring-gray-300 focus:ring-offset-2 transition ease-in-out duration-150">
                    <i class="fas fa-redo mr-2"></i>重置
                </button>
            </div>
            <button id="addEmployeeButton"
                    class="w-full md:w-auto px-6 py-3 bg-green-600 text-white font-semibold rounded-lg shadow hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2 transition ease-in-out duration-150">
                <i class="fas fa-plus-circle mr-2"></i>新增員工
            </button>
        </div>

        <!-- Employee List Table -->
        <div class="overflow-x-auto relative shadow-md sm:rounded-lg">
            <table class="w-full text-sm text-left text-gray-500">
                <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                    <tr>
                        <th scope="col" class="py-3 px-6 rounded-tl-lg">ID</th>
                        <th scope="col" class="py-3 px-6">員工編號</th>
                        <th scope="col" class="py-3 px-6">姓名</th>
                        <th scope="col" class="py-3 px-6">部門</th>
                        <th scope="col" class="py-3 px-6">職位</th>
                        <th scope="col" class="py-3 px-6 rounded-tr-lg">入職日期</th>
                        <th scope="col" class="py-3 px-6 rounded-tr-lg">操作</th>
                    </tr>
                </thead>
                <tbody id="employeeTableBody">
                    <!-- Employee data will be loaded here by JavaScript -->
                    <tr class="bg-white border-b">
                        <td colspan="7" class="py-4 px-6 text-center text-gray-400">載入員工數據...</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Add/Edit Employee Modal -->
        <div id="employeeModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 hidden flex items-center justify-center p-4">
            <div class="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
                <h2 id="modalTitle" class="text-2xl font-bold text-gray-800 mb-4 text-center">新增員工</h2>
                <form id="employeeForm" class="space-y-4">
                    <div>
                        <label for="employeeId" class="block text-sm font-medium text-gray-700">員工編號:</label>
                        <input type="text" id="employeeId" name="employeeId" required
                               class="mt-1 block w-full p-3 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500">
                    </div>
                    <div>
                        <label for="name" class="block text-sm font-medium text-gray-700">姓名:</label>
                        <input type="text" id="name" name="name" required
                               class="mt-1 block w-full p-3 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500">
                    </div>
                    <div>
                        <label for="department" class="block text-sm font-medium text-gray-700">部門:</label>
                        <input type="text" id="department" name="department" required
                               class="mt-1 block w-full p-3 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500">
                    </div>
                    <div>
                        <label for="position" class="block text-sm font-medium text-gray-700">職位:</label>
                        <input type="text" id="position" name="position" required
                               class="mt-1 block w-full p-3 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500">
                    </div>
                    <div>
                        <label for="hireDate" class="block text-sm font-medium text-gray-700">入職日期:</label>
                        <input type="date" id="hireDate" name="hireDate" required
                               class="mt-1 block w-full p-3 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500">
                    </div>
                    <input type="hidden" id="employeeInternalId" name="id"> <!-- Hidden field for internal ID -->

                    <div class="flex justify-end space-x-3 mt-6">
                        <button type="button" id="closeModalButton"
                                class="px-5 py-2 bg-gray-300 text-gray-800 font-semibold rounded-lg shadow hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-300 focus:ring-offset-2 transition ease-in-out duration-150">
                            取消
                        </button>
                        <button type="submit" id="submitEmployeeButton"
                                class="px-5 py-2 bg-blue-600 text-white font-semibold rounded-lg shadow hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition ease-in-out duration-150">
                            提交
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Message Box -->
        <div id="messageBox" class="fixed bottom-4 right-4 bg-blue-500 text-white px-6 py-3 rounded-lg shadow-lg hidden">
            這是一個訊息！
        </div>

        <!-- Custom Confirm Modal for Delete -->
        <div id="confirmModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 hidden flex items-center justify-center p-4">
            <div class="bg-white rounded-lg shadow-xl p-6 w-full max-w-sm text-center">
                <h3 class="text-xl font-bold text-gray-800 mb-4">確認操作</h3>
                <p id="confirmMessage" class="text-gray-700 mb-6">您確定要刪除這名員工嗎？</p>
                <div class="flex justify-center space-x-4">
                    <button id="confirmCancelButton"
                            class="px-5 py-2 bg-gray-300 text-gray-800 font-semibold rounded-lg shadow hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-300 focus:ring-offset-2 transition ease-in-out duration-150">
                        取消
                    </button>
                    <button id="confirmDeleteButton"
                            class="px-5 py-2 bg-red-600 text-white font-semibold rounded-lg shadow hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 transition ease-in-out duration-150">
                        刪除
                    </button>
                </div>
            </div>
        </div>

    </div>

    <script>
        // 從 JSP Session 獲取使用者名稱和角色
        const loggedInUser = "<%= session.getAttribute("loggedInUser") != null ? session.getAttribute("loggedInUser") : "Guest" %>";
        const userRole = "<%= session.getAttribute("userRole") != null ? session.getAttribute("userRole") : "guest" %>";

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
        let currentDeleteId = null; // 用於儲存要刪除的員工 ID

        let isEditMode = false; // Flag to determine if modal is for edit or add

        // 檢查使用者角色以啟用/禁用功能
        const isAdminOrPersonnel = (userRole === 'admin' || userRole === 'personnel');
        if (!isAdminOrPersonnel) {
            addEmployeeButton.classList.add('hidden'); // 隱藏新增按鈕
            // 編輯和刪除按鈕會在 renderEmployees 中處理隱藏
        }

        // --- 訊息提示框功能 ---
        function showMessage(message, type = 'info') {
            messageBox.textContent = message;
            messageBox.classList.remove('hidden', 'bg-blue-500', 'bg-green-500', 'bg-red-500');
            if (type === 'success') {
                messageBox.classList.add('bg-green-500');
            } else if (type === 'error') {
                messageBox.classList.add('bg-red-500');
            } else { // info
                messageBox.classList.add('bg-blue-500');
            }
            setTimeout(() => {
                messageBox.classList.add('hidden');
            }, 3000); // 3 秒後自動隱藏
        }

        // --- 清空表單 ---
        function clearForm() {
            employeeForm.reset();
            document.getElementById('employeeInternalId').value = '';
            document.getElementById('employeeId').readOnly = false; // 新增時員工編號可編輯
        }

        // --- 開啟/關閉 Modal ---
        function openModal(isEdit = false, employeeData = null) {
            isEditMode = isEdit;
            clearForm(); // 先清空表單
            if (isEdit) {
                modalTitle.textContent = '編輯員工';
                submitEmployeeButton.textContent = '更新';
                // 填充表單數據
                document.getElementById('employeeInternalId').value = employeeData.id;
                document.getElementById('employeeId').value = employeeData.employeeId;
                document.getElementById('name').value = employeeData.name;
                document.getElementById('department').value = employeeData.department;
                document.getElementById('position').value = employeeData.position;
                document.getElementById('hireDate').value = employeeData.hireDate; // 日期格式應為YYYY-MM-DD
                document.getElementById('employeeId').readOnly = true; // 編輯時員工編號不可編輯
            } else {
                modalTitle.textContent = '新增員工';
                submitEmployeeButton.textContent = '提交';
            }
            employeeModal.classList.remove('hidden');
        }

        function closeModal() {
            employeeModal.classList.add('hidden');
        }

        // --- 開啟/關閉 Confirm Modal ---
        function showConfirmModal(message, onConfirm) {
            confirmMessage.textContent = message;
            confirmModal.classList.remove('hidden');

            const handleConfirm = () => {
                onConfirm();
                confirmModal.classList.add('hidden');
                confirmDeleteButton.removeEventListener('click', handleConfirm);
                confirmCancelButton.removeEventListener('click', handleCancel);
            };

            const handleCancel = () => {
                confirmModal.classList.add('hidden');
                confirmDeleteButton.removeEventListener('click', handleConfirm);
                confirmCancelButton.removeEventListener('click', handleCancel);
            };

            confirmDeleteButton.addEventListener('click', handleConfirm);
            confirmCancelButton.addEventListener('click', handleCancel);
        }

        // --- 載入員工列表 ---
        async function fetchEmployees(searchTerm = '') {
            console.log("fetchEmployees: 開始獲取員工數據, 搜尋詞:", searchTerm);
            employeeTableBody.innerHTML = '<tr><td colspan="7" class="py-4 px-6 text-center text-gray-400">載入中...</td></tr>';
            let url = '<%= request.getContextPath() %>/employeeManagement?action=list';
            if (searchTerm) {
                url = '<%= request.getContextPath() %>/employeeManagement?action=search&searchTerm=' + encodeURIComponent(searchTerm);
            }
            console.log("fetchEmployees: 請求 URL:", url);

            try {
                const response = await fetch(url);
                console.log("fetchEmployees: 收到響應，狀態碼:", response.status);

                // 檢查響應是否成功
                if (!response.ok) {
                    const errorText = await response.text(); // 嘗試獲取錯誤響應文本
                    console.error('HTTP 錯誤! 狀態:', response.status, '響應文本:', errorText);
                    if (response.status === 403) {
                         showMessage('權限不足，無法獲取員工列表。', 'error');
                         employeeTableBody.innerHTML = '<tr><td colspan="7" class="py-4 px-6 text-center text-red-400">權限不足</td></tr>';
                         return;
                    }
                    throw new Error('HTTP 錯誤! 狀態: ' + response.status + ' - ' + errorText);
                }

                const rawResponseText = await response.text(); // 先獲取原始響應文本
                console.log('從伺服器收到的原始響應文本:', rawResponseText); // 除錯用：打印原始響應

                let employees;
                try {
                    employees = JSON.parse(rawResponseText); // 嘗試手動解析 JSON
                    console.log('JSON 解析成功，員工數據:', employees);
                } catch (jsonParseError) {
                    console.error('JSON 解析失敗:', jsonParseError);
                    showMessage('JSON 數據解析失敗，請檢查伺服器響應。', 'error');
                    employeeTableBody.innerHTML = '<tr><td colspan="7" class="py-4 px-6 text-center text-red-400">數據格式錯誤</td></tr>';
                    return; // 阻止繼續執行
                }
                
                renderEmployees(employees);
            } catch (error) {
                console.error('獲取員工數據失敗:', error);
                showMessage('獲取員工數據失敗: ' + error.message, 'error');
                employeeTableBody.innerHTML = '<tr><td colspan="7" class="py-4 px-6 text-center text-red-400">載入失敗</td></tr>';
            }
        }

        // --- 渲染員工列表 ---
        function renderEmployees(employees) {
            console.log("renderEmployees: 開始渲染員工數據.");
            console.log("renderEmployees: employeeTableBody 元素:", employeeTableBody);
            
            // 確保 employeeTableBody 確實是一個 DOM 元素
            if (!employeeTableBody) {
                console.error("renderEmployees: 找不到 'employeeTableBody' 元素。請檢查 HTML ID。");
                showMessage("內部錯誤：表格元素未找到。", "error");
                return;
            }

            employeeTableBody.innerHTML = ''; // 清空現有列表
            
            if (!Array.isArray(employees)) {
                console.error("renderEmployees: 期望的 employees 數據不是一個陣列。", employees);
                showMessage("數據格式錯誤：期望的員工列表不是陣列。", "error");
                employeeTableBody.innerHTML = '<tr><td colspan="7" class="py-4 px-6 text-center text-red-400">數據格式錯誤，無法渲染。</td></tr>';
                return;
            }

            if (employees.length === 0) {
                employeeTableBody.innerHTML = '<tr><td colspan="7" class="py-4 px-6 text-center text-gray-400">沒有找到員工數據。</td></tr>';
                console.log("renderEmployees: 沒有員工數據，顯示空數據訊息。");
                return;
            }

            employees.forEach((employee, index) => {
                console.log(`renderEmployees: 處理第 ${index + 1} 個員工數據:`, employee);
                // 確保所有預期的屬性都存在，並提供預設值以防止 undefined
                // 由於後端已經使用 Jackson 輸出標準 JSON，理論上屬性不會缺失，但加上預防性檢查更好
                const id = employee.id; // ID 應該總是存在
                const employeeId = employee.employeeId || '';
                const name = employee.name || '';
                const department = employee.department || '';
                const position = employee.position || '';
                const hireDate = employee.hireDate || ''; 

                // 檢查是否所有關鍵屬性都為空，這可能是問題所在
                if (!id && !employeeId && !name && !department && !position && !hireDate) {
                    console.warn(`renderEmployees: 第 ${index + 1} 個員工物件的所有關鍵屬性皆為空/undefined，跳過渲染。`, employee);
                    return; // 跳過此空行
                }

                const row = document.createElement('tr');
                row.className = 'bg-white border-b hover:bg-gray-50';

                // 手動創建 TD 元素並設置文本內容，避免模板字串問題
                const tdId = document.createElement('td');
                tdId.className = 'py-4 px-6 font-medium text-gray-900';
                tdId.textContent = id;
                row.appendChild(tdId);

                const tdEmployeeId = document.createElement('td');
                tdEmployeeId.className = 'py-4 px-6';
                tdEmployeeId.textContent = employeeId;
                row.appendChild(tdEmployeeId);

                const tdName = document.createElement('td');
                tdName.className = 'py-4 px-6';
                tdName.textContent = name;
                row.appendChild(tdName);

                const tdDepartment = document.createElement('td');
                tdDepartment.className = 'py-4 px-6';
                tdDepartment.textContent = department;
                row.appendChild(tdDepartment);

                const tdPosition = document.createElement('td');
                tdPosition.className = 'py-4 px-6';
                tdPosition.textContent = position;
                row.appendChild(tdPosition);

                const tdHireDate = document.createElement('td');
                tdHireDate.className = 'py-4 px-6';
                tdHireDate.textContent = hireDate;
                row.appendChild(tdHireDate);

                // 操作按鈕的 TD
                const tdActions = document.createElement('td');
                tdActions.className = 'py-4 px-6 flex space-x-2';

                const editButton = document.createElement('button');
                editButton.className = 'edit-button px-3 py-1 bg-yellow-500 text-white rounded-md shadow hover:bg-yellow-600 transition-colors duration-150';
                editButton.dataset.id = id;
                editButton.dataset.employee = JSON.stringify(employee); // 編輯按鈕仍需完整的員工數據
                editButton.innerHTML = '<i class="fas fa-edit"></i>';
                tdActions.appendChild(editButton);

                const deleteButton = document.createElement('button');
                deleteButton.className = 'delete-button px-3 py-1 bg-red-500 text-white rounded-md shadow hover:bg-red-600 transition-colors duration-150';
                deleteButton.dataset.id = id;
                deleteButton.innerHTML = '<i class="fas fa-trash-alt"></i>';
                tdActions.appendChild(deleteButton);
                
                row.appendChild(tdActions); // 將操作按鈕的 TD 添加到行

                employeeTableBody.appendChild(row);
                console.log(`renderEmployees: 已添加員工 ${employeeId} 的行。行內容 (手動創建):`, row.outerHTML); // 打印實際的行 HTML
            });

            // 為每個編輯和刪除按鈕添加事件監聽器
            document.querySelectorAll('.edit-button').forEach(button => {
                if (isAdminOrPersonnel) { // 只有管理員和人事部能編輯
                    button.addEventListener('click', (event) => {
                        const employeeData = JSON.parse(event.currentTarget.dataset.employee);
                        openModal(true, employeeData);
                    });
                } else {
                    button.classList.add('hidden'); // 隱藏編輯按鈕
                }
            });

            document.querySelectorAll('.delete-button').forEach(button => {
                if (isAdminOrPersonnel) { // 只有管理員和人事部能刪除
                    button.addEventListener('click', (event) => {
                        currentDeleteId = event.currentTarget.dataset.id; // 儲存要刪除的 ID
                        showConfirmModal('確定要刪除這名員工嗎？此操作不可逆轉。', () => {
                            deleteEmployee(currentDeleteId); // 在確認後調用刪除函數
                        });
                    });
                } else {
                    button.classList.add('hidden'); // 隱藏刪除按鈕
                }
            });
            console.log("renderEmployees: 員工數據渲染完成，事件監聽器已附加。");
        }

        // --- 提交表單（新增或更新）---
        employeeForm.addEventListener('submit', async (event) => {
            event.preventDefault(); // 阻止表單預設提交行為

            const formData = new FormData(employeeForm);
            const employeeData = Object.fromEntries(formData.entries());

            let url = '<%= request.getContextPath() %>/employeeManagement';
            let actionType = '';
            let actionText = ''; // 用於顯示訊息的中文文字

            if (isEditMode) {
                actionType = 'update';
                actionText = '更新';
                url += '?action=update';
            } else {
                actionType = 'add';
                actionText = '新增';
                url += '?action=add';
            }
            console.log(`提交表單: 執行動作: ${actionType}, 數據:`, employeeData);

            try {
                const response = await fetch(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded' // 表單數據通常用這種 Content-Type
                    },
                    body: new URLSearchParams(employeeData).toString() // 將物件轉換為 URL 編碼的字串
                });

                if (!response.ok) {
                    const errorText = await response.text(); // 獲取後端錯誤訊息
                    console.error('表單提交 HTTP 錯誤! 狀態:', response.status, '響應文本:', errorText);
                    throw new Error('操作失敗: ' + errorText);
                }

                showMessage(actionText + '員工成功！', 'success');
                closeModal();
                fetchEmployees(); // 重新載入列表
                console.log(`${actionText}員工成功！`);
            } catch (error) {
                console.error(actionText + '員工失敗:', error);
                showMessage(actionText + '員工失敗: ' + error.message, 'error');
            }
        });

        // --- 刪除員工 ---
        async function deleteEmployee(id) {
            console.log("deleteEmployee: 嘗試刪除員工 ID:", id);
            try {
                const response = await fetch('<%= request.getContextPath() %>/employeeManagement?action=delete&id=' + id, {
                    method: 'POST' // 刪除操作也可以用 POST
                });
                console.log("deleteEmployee: 收到刪除響應，狀態碼:", response.status);

                if (!response.ok) {
                    const errorText = await response.text();
                    console.error('刪除 HTTP 錯誤! 狀態:', response.status, '響應文本:', errorText);
                    throw new Error('刪除失敗: ' + errorText);
                }

                showMessage('員工刪除成功！', 'success');
                fetchEmployees(); // 重新載入列表
                console.log('員工刪除成功！');
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

        // 初始載入員工列表
        window.onload = fetchEmployees;

    </script>
</body>
</html>
