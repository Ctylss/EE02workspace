<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 這裡不再聲明 userRole，它將從 dashboard.jsp 繼承 --%>

<div class="sidebar-brand">
    <a href="<%= request.getContextPath() %>/dashboard/dashboard.jsp" class="brand-link">
        <i class="fas fa-industry"></i> <span>ERP System</span>
    </a>
</div>

<nav class="sidebar-nav" role="navigation" aria-label="側邊欄選單">
    <ul class="sidebar-menu">
        <li class="menu-item">
            <a href="<%= request.getContextPath() %>/dashboard/dashboard.jsp" class="menu-link active">
                <i class="fas fa-tachometer-alt"></i> <span>儀表板</span>
            </a>
        </li>

        <% if ("supplier".equals(userRole) || "admin".equals(userRole)) { %>
        <li class="menu-item"> <%-- 移除 has-submenu --%>
            <a href="#" class="menu-link"> <%-- 移除 dropdown-toggle 和 aria-屬性，移除箭頭圖標 --%>
                <i class="fas fa-truck-loading"></i> <span>供應商與採購</span>
            </a>
            <ul class="submenu-always-open"> <%-- 新增一個 class 讓它始終展開 --%>
                <li><a href="<%= request.getContextPath() %>/OrderAddFormServlet" class="submenu-link">新增採購訂單</a></li>
                <li><a href="<%= request.getContextPath() %>../../servlet/suppler/OrderListServlet" class="submenu-link">採購訂單列表</a></li>
                <li><a href="<%= request.getContextPath() %>../../servlet/suppler/SupplierListServlet" class="submenu-link">供應商資料維護</a></li>
            </ul>
        </li>
        <% } %>

        <% if ("machine".equals(userRole) || "admin".equals(userRole)) { %>
        <li class="menu-item"> <%-- 移除 has-submenu --%>
            <a href="#" class="menu-link"> <%-- 移除 dropdown-toggle 和 aria-屬性，移除箭頭圖標 --%>
                <i class="fas fa-cogs"></i> <span>設備管理</span>
            </a>
            <ul class="submenu-always-open"> <%-- 新增一個 class 讓它始終展開 --%>
                <li class="nested-submenu-item"> <%-- 新增一個類來識別嵌套子菜單的父項 --%>
                    <a href="#" class="submenu-link">機台系統</a> <%-- 移除 dropdown-toggle 和箭頭圖標 --%>
                    <ul class="nested-submenu-always-open"> <%-- 新增一個 class 讓嵌套子菜單始終展開 --%>
                        <li><a href="<%= request.getContextPath() %>/machines/machineList.jsp" class="submenu-link">機台查詢 (前台頁面)</a></li>
                        <li><a href="<%= request.getContextPath() %>../WEB-INF/html/Test.html" class="submenu-link">機台管理 (後台頁面)</a></li>
                    </ul>
                </li>
                <li class="nested-submenu-item">
                    <a href="#" class="submenu-link"><i class="fas fa-wrench"></i> 維修管理</a> <%-- 移除 dropdown-toggle 和箭頭圖標 --%>
                    <ul class="nested-submenu-always-open">
                        <li><a href="<%= request.getContextPath() %>/maintenance/repairForm.jsp" class="submenu-link">填寫維修表單 (前台頁面)</a></li>
                        <li><a href="<%= request.getContextPath() %>/maintenance/repairRecordList.jsp" class="submenu-link">查詢維修表單 (前台頁面)</a></li>
                        <li><a href="<%= request.getContextPath() %>/maintenance/repairManagement.jsp" class="submenu-link">維修管理 (後台頁面)</a></li>
                    </ul>
                </li>
                <li class="nested-submenu-item">
                    <a href="#" class="submenu-link"><i class="fas fa-calendar-alt"></i> 保養管理</a> <%-- 移除 dropdown-toggle 和箭頭圖標 --%>
                    <ul class="nested-submenu-always-open">
                        <li><a href="<%= request.getContextPath() %>/maintenance/maintenanceScheduleList.jsp" class="submenu-link">查詢保養排程 (前台頁面)</a></li>
                        <li><a href="<%= request.getContextPath() %>/maintenance/maintenancePlanManagement.jsp" class="submenu-link">保養計劃管理 (後台頁面)</a></li>
                    </ul>
                </li>
                <li class="nested-submenu-item">
                    <a href="#" class="submenu-link"><i class="fas fa-file-alt"></i> 文件管理</a> <%-- 移除 dropdown-toggle 和箭頭圖標 --%>
                    <ul class="nested-submenu-always-open">
                        <li><a href="<%= request.getContextPath() %>/documents/machineDocumentBrowser.jsp" class="submenu-link">機台文件瀏覽 (前台頁面)</a></li>
                        <li><a href="<%= request.getContextPath() %>/documents/documentUploadManagement.jsp" class="submenu-link">文件上傳管理 (後台頁面)</a></li>
                    </ul>
                </li>
            </ul>
        </li>
        <% } %>

        <% if ("personnel".equals(userRole) || "admin".equals(userRole)) { %>
        <li class="menu-item"> <%-- 移除 has-submenu --%>
            <a href="#" class="menu-link"> <%-- 移除 dropdown-toggle 和 aria-屬性，移除箭頭圖標 --%>
                <i class="fas fa-users"></i> <span>人員與帳號</span>
            </a>
            <ul class="submenu-always-open"> <%-- 新增一個 class 讓它始終展開 --%>
                <li><a href="<%= request.getContextPath() %>/personnel/manage.jsp" class="submenu-link">員工管理</a></li>
                <li><a href="<%= request.getContextPath() %>/personnel/manageUsers.jsp" class="submenu-link"><i class="fas fa-key"></i> 帳號密碼管理</a></li>
            </ul>
        </li>
        <% } %>

        <% if ("inventory".equals(userRole) || "admin".equals(userRole)) { %>
        <li class="menu-item"> <%-- 移除 has-submenu --%>
            <a href="#" class="menu-link"> <%-- 移除 dropdown-toggle 和 aria-屬性，移除箭頭圖標 --%>
                <i class="fas fa-boxes"></i> <span>物料與庫存</span>
            </a>
            <ul class="submenu-always-open"> <%-- 新增一個 class 讓它始終展開 --%>
                <li><a href="<%= request.getContextPath() %>/inventory/materialList.jsp" class="submenu-link">物料主檔</a></li>
                <li><a href="<%= request.getContextPath() %>/inventory/materialReceiptIssue.jsp?type=receipt" class="submenu-link">物料入庫</a></li>
                <li><a href="<%= request.getContextPath() %>/inventory/materialReceiptIssue.jsp?type=issue" class="submenu-link">物料出庫</a></li>
            </ul>
        </li>
        <% } %>

        <% if ("production".equals(userRole) || "admin".equals(userRole)) { %>
        <li class="menu-item"> <%-- 移除 has-submenu --%>
            <a href="#" class="menu-link"> <%-- 移除 dropdown-toggle 和 aria-屬性，移除箭頭圖標 --%>
                <i class="fas fa-industry"></i> <span>生產排程</span>
            </a>
            <ul class="submenu-always-open"> <%-- 新增一個 class 讓它始終展開 --%>
                <li><a href="<%= request.getContextPath() %>/production/productList.jsp" class="submenu-link">產品主檔</a></li>
                <li><a href="<%= request.getContextPath() %>/production/workOrderList.jsp" class="submenu-link">生產工單</a></li>
                <li><a href="<%= request.getContextPath() %>/production/productionScheduleDetail.jsp" class="submenu-link">生產排程明細</a></li>
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