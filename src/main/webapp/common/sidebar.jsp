<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--
    userRole 變數應由包含此頁面的父頁面 (例如 dashboard.jsp 或 manageUsers.jsp)
    從 Session 獲取並設置為 Request 屬性。
    由於此頁面是通過 <jsp:include> 包含的，它不會直接繼承父頁面的局部變量，
    因此需要在此處從 Request 屬性中明確獲取 userRole。
--%>
<%
    String userRole = (String) request.getAttribute("userRole"); // <-- 確保這一行存在且正確
    // 如果 Request 中沒有 userRole (例如未經登入直接訪問，或父頁面未設置)，設置一個預設值以防止 NullPointerException
    if (userRole == null) {
        userRole = "guest"; // 設定為預設值，確保後續的 .equals() 比較安全
    }
%>

<style>
    /* 側邊欄整體樣式 */
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
        /* 為確保在主頁面佈局中正確顯示，可能需要以下定位或 flexbox 設置 */
        height: 100vh; /* 讓側邊欄佔據整個視窗高度 */
        position: sticky; /* 黏在頂部 */
        top: 0;
        left: 0;
    }

    /* 品牌區塊樣式 */
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

    /* 導航菜單樣式 */
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

    /* 子菜單樣式 */
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

    /* 響應式設計 */
    @media (max-width: 768px) {
        .main-sidebar {
            width: 100%;
            height: auto;
            border-radius: 0;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            margin-bottom: 10px;
            position: relative; /* 在小螢幕上不再黏在頂部 */
        }
    }
</style>

<div class="sidebar-brand">
    <a href="<%= request.getContextPath() %>/dashboard/dashboard.jsp" class="brand-link">
        <i class="fas fa-industry"></i> <span>MES System</span>
    </a>
</div>

<nav class="sidebar-nav" role="navigation" aria-label="側邊欄選單">
    <ul class="sidebar-menu">
        <li class="menu-item">
            <a href="<%= request.getContextPath() %>/dashboard/dashboard.jsp" class="menu-link active">
                <i class="fas fa-tachometer-alt"></i> <span>儀表板</span>
            </a>
        </li>

        <%-- 使用已從 Request 獲取的 userRole --%>
        <% if ("supplier".equals(userRole) || "admin".equals(userRole)) { %>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-truck-loading"></i> <span>供應商與採購</span>
            </a>
             <ul class="submenu-always-open">
                <li class="nested-submenu-item">
                    <a href="#" class="submenu-link">供應商管理</a>
                    <ul class="nested-submenu-always-open">
                        <li><a href="<%= request.getContextPath() %>/OrderAddFormServlet" class="submenu-link">新增訂單</a></li>
                        <li><a href="<%= request.getContextPath() %>/SupplierListServlet" class="submenu-link">A</a></li>
                        <li><a href="<%= request.getContextPath() %>/OrderListServlet" class="submenu-link">B</a></li>
                    </ul>
                </li>
             </ul>
        </li>
        <% } %>

        <% if ("machine".equals(userRole) || "admin".equals(userRole)) { %>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-cogs"></i> <span>設備管理</span>
            </a>
            <ul class="submenu-always-open">
                <li class="nested-submenu-item">
                    <a href="#" class="submenu-link">機台系統</a>
                    <ul class="nested-submenu-always-open">
                        <li><a href="<%= request.getContextPath() %>/backstage" class="submenu-link">機台查詢 (前台頁面)</a></li>
                        <li><a href="<%= request.getContextPath() %>/frontend" class="submenu-link">機台管理 (後台頁面)</a></li>
                    </ul>
                </li>
                <li class="nested-submenu-item">
                    <a href="#" class="submenu-link"><i class="fas fa-wrench"></i> 維修管理</a>
                    <ul class="nested-submenu-always-open">
                        <li><a href="<%= request.getContextPath() %>/repairForm" class="submenu-link">填寫維修表單 (前台頁面)</a></li>
                        <li><a href="<%= request.getContextPath() %>/AdminRepairListServlet" class="submenu-link">維修管理 (後台頁面)</a></li>
                    </ul>
                </li>
                <li class="nested-submenu-item">
                    <a href="#" class="submenu-link"><i class="fas fa-calendar-alt"></i> 保養管理</a>
                    <ul class="nested-submenu-always-open">
                        <li><a href="<%= request.getContextPath() %>/FindAllMachineMaintenance" class="submenu-link">查詢保養排程 (前台頁面)</a></li>
                        <li><a href="<%= request.getContextPath() %>/AdminMaintenanceServlet" class="submenu-link">保養計劃管理 (後台頁面)</a></li>
                    </ul>
                </li>
                <li class="nested-submenu-item">
                    <a href="#" class="submenu-link"><i class="fas fa-file-alt"></i> 文件管理</a>
                    <ul class="nested-submenu-always-open">
                        <li><a href="<%= request.getContextPath() %>/FileManagementServlet" class="submenu-link">文件上傳管理 (後台頁面)</a></li>
                    </ul>
                </li>
            </ul>
        </li>
        <% } %>

        <% if ("personnel".equals(userRole) || "admin".equals(userRole)) { %>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-users"></i> <span>人員與帳號</span>
            </a>
            <ul class="submenu-always-open">
                <li><a href="<%= request.getContextPath() %>/JSP/ty/manage.jsp" class="submenu-link">員工管理</a></li>
                <li><a href="<%= request.getContextPath() %>/JSP/ty/manageUsers.jsp" class="submenu-link"><i class="fas fa-key"></i> 帳號密碼管理</a></li>
            </ul>
        </li>
        <% } %>

        <% if ("inventory".equals(userRole) || "admin".equals(userRole)) { %>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-boxes"></i> <span>物料與庫存</span>
            </a>
            <ul class="submenu-always-open">
                <li><a href="<%= request.getContextPath() %>/inventory/bom/prodBomQuery.html" class="submenu-link">物料主檔</a></li>
     
            </ul>
        </li>
        <% } %>

        <% if ("production".equals(userRole) || "admin".equals(userRole)) { %>
        <li class="menu-item">
            <a href="#" class="menu-link">
                <i class="fas fa-industry"></i> <span>生產排程</span>
            </a>
            <ul class="submenu-always-open">
                <li><a href="<%= request.getContextPath() %>/JSP/yw/productList.jsp" class="submenu-link">產品主檔</a></li>
                <li><a href="<%= request.getContextPath() %>/JSP/yw/workOrderList.jsp" class="submenu-link">生產工單</a></li>
                <li><a href="<%= request.getContextPath() %>/JSP/yw/productionScheduleDetail.jsp" class="submenu-link">生產排程明細</a></li>
            </ul>
        </li>
        <% } %>

        <li class="menu-item">
            <a href="<%= request.getContextPath() %>/logoutProcess" class="menu-link">
                <i class="fas fa-sign-out-alt"></i> <span>登出</span>
            </a>
        </li>
    </ul>
</nav>
