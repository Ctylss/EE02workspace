<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "機台檔案管理系統"); %>
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
        background: linear-gradient(to right bottom, #e0f7fa, #b2ebf2); /* 柔和的藍綠漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
    }

    /* 主要內容容器 */
    .dashboard-container {
        flex-grow: 1;
        max-width: 1200px;
        margin: 2.5rem auto;
        padding: 2.5rem;
        background-color: #ffffff;
        border-radius: 15px;
        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
    }

    .dashboard-container h1 {
        font-size: 2.8rem;
        color: #2c3e50;
        margin-bottom: 0.8rem;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 15px;
    }

    .dashboard-container > p { /* 直接子元素p */
        font-size: 1.2rem;
        color: #555;
        margin-top: 0;
        margin-bottom: 2rem;
        text-align: center;
    }

    /* 訊息顯示 */
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

    .message.success {
        background-color: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }

    .message.error {
        background-color: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }

    /* 搜尋表單 */
    .search-area {
        background-color: #f8fafd;
        padding: 1.5rem 2rem;
        border-radius: 10px;
        margin-bottom: 2.5rem;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
    }

    .search-area form {
        display: flex;
        gap: 1rem;
        flex-wrap: wrap; /* 允許換行 */
        align-items: center;
    }

    .search-area input[type="text"] {
        flex: 1; /* 彈性佔據空間 */
        min-width: 180px; /* 最小寬度 */
        padding: 12px 15px;
        border: 1px solid #ced4da;
        border-radius: 8px;
        font-size: 1rem;
        color: #333;
        transition: all 0.3s ease-in-out;
    }

    .search-area input[type="text"]:focus {
        border-color: #007bff;
        box-shadow: 0 0 0 4px rgba(0, 123, 255, 0.15);
        outline: none;
    }

    .search-area .btn {
        padding: 12px 25px;
        border-radius: 8px;
        font-size: 1rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s ease;
        border: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        white-space: nowrap;
    }

    .search-area .btn-primary {
        background-color: #007bff;
        color: white;
    }

    .search-area .btn-primary:hover {
        background-color: #0056b3;
        box-shadow: 0 4px 10px rgba(0, 123, 255, 0.2);
    }

    .search-area .btn-secondary {
        background-color: #6c757d;
        color: white;
        text-decoration: none; /* 清除連結下劃線 */
    }

    .search-area .btn-secondary:hover {
        background-color: #5a6268;
        box-shadow: 0 4px 10px rgba(108, 117, 125, 0.2);
    }
    
    /* 檔案列表標題 */
    .dashboard-container h3 {
        font-size: 1.8rem;
        color: #2c3e50;
        margin-top: 2.5rem;
        margin-bottom: 1.5rem;
        display: flex;
        align-items: center;
        gap: 10px;
    }
    
    .dashboard-container h3 span { /* For search results text */
        font-size: 1.2rem;
        color: #666;
        margin-left: 10px;
    }

    /* 無檔案訊息 */
    .empty-message {
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

    .empty-message .icon {
        font-size: 3rem;
        color: #ccc;
    }
    
    .empty-message small {
        font-size: 0.9em;
        color: #999;
    }

    /* 表格樣式 */
    .table-responsive {
        overflow-x: auto; /* 橫向滾動 */
        width: 100%;
        border-radius: 10px;
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
        background-color: #ffffff; /* 確保背景色 */
    }

    table {
        width: 100%;
        border-collapse: separate; /* 為了圓角和陰影 */
        border-spacing: 0;
    }

    table thead {
        background-color: #e9ecef; /* 表頭背景色 */
    }

    table th,
    table td {
        padding: 15px;
        text-align: left;
        border-bottom: 1px solid #dee2e6; /* 底部邊框 */
    }

    table th {
        color: #495057;
        font-weight: 600;
        font-size: 1.05rem;
        position: sticky; /* 粘性表頭 */
        top: 0;
        background-color: #e9ecef;
        z-index: 1;
    }
    
    table tbody tr:last-child td {
        border-bottom: none; /* 最後一行沒有底部邊框 */
    }

    table tbody tr:hover {
        background-color: #f8f9fa; /* 行懸停效果 */
    }

    table td {
        color: #555;
        font-size: 0.95rem;
    }

    /* 檔案路徑連結樣式 */
    table td a {
        color: #007bff;
        text-decoration: none;
        word-break: break-all; /* 長路徑自動換行 */
        display: inline-flex; /* 讓圖標和文字對齊 */
        align-items: center;
        gap: 5px;
    }

    table td a:hover {
        text-decoration: underline;
        color: #0056b3;
    }

    /* 操作連結 */
    .action-links {
        white-space: nowrap; /* 防止按鈕換行 */
    }

    .action-links a {
        display: inline-flex;
        align-items: center;
        gap: 5px;
        padding: 6px 12px;
        margin-right: 8px; /* 按鈕間距 */
        border-radius: 6px;
        font-size: 0.85rem;
        text-decoration: none;
        transition: background-color 0.2s ease, transform 0.2s ease;
        border: 1px solid transparent; /* 預設透明邊框 */
    }

    .action-links a.edit-btn {
        background-color: #17a2b8; /* 信息藍 */
        color: white;
    }

    .action-links a.edit-btn:hover {
        background-color: #138496;
        transform: translateY(-1px);
    }

    .action-links a.delete-btn {
        background-color: #dc3545; /* 警告紅 */
        color: white;
    }

    .action-links a.delete-btn:hover {
        background-color: #c82333;
        transform: translateY(-1px);
    }

    /* 管理操作區塊 (新增按鈕) */
    .admin-controls {
        text-align: right; /* 新增按鈕靠右 */
        margin-top: 2rem;
    }

    .admin-controls .btn-success {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 12px 25px;
        background-color: #28a745; /* 成功綠 */
        color: white;
        text-decoration: none;
        border-radius: 8px;
        font-weight: 500;
        transition: background-color 0.3s ease, transform 0.2s ease;
        box-shadow: 0 4px 10px rgba(40, 167, 69, 0.2);
    }

    .admin-controls .btn-success:hover {
        background-color: #218838;
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(40, 167, 69, 0.3);
    }

    /* 響應式調整 */
    @media (max-width: 992px) {
        .dashboard-container {
            margin: 2rem auto;
            padding: 2rem;
        }
        .dashboard-container h1 {
            font-size: 2.2rem;
        }
        .dashboard-container > p {
            font-size: 1.1rem;
        }
        .search-area form {
            flex-direction: column;
            align-items: stretch;
        }
        .search-area input[type="text"] {
            min-width: unset;
        }
        .search-area .btn {
            width: 100%;
            justify-content: center;
        }
        table th, table td {
            padding: 12px;
            font-size: 0.9rem;
        }
        .action-links a {
            padding: 5px 10px;
            margin-right: 6px;
            font-size: 0.8rem;
        }
    }

    @media (max-width: 768px) {
        .dashboard-container {
            margin: 1.5rem auto;
            padding: 1.5rem;
            border-radius: 10px;
        }
        .dashboard-container h1 {
            font-size: 2rem;
            gap: 10px;
        }
        .dashboard-container > p {
            font-size: 1rem;
            margin-bottom: 1.5rem;
        }
        .message-container {
            padding: 0.8rem 1rem;
            font-size: 0.95rem;
            flex-direction: column; /* 訊息堆疊 */
            align-items: flex-start;
        }
        .message-container a {
            margin-left: 0;
            width: 100%;
            text-align: center;
            margin-top: 0.5rem;
        }
        .search-area {
            padding: 1rem 1.2rem;
            margin-bottom: 2rem;
        }
        .search-area form {
            gap: 0.75rem;
        }
        .dashboard-container h3 {
            font-size: 1.5rem;
            flex-direction: column; /* 標題換行 */
            align-items: flex-start;
            gap: 5px;
        }
        .dashboard-container h3 span {
            font-size: 1rem;
            margin-left: 0;
        }
        .empty-message {
            padding: 2rem 1rem;
            font-size: 1rem;
        }
        .admin-controls {
            text-align: center; /* 按鈕居中 */
        }
        .admin-controls .btn-success {
            width: 100%;
            justify-content: center;
        }
    }
</style>
</head>
<body>
    <%-- 這裡假設你的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>
  
    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="dashboard-container">
        <h1><i class="fas fa-folder-open"></i> 機台檔案管理系統</h1>
    

        <%-- 訊息顯示 --%>
        <c:if test="${not empty successMessage || not empty errorMessage}">
            <div class="message-container <c:if test='${not empty successMessage}'>success</c:if><c:if test='${not empty errorMessage}'>error</c:if>">
                <c:if test="${not empty successMessage}"><i class="fas fa-check-circle"></i> ${successMessage}</c:if>
                <c:if test="${not empty errorMessage}"><i class="fas fa-exclamation-triangle"></i> ${errorMessage}</c:if>
            </div>
        </c:if>

        <hr>

        <%-- 搜尋表單 --%>
        <div class="search-area">
            <form method="get" action="${pageContext.request.contextPath}/FileManagementServlet">
                <input type="text" name="keyword" value="${keyword != null ? keyword : ''}" placeholder="搜尋檔案名稱或路徑..." aria-label="搜尋關鍵字">
                <input type="number" name="machineId" value="${selectedMachineId != null ? selectedMachineId : ''}" placeholder="依機台ID搜尋" min="1" aria-label="機台ID">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search"></i> 搜尋
                </button>
                <a href="${pageContext.request.contextPath}/FileManagementServlet" class="btn btn-secondary">
                    <i class="fas fa-sync-alt"></i> 清除篩選
                </a>
            </form>
        </div>

        <%-- 檔案列表 --%>
        <h3>
            <i class="fas fa-list-alt"></i> 檔案列表
            <%-- 顯示總數和搜尋/篩選狀態 --%>
            <c:choose>
                <c:when test="${not empty totalFiles && totalFiles > 0}">
                    <span>(${totalFiles} 筆)</span>
                </c:when>
                <c:otherwise>
                    <span>(0 筆)</span>
                </c:otherwise>
            </c:choose>
            <c:if test="${searchMode || not empty selectedMachineId}">
                <span> - 
                    <c:if test="${searchMode}">搜尋：「<c:out value="${keyword}" />」</c:if>
                    <c:if test="${searchMode && not empty selectedMachineId}">&nbsp;|&nbsp;</c:if>
                    <c:if test="${not empty selectedMachineId}">機台 ID：<c:out value="${selectedMachineId}" /></c:if>
                </span>
            </c:if>
        </h3>

        <c:choose>
            <c:when test="${empty files}">
                <div class="empty-message">
                    <i class="fas fa-box-open icon"></i>
                    <p>目前沒有任何符合條件的檔案。</p>
                    <small>請調整您的搜尋條件，或點擊下方按鈕新增檔案。</small>
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th><i class="fas fa-id-badge"></i> 檔案 ID</th>
                                <th><i class="fas fa-microchip"></i> 機台 ID</th>
                                <th><i class="fas fa-file-alt"></i> 檔案名稱</th>
                                <th><i class="fas fa-file-export"></i> 檔案路徑</th>
                                <th><i class="fas fa-clock"></i> 上傳時間</th>
                                <th><i class="fas fa-cogs"></i> 操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="file" items="${files}">
                                <tr>
                                    <td>#<c:out value="${file.fileId}" /></td>
                                    <td><c:out value="${file.machineId}" /></td>
                                    <td><c:out value="${file.fileName}" /></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/${file.filePath}" target="_blank" rel="noopener noreferrer" title="點擊開啟檔案">
                                            <c:choose>
                                                <c:when test="${fn:length(file.filePath) > 40}">
                                                    <c:out value="${fn:substring(file.filePath, 0, 40)}" />... <i class="fas fa-external-link-alt"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${file.filePath}" /> <i class="fas fa-external-link-alt"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                    </td>
                                    <td>
                                        <%-- 假設 file.formattedUploadTime 已經是格式化好的字符串 --%>
                                        <c:out value="${file.formattedUploadTime}" />
                                        <%-- 如果 file.uploadTime 是 java.util.Date 或 java.sql.Timestamp，可以使用 fmt 標籤格式化：--%>
                                        <%-- <fmt:formatDate value="${file.uploadTime}" pattern="yyyy-MM-dd HH:mm:ss"/> --%>
                                    </td>
                                    <td class="action-links">
                                        <a href="${pageContext.request.contextPath}/UpdateFilesServlet?fileId=${file.fileId}" class="edit-btn">
                                            <i class="fas fa-edit"></i> 編輯
                                        </a>
                                        <a href="${pageContext.request.contextPath}/DeleteFilesConfirmServlet?fileId=${file.fileId}" class="delete-btn">
                                            <i class="fas fa-trash-alt"></i> 刪除
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
        
        <div class="admin-controls">
            <a href="${pageContext.request.contextPath}/InsertFilesServlet" class="btn btn-success">
                <i class="fas fa-plus-circle"></i> 新增檔案
            </a>
        </div>
    </div>
</body>
</html>