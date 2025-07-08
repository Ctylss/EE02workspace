<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "確認刪除保養資料"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="zh-Hant">
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
        background: linear-gradient(to right bottom, #f0f4f8, #d9e2ec); /* 柔和的淺藍灰漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
    }

    /* 主要內容容器 */
    .confirmation-container {
        flex-grow: 1;
        max-width: 600px; /* 較窄的容器，符合確認頁面的需求 */
        margin: 4rem auto; /* 增加上下外邊距 */
        padding: 3rem; /* 增加內邊距 */
        background-color: #ffffff;
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        text-align: center; /* 內容居中 */
    }

    .confirmation-container h2 {
        font-size: 2.2rem;
        color: #dc3545; /* 刪除操作，使用紅色強調 */
        margin-bottom: 1.5rem;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 12px;
        padding-bottom: 1rem;
        border-bottom: 1px solid #f0f0f0;
    }

    /* 警告訊息 */
    .warning-message {
        background-color: #ffe0b2; /* 淺橙色背景 */
        color: #e65100; /* 深橙色文字 */
        border: 1px solid #ffab40;
        padding: 1.5rem;
        margin-bottom: 2.5rem;
        border-radius: 8px;
        font-weight: 500;
        display: flex;
        align-items: center;
        gap: 12px;
        font-size: 1.1rem;
        line-height: 1.5;
    }

    /* 顯示要刪除的資訊 */
    .detail-info {
        text-align: left; /* 資訊靠左對齊 */
        margin-bottom: 2.5rem;
        font-size: 1.05rem;
        color: #555;
    }
    
    .detail-info p {
        margin-bottom: 0.8rem;
        display: flex;
        align-items: flex-start;
        gap: 10px;
    }

    .detail-info p strong {
        color: #333;
        min-width: 100px; /* 標籤對齊 */
        flex-shrink: 0;
    }

    /* 表單和按鈕 */
    form {
        display: flex;
        justify-content: center; /* 按鈕居中 */
        gap: 1.5rem;
        margin-top: 2rem;
    }

    form input[type="submit"],
    form .btn-cancel {
        padding: 12px 28px;
        border-radius: 8px;
        font-size: 1.05rem;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        white-space: nowrap;
        border: none; /* 移除預設邊框 */
    }

    form input[type="submit"] {
        background-color: #dc3545; /* 刪除按鈕使用紅色 */
        color: white;
        box-shadow: 0 4px 10px rgba(220, 53, 69, 0.2);
    }

    form input[type="submit"]:hover {
        background-color: #c82333;
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(220, 53, 69, 0.3);
    }

    form .btn-cancel {
        background-color: #6c757d; /* 取消按鈕使用灰色 */
        color: white;
        box-shadow: 0 4px 10px rgba(108, 117, 125, 0.2);
    }

    form .btn-cancel:hover {
        background-color: #5a6268;
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(108, 117, 125, 0.3);
    }

    /* 響應式調整 */
    @media (max-width: 768px) {
        .confirmation-container {
            margin: 2rem auto;
            padding: 2rem;
            border-radius: 10px;
        }
        .confirmation-container h2 {
            font-size: 1.8rem;
            gap: 10px;
            margin-bottom: 1rem;
        }
        .warning-message {
            padding: 1rem;
            font-size: 1rem;
            flex-direction: column;
            align-items: flex-start;
            gap: 8px;
        }
        .detail-info {
            font-size: 0.95rem;
        }
        .detail-info p {
            flex-direction: column;
            align-items: flex-start;
            gap: 5px;
        }
        .detail-info p strong {
            min-width: unset;
        }
        form {
            flex-direction: column;
            gap: 1rem;
        }
        form input[type="submit"],
        form .btn-cancel {
            width: 100%;
            padding: 10px 20px;
        }
    }
</style>
</head>
<body>
    <%-- 這裡假設您的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>
   
    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="confirmation-container">
        <h2><i class="fas fa-exclamation-triangle"></i> 確認刪除保養資料</h2>
        
        <div class="warning-message">
            <i class="fas fa-info-circle fa-2x"></i>
            <div>
                此操作將永久刪除以下保養資料，且無法復原。請在確認無誤後點擊「確認刪除」按鈕。
            </div>
        </div>

        <div class="detail-info">
            <p><strong><i class="fas fa-hashtag"></i> 保養編號：</strong><c:out value="${maintenance.scheduleId}" /></p>
            <p><strong><i class="fas fa-microchip"></i> 機台編號：</strong><c:out value="${maintenance.machineId}" /></p>
            <p><strong><i class="fas fa-file-alt"></i> 保養描述：</strong><c:out value="${maintenance.maintenanceDescription}" /></p>
            <%-- 假設還有更多需要確認的資料，例如：--%>
            <p><strong><i class="fas fa-calendar-alt"></i> 預計保養日期：</strong><c:out value="${maintenance.scheduleDate}" /></p>
            <p><strong><i class="fas fa-user-tie"></i> 保養人員編號：</strong><c:out value="${maintenance.employeeId}" /></p>
            <p><strong><i class="fas fa-check-circle"></i> 保養狀態：</strong><c:out value="${maintenance.maintenanceStatus}" /></p>
        </div>

        <form action="${pageContext.request.contextPath}/DeleteMaintenanceServlet" method="post">
            <input type="hidden" name="scheduleId" value="${maintenance.scheduleId}" />
            <input type="submit" value="確認刪除" onclick="return confirm('您即將永久刪除此筆保養資料，此操作無法復原。確定要繼續嗎？');" />
            <a href="${pageContext.request.contextPath}/AdminMaintenanceServlet" class="btn-cancel">
                <i class="fas fa-times-circle"></i> 取消
            </a>
        </form>
    </div>
</body>
</html>