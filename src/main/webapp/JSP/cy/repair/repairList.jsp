<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 將 pageTitle 設置為請求屬性，供主佈局 JSP 使用 --%>
<% request.setAttribute("pageTitle", "報修紀錄查詢"); %>
<%-- 如果您的環境是 Jakarta EE 9+，則建議使用 jakarta.tags。否則保持 java.sun.com --%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
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
        background: linear-gradient(to right bottom, #e8f5e9, #c8e6c9); /* 柔和的綠色漸變背景 */
        color: #333;
        margin: 0;
        padding: 0;
        line-height: 1.6;
        min-height: 100vh;
        display: flex;
        flex-direction: column;
    }

    /* 主要內容容器 */
    .query-container {
        flex-grow: 1;
        max-width: 1200px;
        margin: 2.5rem auto;
        padding: 2.5rem;
        background-color: #ffffff;
        border-radius: 15px;
        box-shadow: 0 15px 40px rgba(0, 0, 0, 0.1);
    }

    .query-container h1 {
        font-size: 2.8rem;
        color: #2c3e50;
        margin-bottom: 0.8rem;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 15px;
    }

    .query-container > p {
        font-size: 1.2rem;
        color: #555;
        margin-top: 0;
        margin-bottom: 2rem;
        text-align: center;
    }

    /* 搜尋表單容器 */
    .search-form-card {
        background: #f8f9fa;
        padding: 25px 30px;
        margin-bottom: 30px;
        border-radius: 12px;
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        justify-content: center;
        align-items: flex-end;
    }

    .search-form-card h3 {
        width: 100%;
        font-size: 1.8rem;
        color: #34495e;
        margin-top: 0;
        margin-bottom: 20px;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
    }

    .search-form-group {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        gap: 8px;
    }

    .search-form-group label {
        font-weight: 500;
        color: #495057;
        font-size: 1rem;
        white-space: nowrap;
        display: flex;
        align-items: center;
        gap: 5px;
    }

    .search-form-card input[type="text"],
    .search-form-card select {
        padding: 10px 15px;
        border: 1px solid #ced4da;
        border-radius: 8px;
        font-size: 0.95rem;
        color: #333;
        transition: all 0.3s ease;
        background-color: #ffffff;
        min-width: 160px;
        box-sizing: border-box; /* 確保 padding 不增加寬度 */
        appearance: none; /* 移除 select 的預設樣式 */
        background-image: url('data:image/svg+xml;charset=UTF-8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>');
        background-repeat: no-repeat;
        background-position: right 12px center;
        background-size: 18px;
    }

    .search-form-card input[type="text"]:focus,
    .search-form-card select:focus {
        border-color: #007bff;
        box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
        outline: none;
    }

    .search-form-card button {
        padding: 10px 20px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 8px;
        font-size: 0.95rem;
        cursor: pointer;
        transition: background-color 0.2s ease, transform 0.2s ease;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        margin-top: 5px; /* 與輸入框對齊 */
    }

    .search-form-card button:hover {
        background-color: #0056b3;
        transform: translateY(-1px);
    }
    
    /* 搜尋結果資訊 */
    .search-result-info {
        background: #e7f3ff;
        padding: 15px 25px;
        margin-bottom: 25px;
        border-left: 5px solid #007bff;
        border-radius: 8px;
        color: #004085;
        font-size: 1.05rem;
        display: flex;
        align-items: center;
        gap: 12px;
    }

    /* 表格容器 */
    .table-responsive {
        overflow-x: auto;
        width: 100%;
        border-radius: 10px;
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
        background-color: #ffffff;
    }

    table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
    }

    table thead {
        background-color: #e9ecef;
    }

    table th,
    table td {
        padding: 15px;
        text-align: left;
        border-bottom: 1px solid #dee2e6;
    }

    table th {
        color: #495057;
        font-weight: 600;
        font-size: 1.05rem;
        position: sticky; /* 粘性表頭 */
        top: 0;
        background-color: #e9ecef;
        z-index: 1;
    }
    
    table tbody tr:last-child td {
        border-bottom: none;
    }

    table tbody tr:hover {
        background-color: #f8f9fa;
    }

    table td {
        color: #555;
        font-size: 0.95rem;
    }

    /* 狀態顯示優化 */
    .status-badge {
        display: inline-block;
        padding: 6px 12px;
        border-radius: 20px;
        font-weight: 600;
        font-size: 0.85rem;
        white-space: nowrap;
        text-align: center;
        min-width: 80px;
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

    /* 操作連結 */
    .action-link {
        display: inline-flex;
        align-items: center;
        gap: 5px;
        padding: 8px 12px;
        background-color: #17a2b8; /* 信息藍 */
        color: white;
        text-decoration: none;
        border-radius: 6px;
        font-size: 0.9rem;
        transition: background-color 0.2s ease, transform 0.2s ease;
    }

    .action-link:hover {
        background-color: #138496;
        transform: translateY(-1px);
    }

    /* 沒有資料的提示 */
    .no-data {
        text-align: center;
        padding: 3rem 1.5rem;
        color: #777;
        font-size: 1.2rem;
        background-color: #fefefe;
        border-radius: 10px;
        box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
        margin-top: 1rem;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 1rem;
    }
    
    .no-data::before {
        font-family: "Font Awesome 6 Free";
        content: "\f468"; /* 例如，一個盒子打開的圖標 */
        font-weight: 900;
        font-size: 3rem;
        color: #ccc;
        margin-bottom: 0.5rem;
    }

    /* 錯誤訊息 (前端驗證用) */
    .error-text {
        color: #dc3545;
        font-size: 0.875em;
        margin-top: 0.25rem;
        min-height: 1.25em; /* 預留空間避免佈局跳動 */
        display: block; /* 確保佔用一行 */
    }

    /* 響應式調整 */
    @media (max-width: 992px) {
        .query-container {
            margin: 2rem auto;
            padding: 2rem;
        }
        .query-container h1 {
            font-size: 2.2rem;
        }
        .search-form-card {
            flex-direction: column;
            align-items: stretch;
            gap: 20px;
        }
        .search-form-group {
            width: 100%; /* 小螢幕下佔滿寬度 */
        }
        .search-form-card input[type="text"],
        .search-form-card select,
        .search-form-card button {
            width: 100%;
            min-width: unset;
        }
        table th, table td {
            padding: 12px;
            font-size: 0.9rem;
        }
        .status-badge {
             padding: 5px 10px;
             font-size: 0.8rem;
             min-width: 70px;
        }
    }

    @media (max-width: 768px) {
        .query-container {
            margin: 1.5rem auto;
            padding: 1.5rem;
            border-radius: 10px;
        }
        .query-container h1 {
            font-size: 2rem;
            gap: 10px;
        }
        .query-container > p {
            font-size: 1rem;
            margin-bottom: 1.5rem;
        }
        .search-form-card {
            padding: 15px 20px;
        }
        .search-form-card h3 {
            font-size: 1.5rem;
            margin-bottom: 15px;
        }
        .search-result-info {
            padding: 12px 20px;
            font-size: 0.95rem;
        }
        .no-data {
            padding: 2rem 1rem;
            font-size: 1rem;
        }
        .no-data::before {
            font-size: 2.5rem;
        }
    }
</style>
</head>
<body>
   

    <div class="query-container">
        <h1><i class="fas fa-search-dollar"></i> 報修紀錄查詢</h1>
        <p>您可以透過機台編號、員工編號或報修狀態來查詢相關維修紀錄。</p>

        <div class="search-form-card">
            <h3><i class="fas fa-filter"></i> 查詢篩選</h3>
            <form action="${pageContext.request.contextPath}/FindRepairsServlet" method="get" id="searchForm">
                <div class="search-form-group">
                    <label for="machineId"><i class="fas fa-microchip"></i> 機台編號：</label>
                    <input type="text" name="machineId" id="machineId" placeholder="輸入機台編號"
                           value="${param.machineId != null ? param.machineId : ''}">
                    <div id="machineIdError" class="error-text"></div>
                </div>
                
                <div class="search-form-group">
                    <label for="employeeId"><i class="fas fa-user-tie"></i> 員工編號：</label>
                    <input type="text" name="employeeId" id="employeeId" placeholder="輸入員工編號"
                           value="${param.employeeId != null ? param.employeeId : ''}">
                    <div id="employeeIdError" class="error-text"></div>
                </div>
                
                <div class="search-form-group">
                    <label for="status"><i class="fas fa-tags"></i> 狀態：</label>
                    <select name="status" id="status">
                        <option value="" ${param.status == '' ? 'selected' : ''}>全部</option>
                        <option value="待處理" ${param.status == '待處理' ? 'selected' : ''}>待處理</option>
                        <option value="處理中" ${param.status == '處理中' ? 'selected' : ''}>處理中</option>
                        <option value="已完成" ${param.status == '已完成' ? 'selected' : ''}>已完成</option>
                    </select>
                </div>
                
                <button type="submit"><i class="fas fa-search"></i> 查詢</button>
            </form>
        </div>

        <%-- 顯示搜尋結果資訊 (如果有的話) --%>
        <c:if test="${not empty param.machineId || not empty param.employeeId || not empty param.status}">
            <div class="search-result-info">
                <i class="fas fa-info-circle"></i>
                查詢條件:
                <c:if test="${not empty param.machineId}">機台編號: <c:out value="${param.machineId}"/> </c:if>
                <c:if test="${not empty param.employeeId}">員工編號: <c:out value="${param.employeeId}"/> </c:if>
                <c:if test="${not empty param.status}">狀態: <c:out value="${param.status}"/> </c:if>
                <c:if test="${empty param.machineId && empty param.employeeId && empty param.status}">所有紀錄</c:if>
                (共找到 <c:out value="${fn:length(repairList)}" /> 筆紀錄)
            </div>
        </c:if>

        <h2 style="text-align:center; color: #2c3e50; margin-top: 30px; margin-bottom: 20px;"><i class="fas fa-list"></i> 報修紀錄列表</h2>
        
        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th><i class="fas fa-receipt"></i> 報修編號</th>
                        <th><i class="fas fa-microchip"></i> 機台編號</th>
                        <th><i class="fas fa-desktop"></i> 機台名稱</th>
                        <th><i class="fas fa-user-tie"></i> 報修人員</th>
                        <th><i class="fas fa-sync-alt"></i> 狀態</th>
                        <th><i class="fas fa-clock"></i> 報修時間</th>
                        <th><i class="fas fa-cogs"></i> 動作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty repairList}">
                            <tr>
                                <td colspan="7" class="no-data">
                                    <p><i class="fas fa-folder-open"></i> 無符合條件的報修紀錄</p>
                                    <p><small>請嘗試調整您的查詢條件。</small></p>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="repair" items="${repairList}">
                                <tr>
                                    <td>#<c:out value="${repair.repairId}" /></td>
                                    <td><c:out value="${repair.machineId}" /></td>
                                    <td><c:out value="${repair.machineName}" /></td>
                                    <td><c:out value="${repair.employeeId}" /></td>
                                    <td>
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
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty repair.repairTime}">
                                                <fmt:parseDate value="${repair.repairTime}" pattern="yyyy-MM-dd HH:mm:ss.S" var="parsedRepairTime" />
                                                <fmt:formatDate value="${parsedRepairTime}" pattern="yyyy 年 MM 月 dd 日 HH:mm:ss" />
                                            </c:when>
                                            <c:otherwise>
                                                N/A
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/RepairDetailServlet?repairId=${repair.repairId}" class="action-link">
                                            <i class="fas fa-info-circle"></i> 查看詳情
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const searchForm = document.getElementById('searchForm');
    const machineIdInput = document.getElementById('machineId');
    const employeeIdInput = document.getElementById('employeeId');
    const machineIdError = document.getElementById('machineIdError');
    const employeeIdError = document.getElementById('employeeIdError');

    function showError(element, message) {
        element.textContent = message;
        element.style.display = 'block';
    }

    function clearError(element) {
        element.textContent = '';
        element.style.display = 'none';
    }

    searchForm.addEventListener('submit', function(event) {
        let isValid = true;

        // Clear previous errors
        clearError(machineIdError);
        clearError(employeeIdError);

        const machineId = machineIdInput.value.trim();
        const employeeId = employeeIdInput.value.trim();

        // Validate machineId if entered
        if (machineId !== '' && (!/^\d+$/.test(machineId) || parseInt(machineId) <= 0)) {
            showError(machineIdError, '機台編號必須是有效的正整數。');
            isValid = false;
        }

        // Validate employeeId if entered
        if (employeeId !== '' && (!/^\d+$/.test(employeeId) || parseInt(employeeId) <= 0)) {
            showError(employeeIdError, '員工編號必須是有效的正整數。');
            isValid = false;
        }

        if (!isValid) {
            event.preventDefault(); // Stop form submission if there are errors
        }
    });

    // Optional: Real-time validation feedback
    machineIdInput.addEventListener('input', function() {
        clearError(machineIdError);
        const machineId = this.value.trim();
        if (machineId !== '' && (!/^\d+$/.test(machineId) || parseInt(machineId) <= 0)) {
            showError(machineIdError, '機台編號必須是有效的正整數。');
        }
    });

    employeeIdInput.addEventListener('input', function() {
        clearError(employeeIdError);
        const employeeId = this.value.trim();
        if (employeeId !== '' && (!/^\d+$/.test(employeeId) || parseInt(employeeId) <= 0)) {
            showError(employeeIdError, '員工編號必須是有效的正整數。');
        }
    });
});
</script>
</body>
</html>