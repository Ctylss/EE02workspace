<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "編輯保養資料"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${pageTitle}</title>
<%-- 引入 Font Awesome 用於圖標 --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<%-- 可選：引入 Google Fonts 提升字體美觀 --%>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@300;400;500;700&display=swap" rel="stylesheet">
<script src="${pageContext.request.contextPath}/sidebar.js"></script>

<style>
    /* 基本樣式和字體設定 */
    body {
        font-family: 'Noto Sans TC', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background: linear-gradient(to right bottom, #f0f4f8, #d9e2ec); /* 柔和的淺藍灰漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
    }

    /* 表單容器 */
    .form-container {
        flex-grow: 1;
        max-width: 650px; /* 適中寬度 */
        margin: 3rem auto;
        padding: 2.5rem 3rem;
        background-color: #ffffff;
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    }

    .form-container h2 {
        font-size: 2.2rem;
        color: #2c3e50;
        margin-bottom: 1.5rem;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        padding-bottom: 1rem;
        border-bottom: 1px solid #eee;
    }

    /* 表單樣式 */
    form {
        display: grid;
        grid-template-columns: 1fr;
        gap: 1.5rem;
    }

    .form-group {
        margin-bottom: 0.5rem;
    }

    .form-group label {
        font-weight: 600;
        color: #495057;
        margin-bottom: 0.5rem;
        display: block;
        font-size: 1.05rem;
    }

    form input[type="text"],
    form input[type="number"],
    form textarea,
    form select {
        width: 100%;
        padding: 12px 15px;
        border: 1px solid #ced4da;
        border-radius: 8px;
        font-size: 1rem;
        color: #333;
        transition: all 0.3s ease-in-out;
        background-color: #fcfcfc;
        box-sizing: border-box;
        appearance: none; /* 移除 select 的預設樣式 */
    }

    form input[type="text"]:focus,
    form input[type="number"]:focus,
    form textarea:focus,
    form select:focus {
        border-color: #007bff;
        box-shadow: 0 0 0 4px rgba(0, 123, 255, 0.15);
        outline: none;
        background-color: #ffffff;
    }

    form textarea {
        resize: vertical;
        min-height: 100px;
    }

    /* select 箭頭圖標 */
    .select-wrapper {
        position: relative;
    }

    .select-wrapper::after {
        content: "\f078"; /* Font Awesome down arrow */
        font-family: "Font Awesome 6 Free";
        font-weight: 900;
        position: absolute;
        right: 15px;
        top: 50%;
        transform: translateY(-50%);
        color: #666;
        pointer-events: none; /* 讓點擊事件穿透到 select */
    }

    /* 按鈕區域 */
    .form-actions {
        display: flex;
        justify-content: center;
        gap: 1.5rem;
        margin-top: 2.5rem;
    }

    .form-actions button,
    .form-actions .btn {
        padding: 12px 28px;
        border-radius: 8px;
        font-size: 1.05rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        white-space: nowrap;
        border: none;
    }

    .form-actions button[type="submit"] {
        background-color: #007bff; /* 藍色更新按鈕 */
        color: white;
        box-shadow: 0 4px 10px rgba(0, 123, 255, 0.2);
    }

    .form-actions button[type="submit"]:hover {
        background-color: #0056b3;
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(0, 123, 255, 0.3);
    }

    .form-actions .btn-secondary {
        background-color: #6c757d; /* 灰色取消按鈕 */
        color: white;
        box-shadow: 0 4px 10px rgba(108, 117, 125, 0.2);
    }

    .form-actions .btn-secondary:hover {
        background-color: #5a6268;
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(108, 117, 125, 0.3);
    }

    /* 前端驗證錯誤文字 */
    .error-text {
        color: #dc3545;
        font-size: 0.875em;
        margin-top: 0.25rem;
        min-height: 1.25em; /* 預留空間避免佈局跳動 */
    }

    /* 後端錯誤訊息 */
    .backend-error-message {
        background-color: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
        padding: 1rem;
        margin-bottom: 1.5rem;
        border-radius: 8px;
        font-weight: 500;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    /* 響應式調整 */
    @media (max-width: 768px) {
        .form-container {
            margin: 1.5rem auto;
            padding: 1.5rem;
            border-radius: 8px;
        }
        .form-container h2 {
            font-size: 1.8rem;
            margin-bottom: 1rem;
        }
        form input[type="text"],
        form input[type="number"],
        form textarea,
        form select {
            padding: 10px 12px;
            font-size: 0.95rem;
        }
        .form-actions {
            flex-direction: column;
            gap: 1rem;
            margin-top: 1.5rem;
        }
        .form-actions button,
        .form-actions .btn {
            width: 100%;
            padding: 10px 20px;
        }
    }
</style>
</head>
<body>
    <%-- 這裡假設您的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>

    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="form-container">
        <h2><i class="fas fa-edit"></i> 編輯保養資料</h2>
        <p style="text-align: center; color: #555; margin-top: -1rem; margin-bottom: 2rem;">請更新以下保養資料，完成後點擊「更新」按鈕。</p>

        <%-- 後端錯誤訊息 --%>
        <c:if test="${not empty errorMessage}">
            <div class="backend-error-message">
                <i class="fas fa-exclamation-triangle"></i> <c:out value="${errorMessage}" />
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/UpdateMaintenanceServlet" method="post" id="updateMaintenanceForm">
            <input type="hidden" name="scheduleId" value="${maintenance.scheduleId}"/>
            
            <div class="form-group">
                <label for="machineId"><i class="fas fa-microchip"></i> 機台編號：</label>
                <input type="number" id="machineId" name="machineId" value="${maintenance.machineId}" required min="1" placeholder="請輸入機台的唯一編號">
                <div id="machineIdError" class="error-text"></div>
            </div>
            
            <div class="form-group">
                <label for="maintenanceDescription"><i class="fas fa-align-left"></i> 保養描述：</label>
                <textarea id="maintenanceDescription" name="maintenanceDescription" required placeholder="請簡要描述此次保養的內容或目的">${maintenance.maintenanceDescription}</textarea>
                <div id="maintenanceDescriptionError" class="error-text"></div>
            </div>
            
            <div class="form-group">
                <label for="maintenanceStatus"><i class="fas fa-tasks"></i> 保養狀態：</label>
                <div class="select-wrapper">
                    <select id="maintenanceStatus" name="maintenanceStatus" required>
                        <option value="待處理" ${maintenance.maintenanceStatus == '待處理' ? 'selected' : ''}>待處理</option>
                        <option value="進行中" ${maintenance.maintenanceStatus == '進行中' ? 'selected' : ''}>進行中</option>
                        <option value="已完成" ${maintenance.maintenanceStatus == '已完成' ? 'selected' : ''}>已完成</option>
                    </select>
                </div>
                <div id="maintenanceStatusError" class="error-text"></div>
            </div>
            
            <div class="form-group">
                <label for="employeeId"><i class="fas fa-user-tie"></i> 員工編號：</label>
                <input type="number" id="employeeId" name="employeeId" value="${maintenance.employeeId}" required min="1" placeholder="請輸入負責保養的員工編號">
                <div id="employeeIdError" class="error-text"></div>
            </div>
            
            <div class="form-actions">
                <button type="submit">
                    <i class="fas fa-save"></i> 更新
                </button>
                <a href="${pageContext.request.contextPath}/AdminMaintenanceServlet" class="btn btn-secondary">
                    <i class="fas fa-times-circle"></i> 取消
                </a>
            </div>
        </form>
    </div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('updateMaintenanceForm');
    const machineIdInput = document.getElementById('machineId');
    const maintenanceDescriptionTextarea = document.getElementById('maintenanceDescription');
    const employeeIdInput = document.getElementById('employeeId');

    const machineIdError = document.getElementById('machineIdError');
    const maintenanceDescriptionError = document.getElementById('maintenanceDescriptionError');
    const employeeIdError = document.getElementById('employeeIdError');

    function showError(element, message) {
        element.textContent = message;
    }

    function clearError(element) {
        element.textContent = '';
    }

    form.addEventListener('submit', function(e) {
        let isValid = true;

        // 清除所有錯誤訊息
        clearError(machineIdError);
        clearError(maintenanceDescriptionError);
        clearError(employeeIdError);

        // 驗證機台編號
        const machineId = machineIdInput.value.trim();
        if (!machineId) {
            showError(machineIdError, '請輸入機台編號。');
            isValid = false;
        } else {
            const parsedMachineId = parseInt(machineId);
            if (isNaN(parsedMachineId) || parsedMachineId <= 0) {
                showError(machineIdError, '機台編號必須是有效的正整數。');
                isValid = false;
            }
        }

        // 驗證保養描述
        const maintenanceDescription = maintenanceDescriptionTextarea.value.trim();
        if (!maintenanceDescription) {
            showError(maintenanceDescriptionError, '請輸入保養描述。');
            isValid = false;
        } else if (maintenanceDescription.length > 500) { // 假設描述最大長度為500
            showError(maintenanceDescriptionError, '保養描述不能超過500個字元。');
            isValid = false;
        }

        // 驗證員工編號
        const employeeId = employeeIdInput.value.trim();
        if (!employeeId) {
            showError(employeeIdError, '請輸入員工編號。');
            isValid = false;
        } else {
            const parsedEmployeeId = parseInt(employeeId);
            if (isNaN(parsedEmployeeId) || parsedEmployeeId <= 0) {
                showError(employeeIdError, '員工編號必須是有效的正整數。');
                isValid = false;
            }
        }

        if (!isValid) {
            e.preventDefault(); // 阻止表單提交
        }
    });

    // 可以在輸入時提供即時驗證回饋 (可選)
    machineIdInput.addEventListener('input', function() {
        clearError(machineIdError);
        const machineId = this.value.trim();
        if (machineId && (isNaN(parseInt(machineId)) || parseInt(machineId) <= 0)) {
            showError(machineIdError, '機台編號必須是有效的正整數。');
        }
    });

    maintenanceDescriptionTextarea.addEventListener('input', function() {
        clearError(maintenanceDescriptionError);
        const maintenanceDescription = this.value.trim();
        if (!maintenanceDescription) {
            showError(maintenanceDescriptionError, '請輸入保養描述。');
        } else if (maintenanceDescription.length > 500) {
            showError(maintenanceDescriptionError, '保養描述不能超過500個字元。');
        }
    });

    employeeIdInput.addEventListener('input', function() {
        clearError(employeeIdError);
        const employeeId = this.value.trim();
        if (employeeId && (isNaN(parseInt(employeeId)) || parseInt(employeeId) <= 0)) {
            showError(employeeIdError, '員工編號必須是有效的正整數。');
        }
    });
});
</script>

</body>
</html>