<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, com.mes.bean.Supplier" %>
<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    request.setAttribute("pageTitle", "供應商資料維護");
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>供應商資料維護</title>

    <!-- 引入共用樣式 -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/stylee.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/supplier.css">
    <script src="<%= request.getContextPath() %>/JS/sidebaar.js" defer></script>
</head>
<body>
<div class="wrapper">
  

    <main class="content">
        <h2>供應商列表</h2>
	<div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>名稱</th>
                    <th>聯絡人</th>
                    <th>電話</th>
                    <th>Email</th>
                    <th>地址</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Supplier> list = (List<Supplier>) request.getAttribute("supplierList");
                    if (list != null) {
                        for (Supplier s : list) {
                %>
                <tr>
                    <td><%= s.getSupplierId() %></td>
                    <td><%= s.getSupplierName() %></td>
                    <td><%= s.getPm() %></td>
                    <td><%= s.getSupplierPhone() %></td>
                    <td><%= s.getSupplierEmail() %></td>
                    <td><%= s.getSupplierAddress() %></td>
                    <td>
                        <a href="DeleteSupplierServlet?supplierId=<%= s.getSupplierId() %>"
                           class="button btn-danger"
                           onclick="return confirm('確定要下架這筆供應商嗎？')">下架</a>
                        <a href="EditSupplierServlet?supplierId=<%= s.getSupplierId() %>"
                           class="button btn-edit">修改</a>
                    </td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr><td colspan="7">尚無供應商資料</td></tr>
                <%
                    }
                %>
            </tbody>
        </table>
</div>
        <div class="add-btn-container">
            <a class="button btn-add" href="<%= request.getContextPath() %>/JSP/AddSupplier.jsp">新增供應商</a>
        </div>
    </main>
</div>
</body>
</html>
