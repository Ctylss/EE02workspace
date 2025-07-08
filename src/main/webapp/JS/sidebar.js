// webapp/js/sidebar.js
document.addEventListener('DOMContentLoaded', () => {
    const body = document.body;
    const sidebarToggle = document.getElementById('sidebarToggle'); // 側邊欄切換按鈕 (通常在 header.jsp 中)
    const toggles = document.querySelectorAll('.dropdown-toggle'); // 子菜單切換按鈕

    // 側邊欄開關邏輯 (如果需要手動切換側邊欄狀態，則保留此部分)
    // 注意：在純桌面模式下，您可能不需要這個按鈕，或者需要將其放置在側邊欄內部。
    // 如果您不希望有任何側邊欄收起狀態，可以移除此 if 區塊。
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', () => {
            // 在純桌面模式下，只切換 'sidebar-closed' 類
            body.classList.toggle('sidebar-closed');

            // 點擊關閉側邊欄時，關閉所有子菜單
            if (body.classList.contains('sidebar-closed')) {
                document.querySelectorAll('.dropdown-menu.open').forEach(openMenu => {
                    openMenu.classList.remove('open');
                    const toggle = openMenu.previousElementSibling;
                    if (toggle) {
                        toggle.setAttribute('aria-expanded', 'false');
                        toggle.classList.remove('active');
                    }
                });
            }
        });
    }

    // 子菜單開合邏輯
    toggles.forEach(toggle => {
        toggle.addEventListener('click', e => {
            e.preventDefault();
            e.stopPropagation(); // 防止事件冒泡到 document，導致立即關閉

            const submenu = toggle.nextElementSibling;
            if (!submenu || !submenu.classList.contains('dropdown-menu')) return; // 確保有 submenu 且 class 正確

            const isOpen = submenu.classList.contains('open');

            // 關閉同層級的其他開啟子菜單
            const siblings = toggle.closest('ul').querySelectorAll('.dropdown-menu.open');
            siblings.forEach(sib => {
                if (sib !== submenu) {
                    sib.classList.remove('open');
                    const sibToggle = sib.previousElementSibling;
                    if (sibToggle) sibToggle.setAttribute('aria-expanded', 'false');
                    sibToggle.classList.remove('active');
                }
            });

            // 切換當前子菜單狀態
            if (isOpen) {
                submenu.classList.remove('open');
                toggle.setAttribute('aria-expanded', 'false');
                toggle.classList.remove('active');
            } else {
                submenu.classList.add('open');
                toggle.setAttribute('aria-expanded', 'true');
                toggle.classList.add('active');
            }
        });
    });

    // 點擊側邊欄外部關閉所有 submenu
    document.addEventListener('click', e => {
        const sidebar = document.querySelector('.main-sidebar');
        const sidebarToggleBtn = document.getElementById('sidebarToggle'); // 獲取側邊欄切換按鈕

        // 檢查點擊是否在側邊欄內部，並且不是點擊了切換按鈕
        if (sidebar && !sidebar.contains(e.target) && (!sidebarToggleBtn || !sidebarToggleBtn.contains(e.target))) {
            document.querySelectorAll('.dropdown-menu.open').forEach(openMenu => {
                openMenu.classList.remove('open');
                const toggle = openMenu.previousElementSibling;
                if (toggle) {
                    toggle.setAttribute('aria-expanded', 'false');
                    toggle.classList.remove('active');
                }
            });
            // 由於是純桌面模式，我們不希望點擊外部時側邊欄本身關閉
            // 因此移除了 body.classList.remove('sidebar-expanded') 等響應式相關邏輯
        }
    });

    // 偵測當前頁面路徑並高亮對應連結與展開父選單
    const currentPath = window.location.pathname + window.location.search; // 包含查詢參數
    document.querySelectorAll('.sidebar-nav a').forEach(link => {
        const linkPath = new URL(link.href).pathname + new URL(link.href).search;

        if (linkPath === currentPath) {
            link.classList.add('active');
            // 向上展開所有父選單
            let parentSubmenu = link.closest('.dropdown-menu');
            while (parentSubmenu) {
                parentSubmenu.classList.add('open');
                const parentToggle = parentSubmenu.previousElementSibling;
                if (parentToggle && parentToggle.classList.contains('dropdown-toggle')) {
                    parentToggle.classList.add('active');
                    parentToggle.setAttribute('aria-expanded', 'true');
                }
                // 繼續往上找父級 submenu
                parentSubmenu = parentSubmenu.parentElement.closest('.dropdown-menu');
            }
        }
    });

    // 移除所有關於初始載入時檢查螢幕寬度及視窗大小改變時調整側邊欄狀態的邏輯
    // 不再有 setInitialSidebarState() 函數及 window.addEventListener('resize')
});