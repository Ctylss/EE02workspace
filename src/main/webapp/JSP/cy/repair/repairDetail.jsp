<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "報修詳細資訊"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>

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
        background: linear-gradient(to right bottom, #e0f7fa, #b2ebf2); /* 柔和的藍綠漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
    }

    /* 容器樣式 */
    .detail-container {
        flex-grow: 1;
        max-width: 700px; /* 適中寬度 */
        margin: 3rem auto;
        padding: 2.5rem 3rem;
        background-color: #ffffff;
        border-radius: 15px;
        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
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

    /* 詳細資訊行樣式 */
    .detail-item {
        font-size: 1.15rem;
        color: #495057;
        margin-bottom: 1rem;
        padding: 0.8rem 0;
        border-bottom: 1px dashed #f0f0f0; /* 虛線分隔 */
        display: flex;
        align-items: center;
        gap: 12px; /* 圖標與文字間距 */
    }

    .detail-item:last-child {
        border-bottom: none; /* 最後一項無邊框 */
        margin-bottom: 0;
    }

    .detail-item strong {
        color: #34495e;
        font-weight: 600;
        min-width: 120px; /* 統一標籤寬度 */
        display: inline-block;
    }

    .detail-item i {
        color: #007bff; /* 圖標顏色 */
        font-size: 1.3rem;
        width: 25px; /* 固定圖標寬度 */
        text-align: center;
    }

    /* 狀態顯示優化 */
    .status-badge {
        display: inline-block;
        padding: 8px 15px;
        border-radius: 25px;
        font-weight: 600;
        font-size: 1.05rem;
        white-space: nowrap;
        text-align: center;
        min-width: 100px;
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
    }

    .status-badge.pending { /* 待處理 */
        background-color: #fff3e0;
        color: #ff9800;
        border: 1px solid #ffcc80;
    }

    .status-badge.processing { /* 處理中 */
        background-color: #e3f2fd;
        color: #2196F3;
        border: 1px solid #90caf9;
    }

    .status-badge.completed { /* 已完成 */
        background-color: #e8f5e9;
        color: #4CAF50;
        border: 1px solid #a5d6a7;
    }
    
    /* 返回按鈕 */
    .back-button-container {
        text-align: center;
        margin-top: 3rem;
    }

    .back-button {
        display: inline-flex;
        align-items: center;
        gap: 10px;
        padding: 15px 30px;
        background-color: #6c757d; /* 灰色 */
        color: white;
        text-decoration: none;
        border-radius: 8px;
        font-size: 1.1rem;
        font-weight: 500;
        transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
        box-shadow: 0 4px 12px rgba(108, 117, 125, 0.25);
    }

    .back-button:hover {
        background-color: #5a6268;
        transform: translateY(-3px);
        box-shadow: 0 6px 16px rgba(108, 117, 125, 0.35);
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
        .detail-item {
            font-size: 1rem;
            flex-direction: column; /* 小螢幕下垂直排列 */
            align-items: flex-start;
            gap: 5px;
            padding: 0.6rem 0;
        }
        .detail-item strong {
            min-width: unset; /* 移除固定寬度 */
            width: 100%; /* 佔滿一行 */
        }
        .detail-item i {
            font-size: 1.1rem;
            width: auto;
        }
        .status-badge {
            font-size: 0.95rem;
            padding: 6px 12px;
            min-width: 80px;
        }
        .back-button {
            padding: 12px 25px;
            font-size: 1rem;
            width: 100%; /* 小螢幕下佔滿寬度 */
            box-sizing: border-box;
        }
        .back-button-container {
            margin-top: 2rem;
        }
    }

    @media (max-width: 480px) {
        .detail-container {
            padding: 1.5rem;
        }
        .detail-container h2 {
            font-size: 1.8rem;
        }
        .detail-item {
            font-size: 0.95rem;
        }
    }
</style>
</head>
<body>
    <%-- 這裡假設您的 header.jsp 和 sidebar.js 已經包含了必要的 HTML 結構 --%>
  
    <%-- 如果有側邊欄，通常會放在 container 的同級或內部 --%>
    <%-- <%@ include file="../sidebar.jsp" %> --%>

    <div class="detail-container">
        <h2><i class="fas fa-clipboard-list"></i> 報修詳細資料</h2>
        <p style="text-align: center; color: #555; margin-top: -1rem; margin-bottom: 2rem;">以下是維修單 **#<c:out value="${repair.repairId}" />** 的完整資訊。</p>

        <div class="detail-item">
            <i class="fas fa-receipt"></i> <strong>報修編號：</strong> <c:out value="${repair.repairId}" />
        </div>
        <div class="detail-item">
            <i class="fas fa-microchip"></i> <strong>機台編號：</strong> <c:out value="${repair.machineId}" />
        </div>
        <div class="detail-item">
            <i class="fas fa-desktop"></i> <strong>機台名稱：</strong> <c:out value="${repair.machineName}" />
        </div>
        <div class="detail-item">
            <i class="fas fa-exclamation-triangle"></i> <strong>報修描述：</strong> <c:out value="${repair.repairDescription}" />
        </div>
        <div class="detail-item">
            <i class="fas fa-clock"></i> <strong>報修時間：</strong>
            <c:choose>
                <c:when test="${not empty repair.repairTime}">
                    <fmt:parseDate value="${repair.repairTime}" pattern="yyyy-MM-dd HH:mm:ss.S" var="parsedRepairTime" />
                    <fmt:formatDate value="${parsedRepairTime}" pattern="yyyy 年 MM 月 dd 日 HH:mm:ss" />
                </c:when>
                <c:otherwise>
                    N/A
                </c:otherwise>
            </c:choose>
        </div>
        <div class="detail-item">
            <i class="fas fa-sync-alt"></i> <strong>狀態：</strong>
            <span class="status-badge
                <c:choose>
                    <c:when test='${repair.repairStatus == "待處理"}'>pending</c:when>
                    <c:when test='${repair.repairStatus == "處理中"}'>processing</c:when>
                    <c:when test='${repair.repairStatus == "已完成"}'>completed</c:when>
                    <c:otherwise>other</c:otherwise>
                </c:choose>
            ">
                <c:out value="${repair.repairStatus}" />
            </span>
        </div>
        <div class="detail-item">
            <i class="fas fa-user-tie"></i> <strong>員工編號：</strong> <c:out value="${repair.employeeId}" />
        </div>

        <div class="back-button-container">
            <a href="${pageContext.request.contextPath}/MachineRepairViewServlet" class="back-button">
                <i class="fas fa-arrow-left"></i> 返回列表
            </a>
        </div>
    </div>
</body>
</html>
