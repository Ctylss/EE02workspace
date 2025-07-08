<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8" />
    <%-- 移除 width=device-width, initial-scale=1 以避免響應式縮放 --%>
    <meta name="viewport" content="width=1024" /> <title><%= request.getAttribute("pageTitle") != null ? request.getAttribute("pageTitle") : "工業管理系統" %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
</head>
<body>
    <%-- 在這裡聲明 userRole 並設置為 Request 屬性，使其在所有包含的頁面中都可用 --%>
    <%
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null) {
            userRole = "guest";
        }
        // 將 userRole 設置為 Request 屬性，以便 common/sidebar.jsp 可以訪問
        // 注意：這裡的 userRole 是 dashboard.jsp 的局部變量，設置為 Request 屬性後，
        // 被 <jsp:include> 包含的頁面可以安全地從 Request 中獲取。
        request.setAttribute("userRole", userRole);
    %>

    <div class="app-wrapper">
        <aside class="main-sidebar" role="complementary" aria-label="側邊欄">
            <%-- 將指令包含 (<%@ include %>) 改為動作標籤包含 (<jsp:include>) --%>
            <%-- 使用絕對路徑從 Web Context Root 開始 --%>
            <jsp:include page="/common/sidebar.jsp" />
        </aside>

        <div class="main-right-content-wrapper">
            <header class="main-header">
                <%-- 修正 header.jsp 的包含路徑，使用 jsp:include --%>
                <jsp:include page="/common/header.jsp" />
            </header>

            <div class="actual-page-content">
                <main class="main-content">
                    <div class="content-header">
                        <h1>歡迎, <%= session.getAttribute("loggedInUser") != null ? session.getAttribute("loggedInUser") : "訪客" %>!</h1>
                        <p>您已成功登入工業管理系統儀表板。</p>
                        <p>在這裡您可以查看和管理各項工業數據。</p>
                        <%-- 這裡直接使用 userRole，它現在是從 Request 屬性獲得的 --%>
                        <p class="user-role">您的角色是：<strong><%= userRole.substring(0, 1).toUpperCase() + userRole.substring(1) %></strong></p>
                    </div>

                    <div class="dashboard-grid">
                        <div class="card dashboard-card production-orders">
                            <div class="card-icon"><i class="fas fa-clipboard-list"></i></div>
                            <div class="card-content">
                                <h2 class="card-title">待處理生產工單</h2>
                                <p class="card-value">10 筆待處理工單</p>
                                <a href="<%= request.getContextPath() %>/production/workOrderList.jsp" class="card-link">查看詳情 <i class="fas fa-arrow-right"></i></a>
                            </div>
                        </div>

                        <div class="card dashboard-card purchase-orders">
                            <div class="card-icon"><i class="fas fa-shopping-cart"></i></div>
                            <div class="card-content">
                                <h2 class="card-title">待處理採購訂單</h2>
                                <p class="card-value">5 筆待確認訂單</p>
                                <a href="<%= request.getContextPath() %>/purchase/purchaseOrderList.jsp" class="card-link">查看詳情 <i class="fas fa-arrow-right"></i></a>
                            </div>
                        </div>

                        <div class="card dashboard-card equipment-alerts">
                            <div class="card-icon"><i class="fas fa-exclamation-triangle"></i></div>
                            <div class="card-content">
                                <h2 class="card-title">設備異常報告</h2>
                                <p class="card-value">2 筆新異常報告</p>
                                <a href="<%= request.getContextPath() %>/maintenance/repairRecordList.jsp" class="card-link">查看詳情 <i class="fas fa-arrow-right"></i></a>
                            </div>
                        </div>

                        <div class="card dashboard-card production-progress">
                            <div class="card-icon"><i class="fas fa-chart-line"></i></div>
                            <div class="card-content">
                                <h2 class="card-title">今日生產進度</h2>
                                <p class="card-value">85% 完成</p>
                                <div class="progress-bar-container">
                                    <div class="progress-bar" style="width: 85%;"></div>
                                </div>
                                <a href="<%= request.getContextPath() %>/production/productionScheduleDetail.jsp" class="card-link">查看詳情 <i class="fas fa-arrow-right"></i></a>
                            </div>
                        </div>

                        <div class="card dashboard-card inventory-warning">
                            <div class="card-icon"><i class="fas fa-boxes"></i></div>
                            <div class="card-content">
                                <h2 class="card-title">庫存預警</h2>
                                <p class="card-value">3 項物料低於安全庫存</p>
                                <a href="<%= request.getContextPath() %>/inventory/materialList.jsp" class="card-link">查看詳情 <i class="fas fa-arrow-right"></i></a>
                            </div>
                        </div>
                    </div>
                </main>
            </div>

            <footer class="main-footer">
                <%-- 修正 footer.jsp 的包含路徑，使用 jsp:include --%>
                <jsp:include page="/common/footer.jsp" />
            </footer>
        </div>
    </div>

    <%-- 如果您不希望任何側邊欄切換功能，可以移除 sidebar.js。
        如果希望保留手動切換功能，請確保 sidebar.js 沒有根據螢幕寬度自動切換的邏輯。 --%>
    <script src="<%= request.getContextPath() %>../JS/sidebar.js"></script>
</body>
</html>
