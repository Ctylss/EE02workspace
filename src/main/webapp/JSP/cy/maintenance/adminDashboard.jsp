<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
        <% request.setAttribute("pageTitle", "後台保養列表管理" ); %>
            <%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
                <%@ taglib prefix="c" uri="jakarta.tags.core" %>
                    <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

                        <!DOCTYPE html>
                        <html lang="zh-Hant">

                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>${pageTitle}</title>
                            <%-- 引入 Font Awesome 用於圖標 --%>
                                <link rel="stylesheet"
                                    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
                                <%-- 可選：引入 Google Fonts 提升字體美觀 --%>
                                    <link
                                        href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@300;400;500;700&display=swap"
                                        rel="stylesheet">
                                    <script src="${pageContext.request.contextPath}/sidebar.js"></script>

                                    <style>
                                        /* 基本樣式和字體設定 */
                                        body {
                                            font-family: 'Noto Sans TC', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                            background: linear-gradient(to right bottom, #e0f7fa, #b2ebf2);
                                            /* 柔和的藍綠漸變背景 */
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

                                        .dashboard-container h2 {
                                            font-size: 2.5rem;
                                            color: #2c3e50;
                                            margin-bottom: 0.8rem;
                                            text-align: center;
                                            display: flex;
                                            align-items: center;
                                            justify-content: center;
                                            gap: 15px;
                                        }

                                        .dashboard-container>p {
                                            /* 直接子元素p */
                                            font-size: 1.2rem;
                                            color: #555;
                                            margin-top: 0;
                                            margin-bottom: 2rem;
                                            text-align: center;
                                        }

                                        /* 主要操作按鈕區域 */
                                        .action-bar {
                                            display: flex;
                                            justify-content: space-between;
                                            /* 元素分散對齊 */
                                            align-items: center;
                                            margin-bottom: 2rem;
                                            flex-wrap: wrap;
                                            /* 允許換行 */
                                            gap: 1rem;
                                            /* 元素間距 */
                                            padding-bottom: 1rem;
                                            border-bottom: 1px solid #eee;
                                        }

                                        .action-bar .btn {
                                            display: inline-flex;
                                            align-items: center;
                                            gap: 8px;
                                            padding: 10px 20px;
                                            text-decoration: none;
                                            border-radius: 8px;
                                            font-weight: 500;
                                            transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
                                            border: none;
                                            cursor: pointer;
                                            white-space: nowrap;
                                            /* 防止按鈕文字換行 */
                                        }

                                        .action-bar .btn-primary {
                                            background-color: #007bff;
                                            color: white;
                                            box-shadow: 0 4px 10px rgba(0, 123, 255, 0.2);
                                        }

                                        .action-bar .btn-primary:hover {
                                            background-color: #0056b3;
                                            transform: translateY(-2px);
                                            box-shadow: 0 6px 15px rgba(0, 123, 255, 0.3);
                                        }

                                        .action-bar .btn-secondary {
                                            background-color: #6c757d;
                                            color: white;
                                            box-shadow: 0 4px 10px rgba(108, 117, 125, 0.2);
                                        }

                                        .action-bar .btn-secondary:hover {
                                            background-color: #5a6268;
                                            transform: translateY(-2px);
                                            box-shadow: 0 6px 15px rgba(108, 117, 125, 0.3);
                                        }

                                        /* 搜尋/篩選區域 (佔位符) */
                                        .search-filter-area {
                                            display: flex;
                                            gap: 1rem;
                                            flex-wrap: wrap;
                                            align-items: center;
                                        }

                                        .search-filter-area input[type="text"] {
                                            padding: 8px 12px;
                                            border: 1px solid #ced4da;
                                            border-radius: 8px;
                                            font-size: 0.95rem;
                                            transition: border-color 0.3s, box-shadow 0.3s;
                                        }

                                        .search-filter-area input[type="text"]:focus {
                                            border-color: #007bff;
                                            box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
                                            outline: none;
                                        }

                                        .search-filter-area button {
                                            padding: 8px 15px;
                                            border-radius: 8px;
                                            border: 1px solid #007bff;
                                            background-color: #007bff;
                                            color: white;
                                            cursor: pointer;
                                            transition: background-color 0.3s, border-color 0.3s;
                                        }

                                        .search-filter-area button:hover {
                                            background-color: #0056b3;
                                            border-color: #0056b3;
                                        }


                                        /* 表格樣式 */
                                        .table-responsive {
                                            overflow-x: auto;
                                            /* 橫向滾動 */
                                            width: 100%;
                                            border-radius: 10px;
                                            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
                                            background-color: #ffffff;
                                        }

                                        table {
                                            width: 100%;
                                            border-collapse: separate;
                                            /* 為了圓角和陰影 */
                                            border-spacing: 0;
                                        }

                                        table thead {
                                            background-color: #e9ecef;
                                            /* 表頭背景色 */
                                        }

                                        table th,
                                        table td {
                                            padding: 15px;
                                            text-align: left;
                                            border-bottom: 1px solid #dee2e6;
                                            /* 底部邊框 */
                                        }

                                        table th {
                                            color: #495057;
                                            font-weight: 600;
                                            font-size: 1.05rem;
                                            position: sticky;
                                            /* 粘性表頭 */
                                            top: 0;
                                            background-color: #e9ecef;
                                            z-index: 1;
                                        }

                                        table tbody tr:last-child td {
                                            border-bottom: none;
                                            /* 最後一行沒有底部邊框 */
                                        }

                                        table tbody tr:hover {
                                            background-color: #f8f9fa;
                                            /* 行懸停效果 */
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
                                            min-width: 80px;
                                            /* 確保最小寬度 */
                                        }

                                        .status-badge.pending {
                                            /* 待處理 */
                                            background-color: #fff3e0;
                                            color: #ff9800;
                                            border: 1px solid #ffcc80;
                                        }

                                        .status-badge.in-progress {
                                            /* 進行中 */
                                            background-color: #e3f2fd;
                                            color: #2196F3;
                                            border: 1px solid #90caf9;
                                        }

                                        .status-badge.completed {
                                            /* 已完成 */
                                            background-color: #e8f5e9;
                                            color: #4CAF50;
                                            border: 1px solid #a5d6a7;
                                        }

                                        .status-badge.other {
                                            /* 其他狀態 */
                                            background-color: #f0f0f0;
                                            color: #666;
                                            border: 1px solid #ccc;
                                        }


                                        /* 操作連結 */
                                        .action-links {
                                            white-space: nowrap;
                                            /* 防止按鈕換行 */
                                        }

                                        .action-links a {
                                            display: inline-flex;
                                            align-items: center;
                                            gap: 5px;
                                            padding: 6px 12px;
                                            margin-right: 8px;
                                            /* 按鈕間距 */
                                            border-radius: 6px;
                                            font-size: 0.85rem;
                                            text-decoration: none;
                                            transition: background-color 0.2s ease, transform 0.2s ease;
                                            border: 1px solid transparent;
                                            /* 預設透明邊框 */
                                        }

                                        .action-links a.detail-btn {
                                            background-color: #007bff;
                                            /* 主要藍 */
                                            color: white;
                                        }

                                        .action-links a.detail-btn:hover {
                                            background-color: #0056b3;
                                            transform: translateY(-1px);
                                        }

                                        .action-links a.edit-btn {
                                            background-color: #17a2b8;
                                            /* 信息藍 */
                                            color: white;
                                        }

                                        .action-links a.edit-btn:hover {
                                            background-color: #138496;
                                            transform: translateY(-1px);
                                        }

                                        .action-links a.delete-btn {
                                            background-color: #dc3545;
                                            /* 警告紅 */
                                            color: white;
                                        }

                                        .action-links a.delete-btn:hover {
                                            background-color: #c82333;
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
                                            /* 調整與表格的間距 */
                                            display: flex;
                                            flex-direction: column;
                                            align-items: center;
                                            gap: 1rem;
                                        }

                                        .no-data::before {
                                            /* 引入圖標 */
                                            font-family: "Font Awesome 6 Free";
                                            content: "\f468";
                                            /* 例如，一個盒子打開的圖標 */
                                            font-weight: 900;
                                            font-size: 3rem;
                                            color: #ccc;
                                            margin-bottom: 0.5rem;
                                        }

                                        /* 底部資訊 */
                                        .footer-info {
                                            text-align: center;
                                            margin-top: 2rem;
                                            color: #666;
                                            font-size: 0.95rem;
                                            padding-top: 1rem;
                                            border-top: 1px solid #eee;
                                        }

                                        /* 響應式調整 */
                                        @media (max-width: 992px) {
                                            .dashboard-container {
                                                margin: 2rem auto;
                                                padding: 2rem;
                                            }

                                            .dashboard-container h2 {
                                                font-size: 2rem;
                                            }

                                            .action-bar {
                                                flex-direction: column;
                                                align-items: stretch;
                                                gap: 1rem;
                                            }

                                            .action-bar .btn {
                                                width: 100%;
                                                justify-content: center;
                                            }

                                            .search-filter-area {
                                                flex-direction: column;
                                                align-items: stretch;
                                                width: 100%;
                                            }

                                            .search-filter-area input {
                                                width: 100%;
                                                box-sizing: border-box;
                                            }

                                            table th,
                                            table td {
                                                padding: 12px;
                                                font-size: 0.9rem;
                                            }

                                            .action-links a {
                                                padding: 5px 10px;
                                                margin-right: 6px;
                                                font-size: 0.8rem;
                                            }

                                            .status-badge {
                                                padding: 5px 10px;
                                                font-size: 0.8rem;
                                                min-width: 70px;
                                            }
                                        }

                                        @media (max-width: 768px) {
                                            .dashboard-container {
                                                margin: 1.5rem auto;
                                                padding: 1.5rem;
                                                border-radius: 10px;
                                            }

                                            .dashboard-container h2 {
                                                font-size: 1.8rem;
                                                gap: 10px;
                                            }

                                            .dashboard-container>p {
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
                            <%-- 這裡假設您的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>

                                <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
                                    <%-- <%@ include file="../sidebar.jsp" %> --%>

                                        <div class="dashboard-container">
                                            <h2><i class="fas fa-tools"></i> 後台保養列表管理</h2>
                                            <p>在此管理所有機台的保養排程和狀態。</p>

                                            <div class="action-bar">
                                                <a href="${pageContext.request.contextPath}/AdminInsertMaintenanceServlet"
                                                    class="btn btn-primary">
                                                    <i class="fas fa-plus-circle"></i> 新增保養排程
                                                </a>
                                                <%-- 搜尋/篩選區域 (未來可擴展) --%>
                                                    <div class="search-filter-area">
                                                        <input type="text" placeholder="搜尋機台ID或狀態..." />
                                                        <button><i class="fas fa-search"></i> 搜尋</button>
                                                    </div>
                                            </div>

                                            <div class="table-responsive">
                                                <table>
                                                    <thead>
                                                        <tr>
                                                            <th><i class="fas fa-clipboard-list"></i> 保養編號</th>
                                                            <th><i class="fas fa-microchip"></i> 機台編號</th>
                                                            <%-- 更多欄位，例如：預計保養日期，實際完成日期等 --%>
                                                                <th><i class="fas fa-sync-alt"></i> 保養狀態</th>
                                                                <th><i class="fas fa-cogs"></i> 操作</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="maintenance" items="${maintenanceList}">
                                                            <tr>
                                                                <td>#
                                                                    <c:out value="${maintenance.scheduleId}" />
                                                                </td>
                                                                <td>
                                                                    <c:out value="${maintenance.machineId}" />
                                                                </td>
                                                                <%-- 更多欄位資料 --%>
                                                                    <td>
                                                                        <span class="status-badge
                                    <c:choose>
                                        <c:when test='${maintenance.maintenanceStatus == " 待處理"}'>pending</c:when>
                                                                            <c:when
                                                                                test='${maintenance.maintenanceStatus == "進行中"}'>
                                                                                in-progress</c:when>
                                                                            <c:when
                                                                                test='${maintenance.maintenanceStatus == "已完成"}'>
                                                                                completed</c:when>
                                                                            <c:otherwise>other</c:otherwise>
                                                                            </c:choose>
                                                                            ">
                                                                            <c:out
                                                                                value="${maintenance.maintenanceStatus}" />
                                                                        </span>
                                                                    </td>
                                                                    <td class="action-links">
                                                                        <a href="${pageContext.request.contextPath}/AdminMaintenanceDetailServlet?scheduleId=${maintenance.scheduleId}"
                                                                            class="btn detail-btn">
                                                                            <i class="fas fa-info-circle"></i> 詳情
                                                                        </a>
                                                                        <a href="${pageContext.request.contextPath}/AdminUpdateMaintenanceServlet?scheduleId=${maintenance.scheduleId}"
                                                                            class="btn edit-btn">
                                                                            <i class="fas fa-edit"></i> 編輯
                                                                        </a>
                                                                        <a href="${pageContext.request.contextPath}/DeleteMaintenanceServlet?scheduleId=${maintenance.scheduleId}"
                                                                            class="btn delete-btn"
                                                                            onclick="return confirm('確定要刪除保養編號 ${maintenance.scheduleId} 的資料嗎？這個操作無法復原。');">
                                                                            <i class="fas fa-trash-alt"></i> 刪除
                                                                        </a>
                                                                    </td>
                                                            </tr>
                                                        </c:forEach>
                                                        <c:if test="${empty maintenanceList}">
                                                            <tr>
                                                                <td colspan="4" class="no-data">
                                                                    <p>目前沒有保養資料。請點擊上方「新增保養排程」按鈕新增資料。</p>
                                                                    <a href="${pageContext.request.contextPath}/AdminInsertMaintenanceServlet"
                                                                        class="btn btn-primary" style="width: auto;">
                                                                        <i class="fas fa-plus-circle"></i> 新增第一筆保養
                                                                    </a>
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                    </tbody>
                                                </table>
                                            </div>

                                            <%-- 底部資訊 --%>
                                                <c:if test="${not empty maintenanceList}">
                                                    <div class="footer-info">
                                                        <i class="fas fa-info-circle"></i> 共顯示
                                                        <c:out value="${maintenanceList.size()}" /> 筆保養資料。
                                                    </div>
                                                </c:if>
                                        </div>
                        </body>

                        </html>