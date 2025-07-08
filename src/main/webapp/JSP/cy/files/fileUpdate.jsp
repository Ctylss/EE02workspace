<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "編輯機台檔案"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

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
        background: linear-gradient(to right bottom, #f0f4f8, #d9e2ec); /* 柔和的漸變背景 */
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
        max-width: 750px; /* 調整容器寬度以適應表單和說明 */
        margin: 3rem auto; /* 增加上下外邊距 */
        padding: 2.5rem 3rem; /* 增加內邊距 */
        background-color: #ffffff;
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    }

    .form-container h2 {
        font-size: 2.2rem;
        color: #2c3e50;
        margin-bottom: 0.5rem;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
    }

    .form-container > p { /* 直接子元素p */
        font-size: 1.1rem;
        color: #555;
        margin-top: 0;
        margin-bottom: 2rem;
        text-align: center;
    }

    /* 頂部操作連結 */
    .top-actions {
        display: flex;
        justify-content: center; /* 居中對齊 */
        gap: 1.5rem;
        margin-bottom: 2rem;
        padding-bottom: 1rem;
        border-bottom: 1px solid #eee;
    }

    .top-actions a {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        text-decoration: none;
        color: #007bff;
        font-weight: 500;
        font-size: 1.05rem;
        transition: color 0.3s ease;
    }

    .top-actions a:hover {
        color: #0056b3;
        text-decoration: underline;
    }

    .top-actions .delete-link {
        color: #dc3545; /* 刪除連結使用紅色 */
    }

    .top-actions .delete-link:hover {
        color: #c82333;
    }

    /* 原始檔案資訊區塊 */
    .info-card {
        background-color: #eaf6ff;
        border: 1px solid #a6e3f7;
        padding: 1.5rem;
        border-radius: 10px;
        margin-bottom: 2.5rem;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
    }

    .info-card h3 {
        font-size: 1.5rem;
        color: #007bff;
        margin-top: 0;
        margin-bottom: 1rem;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .info-card ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .info-card li {
        margin-bottom: 0.8rem;
        font-size: 1rem;
        color: #444;
        display: flex;
        align-items: center;
        gap: 8px; /* 圖標和文字的間距 */
    }

    .info-card li .label {
        font-weight: 600;
        color: #333;
    }
    
    .info-card li code {
        background-color: #f0f0f0;
        padding: 3px 6px;
        border-radius: 4px;
        font-family: 'Consolas', 'Monaco', monospace;
        font-size: 0.95em;
        word-break: break-all; /* 長路徑自動換行 */
    }

    /* 後端錯誤訊息 */
    .error-message {
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
    form input[type="number"] {
        width: 100%;
        padding: 12px 15px;
        border: 1px solid #ced4da;
        border-radius: 8px;
        font-size: 1rem;
        color: #333;
        transition: all 0.3s ease-in-out;
        background-color: #fcfcfc;
        box-sizing: border-box;
    }

    form input[type="text"]:focus,
    form input[type="number"]:focus {
        border-color: #007bff;
        box-shadow: 0 0 0 4px rgba(0, 123, 255, 0.15);
        outline: none;
        background-color: #ffffff;
    }

    /* Checkbox */
    .checkbox-group {
        display: flex;
        align-items: center;
        gap: 10px;
        margin-top: 1rem;
        margin-bottom: 1rem;
        font-size: 1.05rem;
        color: #495057;
        cursor: pointer;
    }
    .checkbox-group input[type="checkbox"] {
        width: 20px;
        height: 20px;
        accent-color: #007bff; /* 設置勾選框顏色 */
        cursor: pointer;
    }

    /* 路徑說明區塊 */
    .path-info-box {
        background-color: #fff3cd; /* 警告黃背景 */
        border: 1px solid #ffeeba;
        padding: 1.5rem;
        border-radius: 10px;
        margin-top: 2rem;
        margin-bottom: 2.5rem;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }

    .path-info-box h4 {
        font-size: 1.2rem;
        color: #856404; /* 深黃色文字 */
        margin-top: 0;
        margin-bottom: 1rem;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .path-info-box ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .path-info-box li {
        color: #6a5e4d;
        margin-bottom: 0.5rem;
        font-size: 0.95rem;
        display: flex;
        align-items: flex-start;
        gap: 5px;
    }
    
    .path-info-box li::before {
        content: "•";
        color: #ffc107; /* 黃色點 */
        font-weight: bold;
        display: inline-block;
        width: 1em;
        margin-left: -1em;
    }

    /* 表單動作按鈕 */
    .form-actions {
        display: flex;
        justify-content: flex-end; /* 靠右對齊 */
        gap: 1rem;
        margin-top: 2.5rem;
    }

    .form-actions button,
    .form-actions .btn {
        padding: 12px 25px;
        border-radius: 8px;
        font-size: 1rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        white-space: nowrap;
    }

    .form-actions button[type="submit"] {
        background-color: #28a745; /* 綠色 */
        color: white;
        border: 1px solid #28a745;
    }

    .form-actions button[type="submit"]:hover {
        background-color: #218838;
        border-color: #1e7e34;
        box-shadow: 0 4px 12px rgba(40, 167, 69, 0.2);
    }

    .form-actions .btn-secondary {
        background-color: #6c757d;
        color: white;
        border: 1px solid #6c757d;
    }

    .form-actions .btn-secondary:hover {
        background-color: #545b62;
        border-color: #545b62;
        box-shadow: 0 4px 12px rgba(108, 117, 125, 0.2);
    }

    /* 編輯說明區塊 */
    .edit-info-box {
        background-color: #e6f7ff; /* 淺藍色背景 */
        border: 1px solid #91d5ff; /* 藍色邊框 */
        padding: 1.5rem;
        border-radius: 10px;
        margin-top: 2.5rem;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }

    .edit-info-box h4 {
        font-size: 1.2rem;
        color: #0056b3;
        margin-top: 0;
        margin-bottom: 1rem;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .edit-info-box ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .edit-info-box li {
        color: #444;
        margin-bottom: 0.5rem;
        font-size: 0.95rem;
        display: flex;
        align-items: flex-start;
        gap: 5px;
    }
    
    .edit-info-box li::before {
        content: "•";
        color: #007bff;
        font-weight: bold;
        display: inline-block;
        width: 1em;
        margin-left: -1em;
    }

    /* 前端驗證錯誤文字 */
    .error-text {
        color: #dc3545;
        font-size: 0.875em;
        margin-top: 0.25rem;
        min-height: 1.25em; /* 預留空間避免佈局跳動 */
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

        .form-container > p {
            font-size: 1rem;
            margin-bottom: 1.5rem;
        }

        .top-actions {
            flex-direction: column;
            gap: 1rem;
            border-bottom: none;
            padding-bottom: 0;
        }
        .top-actions a {
            width: 100%;
            text-align: center;
            justify-content: center;
        }

        .info-card, .path-info-box, .edit-info-box {
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1.5rem;
            margin-top: 1.5rem;
        }

        .info-card h3, .path-info-box h4, .edit-info-box h4 {
            font-size: 1.3rem;
            margin-bottom: 0.8rem;
        }

        .form-actions {
            flex-direction: column;
            align-items: stretch;
            gap: 0.75rem;
        }
        
        .form-actions button,
        .form-actions .btn {
            width: 100%;
        }
    }
</style>
</head>
<body>
    <%-- 這裡假設你的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>
   
    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="form-container">
        <h2><i class="fas fa-edit"></i> 編輯檔案記錄</h2>
        <p>在此頁面修改選定檔案的記錄資訊，包括檔案名稱、關聯機台。</p>

        <div class="top-actions">
            <a href="${pageContext.request.contextPath}/FileManagementServlet">
                <i class="fas fa-arrow-left"></i> 返回檔案管理
            </a>
            <a href="${pageContext.request.contextPath}/DeleteFilesConfirmServlet?fileId=${file.fileId}" class="delete-link">
                <i class="fas fa-trash-alt"></i> 刪除此檔案
            </a>
        </div>

        <div class="info-card">
            <h3><i class="fas fa-info-circle"></i> 原始檔案資訊</h3>
            <ul>
                <li><span class="label">檔案 ID：</span>#<c:out value="${file.fileId}" /></li>
                <li><span class="label">上傳時間：</span><c:out value="${file.formattedUploadTime}" /></li>
                <li><span class="label">目前檔案路徑：</span>
                    <code>
                        <c:choose>
                            <c:when test="${fn:length(file.filePath) > 60}">
                                <c:out value="${fn:substring(file.filePath, 0, 60)}" />...
                            </c:when>
                            <c:otherwise>
                                <c:out value="${file.filePath}" />
                            </c:otherwise>
                        </c:choose>
                    </code>
                    <c:if test="${fn:length(file.filePath) > 60}">
                         <i class="fas fa-info-circle" title="${file.filePath}" style="cursor: pointer;"></i>
                    </c:if>
                </li>
            </ul>
        </div>

        <%-- 後端錯誤訊息 --%>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                <i class="fas fa-exclamation-triangle"></i> <c:out value="${errorMessage}" />
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/UpdateFilesServlet" id="updateFileForm">
            <input type="hidden" name="fileId" value="${file.fileId}">

            <div class="form-group">
                <label for="fileName">檔案名稱：</label>
                <input type="text" id="fileName" name="fileName" value="${file.fileName != null ? file.fileName : ''}" required placeholder="請輸入檔案名稱，包含副檔名">
                <div id="fileNameError" class="error-text"></div>
            </div>

            <div class="form-group">
                <label for="machineId">機台 ID：</label>
                <input type="number" id="machineId" name="machineId" value="${file.machineId != null ? file.machineId : ''}" required min="1" placeholder="請輸入存在的機台ID">
                <div id="machineIdError" class="error-text"></div>
                <small>請確認機台 ID 存在於系統中，並為正整數。</small>
            </div>

            <label class="checkbox-group">
                <input type="checkbox" id="keepOriginalPath" name="keepOriginalPath" value="true">
                <i class="fas fa-map-pin"></i> 保持原檔案路徑 (不勾選則自動生成新路徑)
            </label>

            <div class="path-info-box">
                <h4><i class="fas fa-exclamation-circle"></i> 路徑處理說明：</h4>
                <ul>
                    <li>變更 **檔案名稱** 或 **機台 ID** 時，系統通常會建議您<span style="font-weight: bold; color: #dc3545;">重新生成檔案路徑</span>，以確保路徑與內容一致性。</li>
                    <li>勾選「保持原檔案路徑」僅適用於您**不希望路徑發生任何改變**的情況。</li>
                    <li>新路徑格式範例：`https://cloud-storage.example.com/machine-files/machine-{機台ID}/{時間戳}_{檔名}`</li>
                    <li>請注意：此處的「路徑變更」僅影響資料庫中記錄的檔案路徑，**不影響實際儲存在伺服器上的檔案名稱或位置**。</li>
                </ul>
            </div>

            <div class="form-actions">
                <button type="submit">
                    <i class="fas fa-save"></i> 確認修改
                </button>
                <a href="${pageContext.request.contextPath}/FileManagementServlet" class="btn btn-secondary">
                    <i class="fas fa-times-circle"></i> 取消
                </a>
            </div>
        </form>

        <div class="edit-info-box">
            <h4><i class="fas fa-lightbulb"></i> 編輯提示：</h4>
            <ul>
                <li>檔案名稱建議包含副檔名（如 `.pdf`, `.jpg`, `.doc`），以便系統正確識別。</li>
                <li>變更機台 ID 時，請務必確認目標機台 ID 已經存在於系統中。</li>
                <li>所有修改都會即時反映在檔案列表中，請仔細核對。</li>
            </ul>
        </div>
    </div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // 初始化時不勾選「保持原檔案路徑」
    document.getElementById('keepOriginalPath').checked = false;

    // 獲取原始值
    const originalFileName = '<c:out value="${file.fileName}" />';
    const originalMachineId = '<c:out value="${file.machineId}" />';

    const fileNameInput = document.getElementById('fileName');
    const machineIdInput = document.getElementById('machineId');
    const keepOriginalPathCheckbox = document.getElementById('keepOriginalPath');
    const fileNameError = document.getElementById('fileNameError');
    const machineIdError = document.getElementById('machineIdError');

    function showError(element, message) {
        element.textContent = message;
    }
    function clearError(element) {
        element.textContent = '';
    }

    // 當檔名或機台 ID 改變時，提示使用者是否取消勾選「保持原檔案路徑」
    function checkPathKeepingSuggestion() {
        const currentFileName = fileNameInput.value.trim();
        const currentMachineId = machineIdInput.value.trim();

        const isFileNameChanged = currentFileName !== originalFileName;
        const isMachineIdChanged = currentMachineId !== originalMachineId;

        if (keepOriginalPathCheckbox.checked && (isFileNameChanged || isMachineIdChanged)) {
            if (confirm('檔案名稱或機台 ID 已變更，建議取消「保持原檔案路徑」以生成新路徑。是否取消勾選？')) {
                keepOriginalPathCheckbox.checked = false;
            }
        }
    }

    fileNameInput.addEventListener('blur', checkPathKeepingSuggestion);
    machineIdInput.addEventListener('blur', checkPathKeepingSuggestion);

    // 表單提交前的最終驗證和確認
    document.getElementById('updateFileForm').addEventListener('submit', function(e) {
        let isValid = true;

        clearError(fileNameError);
        clearError(machineIdError);

        const fileName = fileNameInput.value.trim();
        const machineId = machineIdInput.value.trim();
        const keepOriginalPath = keepOriginalPathCheckbox.checked;

        // 1. 檔案名稱驗證
        if (!fileName) {
            showError(fileNameError, '請輸入檔案名稱。');
            isValid = false;
        } else if (fileName.length > 255) {
            showError(fileNameError, '檔案名稱不能超過255個字元。');
            isValid = false;
        } else {
            const invalidChars = /[<>:"/\\|?*]/;
            if (invalidChars.test(fileName)) {
                showError(fileNameError, '檔案名稱不能包含特殊字符：< > : " / \\ | ? *');
                isValid = false;
            } else if (!fileName.includes('.') && !confirm('檔案名稱似乎沒有副檔名，確定要繼續嗎？')) {
                e.preventDefault(); // 阻止提交
                return; // 直接返回，不再執行後續驗證
            }
        }

        // 2. 機台 ID 驗證
        if (!machineId) {
            showError(machineIdError, '請輸入機台 ID。');
            isValid = false;
        } else {
            const parsedMachineId = parseInt(machineId);
            if (isNaN(parsedMachineId) || parsedMachineId <= 0) {
                showError(machineIdError, '機台 ID 必須是有效的正整數。');
                isValid = false;
            }
        }

        if (!isValid) {
            e.preventDefault(); // 如果有任何驗證失敗，阻止表單提交
            return;
        }

        // 3. 最終確認彈窗
        const isFileNameChanged = fileName !== originalFileName;
        const isMachineIdChanged = machineId !== originalMachineId;

        let confirmMsg = '確定要修改檔案記錄嗎？\n\n';
        confirmMsg += '檔案 ID：#' + '${file.fileId}' + '\n';
        confirmMsg += '修改後檔案名稱：' + fileName + '\n';
        confirmMsg += '修改後機台 ID：' + machineId + '\n';
        confirmMsg += '路徑處理：' + (keepOriginalPath ? '保持原路徑' : '自動生成新路徑');

        if (!keepOriginalPath && (isFileNameChanged || isMachineIdChanged)) {
             confirmMsg += '\n\n**注意：您已更改檔案名稱或機台 ID，且未勾選保持原路徑，系統將生成新的路徑。**';
        }

        if (!confirm(confirmMsg)) {
            e.preventDefault(); // 如果使用者取消，阻止表單提交
        }
    });
});
</script>

</body>
</html>
