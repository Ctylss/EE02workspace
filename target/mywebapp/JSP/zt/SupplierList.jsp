<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, com.mes.bean.Supplier" %>
<%
    if (session.getAttribute("loggedInUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 設定 userRole 給 sidebar.jsp 使用
    String userRole = (String) session.getAttribute("userRole");
    if (userRole == null) {
        userRole = "guest";
    }
    request.setAttribute("userRole", userRole);

    request.setAttribute("pageTitle", "供應商資料維護");
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>供應商資料維護</title>

    <!-- 共用樣式 -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/supplier.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

    <!-- 共用 JS -->
    <script src="<%= request.getContextPath() %>/JS/sidebar.js" defer></script>
</head>
<body>
<div class="app-wrapper">
    <!-- 側邊欄 -->
    <aside class="main-sidebar" role="complementary" aria-label="側邊欄">
        <jsp:include page="/common/sidebar.jsp" />
    </aside>

    <div class="main-right-content-wrapper">
        <div class="actual-page-content">
            <main class="main-content">
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
                                    <a href="DeleteSupplierServlet?supplierId=<%= s.getSupplierId() %>" class="button btn-danger"
                                       onclick="return confirm('確定要下架這筆供應商嗎？')">下架</a>
                                    <a href="EditSupplierServlet?supplierId=<%= s.getSupplierId() %>" class="button btn-edit">修改</a>
                                </td>
                            </tr>
                            <%
                                    }
                                } else {
                            %>
                            <tr><td colspan="7">尚無供應商資料</td></tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>

                <div class="add-btn-container">
                    <a class="button btn-add" href="<%= request.getContextPath() %>/JSP/zt/AddSupplier.jsp">新增供應商</a>
                </div>
            </main>
        </div>
    </div>
</div>
</body>
</html>
