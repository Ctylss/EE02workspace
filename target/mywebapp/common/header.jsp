<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 這裡不再聲明 userRole，它將從 dashboard.jsp 繼承 --%>

<div class="header-content">
    <%-- 
        在純桌面模式下，側邊欄預設展開，且其自身已包含品牌 Logo。
        因此，Header 中的這個較小的 Logo 通常無需顯示。
        CSS (style.css) 已將 .main-header .logo 預設為 display: none;
        如果您希望在桌面模式下這裡也顯示一個 Logo，請在 CSS 中調整。
    --%>
    <div class="logo">
        <a href="<%= request.getContextPath() %>/dashboard/dashboard.jsp" class="logo-link">
            <i class="fas fa-industry"></i> ERP
        </a>
    </div>
    <nav class="top-nav" role="navigation" aria-label="主選單">
        <ul class="top-nav-list">
            <li><a href="#" title="通知"><i class="fas fa-bell"></i><span class="sr-only">通知</span></a></li>
            <li>
                <a href="#" title="使用者資訊" class="user-info">
                    <i class="fas fa-user-circle"></i>
                    <span><%= session.getAttribute("loggedInUser") != null ? session.getAttribute("loggedInUser") : "訪客" %></span>
                </a>
            </li>
        </ul>
    </nav>
    <%-- 
        在純桌面模式下，側邊欄預設展開且始終如此。
        此切換按鈕通常只用於響應式佈局或手動收縮側邊欄。
        由於您要求純桌面模式，這個按鈕通常是多餘的，並已在 CSS 中隱藏。
        為簡潔起見，我將其從 HTML 中移除。
        如果您確實需要一個手動切換側邊欄的功能，請告訴我，我們可以重新評估其放置和行為。
    --%>
    <%-- <button class="sidebar-toggle-btn" id="sidebarToggle" aria-label="切換側邊欄">
        <i class="fas fa-bars"></i>
    </button> --%>
</div>