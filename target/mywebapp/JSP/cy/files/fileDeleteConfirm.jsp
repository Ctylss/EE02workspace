<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "刪除檔案確認"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle}</title>
    <%-- 引入 Font Awesome 用於圖標 --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <%-- 可選：引入 Google Fonts 提升字體美觀 --%>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@300;400;500;700&display=swap" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/sidebar.js"></script>

    <style>
        /* 基本樣式和字體設定 */
        body {
            font-family: 'Noto Sans TC', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(to right bottom, #fde7e7, #f7d2d2); /* 警告色系的柔和漸變背景 */
            color: #333;
            margin: 0;
            padding: 0;
            line-height: 1.6;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        /* 內容容器 */
        .delete-confirm-container {
            flex-grow: 1;
            max-width: 600px;
            margin: 3rem auto; /* 增加上下外邊距 */
            padding: 2.5rem 3rem; /* 增加內邊距 */
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15); /* 更明顯的陰影 */
            text-align: center; /* 內容居中 */
        }

        .delete-confirm-container h2 {
            font-size: 2.2rem;
            color: #dc3545; /* 強烈的紅色警告 */
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 15px;
        }

        .delete-confirm-container > p {
            font-size: 1.1rem;
            color: #555;
            margin-bottom: 2rem;
            line-height: 1.8;
        }

        /* 返回連結 */
        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            color: #007bff;
            text-decoration: none;
            font-weight: 500;
            margin-bottom: 2rem;
            transition: color 0.3s ease;
        }

        .back-link:hover {
            color: #0056b3;
            text-decoration: underline;
        }

        /* 檔案資訊區塊 */
        .file-info-box {
            background-color: #fff0f0; /* 淺紅色背景 */
            border: 1px solid #ffcccc; /* 淺紅色邊框 */
            padding: 1.8rem;
            border-radius: 10px;
            text-align: left; /* 列表內容左對齊 */
            margin-bottom: 2.5rem;
            box-shadow: 0 2px 10px rgba(255, 0, 0, 0.05);
        }

        .file-info-box h3 {
            font-size: 1.6rem;
            color: #e60000; /* 深紅色 */
            margin-bottom: 1.2rem;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .file-info-box ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .file-info-box li {
            margin-bottom: 0.8rem;
            font-size: 1.05rem;
            color: #444;
            display: flex;
            gap: 8px;
            align-items: flex-start; /* 確保內容對齊 */
        }

        .file-info-box li strong {
            color: #222;
            flex-shrink: 0; /* 防止標籤收縮 */
            width: 100px; /* 固定標籤寬度 */
            text-align: right;
        }
        
        .file-info-box li a {
            word-break: break-all; /* 長路徑換行 */
            color: #007bff;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        .file-info-box li a:hover {
            color: #0056b3;
            text-decoration: underline;
        }

        /* 最終警告提醒 */
        .final-warning {
            font-size: 1.2rem;
            color: #dc3545; /* 警告紅 */
            font-weight: 700; /* 更粗的字體 */
            margin-bottom: 2.5rem;
            padding: 1rem;
            background-color: #ffe0e0;
            border-radius: 8px;
            border: 2px dashed #ff9999; /* 虛線邊框增加警示感 */
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
        }

        /* 按鈕群組 */
        .action-buttons {
            display: flex;
            justify-content: center; /* 按鈕居中 */
            gap: 1.5rem;
        }

        .action-buttons button,
        .action-buttons .btn-cancel {
            padding: 12px 28px;
            border-radius: 8px;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            white-space: nowrap;
        }

        .action-buttons button[type="submit"] {
            background-color: #dc3545; /* 刪除的紅色 */
            color: white;
            border: 1px solid #dc3545;
            box-shadow: 0 4px 10px rgba(220, 53, 69, 0.2);
        }

        .action-buttons button[type="submit"]:hover {
            background-color: #c82333;
            border-color: #bd2130;
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(220, 53, 69, 0.3);
        }

        .action-buttons .btn-cancel {
            background-color: #6c757d;
            color: white;
            border: 1px solid #6c757d;
            box-shadow: 0 4px 10px rgba(108, 117, 125, 0.15);
        }

        .action-buttons .btn-cancel:hover {
            background-color: #5a6268;
            border-color: #545b62;
            transform: translateY(-2px);
            box-shadow: 0 6px 15px rgba(108, 117, 125, 0.25);
        }

        /* 響應式調整 */
        @media (max-width: 768px) {
            .delete-confirm-container {
                margin: 1.5rem auto;
                padding: 1.5rem;
                border-radius: 10px;
            }

            .delete-confirm-container h2 {
                font-size: 1.8rem;
                gap: 10px;
                margin-bottom: 1rem;
            }

            .delete-confirm-container > p {
                font-size: 1rem;
                margin-bottom: 1.5rem;
            }

            .file-info-box {
                padding: 1.2rem;
            }

            .file-info-box h3 {
                font-size: 1.4rem;
                gap: 8px;
            }

            .file-info-box li {
                font-size: 0.95rem;
                flex-direction: column; /* 小螢幕下標籤和值垂直堆疊 */
                align-items: flex-start;
                gap: 3px; /* 調整垂直間距 */
            }

            .file-info-box li strong {
                width: auto; /* 取消固定寬度 */
                text-align: left;
            }

            .final-warning {
                font-size: 1.05rem;
                padding: 0.8rem;
                margin-bottom: 2rem;
            }

            .action-buttons {
                flex-direction: column; /* 按鈕垂直堆疊 */
                gap: 1rem;
            }
            .action-buttons button,
            .action-buttons .btn-cancel {
                width: 100%;
                padding: 10px 20px;
                font-size: 1rem;
            }
        }
    </style>
</head>
<body>
    <%-- 這裡假設你的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>
    
    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="delete-confirm-container">
        <h2>⚠️ 確認刪除檔案</h2>
        <p>您即將刪除以下檔案記錄，此操作將會<strong>永久移除</strong>資料庫中的檔案資訊，請務必謹慎確認！</p>

        <a href="${pageContext.request.contextPath}/FileManagementServlet" class="back-link">
            <i class="fas fa-arrow-left"></i> 返回檔案管理頁面
        </a>

        <%-- 檔案詳細資訊 --%>
        <div class="file-info-box">
            <h3><i class="fas fa-info-circle"></i> 檔案詳細資訊</h3>
            <c:choose>
                <c:when test="${not empty file}">
                    <ul>
                        <li><strong>檔案 ID：</strong> <c:out value="${file.fileId}"/></li>
                        <li><strong>機台 ID：</strong> <c:out value="${file.machineId}"/></li>
                        <li><strong>檔案名稱：</strong> <c:out value="${file.fileName}"/></li>
                        <li><strong>檔案路徑：</strong> 
                            <a href="${pageContext.request.contextPath}/${file.filePath}" target="_blank" rel="noopener noreferrer">
                                <c:out value="${file.filePath}"/> <i class="fas fa-external-link-alt"></i>
                            </a>
                        </li>
                        <li><strong>上傳時間：</strong> 
                            <c:out value="${file.formattedUploadTime}"/>
                            <%-- 假設formattedUploadTime已經是格式化好的字符串 --%>
                            <%-- 如果 file.uploadTime 是 java.util.Date 或 java.sql.Timestamp，可以使用 fmt 標籤格式化：--%>
                            <%-- <fmt:formatDate value="${file.uploadTime}" pattern="yyyy-MM-dd HH:mm:ss"/> --%>
                        </li>
                    </ul>
                </c:when>
                <c:otherwise>
                    <p>找不到該檔案的詳細資訊。請確認檔案 ID 是否正確。</p>
                </c:otherwise>
            </c:choose>
        </div>

        <%-- 最終警告區塊 --%>
        <p class="final-warning">
            <i class="fas fa-exclamation-triangle"></i> <strong>此操作將永久刪除資料，且無法復原！</strong>
        </p>

        <%-- 表單操作 --%>
        <form method="post" action="${pageContext.request.contextPath}/DeleteFilesServlet" id="deleteForm">
            <input type="hidden" name="fileId" value="${file.fileId}">
            <input type="hidden" name="confirmDelete" value="true">

            <div class="action-buttons">
                <button type="submit" id="deleteButton">
                    <i class="fas fa-trash-alt"></i> 確認刪除
                </button>
                <a href="${pageContext.request.contextPath}/FileManagementServlet" class="btn-cancel">
                    <i class="fas fa-times-circle"></i> 取消
                </a>
            </div>
        </form>
    </div>

    <script>
        document.getElementById('deleteForm').addEventListener('submit', function(e) {
            if (!confirm('⚠️ 您確定要永久刪除這筆檔案資料嗎？此操作無法復原，請再次確認！')) {
                e.preventDefault(); // 如果用戶取消，則阻止表單提交
            }
        });
    </script>
</body>
</html>