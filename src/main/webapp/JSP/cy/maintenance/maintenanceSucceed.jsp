<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "新增成功"); %>
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
        background: linear-gradient(to right bottom, #e8f5e9, #c8e6c9); /* 柔和的綠色漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
        justify-content: center; /* 內容垂直居中 */
        align-items: center; /* 內容水平居中 */
    }

    /* 成功訊息容器 */
    .success-container {
        max-width: 600px;
        margin: 3rem auto;
        padding: 3rem;
        background-color: #ffffff;
        border-radius: 15px;
        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
        text-align: center;
        transform: translateY(0);
        animation: fadeInScale 0.6s ease-out forwards; /* 加入進入動畫 */
    }

    @keyframes fadeInScale {
        from {
            opacity: 0;
            transform: translateY(20px) scale(0.95);
        }
        to {
            opacity: 1;
            transform: translateY(0) scale(1);
        }
    }

    .success-container h2 {
        font-size: 2.8rem;
        color: #28a745; /* 成功綠 */
        margin-bottom: 1.5rem;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 15px;
        animation: slideInLeft 0.8s ease-out forwards; /* 標題動畫 */
    }

    /* 成功圖標動畫 */
    .success-icon {
        color: #28a745; /* 成功綠 */
        font-size: 3.5rem;
        animation: bounceIn 1s ease-out forwards;
        transform-origin: center;
    }

    @keyframes bounceIn {
        0% { transform: scale(0.1); opacity: 0; }
        60% { transform: scale(1.2); opacity: 1; }
        100% { transform: scale(1); }
    }

    @keyframes slideInLeft {
        from {
            opacity: 0;
            transform: translateX(-50px);
        }
        to {
            opacity: 1;
            transform: translateX(0);
        }
    }

    .success-message {
        font-size: 1.3rem;
        color: #555;
        margin-bottom: 2.5rem;
        line-height: 1.8;
    }

    /* 返回按鈕 */
    .back-button {
        display: inline-flex;
        align-items: center;
        gap: 10px;
        padding: 15px 30px;
        background-color: #007bff; /* 主要藍 */
        color: white;
        text-decoration: none;
        border-radius: 8px;
        font-size: 1.1rem;
        font-weight: 500;
        transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
        box-shadow: 0 4px 12px rgba(0, 123, 255, 0.25);
    }

    .back-button:hover {
        background-color: #0056b3;
        transform: translateY(-3px);
        box-shadow: 0 6px 16px rgba(0, 123, 255, 0.35);
    }

    /* 響應式調整 */
    @media (max-width: 768px) {
        .success-container {
            margin: 2rem auto;
            padding: 2rem;
            border-radius: 10px;
        }
        .success-container h2 {
            font-size: 2.2rem;
            gap: 10px;
        }
        .success-icon {
            font-size: 3rem;
        }
        .success-message {
            font-size: 1.1rem;
            margin-bottom: 2rem;
        }
        .back-button {
            padding: 12px 25px;
            font-size: 1rem;
            width: 100%; /* 小螢幕下佔滿寬度 */
            box-sizing: border-box; /* 確保內邊距和邊框包含在寬度內 */
        }
    }

    @media (max-width: 480px) {
        .success-container {
            padding: 1.5rem;
        }
        .success-container h2 {
            font-size: 1.8rem;
            flex-direction: column;
            gap: 8px;
        }
        .success-icon {
            font-size: 2.5rem;
        }
        .success-message {
            font-size: 1rem;
        }
    }
</style>
</head>
<body>
    <%-- 這裡假設您的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>
  
    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="success-container">
        <i class="fas fa-check-circle success-icon"></i>
        <h2>保養排程新增成功！</h2>
        <p class="success-message">
            您的新保養排程已成功建立。您可以返回後台列表查看並管理所有排程。
        </p>
        <a href="${pageContext.request.contextPath}/AdminMaintenanceServlet" class="back-button">
            <i class="fas fa-arrow-left"></i> 返回後台保養列表
        </a>
    </div>
</body>
</html>