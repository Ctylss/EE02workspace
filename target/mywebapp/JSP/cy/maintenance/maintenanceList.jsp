<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "保養總表"); %>
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
<%-- 如果有sidebar.js或其他共用JS，請在此處引入 --%>
<%-- <script src="${pageContext.request.contextPath}/sidebar.js"></script> --%>

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

    .status-badge.scheduled { /* 已排程 */
        background-color: #e3f2fd;
        color: #2196F3;
        border: 1px solid #90caf9;
    }

    .status-badge.in-progress { /* 進行中 */
        background-color: #fffde7;
        color: #FFC107;
        border: 1px solid #ffe082;
    }

    .status-badge.completed { /* 已完成 */
        background-color: #e8f5e9;
        color: #4CAF50;
        border: 1px solid #a5d6a7;
    }

    .status-badge.cancelled { /* 已取消 */
        background-color: #ffebee;
        color: #F44336;
        border: 1px solid #ef9a9a;
    }

    /* 詳細資料連結 */
    .detail-link {
        display: inline-flex;
        align-items: center;
        gap: 5px;
        padding: 8px 12px;
        background-color: #17a2b8; /* 信息藍 */
        color: white;
        text-decoration: none;
        border-radius: 6px;
        font-size: 0.9rem;
        transition: background-color 0.2s ease, transform 0.2s ease;
    }

    .detail-link:hover {
        background-color: #138496;
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
        table th, table td {
            padding: 12px;
            font-size: 0.9rem;
        }
        .status-badge {
             padding: 5px 10px;
             font-size: 0.8rem;
             min-width: 70px;
        }
        .detail-link {
            padding: 7px 10px;
            font-size: 0.85rem;
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
    <%-- 這裡假設您的 header.jsp 已經包含了必要的 HTML 結構 --%>


    <div class="admin-container">
        <h1><i class="fas fa-calendar-check"></i> 保養總表</h1>
        <p>此處顯示所有機台的保養排程與狀態概覽。</p>

        <h2 style="text-align:center; color: #2c3e50; margin-top: 30px; margin-bottom: 20px;"><i class="fas fa-list-alt"></i> 保養紀錄列表</h2>
        
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th><i class="fas fa-id-card"></i> 保養單編號</th>
                        <th><i class="fas fa-microchip"></i> 機台編號</th>
                        <th><i class="fas fa-sync-alt"></i> 保養狀態</th>
                        <th><i class="fas fa-calendar-alt"></i> 保養時間</th>
                        <th><i class="fas fa-info-circle"></i> 詳細資料</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty maintenanceList}">
                            <tr>
                                <td colspan="5" class="no-data">
                                    <p><i class="fas fa-folder-open"></i> 目前沒有任何保養紀錄</p>
                                    <p><small>請稍後再試或新增保養排程。</small></p>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="maintenance" items="${maintenanceList}">
                                <tr>
                                    <td>#<c:out value="${maintenance.scheduleId}" /></td>
                                    <td><c:out value="${maintenance.machineId}" /></td>
                                    <td>
                                        <span class="status-badge
                                            <c:choose>
                                                <c:when test='${maintenance.maintenanceStatus == "已排程"}'>scheduled</c:when>
                                                <c:when test='${maintenance.maintenanceStatus == "進行中"}'>in-progress</c:when>
                                                <c:when test='${maintenance.maintenanceStatus == "已完成"}'>completed</c:when>
                                                <c:when test='${maintenance.maintenanceStatus == "已取消"}'>cancelled</c:when>
                                                <c:otherwise>other</c:otherwise>
                                            </c:choose>
                                        ">
                                            <c:out value="${maintenance.maintenanceStatus}" />
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty maintenance.scheduleDate}">
                                                <fmt:parseDate value="${maintenance.scheduleDate}" pattern="yyyy-MM-dd" var="parsedDate" />
                                                <fmt:formatDate value="${parsedDate}" pattern="yyyy 年 MM 月 dd 日" />
                                            </c:when>
                                            <c:otherwise>
                                                N/A
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/MaintenanceDetailServlet?scheduleId=${maintenance.scheduleId}" class="detail-link">
                                            <i class="fas fa-eye"></i> 查看
                                        </a>
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