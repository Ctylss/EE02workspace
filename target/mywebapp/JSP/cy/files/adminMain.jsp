<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "後台管理系統"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

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
<%-- 由於這是後台首頁，通常會直接包含必要樣式，或通過一個主佈局JSP來管理 --%>
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css" /> --%>
<%-- <script src="${pageContext.request.contextPath}/sidebar.js"></script> --%>

<style>
    /* 基本樣式和字體設定 */
    body {
        font-family: 'Noto Sans TC', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background: linear-gradient(to right bottom, #e0f2f7, #c1e7f0); /* 柔和的漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
    }

    /* 主容器 */
    .dashboard-container {
        flex-grow: 1;
        max-width: 1200px;
        margin: 2.5rem auto;
        padding: 2.5rem;
        background-color: #ffffff;
        border-radius: 15px;
        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1); /* 更明顯的陰影 */
        display: flex;
        flex-direction: column;
        gap: 2rem; /* 區塊之間的間距 */
    }

    /* 頂部標題區塊 */
    .dashboard-header {
        text-align: center;
        padding-bottom: 1.5rem;
        border-bottom: 1px solid #e0e0e0;
    }

    .dashboard-header h1 {
        font-size: 2.8rem; /* 放大標題字體 */
        color: #2c3e50;
        margin-bottom: 0.8rem;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 15px; /* 圖標和文字間距 */
    }

    .dashboard-header p {
        font-size: 1.2rem;
        color: #555;
        margin-top: 0;
    }

    /* 功能卡片區域 */
    .feature-cards {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); /* 響應式網格佈局 */
        gap: 1.5rem; /* 卡片間距 */
        margin-bottom: 2rem;
    }

    .feature-card {
        background-color: #f8fafd;
        padding: 1.8rem;
        border-radius: 12px;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
        transition: transform 0.3s ease, box-shadow 0.3s ease;
        display: flex;
        flex-direction: column;
        justify-content: space-between; /* 內容上下分佈 */
        min-height: 180px; /* 最小高度 */
    }

    .feature-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
    }

    .feature-card h3 {
        font-size: 1.8rem;
        color: #007bff; /* 主題藍色 */
        margin-bottom: 0.8rem;
        display: flex;
        align-items: center;
        gap: 10px; /* 圖標和文字間距 */
    }

    .feature-card p {
        color: #666;
        font-size: 1.05rem;
        margin-bottom: 1.2rem;
        flex-grow: 1; /* 讓段落佔據更多空間 */
    }

    .feature-card a {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        padding: 0.8rem 1.5rem;
        background-color: #007bff;
        color: white;
        text-decoration: none;
        border-radius: 8px;
        font-weight: 500;
        transition: background-color 0.3s ease, transform 0.2s ease;
        font-size: 1rem;
        gap: 8px;
    }

    .feature-card a:hover {
        background-color: #0056b3;
        transform: translateY(-2px);
    }

    /* 系統資訊區塊 */
    .system-info {
        background-color: #f0f8ff; /* 淺藍背景 */
        padding: 2rem;
        border-radius: 12px;
        box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.05); /* 內陰影效果 */
        margin-bottom: 2rem;
    }

    .system-info h3 {
        font-size: 1.8rem;
        color: #2c3e50;
        margin-bottom: 1.2rem;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .system-info ul {
        list-style: none; /* 移除默認列表點 */
        padding: 0;
        margin: 0;
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); /* 響應式列表 */
        gap: 0.8rem;
    }

    .system-info li {
        color: #444;
        font-size: 1.1rem;
        display: flex;
        align-items: center;
        gap: 10px;
        padding: 0.5rem 0;
    }

    .system-info li i {
        color: #007bff; /* 圖標顏色 */
    }

    /* 回首頁按鈕 */
    .back-home {
        text-align: center;
        margin-top: 2rem;
    }

    .back-home a {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        padding: 1rem 2rem;
        background-color: #6c757d; /* 灰色按鈕 */
        color: white;
        text-decoration: none;
        border-radius: 8px;
        font-weight: 500;
        transition: background-color 0.3s ease, transform 0.2s ease;
        font-size: 1.1rem;
        gap: 10px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    }

    .back-home a:hover {
        background-color: #5a6268;
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(0, 0, 0, 0.15);
    }

    /* 響應式調整 */
    @media (max-width: 992px) {
        .dashboard-container {
            margin: 2rem auto;
            padding: 2rem;
        }

        .dashboard-header h1 {
            font-size: 2.2rem;
            flex-wrap: wrap; /* 允許標題換行 */
            justify-content: center;
        }

        .dashboard-header p {
            font-size: 1rem;
        }

        .feature-card h3, .system-info h3 {
            font-size: 1.5rem;
        }

        .feature-card p, .system-info li {
            font-size: 1rem;
        }
    }

    @media (max-width: 768px) {
        .dashboard-container {
            margin: 1rem auto;
            padding: 1.5rem;
            border-radius: 10px;
        }

        .dashboard-header h1 {
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .feature-cards {
            grid-template-columns: 1fr; /* 單列佈局 */
            gap: 1rem;
        }

        .feature-card {
            padding: 1.5rem;
            min-height: unset; /* 取消最小高度限制 */
        }

        .feature-card h3 {
            font-size: 1.4rem;
        }

        .feature-card p {
            margin-bottom: 1rem;
        }

        .system-info ul {
            grid-template-columns: 1fr; /* 單列列表 */
        }

        .back-home a {
            padding: 0.8rem 1.5rem;
            font-size: 1rem;
        }
    }
</style>
</head>
<body>
    <%-- 這裡可以包含 header.jsp 和 sidebar.jsp，但為了展示核心頁面，暫不包含 --%>
    <%-- 如果您有共通的 header 或 sidebar，請在此處加入 --%>
    <%-- <%@ include file="../header.jsp" %> --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="dashboard-container">

        <div class="dashboard-header">
            <h1>⚙️ 後台管理系統</h1>
            <p>機台檔案及資料管理中心</p>
        </div>

        <hr>

        <div class="feature-cards">

            <div class="feature-card">
                <h3><i class="fas fa-folder-open"></i> 檔案管理</h3>
                <p>管理機台相關檔案，包含檔案的上傳、編輯、刪除和快速搜尋功能。</p>
                <a href="${pageContext.request.contextPath}/FindFilesServlet">
                    <i class="fas fa-arrow-right"></i> 進入管理
                </a>
            </div>

            <div class="feature-card">
                <h3><i class="fas fa-clipboard-list"></i> 機台相關文件列表</h3>
                <p>集中查看所有機台的關聯文件，支援便捷的搜尋與篩選功能，快速找到所需文件。</p>
                <a href="${pageContext.request.contextPath}/FindFilesServlet">
                    <i class="fas fa-eye"></i> 查看列表
                </a>
            </div>

            <div class="feature-card">
                <h3><i class="fas fa-plus-circle"></i> 新增機台相關文件</h3>
                <p>為特定機台新增文件記錄，建立文件與機台的關聯，確保資料的完整性。</p>
                <a href="${pageContext.request.contextPath}/InsertFilesServlet">
                    <i class="fas fa-upload"></i> 新增文件
                </a>
            </div>

            <div class="feature-card">
                <h3><i class="fas fa-cogs"></i> 機台數據管理</h3>
                <p>管理機台的基本資訊，包括機台的新增、編輯、狀態更新與位置變更。</p>
                <a href="${pageContext.request.contextPath}/machines?action=list">
                    <i class="fas fa-database"></i> 進入管理
                </a>
            </div>

        </div>

        <div class="system-info">
            <h3><i class="fas fa-info-circle"></i> 系統快速導覽</h3>
            <ul>
                <li><i class="fas fa-folder-open"></i> **檔案管理**: 處理機台相關文件的上傳、檢視與維護。</li>
                <li><i class="fas fa-cogs"></i> **機台數據管理**: 進行機台基本資料的增、刪、改、查。</li>
                <li><i class="fas fa-chart-line"></i> **數據報告**: 預留功能，未來可生成機台運行報告。</li>
                <li><i class="fas fa-users-cog"></i> **權限管理**: 預留功能，未來可分配不同用戶的操作權限。</li>
            </ul>
        </div>

        <div class="back-home">
            <a href="${pageContext.request.contextPath}/">
                <i class="fas fa-home"></i> 回到首頁
            </a>
        </div>

    </div>

</body>
</html>