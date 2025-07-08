<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% request.setAttribute("pageTitle", "生產工單"); %>

<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>生產工單管理</title>

<!-- FontAwesome -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
      xintegrity="sha512-pO2m14i3LKKkzG4NClSt+vIRk7wX2kZaPoYFCsr+ROxfNUUMgAwPf2OmZLDHhKxKpV7eT2U2kE4X2yU1ro2zYw=="
      crossorigin="anonymous" referrerpolicy="no-referrer" />

<style>
  /* 你原本的 CSS 加上優化操作按鈕區塊 */
  body {
    font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
    margin: 0; padding: 0;
    background: #fefefe;
    color: #333;
  }
  .data-table-section {
    max-width: 1100px;
    margin: 30px auto 40px;
    padding: 0 15px;
  }
  h2 {
    margin-bottom: 20px;
    font-weight: 700;
    border-bottom: 2px solid #2c3e50;
    padding-bottom: 8px;
    color: #222;
  }
  p[style*="color: red"] {
    background-color: #f8d7da;
    border: 1px solid #f5c2c7;
    padding: 10px 15px;
    border-radius: 4px;
    margin-bottom: 20px;
    font-weight: 600;
  }
  p[style*="color: green"] {
    background-color: #d1e7dd;
    border: 1px solid #badbcc;
    padding: 10px 15px;
    border-radius: 4px;
    margin-bottom: 20px;
    font-weight: 600;
  }
  .toolbar {
    margin-bottom: 15px;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 12px;
  }
  .btn-primary {
    background-color: #007bff;
    color: white !important;
    padding: 8px 18px;
    border-radius: 5px;
    font-weight: 600;
    text-decoration: none;
    transition: background-color 0.3s ease;
  }
  .btn-primary:hover {
    background-color: #0056b3;
  }
  select.search-input,
  button[type="submit"] {
    padding: 6px 10px;
    border-radius: 4px;
    border: 1px solid #ccc;
    font-size: 14px;
  }
  button[type="submit"] {
    background-color: #28a745;
    color: white;
    border: none;
    cursor: pointer;
    font-weight: 600;
    transition: background-color 0.3s ease;
  }
  button[type="submit"]:hover {
    background-color: #1e7e34;
  }
  a[href$="list"] {
    color: #007bff;
    text-decoration: underline;
    font-weight: 500;
  }
  a[href$="list"]:hover {
    color: #0056b3;
  }
  table.data-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
    box-shadow: 0 0 10px rgba(0,0,0,0.07);
  }
  table.data-table th,
  table.data-table td {
    border: 1px solid #ddd;
    padding: 10px 12px;
    font-size: 14px;
    text-align: left;
    vertical-align: middle;
  }
  table.data-table th {
    background-color: #2c3e50;
    color: white;
    font-weight: 600;
  }
  table.data-table tbody tr:hover {
    background-color: #f1f9ff;
  }
  /* 操作按鈕群組 */
  .actions {
    display: flex;
    gap: 8px;
  }
  .btn-icon {
    background: none;
    border: none;
    cursor: pointer;
    font-size: 18px;
    color: #555;
    padding: 4px;
    border-radius: 4px;
    transition: color 0.3s ease, background-color 0.3s ease;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }
  .btn-icon:hover {
    color: #007bff;
    background-color: rgba(0, 123, 255, 0.1);
  }
  .view-icon i {
    color: #17a2b8;
  }
  .edit-icon i {
    color: #ffc107;
  }
  .delete-icon i {
    color: #dc3545;
  }
  /* 表單 */
  form.data-form {
    max-width: 650px;
    margin-top: 20px;
  }
  .form-group {
    margin-bottom: 18px;
  }
  .form-group label {
    font-weight: 600;
    display: block;
    margin-bottom: 6px;
  }
  .form-group input[type="text"],
  .form-group input[type="number"],
  .form-group input[type="date"],
  .form-group select,
  .form-group textarea {
    width: 100%;
    padding: 8px 10px;
    font-size: 14px;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-sizing: border-box;
    font-family: inherit;
    transition: border-color 0.3s ease;
  }
  .form-group textarea {
    resize: vertical;
    min-height: 90px;
  }
  .form-group input[type="text"]:focus,
  .form-group input[type="number"]:focus,
  .form-group input[type="date"]:focus,
  .form-group select:focus,
  .form-group textarea:focus {
    border-color: #007bff;
    outline: none;
  }
  .form-static-text {
    background-color: #eee;
    padding: 8px 10px;
    border-radius: 4px;
    font-family: monospace;
    font-size: 14px;
    user-select: none;
  }
  .help-text {
    font-size: 12px;
    color: #888;
  }
  input[type="submit"] {
    background-color: #007bff;
    border: none;
    padding: 10px 22px;
    color: white;
    font-weight: 600;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  input[type="submit"]:hover {
    background-color: #0056b3;
  }
  a.btn-sm {
    background-color: #28a745;
    color: white !important;
    padding: 4px 10px;
    border-radius: 4px;
    font-size: 13px;
    text-decoration: none;
    margin-left: 10px;
    transition: background-color 0.3s ease;
  }
  a.btn-sm:hover {
    background-color: #1e7e34;
  }
</style>

</head>
<body>
<section class="data-table-section">

  <%-- 錯誤訊息 --%>
  <c:if test="${not empty errorMessage}">
    <p style="color: red; font-weight: bold;">錯誤: <c:out value="${errorMessage}"/></p>
  </c:if>

  <%-- 成功訊息 --%>
  <c:if test="${not empty sessionScope.message}">
    <p style="color: green; font-weight: bold;"><c:out value="${sessionScope.message}"/></p>
    <c:remove var="message" scope="session"/>
  </c:if>

  <%-- 工單列表 --%>
  <c:if test="${action == 'list' || action == 'byStatus' || action == 'byProduct' || action == null}">
    <h2>生產工單列表</h2>
    <div class="toolbar">
      <a href="${pageContext.request.contextPath}/workorders?action=new" class="btn-primary">新增工單</a>

      <form action="${pageContext.request.contextPath}/workorders" method="get" style="display: inline-block;">
        <input type="hidden" name="action" value="byStatus" />
        <select name="status" class="search-input">
          <option value="">所有狀態</option>
          <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>待處理</option>
          <option value="In Progress" ${param.status == 'In Progress' ? 'selected' : ''}>進行中</option>
          <option value="Completed" ${param.status == 'Completed' ? 'selected' : ''}>已完成</option>
          <option value="Cancelled" ${param.status == 'Cancelled' ? 'selected' : ''}>已取消</option>
        </select>
        <button type="submit">篩選</button>
      </form>

      <a href="${pageContext.request.contextPath}/workorders?action=list">顯示全部</a>
    </div>

    <table class="data-table" cellspacing="0" cellpadding="0">
      <thead>
        <tr>
          <th>工單編號</th>
          <th>產品名稱</th>
          <th>數量</th>
          <th>單位</th>
          <th>預計開始日期</th>
          <th>預計完成日期</th>
          <th>狀態</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty workOrders}">
            <c:forEach var="workOrder" items="${workOrders}">
              <tr>
                <td><c:out value="${workOrder.workOrderNumber}"/></td>
                <td><c:out value="${workOrder.productName}"/></td>
                <td><fmt:formatNumber value="${workOrder.quantity}" pattern="#,##0"/></td>
                <td><c:out value="${workOrder.unit}"/></td>
                <%-- CORRECTED: Directly output formatted date string --%>
                <td><c:out value="${workOrder.scheduledStartDate}"/></td>
                <td><c:out value="${workOrder.scheduledDueDate}"/></td>
                <td><c:out value="${workOrder.status}"/></td>
                <td>
                  <div class="actions">
                    <a href="${pageContext.request.contextPath}/workorders?action=view&id=${workOrder.workOrderId}" 
                       class="btn-icon view-icon" title="查看">
                      <i class="fas fa-eye"></i>
                    </a>
                    <a href="${pageContext.request.contextPath}/workorders?action=edit&id=${workOrder.workOrderId}" 
                       class="btn-icon edit-icon" title="編輯">
                      <i class="fas fa-edit"></i>
                    </a>
                    <form action="${pageContext.request.contextPath}/workorders" method="post" style="display:inline;" 
                          onsubmit="return confirm('確定要刪除工單 ${workOrder.workOrderNumber} 嗎？');">
                      <input type="hidden" name="action" value="delete" />
                      <input type="hidden" name="id" value="${workOrder.workOrderId}" />
                      <button type="submit" class="btn-icon delete-icon" title="刪除">
                        <i class="fas fa-trash"></i>
                      </button>
                    </form>
                  </div>
                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr>
              <td colspan="8" style="text-align:center;">沒有找到任何工單。</td>
            </tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </c:if>

  <%-- 新增工單表單 --%>
  <c:if test="${action == 'new'}">
    <h2>新增生產工單</h2>
    <form action="${pageContext.request.contextPath}/workorders" method="post" class="data-form">
      <input type="hidden" name="action" value="add" />
      
      <div class="form-group">
        <label for="workOrderNumber">工單編號:</label>
        <input type="text" id="workOrderNumber" name="workOrderNumber" required value="<c:out value='${workOrder.workOrderNumber}'/>" />
      </div>

      <div class="form-group">
        <label for="productId">生產產品:</label>
        <select id="productId" name="productId" required>
          <option value="">--請選擇產品--</option>
          <c:forEach var="product" items="${products}">
            <option value="<c:out value='${product.productId}'/>" 
              ${workOrder.product.productId == product.productId ? 'selected' : ''}>
              <c:out value="${product.productCode}"/> - <c:out value="${product.productName}"/>
            </option>
          </c:forEach>
        </select>
      </div>

      <div class="form-group">
        <label for="quantity">數量:</label>
        <input type="number" step="1" id="quantity" name="quantity" min="1" required value="<c:out value='${workOrder.quantity}'/>" />
      </div>

      <div class="form-group">
        <label for="unit">單位:</label>
        <input type="text" id="unit" name="unit" required value="<c:out value='${workOrder.unit}'/>" />
      </div>

      <div class="form-group">
        <label for="scheduledStartDate">預計開始日期:</label>
        <%-- CORRECTED: Directly output formatted date string for input type="date" --%>
        <input type="date" id="scheduledStartDate" name="scheduledStartDate" required 
               value="<c:out value='${workOrder.scheduledStartDate}'/>" />
      </div>

      <div class="form-group">
        <label for="scheduledDueDate">預計完成日期:</label>
        <%-- CORRECTED: Directly output formatted date string for input type="date" --%>
        <input type="date" id="scheduledDueDate" name="scheduledDueDate" required 
               value="<c:out value='${workOrder.scheduledDueDate}'/>" />
      </div>

      <div class="form-group">
        <label for="status">狀態:</label>
        <select id="status" name="status" required>
          <option value="Pending" ${workOrder.status == 'Pending' ? 'selected' : ''}>待處理</option>
          <option value="In Progress" ${workOrder.status == 'In Progress' ? 'selected' : ''}>進行中</option>
          <option value="Completed" ${workOrder.status == 'Completed' ? 'selected' : ''}>已完成</option>
          <option value="Cancelled" ${workOrder.status == 'Cancelled' ? 'selected' : ''}>已取消</option>
        </select>
      </div>

      <div class="form-group">
        <label for="notes">備註:</label>
        <textarea id="notes" name="notes" rows="5"><c:out value="${workOrder.notes}"/></textarea>
      </div>

      <input type="submit" value="提交新增" />
    </form>
    <p><a href="${pageContext.request.contextPath}/workorders?action=list">返回列表</a></p>
  </c:if>

  <%-- 查看工單詳情 --%>
  <c:if test="${action == 'view'}">
    <h2>工單詳情</h2>
    <c:if test="${not empty workOrder}">
      <p><strong>工單ID:</strong> <c:out value="${workOrder.workOrderId}"/></p>
      <p><strong>工單編號:</strong> <c:out value="${workOrder.workOrderNumber}"/></p>
      <p><strong>產品名稱:</strong> <c:out value="${workOrder.productName}"/></p>
      <p><strong>產品代碼:</strong> <c:out value="${workOrder.product.productCode}"/></p>
      <p><strong>數量:</strong> <fmt:formatNumber value="${workOrder.quantity}" pattern="#,##0"/></p>
      <p><strong>單位:</strong> <c:out value="${workOrder.unit}"/></p>
      <p><strong>預計開始日期:</strong> <c:out value="${workOrder.scheduledStartDate}"/></p>
      <p><strong>預計完成日期:</strong> <c:out value="${workOrder.scheduledDueDate}"/></p>
      <p><strong>實際開始日期:</strong> 
          <c:out value="${workOrder.actualStartDate}"/>
          <c:if test="${empty workOrder.actualStartDate && workOrder.status != 'Completed' && workOrder.status != 'Cancelled'}">
            <a href="${pageContext.request.contextPath}/workorders?action=start&id=${workOrder.workOrderId}" class="btn-sm">開始工單</a>
          </c:if>
      </p>
      <p><strong>實際完成日期:</strong>
          <c:out value="${workOrder.actualCompletionDate}"/>
          <c:if test="${empty workOrder.actualCompletionDate && workOrder.status == 'In Progress'}">
            <a href="${pageContext.request.contextPath}/workorders?action=complete&id=${workOrder.workOrderId}" class="btn-sm">完成工單</a>
          </c:if>
      </p>
      <p><strong>狀態:</strong> <c:out value="${workOrder.status}"/></p>
      <p><strong>備註:</strong> <c:out value="${workOrder.notes}"/></p>
      <%-- Added createDate and updateDate to view section --%>
      <p><strong>創建日期:</strong> <c:out value="${workOrder.createDate}"/></p>
      <p><strong>更新日期:</strong> <c:out value="${workOrder.updateDate}"/></p>
      <p><a href="${pageContext.request.contextPath}/workorders?action=list">返回列表</a></p>
    </c:if>
  </c:if>

  <%-- 編輯工單表單 --%>
  <c:if test="${action == 'edit'}">
    <h2>編輯生產工單</h2>
    <form action="${pageContext.request.contextPath}/workorders" method="post" class="data-form">
      <input type="hidden" name="action" value="update" />
      <input type="hidden" name="workOrderId" value="${workOrder.workOrderId}" />

      <div class="form-group">
        <label for="workOrderNumber">工單編號:</label>
        <input type="text" id="workOrderNumber" name="workOrderNumber" required value="<c:out value='${workOrder.workOrderNumber}'/>" />
      </div>

      <div class="form-group">
        <label for="productId">生產產品:</label>
        <select id="productId" name="productId" required>
          <option value="">--請選擇產品--</option>
          <c:forEach var="product" items="${products}">
            <option value="<c:out value='${product.productId}'/>" 
              ${workOrder.product.productId == product.productId ? 'selected' : ''}>
              <c:out value="${product.productCode}"/> - <c:out value="${product.productName}"/>
            </option>
          </c:forEach>
        </select>
      </div>

      <div class="form-group">
        <label for="quantity">數量:</label>
        <input type="number" step="1" id="quantity" name="quantity" min="1" required value="<c:out value='${workOrder.quantity}'/>" />
      </div>

      <div class="form-group">
        <label for="unit">單位:</label>
        <input type="text" id="unit" name="unit" required value="<c:out value='${workOrder.unit}'/>" />
      </div>

      <div class="form-group">
        <label for="scheduledStartDate">預計開始日期:</label>
        <%-- CORRECTED: Directly output formatted date string for input type="date" --%>
        <input type="date" id="scheduledStartDate" name="scheduledStartDate" required 
               value="<c:out value='${workOrder.scheduledStartDate}'/>" />
      </div>

      <div class="form-group">
        <label for="scheduledDueDate">預計完成日期:</label>
        <%-- CORRECTED: Directly output formatted date string for input type="date" --%>
        <input type="date" id="scheduledDueDate" name="scheduledDueDate" required 
               value="<c:out value='${workOrder.scheduledDueDate}'/>" />
      </div>

      <div class="form-group">
        <label for="status">狀態:</label>
        <select id="status" name="status" required>
          <option value="Pending" ${workOrder.status == 'Pending' ? 'selected' : ''}>待處理</option>
          <option value="In Progress" ${workOrder.status == 'In Progress' ? 'selected' : ''}>進行中</option>
          <option value="Completed" ${workOrder.status == 'Completed' ? 'selected' : ''}>已完成</option>
          <option value="Cancelled" ${workOrder.status == 'Cancelled' ? 'selected' : ''}>已取消</option>
        </select>
      </div>

      <div class="form-group">
        <label for="notes">備註:</label>
        <textarea id="notes" name="notes" rows="5"><c:out value="${workOrder.notes}"/></textarea>
      </div>

      <input type="submit" value="更新工單" />
    </form>
    <p><a href="${pageContext.request.contextPath}/workorders?action=list">返回列表</a></p>
  </c:if>

</section>
</body>
</html>
