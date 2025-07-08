<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "機台查詢系統"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
        background: linear-gradient(to right bottom, #e0f2f7, #c1e7f0); /* 柔和的漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex; /* 讓 header 和 container 能彈性佈局 */
        flex-direction: column;
    }

    .container {
        flex-grow: 1; /* 佔據剩餘空間 */
        max-width: 1300px; /* 擴大容器寬度 */
        margin: 2rem auto;
        padding: 2.5rem; /* 增加內邊距 */
        background-color: #ffffff;
        border-radius: 12px; /* 更圓潤的邊角 */
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1); /* 更明顯的陰影 */
        display: flex;
        flex-direction: column;
        gap: 1.5rem; /* 內容區塊之間的間距 */
    }

    /* 頁面頂部標題區塊 */
    .header {
        text-align: center;
        margin-bottom: 2rem;
        padding-bottom: 1.5rem;
        border-bottom: 1px solid #e0e0e0;
    }

    .header h1 {
        font-size: 2.5rem; /* 放大標題字體 */
        color: #2c3e50;
        margin-bottom: 0.5rem;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
    }

    .header p {
        font-size: 1.1rem;
        color: #555;
        margin-top: 0;
    }

    .header div { /* 訪客模式標籤 */
        background: linear-gradient(to right, #6dd5ed, #2193b0); /* 漸變背景 */
        color: white;
        padding: 8px 20px;
        border-radius: 25px;
        font-size: 0.9em;
        margin-top: 15px;
        display: inline-flex; /* 保持圖標和文字在一行 */
        align-items: center;
        gap: 5px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    }

    /* 搜尋區域 */
    .search-area {
        background-color: #f9fbfd; /* 淺色背景 */
        padding: 2rem; /* 增加內邊距 */
        border-radius: 10px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        margin-bottom: 2rem;
    }

    .search-area form {
        display: flex;
        gap: 1rem; /* 元素間距 */
        flex-wrap: wrap; /* 換行顯示 */
        align-items: center;
    }

    .search-area input[type="text"],
    .search-area select {
        flex: 1; /* 彈性佔據空間 */
        padding: 12px 15px;
        border: 1px solid #dcdcdc;
        border-radius: 8px;
        font-size: 1rem;
        color: #333;
        transition: all 0.3s ease;
        min-width: 200px; /* 最小寬度 */
    }

    .search-area input[type="text"]:focus,
    .search-area select:focus {
        border-color: #007bff;
        box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.15);
        outline: none;
    }

    .search-area select {
        appearance: none; /* 移除默認下拉箭頭 */
        -webkit-appearance: none;
        background-image: url('data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22292.4%22%20height%3D%22292.4%22%3E%3Cpath%20fill%3D%22%23007bff%22%20d%3D%22M287%2069.4a17.6%2017.6%200%200%200-13.2-5.6H18.8c-7.7%200-13.5%204.6-13.5%2012.3%200%204.2%201.8%207.8%205.3%2010.5l129.5%20129.5c2.4%202.4%205.6%204.2%208.7%204.2s6.3-1.8%208.7-4.2l129.5-129.5c3.5-2.7%205.3-6.3%205.3-10.5%200-7.7-5.8-12.3-13.5-12.3z%22%2F%3E%3C%2Fsvg%3E');
        background-repeat: no-repeat;
        background-position: right 12px top 50%;
        background-size: 12px auto;
        padding-right: 35px; /* 為箭頭預留空間 */
    }

    .search-area .btn {
        padding: 12px 25px;
        border-radius: 8px;
        font-size: 1rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s ease;
        white-space: nowrap;
        text-decoration: none; /* 重置按鈕的下劃線 */
        display: inline-flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
    }

    .search-area .btn[type="submit"] {
        background-color: #007bff;
        color: white;
        border: 1px solid #007bff;
    }

    .search-area .btn[type="submit"]:hover {
        background-color: #0056b3;
        border-color: #0056b3;
        box-shadow: 0 4px 12px rgba(0, 90, 179, 0.2);
    }

    .search-area .btn.reset { /* 重置按鈕獨立樣式 */
        background-color: #6c757d;
        color: white;
        border: 1px solid #6c757d;
    }

    .search-area .btn.reset:hover {
        background-color: #545b62;
        border-color: #545b62;
        box-shadow: 0 4px 12px rgba(108, 117, 125, 0.2);
    }


    /* 機台列表標題 */
    h3 {
        font-size: 1.6rem;
        color: #2c3e50;
        margin-top: 0;
        margin-bottom: 1.5rem;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    /* 表格樣式 */
    table {
        width: 100%;
        border-collapse: collapse;
        background-color: #fff;
        border-radius: 10px;
        overflow: hidden; /* 確保圓角生效 */
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
    }

    table thead th {
        background-color: #e9ecef;
        color: #495057;
        font-weight: 600;
        padding: 1rem 1.2rem;
        text-align: left;
        text-transform: uppercase;
        font-size: 0.95rem;
    }

    table tbody tr:nth-child(even) {
        background-color: #f8f9fa; /* 斑馬紋效果 */
    }

    table tbody tr:hover {
        background-color: #f2f2f2;
        cursor: pointer;
    }

    table td {
        padding: 1rem 1.2rem;
        border-bottom: 1px solid #e9ecef;
        vertical-align: middle;
    }

    table tbody tr:last-child td {
        border-bottom: none;
    }

    /* 狀態徽章 */
    .status {
        display: inline-flex;
        align-items: center;
        padding: 0.4em 0.8em;
        border-radius: 5px;
        font-weight: 600;
        font-size: 0.9em;
        gap: 5px;
    }

    .status.running {
        background-color: #d4edda;
        color: #155724;
    }

    .status.maintenance {
        background-color: #fff3cd;
        color: #856404;
    }

    .status.stopped {
        background-color: #f8d7da;
        color: #721c24;
    }

    /* 查看詳情按鈕 */
    .machine-detail {
        display: inline-flex;
        align-items: center;
        background-color: #007bff;
        color: white;
        padding: 0.5rem 1rem;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.2s ease;
        font-size: 0.9em;
        gap: 5px;
        text-decoration: none;
    }

    .machine-detail:hover {
        background-color: #0056b3;
    }

    /* 空資料訊息 */
    .empty-message {
        text-align: center;
        padding: 3rem 1rem;
        color: #777;
        font-size: 1.1rem;
    }

    .empty-message small {
        display: block;
        margin-top: 0.5rem;
        color: #999;
    }

    /* 響應式表格 */
    @media (max-width: 768px) {
        .container {
            padding: 1rem;
            margin: 1rem auto;
            border-radius: 8px;
        }

        .header h1 {
            font-size: 2rem;
            flex-wrap: wrap;
            text-align: center;
            justify-content: center;
        }

        .search-area form {
            flex-direction: column;
            gap: 10px;
        }

        .search-area input[type="text"],
        .search-area select,
        .search-area .btn {
            width: 100%;
            min-width: unset;
        }

        table thead {
            display: none; /* 在小螢幕上隱藏表頭 */
        }

        table, tbody, tr, td {
            display: block;
            width: 100%;
        }

        table tr {
            margin-bottom: 1rem;
            border: 1px solid #eee;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 5px rgba(0,0,0,0.05);
        }

        table td {
            text-align: right;
            padding-left: 50%;
            position: relative;
            border: none;
            padding-top: 0.8rem;
            padding-bottom: 0.8rem;
        }

        table td::before {
            content: attr(data-label); /* 使用 data-label 作為偽元素的內容 */
            position: absolute;
            left: 10px;
            width: calc(50% - 20px);
            white-space: nowrap;
            text-align: left;
            font-weight: 600;
            color: #6c757d;
        }
        
        /* 針對特定欄位設定 data-label */
        table td:nth-of-type(1):before { content: "機台編號:"; }
        table td:nth-of-type(2):before { content: "機台名稱:"; }
        table td:nth-of-type(3):before { content: "出廠編號:"; }
        table td:nth-of-type(4):before { content: "運行狀態:"; }
        table td:nth-of-type(5):before { content: "機台位置:"; }
        table td:nth-of-type(6):before { content: "操作:"; }

        .machine-detail {
            width: fit-content;
            margin-left: auto; /* 讓按鈕在右側 */
            margin-right: 0;
        }
    }
</style>
</head>
<body>
    <%-- 這裡假設你的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>
  
    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="container">
        <div class="header">
            <h1>🔍 機台查詢系統</h1>
            <p>即時查看機台運行狀態</p>
            <div>
                <i class="fas fa-user-tag"></i> 訪客模式 - 僅供查看
            </div>
        </div>
        
        <div class="content">
            <div class="search-area">
                <form method="get" action="${pageContext.request.contextPath}/frontend">
                    <input type="text" name="search" placeholder="🔍 搜尋機台名稱或編號..." value="${param.search}">
                    <select name="statusFilter">
                        <option value="">📋 所有狀態</option>
                        <option value="運行中" ${param.statusFilter == '運行中' ? 'selected' : ''}>🟢 運行中</option>
                        <option value="維護中" ${param.statusFilter == '維護中' ? 'selected' : ''}>🟡 維護中</option>
                        <option value="停機" ${param.statusFilter == '停機' ? 'selected' : ''}>🔴 停機</option>
                        <%-- 如果有其他狀態，可以在這裡添加 --%>
                    </select>
                    <button type="submit" class="btn">搜尋</button>
                    <a href="${pageContext.request.contextPath}/frontend" class="btn reset">重置</a>
                </form>
            </div>
            
            <h3>📋 機台狀態一覽</h3>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>機台編號</th>
                            <th>機台名稱</th>
                            <th>出廠編號</th>
                            <th>運行狀態</th>
                            <th>機台位置</th>
                            <th>查看詳情</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty machines}">
                                <tr>
                                    <td colspan="6" class="empty-message">
                                        🔍 沒有找到符合條件的機台資料<br>
                                        <small>請調整搜尋條件或聯繫管理員</small>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="machine" items="${machines}">
                                    <tr onclick="showDetail(${machine.machineId}, '${machine.machineName}', '${machine.serialNumber}', '${machine.mstatus}', '${machine.machineLocation}')">
                                        <td data-label="機台編號"><strong>#<c:out value="${machine.machineId}"/></strong></td>
                                        <td data-label="機台名稱"><c:out value="${machine.machineName}"/></td>
                                        <td data-label="出廠編號"><code><c:out value="${machine.serialNumber}"/></code></td>
                                        <td data-label="運行狀態">
                                            <c:choose>
                                                <c:when test="${machine.mstatus == '運行中'}">
                                                    <span class="status running">🟢 <c:out value="${machine.mstatus}"/></span>
                                                </c:when>
                                                <c:when test="${machine.mstatus == '維護中'}">
                                                    <span class="status maintenance">🟡 <c:out value="${machine.mstatus}"/></span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="status stopped">🔴 <c:out value="${machine.mstatus}"/></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td data-label="機台位置">📍 <c:out value="${machine.machineLocation}"/></td>
                                        <td data-label="查看詳情">
                                            <div class="machine-detail">
                                                📊 查看詳情
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <script>
        // 將 alert 替換為更美觀的 Modal/Popup
        // 或者保留 alert 配合機台列表行的 onclick 事件
        function showDetail(id, name, serial, status, location) {
            alert(
                '📋 機台詳細資料\n\n' +
                '🆔 機台編號：' + id + '\n' +
                '📛 機台名稱：' + name + '\n' +
                '🏷️ 出廠編號：' + (serial || 'N/A') + '\n' + /* 處理 serial 為 null 的情況 */
                '⚡ 運行狀態：' + status + '\n' +
                '📍 機台位置：' + (location || 'N/A') /* 處理 location 為 null 的情況 */
            );
        }
    </script>
</body>
</html>
