<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mes.bean.Order" %>
<%@ page import="com.mes.bean.OrderItem" %>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>訂單列表</title>

    <!-- 引入共用 CSS -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css"> 
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/orderList.css">

    <!-- 引入 JS（可選） -->
    <script src="<%=request.getContextPath()%>/JS/orderList.js" defer></script>
    <script src="<%=request.getContextPath()%>/JS/sidebar.js" defer></script>
</head>

<body class="app-body">
<%
    String userRole = (String) session.getAttribute("userRole");
    if (userRole == null) {
        userRole = "guest";
    }
    request.setAttribute("userRole", userRole);
%>

<div class="app-wrapper">
    <aside class="main-sidebar" role="complementary" aria-label="側邊欄">
        <jsp:include page="/common/sidebar.jsp" />
    </aside>

    <div class="main-right-content-wrapper">
        <div class="actual-page-content">
            <main class="main-content">
                <div class="content">
                    <div class="container">
                        <h2>訂單列表</h2>
                        <input type="text" id="searchInput" class="search-input" placeholder="搜尋供應商或狀態…" oninput="filterTable()" />
                        <label>開始日期：<input type="date" id="startDate" onchange="filterTable()"></label>
                        <label>結束日期：<input type="date" id="endDate" onchange="filterTable()"></label>
                    </div>

                    <table id="orderTable" class="data-table" data-sort-dir="asc">
                        <thead>
                            <tr>
                                <th onclick="sortTable(0)">訂單編號</th>
                                <th onclick="sortTable(1)">供應商</th>
                                <th onclick="sortTable(2)">訂單日期</th>
                                <th onclick="sortTable(3)">狀態</th>
                                <th onclick="sortTable(4)">小計</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<Order> orderList = (List<Order>) request.getAttribute("orderList");
                                if (orderList != null && !orderList.isEmpty()) {
                                    for (Order order : orderList) {
                            %>
                            <tr class="main-row" data-orderid="<%= order.getOrderId() %>">
                                <td><%= order.getOrderId() %></td>
                                <td><%= order.getSupplier() != null ? order.getSupplier().getSupplierName() : "無資料" %></td>
                                <td><%= order.getOrderDate() %></td>
                                <td>
                                    <%
                                        String status = order.getOrderStatus();
                                        String statusText = "";
                                        switch (status) {
                                            case "PENDING": statusText = "待處理"; break;
                                            case "ORDERED": statusText = "已下單"; break;
                                            case "RECEIVED": statusText = "已收貨"; break;
                                            case "CANCELLED": statusText = "已取消"; break;
                                            default: statusText = status;
                                        }
                                    %>
                                    <%= statusText %>
                                </td>
                                <td><%= order.getSubTotal() %></td>
                                <td>
                                    <a class="btn-icon edit-icon" href="EditOrderServlet?orderId=<%= order.getOrderId() %>">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <a class="btn-icon delete-icon" href="DeleteOrderServlet?orderId=<%= order.getOrderId() %>"
                                       onclick="return confirm('確定要刪除這筆訂單嗎？');">
                                        <i class="fas fa-trash-alt"></i>
                                    </a>
                                    <a class="btn-icon view-icon" href="javascript:void(0);"
                                       onclick="toggleDetails(<%= order.getOrderId() %>)">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                </td>
                            </tr>
                            <tr id="details-<%= order.getOrderId() %>" style="display: none;">
                                <td colspan="6">
                                    <table class="data-table sub-table">
                                        <thead>
                                            <tr>
                                                <th>物料名稱</th>
                                                <th>數量</th>
                                                <th>單價</th>
                                                <th>出貨狀態</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <%
                                                List<OrderItem> items = order.getItemList();
                                                if (items != null && !items.isEmpty()) {
                                                    for (OrderItem item : items) {
                                            %>
                                            <tr>
                                                <td><%= item.getMaterialName() %></td>
                                                <td><%= item.getQuantity() %></td>
                                                <td><%= item.getUnitPrice() %></td>
                                                <td><%= item.getDeliveryStatus() %></td>
                                            </tr>
                                            <% } } else { %>
                                            <tr>
                                                <td colspan="4">（無明細資料）</td>
                                            </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <% } // for end %>
                            <% } else { // 無資料 %>
                            <tr>
                                <td colspan="6">尚無訂單資料</td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>
</div>
</body>
</html>
