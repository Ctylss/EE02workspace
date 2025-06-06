 $(document).ready(function () {
    const users = [
      { username: 'admin', password: '1234', role: 'admin', userId: 'A000001' },
      { username: 'user1', password: '1234', role: 'user', userId: 'B000002' }
    ];

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

    window.login = function () {
      const username = $('#username').val().trim();
      const password = $('#password').val().trim();
      const role = $('#role').val();

      if (!username || !password) {
        alert('請輸入帳號與密碼');
        return;
      }

      const user = users.find(
        (u) => u.username === username && u.password === password && u.role === role
      );

      if (!user) {
        alert('帳號或密碼錯誤，或角色不符');
        return;
      }

      $('#loginContainer').hide();
      $('#dashboard').show();

      $('#loggedInUser').text(user.username);
      $('#userId').text(user.userId);
      $('#userRole').text(role === 'admin' ? '管理員' : '一般使用者');

      initMenu();
      loadContent('歡迎使用工業管理系統', '<p>請從左側選單選擇功能</p>');
    };

    window.logout = function () {
      if (confirm('確定要登出嗎？')) {
        $('#dashboard').hide();
        $('#loginContainer').show();
        $('#username').val('');
        $('#password').val('');
        $('#role').val('user');
        $('#menuAccordion').empty();
        $('#contentArea').html('<h2 class="ui header">歡迎使用工業管理系統</h2><p>請從左側選單選擇功能</p>');
      }
    };

    function initMenu() {
      const $accordion = $('#menuAccordion');
      $accordion.empty();

      menuData.forEach((item) => {
        const title = $(`
          <div class="title">
            <i class="dropdown icon"></i>
            ${item.title}
          </div>
        `);

        const content = $('<div class="content"><div class="ui list"></div></div>');
        const $list = content.find('.list');

        item.children.forEach((child) => {
          const $childItem = $(`<a class="item" href="#" data-key="${child.key}">${child.title}</a>`);
          $childItem.on('click', function (e) {
            e.preventDefault();
            loadContent(child.title, `<p>這是「${child.title}」功能的內容範例。</p>`);
            $('#menuAccordion .list .item').removeClass('active');
            $(this).addClass('active');
          });
          $list.append($childItem);
        });

        $accordion.append(title).append(content);
      });

      $accordion.accordion({
        exclusive: false,
        animateChildren: false,
        duration: 300
      });
    }

    function loadContent(title, html) {
      $('#contentArea').html(`
        <h2 class="ui header">${title}</h2>
        ${html}
      `);
    }
  });   