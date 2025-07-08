<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% request.setAttribute("pageTitle", "保養詳細資訊"); %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%-- 由於不再使用 fmt:formatDate，因此移除 fmt 標籤庫引用 --%>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${pageTitle}</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@300;400;500;700&display=swap" rel="stylesheet">
<script src="${pageContext.request.contextPath}/sidebar.js"></script>

<style>
    /* 基本樣式和字體設定 */
    body {
        font-family: 'Noto Sans TC', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        background: linear-gradient(to right bottom, #e0f7fa, #b2ebf2); /* 柔和的藍綠漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
    }

    /* 主要內容容器 */
    .detail-container {
        flex-grow: 1;
        max-width: 800px; /* 調整容器寬度以適應詳細資訊 */
        margin: 3rem auto; /* 增加上下外邊距 */
        padding: 2.5rem 3rem; /* 增加內邊距 */
        background-color: #ffffff;
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    }

    .detail-container h2 {
        font-size: 2.5rem;
        color: #2c3e50;
        margin-bottom: 1.5rem;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 15px;
        padding-bottom: 1rem;
        border-bottom: 1px solid #eee;
    }

    /* 詳細資訊列表 */
    .detail-list {
        list-style: none;
        padding: 0;
        margin: 2rem 0;
    }

    .detail-list li {
        display: flex;
        align-items: flex-start; /* 確保內容多時也能良好對齊 */
        margin-bottom: 1.2rem;
        font-size: 1.05rem;
        color: #444;
        gap: 12px; /* 圖標和文字的間距 */
        line-height: 1.5;
        padding: 0.5rem 0;
        border-bottom: 1px dashed #f0f0f0; /* 虛線分隔 */
    }

    .detail-list li:last-child {
        border-bottom: none; /* 最後一項無分隔線 */
        margin-bottom: 0;
    }

    .detail-list li .label {
        font-weight: 600;
        color: #2c3e50;
        min-width: 120px; /* 統一標籤寬度 */
        flex-shrink: 0; /* 防止標籤縮小 */
    }
    
    .detail-list li .value {
        flex-grow: 1; /* 數值部分自動佔據剩餘空間 */
        word-break: break-all; /* 長內容自動換行 */
    }

    /* 保養狀態美化 */
    .status-badge {
        display: inline-block;
        padding: 6px 12px;
        border-radius: 20px;
        font-weight: 600;
        font-size: 0.9rem;
        white-space: nowrap;
        text-align: center;
        min-width: 80px; /* 確保最小寬度 */
    }

    .status-badge.pending { /* 待處理 */
        background-color: #fff3e0;
        color: #ff9800;
        border: 1px solid #ffcc80;
    }

    .status-badge.in-progress { /* 進行中 */
        background-color: #e3f2fd;
        color: #2196F3;
        border: 1px solid #90caf9;
    }

    .status-badge.completed { /* 已完成 */
        background-color: #e8f5e9;
        color: #4CAF50;
        border: 1px solid #a5d6a7;
    }

    .status-badge.other { /* 其他狀態 */
        background-color: #f0f0f0;
        color: #666;
        border: 1px solid #ccc;
    }

    /* 返回按鈕 */
    .back-button-container {
        text-align: center;
        margin-top: 3rem;
        padding-top: 1.5rem;
        border-top: 1px solid #eee;
    }

    .back-button-container .btn {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        padding: 12px 25px;
        background-color: #6c757d; /* 灰色按鈕 */
        color: white;
        text-decoration: none;
        border-radius: 8px;
        font-weight: 500;
        transition: background-color 0.3s ease, transform 0.2s ease;
        box-shadow: 0 4px 10px rgba(108, 117, 125, 0.2);
    }

    .back-button-container .btn:hover {
        background-color: #5a6268;
        transform: translateY(-2px);
        box-shadow: 0 6px 15px rgba(108, 117, 125, 0.3);
    }

    /* 響應式調整 */
    @media (max-width: 768px) {
        .detail-container {
            margin: 2rem auto;
            padding: 2rem;
            border-radius: 10px;
        }
        .detail-container h2 {
            font-size: 2rem;
            gap: 10px;
            margin-bottom: 1rem;
        }
        .detail-list li {
            flex-direction: column; /* 小螢幕下垂直堆疊 */
            align-items: flex-start;
            gap: 5px;
            font-size: 1rem;
            margin-bottom: 1rem;
            padding: 0.2rem 0;
        }
        .detail-list li .label {
            min-width: unset; /* 取消最小寬度限制 */
        }
        .status-badge {
            font-size: 0.8rem;
            padding: 4px 10px;
        }
        .back-button-container {
            margin-top: 2rem;
            padding-top: 1rem;
        }
        .back-button-container .btn {
            width: 100%;
            justify-content: center;
            padding: 10px 20px;
        }
    }
</style>
</head>
<body>


    <div class="detail-container">
        <h2><i class="fas fa-clipboard-list"></i> 保養詳細資料</h2>

        <ul class="detail-list">
            <li>
                <span class="label">保養單編號：</span>
                <span class="value">#<c:out value="${maintenance.scheduleId}" /></span>
            </li>
            <li>
                <span class="label">機台編號：</span>
                <span class="value"><c:out value="${maintenance.machineId}" /></span>
            </li>
            <li>
                <span class="label">機台名稱：</span>
                <span class="value"><c:out value="${maintenance.machineName}" /></span>
            </li>
            <li>
                <span class="label">保養描述：</span>
                <span class="value"><c:out value="${maintenance.maintenanceDescription}" /></span>
            </li>
            <li>
                <span class="label">預計保養日期：</span>
                <span class="value">
                    ${not empty maintenance.scheduleDate ? maintenance.scheduleDate : 'N/A'}
                </span>
            </li>
            <li>
                <span class="label">保養狀態：</span>
                <span class="value">
                    <span class="status-badge
                        <c:choose><c:when test='${maintenance.maintenanceStatus == "待處理"}'>pending</c:when>
                            <c:when test='${maintenance.maintenanceStatus == "進行中"}'>in-progress</c:when>
                            <c:when test='${maintenance.maintenanceStatus == "已完成"}'>completed</c:when>
                            <c:otherwise>other</c:otherwise></c:choose>
                    ">
                        <c:out value="${maintenance.maintenanceStatus}" />
                    </span>
                </span>
            </li>
            <li>
                <span class="label">保養人員編號：</span>
                <span class="value"><c:out value="${maintenance.employeeId}" /></span>
            </li>
            <li>
                <span class="label">建立時間：</span>
                <span class="value">
                    ${not empty maintenance.createTime ? maintenance.createTime : 'N/A'}
                </span>
            </li>
            <li>
                <span class="label">更新時間：</span>
                <span class="value">
                    ${not empty maintenance.updateTime ? maintenance.updateTime : 'N/A'}
                </span>
            </li>
        </ul>

        <div class="back-button-container">
            <a href="${pageContext.request.contextPath}/AdminMaintenanceServlet" class="btn">
                <i class="fas fa-arrow-left"></i> 返回保養列表
            </a>
        </div>
    </div>
</body>
</html>