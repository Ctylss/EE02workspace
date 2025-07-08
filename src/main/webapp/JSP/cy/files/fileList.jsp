<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "機台檔案列表"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="zh-TW">
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
        background: linear-gradient(to right bottom, #e0f7fa, #b2ebf2); /* 柔和的藍綠漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
    }

    /* 主內容容器 */
    .file-list-container {
        flex-grow: 1;
        max-width: 1200px;
        margin: 2.5rem auto;
        padding: 2.5rem;
        background-color: #ffffff;
        border-radius: 15px;
        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1); /* 更明顯的陰影 */
    }

    .file-list-container h2 {
        font-size: 2.5rem;
        color: #2c3e50;
        margin-bottom: 1.5rem;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 15px;
    }

    /* 成功或錯誤訊息 */
    .message-container {
        padding: 1rem 1.5rem;
        margin-bottom: 2rem;
        border-radius: 8px;
        display: flex;
        align-items: center;
        gap: 12px;
        font-weight: 500;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
    }

    .success-message {
        background-color: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }

    .error-message {
        background-color: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }

    .message-container a {
        color: inherit; /* 繼承父容器顏色 */
        text-decoration: none;
        font-weight: 600;
        margin-left: auto; /* 推到最右邊 */
        padding: 5px 10px;
        border-radius: 5px;
        transition: background-color 0.3s ease;
    }

    .success-message a:hover {
        background-color: #c0d8c7;
    }

    .error-message a:hover {
        background-color: #e9c3c6;
    }

    /* 功能按鈕區塊 */
    .action-bar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1.5rem;
        flex-wrap: wrap; /* 允許換行 */
        gap: 1rem; /* 元素間距 */
    }

    .action-bar .btn-primary {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 10px 20px;
        background-color: #007bff;
        color: white;
        text-decoration: none;
        border-radius: 8px;
        font-weight: 500;
        transition: background-color 0.3s ease, transform 0.2s ease;
        box-shadow: 0 4px 10px rgba(0, 123, 255, 0.2);
    }

    .action-bar .btn-primary:hover {
        background-color: #0056b3;
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(0, 123, 255, 0.3);
    }

    /* 表格樣式 */
    .file-table-responsive {
        overflow-x: auto; /* 橫向滾動 */
        width: 100%;
        border-radius: 10px;
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
    }

    .file-table {
        width: 100%;
        border-collapse: separate; /* 為了圓角和陰影 */
        border-spacing: 0;
        background-color: #ffffff;
    }

    .file-table thead {
        background-color: #e9ecef; /* 表頭背景色 */
    }

    .file-table th,
    .file-table td {
        padding: 15px;
        text-align: left;
        border-bottom: 1px solid #dee2e6; /* 底部邊框 */
    }

    .file-table th {
        color: #495057;
        font-weight: 600;
        font-size: 1.05rem;
        position: sticky; /* 粘性表頭 */
        top: 0;
        background-color: #e9ecef;
        z-index: 1; /* 確保表頭在滾動時在上方 */
    }

    .file-table tbody tr:hover {
        background-color: #f8f9fa; /* 行懸停效果 */
    }

    .file-table td {
        color: #555;
        font-size: 0.95rem;
    }

    /* 動作按鈕在表格內 */
    .file-actions {
        display: flex;
        gap: 8px;
        white-space: nowrap; /* 防止按鈕換行 */
    }

    .file-actions .btn {
        padding: 6px 12px;
        border-radius: 6px;
        font-size: 0.85rem;
        text-decoration: none;
        transition: background-color 0.2s ease;
        display: inline-flex;
        align-items: center;
        gap: 5px;
    }

    .file-actions .btn-info {
        background-color: #17a2b8;
        color: white;
    }

    .file-actions .btn-info:hover {
        background-color: #138496;
    }

    .file-actions .btn-danger {
        background-color: #dc3545;
        color: white;
    }

    .file-actions .btn-danger:hover {
        background-color: #c82333;
    }

    /* 沒有資料的提示 */
    .no-files-message {
        text-align: center;
        padding: 3rem 1.5rem;
        color: #777;
        font-size: 1.2rem;
        background-color: #fefefe;
        border-radius: 10px;
        box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
        margin-top: 2rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 1rem;
    }

    .no-files-message .icon {
        font-size: 3rem;
        color: #ccc;
    }
    
    /* 檔案路徑連結樣式 */
    .file-path-link {
        color: #007bff;
        text-decoration: none;
        word-break: break-all; /* 自動換行 */
    }
    
    .file-path-link:hover {
        text-decoration: underline;
    }

    /* 響應式調整 */
    @media (max-width: 992px) {
        .file-list-container {
            margin: 2rem auto;
            padding: 2rem;
        }
        .file-list-container h2 {
            font-size: 2rem;
        }
    }

    @media (max-width: 768px) {
        .file-list-container {
            margin: 1.5rem auto;
            padding: 1.5rem;
            border-radius: 10px;
        }
        .file-list-container h2 {
            font-size: 1.8rem;
        }
        .action-bar {
            flex-direction: column;
            align-items: stretch;
            gap: 1rem;
        }
        .action-bar .btn-primary {
            width: 100%;
            justify-content: center;
        }
        .file-table-responsive {
            border-radius: 8px;
        }
        .file-table th, .file-table td {
            padding: 12px;
        }
        .no-files-message {
            padding: 2rem 1rem;
            font-size: 1rem;
        }
    }
</style>
</head>
<body>
    <%-- 這裡假設你的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>
 
    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="file-list-container">
        <h2>📚 機台檔案列表</h2>

        <%-- 成功或錯誤訊息 --%>
        <c:if test="${not empty successMessage}">
            <div class="message-container success-message">
                <i class="fas fa-check-circle"></i>
                <c:out value="${successMessage}" />
                <a href="${pageContext.request.contextPath}/FileManagementServlet">
                    <i class="fas fa-arrow-left"></i> 返回檔案管理
                </a>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="message-container error-message">
                <i class="fas fa-times-circle"></i>
                <c:out value="${errorMessage}" />
                <a href="${pageContext.request.contextPath}/FileManagementServlet">
                    <i class="fas fa-redo-alt"></i> 重新載入
                </a>
            </div>
        </c:if>

        <div class="action-bar">
            <%-- 新增檔案按鈕 --%>
            <a href="${pageContext.request.contextPath}/InsertFilesServlet" class="btn-primary">
                <i class="fas fa-plus-circle"></i> 新增機台檔案
            </a>
            <%-- 搜尋/篩選功能 (此處僅為佔位符，實際功能需由後端實現) --%>
            <div class="search-filter">
                <input type="text" placeholder="搜尋檔案名稱或機台ID..." style="padding: 8px 12px; border: 1px solid #ccc; border-radius: 8px;">
                <button type="button" style="padding: 8px 12px; background-color: #f0f0f0; border: 1px solid #ccc; border-radius: 8px; cursor: pointer;">
                    <i class="fas fa-search"></i> 搜尋
                </button>
            </div>
        </div>

        <%-- 檔案列表表格 --%>
        <c:choose>
            <c:when test="${not empty fileList}">
                <div class="file-table-responsive">
                    <table class="file-table">
                        <thead>
                            <tr>
                                <th><i class="fas fa-hashtag"></i> 檔案 ID</th>
                                <th><i class="fas fa-microchip"></i> 機台 ID</th>
                                <th><i class="fas fa-file-alt"></i> 檔案名稱</th>
                                <th><i class="fas fa-link"></i> 檔案路徑</th>
                                <th><i class="fas fa-upload"></i> 上傳時間</th>
                                <th><i class="fas fa-cogs"></i> 操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="file" items="${fileList}">
                                <tr>
                                    <td><c:out value="${file.fileId}" /></td>
                                    <td><c:out value="${file.machineId}" /></td>
                                    <td><c:out value="${file.fileName}" /></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/${file.filePath}" target="_blank" rel="noopener noreferrer" class="file-path-link">
                                            <c:out value="${file.filePath}" /> <i class="fas fa-external-link-alt"></i>
                                        </a>
                                    </td>
                                    <td>
                                        <c:out value="${file.formattedUploadTime}" />
                                        <%-- 如果file.uploadTime是Date或Timestamp物件，可以使用fmt:formatDate --%>
                                        <%-- <fmt:formatDate value="${file.uploadTime}" pattern="yyyy-MM-dd HH:mm:ss"/> --%>
                                    </td>
                                    <td class="file-actions">
                                        <a href="${pageContext.request.contextPath}/EditFilesServlet?fileId=${file.fileId}" class="btn btn-info">
                                            <i class="fas fa-edit"></i> 編輯
                                        </a>
                                        <a href="${pageContext.request.contextPath}/DeleteFilesConfirmServlet?fileId=${file.fileId}" class="btn btn-danger">
                                            <i class="fas fa-trash-alt"></i> 刪除
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="no-files-message">
                    <i class="fas fa-box-open icon"></i>
                    <p>目前沒有任何檔案資料。請點擊上方「新增機台檔案」按鈕新增。</p>
                    <a href="${pageContext.request.contextPath}/InsertFilesServlet" class="btn-primary">
                        <i class="fas fa-plus-circle"></i> 新增第一個檔案
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

        <%-- 分頁導航 (佔位符) --%>
        <div class="pagination" style="text-align: center; margin-top: 2rem;">
            </div>

    </div>
</body>
</html>