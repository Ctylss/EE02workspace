<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title><c:out value="${pageTitle}"/></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@300;400;500;700&display=swap" rel="stylesheet">

    <style>
        /* --- Base Styles --- */
        body {
            font-family: 'Noto Sans TC', sans-serif; /* Ensure Google Font is loaded */
            background-color: #f4f6f8; /* Light gray background */
            margin: 0;
            padding: 0;
            color: #34495e; /* Darker gray for main text */
            line-height: 1.6; /* Improve readability of text blocks */
        }

        /* --- Layout & Container --- */
        .container-main {
            max-width: 1000px;
            margin: 40px auto; /* Center the container with vertical spacing */
            padding: 30px 35px;
            background-color: #fff; /* White background for the card */
            border-radius: 12px; /* Slightly more rounded corners */
            box-shadow: 0 8px 25px rgba(0,0,0,0.12); /* Slightly stronger, diffused shadow */
        }

        /* --- Headings --- */
        h2, h3 {
            color: #2c3e50; /* Darker blue-gray for headings */
            margin-bottom: 25px; /* More space below headings */
            font-weight: 700; /* Bolder headings */
        }
        h3 {
            font-size: 1.5rem; /* Adjust sub-heading size */
            margin-top: 15px;
            margin-bottom: 15px;
        }

        /* --- Alert Messages --- */
        .alert-base {
            padding: 14px 18px;
            border-radius: 6px;
            margin-bottom: 25px;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 10px; /* Space between icon and text */
            box-shadow: 0 2px 5px rgba(0,0,0,0.05); /* Subtle shadow for alerts */
        }

        .alert-error {
            background-color: #fdecea;
            color: #d32f2f;
            border-left: 5px solid #d32f2f;
        }

        .alert-success {
            background-color: #e6ffed;
            color: #2e7d32;
            border-left: 5px solid #2e7d32;
        }

        /* --- Toolbar / Action Buttons Group --- */
        .toolbar {
            margin-bottom: 30px; /* More space below toolbar */
            display: flex;
            gap: 15px;
            flex-wrap: wrap; /* Allows buttons to wrap on smaller screens */
        }

        .toolbar .btn-primary { /* Targeting Bootstrap's btn-primary directly */
            background-color: #3498db; /* Blue */
            color: white;
            padding: 8px 18px; /* Slightly more horizontal padding */
            border-radius: 6px;
            text-decoration: none;
            font-weight: 500;
            transition: background-color 0.3s ease, transform 0.15s ease, box-shadow 0.3s ease;
            border: none; /* Ensure no default bootstrap border */
        }
        .toolbar .btn-primary:hover {
            background-color: #2980b9; /* Darker blue on hover */
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(0,0,0,0.1); /* Add shadow on hover */
        }

        /* --- Data Table --- */
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px; /* Adjust spacing */
            font-size: 0.95rem;
            border-radius: 8px; /* Apply border-radius to the table itself */
            overflow: hidden; /* Ensures border-radius is applied correctly with overflow */
            box-shadow: 0 4px 15px rgba(0,0,0,0.08); /* Subtle shadow for the table */
        }

        .data-table th, .data-table td {
            padding: 15px 15px; /* Slightly more padding for better spacing */
            text-align: left;
            border-bottom: 1px solid #eee; /* Lighter border */
            vertical-align: middle;
        }

        .data-table thead th {
            background-color: #e9ecef; /* Slightly darker header background */
            color: #495057; /* Darker text for headers */
            font-weight: 600;
            text-transform: uppercase; /* Make headers slightly more pronounced */
            letter-spacing: 0.03em;
        }
        .data-table tbody tr:last-child td {
            border-bottom: none; /* Remove bottom border for the last row */
        }

        .data-table td.text-end {
            text-align: right;
        }

        .data-table tr:hover {
            background-color: #eef2f5; /* Slightly more noticeable hover effect */
        }

        /* --- Icon Buttons (View, Edit, Delete) --- */
        .data-table .actions-column {
            white-space: nowrap; /* Prevent buttons from wrapping to next line */
            min-width: 150px; /* Ensure enough space for 3 buttons and spacing */
        }
        .btn-icon {
            border: none;
            background: none;
            cursor: pointer;
            font-size: 1.15rem; /* Adjusted slightly smaller for a more button-like feel */
            margin: 0 4px; /* Adjust margin for spacing between buttons */
            padding: 8px 10px; /* Add more padding for easier clicking and button shape */
            border-radius: 5px; /* Rounded corners for the button background */
            transition: background-color 0.3s ease, color 0.3s ease, transform 0.2s ease;
            display: inline-flex; /* Allows icon and text to center if needed, better control */
            align-items: center;
            justify-content: center;
            text-decoration: none; /* Remove underline for links */
        }
        .btn-icon i {
            pointer-events: none; /* Ensure clicks go to the button, not the icon */
        }
        .btn-icon.view-icon { color: #28a745; } /* Bootstrap success green */
        .btn-icon.edit-icon { color: #ffc107; } /* Bootstrap warning yellow */
        .btn-icon.delete-icon { color: #dc3545; } /* Bootstrap danger red */

        .btn-icon:hover {
            transform: translateY(-1px); /* Subtle lift on hover */
            box-shadow: 0 2px 8px rgba(0,0,0,0.1); /* Subtle shadow on hover */
        }
        .btn-icon.view-icon:hover {
            background-color: #28a745;
            color: white !important;
        }
        .btn-icon.edit-icon:hover {
            background-color: #ffc107;
            color: white !important;
        }
        .btn-icon.delete-icon:hover {
            background-color: #dc3545;
            color: white !important;
        }

        /* --- Form Elements --- */
        form label {
            font-weight: 600;
            margin-top: 18px; /* More space above labels */
            margin-bottom: 5px; /* Small space below labels */
            display: block; /* Ensure label takes full width */
            color: #555;
        }
        form select,
        form input[type="number"],
        form input[type="text"] {
            width: 100%;
            padding: 12px 14px; /* Slightly more padding for input fields */
            margin-top: 0; /* Removed default margin-top from previous version */
            border: 1px solid #ced4da; /* Bootstrap default border color */
            border-radius: 0.375rem; /* Bootstrap default border-radius */
            font-size: 1rem;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        form select:focus,
        form input[type="number"]:focus,
        form input[type="text"]:focus {
            outline: none;
            border-color: #80bdff; /* Bootstrap focus blue */
            box-shadow: 0 0 0 0.25rem rgba(0, 123, 255, 0.25); /* Bootstrap focus shadow */
        }

        /* --- Submit Button --- */
        form input[type="submit"] {
            margin-top: 30px; /* More space above submit button */
            background-color: #28a745; /* Bootstrap success green */
            color: white;
            border: none;
            padding: 12px 25px; /* Larger padding for the button */
            font-size: 1.1rem; /* Slightly larger font size */
            border-radius: 0.375rem; /* Bootstrap default border-radius */
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
        }
        form input[type="submit"]:hover {
            background-color: #218838; /* Darker green on hover */
            transform: translateY(-1px); /* Subtle lift on hover */
            box-shadow: 0 4px 10px rgba(0,0,0,0.1); /* Add shadow on hover */
        }

        /* --- Form Link (Return to list) --- */
        .form-link {
            margin-top: 25px; /* Adjust spacing */
            display: inline-block;
            font-weight: 500; /* Slightly less bold */
            color: #007bff; /* Bootstrap primary blue for links */
            text-decoration: none; /* Remove default underline */
            cursor: pointer;
            transition: color 0.3s ease, text-decoration 0.3s ease;
        }
        .form-link:hover {
            color: #0056b3; /* Darker blue on hover */
            text-decoration: underline; /* Add underline on hover */
        }

        /* Responsive adjustments for smaller screens */
        @media (max-width: 768px) {
            .container-main {
                margin: 20px auto;
                padding: 20px;
            }
            .data-table th, .data-table td {
                padding: 10px;
                font-size: 0.9rem;
            }
            .btn-icon {
                font-size: 1rem; /* Slightly smaller for mobile */
                margin: 0 3px; /* Tighter spacing */
                padding: 6px 8px;
            }
            .data-table .actions-column {
                min-width: unset; /* Allow column to shrink */
            }
        }

        @media (max-width: 576px) {
            .toolbar {
                flex-direction: column; /* Stack buttons vertically */
                gap: 10px;
            }
            .toolbar .btn-primary {
                width: 100%; /* Make buttons full width */
                text-align: center;
            }
            /* Consider making table scrollable horizontally for very small screens if columns are too wide */
            .table-responsive-sm {
                overflow-x: auto;
            }
        }
    </style>
</head>
<body>

<div class="container-main">

    <c:if test="${not empty errorMessage}">
        <div class="alert-error alert-base">
            <i class="fas fa-exclamation-triangle"></i>
            <span>錯誤: <c:out value="${errorMessage}"/></span>
        </div>
    </c:if>

    <c:if test="${not empty sessionScope.message}">
        <div class="alert-success alert-base">
            <i class="fas fa-check-circle"></i>
            <span><c:out value="${sessionScope.message}"/></span>
        </div>
        <c:remove var="message" scope="session"/>
    </c:if>

    <c:if test="${action == 'list' || action == 'listByProduct' || action == null}">
        <c:choose>
            <c:when test="${action == 'listByProduct'}">
                <h2>產品 "<c:out value="${productName}"/>" 的用料清單</h2> <%-- 使用 productName 屬性 --%>
                <c:if test="${not empty currentProductId}">
                    <h3>查看產品 ID: <c:out value="${currentProductId}"/> 的用料</h3>
                </c:if>
            </c:when>
            <c:otherwise>
                <h2>所有產品用料清單</h2>
            </c:otherwise>
        </c:choose>

        <div class="toolbar">
            <a href="${pageContext.request.contextPath}/bom?action=new" class="btn btn-primary">新增 BOM 項目</a>
            <a href="${pageContext.request.contextPath}/bom?action=list" class="btn btn-primary">顯示所有 BOM</a>
        </div>

        <table class="data-table table table-striped table-hover">
            <thead>
                <tr>
                    <th>BOM ID</th>
                    <th>產品名稱</th>
                    <th>物料名稱</th>
                    <th class="text-end">消耗數量</th>
                    <th>單位</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty boms}">
                        <c:forEach var="bom" items="${boms}">
                            <tr>
                                <td><c:out value="${bom.bomId}"/></td>
                                <td><c:out value="${bom.productName}"/></td>
                                <td><c:out value="${bom.materialName}"/></td>
                                <td class="text-end"><fmt:formatNumber value="${bom.quantity}" pattern="#,##0.0000"/></td>
                                <td><c:out value="${bom.materialUnit}"/></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/bom?action=view&id=${bom.bomId}" class="btn-icon view-icon" title="查看"><i class="fas fa-eye"></i></a>
                                    <a href="${pageContext.request.contextPath}/bom?action=edit&id=${bom.bomId}" class="btn-icon edit-icon" title="編輯"><i class="fas fa-edit"></i></a>
                                    <button type="button" class="btn-icon delete-icon" title="刪除"
                                            onclick="if(confirm('確定要刪除產品 <c:out value="${bom.productName}"/> 的物料 <c:out value="${bom.materialName}"/> 用量嗎？這將無法恢復！')) { window.location.href='${pageContext.request.contextPath}/bom?action=delete&id=${bom.bomId}'; }">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="6" class="text-center">沒有找到任何產品用料清單項目。</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </c:if>

    <c:if test="${action == 'new'}">
        <h2>新增產品用料</h2>
        <form action="${pageContext.request.contextPath}/bom" method="post" class="needs-validation" novalidate>
            <input type="hidden" name="action" value="add" />

            <label for="productId">產品:</label>
            <select id="productId" name="productId" class="form-select" required>
                <option value="">請選擇產品</option>
                <c:forEach var="product" items="${products}">
                    <option value="${product.productId}" ${param.productId == product.productId ? 'selected' : ''}>
                        <c:out value="${product.productName}"/> (ID: <c:out value="${product.productId}"/>)
                    </option>
                </c:forEach>
            </select>

            <label for="materialId">物料:</label>
            <select id="materialId" name="materialId" class="form-select" required>
                <option value="">請選擇物料</option>
                <c:forEach var="material" items="${materials}">
                    <option value="${material.materialId}" ${param.materialId == material.materialId ? 'selected' : ''}>
                        <c:out value="${material.materialName}"/> (<c:out value="${material.unit}"/>) (ID: <c:out value="${material.materialId}"/>)
                    </option>
                </c:forEach>
            </select>

            <label for="quantity">消耗數量:</label>
            <input type="number" id="quantity" name="quantity" step="0.0001" min="0.0001" class="form-control" required
                    value="<c:out value='${param.quantity != null ? param.quantity : "1.0000"}'/>" />

            <input type="submit" value="提交新增" class="btn btn-success" />
        </form>
        <a href="${pageContext.request.contextPath}/bom?action=list" class="form-link">返回列表</a>
    </c:if>

    <c:if test="${action == 'view'}">
        <h2>產品用料詳情</h2>
        <c:if test="${not empty bom}">
            <dl class="row">
                <dt class="col-sm-3">BOM ID:</dt>
                <dd class="col-sm-9"><c:out value="${bom.bomId}"/></dd>

                <dt class="col-sm-3">產品:</dt>
                <dd class="col-sm-9"><c:out value="${bom.productName}"/> (ID: <c:out value="${bom.productId}"/>)</dd>

                <dt class="col-sm-3">物料:</dt>
                <dd class="col-sm-9"><c:out value="${bom.materialName}"/> (ID: <c:out value="${bom.materialId}"/>)</dd>

                <dt class="col-sm-3">消耗數量:</dt>
                <dd class="col-sm-9"><fmt:formatNumber value="${bom.quantity}" pattern="#,##0.0000"/> <c:out value="${bom.materialUnit}"/></dd>
            </dl>
            <p>
                <a href="${pageContext.request.contextPath}/bom?action=edit&id=${bom.bomId}" class="btn btn-warning me-2">
                    <i class="fas fa-edit"></i> 編輯此用料
                </a>
                <a href="${pageContext.request.contextPath}/bom?action=list" class="btn btn-secondary">
                    <i class="fas fa-list"></i> 返回列表
                </a>
            </p>
        </c:if>
        <c:if test="${empty bom}">
            <p>產品用料項目不存在或無法載入。</p>
            <a href="${pageContext.request.contextPath}/bom?action=list" class="form-link">返回列表</a>
        </c:if>
    </c:if>

    <c:if test="${action == 'edit'}">
        <h2>編輯產品用料</h2>
        <c:if test="${not empty bom}">
            <form action="${pageContext.request.contextPath}/bom" method="post" class="needs-validation" novalidate>
                <input type="hidden" name="action" value="update" />
                <input type="hidden" name="bomId" value="${bom.bomId}" />

                <p><strong>BOM ID:</strong> <c:out value="${bom.bomId}"/></p>

                <label for="productId">產品:</label>
                <select id="productId" name="productId" class="form-select" required>
                    <option value="">請選擇產品</option>
                    <c:forEach var="product" items="${products}">
                        <option value="${product.productId}" ${bom.productId == product.productId ? 'selected' : ''}>
                            <c:out value="${product.productName}"/> (ID: <c:out value="${product.productId}"/>)
                        </option>
                    </c:forEach>
                </select>

                <label for="materialId">物料:</label>
                <select id="materialId" name="materialId" class="form-select" required>
                    <option value="">請選擇物料</option>
                    <c:forEach var="material" items="${materials}">
                        <option value="${material.materialId}" ${bom.materialId == material.materialId ? 'selected' : ''}>
                            <c:out value="${material.materialName}"/> (<c:out value="${material.unit}"/>) (ID: <c:out value="${material.materialId}"/>)
                        </option>
                    </c:forEach>
                </select>

                <label for="quantity">消耗數量:</label>
                <input type="number" id="quantity" name="quantity" step="0.0001" min="0.0001" class="form-control" required
                        value="<fmt:formatNumber value='${bom.quantity}' pattern='0.0000'/>" />

                <input type="submit" value="提交更新" class="btn btn-success mt-3" />
            </form>
            <p class="mt-3">
                <a href="${pageContext.request.contextPath}/bom?action=view&id=${bom.bomId}" class="btn btn-secondary me-2">
                    <i class="fas fa-eye"></i> 返回詳情
                </a>
                <a href="${pageContext.request.contextPath}/bom?action=list" class="btn btn-secondary">
                    <i class="fas fa-list"></i> 返回列表
                </a>
            </p>
        </c:if>
        <c:if test="${empty bom}">
            <p>產品用料項目不存在或無法載入編輯頁面。</p>
            <a href="${pageContext.request.contextPath}/bom?action=list" class="form-link">返回列表</a>
        </c:if>
    </c:if>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
(() => {
    'use strict';
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
})();
</script>

</body>
</html>
