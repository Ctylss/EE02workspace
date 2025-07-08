<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "維修單管理"); %>
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
        background: linear-gradient(to right bottom, #f0f4f8, #d9e2ec); /* 柔和的淺藍灰漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
    }

    /* 主要內容容器 */
    .admin-container {
        flex-grow: 1;
        max-width: 1200px; /* 較寬的容器 */
        margin: 2.5rem auto;
        padding: 2.5rem;
        background-color: #ffffff;
        border-radius: 15px;
        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
    }

    .admin-container h1 {
        font-size: 2.8rem;
        color: #2c3e50;
        margin-bottom: 0.8rem;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 15px;
    }

    .admin-container > p { /* 直接子元素p */
        font-size: 1.2rem;
        color: #555;
        margin-top: 0;
        margin-bottom: 2rem;
        text-align: center;
    }

    /* 搜尋與篩選區域 */
    .search-filter-card {
        background: #f8f9fa;
        padding: 25px 30px;
        margin-bottom: 30px;
        border-radius: 12px;
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
        display: flex;
        flex-wrap: wrap; /* 允許換行 */
        gap: 25px; /* 增加間距 */
        justify-content: center;
        align-items: flex-end;
    }

    .search-filter-card h3 {
        width: 100%; /* 標題佔滿一行 */
        font-size: 1.8rem;
        color: #34495e;
        margin-top: 0;
        margin-bottom: 20px;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
    }

    .search-filter-form {
        display: flex;
        align-items: center;
        gap: 10px;
        flex-wrap: wrap; /* 允許表單內部元素換行 */
    }

    .search-filter-form label {
        font-weight: 500;
        color: #495057;
        font-size: 1rem;
        white-space: nowrap; /* 防止標籤換行 */
    }

    .search-filter-form input[type="number"], /* 將 input type 改為 number 更符合 ID 查詢 */
    .search-filter-form select {
        padding: 10px 15px;
        border: 1px solid #ced4da;
        border-radius: 8px;
        font-size: 0.95rem;
        color: #333;
        transition: all 0.3s ease;
        background-color: #ffffff;
        min-width: 150px; /* 最小寬度 */
        box-sizing: border-box; /* 確保 padding 不增加寬度 */
        appearance: none; /* 移除 select 的預設樣式 */
        background-image: url('data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>');
        background-repeat: no-repeat;
        background-position: right 12px center;
        background-size: 18px;
    }

    .search-filter-form input[type="number"]:focus,
    .search-filter-form select:focus {
        border-color: #007bff;
        box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
        outline: none;
    }

    .search-filter-form button {
        padding: 10px 20px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 8px;
        font-size: 0.95rem;
        cursor: pointer;
        transition: background-color 0.2s ease, transform 0.2s ease;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .search-filter-form button:hover {
        background-color: #0056b3;
        transform: translateY(-1px);
    }
    
    /* 搜尋結果資訊 */
    .search-result-info {
        background: #e7f3ff;
        padding: 15px 25px;
        margin-bottom: 25px;
        border-left: 5px solid #007bff;
        border-radius: 8px;
        color: #004085;
        font-size: 1.05rem;
        display: flex;
        align-items: center;
        gap: 12px;
    }

    /* 統計資訊 */
    .stats-info {
        background-color: #e2f0d9; /* 淺綠色背景 */
        color: #28a745; /* 綠色文字 */
        padding: 15px 25px;
        margin-bottom: 25px;
        border-radius: 8px;
        font-weight: 600;
        text-align: center;
        font-size: 1.1rem;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        box-shadow: 0 2px 10px rgba(40, 167, 69, 0.1);
    }

    /* 表格容器 */
    .table-responsive {
        overflow-x: auto; /* 橫向滾動 */
        width: 100%;
        border-radius: 10px;
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
        background-color: #ffffff;
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

    /* 狀態顯示優化 */
    .status-badge {
        display: inline-block;
        padding: 6px 12px;
        border-radius: 20px;
        font-weight: 600;
        font-size: 0.85rem;
        white-space: nowrap;
        text-align: center;
        min-width: 80px; /* 確保最小寬度 */
    }

    .status-badge.pending { /* 待處理 */
        background-color: #fff3e0;
        color: #ff9800;
        border: 1px solid #ffcc80;
    }

    .status-badge.processing { /* 處理中 */
        background-color: #e3f2fd;
        color: #2196F3;
        border: 1px solid #90caf9;
    }

    .status-badge.completed { /* 已完成 */
        background-color: #e8f5e9;
        color: #4CAF50;
        border: 1px solid #a5d6a7;
    }

    /* 操作區塊 */
    .action-cell {
        white-space: nowrap; /* 防止內容換行 */
        display: flex;
        align-items: center;
        gap: 10px; /* 下拉選單和按鈕間距 */
    }

    .action-cell select {
        padding: 8px 12px;
        border: 1px solid #ced4da;
        border-radius: 6px;
        font-size: 0.9rem;
        background-color: #ffffff;
        appearance: none; /* 移除預設箭頭 */
        background-image: url('data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>');
        background-repeat: no-repeat;
        background-position: right 8px center;
        background-size: 18px;
        cursor: pointer;
        transition: border-color 0.2s ease;
    }

    .action-cell select:focus {
        border-color: #007bff;
        outline: none;
    }

    .action-cell button {
        padding: 8px 15px;
        background-color: #28a745; /* 綠色更新按鈕 */
        color: white;
        border: none;
        border-radius: 6px;
        font-size: 0.9rem;
        cursor: pointer;
        transition: background-color 0.2s ease, transform 0.2s ease;
        display: inline-flex;
        align-items: center;
        gap: 5px;
    }

    .action-cell button:hover {
        background-color: #218838;
        transform: translateY(-1px);
    }

    /* 沒有資料的提示 */
    .no-data {
        text-align: center;
        padding: 3rem 1.5rem;
        color: #777;
        font-size: 1.2rem;
        background-color: #fefefe;
        border-radius: 10px;
        box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
        margin-top: 1rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 1rem;
    }
    
    .no-data::before {
        font-family: "Font Awesome 6 Free";
        content: "\f468"; /* 例如，一個盒子打開的圖標 */
        font-weight: 900;
        font-size: 3rem;
        color: #ccc;
        margin-bottom: 0.5rem;
    }

    /* 響應式調整 */
    @media (max-width: 992px) {
        .admin-container {
            margin: 2rem auto;
            padding: 2rem;
        }
        .admin-container h1 {
            font-size: 2.2rem;
        }
        .search-filter-card {
            flex-direction: column;
            align-items: stretch;
            gap: 20px;
        }
        .search-filter-form {
            flex-direction: column;
            align-items: stretch;
            gap: 10px;
        }
        .search-filter-form input[type="number"],
        .search-filter-form select,
        .search-filter-form button {
            width: 100%;
            min-width: unset;
        }
        table th, table td {
            padding: 12px;
            font-size: 0.9rem;
        }
        .status-badge {
             padding: 5px 10px;
             font-size: 0.8rem;
             min-width: 70px;
        }
        .action-cell {
            flex-direction: column;
            align-items: flex-start;
            gap: 8px;
        }
        .action-cell select,
        .action-cell button {
            width: 100%;
        }
    }

    @media (max-width: 768px) {
        .admin-container {
            margin: 1.5rem auto;
            padding: 1.5rem;
            border-radius: 10px;
        }
        .admin-container h1 {
            font-size: 2rem;
            gap: 10px;
        }
        .admin-container > p {
            font-size: 1rem;
            margin-bottom: 1.5rem;
        }
        .search-filter-card {
            padding: 15px 20px;
        }
        .search-filter-card h3 {
            font-size: 1.5rem;
            margin-bottom: 15px;
        }
        .search-result-info, .stats-info {
            padding: 12px 20px;
            font-size: 0.95rem;
        }
        .no-data {
            padding: 2rem 1rem;
            font-size: 1rem;
        }
        .no-data::before {
            font-size: 2.5rem;
        }
    }
</style>
</head>
<body>
    <%-- 引入 header.jsp，如果它存在且結構正確 --%>
  
    <div class="admin-container">
        <h1><i class="fas fa-tools"></i> 維修單管理系統</h1>
        <p>此處您可查看、搜尋及更新所有機台的維修單狀態。</p>

        <div class="search-filter-card">
            <h3><i class="fas fa-filter"></i> 查詢與篩選功能</h3>

            <form class="search-filter-form" action="${pageContext.request.contextPath}/SearchRepairByMachineServlet" method="get">
                <label for="searchMachineId"><i class="fas fa-barcode"></i> 按機台ID查詢：</label>
                <input type="number" id="searchMachineId" name="machineId" placeholder="輸入機台ID" min="1" value="${param.machineId != null ? param.machineId : ''}">
                <button type="submit"><i class="fas fa-search"></i> 查詢</button>
            </form>

            <form class="search-filter-form" action="${pageContext.request.contextPath}/SearchRepairByStatusServlet" method="get">
                <label for="filterStatus"><i class="fas fa-tags"></i> 按狀態篩選：</label>
                <select id="filterStatus" name="status">
                    <option value="" ${param.status == '' ? 'selected' : ''}>所有狀態</option>
                    <option value="待處理" ${param.status == '待處理' ? 'selected' : ''}>待處理</option>
                    <option value="處理中" ${param.status == '處理中' ? 'selected' : ''}>處理中</option>
                    <option value="已完成" ${param.status == '已完成' ? 'selected' : ''}>已完成</option>
                </select>
                <button type="submit"><i class="fas fa-filter"></i> 篩選</button>
            </form>

            <div class="search-filter-form">
                <a href="${pageContext.request.contextPath}/FindAllRepairServlet" class="search-filter-form button" style="background-color: #6c757d; color: white;">
                    <i class="fas fa-redo"></i> 清除篩選
                </a>
            </div>
        </div>

        <c:if test="${not empty searchType}">
            <div class="search-result-info">
                <i class="fas fa-info-circle"></i>
                <strong>搜尋結果：</strong>
                <c:choose>
                    <c:when test="${searchType == 'machineId'}">
                        按機台ID "<c:out value="${searchValue}" />" 查詢
                    </c:when>
                    <c:when test="${searchType == 'status'}">
                        按狀態 "<c:out value="${searchValue}" />" 篩選
                    </c:when>
                    <c:otherwise>
                        依據 "${searchType}" 查詢 "${searchValue}"
                    </c:otherwise>
                </c:choose>
                (共找到 <c:out value="${fn:length(repairList)}" /> 筆記錄)
            </div>
        </c:if>

        <div class="stats-info">
            <i class="fas fa-chart-pie"></i>
            總維修單：<c:out value="${fn:length(repairList)}" /> 筆
        </div>

        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th><i class="fas fa-receipt"></i> 維修單號</th>
                        <th><i class="fas fa-microchip"></i> 機台名稱 (ID)</th>
                        <th><i class="fas fa-exclamation-triangle"></i> 故障描述</th>
                        <th><i class="fas fa-sync-alt"></i> 狀態</th>
                        <th><i class="fas fa-clock"></i> 提交時間</th>
                        <th><i class="fas fa-user-tie"></i> 員工</th>
                        <th><i class="fas fa-cogs"></i> 操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty repairList}">
                            <tr>
                                <td colspan="7" class="no-data">
                                    <p><i class="fas fa-folder-open"></i> 查無維修記錄</p>
                                    <p><small>請嘗試其他查詢條件或稍後再試。</small></p>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="repair" items="${repairList}">
                                <tr>
                                    <td>#<c:out value="${repair.repairId}" /></td>
                                    <td><c:out value="${repair.machineName}" /> (ID:<c:out value="${repair.machineId}" />)</td>
                                    <td><c:out value="${fn:substring(repair.repairDescription, 0, 50)}" /><c:if test="${fn:length(repair.repairDescription) > 50}">...</c:if></td>
                                    <td>
                                        <span class="status-badge
                                            <c:choose>
                                                <c:when test='${repair.repairStatus == "待處理"}'>pending</c:when>
                                                <c:when test='${repair.repairStatus == "處理中"}'>processing</c:when>
                                                <c:when test='${repair.repairStatus == "已完成"}'>completed</c:when>
                                                <c:otherwise>other</c:otherwise>
                                            </c:choose>
                                        ">
                                            <c:out value="${repair.repairStatus}" />
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty repair.repairTime}">
                                                <%-- 修正日期解析模式以匹配 "2025-06-26T14:20" --%>
                                                <fmt:parseDate value="${repair.repairTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedRepairTime" />
                                                <fmt:formatDate value="${parsedRepairTime}" pattern="yyyy 年 MM 月 dd 日 HH:mm" />
                                            </c:when>
                                            <c:otherwise>
                                                N/A
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${repair.employeeId}" /></td>
                                    <td class="action-cell">
                                        <form method="post" action="${pageContext.request.contextPath}/UpdateRepairStatusServlet">
                                            <input type="hidden" name="repairId" value="${repair.repairId}">
                                            <select name="newStatus">
                                                <option value="待處理" ${repair.repairStatus == '待處理' ? 'selected' : ''}>待處理</option>
                                                <option value="處理中" ${repair.repairStatus == '處理中' ? 'selected' : ''}>處理中</option>
                                                <option value="已完成" ${repair.repairStatus == '已完成' ? 'selected' : ''}>已完成</option>
                                            </select>
                                            <button type="submit"><i class="fas fa-check"></i> 更新</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>