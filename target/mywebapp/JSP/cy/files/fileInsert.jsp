<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "新增機台檔案"); %>
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
        max-width: 700px; /* 調整容器寬度以適應表單 */
        margin: 3rem auto; /* 增加上下外邊距 */
        padding: 2.5rem 3rem; /* 增加內邊距 */
        background-color: #ffffff;
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    }

    .form-container h2 {
        font-size: 2rem;
        color: #2c3e50;
        margin-bottom: 2rem; /* 增加標題和表單的間距 */
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
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
        grid-template-columns: 1fr; /* 單列佈局 */
        gap: 1.5rem; /* 欄位之間的間距 */
    }

    .form-group {
        margin-bottom: 0.5rem; /* 每個輸入組的底部間距 */
    }

    .form-group label {
        font-weight: 600;
        color: #495057;
        margin-bottom: 0.5rem;
        display: block; /* 讓 label 獨佔一行 */
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
        background-color: #fcfcfc; /* 輕微背景色 */
        box-sizing: border-box; /* 包含 padding 和 border 在寬度內 */
    }

    form input[type="text"]:focus,
    form input[type="number"]:focus {
        border-color: #007bff;
        box-shadow: 0 0 0 4px rgba(0, 123, 255, 0.15); /* 更明顯的聚焦效果 */
        outline: none;
        background-color: #ffffff;
    }

    /* 檔案上傳樣式 */
    form input[type="file"] {
        width: 100%;
        padding: 12px 0; /* 檔案輸入框的垂直內距 */
        font-size: 1rem;
        color: #333;
    }

    /* 前端驗證錯誤文字 */
    .error-text {
        color: #dc3545; /* 紅色 */
        font-size: 0.875em;
        margin-top: 0.25rem;
        min-height: 1.25em; /* 預留空間避免佈局跳動 */
    }

    /* 額外小提示文字 */
    .form-group small {
        color: #6c757d;
        font-size: 0.9em;
        margin-top: 0.5rem;
        display: block;
    }

    /* 按鈕群組 */
    .form-actions {
        display: flex;
        justify-content: flex-end; /* 靠右對齊 */
        gap: 1rem;
        margin-top: 2rem; /* 增加與表單欄位的間距 */
    }

    .form-actions button,
    .form-actions .btn {
        padding: 12px 25px;
        border-radius: 8px;
        font-size: 1rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s ease;
        text-decoration: none; /* 取消連結下劃線 */
        display: inline-flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        white-space: nowrap; /* 防止文字換行 */
    }

    .form-actions button[type="submit"] {
        background-color: #28a745; /* 綠色，表示成功操作 */
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

    /* 說明資訊框 */
    .info-box {
        background-color: #e6f7ff; /* 淺藍色背景 */
        border: 1px solid #91d5ff; /* 藍色邊框 */
        padding: 1.5rem;
        border-radius: 10px;
        margin-top: 2.5rem; /* 與表單的間距 */
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }

    .info-box h4 {
        font-size: 1.2rem;
        color: #0056b3;
        margin-top: 0;
        margin-bottom: 1rem;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .info-box ul {
        list-style: none; /* 移除默認列表點 */
        padding: 0;
        margin: 0;
    }

    .info-box li {
        color: #444;
        margin-bottom: 0.5rem;
        font-size: 0.95rem;
        display: flex;
        align-items: flex-start;
        gap: 5px; /* 圖標和文字間距 */
    }
    
    .info-box li::before {
        content: "•"; /* 自定義列表點 */
        color: #007bff;
        font-weight: bold;
        display: inline-block;
        width: 1em;
        margin-left: -1em;
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
            margin-bottom: 1.5rem;
        }

        .form-actions {
            flex-direction: column; /* 按鈕垂直堆疊 */
            align-items: stretch; /* 填充整個寬度 */
            gap: 0.75rem;
        }
        
        .form-actions button,
        .form-actions .btn {
            width: 100%; /* 按鈕佔滿寬度 */
        }

        .info-box {
            padding: 1.2rem;
        }
    }
</style>
</head>
<body>
    <%-- 這裡假設你的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>

    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="form-container">
        <h2>➕ 新增機台檔案</h2>

        <%-- 來自後端的錯誤訊息 --%>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                <i class="fas fa-exclamation-triangle"></i> <c:out value="${errorMessage}"/>
            </div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/InsertFilesServlet" id="fileForm" enctype="multipart/form-data">

            <div class="form-group">
                <label for="fileName">檔案名稱：</label>
                <input type="text" id="fileName" name="fileName" required
                    value="${fileName != null ? fileName : ''}"
                    placeholder="例如：機台操作手冊V1.0.pdf">
                <div id="fileNameError" class="error-text"></div>
            </div>

            <div class="form-group">
                <label for="machineId">機台 ID：</label>
                <input type="number" id="machineId" name="machineId" required min="1"
                    value="${machineId != null ? machineId : ''}"
                    placeholder="請輸入存在的機台ID (例如：101)">
                <div id="machineIdError" class="error-text"></div>
                <small>請確認機台 ID 存在於系統中，並為正整數。</small>
            </div>

            <div class="form-group">
                <label for="fileUpload">選擇檔案：</label>
                <input type="file" id="fileUpload" name="fileUpload" required />
                <div id="fileUploadError" class="error-text"></div>
                <small>支援 PDF, JPG, PNG, DOCX 等常見檔案格式。</small>
            </div>

            <div class="form-actions">
                <button type="submit">
                    <i class="fas fa-upload"></i> 儲存檔案
                </button>
                <a href="${pageContext.request.contextPath}/FindFilesServlet" class="btn btn-secondary">
                    <i class="fas fa-times-circle"></i> 取消
                </a>
            </div>
        </form>

        <div class="info-box">
            <h4><i class="fas fa-info-circle"></i> 檔案上傳說明：</h4>
            <ul>
                <li><i class="fas fa-file-alt"></i> 檔案名稱：建議填寫完整且具描述性的名稱（例如：機台型號-操作手冊-2025.pdf）。</li>
                <li><i class="fas fa-microchip"></i> 機台 ID：請務必填寫系統中已存在的機台編號，否則檔案將無法關聯。</li>
                <li><i class="fas fa-folder-open"></i> 檔案儲存：系統會自動為您處理檔案上傳與儲存路徑。</li>
                <li><i class="fas fa-clock"></i> 上傳時間：檔案上傳成功後，系統會自動記錄當前的上傳日期與時間。</li>
            </ul>
        </div>
    </div>

    <script>
    document.getElementById('fileForm').addEventListener('submit', function(e) {
        let isValid = true;

        function showError(id, message) {
            document.getElementById(id + 'Error').textContent = message;
        }
        function clearError(id) {
            document.getElementById(id + 'Error').textContent = '';
        }

        clearError('fileName');
        clearError('machineId');
        clearError('fileUpload'); // 清除檔案上傳的錯誤

        const fileName = document.getElementById('fileName');
        const machineId = document.getElementById('machineId');
        const fileUpload = document.getElementById('fileUpload');

        // 檔案名稱驗證
        if (!fileName.value.trim()) {
            showError('fileName', '請輸入檔案名稱。');
            isValid = false;
        } else if (fileName.value.length > 255) {
            showError('fileName', '檔案名稱不能超過255個字元。');
            isValid = false;
        }

        // 機台 ID 驗證
        if (!machineId.value.trim()) {
            showError('machineId', '請輸入機台 ID。');
            isValid = false;
        } else {
            const parsedMachineId = parseInt(machineId.value);
            if (isNaN(parsedMachineId) || parsedMachineId <= 0) {
                showError('machineId', '機台 ID 必須是有效的正整數。');
                isValid = false;
            }
        }

        // 檔案上傳驗證 (檢查是否選擇了檔案)
        if (fileUpload.files.length === 0) {
            showError('fileUpload', '請選擇要上傳的檔案。');
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault(); // 如果驗證失敗，阻止表單提交
        }
    });
    </script>
</body>
</html>
