<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-TW">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>工業管理系統 - 登入</title>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+TC:wght@300;400;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Noto Sans TC', sans-serif;
            background: linear-gradient(135deg, #2c3e50, #4ca1af);
            margin: 0;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }

        .login-page {
            background-color: transparent;
        }

        .login-wrapper {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .login-container {
            background-color: #ffffffee;
            padding: 40px 30px;
            border-radius: 16px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.25);
            width: 100%;
            max-width: 400px;
        }

        .login-container h2 {
            margin-bottom: 30px;
            text-align: center;
            color: #2c3e50;
            font-weight: 700;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 6px;
            color: #34495e;
            font-size: 0.95rem;
        }

        .form-group input {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 1rem;
            box-sizing: border-box;
            transition: border-color 0.3s ease;
        }

        .form-group input:focus {
            border-color: #4ca1af;
            outline: none;
        }

        .error-message {
            color: #e74c3c;
            font-size: 0.9rem;
            margin-top: -10px;
            margin-bottom: 20px;
            text-align: center;
        }

        .btn-primary {
            width: 100%;
            padding: 12px;
            background: #4ca1af;
            border: none;
            color: #fff;
            font-size: 1rem;
            border-radius: 8px;
            cursor: pointer;
            transition: background 0.3s ease;
        }

        .btn-primary:hover {
            background: #3c8fa0;
        }
    </style>
</head>
<body class="login-page">
    <div class="login-wrapper">
        <div class="login-container">
            <h2>工業管理系統</h2>
            <form action="<%= request.getContextPath() %>/loginProcess" method="post">
                <div class="form-group">
                    <label for="username">使用者名稱:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div class="form-group">
                    <label for="password">密碼:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <%
                    String error = request.getParameter("error");
                    if (error != null && error.equals("invalid")) {
                %>
                    <p class="error-message">使用者名稱或密碼錯誤。</p>
                <%
                    }
                %>
                <button type="submit" class="btn-primary">登入</button>
            </form>
        </div>
    </div>
</body>
</html>
