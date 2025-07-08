<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- 設置頁面標題 --%>
<% request.setAttribute("pageTitle", "帳號密碼管理"); %>

<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("pageTitle") != null ? request.getAttribute("pageTitle") : "工業管理系統" %></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />

    <style>
        /* 通用樣式 */
        body {
            font-family: 'Inter', sans-serif; /* 使用 Inter 字體 */
            margin: 0;
            padding: 0;
            background-color: #f4f7f6; /* 淺灰色背景 */
            color: #333;
            display: flex;
            min-height: 100vh; /* 讓頁面至少佔據視窗高度 */
        }

        .app-wrapper {
            display: flex;
            width: 100%;
        }

        /* 側邊欄樣式 */
        .main-sidebar {
            width: 250px; /* 固定寬度 */
            background-color: #2c3e50; /* 深藍色 */
            color: #ecf0f1;
            padding: 20px 0;
            box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
            display: flex;
            flex-direction: column;
            border-top-right-radius: 15px; /* 圓角 */
            border-bottom-right-radius: 15px; /* 圓角 */
        }

        .sidebar-brand {
            text-align: center;
            margin-bottom: 30px;
            padding: 0 15px;
        }

        .brand-link {
            display: flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
            color: #ecf0f1;
            font-size: 1.5em;
            font-weight: bold;
            padding: 10px 0;
            background-color: #34495e; /* 品牌背景色 */
            border-radius: 8px; /* 圓角 */
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            transition: background-color 0.3s ease;
        }

        .brand-link:hover {
            background-color: #4a667b;
        }

        .brand-link i {
            margin-right: 10px;
            font-size: 1.8em;
            color: #1abc9c; /* 品牌圖標顏色 */
        }

        .sidebar-nav {
            flex-grow: 1; /* 佔據剩餘空間 */
        }

        .sidebar-menu {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .menu-item {
            position: relative;
            margin-bottom: 5px;
        }

        .menu-link {
            display: flex;
            align-items: center;
            padding: 12px 20px;
            color: #ecf0f1;
            text-decoration: none;
            font-size: 1em;
            transition: background-color 0.3s ease, color 0.3s ease;
            border-radius: 8px; /* 圓角 */
            margin: 0 10px;
        }

        .menu-link:hover, .menu-link.active {
            background-color: #1abc9c; /* 活躍和懸停顏色 */
            color: #fff;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
        }

        .menu-link i {
            margin-right: 15px;
            font-size: 1.2em;
        }

        .submenu-always-open, .nested-submenu-always-open {
            list-style: none;
            padding: 0;
            margin-top: 5px;
            background-color: #34495e; /* 子菜單背景色 */
            border-radius: 8px; /* 圓角 */
            margin: 5px 15px 10px 15px;
            box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        .submenu-always-open li, .nested-submenu-always-open li {
            margin: 0;
        }

        .submenu-link, .nested-submenu-link {
            display: block;
            padding: 10px 25px; /* 內縮一些 */
            color: #bdc3c7; /* 子菜單文字顏色 */
            text-decoration: none;
            font-size: 0.95em;
            transition: background-color 0.3s ease, color 0.3s ease;
            border-radius: 6px; /* 圓角 */
            margin: 0 5px;
        }

        .submenu-link:hover, .nested-submenu-link:hover {
            background-color: #2ecc71; /* 懸停時的亮綠色 */
            color: #fff;
        }

        /* 主內容區域樣式 */
        .main-right-content-wrapper {
            flex-grow: 1;
            display: flex;
            flex-direction: column;
        }

        .main-header {
            background-color: #fff;
            padding: 15px 30px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: sticky; /* 讓 header 黏在頂部 */
            top: 0;
            z-index: 1000;
            border-bottom-left-radius: 15px; /* 圓角 */
            border-bottom-right-radius: 15px; /* 圓角 */
            margin: 10px 10px 0 10px;
        }

        .header-left h1 {
            margin: 0;
            font-size: 1.5em;
            color: #2c3e50;
        }

        .header-right .user-info {
            display: flex;
            align-items: center;
            font-size: 1em;
            color: #555;
        }

        .header-right .user-info i {
            margin-right: 8px;
            color: #2980b9; /* 用戶圖標顏色 */
        }

        .actual-page-content {
            flex-grow: 1;
            padding: 20px;
            overflow-y: auto; /* 允許內容區域滾動 */
            margin: 10px; /* 與 Header 保持間距 */
            background-color: #ffffff; /* 內容區背景色 */
            border-radius: 15px; /* 圓角 */
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05); /* 輕微陰影 */
        }

        .main-content {
            padding: 20px;
        }

        .container {
            max-width: 960px;
            margin: 0 auto;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
        }

        h2 {
            color: #2c3e50;
            margin-bottom: 25px;
            text-align: center;
            font-size: 2em;
            font-weight: 600;
        }

        /* 表單樣式 */
        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }

        .form-group input[type="password"],
        .form-group select {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 1em;
            box-sizing: border-box; /* 確保 padding 不增加寬度 */
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-group input[type="password"]:focus,
        .form-group select:focus {
            border-color: #2980b9;
            box-shadow: 0 0 0 3px rgba(41, 128, 185, 0.2);
            outline: none;
        }

        .btn-primary {
            background-color: #2980b9; /* 藍色按鈕 */
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 1.1em;
            font-weight: bold;
            transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            display: block;
            width: fit-content;
            margin: 20px auto 0 auto; /* 居中 */
        }

        .btn-primary:hover {
            background-color: #3498db; /* 淺藍色 */
            transform: translateY(-2px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.25);
        }

        .btn-primary:active {
            transform: translateY(0);
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.15);
        }

        /* 消息提示 */
        .message {
            padding: 12px 20px;
            margin-top: 20px;
            border-radius: 8px;
            font-weight: bold;
            text-align: center;
            opacity: 0; /* 初始隱藏 */
            transition: opacity 0.3s ease;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .message.success {
            background-color: #d4edda; /* 綠色背景 */
            color: #155724; /* 深綠文字 */
            border: 1px solid #c3e6cb;
            opacity: 1; /* 顯示時 */
        }

        .message.error {
            background-color: #f8d7da; /* 紅色背景 */
            color: #721c24; /* 深紅文字 */
            border: 1px solid #f5c6cb;
            opacity: 1; /* 顯示時 */
        }

        /* 儀表板卡片樣式 (用於 dashboard.jsp，但在此處也提供以保持完整性) */
        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 25px;
            margin-top: 30px;
        }

        .card {
            background-color: #fff;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
            display: flex;
            align-items: center;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            position: relative;
            overflow: hidden; /* 隱藏背景圖標溢出 */
        }

        .card::before {
            content: '';
            position: absolute;
            top: -10px;
            right: -10px;
            font-family: 'Font Awesome 5 Free';
            font-weight: 900;
            font-size: 5em;
            color: rgba(0, 0, 0, 0.05); /* 淺色背景圖標 */
            z-index: 0;
            transform: rotate(15deg);
        }
        .production-orders::before { content: "\f0ea"; } /* fa-clipboard-list */
        .purchase-orders::before { content: "\f07a"; } /* fa-shopping-cart */
        .equipment-alerts::before { content: "\f071"; } /* fa-exclamation-triangle */
        .production-progress::before { content: "\f201"; } /* fa-chart-line */
        .inventory-warning::before { content: "\f468"; } /* fa-boxes */


        .card:hover {
            transform: translateY(-8px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
        }

        .card-icon {
            font-size: 2.5em;
            margin-right: 20px;
            color: #3498db; /* 默認圖標顏色 */
            background-color: rgba(52, 152, 219, 0.1);
            padding: 15px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            min-width: 70px;
            min-height: 70px;
            z-index: 1; /* 確保在背景圖標之上 */
        }

        /* 特定卡片圖標顏色 */
        .production-orders .card-icon { color: #2ecc71; background-color: rgba(46, 204, 113, 0.1); }
        .purchase-orders .card-icon { color: #f39c12; background-color: rgba(243, 156, 18, 0.1); }
        .equipment-alerts .card-icon { color: #e74c3c; background-color: rgba(231, 76, 60, 0.1); }
        .production-progress .card-icon { color: #9b59b6; background-color: rgba(155, 89, 182, 0.1); }
        .inventory-warning .card-icon { color: #16a085; background-color: rgba(22, 160, 133, 0.1); }


        .card-content {
            flex-grow: 1;
            z-index: 1; /* 確保在背景圖標之上 */
        }

        .card-title {
            font-size: 1.3em;
            margin: 0 0 8px 0;
            color: #2c3e50;
        }

        .card-value {
            font-size: 1.1em;
            font-weight: bold;
            color: #555;
            margin-bottom: 15px;
        }

        .card-link {
            display: inline-flex;
            align-items: center;
            color: #3498db;
            text-decoration: none;
            font-weight: bold;
            font-size: 0.9em;
            transition: color 0.3s ease;
        }

        .card-link:hover {
            color: #2980b9;
        }

        .card-link i {
            margin-left: 8px;
            font-size: 0.9em;
        }

        .progress-bar-container {
            background-color: #e0e0e0;
            border-radius: 5px;
            height: 10px;
            margin-top: 10px;
            overflow: hidden;
        }

        .progress-bar {
            background-color: #2ecc71; /* 進度條綠色 */
            height: 100%;
            width: 0; /* JS 會更新 */
            border-radius: 5px;
            transition: width 0.5s ease-in-out;
        }

        /* 用戶角色顯示 */
        .content-header .user-role {
            font-size: 1.1em;
            color: #666;
            margin-top: 15px;
            padding: 10px 15px;
            background-color: #e9ecef;
            border-left: 5px solid #007bff;
            border-radius: 5px;
        }

        /* Footer 樣式 */
        .main-footer {
            background-color: #2c3e50;
            color: #ecf0f1;
            padding: 20px 30px;
            text-align: center;
            box-shadow: 0 -2px 6px rgba(0, 0, 0, 0.1);
            border-top-left-radius: 15px;
            border-top-right-radius: 15px;
            margin: 10px 10px 0 10px; /* 與實際內容區保持間距 */
        }

        .footer-content {
            font-size: 0.9em;
        }

        /* 響應式設計 */
        @media (max-width: 768px) {
            body {
                flex-direction: column;
            }
            .main-sidebar {
                width: 100%;
                height: auto;
                border-radius: 0;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
                margin-bottom: 10px;
            }
            .main-right-content-wrapper {
                margin-left: 0;
            }
            .main-header, .main-footer {
                border-radius: 0;
                margin: 0;
            }
            .actual-page-content {
                margin: 0;
                border-radius: 0;
            }
            .dashboard-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <%-- 引入 header.jsp，它現在包含了 <head> 區塊和 <header> 導航條 --%>
    <%-- 注意：header.jsp 中如果也包含了 <head> 標籤，這將導致重複。
         建議將公共的 <head> 內容（如 meta, link to font-awesome）放在 manageUsers.jsp 中，
         而 header.jsp 只包含實際的 HTML 頭部結構。
         此處假設 header.jsp 不再包含 <head> 和 <body> 標籤。
    --%>
  

    <%-- 這裡開始定義頁面佈局 --%>
    <div class="app-wrapper">
        <%
            // 檢查使用者是否已登入
            String loggedInUser = (String) session.getAttribute("loggedInUser");
            String userRole = (String) session.getAttribute("userRole"); // 從 Session 獲取 userRole

            // 將 userRole 設置為 Request 屬性，以便 common/sidebar.jsp 可以訪問
            request.setAttribute("userRole", userRole); // <-- 新增這一行

            // 僅允許 'admin' 和 'personnel' 訪問此頁面
            if (loggedInUser == null || (!"admin".equals(userRole) && !"personnel".equals(userRole))) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
        %>

        <aside class="main-sidebar" role="complementary" aria-label="側邊欄">
            <jsp:include page="/common/sidebar.jsp" /> <%-- 側邊欄 --%>
        </aside>

        <div class="main-right-content-wrapper">
            <div class="actual-page-content">
                <div class="main-content">
                    <div class="container">
                        <h2>帳號密碼管理</h2>
                        <form id="updatePasswordForm">
                            <div class="form-group">
                                <label for="targetUsername">選擇使用者帳號:</label>
                                <select id="targetUsername" name="targetUsername" required>
                                    <%-- 使用 JavaScript 動態載入使用者列表 --%>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="newPassword">新密碼:</label>
                                <input type="password" id="newPassword" name="newPassword" required minlength="6">
                            </div>
                            <div class="form-group">
                                <label for="confirmPassword">確認新密碼:</label>
                                <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6">
                            </div>
                            <button type="submit" class="btn-primary">更新密碼</button>
                        </form>
                        <div id="message" class="message" style="display: none;"></div>
                    </div>
                </div>

                <%-- 頁面底部內容的 footer.jsp --%>
                <jsp:include page="/common/footer.jsp" />
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const selectElement = document.getElementById('targetUsername');
            const updateForm = document.getElementById('updatePasswordForm');
            const newPasswordInput = document.getElementById('newPassword');
            const confirmPasswordInput = document.getElementById('confirmPassword');
            const messageDiv = document.getElementById('message');

            // 1. 載入使用者列表
            fetch('<%= request.getContextPath() %>/userManagement?action=getUsers')
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(text => {
                            throw new Error('伺服器錯誤: ' + response.status + ' ' + response.statusText + ' - ' + text);
                        });
                    }
                    return response.text();
                })
                .then(data => {
                    if (data && data.trim().length > 0) {
                        const users = data.split(',');
                        users.forEach(user => {
                            const option = document.createElement('option');
                            option.value = user.trim();
                            option.textContent = user.trim();
                            selectElement.appendChild(option);
                        });
                    } else {
                        showMessage('沒有使用者資料可供載入。', 'error');
                    }
                })
                .catch(error => {
                    showMessage('無法載入使用者列表: ' + error.message, 'error');
                    console.error('Error fetching users:', error);
                });

            // 2. 處理密碼更新提交
            updateForm.addEventListener('submit', function(e) {
                e.preventDefault();

                if (newPasswordInput.value !== confirmPasswordInput.value) {
                    showMessage('新密碼與確認密碼不一致！', 'error');
                    return;
                }
                if (newPasswordInput.value.length < 6) {
                    showMessage('密碼長度至少為6位！', 'error');
                    return;
                }

                const formData = new FormData();
                formData.append('action', 'updatePassword');
                formData.append('targetUsername', selectElement.value);
                formData.append('newPassword', newPasswordInput.value);

                fetch('<%= request.getContextPath() %>/userManagement', {
                    method: 'POST',
                    body: formData
                })
                .then(response => {
                    if (response.ok) {
                        return response.text();
                    } else {
                        return response.text().then(text => { throw new Error(text || response.statusText); });
                    }
                })
                .then(data => {
                    if (data === 'success') {
                        showMessage('密碼更新成功！', 'success');
                        newPasswordInput.value = '';
                        confirmPasswordInput.value = '';
                    } else {
                        showMessage('密碼更新失敗: ' + data, 'error');
                    }
                })
                .catch(error => {
                    showMessage('發生錯誤: ' + error.message, 'error');
                    console.error('Error updating password:', error);
                });
            });

            function showMessage(msg, type) {
                messageDiv.textContent = msg;
                messageDiv.className = 'message ' + type;
                messageDiv.style.display = 'block';
                setTimeout(() => {
                    messageDiv.style.display = 'none';
                }, 5000);
            }
        });
    </script>
</body>
</html>
