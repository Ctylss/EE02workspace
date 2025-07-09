<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
        <% request.setAttribute("pageTitle", "報修成功" ); %>
            <%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
                <%@ taglib prefix="c" uri="jakarta.tags.core" %>

                    <!DOCTYPE html>
                    <html lang="zh-Hant">

                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>${pageTitle}</title>
                        <%-- 引入 Font Awesome 用於圖標 --%>
                            <link rel="stylesheet"
                                href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
                            <%-- 可選：引入 Google Fonts 提升字體美觀 --%>
                                <link
                                    href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@300;400;500;700&display=swap"
                                    rel="stylesheet">
                                <script src="${pageContext.request.contextPath}/sidebar.js"></script>

                                <style>
                                    /* 基本樣式和字體設定 */
                                    body {
                                        font-family: 'Noto Sans TC', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                        background: linear-gradient(to right bottom, #e0f2f7, #c6e0eb);
                                        /* 柔和的淺藍漸變背景 */
                                        color: #333;
                                        margin: 0;
                                        padding: 0;
                                        line-height: 1.6;
                                        min-height: 100vh;
                                        display: flex;
                                        flex-direction: column;
                                    }

                                    /* 容器樣式 */
                                    .success-container {
                                        flex-grow: 1;
                                        max-width: 700px;
                                        /* 適中寬度 */
                                        margin: 3rem auto;
                                        padding: 2.5rem 3rem;
                                        background-color: #ffffff;
                                        border-radius: 15px;
                                        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
                                        text-align: center;
                                        display: flex;
                                        flex-direction: column;
                                        align-items: center;
                                        justify-content: center;
                                    }

                                    .success-container h2 {
                                        font-size: 2.8rem;
                                        color: #28a745;
                                        /* 成功綠 */
                                        margin-bottom: 1.5rem;
                                        display: flex;
                                        align-items: center;
                                        justify-content: center;
                                        gap: 20px;
                                        position: relative;
                                        padding-bottom: 1rem;
                                    }

                                    .success-container h2::after {
                                        content: '';
                                        position: absolute;
                                        bottom: 0;
                                        left: 50%;
                                        transform: translateX(-50%);
                                        width: 80px;
                                        height: 4px;
                                        background-color: #28a745;
                                        border-radius: 2px;
                                    }

                                    .success-message {
                                        font-size: 1.3rem;
                                        color: #555;
                                        margin-bottom: 2.5rem;
                                        line-height: 1.8;
                                    }

                                    /* 按鈕組樣式 */
                                    .btn-group {
                                        display: flex;
                                        flex-wrap: wrap;
                                        /* 允許換行 */
                                        justify-content: center;
                                        gap: 20px;
                                        /* 按鈕間距 */
                                        margin-top: 2rem;
                                    }

                                    .btn {
                                        display: inline-flex;
                                        align-items: center;
                                        gap: 10px;
                                        padding: 15px 30px;
                                        border-radius: 8px;
                                        font-size: 1.1rem;
                                        font-weight: 600;
                                        text-decoration: none;
                                        transition: all 0.3s ease, transform 0.2s ease;
                                        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                                        color: white;
                                        /* 預設文字顏色 */
                                    }

                                    .btn:hover {
                                        transform: translateY(-3px);
                                        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
                                    }

                                    .btn.primary {
                                        background-color: #007bff;
                                    }

                                    .btn.primary:hover {
                                        background-color: #0056b3;
                                    }

                                    .btn.secondary {
                                        background-color: #6c757d;
                                    }

                                    .btn.secondary:hover {
                                        background-color: #5a6268;
                                    }

                                    .btn.success {
                                        background-color: #28a745;
                                    }

                                    .btn.success:hover {
                                        background-color: #218838;
                                    }

                                    /* 圖標顏色 */
                                    .btn i {
                                        color: white;
                                        /* 確保圖標也是白色 */
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
                                            gap: 15px;
                                        }

                                        .success-message {
                                            font-size: 1.1rem;
                                            margin-bottom: 2rem;
                                        }

                                        .btn-group {
                                            flex-direction: column;
                                            /* 小螢幕下垂直堆疊 */
                                            gap: 15px;
                                            width: 80%;
                                            /* 按鈕組佔寬度 */
                                            max-width: 300px;
                                            /* 限制最大寬度 */
                                            margin: 2rem auto 0;
                                        }

                                        .btn {
                                            padding: 12px 20px;
                                            font-size: 1rem;
                                            width: 100%;
                                            /* 確保按鈕佔滿容器寬度 */
                                            box-sizing: border-box;
                                        }
                                    }

                                    @media (max-width: 480px) {
                                        .success-container {
                                            padding: 1.5rem;
                                        }

                                        .success-container h2 {
                                            font-size: 1.8rem;
                                        }

                                        .success-message {
                                            font-size: 1rem;
                                        }

                                        .btn-group {
                                            gap: 10px;
                                        }
                                    }
                                </style>
                    </head>

                    <body>
                        <%@ include file="/common/header.jsp" %>

                            <div class="success-container">
                                <h2><i class="fas fa-check-circle"></i> 報修申請成功！</h2>
                                <p class="success-message">感謝您填寫報修單。您的申請已成功送出，我們會盡快處理您的機台問題。</p>

                                <div class="btn-group">
                                    <a href="${pageContext.request.contextPath}/repairForm" class="btn primary">
                                        <i class="fas fa-plus-circle"></i> 再填一筆
                                    </a>
                                    <a href="${pageContext.request.contextPath}/MachineRepairViewServlet"
                                        class="btn secondary">
                                        <i class="fas fa-clipboard-list"></i> 查看報修紀錄
                                    </a>
                                    <a href="${pageContext.request.contextPath}/HTML/repair/index.html"
                                        class="btn success">
                                        <i class="fas fa-home"></i> 返回首頁
                                    </a>
                                </div>
                            </div>
                    </body>

                    </html>