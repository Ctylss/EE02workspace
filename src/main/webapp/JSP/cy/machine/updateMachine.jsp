<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "編輯機台資料"); %>
<%
    String[] statusOptions = { "運行中", "維護中", "停機" };
    request.setAttribute("statusOptions", statusOptions);
%>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

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
    .edit-form-container {
        flex-grow: 1;
        max-width: 700px; /* 調整容器寬度以適應表單 */
        margin: 3rem auto; /* 增加上下外邊距 */
        padding: 2.5rem 3rem; /* 增加內邊距 */
        background-color: #ffffff;
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    }

    .edit-form-container h2 {
        font-size: 2rem;
        color: #2c3e50;
        margin-bottom: 2rem; /* 增加標題和表單的間距 */
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
    }

    /* 表單錯誤訊息 (如果後端有傳遞) */
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

    form label {
        font-weight: 600;
        color: #495057;
        margin-bottom: 0.5rem;
        display: block; /* 讓 label 獨佔一行 */
        font-size: 1.05rem;
    }

    form input[type="text"],
    form select {
        width: 100%;
        padding: 12px 15px;
        border: 1px solid #ced4da;
        border-radius: 8px;
        font-size: 1rem;
        color: #333;
        transition: all 0.3s ease-in-out;
        background-color: #fcfcfc; /* 輕微背景色 */
    }

    form input[type="text"]:focus,
    form select:focus {
        border-color: #007bff;
        box-shadow: 0 0 0 4px rgba(0, 123, 255, 0.15); /* 更明顯的聚焦效果 */
        outline: none;
        background-color: #ffffff;
    }

    form select {
        appearance: none; /* 移除默認下拉箭頭 */
        -webkit-appearance: none;
        background-image: url('data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22292.4%22%20height%3D%22292.4%22%3E%3Cpath%20fill%3D%22%23007bff%22%20d%3D%22M287%2069.4a17.6%2017.6%200%200%200-13.2-5.6H18.8c-7.7%200-13.5%204.6-13.5%2012.3%200%204.2%201.8%207.8%205.3%2010.5l129.5%20129.5c2.4%202.4%205.6%204.2%208.7%204.2s6.3-1.8%208.7-4.2l129.5-129.5c3.5-2.7%205.3-6.3%205.3-10.5%200-7.7-5.8-12.3-13.5-12.3z%22%2F%3E%3C%2Fsvg%3E');
        background-repeat: no-repeat;
        background-position: right 15px center;
        background-size: 12px auto;
        padding-right: 40px; /* 為箭頭預留空間 */
    }

    /* 前端驗證錯誤文字 */
    .error-text {
        color: #dc3545; /* 紅色 */
        font-size: 0.875em;
        margin-top: 0.25rem;
        min-height: 1.25em; /* 預留空間避免佈局跳動 */
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
        background-color: #007bff; /* 藍色，表示更新操作 */
        color: white;
        border: 1px solid #007bff;
    }

    .form-actions button[type="submit"]:hover {
        background-color: #0056b3;
        border-color: #0056b3;
        box-shadow: 0 4px 12px rgba(0, 123, 255, 0.2);
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

    /* 響應式調整 */
    @media (max-width: 768px) {
        .edit-form-container {
            margin: 1.5rem auto;
            padding: 1.5rem;
            border-radius: 8px;
        }

        .edit-form-container h2 {
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
    }
</style>
</head>
<body>
    <%-- 這裡假設你的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>
    
    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="edit-form-container">
        <h2>✏️ 編輯機台資料</h2>

        <%-- 來自後端的錯誤訊息 (如果有) --%>
        <c:if test="${not empty error}">
            <div class="error-message">
                <i class="fas fa-exclamation-triangle"></i> <c:out value="${error}"/>
            </div>
        </c:if>

        <%-- 確保 machine 物件存在才顯示表單 --%>
        <c:choose>
            <c:when test="${not empty machine}">
                <form id="editMachineForm" action="${pageContext.request.contextPath}/UpdateMachineServlet" method="post">
                    <input type="hidden" name="machineId" value="${machine.machineId}" />
                    
                    <div>
                        <label for="machineName">機台名稱：</label>
                        <input type="text" id="machineName" name="machineName" value="${machine.machineName}" required />
                        <div id="machineNameError" class="error-text"></div>
                    </div>
                    
                    <div>
                        <label for="serialNumber">出廠編號：</label>
                        <input type="text" id="serialNumber" name="serialNumber" value="${machine.serialNumber}" required />
                        <div id="serialNumberError" class="error-text"></div>
                    </div>
                    
                    <div>
                        <label for="mstatus">機台狀態：</label>
                        <select id="mstatus" name="mstatus" required>
                            <option value="">請選擇機台狀態</option>
                            <c:forEach var="option" items="${statusOptions}">
                                <option value="${option}" <c:if test="${machine.mstatus == option}">selected</c:if>>
                                    ${option}
                                </option>
                            </c:forEach>
                        </select>
                        <div id="mstatusError" class="error-text"></div>
                    </div>
                    
                    <div>
                        <label for="machineLocation">機台位置：</label>
                        <input type="text" id="machineLocation" name="machineLocation" value="${machine.machineLocation}" />
                        <div id="machineLocationError" class="error-text"></div>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit">
                            <i class="fas fa-save"></i> 更新機台
                        </button>
                        <a href="${pageContext.request.contextPath}/backstage" class="btn btn-secondary">
                            <i class="fas fa-times-circle"></i> 取消
                        </a>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="error-message">
                    <i class="fas fa-info-circle"></i> 無法載入機台資料，請確認機台ID是否正確。
                    <div class="form-actions" style="justify-content: center; margin-top: 1rem;">
                        <a href="${pageContext.request.contextPath}/backstage" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> 返回機台列表
                        </a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script>
        document.getElementById("serialNumber").addEventListener("input", function () {
            this.value = this.value.toUpperCase(); // 自動轉換為大寫
        });

        document.getElementById("editMachineForm").addEventListener("submit", function (e) {
            let isValid = true;

            function showError(id, message) {
                document.getElementById(id + "Error").textContent = message;
            }

            function clearError(id) {
                document.getElementById(id + "Error").textContent = "";
            }

            const machineName = document.getElementById("machineName");
            const serialNumber = document.getElementById("serialNumber");
            const machineLocation = document.getElementById("machineLocation");
            const mstatus = document.getElementById("mstatus");

            // 機台名稱驗證
            clearError("machineName");
            if (!machineName.value.trim()) {
                showError("machineName", "請輸入機台名稱。");
                isValid = false;
            } else if (machineName.value.length < 2 || machineName.value.length > 50) {
                showError("machineName", "機台名稱長度需介於2~50個字元。");
                isValid = false;
            }

            // 出廠編號驗證
            clearError("serialNumber");
            const snPattern = /^[A-Z0-9\-]+$/; // 允許大寫字母、數字和連字號
            if (!serialNumber.value.trim()) {
                showError("serialNumber", "請輸入出廠編號。");
                isValid = false;
            } else if (!snPattern.test(serialNumber.value)) {
                showError("serialNumber", "出廠編號格式錯誤（限大寫字母、數字與連字號）。");
                isValid = false;
            }

            // 機台狀態驗證
            clearError("mstatus");
            if (!mstatus.value.trim()) {
                showError("mstatus", "請選擇機台狀態。");
                isValid = false;
            }

            // 機台位置驗證 (不再是 required，但檢查長度)
            clearError("machineLocation");
            if (machineLocation.value.length > 100) {
                showError("machineLocation", "機台位置不可超過100個字元。");
                isValid = false;
            }

            if (!isValid) {
                e.preventDefault(); // 阻止表單提交
            }
        });
    </script>
</body>
</html>