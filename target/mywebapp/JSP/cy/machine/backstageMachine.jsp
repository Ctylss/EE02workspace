<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>機台管理系統 - 後台管理</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

    <style>
        /* 通用樣式 */
        body {
            font-family: 'Inter', sans-serif; /* 使用 Inter 字體 */
            margin: 0;
            padding: 0;
            background-color: #f4f7f6; /* 淺灰色背景 */
            color: #333;
            display: flex; /* 使用 Flexbox 佈局 */
            min-height: 100vh; /* 確保頁面佔據整個視窗高度 */
        }

        .app-wrapper {
            display: flex;
            width: 100%;
        }

        /* 側邊欄樣式 (如果 sidebar.jsp 沒有內嵌 CSS，請取消註釋以下內容) */
        .main-sidebar {
            width: 250px; /* 固定寬度 */
            background-color: #2c3e50; /* 深藍色 */
            color: #ecf0f1;
            padding: 20px 0;
            box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            border-top-right-radius: 15px; /* 圓角 */
            border-bottom-right-radius: 15px; /* 圓角 */
            height: 100vh; /* 確保側邊欄佔據整個視窗高度 */
            position: sticky; /* 黏在頂部 */
            top: 0;
            left: 0;
        }

        .sidebar-brand {
            text-align: center;
            margin-bottom: 30px;
            padding: 0 15px;
        }

        .brand-link {
            display: flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
            color: #ecf0f1;
            font-size: 1.5em;
            font-weight: bold;
            padding: 10px 0;
            background-color: #34495e; /* 品牌背景色 */
            border-radius: 8px; /* 圓角 */
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            transition: background-color 0.3s ease;
        }

        .brand-link:hover {
            background-color: #4a667b;
        }

        .brand-link i {
            margin-right: 10px;
            font-size: 1.8em;
            color: #1abc9c; /* 品牌圖標顏色 */
        }

        .sidebar-nav {
            flex-grow: 1; /* 佔據剩餘空間 */
        }

        .sidebar-menu {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .menu-item {
            position: relative;
            margin-bottom: 5px;
        }

        .menu-link {
            display: flex;
            align-items: center;
            padding: 12px 20px;
            color: #ecf0f1;
            text-decoration: none;
            font-size: 1em;
            transition: background-color 0.3s ease, color 0.3s ease;
            border-radius: 8px; /* 圓角 */
            margin: 0 10px;
        }

        .menu-link:hover, .menu-link.active {
            background-color: #1abc9c; /* 活躍和懸停顏色 */
            color: #fff;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
        }

        .menu-link i {
            margin-right: 15px;
            font-size: 1.2em;
        }

        .submenu-always-open, .nested-submenu-always-open {
            list-style: none;
            padding: 0;
            margin-top: 5px;
            background-color: #34495e; /* 子菜單背景色 */
            border-radius: 8px; /* 圓角 */
            margin: 5px 15px 10px 15px;
            box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        .submenu-always-open li, .nested-submenu-always-open li {
            margin: 0;
        }

        .submenu-link, .nested-submenu-link {
            display: block;
            padding: 10px 25px; /* 內縮一些 */
            color: #bdc3c7; /* 子菜單文字顏色 */
            text-decoration: none;
            font-size: 0.95em;
            transition: background-color 0.3s ease, color 0.3s ease;
            border-radius: 6px; /* 圓角 */
            margin: 0 5px;
        }

        .submenu-link:hover, .nested-submenu-link:hover {
            background-color: #2ecc71; /* 懸停時的亮綠色 */
            color: #fff;
        }


        /* 主內容區域樣式 */
        .main-right-content-wrapper {
            flex-grow: 1;
            display: flex;
            flex-direction: column;
        }

        .main-header {
            background-color: #fff;
            padding: 15px 30px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: sticky; /* 讓 header 黏在頂部 */
            top: 0;
            z-index: 1000;
            border-bottom-left-radius: 15px; /* 圓角 */
            border-bottom-right-radius: 15px; /* 圓角 */
            margin: 10px 10px 0 10px;
        }

        .header-left h1 {
            margin: 0;
            font-size: 1.5em;
            color: #2c3e50;
        }

        .header-right .user-info {
            display: flex;
            align-items: center;
            font-size: 1em;
            color: #555;
        }

        .header-right .user-info i {
            margin-right: 8px;
            color: #2980b9; /* 用戶圖標顏色 */
        }

        .content {
            flex-grow: 1;
            padding: 20px;
            overflow-y: auto; /* 允許內容區域滾動 */
            margin: 10px; /* 與 Header 保持間距 */
            background-color: #ffffff; /* 內容區背景色 */
            border-radius: 15px; /* 圓角 */
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05); /* 輕微陰影 */
        }

        .container {
            max-width: 1200px; /* 擴大容器寬度 */
            margin: 20px auto; /* 居中並留有上下間距 */
            padding: 30px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
        }

        h1, h3 {
            color: #2c3e50;
            margin-bottom: 25px;
        }

        /* 管理員專用標頭 */
        .admin-header {
            background-color: #e74c3c; /* 紅色，表示管理員模式 */
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .admin-header h1 {
            margin: 0;
            font-size: 1.8em;
            display: flex;
            align-items: center;
        }

        .admin-header h1 i {
            margin-right: 10px;
        }

        .admin-badge {
            background-color: #c0392b;
            padding: 8px 15px;
            border-radius: 20px;
            font-weight: bold;
            font-size: 0.9em;
        }

        /* 訊息提示 */
        .message {
            padding: 12px 20px;
            margin-bottom: 20px;
            border-radius: 8px;
            font-weight: bold;
            text-align: center;
            animation: fadeIn 0.5s ease-out;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .message.success {
            background-color: #d4edda; /* 綠色背景 */
            color: #155724; /* 深綠文字 */
            border: 1px solid #c3e6cb;
        }

        .message.error {
            background-color: #f8d7da; /* 紅色背景 */
            color: #721c24; /* 深紅文字 */
            border: 1px solid #f5c6cb;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        /* 控制區域 */
        .admin-controls {
            background-color: #ecf0f1;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: inset 0 2px 5px rgba(0, 0, 0, 0.05);
            display: flex;
            flex-direction: column;
            gap: 20px; /* 控制行之間的間距 */
        }

        .control-row {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            align-items: center;
        }

        .search-area input[type="text"],
        .search-area select {
            flex: 1; /* 讓輸入框和選擇框佔用更多空間 */
            padding: 10px 15px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 0.95em;
            box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
            transition: border-color 0.3s ease;
            min-width: 180px; /* 最小寬度 */
        }

        .search-area input[type="text"]:focus,
        .search-area select:focus {
            border-color: #3498db;
            outline: none;
            box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.2);
        }

        /* 按鈕樣式 */
        .btn {
            display: inline-flex;
            align-items: center;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 0.95em;
            font-weight: bold;
            text-decoration: none;
            transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
            color: white; /* 確保文字顏色為白色 */
        }

        .btn-primary {
            background-color: #3498db; /* 藍色 */
        }
        .btn-primary:hover {
            background-color: #2980b9;
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
        }

        .btn-success {
            background-color: #2ecc71; /* 綠色 */
        }
        .btn-success:hover {
            background-color: #27ae60;
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
        }

        .btn:active {
            transform: translateY(0);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        /* 表格樣式 */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 25px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
            border-radius: 10px; /* 圓角 */
            overflow: hidden; /* 確保圓角生效 */
        }

        table thead {
            background-color: #34495e; /* 表頭深藍色 */
            color: white;
            font-weight: bold;
        }

        table th, table td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #ecf0f1; /* 淺色分割線 */
        }

        table th {
            font-size: 1.1em;
            text-transform: uppercase;
        }

        table tbody tr:nth-child(even) {
            background-color: #f9f9f9; /* 斑馬紋效果 */
        }

        table tbody tr:hover {
            background-color: #e8f4f8; /* 懸停效果 */
            transition: background-color 0.3s ease;
        }

        .status {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 20px;
            font-weight: bold;
            font-size: 0.85em;
            text-shadow: 0 1px 1px rgba(0,0,0,0.1);
        }
        .status.running {
            background-color: #e6ffe6;
            color: #28a745; /* 綠色 */
            border: 1px solid #28a745;
        }
        .status.maintenance {
            background-color: #fffacd;
            color: #ffc107; /* 黃色 */
            border: 1px solid #ffc107;
        }
        .status.stopped {
            background-color: #ffe6e6;
            color: #dc3545; /* 紅色 */
            border: 1px solid #dc3545;
        }

        .empty-message {
            text-align: center;
            padding: 40px 20px;
            font-size: 1.2em;
            color: #7f8c8d;
            background-color: #fdfdfd;
            border-radius: 10px;
            margin-top: 20px;
            box-shadow: inset 0 1px 5px rgba(0, 0, 0, 0.05);
        }

        .empty-message small {
            display: block;
            margin-top: 10px;
            font-size: 0.8em;
            color: #95a5a6;
        }

        .action-links a {
            margin-right: 10px;
            text-decoration: none;
            font-weight: bold;
            transition: color 0.3s ease;
        }
        .action-links .edit {
            color: #3498db; /* 藍色 */
        }
        .action-links .edit:hover {
            color: #2980b9;
        }
        .action-links .delete {
            color: #e74c3c; /* 紅色 */
        }
        .action-links .delete:hover {
            color: #c0392b;
        }

        /* 頁腳樣式 */
        .main-footer {
            background-color: #2c3e50;
            color: #ecf0f1;
            padding: 20px 30px;
            text-align: center;
            box-shadow: 0 -2px 6px rgba(0, 0, 0, 0.1);
            border-top-left-radius: 15px;
            border-top-right-radius: 15px;
            margin: 10px 10px 0 10px; /* 與實際內容區保持間距 */
        }

        .footer-content {
            font-size: 0.9em;
        }


        /* 響應式設計 */
        @media (max-width: 992px) {
            .container {
                padding: 20px;
                margin: 10px auto;
            }
            table th, table td {
                padding: 12px;
                font-size: 0.9em;
            }
            .admin-header {
                flex-direction: column;
                align-items: flex-start;
                padding: 15px 20px;
            }
            .admin-badge {
                margin-top: 10px;
            }
            .search-area form {
                flex-direction: column;
            }
            .search-area input, .search-area select, .search-area button {
                width: 100%;
                max-width: none;
            }
            .main-sidebar {
                width: 100%;
                height: auto;
                border-radius: 0;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
                margin-bottom: 10px;
                position: relative;
            }
            .main-header, .main-footer, .content {
                 border-radius: 0;
                 margin: 0;
            }
        }
        @media (max-width: 768px) {
            body {
                flex-direction: column;
            }
            .admin-header h1 {
                font-size: 1.4em;
            }
            .btn {
                padding: 8px 15px;
                font-size: 0.9em;
            }
            .action-links a {
                display: block;
                margin-bottom: 5px;
            }
        }
    </style>
</head>
<body>
    <%-- 從 Session 中獲取 userRole，並設置為 Request 屬性，以供包含的 JSP 使用 --%>
    <%
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null) {
            userRole = "guest"; // 設定預設角色，以防未登入
        }
        // 將 userRole 設置為 Request 屬性，以便 common/sidebar.jsp 可以訪問
        request.setAttribute("userRole", userRole);

        // 如果需要在此處限制頁面訪問權限，可以添加類似 manageUsers.jsp 中的檢查
        // if (loggedInUser == null || (!"admin".equals(userRole) && !"machine".equals(userRole))) {
        //     response.sendRedirect(request.getContextPath() + "/login.jsp");
        //     return;
        // }
    %>

    <div class="app-wrapper">
        <aside class="main-sidebar" role="complementary" aria-label="側邊欄">
            <%-- 使用 <jsp:include> 動態包含側邊欄，這是推薦的方式 --%>
            <jsp:include page="/common/sidebar.jsp" />
        </aside>

        <div class="main-right-content-wrapper">
            <header class="main-header">
                <%-- 使用 <jsp:include> 動態包含頭部，這是推薦的方式 --%>
               
            </header>

            <div class="content">
                <!-- 管理員專用標頭 -->
                <div class="admin-header">
                    <h1>🛠️ 機台管理系統</h1>
                    <div class="admin-badge">👨‍💼 管理員模式</div>
                </div>

                <div class="container">
                    <!-- 顯示成功或錯誤訊息 -->
                    <c:if test="${not empty param.success}">
                        <div class="message success">
                            <c:choose>
                                <c:when test="${param.success == 'insert'}">✅ 新增機台成功！</c:when>
                                <c:when test="${param.success == 'update'}">✅ 機台資料更新成功！</c:when>
                                <c:when test="${param.success == 'delete'}">✅ 機台刪除成功！</c:when>
                            </c:choose>
                        </div>
                    </c:if>

                    <c:if test="${not empty param.error}">
                        <div class="message error">❌ ${param.error}</div>
                    </c:if>

                    <c:if test="${not empty error}">
                        <div class="message error">❌ ${error}</div>
                    </c:if>

                    <!-- 管理員控制區域 -->
                    <div class="admin-controls">
                        <h3 style="margin-bottom: 15px; color: #e74c3c;">🔧 管理員操作</h3>

                        <!-- 搜尋功能 -->
                        <div class="control-row search-area">
                            <form method="get" action="${pageContext.request.contextPath}/backstage" style="display: flex; gap: 15px; flex-wrap: wrap; width: 100%;">
                                <input type="text" name="search" placeholder="🔍 搜尋機台名稱或出廠編號..." value="${param.search}">
                                <select name="statusFilter">
                                    <option value="">📋 所有狀態</option>
                                    <option value="運行中" ${param.statusFilter == '運行中' ? 'selected' : ''}>🟢 運行中</option>
                                    <option value="維護中" ${param.statusFilter == '維護中' ? 'selected' : ''}>🟡 維護中</option>
                                    <option value="停機" ${param.statusFilter == '停機' ? 'selected' : ''}>🔴 停機</option>
                                </select>
                                <button type="submit" class="btn btn-primary">搜尋</button>
                            </form>
                        </div>

                        <!-- 管理按鈕 -->
                        <div class="control-row">
                            <a href="${pageContext.request.contextPath}/GoInsertMachineServlet" class="btn btn-success">➕ 新增機台</a>
                        </div>
                    </div>

                    <!-- 機台列表 -->
                    <h3 style="margin-bottom: 20px; color: #2c3e50;">📋 機台管理列表</h3>
                    <div style="overflow-x: auto;">
                        <table>
                            <thead>
                                <tr>
                                    <th>機台ID</th>
                                    <th>機台名稱</th>
                                    <th>出廠編號</th>
                                    <th>運行狀態</th>
                                    <th>機台位置</th>
                                    <th>管理操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty machines}">
                                        <tr>
                                            <td colspan="6" class="empty-message">
                                                📂 沒有找到符合條件的機台資料<br>
                                                <small>請調整搜尋條件或新增機台資料</small>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="machine" items="${machines}">
                                            <tr>
                                                <td><strong>#${machine.machineId}</strong></td>
                                                <td>${machine.machineName}</td>
                                                <td><code>${machine.serialNumber}</code></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${machine.mstatus == '運行中'}">
                                                            <span class="status running">🟢 ${machine.mstatus}</span>
                                                        </c:when>
                                                        <c:when test="${machine.mstatus == '維護中'}">
                                                            <span class="status maintenance">🟡 ${machine.mstatus}</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="status stopped">🔴 ${machine.mstatus}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>📍 ${machine.machineLocation}</td>
                                                <td class="action-links">
                                                    <a href="${pageContext.request.contextPath}/GoUpdateMachineServlet?machineId=${machine.machineId}" class="edit">✏️ 編輯</a>
                                                    <a href="javascript:void(0)" class="delete" onclick="confirmDelete(${machine.machineId}, '${fn:escapeXml(machine.machineName)}')">🗑️ 刪除</a>
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

            <footer class="main-footer">
                <%-- 使用 <jsp:include> 動態包含頁腳 --%>
                <jsp:include page="/common/footer.jsp" />
            </footer>
        </div>
    </div>

    <script>
        // 刪除確認
        function confirmDelete(machineId, machineName) {
            // 使用自定義的 confirm 彈窗，避免 alert/confirm 阻塞問題
            // 這裡為了快速測試先用瀏覽器內建的 confirm，生產環境建議替換為美觀的模態框
            if (window.confirm('⚠️ 管理員確認\\n\\n確定要刪除機台 "' + machineName + '" 嗎？\\n此操作無法復原！')) {
                window.location.href = '${pageContext.request.contextPath}/ConfirmDeleteMachineServlet?machineId=' + machineId;
            }
        }

        // 自動清除 URL 參數 (例如成功/錯誤訊息)
        window.onload = function() {
            if (window.location.search.includes('success=') || window.location.search.includes('error=')) {
                setTimeout(function() {
                    history.replaceState(null, '', window.location.pathname);
                }, 3000);
            }
        }
    </script>
    <%-- 如果您有需要，可以繼續在最底部包含額外的 JS 檔案 --%>
    <%-- 例如：<script src="<%= request.getContextPath() %>/js/sidebar.js"></script> --%>
</body>
</html>
