<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>新增供應商</title>

    <!-- 共用樣式 -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/stylee.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/supplier.css">

    <!-- JS -->
    <script src="<%=request.getContextPath()%>/JS/sidebaar.js" defer></script>
</head>
<body>

    
 

    <div class="content">
        <h2>新增供應商</h2>

        <div class="form-container">
            <form action="<%=request.getContextPath()%>/AddSupplierServlet" method="post">
                <div class="form-group">
                    <label for="supplierName">供應商名稱</label>
                    <input type="text" name="supplierName" id="supplierName" required>
                </div>

                <div class="form-group">
                    <label for="pm">聯絡人 (PM)</label>
                    <input type="text" name="pm" id="pm" required>
                </div>

                <div class="form-group">
                    <label for="supplierPhone">電話</label>
                    <input type="text" name="supplierPhone" id="supplierPhone" required>
                </div>

                <div class="form-group">
                    <label for="supplierEmail">Email</label>
                    <input type="email" name="supplierEmail" id="supplierEmail" required>
                </div>

                <div class="form-group">
                    <label for="supplierAddress">地址</label>
                    <input type="text" name="supplierAddress" id="supplierAddress" required>
                </div>

                <button type="submit">新增供應商</button>
            </form>
        </div>
    </div>

</body>
</html>
