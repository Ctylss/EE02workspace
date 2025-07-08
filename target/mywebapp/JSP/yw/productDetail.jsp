<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<c:set var="isNew" value="${empty requestScope.product}"/>
<c:set var="formAction" value="${isNew ? 'add' : 'update'}"/>
<c:set var="pageTitle" value="${isNew ? '新增產品' : '編輯產品'}"/>
<% request.setAttribute("pageTitle", pageTitle); %>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title><c:out value="${pageTitle}"/></title>

  <!-- Bootstrap 5 CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
  <!-- FontAwesome -->
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
  
  <style>
    body {
      font-family: 'Noto Sans TC', sans-serif;
      background-color: #f4f6f8;
      margin: 0;
      padding: 0;
      color: #34495e;
    }
    .form-container {
      max-width: 700px;
      margin: 40px auto;
      padding: 36px 48px;
      background-color: #fff;
      border-radius: 12px;
      box-shadow: 0 6px 20px rgba(0,0,0,0.1);
      transition: box-shadow 0.3s ease;
    }
    .form-container:hover {
      box-shadow: 0 8px 28px rgba(0,0,0,0.15);
    }
    h2 {
      color: #2c3e50;
      margin-bottom: 32px;
      font-weight: 700;
      text-align: center;
      letter-spacing: 0.05em;
    }
    .error-message {
      background-color: #fdecea;
      color: #d32f2f;
      border-left: 5px solid #d32f2f;
      padding: 14px 18px;
      border-radius: 6px;
      margin-bottom: 26px;
      font-weight: 600;
      font-size: 1rem;
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .form-static-text {
      background-color: #f0f4f8;
      border-radius: 8px;
      font-weight: 700;
      font-size: 1rem;
      color: #34495e;
      padding: 10px 14px;
      user-select: none;
      margin-top: 0.375rem;
    }
    @media (max-width: 480px) {
      .form-container {
        margin: 20px 15px;
        padding: 24px 20px;
      }
    }
  </style>
</head>
<body>

<div class="form-container shadow-sm">
  <h2><c:out value="${pageTitle}"/></h2>

  <c:if test="${not empty requestScope.errorMessage}">
    <div class="error-message">
      <i class="fas fa-exclamation-triangle"></i>
      <span><c:out value="${requestScope.errorMessage}"/></span>
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/products/${formAction}" method="post" class="needs-validation" novalidate>
    <c:if test="${not isNew}">
      <input type="hidden" name="productId" value="${product.productId}" />
      <div class="mb-3">
        <label class="form-label fw-bold">產品ID:</label>
        <div class="form-static-text"><c:out value="${product.productId}"/></div>
      </div>
    </c:if>

    <div class="mb-3">
      <label for="productCode" class="form-label fw-semibold">產品代碼 <span class="text-danger">*</span></label>
      <input type="text" class="form-control" id="productCode" name="productCode"
             value="<c:out value='${product.productCode}'/>"
             ${isNew ? '' : 'readonly'} required />
      <div class="form-text text-muted">
        <c:if test="${not isNew}">
          產品代碼在編輯模式下不可修改。
        </c:if>
      </div>
      <div class="invalid-feedback">請輸入產品代碼。</div>
    </div>

    <div class="mb-3">
      <label for="productName" class="form-label fw-semibold">產品名稱 <span class="text-danger">*</span></label>
      <input type="text" class="form-control" id="productName" name="productName"
             value="<c:out value='${product.productName}'/>" required />
      <div class="invalid-feedback">請輸入產品名稱。</div>
    </div>

    <div class="mb-3">
      <label for="category" class="form-label fw-semibold">類別</label>
      <input type="text" class="form-control" id="category" name="category"
             value="<c:out value='${product.category}'/>" />
    </div>

    <div class="mb-3">
      <label for="unit" class="form-label fw-semibold">單位 <span class="text-danger">*</span></label>
      <input type="text" class="form-control" id="unit" name="unit"
             value="<c:out value='${product.unit}'/>" required />
      <div class="invalid-feedback">請輸入單位。</div>
    </div>

    <div class="mb-3">
      <label for="sellingPrice" class="form-label fw-semibold">銷售價格 <span class="text-danger">*</span></label>
      <input type="number" class="form-control" id="sellingPrice" name="sellingPrice" step="0.01" min="0"
             value="<c:out value='${product.sellingPrice}'/>" required />
      <div class="invalid-feedback">請輸入正確的銷售價格。</div>
    </div>

    <div class="mb-3">
      <label for="cost" class="form-label fw-semibold">成本</label>
      <input type="number" class="form-control" id="cost" name="cost" step="0.01" min="0"
             value="<c:out value='${product.cost}'/>" />
      <div class="form-text">如果無成本，可以留空。</div>
    </div>

    <div class="form-check mb-3">
      <input class="form-check-input" type="checkbox" id="isActive" name="isActive"
             <c:if test="${product.isActive}">checked</c:if> />
      <label class="form-check-label fw-semibold" for="isActive">啟用</label>
    </div>

    <div class="mb-3">
      <label for="imageUrl" class="form-label fw-semibold">圖片URL</label>
      <input type="url" class="form-control" id="imageUrl" name="imageUrl"
             value="<c:out value='${product.imageUrl}'/>" />
      <div class="form-text">可選的產品圖片連結。</div>
    </div>

    <div class="mb-3">
      <label for="description" class="form-label fw-semibold">描述</label>
      <textarea class="form-control" id="description" name="description" rows="5"><c:out value='${product.description}'/></textarea>
      <div class="form-text">產品的詳細描述。</div>
    </div>

    <div class="d-flex gap-3">
      <button type="submit" class="btn btn-success">
        <i class="fas fa-save"></i> <c:out value="${isNew ? '新增產品' : '更新產品'}"/>
      </button>
      <a href="${pageContext.request.contextPath}/products/list" class="btn btn-secondary">
        <i class="fas fa-times-circle"></i> 取消
      </a>
    </div>
  </form>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Client-side validation -->
<script>
(() => {
  'use strict'
  const forms = document.querySelectorAll('.needs-validation')
  Array.from(forms).forEach(form => {
    form.addEventListener('submit', event => {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }
      form.classList.add('was-validated')
    }, false)
  })
})();
</script>

</body>
</html>
