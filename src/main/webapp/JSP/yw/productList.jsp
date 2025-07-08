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
            background-color: #f8f9fa; /* Lighter background */
            margin: 0;
            padding: 0;
            color: #34495e; /* Darker gray for main text */
            line-height: 1.6;
        }

        /* --- New styles for sidebar integration --- */
        .app-wrapper {
            display: flex;
            min-height: 100vh; /* Ensures wrapper takes full viewport height */
        }

        .main-sidebar {
            width: 250px; /* Adjust as needed */
            background-color: #2c3e50; /* Darker background for sidebar */
            color: white;
            padding: 20px;
            box-shadow: 2px 0 5px rgba(0,0,0,0.1);
            flex-shrink: 0; /* Prevents sidebar from shrinking */
        }

        .main-right-content-wrapper {
            flex-grow: 1; /* Allows content wrapper to take remaining space */
            display: flex;
            flex-direction: column;
        }

        .main-header {
            background-color: #ffffff;
            padding: 15px 30px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            z-index: 1000; /* Ensure header is above content */
        }

        .actual-page-content {
            flex-grow: 1; /* Allows actual content to take available height */
            padding: 20px;
            overflow-y: auto; /* Allows content to scroll if it overflows */
        }

        .main-footer {
            background-color: #ecf0f1;
            padding: 15px 20px;
            text-align: center;
            color: #7f8c8d;
            border-top: 1px solid #e0e6ea;
            flex-shrink: 0; /* Prevents footer from shrinking */
        }

        /* --- Main Container / Section (Existing styles, potentially adjusted for new layout) --- */
        .data-table-section {
            max-width: 1100px; /* Keep max-width for content within the main area */
            margin: 40px auto; /* Center with more vertical spacing */
            padding: 30px 35px;
            background-color: #fff; /* White background for the card */
            border-radius: 12px; /* Slightly more rounded corners */
            box-shadow: 0 8px 25px rgba(0,0,0,0.12); /* Stronger, diffused shadow */
        }

        /* --- Headings --- */
        h2 {
            margin-bottom: 25px;
            color: #2c3e50;
            border-bottom: 3px solid #3498db; /* Blue border */
            padding-bottom: 8px;
            font-weight: 700; /* Bolder */
        }

        /* --- Alert Messages (New Styling) --- */
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

        /* --- Toolbar --- */
        .toolbar {
            margin-bottom: 30px; /* More space below toolbar */
            display: flex;
            gap: 15px;
            flex-wrap: wrap; /* Allows buttons to wrap on smaller screens */
            align-items: center; /* Vertically align items in the toolbar */
        }

        /* --- Primary Buttons (e.g., 新增產品) --- */
        .btn-primary {
            background-color: #3498db; /* Blue */
            color: white;
            padding: 10px 20px; /* Larger padding */
            border: none;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 500;
            transition: background-color 0.3s ease, transform 0.15s ease, box-shadow 0.3s ease;
            display: inline-flex; /* For better icon alignment if added */
            align-items: center;
            gap: 5px;
        }
        .btn-primary:hover {
            background-color: #2980b9; /* Darker blue on hover */
            transform: translateY(-2px); /* Lift effect */
            box-shadow: 0 4px 10px rgba(0,0,0,0.1); /* Add shadow on hover */
        }

        /* --- Search Form in Toolbar --- */
        .toolbar form {
            display: flex;
            gap: 8px; /* Space between input and button */
            align-items: center;
        }
        .toolbar .search-input {
            padding: 8px 12px; /* Adjusted padding */
            border: 1px solid #ced4da;
            border-radius: 6px;
            min-width: 200px; /* Slightly wider search input */
            font-size: 0.95rem;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        .toolbar .search-input:focus {
            outline: none;
            border-color: #80bdff;
            box-shadow: 0 0 0 0.25rem rgba(0, 123, 255, 0.25);
        }
        .toolbar form button {
            background-color: #6c757d; /* Muted gray for search button */
            color: white;
            padding: 8px 15px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            transition: background-color 0.3s ease;
        }
        .toolbar form button:hover {
            background-color: #5a6268; /* Darker gray on hover */
        }
        .toolbar a.show-all-link { /* Style for "顯示全部" link */
            color: #007bff;
            text-decoration: none;
            font-weight: 500;
            padding: 8px 0; /* Align with button vertical padding */
            transition: color 0.3s ease, text-decoration 0.3s ease;
        }
        .toolbar a.show-all-link:hover {
            color: #0056b3;
            text-decoration: underline;
        }


        /* --- Data Table --- */
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
            font-size: 0.95rem;
            border-radius: 8px;
            overflow: hidden; /* Ensures border-radius is applied correctly */
            box-shadow: 0 4px 15px rgba(0,0,0,0.08); /* Subtle shadow for the table */
        }

        .data-table thead {
            background-color: #e9ecef; /* Light gray header */
            color: #495057; /* Darker text for headers */
        }
        .data-table thead th {
            padding: 15px 15px;
            text-align: left;
            font-weight: 600;
            text-transform: uppercase; /* Make headers slightly more pronounced */
            letter-spacing: 0.03em;
        }
        .data-table tbody td {
            padding: 12px 15px;
            vertical-align: middle;
            border-bottom: 1px solid #dee2e6; /* Lighter border */
            word-break: break-word;
        }
        .data-table tbody tr:nth-child(even) {
            background-color: #f6f7f9; /* Subtle zebra striping */
        }
        .data-table tbody tr:hover {
            background-color: #eef2f5; /* More noticeable hover effect */
        }
        .data-table tbody tr:last-child td {
            border-bottom: none; /* Remove bottom border for the last row */
        }

        /* Ensure right alignment for price */
        .data-table td:nth-child(6) { /* Targeting Selling Price column */
            text-align: right;
        }

        /* --- Icon Buttons in Table Actions --- */
        .data-table .actions-column {
            white-space: nowrap; /* Prevent buttons from wrapping */
            min-width: 180px; /* Ensure enough space for 4 buttons + spacing */
        }
        .btn-icon {
            border: none;
            background: none;
            cursor: pointer;
            font-size: 1.2rem; /* Slightly larger icons */
            margin: 0 4px; /* Space between buttons */
            padding: 8px 10px; /* Add padding for a button-like feel */
            border-radius: 6px; /* Rounded corners for the button background */
            transition: background-color 0.3s ease, color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
            display: inline-flex; /* Allows icon and text to center, better control */
            align-items: center;
            justify-content: center;
            text-decoration: none; /* Remove underline for links */
        }
        .btn-icon i {
            pointer-events: none; /* Ensure clicks go to the button, not the icon */
        }

        /* Specific colors for icon buttons */
        .btn-icon.view-icon { color: #28a745; } /* Green */
        .btn-icon.edit-icon { color: #ffc107; } /* Yellow */
        .btn-icon.tools-icon { color: #17a2b8; } /* Info blue/cyan for BOM */
        .btn-icon.delete-icon { color: #dc3545; } /* Red */

        /* Hover effects for icon buttons */
        .btn-icon:hover {
            transform: translateY(-1px); /* Subtle lift on hover */
            box-shadow: 0 2px 8px rgba(0,0,0,0.1); /* Subtle shadow on hover */
        }
        .btn-icon.view-icon:hover { background-color: #28a745; color: white !important; }
        .btn-icon.edit-icon:hover { background-color: #ffc107; color: #343a40 !important; } /* Darker text for yellow background */
        .btn-icon.tools-icon:hover { background-color: #17a2b8; color: white !important; }
        .btn-icon.delete-icon:hover { background-color: #dc3545; color: white !important; }


        /* --- Form Styling (New/Edit/View) --- */
        .data-form {
            background: white;
            padding: 30px 35px; /* More padding */
            border-radius: 10px; /* Slightly more rounded */
            box-shadow: 0 4px 15px rgba(0,0,0,0.08); /* Stronger shadow */
            max-width: 650px; /* Slightly wider form */
            margin-bottom: 30px;
        }
        .data-form .form-group {
            margin-bottom: 20px; /* More space between groups */
        }
        .data-form label {
            font-weight: 600;
            display: block;
            margin-bottom: 8px; /* More space below labels */
            color: #555;
            font-size: 1.05rem;
        }
        .data-form input[type="text"],
        .data-form input[type="number"],
        .data-form input[type="url"],
        .data-form textarea {
            width: 100%;
            padding: 12px 14px; /* More padding for inputs */
            font-size: 1rem;
            border-radius: 0.375rem; /* Bootstrap default border-radius */
            border: 1px solid #ced4da;
            box-sizing: border-box;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        .data-form input[type="text"]:focus,
        .data-form input[type="number"]:focus,
        .data-form input[type="url"]:focus,
        .data-form textarea:focus {
            outline: none;
            border-color: #80bdff; /* Bootstrap focus blue */
            box-shadow: 0 0 0 0.25rem rgba(0, 123, 255, 0.25); /* Bootstrap focus shadow */
        }
        .data-form textarea {
            resize: vertical;
            min-height: 100px; /* Min height for textareas */
        }
        .data-form input[type="submit"] {
            background-color: #28a745; /* Green for submit */
            color: white;
            font-weight: 600;
            padding: 12px 25px; /* Larger padding */
            border: none;
            border-radius: 0.375rem;
            cursor: pointer;
            font-size: 1.1rem;
            transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
        }
        .data-form input[type="submit"]:hover {
            background-color: #218838; /* Darker green on hover */
            transform: translateY(-1px);
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
        .checkbox-group {
            display: flex;
            align-items: center;
            gap: 10px; /* More space */
            margin-bottom: 20px;
        }
        .checkbox-group input[type="checkbox"] {
            transform: scale(1.2); /* Slightly larger checkbox */
        }
        .form-static-text {
            background: #e9ecef; /* Light background for static text */
            padding: 10px 14px;
            border-radius: 6px;
            color: #495057;
            user-select: text; /* Allow selection */
            margin-top: 4px;
            font-weight: 500;
            line-height: 1.5;
        }
        .help-text {
            font-size: 0.85em;
            color: #6c757d; /* Muted gray for help text */
            margin-top: 5px;
            display: block; /* Ensure it takes full line */
        }
        /* Styling for the return links in form/view pages */
        .form-links-group {
            margin-top: 25px; /* Space above links */
            display: flex;
            gap: 15px; /* Space between links */
            flex-wrap: wrap;
        }
        .form-links-group a {
            color: #007bff; /* Bootstrap primary blue */
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s ease, text-decoration 0.3s ease;
        }
        .form-links-group a:hover {
            color: #0056b3;
            text-decoration: underline;
        }
        .form-links-group a i {
            margin-right: 5px;
        }


        /* Responsive adjustments */
        @media (max-width: 768px) {
            .app-wrapper {
                flex-direction: column; /* Stack sidebar and content vertically */
            }

            .main-sidebar {
                width: 100%; /* Full width sidebar on smaller screens */
                height: auto; /* Allow sidebar height to adjust */
                padding: 15px;
            }

            .data-table-section {
                margin: 20px auto;
                padding: 20px;
            }
            .data-table thead th, .data-table tbody td {
                padding: 10px;
                font-size: 0.85rem;
            }
            .toolbar {
                flex-direction: column; /* Stack toolbar items */
                align-items: flex-start;
                gap: 10px;
            }
            .toolbar .btn-primary, .toolbar form, .toolbar form button, .toolbar a.show-all-link {
                width: 100%; /* Full width for toolbar items */
                justify-content: center; /* Center content for buttons */
                text-align: center;
            }
            .toolbar .search-input {
                min-width: unset; /* Remove min-width for mobile */
                width: 100%; /* Full width */
            }
            .data-table .actions-column {
                min-width: unset; /* Allow column to shrink */
                text-align: center; /* Center icons on mobile */
            }
            .btn-icon {
                font-size: 1.1rem;
                margin: 0 2px; /* Tighter spacing */
                padding: 6px 8px;
            }
            .data-form {
                padding: 20px;
            }
            .form-links-group {
                flex-direction: column;
                gap: 10px;
            }
        }
    </style>
</head>
<body>
    <div class="app-wrapper">
        <aside class="main-sidebar" role="complementary" aria-label="側邊欄">
            <%-- Include the sidebar.jsp here --%>
            <jsp:include page="/common/sidebar.jsp" />
        </aside>

        <div class="main-right-content-wrapper">
            <header class="main-header">
                <%-- Include the header.jsp here --%>
                <jsp:include page="/common/header.jsp" />
            </header>

            <div class="actual-page-content">
                <section class="data-table-section">

                    <%-- Error Message Display (New Styling) --%>
                    <c:if test="${not empty errorMessage}">
                        <div class="alert-error alert-base">
                            <i class="fas fa-exclamation-triangle"></i>
                            <span>錯誤: <c:out value="${errorMessage}"/></span>
                        </div>
                    </c:if>

                    <%-- Success Message Display (New Styling) --%>
                    <c:if test="${not empty sessionScope.message}">
                        <div class="alert-success alert-base">
                            <i class="fas fa-check-circle"></i>
                            <span><c:out value="${sessionScope.message}"/></span>
                        </div>
                        <c:remove var="message" scope="session"/>
                    </c:if>

                    <%-- Product List Section --%>
                    <c:if test="${action == 'list' || action == 'byCategory' || action == null}">
                        <h2>產品主檔列表</h2>
                        <div class="toolbar">
                            <a href="${pageContext.request.contextPath}/products/new" class="btn-primary">
                                <i class="fas fa-plus-circle"></i> 新增產品
                            </a>
                            <form action="${pageContext.request.contextPath}/products/byCategory" method="get">
                                <input type="text" name="category" placeholder="按類別篩選..." class="search-input" value="${param.category}" />
                                <button type="submit" class="btn btn-secondary">
                                    <i class="fas fa-filter"></i> 篩選
                                </button>
                            </form>
                            <a href="${pageContext.request.contextPath}/products/list" class="show-all-link">
                                <i class="fas fa-list"></i> 顯示全部
                            </a>
                        </div>
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>產品編號</th>
                                    <th>產品代碼</th>
                                    <th>產品名稱</th>
                                    <th>類別</th>
                                    <th>單位</th>
                                    <th>銷售價格</th>
                                    <th class="actions-column">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty products}">
                                        <c:forEach var="product" items="${products}">
                                            <tr>
                                                <td><c:out value="${product.productId}"/></td>
                                                <td><c:out value="${product.productCode}"/></td>
                                                <td><c:out value="${product.productName}"/></td>
                                                <td><c:out value="${product.category}"/></td>
                                                <td><c:out value="${product.unitOfMeasure}"/></td>
                                                <td><fmt:formatNumber value="${product.sellingPrice}" pattern="#,##0.00"/></td>
                                                <td class="actions-column">
                                                    <a href="${pageContext.request.contextPath}/products/view/${product.productId}" class="btn-icon view-icon" title="查看"><i class="fas fa-eye"></i></a>
                                                    <a href="${pageContext.request.contextPath}/products/edit/${product.productId}" class="btn-icon edit-icon" title="編輯"><i class="fas fa-edit"></i></a>
                                                    <a href="${pageContext.request.contextPath}/bom/listByProduct?productId=${product.productId}&productName=${product.productName}" class="btn-icon tools-icon" title="查看產品用料"><i class="fas fa-tools"></i></a>
                                                    <button type="button" class="btn-icon delete-icon" title="刪除"
                                                            onclick="confirmAndDelete('${product.productName}', '${product.productId}', '${pageContext.request.contextPath}/products/delete/${product.productId}');">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="7" class="text-center">沒有找到任何產品。</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </c:if>

                    <%-- Add Product Form --%>
                    <c:if test="${action == 'new'}">
                        <h2>新增產品</h2>
                        <form action="${pageContext.request.contextPath}/products/add" method="post" class="data-form">
                            <div class="form-group">
                                <label for="productCode">產品代碼:</label>
                                <input type="text" id="productCode" name="productCode" required value="<c:out value="${not empty product ? product.productCode : ''}"/>" />
                            </div>
                            <div class="form-group">
                                <label for="productName">產品名稱:</label>
                                <input type="text" id="productName" name="productName" required value="<c:out value="${not empty product ? product.productName : ''}"/>" />
                            </div>
                            <div class="form-group">
                                <label for="category">類別:</label>
                                <input type="text" id="category" name="category" value="<c:out value="${not empty product ? product.category : ''}"/>" />
                            </div>
                            <div class="form-group">
                                <label for="unit">單位:</label>
                                <input type="text" id="unit" name="unit" required value="<c:out value="${not empty product ? product.unitOfMeasure : ''}"/>" />
                            </div>
                            <div class="form-group">
                                <label for="sellingPrice">銷售價格:</label>
                                <input type="number" step="0.01" id="sellingPrice" name="sellingPrice" required min="0" value="<c:out value="${not empty product ? product.sellingPrice : ''}"/>" />
                            </div>
                            <div class="form-group">
                                <label for="cost">成本:</label>
                                <input type="number" step="0.01" id="cost" name="cost" min="0" value="<c:out value="${not empty product ? product.cost : ''}"/>" />
                            </div>
                            <div class="form-group checkbox-group">
                                <input type="checkbox" id="isActive" name="isActive" ${(product != null && product.isActive) ? 'checked' : ''} />
                                <label for="isActive">啟用</label>
                            </div>
                            <div class="form-group">
                                <label for="imageUrl">圖片URL:</label>
                                <input type="url" id="imageUrl" name="imageUrl" value="<c:out value="${not empty product ? product.imageUrl : ''}"/>" />
                            </div>
                            <div class="form-group">
                                <label for="description">描述:</label>
                                <textarea id="description" name="description" rows="5"><c:out value="${not empty product ? product.description : ''}"/></textarea>
                            </div>
                            <input type="submit" value="提交新增" />
                        </form>
                        <div class="form-links-group">
                            <a href="${pageContext.request.contextPath}/products/list"><i class="fas fa-list"></i> 返回列表</a>
                        </div>
                    </c:if>

                    <%-- Product Details View Section --%>
                    <c:if test="${action == 'view'}">
                        <h2>產品詳情</h2>
                        <c:if test="${not empty product}">
                            <div class="data-form"> <%-- Use data-form style for view details --%>
                                <div class="form-group">
                                    <label>產品ID:</label>
                                    <p class="form-static-text"><c:out value="${product.productId}"/></p>
                                </div>
                                <div class="form-group">
                                    <label>產品代碼:</label>
                                    <p class="form-static-text"><c:out value="${product.productCode}"/></p>
                                </div>
                                <div class="form-group">
                                    <label>產品名稱:</label>
                                    <p class="form-static-text"><c:out value="${product.productName}"/></p>
                                </div>
                                <div class="form-group">
                                    <label>類別:</label>
                                    <p class="form-static-text"><c:out value="${product.category}"/></p>
                                </div>
                                <div class="form-group">
                                    <label>單位:</label>
                                    <p class="form-static-text"><c:out value="${product.unitOfMeasure}"/></p>
                                </div>
                                <div class="form-group">
                                    <label>銷售價格:</label>
                                    <p class="form-static-text"><fmt:formatNumber value="${product.sellingPrice}" pattern="#,##0.00"/></p>
                                </div>
                                <div class="form-group">
                                    <label>成本:</label>
                                    <p class="form-static-text"><fmt:formatNumber value="${product.cost}" pattern="#,##0.00"/></p>
                                </div>
                                <div class="form-group">
                                    <label>啟用:</label>
                                    <p class="form-static-text"><c:out value="${product.isActive ? '是' : '否'}"/></p>
                                </div>
                                <div class="form-group">
                                    <label>圖片URL:</label>
                                    <p class="form-static-text"><c:out value="${product.imageUrl}"/></p>
                                </div>
                                <div class="form-group">
                                    <label>描述:</label>
                                    <p class="form-static-text"><c:out value="${product.description}"/></p>
                                </div>
                                <div class="form-group">
                                    <label>創建日期:</label>
                                    <p class="form-static-text"><fmt:formatDate value="${product.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                                </div>
                                <div class="form-group">
                                    <label>更新日期:</label>
                                    <p class="form-static-text"><fmt:formatDate value="${product.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
                                </div>
                            </div>
                            <div class="form-links-group">
                                <a href="${pageContext.request.contextPath}/products/edit/${product.productId}" class="btn btn-warning"><i class="fas fa-edit"></i> 編輯產品</a>
                                <a href="${pageContext.request.contextPath}/products/list" class="btn btn-secondary"><i class="fas fa-list"></i> 返回列表</a>
                            </div>
                        </c:if>
                        <c:if test="${empty product}">
                            <p>產品不存在或無法載入。</p>
                            <p><a href="${pageContext.request.contextPath}/products/list">返回列表</a></p>
                        </c:if>
                    </c:if>

                    <%-- Edit Product Form Section --%>
                    <c:if test="${action == 'edit'}">
                        <h2>編輯產品</h2>
                        <c:if test="${not empty product}">
                            <form action="${pageContext.request.contextPath}/products/update" method="post" class="data-form">
                                <input type="hidden" name="productId" value="${product.productId}" />

                                <div class="form-group">
                                    <label>產品ID:</label>
                                    <p class="form-static-text"><c:out value="${product.productId}"/></p>
                                </div>

                                <div class="form-group">
                                    <label for="productCode">產品代碼:</label>
                                    <input type="text" id="productCode" name="productCode" value="<c:out value="${product.productCode}"/>" readonly required />
                                    <small class="help-text"><i class="fas fa-info-circle"></i> 產品代碼在編輯模式下不可修改。</small>
                                </div>

                                <div class="form-group">
                                    <label for="productName">產品名稱:</label>
                                    <input type="text" id="productName" name="productName" required value="<c:out value="${product.productName}"/>" />
                                </div>
                                <div class="form-group">
                                    <label for="category">類別:</label>
                                    <input type="text" id="category" name="category" value="<c:out value="${product.category}"/>" />
                                </div>
                                <div class="form-group">
                                    <label for="unit">單位:</label>
                                    <input type="text" id="unit" name="unit" required value="<c:out value="${product.unitOfMeasure}"/>" />
                                </div>
                                <div class="form-group">
                                    <label for="sellingPrice">銷售價格:</label>
                                    <input type="number" step="0.01" id="sellingPrice" name="sellingPrice" required min="0" value="<c:out value="${product.sellingPrice}"/>" />
                                </div>
                                <div class="form-group">
                                    <label for="cost">成本:</label>
                                    <input type="number" step="0.01" id="cost" name="cost" min="0" value="<c:out value="${product.cost}"/>" />
                                </div>
                                <div class="form-group checkbox-group">
                                    <input type="checkbox" id="isActive" name="isActive" ${product.isActive ? 'checked' : ''} />
                                    <label for="isActive">啟用</label>
                                </div>
                                <div class="form-group">
                                    <label for="imageUrl">圖片URL:</label>
                                    <input type="url" id="imageUrl" name="imageUrl" value="<c:out value="${product.imageUrl}"/>" />
                                </div>
                                <div class="form-group">
                                    <label for="description">描述:</label>
                                    <textarea id="description" name="description" rows="5"><c:out value="${product.description}"/></textarea>
                                </div>
                                <input type="submit" value="更新產品" />
                            </form>
                            <div class="form-links-group">
                                <a href="${pageContext.request.contextPath}/products/view/${product.productId}"><i class="fas fa-eye"></i> 查看產品詳情</a>
                                <a href="${pageContext.request.contextPath}/products/list" class="btn btn-secondary"><i class="fas fa-list"></i> 返回列表</a>
                            </div>
                        </c:if>
                        <c:if test="${empty product}">
                            <p>無法載入要編輯的產品，可能不存在。</p>
                            <p><a href="${pageContext.request.contextPath}/products/list">返回列表</a></p>
                        </c:if>
                    </c:if>

                </section>
            </div>

            <footer class="main-footer">
                <%-- Include the footer.jsp here --%>
                <jsp:include page="/common/footer.jsp" />
            </footer>
        </div>
    </div>

    <%-- Optional: Include any necessary JavaScript files --%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // JavaScript function to handle POST deletion
        function confirmAndDelete(productName, productId, deleteUrl) {
            if (confirm('確定要刪除產品 ' + productName + ' (ID: ' + productId + ') 嗎？這將無法恢復！')) {
                // Create a temporary form
                var form = document.createElement('form');
                form.setAttribute('method', 'POST');
                form.setAttribute('action', deleteUrl);

                // Add CSRF token if Spring Security is enabled
                // You'll need to retrieve the actual CSRF token from your page,
                // e.g., from a hidden input generated by Spring Security.
                // For example: var csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
                // var csrfParam = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
                // if (csrfToken && csrfParam) {
                //     var csrfInput = document.createElement('input');
                //     csrfInput.setAttribute('type', 'hidden');
                //     csrfInput.setAttribute('name', csrfParam);
                //     csrfInput.setAttribute('value', csrfToken);
                //     form.appendChild(csrfInput);
                // }

                // Append the form to the body and submit it
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
    <%-- If you have a sidebar.js for interactivity, include it.
        Make sure the path is correct relative to the current JSP or the web context root. --%>
    <%-- <script src="${pageContext.request.contextPath}/js/sidebar.js"></script> --%>
</body>

</html>
