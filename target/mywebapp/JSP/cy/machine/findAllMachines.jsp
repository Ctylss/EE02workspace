<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "機台列表"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>機台列表</title>
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
        max-width: 1200px;
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

    .btn-danger {
        background-color: #dc3545;
        color: white;
        border: 1px solid #dc3545;
    }

    .btn-danger:hover {
        background-color: #c82333;
        border-color: #bd2130;
    }

    .btn i {
        margin-right: 0.5rem;
    }

    /* 訊息提示 */
    .alert {
        padding: 1rem;
        margin-bottom: 1.5rem;
        border-radius: 6px;
        font-weight: 500;
        display: flex;
        align-items: center;
    }

    .alert-success {
        background-color: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }

    .alert-danger {
        background-color: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }

    /* 表單控制項 */
    .form-control {
        display: block;
        width: 100%; /* Tailwind w-full */
        padding: 0.6rem 0.8rem;
        font-size: 1rem;
        line-height: 1.5;
        color: #495057;
        background-color: #fff;
        background-clip: padding-box;
        border: 1px solid #ced4da;
        border-radius: 0.25rem;
        transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
    }

    .form-control:focus {
        border-color: #80bdff;
        outline: 0;
        box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
    }

    /* 資料表格樣式 */
    .data-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 1.5rem;
        background-color: #fff;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        border-radius: 8px;
        overflow: hidden; /* 確保圓角生效 */
    }

    .data-table th,
    .data-table td {
        padding: 1rem 1.2rem;
        text-align: left;
        border-bottom: 1px solid #e9ecef;
    }

    .data-table th {
        background-color: #f8f9fa;
        color: #495057;
        font-weight: 600;
        text-transform: uppercase;
        font-size: 0.9rem;
    }

    .data-table tbody tr:hover {
        background-color: #f2f2f2;
    }

    .data-table tbody tr:last-child td {
        border-bottom: none;
    }

    /* 響應式表格：在小螢幕上將標題顯示為數據行的標籤 */
    @media (max-width: 768px) {
        .data-table thead {
            display: none; /* 在小螢幕上隱藏表頭 */
        }

        .data-table, .data-table tbody, .data-table tr, .data-table td {
            display: block;
            width: 100%;
        }

        .data-table tr {
            margin-bottom: 1rem;
            border: 1px solid #e9ecef;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }

        .data-table td {
            text-align: right;
            padding-left: 50%; /* 為數據內容騰出空間 */
            position: relative;
            border: none;
        }

        .data-table td::before {
            content: attr(data-label); /* 使用 data-label 作為偽元素的內容 */
            position: absolute;
            left: 10px;
            width: calc(50% - 20px);
            padding-right: 10px;
            white-space: nowrap;
            text-align: left;
            font-weight: 600;
            color: #6c757d;
        }
        
        .action-buttons-cell {
            text-align: center; /* 操作按鈕在小螢幕上居中 */
        }

        .action-buttons-cell .btn {
             margin-right: 0.5rem; /* 小螢幕上的按鈕間距 */
             margin-bottom: 0.5rem; /* 小螢幕上的按鈕間距 */
        }
    }

    /* 狀態徽章 */
    .status-badge {
        display: inline-block;
        padding: 0.3em 0.6em;
        border-radius: 0.25rem;
        font-size: 0.85em;
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
    /* 你可以根據你實際的機台狀態添加更多徽章顏色 */

</style>
</head>
<body>
    <%-- 這裡假設你的 header.jsp 和 sidebar.jsp 已經包含了必要的 HTML 結構 --%>
    <%-- 並且 container 類別定義了主要的內容區域樣式 --%>
    <%-- 如果 header.jsp 已經定義了 body 和 html 標籤，請調整此處的結構以避免重複 --%>

    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="container">
        <section class="data-table-section">

            <%-- 頁面標題和動作按鈕 --%>
            <div class="header">
                <h1 class="text-2xl font-bold">${pageTitle != null ? pageTitle : "機台列表"}</h1>
                <div class="actions">
                    <a href="<%= request.getContextPath() %>/machines?action=new" class="btn btn-primary">新增機台</a>
                    <a href="<%= request.getContextPath() %>/machines?action=list" class="btn btn-secondary">顯示全部機台</a>
                </div>
            </div>

            <%-- 顯示成功或錯誤訊息 --%>
            <c:if test="${not empty sessionScope.message}">
                <div class="alert alert-success">
                    <c:out value="${sessionScope.message}"/>
                </div>
                <c:remove var="message" scope="session"/>
            </c:if>
            <c:if test="${not empty requestScope.errorMessage}">
                <div class="alert alert-danger">
                    <c:out value="${requestScope.errorMessage}"/>
                </div>
            </c:if>

            <div class="toolbar flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-4 mb-6">
                <%-- 狀態篩選表單 (範例，如果機台也有狀態篩選需求) --%>
                <form action="${pageContext.request.contextPath}/machines" method="GET" class="flex items-center space-x-2 w-full md:w-auto">
                    <input type="hidden" name="action" value="byStatus">
                    <label for="statusFilter" class="font-medium whitespace-nowrap">篩選狀態:</label>
                    <select name="status" id="statusFilter" class="form-control w-full md:w-auto">
                        <option value="">所有狀態</option>
                        <option value="Operational" <c:if test="${param.status eq 'Operational'}">selected</c:if>>運行中</option>
                        <option value="Maintenance" <c:if test="${param.status eq 'Maintenance'}">selected</c:if>>維護中</option>
                        <option value="Idle" <c:if test="${param.status eq 'Idle'}">selected</c:if>>閒置</option>
                        <option value="Broken" <c:if test="${param.status eq 'Broken'}">selected</c:if>>故障</option>
                    </select>
                    <button type="submit" class="btn btn-primary flex-shrink-0">篩選</button>
                </form>

                <%-- 搜尋表單 (範例，可以按名稱或編號搜尋) --%>
                <form action="${pageContext.request.contextPath}/machines" method="GET" class="flex items-center space-x-2 w-full md:w-auto">
                    <input type="hidden" name="action" value="search">
                    <label for="searchTerm" class="font-medium whitespace-nowrap">搜尋:</label>
                    <input type="text" name="term" id="searchTerm" class="form-control w-full md:w-auto" placeholder="輸入名稱或編號" value="<c:out value="${param.term}"/>">
                    <button type="submit" class="btn btn-primary flex-shrink-0">搜尋</button>
                </form>
            </div>

            <div class="overflow-x-auto">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>機台ID</th>
                            <th>名稱</th>
                            <th>出廠編號</th>
                            <th>狀態</th>
                            <th>位置</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="machine" items="${machines}">
                            <tr>
                                <td data-label="機台ID"><c:out value="${machine.machineId}"/></td>
                                <td data-label="名稱"><c:out value="${machine.machineName}"/></td>
                                <td data-label="出廠編號"><c:out value="${machine.serialNumber}"/></td>
                                <td data-label="狀態">
                                    <span class="status-badge status-<c:out value="${fn:replace(machine.mstatus, ' ', '')}"/>">
                                        <c:out value="${machine.mstatus}"/>
                                    </span>
                                </td>
                                <td data-label="位置"><c:out value="${machine.machineLocation}"/></td>
                                <td data-label="操作" class="whitespace-nowrap action-buttons-cell">
                                    <a href="${pageContext.request.contextPath}/machines?action=view&id=<c:out value="${machine.machineId}"/>" class="btn btn-secondary text-sm px-2 py-1 mb-1 sm:mb-0 sm:mr-1">
                                        <i class="fas fa-eye"></i> 查看
                                    </a>
                                    <a href="${pageContext.request.contextPath}/machines?action=edit&id=<c:out value="${machine.machineId}"/>" class="btn btn-info text-sm px-2 py-1 mb-1 sm:mb-0 sm:mr-1">
                                        <i class="fas fa-edit"></i> 編輯
                                    </a>
                                    <button class="btn btn-danger text-sm px-2 py-1"
                                            onclick="if(confirm('確定要刪除機台ID: <c:out value="${machine.machineId}"/> 嗎？')) { window.location.href='${pageContext.request.contextPath}/machines?action=delete&id=<c:out value="${machine.machineId}"/>'; }">
                                        <i class="fas fa-trash"></i> 刪除
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty machines}">
                            <tr>
                                <td colspan="6" class="text-center py-4">沒有找到機台資料。</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </section>
    </div>
</body>
</html>