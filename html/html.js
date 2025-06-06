$(document).ready(function () {
    // User data for login
    const users = [
        { username: 'admin', password: '1234', role: 'admin', userId: 'A000001' },
        { username: 'user1', password: '1234', role: 'user', userId: 'B000002' }
    ];

    // Menu data for the sidebar
    const menuData = [
        {
            title: '人員及員工管理',
            key: 'personnel',
            children: [
                { title: '員工列表', key: 'employeeList' },
                { title: '新增員工', key: 'addEmployee' }
            ]
        },
        {
            title: '設備及機器管理',
            key: 'equipment',
            children: [
                { title: '設備狀態', key: 'equipmentStatus' },
                { title: '維修記錄', key: 'maintenance' }
            ]
        },
        {
            title: '工單管理',
            key: 'workOrders',
            children: [
                { title: '工單列表', key: 'workOrderList' },
                { title: '新增工單', key: 'addWorkOrder' }
            ]
        },
        {
            title: '生產物料管理',
            key: 'materials',
            children: [
                { title: '庫存查詢', key: 'inventory' },
                { title: '物料申請', key: 'materialRequest' }
            ]
        },
        {
            title: '出缺勤及人事管理',
            key: 'attendance',
            children: [
                { title: '出勤紀錄', key: 'attendanceRecords' },
                { title: '請假申請', key: 'leaveRequests' }
            ]
        }
    ];

    /**
     * 顯示 Semantic UI 訊息
     * @param {jQuery} $messageElement - 訊息容器的 jQuery 物件
     * @param {string} type - 訊息類型 ('positive', 'negative', 'info', 'warning')
     * @param {string} header - 訊息標題
     * @param {string} content - 訊息內容
     */
    function showSemanticMessage($messageElement, type, header, content) {
        $messageElement
            .removeClass('positive negative info warning')
            .addClass(type)
            .html(`
                <i class="close icon"></i>
                <div class="header">${header}</div>
                <p>${content}</p>
            `)
            .show()
            .transition('fade in');

        // 點擊關閉按鈕隱藏訊息
        $messageElement.find('.close.icon').off('click').on('click', function() {
            $(this).closest('.message').transition('fade out');
        });
    }

    /**
     * 隱藏 Semantic UI 訊息
     * @param {jQuery} $messageElement - 訊息容器的 jQuery 物件
     */
    function hideSemanticMessage($messageElement) {
        $messageElement.hide().empty();
    }


    /**
     * Handles the user login process.
     * Verifies username, password against predefined users.
     */
    window.login = function () {
        const $loginButton = $('.ui.fluid.large.teal.submit.button');
        const $usernameInput = $('#username');
        const $passwordInput = $('#password');
        const $loginMessage = $('#loginMessage');

        // 清除之前的訊息
        hideSemanticMessage($loginMessage);

        const username = $usernameInput.val().trim();
        const password = $passwordInput.val().trim();

        if (!username || !password) {
            showSemanticMessage($loginMessage, 'warning', '輸入錯誤', '請輸入帳號與密碼');
            return;
        }

        $loginButton.addClass('loading disabled'); // 顯示 loading 狀態

        // 模擬異步驗證 (例如 AJAX 請求)
        setTimeout(() => {
            const user = users.find((u) => u.username === username && u.password === password);

            if (!user) {
                showSemanticMessage($loginMessage, 'negative', '登入失敗', '帳號或密碼錯誤');
                $loginButton.removeClass('loading disabled');
                return;
            }

            $('#loginContainer').hide();
            $('#dashboard').show();

            $('#loggedInUser').text(user.username);
            $('#userId').text(user.userId);
            $('#userRole').text(user.role === 'admin' ? '管理員' : '一般使用者');

            initMenu(); // 初始化選單
            loadContent('歡迎使用工業管理系統', '<p>請從左側選單選擇功能</p>');

            $loginButton.removeClass('loading disabled');
            $passwordInput.val(''); // 清除密碼欄位
        }, 800); // 模擬網路延遲
    };

    /**
     * Handles user logout.
     * Confirms with the user, then hides dashboard and shows login.
     */
    window.logout = function () {
        if (confirm('確定要登出嗎？')) {
            $('#dashboard').hide();
            $('#loginContainer').show();
            $('#username').val('');
            $('#password').val('');
            $('#menuAccordion').empty(); // 清空選單
            $('#contentArea').html('<h2 class="ui header">歡迎使用工業管理系統</h2><p>請從左側選單選擇功能</p>');
            // 隱藏登入頁面的所有訊息
            hideSemanticMessage($('#loginMessage'));
        }
    };

    /**
     * Switches view to the registration form.
     */
    window.showRegister = function () {
        $('#loginContainer').hide();
        $('#forgotContainer').hide();
        $('#registerContainer').show();
        hideSemanticMessage($('#loginMessage')); // 隱藏登入頁面訊息
        hideSemanticMessage($('#forgotMessage')); // 隱藏忘記密碼頁面訊息
        hideSemanticMessage($('#registerMessage')); // 隱藏註冊頁面訊息 (如果已有)
        $('#registerUsername').val('');
        $('#registerPassword').val('');
    };

    /**
     * Switches view to the forgot password form.
     */
    window.showForgotPassword = function () {
        $('#loginContainer').hide();
        $('#registerContainer').hide();
        $('#forgotContainer').show();
        hideSemanticMessage($('#loginMessage')); // 隱藏登入頁面訊息
        hideSemanticMessage($('#registerMessage')); // 隱藏註冊頁面訊息
        hideSemanticMessage($('#forgotMessage')); // 隱藏忘記密碼頁面訊息 (如果已有)
        $('#forgotUsername').val('');
    };

    /**
     * Switches view back to the login form.
     */
    window.showLogin = function () {
        $('#registerContainer').hide();
        $('#forgotContainer').hide();
        $('#loginContainer').show();
        hideSemanticMessage($('#registerMessage')); // 隱藏註冊頁面訊息
        hideSemanticMessage($('#forgotMessage')); // 隱藏忘記密碼頁面訊息
        hideSemanticMessage($('#loginMessage')); // 隱藏登入頁面訊息 (如果已有)
        $('#username').val('');
        $('#password').val('');
    };

    /**
     * Handles new user registration (client-side test).
     * Alerts user and switches to login view.
     */
    window.register = function () {
        const $registerButton = $('#registerContainer .ui.fluid.teal.button');
        const $registerUsernameInput = $('#registerUsername');
        const $registerPasswordInput = $('#registerPassword');
        const $registerMessage = $('#registerMessage');

        hideSemanticMessage($registerMessage);

        const username = $registerUsernameInput.val().trim();
        const password = $registerPasswordInput.val().trim();
        if (!username || !password) {
            showSemanticMessage($registerMessage, 'warning', '輸入錯誤', '請輸入帳號與密碼');
            return;
        }

        $registerButton.addClass('loading disabled');

        setTimeout(() => {
            // 實際應用中，這裡會是 AJAX 請求後端進行註冊
            showSemanticMessage($registerMessage, 'positive', '註冊成功', '您的帳號已成功註冊，請返回登入。');
            $registerButton.removeClass('loading disabled');
            $registerUsernameInput.val('');
            $registerPasswordInput.val('');
            // showLogin(); // 可以在成功後自動跳轉，也可以讓用戶手動點擊返回
        }, 800);
    };

    /**
     * Handles forgot password request (client-side test).
     * Alerts user and switches to login view.
     */
    window.forgotPassword = function () {
        const $forgotButton = $('#forgotContainer .ui.fluid.teal.button');
        const $forgotUsernameInput = $('#forgotUsername');
        const $forgotMessage = $('#forgotMessage');

        hideSemanticMessage($forgotMessage);

        const username = $forgotUsernameInput.val().trim();
        if (!username) {
            showSemanticMessage($forgotMessage, 'warning', '輸入錯誤', '請輸入帳號');
            return;
        }

        $forgotButton.addClass('loading disabled');

        setTimeout(() => {
            // 實際應用中，這裡會是 AJAX 請求後端發送重設連結
            showSemanticMessage($forgotMessage, 'positive', '請求已送出', '已寄送重設密碼連結至您的信箱 (測試階段)。');
            $forgotButton.removeClass('loading disabled');
            $forgotUsernameInput.val('');
            // showLogin(); // 可以在成功後自動跳轉
        }, 800);
    };

    /**
     * Initializes the sidebar menu (accordion).
     * Populates menu from menuData and sets up click handlers.
     */
    function initMenu() {
        const $accordion = $('#menuAccordion');
        $accordion.empty(); // 清空現有選單項目

        // 重置 Semantic UI accordion，避免重複綁定和行為異常
        if ($accordion.data('accordion')) {
            $accordion.accordion('destroy');
        }

        menuData.forEach((item) => {
            // 為每個主菜單項目創建一個唯一的 data-key
            const title = $(`
                <div class="title" data-parent-key="${item.key}">
                    <i class="dropdown icon"></i>
                    ${item.title}
                </div>
            `);

            const content = $('<div class="content"><div class="ui list"></div></div>');
            const $list = content.find('.list');

            item.children.forEach((child) => {
                const $childItem = $(`<a class="item" href="#" data-key="${child.key}" data-parent-key="${item.key}">${child.title}</a>`);
                
                $childItem.on('click', function (e) {
                    e.preventDefault();
                    loadContent(child.title, `<p>這是「${child.title}」功能的內容範例。</p>`);
                    
                    // 1. 移除所有子選單項目的 active 狀態
                    $('#menuAccordion .list .item').removeClass('active');
                    // 2. 為當前點擊的子選單項目添加 active 狀態
                    $(this).addClass('active');

                    // 3. 確保父級 accordion title 處於 active 狀態並展開
                    const $parentContent = $(this).closest('.content');
                    const $parentTitle = $parentContent.prev('.title');

                    // 移除所有主選單的 active 狀態
                    $('#menuAccordion .title').removeClass('active');

                    // 如果父級 accordion 尚未展開，則觸發點擊展開
                    // Semantic UI accordion 在展開時會自動添加 'active' class 到 title
                    if (!$parentContent.hasClass('active')) {
                        $parentTitle.trigger('click');
                    } else {
                        // 如果已經是展開狀態，確保 active class 仍然存在（以防手動移除）
                        $parentTitle.addClass('active');
                    }
                });
                $list.append($childItem);
            });

            $accordion.append(title).append(content);
        });

        // 重新初始化 Semantic UI accordion
        $accordion.accordion({
            exclusive: false, // 允許同時開啟多個區塊
            animateChildren: true, // 開啟子選單動畫
            duration: 200, // 動畫時長
            onOpening: function() {
                // 當一個主菜單打開時，為其添加 active 類
                // 這裡的 'this' 是 .content 元素
                $(this).prev('.title').addClass('active');
            },
            onClosing: function() {
                // 當一個主菜單關閉時，移除其 active 類
                // 這裡的 'this' 是 .content 元素
                $(this).prev('.title').removeClass('active');
                // 同時移除其所有子選單的 active 狀態，防止關閉後子選單仍顯示為活躍
                $(this).find('.list .item').removeClass('active');
            }
        });
    }

    /**
     * Loads content into the main content area of the dashboard.
     * @param {string} title - The title for the content area.
     * @param {string} html - The HTML content to display.
     */
    function loadContent(title, html) {
        $('#contentArea').html(`
            <h2 class="ui header">${title}</h2>
            ${html}
        `);
    }

    // 頁面加載時，初始化登入頁面的訊息區塊
    hideSemanticMessage($('#loginMessage'));
    hideSemanticMessage($('#registerMessage'));
    hideSemanticMessage($('#forgotMessage'));
});