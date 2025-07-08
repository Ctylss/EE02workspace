<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mes.bean.Supplier" %>
<%@ page import="com.mes.bean.Material" %>
<%
    List<Supplier> supplierList = (List<Supplier>) request.getAttribute("supplierList");
    List<Material> materialList = (List<Material>) request.getAttribute("materialList");
    StringBuilder materialOptionsHTML = new StringBuilder();
    for (Material m : materialList) {
        materialOptionsHTML.append("<option value='").append(m.getMaterialId()).append("'>")
                           .append(m.getMaterialName()).append("</option>");
    }
%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>新增訂單</title>

    <!-- 共用樣式 -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
    <!-- 共用 JS -->
    <script src="<%= request.getContextPath() %>/JS/sidebar.js" defer></script>
    <script src="<%= request.getContextPath() %>/JS/order.js" defer></script>

    <!-- 將 materialOptionsHTML 傳入 JS 中 -->
    <script>
        const materialOptionsHTML = `<%= materialOptionsHTML.toString().replace("\"", "\\\"") %>`;
    </script>
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

<div class="content">
<div class="app-wrapper">
        <aside class="main-sidebar" role="complementary" aria-label="側邊欄">
            <%-- 將指令包含 (<%@ include %>) 改為動作標籤包含 (<jsp:include>) --%>
            <%-- 使用絕對路徑從 Web Context Root 開始 --%>
            <jsp:include page="/common/sidebar.jsp" />
        </aside>

<div class="actual-page-content">
        <main class="main-content">
 
    <h2>新增訂單</h2>

    <form action="<%= request.getContextPath() %>/OrderInsertServlet" method="post" onsubmit="return validateForm()">

        <div class="form-container">
            <div class="form-group">
                <label for="supplier">供應商：</label>
                <select id="supplier" name="supplier" required>
                    <option value="">請選擇</option>
                    <% for (Supplier s : supplierList) { %>
                        <option value="<%= s.getSupplierId() %>"><%= s.getSupplierName() %></option>
                    <% } %>
                </select>
            </div>

            <div class="form-group">
                <label for="orderDate">日期：</label>
                <input type="date" id="orderDate" name="orderDate" required>
            </div>

            <div class="form-group">
                <label for="status">狀態：</label>
                <select id="status" name="status">
                    <option>待處理</option>
                    <option>已下單</option>
                    <option>已收貨</option>
                    <option>已取消</option>
                </select>
            </div>
        </div>

        <table id="detailTable">
            <thead>
                <tr>
                    <th>編號</th>
                    <th>物料</th>
                    <th>數量</th>
                    <th>單價</th>
                    <th>小計</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>1</td>
                    <td>
                        <select name="materialId[]" class="material-select">
                            <%= materialOptionsHTML.toString() %>
                        </select>
                    </td>
                    <td><input type="number" name="quantity[]" value="100" class="qty"></td>
                    <td><input type="number" name="unitPrice[]" value="200" class="price"></td>
                    <td class="subtotal">20000</td>
                    <td><button type="button" class="delete-btn" onclick="deleteRow(this)">刪除</button></td>
                </tr>
            </tbody>
        </table>

        <br>
        <button type="button" onclick="addRow()">新增明細</button>
        <button type="submit">提交訂單</button>

        <div class="total" id="totalAmount">總金額：$20000</div>
    </form>
       </main>
            </div>
</div>
</div>
</body>
</html>
