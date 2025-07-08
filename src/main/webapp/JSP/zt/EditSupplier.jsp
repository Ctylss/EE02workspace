<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.mes.bean.Supplier" %>
<%
    Supplier supplier = (Supplier) request.getAttribute("supplier");
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>編輯供應商</title>
    
    <!-- 共用樣式 -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/stylee.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/editSupplier.css">

    <script src="<%= request.getContextPath() %>/JS/sidebaar.js" defer></script>
</head>
<body>



    <h2>編輯供應商</h2>
    <form action="<%= request.getContextPath() %>/UpdateSupplierServlet" method="post">
        <input type="hidden" name="supplierId" value="<%= supplier.getSupplierId() %>">

        <div class="form-group">
            <label for="supplierName">供應商名稱</label>
            <input type="text" id="supplierName" name="supplierName" value="<%= supplier.getSupplierName() %>" required>
        </div>

        <div class="form-group">
            <label for="pm">聯絡人 (PM)</label>
            <input type="text" id="pm" name="pm" value="<%= supplier.getPm() %>">
        </div>

        <div class="form-group">
            <label for="supplierPhone">電話</label>
            <input type="text" id="supplierPhone" name="supplierPhone" value="<%= supplier.getSupplierPhone() %>">
        </div>

        <div class="form-group">
            <label for="supplierEmail">Email</label>
            <input type="email" id="supplierEmail" name="supplierEmail" value="<%= supplier.getSupplierEmail() %>">
        </div>

        <div class="form-group">
            <label for="supplierAddress">地址</label>
            <input type="text" id="supplierAddress" name="supplierAddress" value="<%= supplier.getSupplierAddress() %>">
        </div>

        <button type="submit">儲存變更</button>
    </form>
</body>
</html>
