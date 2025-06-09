$(document).ready(function () {
    // User data for login
    const users = [
        { username: 'admin', password: '1234', role: 'admin', userId: 'A000001', name: '管理員A' },
        { username: 'user1', password: '1234', role: 'user', userId: 'B000002', name: '操作員B' }
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
                { title: '設備維護', key: 'maintenance' } // 修正為 '設備維護' 以符合之前的討論
            ]
        },
        {
            title: '工單管理',
            key: 'workOrders',
            children: [
                { title: '產品列表', key: 'productList' }, // 修正為 '產品列表' 以符合之前的討論
                { title: '工單列表', key: 'workOrderList' },
                { title: '異常通報', key: 'abnormalNotification' } // 新增 '異常通報'
            ]
        },
        {
            title: '生產物料管理',
            key: 'materials',
            children: [
                { title: '物料庫存', key: 'materialInventory' }, // 修正為 '物料庫存'
                { title: '物料交易紀錄', key: 'materialTransactions' }, // 新增 '物料交易紀錄'
                { title: '生產紀錄', key: 'productionRecords' } // 修正為 '生產紀錄' 以符合之前的討論
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

            $('#loggedInUser').text(user.name); // 顯示姓名而不是帳號
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
            // 在這裡可以模擬將新用戶添加到 users 陣列中 (僅供前端 demo)
            // users.push({ username: username, password: password, role: 'user', userId: 'NEW_ID', name: '新用戶' });
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
                    // 根據不同的 key 載入不同的 demo 內容
                    loadDemoContent(child.key, child.title);

                    // 1. 移除所有子選單項目的 active 狀態
                    $('#menuAccordion .list .item').removeClass('active');
                    // 2. 為當前點擊的子選單項目添加 active 狀態
                    $(this).addClass('active');

                    // 3. 確保父級 accordion title 處於 active 狀態並展開
                    const $parentContent = $(this).closest('.content');
                    const $parentTitle = $parentContent.prev('.title');

                    // 移除所有主選單的 active 狀態 (避免點擊子選單時父選單活躍狀態混亂)
                    $('#menuAccordion .title').removeClass('active');

                    // 如果父級 accordion 尚未展開，則觸發點擊展開
                    if (!$parentContent.hasClass('active')) {
                        $parentTitle.trigger('click');
                    } else {
                        // 如果已經是展開狀態，確保 active class 仍然存在
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
                $(this).prev('.title').addClass('active');
            },
            onClosing: function() {
                // 當一個主菜單關閉時，移除其 active 類
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

    /**
     * Loads specific demo content based on the key.
     * This is where you put your module-specific HTML for demonstration.
     * @param {string} key - The key of the selected menu item.
     * @param {string} title - The title of the selected menu item.
     */
    function loadDemoContent(key, title) {
        let contentHtml = '';
        switch (key) {
            case 'employeeList':
                contentHtml = `
                    <p>這是 **員工列表** 頁面。在這裡您可以查看所有員工的資訊。</p>
                    <div class="ui segment">
                        <h4 class="ui header">員工資訊</h4>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>員工編號</th>
                                    <th>姓名</th>
                                    <th>部門</th>
                                    <th>職位</th>
                                    <th>聯絡方式</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>A000001</td><td>王小明</td><td>生產部</td><td>生產經理</td><td>0912-345678</td></tr>
                                <tr><td>B000002</td><td>李大華</td><td>裝配部</td><td>操作員</td><td>0923-456789</td></tr>
                                <tr><td>C000003</td><td>張美麗</td><td>維護部</td><td>維護技師</td><td>0934-567890</td></tr>
                            </tbody>
                        </table>
                        <button class="ui primary button">匯出員工資料</button>
                    </div>
                `;
                break;
            case 'addEmployee':
                contentHtml = `
                    <p>這是 **新增員工** 頁面。請填寫新員工的相關資料。</p>
                    <form class="ui form">
                        <div class="field">
                            <label>姓名</label>
                            <input type="text" placeholder="員工姓名">
                        </div>
                        <div class="field">
                            <label>部門</label>
                            <input type="text" placeholder="所屬部門">
                        </div>
                        <div class="field">
                            <label>職位</label>
                            <input type="text" placeholder="職位">
                        </div>
                        <div class="field">
                            <label>聯絡方式</label>
                            <input type="text" placeholder="電話或Email">
                        </div>
                        <button class="ui teal button" type="submit">提交新增</button>
                    </form>
                `;
                break;
            case 'equipmentStatus':
                contentHtml = `
                    <p>這是 **設備狀態** 頁面。您可以即時查看所有設備的運行情況。</p>
                    <div class="ui segment">
                        <h4 class="ui header">設備概覽</h4>
                        <div class="ui three cards">
                            <div class="card">
                                <div class="content">
                                    <div class="header">切割機001</div>
                                    <div class="meta">狀態: <span class="ui green text">運行中</span></div>
                                    <div class="description">
                                        型號: XYZ-3000<br>
                                        上次維護: 2025-05-20
                                    </div>
                                </div>
                                <div class="extra content">
                                    <div class="ui two buttons">
                                        <div class="ui basic green button">查看詳情</div>
                                        <div class="ui basic blue button">維護紀錄</div>
                                    </div>
                                </div>
                            </div>
                            <div class="card">
                                <div class="content">
                                    <div class="header">組裝線A</div>
                                    <div class="meta">狀態: <span class="ui orange text">維護中</span></div>
                                    <div class="description">
                                        型號: ASM-1000<br>
                                        預計恢復: 2025-06-12
                                    </div>
                                </div>
                                <div class="extra content">
                                    <div class="ui two buttons">
                                        <div class="ui basic green button">查看詳情</div>
                                        <div class="ui basic blue button">維護紀錄</div>
                                    </div>
                                </div>
                            </div>
                             <div class="card">
                                <div class="content">
                                    <div class="header">打包機002</div>
                                    <div class="meta">狀態: <span class="ui red text">故障</span></div>
                                    <div class="description">
                                        型號: PKG-500<br>
                                        報修時間: 2025-06-08
                                    </div>
                                </div>
                                <div class="extra content">
                                    <div class="ui two buttons">
                                        <div class="ui basic green button">查看詳情</div>
                                        <div class="ui basic blue button">維護紀錄</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
                break;
            case 'maintenance':
                contentHtml = `
                    <p>這是 **設備維護** 頁面。您可以新增或查看設備的維護歷史。</p>
                    <div class="ui segment">
                        <h4 class="ui header">維護紀錄列表</h4>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>紀錄ID</th>
                                    <th>設備名稱</th>
                                    <th>維護日期</th>
                                    <th>描述</th>
                                    <th>執行人員</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>M001</td><td>切割機001</td><td>2025-05-20</td><td>定期保養</td><td>張美麗</td></tr>
                                <tr><td>M002</td><td>組裝線A</td><td>2025-06-09</td><td>零件更換</td><td>李大華</td></tr>
                            </tbody>
                        </table>
                        <div class="ui divider"></div>
                        <h4 class="ui header">新增維護紀錄</h4>
                        <form class="ui form">
                            <div class="field">
                                <label>設備</label>
                                <select class="ui dropdown">
                                    <option value="">選擇設備</option>
                                    <option value="EQ001">切割機001</option>
                                    <option value="EQ002">組裝線A</option>
                                    <option value="EQ003">打包機002</option>
                                </select>
                            </div>
                            <div class="field">
                                <label>維護日期</label>
                                <input type="date">
                            </div>
                            <div class="field">
                                <label>描述</label>
                                <textarea rows="2" placeholder="詳細維護內容"></textarea>
                            </div>
                            <div class="field">
                                <label>執行人員</label>
                                <input type="text" placeholder="執行維護的員工姓名">
                            </div>
                            <button class="ui teal button" type="submit">提交紀錄</button>
                        </form>
                    </div>
                `;
                break;
            case 'productList':
                contentHtml = `
                    <p>這是 **產品列表** 頁面。管理系統中的所有產品資訊。</p>
                    <div class="ui segment">
                        <h4 class="ui header">所有產品</h4>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>產品編號</th>
                                    <th>產品名稱</th>
                                    <th>描述</th>
                                    <th>單位</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>PROD001</td><td>智慧燈泡</td><td>可App控制的LED燈泡</td><td>個</td></tr>
                                <tr><td>PROD002</td><td>智慧插座</td><td>支援語音助手的智慧插座</td><td>個</td></tr>
                            </tbody>
                        </table>
                        <button class="ui primary button">新增產品</button>
                    </div>
                `;
                break;
            case 'workOrderList':
                contentHtml = `
                    <p>這是 **工單列表** 頁面。查看所有生產工單的進度。</p>
                    <div class="ui segment">
                        <h4 class="ui header">當前工單</h4>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>工單編號</th>
                                    <th>產品名稱</th>
                                    <th>計畫數量</th>
                                    <th>實際數量</th>
                                    <th>狀態</th>
                                    <th>預計完成日期</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>WO20250601</td><td>智慧燈泡</td><td>1000</td><td>850</td><td>進行中</td><td>2025-06-15</td></tr>
                                <tr><td>WO20250602</td><td>智慧插座</td><td>500</td><td>500</td><td>已完成</td><td>2025-06-08</td></tr>
                            </tbody>
                        </table>
                        <button class="ui teal button">新增工單</button>
                    </div>
                `;
                break;
            case 'abnormalNotification':
                contentHtml = `
                    <p>這是 **異常通報** 頁面。您可以提交或查看生產過程中的異常情況。</p>
                    <div class="ui segment">
                        <h4 class="ui header">提交通報</h4>
                        <form class="ui form">
                            <div class="field">
                                <label>相關工單</label>
                                <select class="ui dropdown">
                                    <option value="">選擇工單</option>
                                    <option value="WO20250601">WO20250601 (智慧燈泡)</option>
                                    <option value="WO20250602">WO20250602 (智慧插座)</option>
                                </select>
                            </div>
                            <div class="field">
                                <label>異常類型</label>
                                <select class="ui dropdown">
                                    <option value="">選擇類型</option>
                                    <option value="EQUIPMENT_FAULT">設備故障</option>
                                    <option value="MATERIAL_DEFECT">物料缺陷</option>
                                    <option value="QUALITY_ISSUE">品質問題</option>
                                    <option value="OTHER">其他</option>
                                </select>
                            </div>
                            <div class="field">
                                <label>詳細描述</label>
                                <textarea rows="3" placeholder="請詳細描述異常情況"></textarea>
                            </div>
                            <div class="field">
                                <label>嚴重程度</label>
                                <select class="ui dropdown">
                                    <option value="LOW">低</option>
                                    <option value="MEDIUM">中</option>
                                    <option value="HIGH">高</option>
                                </select>
                            </div>
                            <button class="ui red button" type="submit">提交通報</button>
                        </form>
                        <div class="ui divider"></div>
                        <h4 class="ui header">異常紀錄列表</h4>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>通報ID</th>
                                    <th>工單</th>
                                    <th>異常類型</th>
                                    <th>嚴重程度</th>
                                    <th>狀態</th>
                                    <th>通報人</th>
                                    <th>發現時間</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>AB001</td><td>WO20250601</td><td>設備故障</td><td>高</td><td>待處理</td><td>李大華</td><td>2025-06-09 10:30</td></tr>
                                <tr><td>AB002</td><td>WO20250601</td><td>物料缺陷</td><td>中</td><td>調查中</td><td>王小明</td><td>2025-06-08 15:00</td></tr>
                            </tbody>
                        </table>
                    </div>
                `;
                break;
            case 'materialInventory':
                contentHtml = `
                    <p>這是 **物料庫存** 頁面。查看所有物料的即時庫存量。</p>
                    <div class="ui segment">
                        <h4 class="ui header">物料庫存概覽</h4>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>物料編號</th>
                                    <th>物料名稱</th>
                                    <th>單位</th>
                                    <th>目前庫存</th>
                                    <th>最低庫存</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>MAT001</td><td>LED晶片</td><td>顆</td><td>50000</td><td>10000</td></tr>
                                <tr><td>MAT002</td><td>電路板</td><td>片</td><td>12000</td><td>5000</td></tr>
                                <tr><td>MAT003</td><td>塑膠外殼</td><td>個</td><td>25000</td><td>8000</td></tr>
                            </tbody>
                        </table>
                        <button class="ui primary button">補貨申請</button>
                    </div>
                `;
                break;
            case 'materialTransactions':
                contentHtml = `
                    <p>這是 **物料交易紀錄** 頁面。查看物料的入庫、出庫和調整記錄。</p>
                    <div class="ui segment">
                        <h4 class="ui header">物料交易歷史</h4>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>交易ID</th>
                                    <th>物料名稱</th>
                                    <th>交易類型</th>
                                    <th>數量</th>
                                    <th>交易日期</th>
                                    <th>相關工單</th>
                                    <th>記錄人員</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>TR001</td><td>LED晶片</td><td>入庫</td><td>10000</td><td>2025-06-05 14:00</td><td>-</td><td>管理員A</td></tr>
                                <tr><td>TR002</td><td>電路板</td><td>出庫</td><td>500</td><td>2025-06-09 09:15</td><td>WO20250601</td><td>操作員B</td></tr>
                                <tr><td>TR003</td><td>塑膠外殼</td><td>調整</td><td>-100</td><td>2025-06-07 11:00</td><td>-</td><td>管理員A</td></tr>
                            </tbody>
                        </table>
                    </div>
                `;
                break;
            case 'productionRecords':
                contentHtml = `
                    <p>這是 **生產紀錄** 頁面。操作員可以提交每日生產數據。</p>
                    <div class="ui segment">
                        <h4 class="ui header">新增生產紀錄</h4>
                        <form class="ui form">
                            <div class="field">
                                <label>選擇工單</label>
                                <select class="ui dropdown">
                                    <option value="">選擇工單</option>
                                    <option value="WO20250601">WO20250601 (智慧燈泡)</option>
                                    <option value="WO20250602">WO20250602 (智慧插座)</option>
                                </select>
                            </div>
                            <div class="field">
                                <label>生產數量</label>
                                <input type="number" placeholder="本次生產的合格數量">
                            </div>
                            <div class="field">
                                <label>不良品數量</label>
                                <input type="number" placeholder="本次生產的不良品數量">
                            </div>
                            <div class="field">
                                <label>備註 (選填)</label>
                                <textarea rows="2"></textarea>
                            </div>
                            <button class="ui teal button" type="submit">提交生產數據</button>
                        </form>
                        <div class="ui divider"></div>
                        <h4 class="ui header">近期生產紀錄</h4>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>紀錄ID</th>
                                    <th>工單</th>
                                    <th>生產數量</th>
                                    <th>不良品數量</th>
                                    <th>操作人員</th>
                                    <th>記錄時間</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>PR001</td><td>WO20250601</td><td>150</td><td>5</td><td>李大華</td><td>2025-06-09 10:00</td></tr>
                                <tr><td>PR002</td><td>WO20250601</td><td>200</td><td>8</td><td>李大華</td><td>2025-06-09 14:00</td></tr>
                            </tbody>
                        </table>
                    </div>
                `;
                break;
            case 'attendanceRecords':
                contentHtml = `
                    <p>這是 **出勤紀錄** 頁面。您可以查看個人的出勤詳情或管理所有員工的出勤。</p>
                    <div class="ui segment">
                        <h4 class="ui header">我的出勤紀錄</h4>
                        <p>今日狀態：<span class="ui green text">已打卡上班</span></p>
                        <button class="ui primary button">打卡上班</button>
                        <button class="ui grey button">打卡下班</button>
                        <div class="ui divider"></div>
                        <h4 class="ui header">所有員工出勤概覽</h4>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>員工姓名</th>
                                    <th>日期</th>
                                    <th>上班時間</th>
                                    <th>下班時間</th>
                                    <th>狀態</th>
                                    <th>總工時</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>王小明</td><td>2025-06-09</td><td>08:55</td><td>17:30</td><td>準時</td><td>8.5小時</td></tr>
                                <tr><td>李大華</td><td>2025-06-09</td><td>09:10</td><td>17:30</td><td>遲到</td><td>8.0小時</td></tr>
                                <tr><td>張美麗</td><td>2025-06-09</td><td>-</td><td>-</td><td>請假</td><td>0小時</td></tr>
                            </tbody>
                        </table>
                        <button class="ui blue button">補打卡/修正</button>
                    </div>
                `;
                break;
            case 'leaveRequests':
                contentHtml = `
                    <p>這是 **請假申請** 頁面。您可以提交請假申請或審批員工的請假請求。</p>
                    <div class="ui segment">
                        <h4 class="ui header">提交請假申請</h4>
                        <form class="ui form">
                            <div class="field">
                                <label>請假類型</label>
                                <select class="ui dropdown">
                                    <option value="">選擇類型</option>
                                    <option value="ANNUAL">年假</option>
                                    <option value="SICK">病假</option>
                                    <option value="PERSONAL">事假</option>
                                </select>
                            </div>
                            <div class="field">
                                <label>開始日期</label>
                                <input type="date">
                            </div>
                            <div class="field">
                                <label>結束日期</label>
                                <input type="date">
                            </div>
                            <div class="field">
                                <label>請假原因</label>
                                <textarea rows="2"></textarea>
                            </div>
                            <button class="ui teal button" type="submit">提交申請</button>
                        </form>
                        <div class="ui divider"></div>
                        <h4 class="ui header">我的請假申請歷史</h4>
                        <table class="ui celled table">
                            <thead>
                                <tr>
                                    <th>申請ID</th>
                                    <th>請假類型</th>
                                    <th>開始日期</th>
                                    <th>結束日期</th>
                                    <th>狀態</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>L001</td><td>年假</td><td>2025-07-01</td><td>2025-07-03</td><td>待審批</td></tr>
                                <tr><td>L002</td><td>病假</td><td>2025-05-10</td><td>2025-05-10</td><td>已批准</td></tr>
                            </tbody>
                        </table>
                    </div>
                `;
                break;
            default:
                contentHtml = `<p>請從左側選單選擇功能，這裡是 **${title}** 的佔位內容。</p>`;
                break;
        }
        loadContent(title, contentHtml);
        // 初始化新的 dropdown 元素 (如果頁面有動態生成 dropdown)
        $('.ui.dropdown').dropdown();
    }

    // 頁面加載時，初始化登入頁面的訊息區塊
    hideSemanticMessage($('#loginMessage'));
    hideSemanticMessage($('#registerMessage'));
    hideSemanticMessage($('#forgotMessage'));

    // 初始化所有的 dropdown 元素，這對於像下拉選單這樣的 Semantic UI 組件是必需的
    $('.ui.dropdown').dropdown();
});