<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "機台詳細資料"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%-- 引入 JSTL Functions 庫用於字串處理，例如 fn:replace --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>機台詳細資料</title>
<%-- 引入 Font Awesome 用於圖標 --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<script src="${pageContext.request.contextPath}/sidebar.js"></script>

<style>
    /* 引入 Tailwind CSS CDN */
    @import url("https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css");

    /* 基本佈局和字體 */
    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background-color: #f4f7f6;
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
    }

    .container {
        max-width: 900px; /* 詳細頁面可以略窄一些 */
        margin: 2rem auto;
        padding: 1.5rem;
        background-color: #ffffff;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    }

    /* 頁面標題和動作按鈕區塊 */
    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1.5rem;
        padding-bottom: 1rem;
        border-bottom: 1px solid #eee;
    }

    .header h1 {
        font-size: 1.8rem;
        color: #2c3e50;
        margin: 0;
    }

    .header .actions {
        display: flex;
        gap: 0.75rem;
    }

    /* 按鈕樣式 */
    .btn {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        padding: 0.6rem 1.2rem;
        border-radius: 6px;
        font-size: 0.95rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.2s ease-in-out;
        text-decoration: none;
        white-space: nowrap;
    }

    .btn:hover {
        transform: translateY(-1px);
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    }

    .btn-primary {
        background-color: #007bff;
        color: white;
        border: 1px solid #007bff;
    }

    .btn-primary:hover {
        background-color: #0056b3;
        border-color: #0056b3;
    }

    .btn-secondary {
        background-color: #6c757d;
        color: white;
        border: 1px solid #6c757d;
    }

    .btn-secondary:hover {
        background-color: #545b62;
        border-color: #545b62;
    }

    .btn-info {
        background-color: #17a2b8;
        color: white;
        border: 1px solid #17a2b8;
    }

    .btn-info:hover {
        background-color: #138496;
        border-color: #138496;
    }

    .btn i {
        margin-right: 0.5rem;
    }

    /* 詳細資訊區塊樣式 */
    .detail-card {
        background-color: #f8f9fa;
        border-radius: 8px;
        padding: 1.5rem 2rem;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); /* 響應式佈局 */
        gap: 1.5rem;
    }

    .detail-item {
        margin-bottom: 0.5rem; /* 移除 p 標籤預設的 margin */
    }

    .detail-item strong {
        display: block; /* 標籤單獨一行 */
        color: #555;
        font-size: 0.9rem;
        margin-bottom: 0.2rem;
    }

    .detail-item span {
        font-size: 1.1rem;
        color: #2c3e50;
        font-weight: 500;
    }

    /* 狀態徽章 (與列表頁保持一致) */
    .status-badge {
        display: inline-block;
        padding: 0.3em 0.6em;
        border-radius: 0.25rem;
        font-size: 0.9em; /* 比列表頁略大一點 */
        font-weight: 600;
        text-align: center;
        white-space: nowrap;
        vertical-align: middle;
        line-height: 1;
        opacity: 0.9;
    }

    /* 根據狀態定義徽章顏色 */
    .status-Operational {
        background-color: #28a745; /* 綠色 */
        color: white;
    }
    .status-Maintenance {
        background-color: #ffc107; /* 黃色 */
        color: #333;
    }
    .status-Idle {
        background-color: #17a2b8; /* 淺藍色 */
        color: white;
    }
    .status-Broken {
        background-color: #dc3545; /* 紅色 */
        color: white;
    }
</style>
</head>
<body>
    <%-- 這裡假設你的 header.jsp 和 sidebar.jsp 已經包含了必要的 HTML 結構 --%>
  
    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="container">
        <section class="detail-section">

            <%-- 頁面標題和動作按鈕 --%>
            <div class="header">
                <h1 class="text-2xl font-bold">${pageTitle != null ? pageTitle : "機台詳細資料"}</h1>
                <div class="actions">
                    <a href="<%= request.getContextPath() %>/machines?action=list" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> 返回列表
                    </a>
                    <c:if test="${not empty machine}">
                        <a href="<%= request.getContextPath() %>/machines?action=edit&id=${machine.machineId}" class="btn btn-info">
                            <i class="fas fa-edit"></i> 編輯
                        </a>
                    </c:if>
                </div>
            </div>

            <%-- 機台詳細資訊顯示區塊 --%>
            <c:choose>
                <c:when test="${not empty machine}">
                    <div class="detail-card">
                        <div class="detail-item">
                            <strong>機台ID:</strong>
                            <span><c:out value="${machine.machineId}"/></span>
                        </div>
                        <div class="detail-item">
                            <strong>機台名稱:</strong>
                            <span><c:out value="${machine.machineName}"/></span>
                        </div>
                        <div class="detail-item">
                            <strong>出廠編號:</strong>
                            <span><c:out value="${machine.serialNumber != null ? machine.serialNumber : 'N/A'}"/></span>
                        </div>
                        <div class="detail-item">
                            <strong>機台狀態:</strong>
                            <span>
                                <span class="status-badge status-<c:out value="${fn:replace(machine.mstatus, ' ', '')}"/>">
                                    <c:out value="${machine.mstatus}"/>
                                </span>
                            </span>
                        </div>
                        <div class="detail-item">
                            <strong>機台位置:</strong>
                            <span><c:out value="${machine.machineLocation != null ? machine.machineLocation : 'N/A'}"/></span>
                        </div>
                        <%-- 如果還有其他詳細資料，可以在這裡添加 --%>
                        <%-- 範例：假設有建立日期和更新日期 --%>
                        <%--
                        <div class="detail-item">
                            <strong>建立日期:</strong>
                            <span><fmt:formatDate value="${machine.createDate}" pattern="yyyy-MM-dd HH:mm"/></span>
                        </div>
                        <div class="detail-item">
                            <strong>更新日期:</strong>
                            <span><fmt:formatDate value="${machine.updateDate}" pattern="yyyy-MM-dd HH:mm"/></span>
                        </div>
                        --%>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-danger">
                        找不到指定的機台資料。
                    </div>
                </c:otherwise>
            </c:choose>

        </section>
    </div>
</body>
</html>