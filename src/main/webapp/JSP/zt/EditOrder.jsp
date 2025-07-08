<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mes.bean.Order" %>
<%@ page import="com.mes.bean.OrderItem" %>
<%@ page import="com.mes.bean.Supplier" %>

<%
    Order order = (Order) request.getAttribute("order");
    List<OrderItem> items = (List<OrderItem>) request.getAttribute("items");
    List<Supplier> supplierList = (List<Supplier>) request.getAttribute("supplierList");
%>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>編輯訂單</title>

    <!-- 共用樣式 -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/stylee.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/editOrder.css">

    <!-- JS -->
    <script src="<%= request.getContextPath() %>/JS/sidebaar.js" defer></script>
</head>

<body >



<div class="content">
    <h2>編輯訂單</h2>

    <form action="<%= request.getContextPath() %>/UpdateOrderServlet" method="post">
        <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">

        <div class="form-container">
            <div class="form-group">
                <label for="supplier">供應商：</label>
                <select id="supplier" name="supplierId" required>
                    <option value="">請選擇</option>
                    <%
                        for (Supplier s : supplierList) {
                            String selected = (s.getSupplierId() == order.getSupplierId()) ? "selected" : "";
                    %>
                        <option value="<%= s.getSupplierId() %>" <%= selected %>><%= s.getSupplierName() %></option>
                    <%
                        }
                    %>
                </select>
            </div>

            <div class="form-group">
                <label for="orderDate">訂單日期：</label>
                <input type="date" id="orderDate" name="orderDate" value="<%= order.getOrderDate() %>" required>
            </div>

            <div class="form-group">
                <label for="orderStatus">狀態：</label>
                <select id="orderStatus" name="orderStatus">
                    <option value="PENDING" <%= "PENDING".equals(order.getOrderStatus()) ? "selected" : "" %>>待處理</option>
                    <option value="ORDERED" <%= "ORDERED".equals(order.getOrderStatus()) ? "selected" : "" %>>已下單</option>
                    <option value="RECEIVED" <%= "RECEIVED".equals(order.getOrderStatus()) ? "selected" : "" %>>已收貨</option>
                    <option value="CANCELLED" <%= "CANCELLED".equals(order.getOrderStatus()) ? "selected" : "" %>>已取消</option>
                </select>
            </div>
        </div>

        <h3>訂單明細</h3>
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>物料</th>
                        <th>數量</th>
                        <th>單價</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (OrderItem item : items) {
                    %>
                    <tr>
                        <td>
                            <select name="materialId[]">
                                <option value="1" <%= item.getMaterialId() == 1 ? "selected" : "" %>>PCB版</option>
                                <option value="2" <%= item.getMaterialId() == 2 ? "selected" : "" %>>顯示晶片</option>
                                <option value="3" <%= item.getMaterialId() == 3 ? "selected" : "" %>>主機板</option>
                                <option value="4" <%= item.getMaterialId() == 4 ? "selected" : "" %>>記憶體模組</option>
                            </select>
                        </td>
                        <td><input type="number" name="quantity[]" value="<%= item.getQuantity() %>" required></td>
                        <td><input type="number" name="unitPrice[]" step="0.01" value="<%= item.getUnitPrice() %>" required></td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>

        <br>
        <button type="submit">更新訂單</button>
    </form>
</div>

</body>
</html>
