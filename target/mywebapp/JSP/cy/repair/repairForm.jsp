<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "報修申請"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
<meta charset="UTF-8" />
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
        background: linear-gradient(to right bottom, #e0f2f7, #c6e0eb); /* 柔和的淺藍漸變背景 */
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
        max-width: 600px; /* 適中寬度 */
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

    .input-icon-wrapper {
        position: relative;
    }

    .input-icon-wrapper i {
        position: absolute;
        left: 15px;
        top: 50%;
        transform: translateY(-50%);
        color: #999;
        pointer-events: none; /* 讓圖標不阻礙點擊輸入框 */
    }

    form input[type="text"],
    form textarea {
        width: 100%;
        padding: 12px 15px 12px 45px; /* 增加左邊距給圖標 */
        border: 1px solid #ced4da;
        border-radius: 8px;
        font-size: 1rem;
        color: #333;
        transition: all 0.3s ease-in-out;
        background-color: #fcfcfc;
        box-sizing: border-box;
    }

    form textarea {
        padding-left: 15px; /* textarea 不需要圖標的左邊距 */
        min-height: 120px; /* 增加 textarea 高度 */
        resize: vertical;
    }

    form input[type="text"]:focus,
    form textarea:focus {
        border-color: #007bff;
        box-shadow: 0 0 0 4px rgba(0, 123, 255, 0.15);
        outline: none;
        background-color: #ffffff;
    }

    /* 錯誤訊息 */
    .error-message {
        background-color: #f8d7da; /* 淺紅色背景 */
        color: #721c24; /* 深紅色文字 */
        border: 1px solid #f5c6cb;
        padding: 1rem 1.25rem;
        margin-bottom: 1.5rem;
        border-radius: 8px;
        font-weight: 500;
        display: flex;
        align-items: center;
        gap: 10px;
        opacity: 0; /* 預設隱藏 */
        max-height: 0;
        overflow: hidden;
        transition: all 0.4s ease-out;
    }

    .error-message.show {
        opacity: 1;
        max-height: 100px; /* 足夠顯示內容的高度 */
        margin-bottom: 1.5rem;
    }

    .error-message i {
        font-size: 1.2em;
    }

    /* 成功訊息 */
    .success-message {
        background-color: #d4edda; /* 淺綠色背景 */
        color: #155724; /* 深綠色文字 */
        border: 1px solid #c3e6cb;
        padding: 1rem 1.25rem;
        margin-bottom: 1.5rem;
        border-radius: 8px;
        font-weight: 500;
        display: flex;
        align-items: center;
        gap: 10px;
        opacity: 0; /* 預設隱藏 */
        max-height: 0;
        overflow: hidden;
        transition: all 0.4s ease-out;
    }

    .success-message.show {
        opacity: 1;
        max-height: 100px; /* 足夠顯示內容的高度 */
        margin-bottom: 1.5rem;
    }

    .success-message i {
        font-size: 1.2em;
    }

    /* 前端驗證的即時錯誤提示 */
    .input-error-text {
        color: #dc3545;
        font-size: 0.875em;
        margin-top: 0.5rem;
        min-height: 1.25em; /* 預留空間避免佈局跳動 */
        display: block; /* 確保佔用一行 */
    }

    /* 提交按鈕 */
    input[type="submit"] {
        padding: 15px 30px;
        background-color: #28a745; /* 綠色提交按鈕 */
        color: white;
        border: none;
        border-radius: 8px;
        font-size: 1.1rem;
        font-weight: 600;
        cursor: pointer;
        transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
        margin-top: 1rem;
        box-shadow: 0 4px 12px rgba(40, 167, 69, 0.25);
    }

    input[type="submit"]:hover {
        background-color: #218838;
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(40, 167, 69, 0.35);
    }

    /* 響應式調整 */
    @media (max-width: 768px) {
        .form-container {
            margin: 2rem auto;
            padding: 2rem;
            border-radius: 10px;
        }
        .form-container h2 {
            font-size: 2rem;
            gap: 10px;
            margin-bottom: 1rem;
        }
        form input[type="text"],
        form textarea {
            padding: 10px 12px 10px 40px;
            font-size: 0.95rem;
        }
        form textarea {
            padding-left: 12px; /* textarea 不需要圖標的左邊距 */
        }
        .input-icon-wrapper i {
            left: 12px;
        }
        input[type="submit"] {
            padding: 12px 25px;
            font-size: 1rem;
            width: 100%;
            box-sizing: border-box;
        }
    }

    @media (max-width: 480px) {
        .form-container {
            padding: 1.5rem;
        }
        .form-container h2 {
            font-size: 1.8rem;
        }
        .error-message, .success-message {
            padding: 0.8rem 1rem;
            font-size: 0.9rem;
        }
    }
</style>
</head>
<body>

<div class="form-container">
    <h2><i class="fas fa-tools"></i> 機台報修申請</h2>
    <p style="text-align: center; color: #555; margin-top: -1rem; margin-bottom: 2rem;">
        請填寫以下資訊以提交機台維修申請。
    </p>

    <c:if test="${not empty errorMessage}">
        <div id="backendErrorMessage" class="error-message show">
            <i class="fas fa-times-circle"></i> <c:out value="${errorMessage}" />
        </div>
    </c:if>
    
    <c:if test="${not empty successMessage}">
        <div id="backendSuccessMessage" class="success-message show">
            <i class="fas fa-check-circle"></i> <c:out value="${successMessage}" />
        </div>
    </c:if>
    
    <form action="${pageContext.request.contextPath}/InsertRepairServlet" method="post" onsubmit="return validateForm()">
        <div class="form-group">
            <label for="employeeId"><i class="fas fa-user-tie"></i> 員工編號：</label>
            <div class="input-icon-wrapper">
                <input type="text" 
                       name="employeeId" 
                       id="employeeId" 
                       placeholder="請輸入您的員工編號"
                       value="<c:out value='${param.employeeId}' />" />
                <i class="fas fa-id-badge"></i>
            </div>
            <div id="employeeIdError" class="input-error-text"></div>
        </div>

        <div class="form-group">
            <label for="machineId"><i class="fas fa-microchip"></i> 機台編號：</label>
            <div class="input-icon-wrapper">
                <input type="text" 
                       name="machineId" 
                       id="machineId" 
                       placeholder="請輸入需要維修的機台編號"
                       value="<c:out value='${param.machineId}' />" />
                <i class="fas fa-barcode"></i>
            </div>
            <div id="machineIdError" class="input-error-text"></div>
        </div>

        <div class="form-group">
            <label for="description"><i class="fas fa-file-alt"></i> 維修描述：</label>
            <textarea id="description" 
                      name="repairDescription" 
                      placeholder="請詳細描述故障或異常狀況，例如：&#10;1. 機台無法啟動&#10;2. 產生異常噪音&#10;3. 顯示錯誤代碼 E01" 
            ><c:out value="${param.repairDescription}" /></textarea>
            <div id="descriptionError" class="input-error-text"></div>
        </div>

        <input type="submit" value="送出申請" />
    </form>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const employeeIdInput = document.getElementById('employeeId');
    const machineIdInput = document.getElementById('machineId');
    const descriptionTextarea = document.getElementById('description');

    const employeeIdError = document.getElementById('employeeIdError');
    const machineIdError = document.getElementById('machineIdError');
    const descriptionError = document.getElementById('descriptionError');

    // 隱藏後端訊息的函數
    function hideBackendMessages() {
        const backendError = document.getElementById('backendErrorMessage');
        const backendSuccess = document.getElementById('backendSuccessMessage');
        if (backendError) {
            backendError.classList.remove('show');
            setTimeout(() => backendError.style.display = 'none', 400); // 讓動畫完成後再隱藏
        }
        if (backendSuccess) {
            backendSuccess.classList.remove('show');
            setTimeout(() => backendSuccess.style.display = 'none', 400); // 讓動畫完成後再隱藏
        }
    }

    // 頁面載入後自動隱藏後端訊息
    // 這裡設置一個延時，讓使用者有時間看到訊息
    if (document.getElementById('backendErrorMessage') || document.getElementById('backendSuccessMessage')) {
        setTimeout(hideBackendMessages, 5000); // 5秒後自動隱藏
    }

    function showError(element, message) {
        element.textContent = message;
        element.style.display = 'block';
    }

    function clearError(element) {
        element.textContent = '';
        element.style.display = 'none';
    }

    // 前端驗證邏輯
    window.validateForm = function() {
        let isValid = true;

        // 清除所有錯誤訊息
        clearError(employeeIdError);
        clearError(machineIdError);
        clearError(descriptionError);

        // 驗證員工編號
        const employeeId = employeeIdInput.value.trim();
        if (!employeeId) {
            showError(employeeIdError, '員工編號不可空白。');
            isValid = false;
        } else if (!/^\d+$/.test(employeeId) || parseInt(employeeId) <= 0) {
            showError(employeeIdError, '員工編號必須是有效的正整數。');
            isValid = false;
        }

        // 驗證機台編號
        const machineId = machineIdInput.value.trim();
        if (!machineId) {
            showError(machineIdError, '機台編號不可空白。');
            isValid = false;
        } else if (!/^\d+$/.test(machineId) || parseInt(machineId) <= 0) {
            showError(machineIdError, '機台編號必須是有效的正整數。');
            isValid = false;
        }

        // 驗證維修描述
        const description = descriptionTextarea.value.trim();
        if (!description) {
            showError(descriptionError, '維修描述不可空白。');
            isValid = false;
        } else if (description.length > 500) { // 假設描述最多500個字
            showError(descriptionError, '維修描述不能超過500個字元。');
            isValid = false;
        }

        return isValid;
    };

    // 添加即時輸入驗證 (可選)
    employeeIdInput.addEventListener('input', function() {
        clearError(employeeIdError);
        const employeeId = this.value.trim();
        if (employeeId && (!/^\d+$/.test(employeeId) || parseInt(employeeId) <= 0)) {
            showError(employeeIdError, '員工編號必須是有效的正整數。');
        }
    });

    machineIdInput.addEventListener('input', function() {
        clearError(machineIdError);
        const machineId = this.value.trim();
        if (machineId && (!/^\d+$/.test(machineId) || parseInt(machineId) <= 0)) {
            showError(machineIdError, '機台編號必須是有效的正整數。');
        }
    });

    descriptionTextarea.addEventListener('input', function() {
        clearError(descriptionError);
        const description = this.value.trim();
        if (description.length > 500) {
            showError(descriptionError, '維修描述不能超過500個字元。');
        }
    });
});
</script>
</body>
</html>